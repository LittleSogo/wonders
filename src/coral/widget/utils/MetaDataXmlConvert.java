package coral.widget.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import wfc.service.util.OrderedHashMap;
import wfc.service.util.OrderedHashSet;
import coral.widget.data.MetaDataDataTypeEnum;

/**
 * 
 * XML对象转换器，内部使用dom4j处理xml
 * 
 * @author 龚云
 * 
 */
public class MetaDataXmlConvert extends MetaDataConvert<Branch> {

	/**
	 * 转换对象为{@link Document}，根节点名称由{@link MetaDataTypeUtils}
	 * .getDataType方法根据o的类型判断得到
	 * 
	 * @param o
	 *            待转换的对象
	 * @return
	 */
	public static Document convertToDocument(Object o) {
		return convertToDocument(o, null);
	}

	/**
	 * 转换对象为{@link Document}，若rootName为空则根节点名称由{@link MetaDataTypeUtils}
	 * .getDataType方法根据o的类型判断得到
	 * 
	 * @param o
	 *            待转换的对象
	 * @param rootName
	 *            自定义根节点名称
	 * @return
	 */
	public static Document convertToDocument(Object o, String rootName) {
		return new MetaDataXmlConvert().convertToXml(o, rootName);
	}

	/**
	 * 序列化doc，去除xml头信息
	 * 
	 * @param doc
	 *            待处理的xml对象
	 * @return
	 */
	public static String toNoHeadXmlString(Document doc) {
		return StringUtils.trimToEmpty(toXmlString(doc)).replaceFirst(
				"<\\?xml.*\\?>", "");
	}

	/**
	 * 序列化doc，去除xml头信息
	 * 
	 * @param doc
	 *            待处理的xml对象
	 * @return
	 */
	public static String toXmlString(Document doc) {
		Writer w = new StringWriter();
		XMLWriter writer = new XMLWriter(w);
		try {
			writer.write(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w.toString();
	}

	protected MetaDataXmlConvert() {
	}

	/**
	 * 添加一个节点
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
	 * @return
	 */
	@Override
	protected Branch add(Branch from, String name, String type, String value,
			Object originalObj) {
		Element el = from.addElement(name);
		if (type != null)
			el.addAttribute("type", type);
		if (value == null)
			value = "";
		el.addText(value);
		return el;
	}

	/**
	 * 将对象转换为xml对象，若rootName为空则根节点名称由{@link MetaDataTypeUtils}
	 * .getDataType方法根据o的类型判断得到
	 * 
	 * @param o
	 * @param rootName
	 * @return
	 */
	protected Document convertToXml(Object o, String rootName) {
		Document doc = createUTF8Document();
		String type = MetaDataTypeUtils.getDataType(o.getClass());
		if (StringUtils.trimToEmpty(rootName).isEmpty())
			rootName = type;
		convertObject(o, rootName, doc, type);
		return doc;
	}

	/**
	 * 创建xml文档对象
	 * 
	 * @param charset
	 *            指定编码
	 * @return
	 */
	protected Document createDocument(String charset) {
		Document d = DocumentHelper.createDocument();
		d.setXMLEncoding(charset);
		return d;
	}

	/**
	 * 创建UTF-8编码的xml文档对象
	 * 
	 * @return
	 */
	protected Document createUTF8Document() {
		return createDocument("UTF-8");
	}

	/**
	 * {@link Collection}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	@Override
	protected void dealCollection(Object o, String name, Branch root,
			String type) {
		Collection<?> cO = (Collection<?>) o;
		Branch el = add(root, name, null, null, null);
		if (o instanceof OrderedHashSet<?>) {
			OrderedHashSet<?> oS = (OrderedHashSet<?>) o;
			for (int i = 0; i < oS.size(); i++) {
				convertObject(oS.get(i), "item", el, null);
			}
		} else {
			for (Object value : cO) {
				convertObject(value, "item", el, null);
			}
		}
	}

	/**
	 * {@link Map}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	@Override
	protected void dealMap(Object o, String name, Branch root, String type) {
		boolean hasType = StringUtils.trimToEmpty(type).isEmpty();
		String _type = hasType ? MetaDataDataTypeEnum.Map.toString() : type;
		if (o instanceof OrderedHashMap<?, ?>) {
			Branch el = add(root, name, _type, null, null);
			OrderedHashMap<?, ?> omO = (OrderedHashMap<?, ?>) o;
			for (int i = 0; i < omO.size(); i++) {
				String k = omO.getKey(i) == null ? "null" : omO.getKey(i)
						.toString();
				Branch item = add(el, "item", null, null, null);
				convertObject(k, "key", item, null);
				convertObject(omO.getValue(i), "value", item, null);
			}
		} else {
			Branch el = add(root, name, _type, null, null);
			Map<?, ?> mO = (Map<?, ?>) o;
			for (Entry<?, ?> entry : mO.entrySet()) {
				Object key = entry.getKey();
				String k = key == null ? "null" : key.toString();
				Branch item = add(el, "item", null, null, null);
				convertObject(k, "key", item, null);
				convertObject(entry.getValue(), "value", item, null);
			}
		}
	}

}
