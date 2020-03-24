package coral.widget.utils.json;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link Date}转换器
 * 
 * @author 龚云
 * 
 */
public class DateConverter extends JsonToFieldConverter {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DateConverter() {
	}

	public DateConverter(JsonToFieldConverter jsonToFieldConverter) {
		super(jsonToFieldConverter);
	}

	public DateConverter(JsonToFieldConverter jsonToFieldConverter,
			DateFormat dateFormat) {
		this(jsonToFieldConverter);
		this.dateFormat = dateFormat;
	}

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象，仅处理{@link Date}
	 * ，否则交给下一个转换器，若jsonValue值不是Long（毫秒）值，则使用dateFormat尝试反格式化字符串为日期
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (Date.class.isAssignableFrom(fieldType)) {
			if (jsonValue instanceof String) {
				String tmpO = (String) jsonValue;
				try {
					jsonValue = Long.parseLong(tmpO);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					if (this.dateFormat != null) {
						DateFormat dateFormat = (DateFormat) this.dateFormat
								.clone();
						try {
							jsonValue = dateFormat.parse(tmpO).getTime();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			if (jsonValue instanceof Long) {
				Long tmpO = (Long) jsonValue;
				if (Date.class.equals(fieldType)) {
					return new Date(tmpO);
				} else if (Timestamp.class.equals(fieldType)) {
					return new Timestamp(tmpO);
				} else if (Time.class.equals(fieldType)) {
					return new Time(tmpO);
				}
			}
		}
		return next(jsonValue, fieldType);
	}

}
