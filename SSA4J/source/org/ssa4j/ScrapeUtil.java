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
	
	private static Object convert(Class<?> targetClass, ScrapeDataRecordField field, String source) throws Exception {
		Object value = null;
		//
		// Convert to Calendar?
		//
		if (instanceOf(targetClass, Calendar.class) ) {
			Calendar cal = new GregorianCalendar();
			SimpleDateFormat format;
			if (field.pattern().length() > 0)
				format = new SimpleDateFormat(field.pattern());
			else
				format = new SimpleDateFormat();
			cal.setTime(format.parse(source));
			value = cal;
		} 
		//
		// Convert to String?
		//
		else if (instanceOf(targetClass, String.class)) {
			if (field.pattern().length() > 0)
				value = String.format(field.pattern(), source);
			else
				value = source.toString();
		} 
		//
		// Convert to Custom?
		//
		else {
			Method m = targetClass.getMethod("valueOf", String.class);
			if (m != null) {
				value = m.invoke(targetClass, source);
			} else {
				Constructor<?> constructor = targetClass.getConstructor(new Class[]{String.class});
				value = constructor.newInstance(source);
				if (constructor == null) {
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
						if (value != null) {
							m.invoke(obj, convert(m.getParameterTypes()[0], field, value));
						}
					} catch (Exception e) {
						throw new Exception("Problem with method: " + m.getName(), e);
					}
				}
			}
			
			for (Field f : c.getFields()) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(ScrapeDataRecordField.class)) {
					try {
						ScrapeDataRecordField field = (ScrapeDataRecordField) f.getAnnotation(ScrapeDataRecordField.class);
						String key = field.name();
						String value = (String) rec.get(key);
						if (value != null) {
							f.set(obj, convert(f.getType(), field, value));
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

	private static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		return true;
	}

}
