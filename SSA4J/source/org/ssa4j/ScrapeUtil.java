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

	@SuppressWarnings("unchecked")
	public static <T> T convertDataRecordToObject(Class<T> c, DataRecord rec) throws Exception {
		if (c.isAnnotationPresent(org.ssa4j.ScrapeDataRecord.class)) {
			T obj = c.newInstance();
			for (Method m : c.getMethods()) {
				if (m.isAnnotationPresent(ScrapeDataRecordField.class)) {
					try {
						ScrapeDataRecordField field = (ScrapeDataRecordField) m.getAnnotation(ScrapeDataRecordField.class);
						String key = field.name();
						Object value = rec.get(key);
						if (value != null) {
							
							switch (field.type()) {
							case DATE:
								Calendar cal = new GregorianCalendar();
								cal.setTime(new SimpleDateFormat(field.pattern()).parse(value.toString()));
								value = cal;
								break;
							case CUSTOM :
								if (isSetter(m)) {
									try {
										Class paramClass = m.getParameterTypes()[0];
										Constructor constructor = paramClass.getConstructor(new Class[]{String.class});
										if (constructor == null) {
											String msg = "@ScrapeDataRecord annotation reqiures that %s " +
												"have a contructor that takes a single string as a parameter";
											throw new ScrapeException(String.format(msg, paramClass));
										}
										value = constructor.newInstance(value);
									} catch (Exception e) {
										
									}
								} else {
									throw new ScrapeException("@ScrapeDataRecord annotation can only be used with a setter method.");
								}
								break;
							case NUMBER :
								Integer num = Integer.parseInt(value.toString());
								value = num;
								break;
							}
							m.invoke(obj, value);
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
						Object value = rec.get(key);
						if (value != null) {
							
							switch (field.type()) {
							case DATE:
								Calendar cal = new GregorianCalendar();
								cal.setTime(new SimpleDateFormat(field.pattern()).parse(value.toString()));
								value = cal;
								break;
							case CUSTOM :
								Class paramClass = f.getType();
								Constructor constructor = paramClass.getConstructor(new Class[]{String.class});
								if (constructor == null) {
									String msg = "@ScrapeDataRecord annotation reqiures that %s " +
										"have a contructor that takes a single string as a parameter";
									throw new ScrapeException(String.format(msg, paramClass));
								}
								value = constructor.newInstance(value);
								break;
							case NUMBER :
								Integer num = Integer.parseInt(value.toString());
								value = num;
								break;
							}
							f.set(obj, value);
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
