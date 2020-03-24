package coral.widget.utils.json;

/**
 * {@link Number}转换器
 * 
 * @author 龚云
 * 
 */
public class NumberConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Number}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Number.class.equals(fieldType)) {
			if (jsonValue instanceof Integer || jsonValue instanceof Long
					|| jsonValue instanceof Double) {
				return jsonValue;
			} else if (jsonValue instanceof String) {
				try {
					return Byte.parseByte((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					return Short.parseShort((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					return Integer.parseInt((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					return Long.parseLong((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					return Float.parseFloat((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					return Double.parseDouble((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
