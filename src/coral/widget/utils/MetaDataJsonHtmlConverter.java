package coral.widget.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;

/**
 * 
 * Json对象转换器，基于{@link MetaDataJsonConverter}，在调用add方法新建节点时转义掉html字符
 * 
 * @author 龚云
 * 
 */
public class MetaDataJsonHtmlConverter extends MetaDataJsonConverter {

	/**
	 * 转换对象为{@link JSONObject}
	 * 
	 * @param o
	 *            待转换的对象
	 * @return
	 */
	public static JSONObject convertToJson(Object o) {
		return new MetaDataJsonHtmlConverter()._convertToJson(o);
	}

	/**
	 * 转换name和value参数中的html字符，然后调用父类{@link MetaDataJsonConverter}中的add方法完成逻辑
	 * 
	 * @param from
	 *            父节点
	 * @param name
	 *            节点名称
	 * @param type
	 *            节点类型
	 * @param value
	 *            节点值
	 * @param originalObj
	 *            节点值的来源（即value的来源对象）
	 * @return 若type为空则返回新建的{@link JSONObject}
	 *         节点，否则返回父节点，也即是说对于无类型的value不在多建立一层json结构
	 */
	@Override
	protected JSONObject add(JSONObject from, String name, String type,
			String value, Object originalObj) {
		name = StringEscapeUtils.escapeHtml(name);
		value = StringEscapeUtils.escapeHtml(value);
		return super.add(from, name, type, value, originalObj);
	}

}
