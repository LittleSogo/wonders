package coral.widget.utils.json;

/**
 * {@link Integer}转换器
 * 
 * @author 龚云
 * 
 */
public class IntegerConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Integer}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
			if (jsonValue instanceof Integer) {
				return jsonValue;
			} else {
				if (jsonValue instanceof String) {
					try {
						return Integer.parseInt((String) jsonValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				if (jsonValue instanceof Long) {
					Long tmpO = (Long) jsonValue;
					if (tmpO >= Integer.MIN_VALUE && tmpO <= Integer.MAX_VALUE)
						return tmpO.intValue();
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
