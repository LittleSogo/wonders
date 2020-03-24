package coral.widget.utils;

import coral.widget.data.MetaDataDataTypeEnum;

/**
 * 元数据类型帮助类
 * 
 * @author 龚云
 * 
 */
public class MetaDataTypeUtils {

	/**
	 * 获取klass的类型字面值，具体逻辑请看{@link MetaDataDataTypeEnum}.getDataTypeStr方法
	 * 
	 * @param klass
	 *            需要获取字面值的类
	 * @return
	 */
	public static String getDataType(Class<?> klass) {
		return new MetaDataTypeUtils()._getDataType(klass);
	}

	@Deprecated
	public static String getFromAllType(Class<?> klass) {
		return new MetaDataTypeUtils()._getFromAllType(klass);
	}

	/**
	 * 获取klass的类型字面值，具体逻辑请看{@link MetaDataDataTypeEnum}.getDataTypeStr方法
	 * 
	 * @param klass
	 *            需要获取字面值的类
	 * @return
	 */
	protected String _getDataType(Class<?> klass) {
		return MetaDataDataTypeEnum.getDataTypeStr(klass);
	}

	@Deprecated
	protected String _getFromAllType(Class<?> klass) {
		Class<?> dataType = MetaDataDataTypeEnum.getDataType(klass);
		if (dataType != null) {
			return _getDataType(klass);
		}
		return klass.getName();
	}
}
