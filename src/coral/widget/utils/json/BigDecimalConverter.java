package coral.widget.utils.json;

import java.math.BigDecimal;

/**
 * {@link BigDecimal}转换器
 * 
 * @author 龚云
 * 
 */
public class BigDecimalConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link BigDecimal}，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (BigDecimal.class.equals(fieldType)) {
			if (jsonValue instanceof String) {
				try {
					return new BigDecimal((String) jsonValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if (jsonValue instanceof Integer) {
				return new BigDecimal(((Integer) jsonValue).toString());
			} else if (jsonValue instanceof Long) {
				return new BigDecimal(((Long) jsonValue).toString());
			} else if (jsonValue instanceof Double) {
				return new BigDecimal(((Double) jsonValue).toString());
			}
		}
		return next(jsonValue, fieldType);
	}

}
