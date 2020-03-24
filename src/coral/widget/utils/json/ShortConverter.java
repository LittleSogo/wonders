package coral.widget.utils.json;

/**
 * {@link Short}转换器
 * 
 * @author 龚云
 * 
 */
public class ShortConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Short}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Short.class.equals(fieldType) || short.class.equals(fieldType)) {
			if (jsonValue instanceof String) {
				try {
					return Short.parseShort((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if (jsonValue instanceof Integer) {
				Integer tmpO = (Integer) jsonValue;
				if (tmpO >= Short.MIN_VALUE && tmpO <= Short.MAX_VALUE)
					return tmpO.byteValue();
			} else if (jsonValue instanceof Long) {
				Long tmpO = (Long) jsonValue;
				if (tmpO >= Short.MIN_VALUE && tmpO <= Short.MAX_VALUE)
					return tmpO.byteValue();
			}
		}
		return next(jsonValue, fieldType);
	}

}
