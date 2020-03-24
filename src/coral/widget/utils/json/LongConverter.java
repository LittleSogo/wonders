package coral.widget.utils.json;

/**
 * {@link Long}转换器
 * 
 * @author 龚云
 * 
 */
public class LongConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Long}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
			if (jsonValue instanceof Long) {
				return jsonValue;
			} else {
				if (jsonValue instanceof String) {
					try {
						return Long.parseLong((String) jsonValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (jsonValue instanceof Integer) {
					return ((Integer) jsonValue).longValue();
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
