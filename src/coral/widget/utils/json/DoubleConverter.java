package coral.widget.utils.json;

/**
 * {@link Double}转换器
 * 
 * @author 龚云
 * 
 */
public class DoubleConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Double}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Double.class.equals(fieldType) || double.class.equals(fieldType)) {
			if (jsonValue instanceof Double) {
				return jsonValue;
			} else {
				if (jsonValue instanceof String) {
					try {
						return Double.parseDouble((String) jsonValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (jsonValue instanceof Long) {
					return ((Long) jsonValue).doubleValue();
				} else if (jsonValue instanceof Integer) {
					return ((Integer) jsonValue).doubleValue();
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
