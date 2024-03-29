package org.ssa4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;

/**
 * Utility class used to convert a {@link DataSet} into an class annotated 
 * with the {@link ScrapeDataRecord} annotations.  Also provides utilities 
 * for converting objects back into DataSets.
 * 
 * @author Rodney Aiglstorfer
 *
 */
public class ScrapeUtil {
	
	private static boolean instanceOf(Class<?> c1, Class<?> c2) {
		return c1 == c2;
	}
	
	public static Object convert(Class<?> targetClass, String sformat, String source) throws Exception {
		Object value = null;
		if (source == null || source.trim().length() == 0)
			return null;
		//
		// Convert to Calendar?
		//
		if (instanceOf(targetClass, Calendar.class) ) {
			Calendar cal = new GregorianCalendar();
			SimpleDateFormat format;
			if (sformat.length() > 0)
				format = new SimpleDateFormat(sformat);
			else
				format = new SimpleDateFormat();
			cal.setTime(format.parse(source));
			value = cal;
		} 
		//
		// Convert to String?
		//
		else if (instanceOf(targetClass, String.class)) {
			if (sformat.length() > 0)
				value = String.format(sformat, source);
			else
				value = source.toString();
		} 
		//
		// Convert to Boolean?
		//
		else if (instanceOf(targetClass, Boolean.class)) {
			if (source.equalsIgnoreCase("true") || source.equalsIgnoreCase("yes"))
				value = Boolean.TRUE;
			else
				value = Boolean.FALSE;
		} 
		//
		// Convert to Custom?
		//
		else {
			try {
				Method m = targetClass.getMethod("valueOf", String.class);
				value = m.invoke(targetClass, source);
			} catch (NoSuchMethodException nsme) {	
				try {
					Constructor<?> constructor = targetClass.getConstructor(new Class[]{String.class});
					value = constructor.newInstance(source);
				} catch (Exception e) {
					String msg = "@ScrapeDataRecord annotation reqiures that %s " +
					"be one of the following:\n" +
					"\t1. java.lang.String\n" +
					"\t2. java.util.Calendar\n" +
					"\t3. A class with a static method 'valueOf(String)'\n" +
					"\t4. A class with a constructor that takes a single String parameter\n";
					throw new ScrapeException(String.format(msg, targetClass));
				}
			}
		}
		return value;
	}

	public static <T> T convertDataRecordToObject(Class<T> c, DataRecord rec) throws Exception {
		if (c.isAnnotationPresent(org.ssa4j.ScrapeDataRecord.class)) {
			T obj = c.newInstance();
			
			for (Method m : c.getMethods()) {
				if (m.isAnnotationPresent(ScrapeDataRecordField.class) && isSetter(m)) {
					try {
						ScrapeDataRecordField field = (ScrapeDataRecordField) m.getAnnotation(ScrapeDataRecordField.class);
						String key = field.name();
						String value = (String) rec.get(key);
						if (value != null && value.length() > 0) {
							m.invoke(obj, convert(m.getParameterTypes()[0], field.format(), value.trim()));
						}
					} catch (Exception e) {
						throw new Exception("Problem with method: " + m.getName(), e);
					}
				}
			}
			
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeDataRecordField.class)) {
					try {
						ScrapeDataRecordField field = (ScrapeDataRecordField) f.getAnnotation(ScrapeDataRecordField.class);
						String key = field.name();
						String value = (String) rec.get(key);
						if (value != null && value.length() > 0) {
							f.set(obj, convert(f.getType(), field.format(), value.trim()));
						}
					} catch (Exception e) {
						throw new Exception("Problem with field: " + f.getName(), e);
					}
				}
			}
			
			return obj;
		}
		return null;
	}

	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		if (method.getReturnType() != void.class)
			return false;
		return true;
	}
	
	public static boolean isGetter(Method method) {
		String name = method.getName();
		if (!(name.startsWith("get") || name.startsWith("is") || name.startsWith("has")))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (method.getReturnType() == void.class)
			return false;
		return true;
	}

}
