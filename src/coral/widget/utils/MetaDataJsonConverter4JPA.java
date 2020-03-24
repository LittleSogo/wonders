/**
 * Project: GM
 * Source file: MetaDataJsonConverter4JPA.java
 * Create At 2012-11-26 上午10:36:33
 * Create By 龚云
 */
package coral.widget.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import javax.persistence.Column;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import wfc.service.log.Log;

/**
 * @author 龚云
 * 
 */
public class MetaDataJsonConverter4JPA extends MetaDataJsonConverter {

	protected MetaDataJsonConverter4JPA() {
	}

	public static JSONObject convertToJson(Object o) {
		MetaDataJsonConverter4JPA m = new MetaDataJsonConverter4JPA();
		JSONObject jsonObject = new JSONObject();
		String name = o.getClass().getSimpleName();
		m.convertObject(o, name, jsonObject, null);
		try {
			return jsonObject.getJSONObject(name);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void dealObject(Object o, String name, JSONObject root,
			String type) {
		boolean hasType = StringUtils.trimToEmpty(type).isEmpty();
		String _type = hasType ? MetaDataTypeUtils.getDataType(o.getClass())
				: type;
		JSONObject el = add(root, name, _type, null, null);
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(o);
		for (PropertyDescriptor pd : pds) {
			if (pd.getReadMethod() != null) {
				String fieldName = pd.getName();
				String fieldType = MetaDataTypeUtils.getDataType(pd
						.getPropertyType());
				Object value = null;
				try {
					value = getNestedProperty(o, pd);
					if (value != null)
						fieldType = MetaDataTypeUtils.getDataType(value
								.getClass());
					String columnName = fieldName;
					try {
						Field fd = o.getClass().getDeclaredField(fieldName);
						if (fd == null) {
							Class<?> superClass = o.getClass().getSuperclass();
							while (!superClass.equals(Object.class)) {
								fd = superClass.getDeclaredField(fieldName);
								if (fd != null)
									break;
								superClass = superClass.getSuperclass();
							}
						}
						Column colAnno = fd.getAnnotation(Column.class);
						columnName = colAnno.name();
					} catch (Exception e1) {
						Class<?> superClass = o.getClass().getSuperclass();
						Field fd = null;
						while (!superClass.equals(Object.class)) {
							try {
								fd = superClass.getDeclaredField(fieldName);
								if (fd != null) {
									Column colAnno = fd
											.getAnnotation(Column.class);
									columnName = colAnno.name();
									break;
								}
							} catch (Exception e) {
							}
							superClass = superClass.getSuperclass();
						}
						if (fd == null)
							Log.debug("No such field with name : " + fieldName);
					}
					convertObject(value, columnName, el, fieldType);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

}
