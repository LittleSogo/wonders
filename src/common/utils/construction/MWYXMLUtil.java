package common.utils.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Arrays;

import wfc.service.util.OrderedHashMap;
import wfc.service.xml.XMLHelper;

/**
 * xml操作工具类
 * @author mywaystay
 */
public class MWYXMLUtil {
	
	/**
	 *  将Document转换成byte[]
	 * @param doc
	 * @return
	 */
	public static byte[] convert2XmlData(Document doc, String strEncoding) {
		return XMLHelper.genXML(doc, strEncoding);
	}
	
	/**
	 *  将orderHashMap转换成byte[]
	 * @param map
	 * @param tagName
	 * @param strEncoding
	 * @return
	 */
	public static byte[] convert2XmlByte(OrderedHashMap<String, Object> map,String tagName, String strEncoding) {
		return convert2XmlData(convert2XmlData(map,tagName,strEncoding), strEncoding);
	}
	
	/**
	 * 将orderHashMap转换成Document
	 * @param map
	 * @param tagName
	 * @param strEncoding
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Document convert2XmlData(OrderedHashMap map,String tagName, String strEncoding){
		Document doc = XMLHelper.newDoc();
		Element eformElement = doc.createElement(tagName);
		//eformElement.setAttribute("id", "123");
		for (int i = 0; i < map.size(); i++) {
			String name = (String) map.getKey(i);
			Object value =  map.get(name);
			if(value!=null){
				if(value instanceof List){
					List values = (List)value;
					for (int j = 0; j < values.size(); j++) {
						String valueStr = String.valueOf(values.get(j));
						try {
							Element nameElement = doc.createElement(name);
							XMLHelper.setOwnText(nameElement, valueStr);
							eformElement.appendChild(nameElement);
						} catch (Exception e) {
						}
					}
				}else if(value instanceof String[]){
					String[] values = (String[])value;
					for (int k = 0; k < values.length; k++) {
						String valueStr = String.valueOf(values[k]);
						try {
							Element nameElement = doc.createElement(name);
							XMLHelper.setOwnText(nameElement, valueStr);
							eformElement.appendChild(nameElement);
						} catch (Exception e) {
						}
					}
				}else{
					String valueStr = String.valueOf(value);
					try {
						Element nameElement = doc.createElement(name);
						XMLHelper.setOwnText(nameElement, valueStr);
						eformElement.appendChild(nameElement);
					} catch (Exception e) {
					}
				}
			}
		}
		doc.appendChild(eformElement);
		return doc;
	}
	
	/**
	 * 生xml文件
	 * @param map
	 * @param tagName
	 * @param strFile
	 * @param strEncoding
	 */
	@SuppressWarnings("unchecked")
	public static void outputXml(OrderedHashMap map,String tagName, String strFile, String strEncoding){
		XMLHelper.genXML(convert2XmlData(map, tagName, strEncoding), strFile, strEncoding);
		System.out.println("已生成 xml文件...");  
	}
	
	/**
	 * 将Document转换成Map
	 * @param doc
	 * @param selectXpath "//test/*"
	 * @return
	 */
	public static Map<String, String> xmlConvert2Map(Document doc,String selectXpath){
		Map<String,String> map = new HashMap<String,String>();
		NodeList nodeList = XMLHelper.selectNodeList(doc, selectXpath);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			String key = element.getNodeName();
			String value = XMLHelper.getOwnText(element);
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * 将Document转换成OrderedHashMap
	 * @param doc
	 * @param selectXpath "//test/*"
	 * @return
	 */
	public static OrderedHashMap<String, String> xmlConvert2OrderedHashMap(Document doc,String selectXpath){
		OrderedHashMap<String,String> map = new OrderedHashMap<String,String>();
		NodeList nodeList = XMLHelper.selectNodeList(doc, selectXpath);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			String key = element.getNodeName();
			String value = XMLHelper.getOwnText(element);
			map.put(key, value);
		}
		return map;
	}
	
	/******************************XML包含多个value****************************************************/
	/**
	 * 将Document转换成Map
	 * @param doc
	 * @param selectXpath "//test/*"
	 * @return
	 */
	public static Map<String, Object> xmlConvert2Map2(Document doc,String selectXpath){
		Map<String,Object> map = new HashMap<String,Object>();
		NodeList nodeList = XMLHelper.selectNodeList(doc, selectXpath);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			String key = element.getNodeName();
			String value = XMLHelper.getOwnText(element);
			putValueToMap(map, key, value);
		}
		return map;
	}
	
	/**
	 * 将Document转换成OrderedHashMap
	 * @param doc
	 * @param selectXpath "//test/*"
	 * @return
	 */
	public static OrderedHashMap<String, Object> xmlConvert2OrderedHashMap2(Document doc,String selectXpath){
		OrderedHashMap<String,Object> map = new OrderedHashMap<String,Object>();
		NodeList nodeList = XMLHelper.selectNodeList(doc, selectXpath);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			String key = element.getNodeName();
			String value = XMLHelper.getOwnText(element);
			putValueToMap(map, key, value);
		}
		return map;
	}
	
	/**
	 * 自动判断Map中是否含存在相同的key，如果存在的话则用List存储
	 * @param map
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static void putValueToMap(Map<String,Object> map ,String key, Object value){
		if(map.containsKey(key)){
			Object valueObj = map.get(key);
			List valueList = null;
			if(valueObj instanceof List){
				valueList = (List) valueObj;
				valueList.add(value);
				map.put(key, valueList);
			}else if(valueObj instanceof Object[]){
				valueList = new ArrayList();
				List tempList = Arrays.asList((Object[])valueObj);
				if(tempList!=null) valueList.addAll(tempList);
				valueList.add(value);
				map.put(key, valueList.toArray(new String[valueList.size()]));
			}else{
				valueList = new ArrayList();
				valueList.add(valueObj);
				valueList.add(value);
				map.put(key, valueList);
			}
			
		}else{
			map.put(key, value);
		}
	}
	
	/**
	 * 自动判断Map中是否含存在相同的key，如果存在的话则用List存储
	 * @param map
	 * @param key
	 */
	public static void putValueToMap(Map<String,Object> map ,String key, String value){
		putValueToMap(map, key, (Object)value);
	}
	
	/**
	 * 自动判断Map中是否含存在相同的key，如果存在的话则用List存储
	 * @param map
	 * @param key
	 */
	public static void putValueToOrderedHashMap(OrderedHashMap<String,Object> map ,String key, String value){
		putValueToOrderedHashMap(map, key, (Object)value);
	}
	
	/**
	 * 自动判断Map中是否含存在相同的key，如果存在的话则用List存储
	 * @param map
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static void putValueToOrderedHashMap(OrderedHashMap<String,Object> map ,String key, Object value){
		if(map.containsKey(key)){
			Object valueObj = map.get(key);
			List valueList = null;
			if(valueObj instanceof List){
				valueList = (List) valueObj;
				valueList.add(value);
				map.put(key, valueList);
			}else if(valueObj instanceof Object[]){
				valueList = new ArrayList();
				List tempList = Arrays.asList((Object[])valueObj);
				if(tempList!=null) valueList.addAll(tempList);
				valueList.add(value);
				map.put(key, valueList.toArray(new String[valueList.size()]));
			}else{
				valueList = new ArrayList();
				valueList.add(valueObj);
				valueList.add(value);
				map.put(key, valueList);
			}
			
		}else{
			map.put(key, value);
		}
	}
	
	/******************************XML包含多个value****************************************************/
	
}
