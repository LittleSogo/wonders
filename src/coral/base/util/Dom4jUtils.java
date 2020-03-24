/**
 * Project: Coral
 * Source file: Dom4jUtils.java
 * Create At 2013-10-23 下午04:12:15
 * Create By 龚云
 */
package coral.base.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author 龚云
 * 
 */
public class Dom4jUtils {

	public static String attr(Element el, String name, String defaultVal) {
		Attribute attr = el.attribute(name);
		if (attr != null) {
			return attr.getValue();
		}
		return defaultVal;
	}

	public static String attrTrimToEmpty(Element el, String name,
			String defaultVal) {
		Attribute attr = el.attribute(name);
		if (attr != null) {
			return StringUtils.trimToEmpty(attr.getValue());
		}
		return defaultVal;
	}

	public static boolean checkNamespace(Element el, String namespaceUri) {
		return namespaceUri.equals(el.getNamespaceURI());
	}

}
