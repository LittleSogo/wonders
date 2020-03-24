package coral.widget.data;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 转换JSON时特殊处理的类类型
 * 
 * @author 龚云
 * 
 */
public enum MetaDataDataTypeEnum {

	Number(Number.class),

	String(String.class),

	Char(Character.class),

	Boolean(Boolean.class),

	Date(Date.class),

	Array(Collection.class),

	Map(Map.class),

	DataSet(DataSet.class),

	RowSet(RowSet.class);

	/**
	 * 获取klass的类型名称，若本枚举中有的特殊类型将使用枚举的toString方法作为类型名，否则调用klass.getName()作为类型名称
	 * 
	 * @param klass
	 *            待处理的klass
	 * @return klass的类型名称
	 */
	public static String getDataTypeStr(Class<?> klass) {
		String type = null;
		if (klass.isArray()) {
			type = MetaDataDataTypeEnum.Array.toString();
		} else {
			for (MetaDataDataTypeEnum dataEnum : values()) {
				if (dataEnum.getKlass().isAssignableFrom(klass)) {
					type = dataEnum.toString();
					break;
				}
			}
			if (type == null)
				type = klass.getName();
		}
		return type;
	}

	/**
	 * 若klass是否是枚举中的类型的子类，返回枚举对应的类类型，否则返回null
	 * 
	 * @param klass
	 *            待处理的klass
	 * @return 枚举中的类类型，没有则返回null
	 */
	public static Class<?> getDataType(Class<?> klass) {
		if (klass.isArray()) {
			return MetaDataDataTypeEnum.Array.getKlass();
		} else {
			for (MetaDataDataTypeEnum dataEnum : values()) {
				if (dataEnum.getKlass().isAssignableFrom(klass)) {
					return dataEnum.getKlass();
				}
			}
			return null;
		}
	}

	private Class<?> klass;

	private MetaDataDataTypeEnum(Class<?> klass) {
		this.klass = klass;
	}

	/**
	 * 获取枚举对应的类类型
	 * 
	 * @return
	 */
	public Class<?> getKlass() {
		return klass;
	};

	/**
	 * 枚举类类型对应的String值
	 */
	@Override
	public String toString() {
		return this.name();
	}

}
