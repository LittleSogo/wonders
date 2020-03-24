package common.utils.construction;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonUtil {
	private JsonGenerator jsonGenerator = null;
	static ObjectMapper objectMapper = new ObjectMapper();

	public void init() {
		try {
			// objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
			// false);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(
					System.out, JsonEncoding.UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String writeListJSON(Object object) {
		String returnJSON = null;
		try {
			returnJSON = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJSON;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(new JacksonUtil().writeListJSON("tt"));
	}
}
