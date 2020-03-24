package coral.widget.utils.json;

/**
 * {@link Boolean}转换器
 * 
 * @author 龚云
 * 
 */
public class BooleanConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Boolean}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
			if (jsonValue instanceof Boolean) {
				return jsonValue;
			} else {
				if (jsonValue instanceof String) {
					try {
						return Boolean.parseBoolean((String) jsonValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
