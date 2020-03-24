package coral.widget.utils.json;

/**
 * {@link CharacterConverter}转换器
 * 
 * @author 龚云
 * 
 */
public class CharacterConverter extends JsonToFieldConverter {

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link CharacterConverter}
	 * ，否则交给下一个转换器
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Character.class.equals(fieldType) || char.class.equals(fieldType)) {
			if (jsonValue instanceof String) {
				String tmpO = (String) jsonValue;
				if (tmpO.length() == 1) {
					return tmpO.charAt(0);
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
