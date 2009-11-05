package org.ssa4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssa4j.ScrapeSessionVariable.BindType;
import org.ssa4j.enterprise.EnterpriseScrapeSessionManager;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;

/**
 * Abstract base class for ScrapeSessionManagers does all the Scraper 
 * annotation binding and processing.
 * 
 * Subclass this class to create specialized ScrapeSessionManagers.
 * 
 * @see ProfessionalScrapeSessionManager
 * @see EnterpriseScrapeSessionManager
 * @see MockScrapeSessionManager
 * 
 * @author Rodney Aiglstorfer
 *
 */
public abstract class ScrapeSessionManager {
	
	public static ScrapeSessionManager createScrapeSessionManager() throws ScrapeException {
		String className = System.getProperty(ScrapeConstants.SSA4J_MANAGER_KEY);
		
		if (className == null) {
			
			String msg = "SSA4J is not properly configured.  " + 
					"You need to set a System property called '%s' to the fully qualified " +
					"classname of the ScrapeSessionManager implementation you want to process " +
					"the annotations.";
			throw new ScrapeException(String.format(msg, ScrapeConstants.SSA4J_MANAGER_KEY));
			
		}
		
		try {
			Class<?> managerClass = Class.forName(className);
			return (ScrapeSessionManager) managerClass.newInstance();
		} catch (Exception e) {
			throw new ScrapeException("Unable to create ScrapeSessionManager", e);
		}
	}
	
	protected static Logger log = LoggerFactory.getLogger(ScrapeSessionManager.class);
	
	/**
	 * Called for every ScrapeSessionVaraible annotation passed back from scrape.
	 * @param name
	 * @param value
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	protected abstract String getVariable(String name) throws ScrapeException;
	
	/**
	 * This class is called for every ScrapeSessionVaraible annotation.
	 * @param name
	 * @param value
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	protected abstract void setVariable(String name, String value) throws ScrapeException;
	
	/**
	 * Returns the DataSet for a given session variable name.
	 * @param name The session variable name
	 * @return the DataSet for the given name
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	protected abstract DataSet getDataSet(String name) throws ScrapeException;
	
	/**
	 * Once the variables have been parsed from the ScrapeSessionVaraible annotations
	 * this method is called.  Presumably here is a where you would kick-off the scrape session.
	 * @param source The object annotated with the ScrapeSession annotation
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	protected abstract void execute(Object source, Map<String,String> cookiejar) throws ScrapeException;
	
	/**
	 * Regardless of the manner in which the scrape has terminated this method
	 * is invoked to allow for clean shutdown and cleanup.
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	public abstract void close() throws ScrapeException;
	

	/**
	 * Kicks off the scrape using the given annotated session object.
	 * @param session A class annotated with {@link ScrapeSession}
	 * @throws ScrapeException thrown if there are any problems with the scrape
	 */
	public void scrape(Object session) throws ScrapeException {
		this.scrape(session, null, null);
	}
	
	/**
	 * Kicks off the scrape using the given annotated session object
	 * with optional support for a listener and cookieJar
	 * @param session A class annotated with {@link ScrapeSession}
	 * @param listener A class that implements {@link ScrapeSessionListener} (can be null)
	 * @param cookiejar A cookieJar within which to read/write {@link ScrapeSessionCookies}
	 * @throws ScrapeException
	 */
	public void scrape(Object session, ScrapeSessionListener listener, Map<String,String> cookiejar) throws ScrapeException {
		if (session.getClass().isAnnotationPresent(ScrapeSession.class) == false) 
			throw new ScrapeException("Object is not correctly annotated.  Expecting @ScrapeSession.");
		
		String sessionId = getSessionId(session);
		long stime = System.currentTimeMillis();
		log.debug(String.format(">> Starting Scrape Session '%s'", sessionId));
		if (sessionId != null) {
			try {
				if (listener != null)
					listener.onScrapeReady();
				// setup any variables required by the session
				setup(session, cookiejar);
	
				// call the concrete impls execute method
				long time = System.currentTimeMillis();
	            execute(session, cookiejar);
	            time = System.currentTimeMillis() - time;
	            log.info(String.format("<<< Scrape Session '%s' completed [%dms]", sessionId, time));
	            
	            // process the DataSet's from the sessions
	            time = System.currentTimeMillis();
	            process(session, cookiejar);
	            time = System.currentTimeMillis() - time;
	            log.info(String.format("<<< Scrape Session '%s' response processed [%dms]", sessionId, time));
	            
	            if (listener != null)
					listener.onScrapeComplete();
	            
			} catch (ScrapeSessionTimeoutException e) {
				if (listener != null)
					listener.onScrapeTimeout();
				else
					throw e;
			} catch (ScrapeSessionException e) {
				if (listener != null)
					listener.onScrapeError(e);
				else 
					throw e;
			} catch (ScrapeException e) {
				// just pass along
				throw e;
			} catch (Exception e) {
				// wrap in a ScrapeException and pass along
				String msg = String.format("Unexpected problem with session '%s'", sessionId);
				throw new ScrapeException(msg, e);
			} finally {
				// Very important! Be sure to disconnect from the server.
	            close();
			}
		} else {
			throw new ScrapeException("Adapter is not correctly annotated");
		}
		stime = System.currentTimeMillis() - stime;
		log.debug(String.format("<< Finished Scrape Session '%s' [%dms]", sessionId, stime));
			
	}
	
	/**
	 * Returns the session id for an annotated {@link ScrapeSession} class.
	 * @param source
	 * @return
	 */
	public String getSessionId(Object source) {
		if (source != null && source.getClass().isAnnotationPresent(ScrapeSession.class)) 
			return source.getClass().getAnnotation(ScrapeSession.class).name();
		return null;
	}
	
	/**
	 * Parses all {@link ScrapeSessionVariable} annotated fields and calls the setVariable() method
	 * @param source The {@link ScrapeSession} annotated object.
	 * @throws ScrapeException
	 */
	private void setup(Object source, Map<String,String> cookiejar) throws ScrapeException {
		setup(source.getClass(), source, cookiejar);
	}
	
	@SuppressWarnings({ "unchecked" })
	private void setup(Class c, Object source, Map<String,String> cookiejar) throws ScrapeException {
		try {
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeSessionVariable.class)) {
					ScrapeSessionVariable meta = f.getAnnotation(ScrapeSessionVariable.class);
					for(BindType bindtype : meta.bindtype()) {
						if (bindtype == BindType.Write) {
							String varname = meta.name();
							Object value = f.get(source);
						
							if (value != null) {
								setVariable(varname, value.toString());			
							}
						}
					}
				}
			}
			for (Method m : c.getDeclaredMethods()) {
				m.setAccessible(true);
				if (m.isAnnotationPresent(ScrapeSessionVariable.class) && ScrapeUtil.isGetter(m)) {
					ScrapeSessionVariable meta = m.getAnnotation(ScrapeSessionVariable.class);
					String varname = meta.name();
					Object value = m.invoke(source);
					if (value != null) {
						setVariable(varname, value.toString());
					}
				}
			}
			Class superclass = c.getSuperclass();
			if (superclass != null && superclass.isAnnotationPresent(ScrapeSession.class)) {
				setup(superclass, source, cookiejar);
			}
			if (cookiejar != null) {
				
				for (Entry<String, String> entry : cookiejar.entrySet()) {
					setVariable(entry.getKey(), entry.getValue());
					log.info(String.format("Reflecting Cookie[name:%s value:%s]", entry.getKey(), entry.getValue()));
				}
			}
		} catch (Exception e) {
			throw new ScrapeException(e);
		}
	}
	
	/**
	 * Processes all Scraper annotation starting from ScrapeSession and works it way all the way to
	 * individual {@link ScrapeDataSet}, {@link ScrapeDataRecord} and {@link ScrapeDataRecordField} annotations.
	 * @param source
	 * @throws ScrapeException
	 */
	@SuppressWarnings({ "unchecked" })
	private void process(Object source, Map<String,String> cookiejar) throws ScrapeException {
		Class c = source.getClass();
		
		log.debug("Processing Variables and DataSets ...");
		processVariablesAndDataSets(c, source, cookiejar);
		
		log.debug("Processing Cookies ...");
		processCookies(c, source, cookiejar);
		
		log.debug("Processing Errors ...");
		ScrapeSessionException exp = processErrors(c, source, null);
		if (exp != null) {
			log.info("ScrapeSessionErrors Found");
			throw exp;
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private void processCookies(Class c, Object source, Map<String,String> cookiejar) throws ScrapeException {
		try {
			log.info(String.format("cookiejar: %b class: %b", cookiejar!=null, c.isAnnotationPresent(ScrapeSessionCookies.class)));
			//
			// Process any "cookies"
			//
			if (cookiejar != null) {
				if (c.isAnnotationPresent(ScrapeSessionCookies.class)) {
					ScrapeSessionCookies meta = 
						(ScrapeSessionCookies) c.getAnnotation(ScrapeSessionCookies.class);
					for (ScrapeSessionCookie cookieMeta : meta.value()) {
						log.info("Reading Cookies ...");
						String name = cookieMeta.value();
						String value = this.getVariable(name);
						log.info(String.format("Set-Cookie[name:%s value:%s]", name, value));
						cookiejar.put(name, value);
						log.info("Cookies Added to CookieJar");
					}
				} else {
					Class superclass = c.getSuperclass();
					if (superclass != null && superclass.isAnnotationPresent(ScrapeSessionCookies.class)) {
						processCookies(superclass, source, cookiejar);
					}
				}
			}
		} catch(ScrapeException e) {
			throw e;
		} catch (Exception e) {
			throw new ScrapeException(e);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private void processVariablesAndDataSets(Class c, Object source, Map<String,String> cookiejar) throws ScrapeException {
		
		try {
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeDataSet.class)) {
					log.debug(String.format("@ScrapeDataSet (field:%s)", f.getName()));
					ScrapeDataSet meta = f.getAnnotation(ScrapeDataSet.class);
					String id = meta.value();
					DataSet dataSet = getDataSet(id);
					if (dataSet != null) {
						int numRecs = dataSet.getNumDataRecords();
						Class arrayType = f.getType();
						if (arrayType.isArray()) {
							Class arrayComponentType = arrayType.getComponentType();
							Object arrayObj = Array.newInstance(arrayComponentType, numRecs);
							f.set(source, arrayObj);
							for (int ndx = 0; ndx < numRecs; ndx++) {
				            	DataRecord rec = dataSet.getDataRecord(ndx);
				            	Object obj = ScrapeUtil.convertDataRecordToObject(arrayComponentType, rec);
				            	Array.set(arrayObj, ndx, obj);
				            }
						} else {
							throw new ScrapeException("@DataSet annotation must be assigned to an Array type.");
						}
						
					}
				} else if (f.isAnnotationPresent(ScrapeSessionVariable.class)) {
					log.debug(String.format("@ScrapeSessionVariable (field:%s)", f.getName()));
					ScrapeSessionVariable meta = f.getAnnotation(ScrapeSessionVariable.class);
					for(BindType bindtype : meta.bindtype()) {
						if (bindtype == BindType.Read) {
							String varname = meta.name();
							String value = getVariable(varname);
							if (value != null && value.length() > 0) {
								Object converted = ScrapeUtil.convert(f.getType(), meta.format(), value.trim());
								if (converted != null) {
									f.set(source, converted);
									log.debug(value + " -> " + converted.getClass());
								}
							}
						}
					}
				} else {
					log.debug(String.format("No Annotations (field:%s)", f.getName()));
				}
			}
			
			for (Method m : c.getDeclaredMethods()) {
				m.setAccessible(true);
				if (m.isAnnotationPresent(ScrapeSessionVariable.class) && ScrapeUtil.isSetter(m)) {
					ScrapeSessionVariable meta = m.getAnnotation(ScrapeSessionVariable.class);
					String varname = meta.name();
					String value = getVariable(varname);
					if (value != null && value.length() > 0) {
						Object converted = ScrapeUtil.convert(m.getParameterTypes()[0], meta.format(), value.trim());
						if (converted != null) {
							m.invoke(source, converted);
							log.debug(value + " -> " + converted.getClass());
						}
					}
				}
			}
			
			
		} catch (ScrapeException e) {
			throw e;
		} catch (Exception e) {
			throw new ScrapeException(e);
		}
		Class superclass = c.getSuperclass();
		if (superclass != null && superclass.isAnnotationPresent(ScrapeSession.class)) {
			processVariablesAndDataSets(superclass, source, cookiejar);
		}
	}

	@SuppressWarnings("unchecked")
	private ScrapeSessionException processErrors(Class c, Object source, ScrapeSessionException exception) throws ScrapeException {
		ScrapeSessionErrors errors = (ScrapeSessionErrors)c.getAnnotation(ScrapeSessionErrors.class);
		if (errors != null) {
			ScrapeSessionError[] errorList = errors.value();
			try {
				if (errorList.length > 0) {
					for (ScrapeSessionError emeta : errorList) {
						String varname = emeta.name();
						String rawText = (String) getVariable(varname);
						if (rawText != null && rawText.trim().length() > 0) {
							log.debug(String.format("@ScrapeSessionError (field:%s) found in ScrapeSession", varname));
							if (exception == null) {
								exception = new ScrapeSessionException(emeta.code(), rawText.toString());
							} else {
								exception.addError(emeta.code(), rawText.toString());
							}
						}
					}
				}
				
			} catch (ScrapeException e) {
				throw e;
			} catch (Exception e) {
				throw new ScrapeException(e);
			}
		}
		Class superclass = c.getSuperclass();
		if (superclass != null && superclass.isAnnotationPresent(ScrapeSession.class)) {
			exception = processErrors(superclass, source, exception);
		}
		return exception;
	}

}
