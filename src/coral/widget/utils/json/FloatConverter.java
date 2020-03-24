package coral.widget.utils.json;

/**
 * {@link Float}转换器
 * 
 * @author 龚云
 * 
 */
public class FloatConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Float}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
			if (jsonValue instanceof Double) {
				Double tmpO = (Double) jsonValue;
				if (tmpO >= Float.MIN_VALUE && tmpO <= Float.MAX_VALUE)
					return tmpO.floatValue();
			} else {
				if (jsonValue instanceof String) {
					try {
						return Float.parseFloat((String) jsonValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (jsonValue instanceof Long) {
					return ((Long) jsonValue).floatValue();
				} else if (jsonValue instanceof Integer) {
					return ((Integer) jsonValue).floatValue();
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
