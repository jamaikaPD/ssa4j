package org.ssa4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssa4j.mock.MockScrapeSessionManager;

import com.screenscraper.common.DataRecord;

/**
 * Abstract base class for ScrapeSessionManagers does all the Scraper annotation binding and processing.
 * 
 * Subclass this class to create specialized ScrapeSessionManagers.
 * 
 * @see ProfessionalScrapeSessionManager
 * @see MockScrapeSessionManager
 * 
 * @author Rodney Aiglstorfer
 *
 */
public abstract class ScrapeSessionManager {
	
	public static ScrapeSessionManager createScrapeSessionManager() throws ScrapeException {
		String className = System.getProperty(ScrapeConstants.SS_MANAGER_KEY, ProfessionalScrapeSessionManager.class.getName());
		
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
	 * @throws ScrapeException
	 */
	protected abstract String getVariable(String name) throws ScrapeException;
	
	/**
	 * This class is called for every ScrapeSessionVaraible annotation.
	 * @param name
	 * @param value
	 * @throws ScrapeException
	 */
	protected abstract void setVariable(String name, String value) throws ScrapeException;
	
	/**
	 * Returns a DataRecord from the specified DataSet
	 * @param id The DataSet Identifier
	 * @param ndx The DataRecord index
	 * @return a DataRecord from the specified DataSet
	 * @throws ScrapeException
	 */
	protected abstract DataRecord getDataRecordFromDataSet(String id, int ndx) throws ScrapeException;
	
	/**
	 * Returns the total number of records in teh specified DataSet
	 * @param id The DataSet Identifier
	 * @return the total number of records in teh specified DataSet
	 * @throws ScrapeException
	 */
	protected abstract int getNumDataRecordsInDataSet(String id) throws ScrapeException;
	
	/**
	 * Once the variables have been parsed from the ScrapeSessionVaraible annotations
	 * this method is called.  Presumably here is a where you would kick-off the scrape session.
	 * @param source The object annotated with the ScrapeSession annotation
	 * @throws ScrapeException
	 */
	protected abstract void execute(Object source, CookieJar cookiejar) throws ScrapeException;
	
	/**
	 * Regardless of the manner in which the scrape has terminated this method
	 * is invoked to allow for clean shutdown and cleanup.
	 * @throws ScrapeException
	 */
	protected abstract void close() throws ScrapeException;
	

	public void scrape(Object source) throws ScrapeException {
		this.scrape(source, null, null);
	}
	
	public void scrape(Object source, ScrapeSessionListener listener, CookieJar cookiejar) throws ScrapeException {
		if (source.getClass().isAnnotationPresent(ScrapeSession.class) == false) 
			throw new ScrapeException("Object is not correctly annotated.  Expecting @ScrapeSession.");
		
		String sessionId = getSessionId(source);
		long stime = System.currentTimeMillis();
		log.debug(String.format(">> Starting Scrape Session '%s'", sessionId));
		if (sessionId != null) {
			try {
				if (listener != null)
					listener.onScrapeReady();
				// setup any variables required by the session
				setup(source, cookiejar);
	
				// call the concrete impls execute method
				long time = System.currentTimeMillis();
	            execute(source, cookiejar);
	            time = System.currentTimeMillis() - time;
	            log.info(String.format("<<< Scrape Session '%s' completed [%dms]", sessionId, time));
	            
	            // process the DataSet's from the sessions
	            time = System.currentTimeMillis();
	            process(source, cookiejar);
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
	
	public String getSessionId(Object source) {
		if (source != null && source.getClass().isAnnotationPresent(ScrapeSession.class)) 
			return source.getClass().getAnnotation(ScrapeSession.class).sessionId();
		return null;
	}
	
	/**
	 * Parses all {@link ScrapeSessionVariable} annotated fields and calls the setVariable() method
	 * @param source The {@link ScrapeSession} annotated object.
	 * @throws ScrapeException
	 */
	private void setup(Object source, CookieJar cookiejar) throws ScrapeException {
		setup(source.getClass(), source, cookiejar);
	}
	
	@SuppressWarnings({ "unchecked" })
	private void setup(Class c, Object source, CookieJar cookiejar) throws ScrapeException {
		try {
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeSessionVariable.class)) {
					ScrapeSessionVariable meta = f.getAnnotation(ScrapeSessionVariable.class);
					switch(meta.bindtype()) {
					case ReadWrite:
					case Write:
						String varname = meta.name();
						Object value = f.get(source);
						if (value != null)
							setVariable(varname, String.format(meta.format(), value));						
					}
				}
			}
			Class superclass = c.getSuperclass();
			if (superclass != null && superclass.isAnnotationPresent(ScrapeSession.class)) {
				setup(superclass, source, cookiejar);
			}
			if (cookiejar != null) {
				String[] names = cookiejar.getAttributeNames();
				for (String name : names) {
					String value = cookiejar.getAttribute(name);
					setVariable(name, value);
					log.info(String.format("Reflecting Cookie[name:%s value:%s]", name, value));
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
	private void process(Object source, CookieJar cookiejar) throws ScrapeException {
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
	private void processCookies(Class c, Object source, CookieJar cookiejar) throws ScrapeException {
		try {
			log.info(String.format("cookiejar: %b class: %b", cookiejar!=null, c.isAnnotationPresent(ScrapeSessionCookies.class)));
			//
			// Process any "cookies"
			//
			if (cookiejar != null) {
				if (c.isAnnotationPresent(ScrapeSessionCookies.class)) {
					ScrapeSessionCookies meta = 
						(ScrapeSessionCookies) c.getAnnotation(ScrapeSessionCookies.class);
					for (ScrapeSessionCookie cookieMeta : meta.cookies()) {
						log.info("Reading Cookies ...");
						String name = cookieMeta.varname();
						String value = this.getVariable(name);
						log.info(String.format("Set-Cookie[name:%s value:%s]", name, value));
						cookiejar.setAttribute(name, value);
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
	private void processVariablesAndDataSets(Class c, Object source, CookieJar cookiejar) throws ScrapeException {
		
		try {
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeDataSet.class)) {
					log.debug(String.format("@ScrapeDataSet (field:%s)", f.getName()));
					ScrapeDataSet meta = f.getAnnotation(ScrapeDataSet.class);
					String id = meta.identifier();
					int numRecs = getNumDataRecordsInDataSet(id);
					if (numRecs > 0) {
						Class arrayType = f.getType();
						if (arrayType.isArray()) {
							Class arrayComponentType = arrayType.getComponentType();
							Object arrayObj = Array.newInstance(arrayComponentType, numRecs);
							f.set(source, arrayObj);
							for (int ndx = 0; ndx < numRecs; ndx++) {
				            	DataRecord rec = getDataRecordFromDataSet(id, ndx);
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
					switch(meta.bindtype()) {
					case ReadWrite:
					case Read:
						String varname = meta.name();
						Object value = getVariable(varname);
						if (value != null) {
							f.set(source, value);
						}
					}
				} else {
					log.debug(String.format("No Annotations (field:%s)", f.getName()));
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
		ScrapeSession cmeta = (ScrapeSession) c.getAnnotation(ScrapeSession.class);
		ScrapeSessionError[] errors = cmeta.errors();
		try {
			if (errors.length > 0) {
				for (ScrapeSessionError emeta : errors) {
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
		Class superclass = c.getSuperclass();
		if (superclass != null && superclass.isAnnotationPresent(ScrapeSession.class)) {
			exception = processErrors(superclass, source, exception);
		}
		return exception;
	}

}
