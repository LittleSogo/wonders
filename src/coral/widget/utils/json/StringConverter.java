package coral.widget.utils.json;

/**
 * {@link String}转换器
 * 
 * @author 龚云
 * 
 */
public class StringConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link String}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (String.class.equals(fieldType))
			return jsonValue.toString();
		return next(jsonValue, fieldType);
	}

}
