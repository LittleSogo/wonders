package coral.widget.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import wfc.service.util.OrderedHashMap;
import wfc.service.util.OrderedHashSet;
import coral.widget.data.MetaDataDataTypeEnum;

/**
 * 
 * 对象转换器超类，子类可继承后实现相应的方法来完成转换，其中待转换对象的迭代逻辑有本类提供
 * 
 * @author 龚云
 * 
 * @param <T>用于给子类提供转换后结果的类型约束
 */
public abstract class MetaDataConvert<T> {

	/**
	 * 添加一个节点
	 * 
	 * @param from
	 *            父节点
	 * @param name
	 *            节点名称
	 * @param type
	 *            节点类型
	 * @param value
	 *            节点值
	 * @param originalObj
	 *            节点值的来源（即value的来源对象）
	 * @return 返回的对象具体子类决定
	 */
	protected abstract T add(T from, String name, String type, String value,
			Object originalObj);

	/**
	 * 转换对象
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void convertObject(Object o, String name, T root, String type) {
		if (o == null) {
			dealNull(name, root, type);
		} else if (o instanceof Collection<?>) {
			dealCollection(o, name, root, type);
		} else if (o instanceof Map<?, ?>) {
			dealMap(o, name, root, type);
		} else if (o.getClass().isArray()) {
			dealArray(o, name, root, type);
		} else if (o instanceof Number) {
			dealNumber(o, name, root, type);
		} else if (o instanceof Character) {
			dealCharacter(o, name, root, type);
		} else if (o instanceof Boolean) {
			dealBoolean(o, name, root, type);
		} else if (o instanceof Date) {
			dealDate(o, name, root, type);
		} else if (o instanceof Class<?>) {
			// TODO 不处理
		} else if (o instanceof String) {
			dealString(o, name, root, type);
		} else if (o.getClass().isEnum()) {
			dealEnum(o, name, root, type);
		} else {
			dealObject(o, name, root, type);
		}
	}

	/**
	 * 数组的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealArray(Object o, String name, T root, String type) {
		T el = add(root, name, null, null, null);
		for (int i = 0; i < Array.getLength(o); i++) {
			Object item = Array.get(o, i);
			convertObject(item, "item", el, null);
		}
	}

	/**
	 * {@link Boolean}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealBoolean(Object o, String name, T root, String type) {
		add(root, name, null, o.toString(), o);
	}

	/**
	 * {@link Character}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealCharacter(Object o, String name, T root, String type) {
		add(root, name, null, o.toString(), o);
	}

	/**
	 * {@link Collection}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealCollection(Object o, String name, T root, String type) {
		Collection<?> cO = (Collection<?>) o;
		if (o instanceof OrderedHashSet<?>) {
			OrderedHashSet<?> oS = (OrderedHashSet<?>) o;
			for (int i = 0; i < oS.size(); i++) {
				convertObject(oS.get(i), name, root, null);
			}
		} else {
			for (Object value : cO) {
				convertObject(value, name, root, null);
			}
		}
	}

	/**
	 * {@link Date}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealDate(Object o, String name, T root, String type) {
		add(root, name, null, String.valueOf(((Date) o).getTime()), o);
	}

	/**
	 * {@link Enum}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealEnum(Object o, String name, T root, String type) {
		add(root, name, null, ((Enum<?>) o).toString(), o);
	}

	/**
	 * {@link Map}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealMap(Object o, String name, T root, String type) {
		boolean hasType = StringUtils.trimToEmpty(type).isEmpty();
		String _type = hasType ? MetaDataDataTypeEnum.Map.toString() : type;
		if (o instanceof OrderedHashMap<?, ?>) {
			T el = add(root, name, _type, null, null);
			OrderedHashMap<?, ?> omO = (OrderedHashMap<?, ?>) o;
			T values = add(el, "values", null, null, null);
			for (int i = 0; i < omO.size(); i++) {
				String k = omO.getKey(i) == null ? "null" : omO.getKey(i)
						.toString();
				convertObject(omO.getValue(i), k, values, null);
			}
		} else {
			T el = add(root, name, _type, null, null);
			Map<?, ?> mO = (Map<?, ?>) o;
			T values = add(el, "values", null, null, null);
			for (Entry<?, ?> entry : mO.entrySet()) {
				Object key = entry.getKey();
				String k = key == null ? "null" : key.toString();
				convertObject(entry.getValue(), k, values, null);
			}
		}
	}

	/**
	 * Null的转换方式
	 * 
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealNull(String name, T root, String type) {
		add(root, name, null, null, null);
	}

	/**
	 * {@link Number}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealNumber(Object o, String name, T root, String type) {
		add(root, name, null, o.toString(), o);
	}

	/**
	 * {@link Object}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealObject(Object o, String name, T root, String type) {
		boolean hasType = StringUtils.trimToEmpty(type).isEmpty();
		String _type = hasType ? MetaDataTypeUtils.getDataType(o.getClass())
				: type;
		T el = add(root, name, _type, null, null);
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
					convertObject(value, fieldName, el, fieldType);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	protected Object getNestedProperty(Object o, PropertyDescriptor pd) {
		Object value = null;
		try {
			value = PropertyUtils.getNestedProperty(o, pd.getName());
		} catch (Exception e) {
			Method md = pd.getReadMethod();
			md.setAccessible(true);
			try {
				value = md.invoke(o, new Object[] {});
			} catch (Exception e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * {@link String}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	protected void dealString(Object o, String name, T root, String type) {
		add(root, name, null, (String) o, o);
	}
}
