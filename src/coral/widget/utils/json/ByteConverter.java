package coral.widget.utils.json;

/**
 * {@link Byte}转换器
 * 
 * @author 龚云
 * 
 */
public class ByteConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Byte}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Byte.class.equals(fieldType) || byte.class.equals(fieldType)) {
			if (jsonValue instanceof String) {
				try {
					return Byte.parseByte((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if (jsonValue instanceof Integer) {
				Integer tmpO = (Integer) jsonValue;
				if (tmpO >= Byte.MIN_VALUE && tmpO <= Byte.MAX_VALUE)
					return tmpO.byteValue();
			} else if (jsonValue instanceof Long) {
				Long tmpO = (Long) jsonValue;
				if (tmpO >= Byte.MIN_VALUE && tmpO <= Byte.MAX_VALUE)
					return tmpO.byteValue();
			}
		}
		return next(jsonValue, fieldType);
	}

}
