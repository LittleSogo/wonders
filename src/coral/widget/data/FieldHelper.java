package coral.widget.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.persistence.Column;
import javax.persistence.Id;

import wfc.facility.tool.autocode.PaginationArrayList;
import wfc.service.util.OrderedHashMap;

/**
 * JavaBean中field信息的描述对象
 * 
 * @author 龚云
 */
public class FieldHelper {

	private FieldHelper() {
	}

	/**
	 * 获取对应的{@link Column}
	 * 
	 * @return {@link Column}
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * 获取对应的{@link Field}
	 * 
	 * @return {@link Field}
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 获取所在的{@link Class}
	 * 
	 * @return {@link Class}
	 */
	public Class<?> getKlass() {
		return klass;
	}

	/**
	 * 获取对应的{@link Method}
	 * 
	 * @return {@link Method}
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 获取对应的{@link PropertyDescriptor}
	 * 
	 * @return {@link PropertyDescriptor}
	 */
	public PropertyDescriptor getPd() {
		return pd;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	/**
	 * 获取列名<br>
	 * 1.fieldName模式：使用{@link Field}的getName()方法获取值<br>
	 * 2.{@link Column}模式：使用{@link Column}的name()方法获取值；
	 * 
	 * @param mode 解析策略，参考 {@link Mode}
	 * @return 列名
	 */
	protected String getColumnName(Mode mode) {
		switch (mode) {
			case FIELD_NAME:
				return getField().getName();
			default:
				return getColumn().name();
		}
	}

	private Class<?> klass;

	private Column column;

	private Field field;

	private Method method;

	private PropertyDescriptor pd;

	private boolean pk;

	/**
	 * 获得klass对应的field描述对象集合OrderedHashMap<String, {@link Field}>， 转换数据集
	 * {@link PaginationArrayList}为数据集，采用默认的列解析模式，参见 DEFAULT_MODE
	 *
	 * @param klass 待解析的Class
	 * @return OrderedHashMap[String, FieldHelper]
	 */
	public static OrderedHashMap<String, FieldHelper> getFieldHelpers(Class<?> klass, Class<?> superKlass) {
		return getFieldHelpers(klass, superKlass, Mode.COLUMN_ANNOTATION);
	}

	/**
	 * 获得klass对应的field描述对象集合OrderedHashMap<String, {@link Field}>
	 * 
	 * @param klass 待解析的Class
	 * @param superKlass 反射解析终止于超类
	 * @param mode 解析策略，参考 {@link Mode}
	 * @return OrderedHashMap[String, FieldHelper]
	 */
	public static OrderedHashMap<String, FieldHelper> getFieldHelpers(Class<?> klass, Class<?> superKlass, Mode mode) {
		OrderedHashMap<String, FieldHelper> fhs = new OrderedHashMap<String, FieldHelper>();
		boolean hasPk = false;
		while (true) {
			hasPk |= getFieldHelpers(fhs, klass, mode);
			if (superKlass.equals(klass) || Object.class.equals(klass))
				break;
			klass = klass.getSuperclass();
		}
		if (!hasPk && mode == Mode.FIELD_NAME) {
			FieldHelper fh = new FieldHelper();
			fh.pk = true;
			fhs.put(GENERATE_PK_COL_NAME, fh);
		}
		return fhs;
	}

	/**
	 * 根据{@link Field}创建field描述对象
	 * 
	 * @param f {@link Field}
	 * @param klass f所在的类
	 * @param mode 解析策略，参考 {@link Mode}
	 * @return FieldHelper
	 */
	protected static FieldHelper create(Field f, Class<?> klass, Mode mode) {
		FieldHelper fh = null;
		try {
			switch (mode) {
				case FIELD_NAME: {
					String fieldName = getValidFieldName(f);
					PropertyDescriptor pd = new PropertyDescriptor(fieldName, klass);
					Method m = pd.getReadMethod();
					if (m != null) {
						m.setAccessible(true);
						fh = new FieldHelper();
						fh.pk = f.getAnnotation(Id.class) != null;
						fh.field = f;
						fh.klass = klass;
						fh.method = m;
						fh.pd = pd;
					} else {
						fh = null;
					}
					break;
				}
				default: {
					Column column = f.getAnnotation(Column.class);
					if (column != null) {
						String fieldName = getValidFieldName(f);
						PropertyDescriptor pd = new PropertyDescriptor(fieldName, klass);
						Method m = pd.getReadMethod();
						if (m != null) {
							m.setAccessible(true);
							fh = new FieldHelper();
							fh.pk = f.getAnnotation(Id.class) != null;
							fh.column = column;
							fh.field = f;
							fh.klass = klass;
							fh.method = m;
							fh.pd = pd;
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fh;
	}

	private static boolean getFieldHelpers(OrderedHashMap<String, FieldHelper> fhs, Class<?> klass, Mode mode) {
		Field[] fs = klass.getDeclaredFields();
		boolean hasPk = false;
		for (Field f : fs) {
			if (Modifier.isStatic(f.getModifiers()))
				continue;
			FieldHelper fh = FieldHelper.create(f, klass, mode);
			if (fh != null) {
				if (fh.pk)
					hasPk = true;
				fhs.put(fh.getColumnName(mode), fh);
			}
		}
		return hasPk;
	}

	private static String getValidFieldName(Field f) {
		String fieldName = f.getName();
		Class<?> type = f.getType();

		boolean isBoolean = Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type);
		boolean isIsPrefix = fieldName.startsWith("is") || fieldName.startsWith("Is");
		if(isBoolean && isIsPrefix){
			return fieldName.substring(2);
		}

		return fieldName;
	}

	public static final String GENERATE_PK_COL_NAME = "_auto_create_pk";

	/**
	 * 反射对象获取列名是使用的策略<br>
	 * 1.解析{@link Field}的fieldName模式；<br>
	 * 2.解析{@link Field}的{@link Column}模式；
	 */
	public static enum Mode {

		FIELD_NAME, COLUMN_ANNOTATION

	}

}
