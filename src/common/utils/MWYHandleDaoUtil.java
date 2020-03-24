package common.utils;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import wfc.facility.tool.autocode.PaginationArrayList;
import wfc.service.database.BlobHelper;
import wfc.service.database.ClobHelper;
import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.database.RecordSet;
import wfc.service.database.SQL;
import wfc.service.util.OrderedHashMap;
import wfc.service.xml.XMLHelper;

import common.utils.construction.JacksonUtil;
import common.utils.construction.MWYReflectUtils;
import common.utils.construction.MWYXMLUtil;
import common.utils.log.MWYLog;
import common.utils.string.MWYCoralStaticConfig;
import common.utils.string.StringUtils;

import coral.widget.data.DataSet;
import coral.widget.data.RowSet;

/**
 * 通用dao操作类
 * @author mywaystay
 */
public class MWYHandleDaoUtil {
	public static final String PAGING = "paging";
	public static final String JSON = "json";
	public static final String TABLENAME = "TABLENAME";
	public static final String BLOBCOLUMNNAME = "BLOBCOLUMNNAME";
	public static final String CLOBCOLUMNNAME = "CLOBCOLUMNNAME";
	public static final String IN = "IN";
	public static final String _UPDATE_FIELD = "_UPDATE_FIELD";
	 
	
	/**
	 * 通用查询，包括是否分页和高级模糊查询
	 * > < = leftLike ...
	 * @param beanDaoClass
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Object getObjectByCodintion(Map<String, Map<String, Object>> map, 
			Class<?> beanDaoClass) throws Exception {
		Object obj = null;
		boolean isPaging = false;
		String page = null;
		String rows = null;
		String suffix = null;
		Conditions conds = null;
		if(map!=null){
			/* 是否分页查询 */
			if (map.get("paging") != null) {
				isPaging = true;
				if(map.get("paging")==null){
					page ="1";
					rows="10";
				}else{
					page =  (page = (String) map.get("paging").get("page")) == null ? "1" : (String)page;
					rows =  (rows = (String) map.get("paging").get("rows")) == null ? "10" : (String)rows;
				}
				System.out.println("--分页查询--- page="+page+", rows="+rows);
				map.remove("paging");
			}
			
			/* 是否通过条件查询 */
			if (!map.isEmpty()) {
				conds = Conditions.newAndConditions();
				for (String key : map.keySet()) {
					int coditionInt = conditionToInt(key);
					/*是否排序*/
					if(coditionInt==-10) {
						suffix =  map.get(key)==null?null:(String) map.get(key).get(key);
						continue;
					}
					
					System.out.println("---->"+key);
					Map<String, Object> childMap = map.get(key);
					if(childMap!=null){
						for (String childKey : childMap.keySet()) {
							//System.out.println("***childKey***"+childMap.get(childKey));
							/*if(coditionInt==6||coditionInt==7||coditionInt==8)
							if(childMap.get(childKey)==null)
								childMap.put(childKey, "");*/
							if(key.equals("IN")){
								
									//System.out.println("走集合");
									conds.add(new Condition(
											MWYReflectUtils.beanNameToColumn(childKey), true,
											(Collection) childMap.get(childKey)));
							}else{
								conds.add(new Condition(
										MWYReflectUtils.beanNameToColumn(childKey), 
										coditionInt, childMap.get(childKey)));
							}
							System.out.println("------>"+childKey +", "+childMap.get(childKey));
						}
					}
				}
			}
		}
		
		if(isPaging)
			obj = MWYReflectUtils.invokeMethod("query", beanDaoClass, 
					new Class[] {Conditions.class, String.class, int.class, int.class},
					new Object[]{conds,suffix,Integer.parseInt(rows),Integer.parseInt(page)});
		else
			obj = MWYReflectUtils.invokeMethod("query", beanDaoClass, 
					new Class[] {Conditions.class, String.class},
					new Object[]{conds,suffix});
		return obj;
	}
	
	/**
	 * map转换为 Conditions
	 * @param map
	 * @return
	 */
	public static Conditions mapToConditions(Map<String, Map<String, Object>> map){
		Conditions conds = null;
		/* 是否通过条件查询 */
		if (!map.isEmpty()) {
			conds = Conditions.newAndConditions();
			for (String key : map.keySet()) {
				int coditionInt = conditionToInt(key);

				Map<String, Object> childMap = map.get(key);
				if(childMap!=null){
					for (String childKey : childMap.keySet()) {
						if(key.equals("IN")){

							//System.out.println("走集合");
							conds.add(new Condition(
									MWYReflectUtils.beanNameToColumn(childKey), true,
									(Collection) childMap.get(childKey)));
						}else{
							conds.add(new Condition(
									MWYReflectUtils.beanNameToColumn(childKey), 
									coditionInt, childMap.get(childKey)));
						}
						System.out.println("------>"+childKey +", "+childMap.get(childKey));
					}
				}
			}
		}
		return conds;
	}
	
	/**
	 * 查询结果转换为JSON
	 */
	@SuppressWarnings("unchecked")
	public static Object getJsonByCodintion(Map<String, Map<String, Object>> map,
			String[] filterBeanNames, Class<?> beanClass, Class<?> beanDaoClass)
			throws Exception {
		Map<String,String> beanNamesMap = MWYReflectUtils.getBeanNameMap(new String[]{"private"},filterBeanNames, beanClass);
		PaginationArrayList objPagingList = null;
		List objList = null;
		org.json.JSONArray jsonArray = new org.json.JSONArray();
		JSONObject returnJson = new JSONObject();
		
		/*分页*/
		if (map!=null&&map.get("paging") != null){
			objPagingList =(PaginationArrayList) getObjectByCodintion(map, beanDaoClass);
			returnJson.put("total", MWYReflectUtils.getter((PaginationArrayList)objPagingList, "totalItemCount"));
			returnJson.put("rows", collectionToJsonArray(objPagingList, beanNamesMap));  
			return returnJson;
		}else{
			objList =(List) getObjectByCodintion(map, beanDaoClass);
			returnJson.put("total", objList.size());
			returnJson.put("rows", collectionToJsonArray(objList, beanNamesMap));  
			return returnJson;
		}
	}
	
	/**
	 * 查询结果转换为JSON,升级版
	 */
	@SuppressWarnings("unchecked")
	public static String getJsonByCodintion2(Map<String, Map<String, Object>> map,
			String[] filterBeanNames, Class<?> beanClass, Class<?> beanDaoClass)
			throws Exception {
		Map<String,String> beanNamesMap = MWYReflectUtils.getBeanNameMap(new String[]{"private"},filterBeanNames, beanClass);
		PaginationArrayList objPagingList = null;
		List objList = null;
		org.json.JSONArray jsonArray = new org.json.JSONArray();
		JSONObject returnJson = new JSONObject();
		/*分页*/
		if (map!=null&&map.get("paging") != null){
			objPagingList =(PaginationArrayList) getObjectByCodintion(map, beanDaoClass);
			returnJson.put("total", MWYReflectUtils.getter((PaginationArrayList)objPagingList, "totalItemCount"));
			returnJson.put("rows", collectionToJsonArray(objPagingList, beanNamesMap));  
			return returnJson.toString();
		}else{
			objList =(List) getObjectByCodintion(map, beanDaoClass);
			return JacksonUtil.writeListJSON(objList);
		}
	}
	
	/**
	 * 集合转换为JSONArray
	 * @param con
	 * @param beanNamesMap
	 * @return
	 * @throws Exception
	 */
	public static JSONArray collectionToJsonArray(Collection con,
			Map<String, String> beanNamesMap) throws Exception {
		JSONArray jsonArray = new org.json.JSONArray();
		if(con!=null){
			for (Object obj : con) {
				JSONObject json = new JSONObject();
				for (String beanNameKey : beanNamesMap.keySet()) {
					/*System.out.println(MWYReflectUtils.getter(obj, beanNamesMap
							.get(beanNameKey)));*/
					json.put(beanNameKey, MWYReflectUtils.getter(obj, beanNamesMap
							.get(beanNameKey)));
				}
				jsonArray.put(json);
			}
		}
		return jsonArray;
	}
	
	/**
	 * 通过分割conditionStr 得到查询条件
	 * @param conditionStr
	 * @param beanDaoClass
	 * @return 返回对象或者集合
	 * @throws Exception 
	 */
	public static Object queryByString (String conditionStr, String suffix, Class<?> beanDaoClass) throws Exception{
		//wfCaseId = "123",wfCaseId > "123"
		/*Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					String fieldValue = values[2].trim();
					if(fieldValue!=null&&fieldValue.equals("null"))
						fieldValue = null;
					conds.add(new Condition(
							MWYReflectUtils.beanNameToColumn(values[0].trim()), 
							coditionInt, fieldValue));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+fieldValue);
				}
			}
		}*/
		Conditions conds = conditionStrToConditions(conditionStr);
		List<?> list =  (List<?>) MWYReflectUtils.invokeMethod("query", beanDaoClass, 
				new Class[] {Conditions.class, String.class},
				new Object[]{conds,suffix});
		if(list!=null&&list.size()==0) return null;
		if(list!=null&&list.size()==1) return list.get(0);
		return list;
	}
	
	/**
	 * 通过分割conditionStr 得到查询条件,只返回集合
	 * @param conditionStr //wfCaseId = "123",wfCaseId > "123"
	 * @param beanDaoClass
	 * @return 只返回集合
	 * @throws Exception 
	 */
	public static List<?> queryByStringReturnList (String conditionStr, String suffix, Class<?> beanDaoClass) throws Exception{
		//wfCaseId = "123",wfCaseId > "123"
		/*Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					String fieldValue = values[2].trim();
					if(fieldValue!=null&&fieldValue.equals("null"))
						fieldValue = null;
					conds.add(new Condition(
							MWYReflectUtils.beanNameToColumn(values[0].trim()), 
							coditionInt, fieldValue));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+fieldValue);
				}
			}
			System.out.println("--sql-->"+conds.toString());
		}*/
		Conditions conds = conditionStrToConditions(conditionStr);
		List<?> list =  (List<?>) MWYReflectUtils.invokeMethod("query", beanDaoClass, 
				new Class[] {Conditions.class, String.class},
				new Object[]{conds,suffix});
		return list;
	}
	
	/**
	 * 通过分割conditionStr 得到查询条件,只返回集合
	 * @param conditionStr //wfCaseId = "123",wfCaseId > "123"
	 * @param beanDaoClass
	 * @return 只返回集合
	 * @throws Exception 
	 */
	public static List<?> queryByStringReturnList (String conditionStr, int columnType, String suffix, Class<?> beanDaoClass) throws Exception{
		Conditions conds = conditionStrToConditions(conditionStr,columnType);
		List<?> list =  (List<?>) MWYReflectUtils.invokeMethod("query", beanDaoClass, 
				new Class[] {Conditions.class, String.class},
				new Object[]{conds,suffix});
		return list;
	}
	
	/**
	 * 通过分割conditionStr 得到查询条件,<br/>
	 * 只返回Map,其中map中的key的值是指定的field调用get方法得到的值 <br/>
	 * 如果针对key相同是它对于的value将会是list
	 * @param conditionStr //wfCaseId = "123",wfCaseId > "123"
	 * @param columnType
	 * @param suffix
	 * @param beanDaoClass
	 * @param keyFieldName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map queryByStringReturnMap(String conditionStr, int columnType, String suffix, Class<?> beanDaoClass,String keyFieldName) throws Exception{
		Map map = null;
		List<?> list = queryByStringReturnList(conditionStr, columnType, suffix, beanDaoClass);
		if(list!=null&&list.size()>0){
			map = new HashMap();
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Object key = MWYReflectUtils.getter(obj, keyFieldName);
				MWYXMLUtil.putValueToMap(map,StringUtils.valueOf(key), obj);
			}
		}
		return map;
	}
	
	/**
	 *  通过分割conditionStr 得到查询条件带分页,只返回Map,<br/>
	 *  其中map中的key的值是指定的field调用get方法得到的值<br/>
	 * 	如果针对key相同是它对于的value将会是list
	 * @param conditionStr //wfCaseId = "123",wfCaseId > "123"
	 * @param columnType
	 * @param suffix
	 * @param rows
	 * @param page
	 * @param beanDaoClass
	 * @param keyFieldName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map queryByStringReturnMap(String conditionStr,int columnType, String suffix, String rows, String page, Class<?> beanDaoClass, String keyFieldName) throws Exception{
		Map map = null;
		List<?> list = queryByStringReturnList(conditionStr, columnType, suffix, beanDaoClass);
		if(list!=null&&list.size()>0){
			map = new HashMap();
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Object key = MWYReflectUtils.getter(obj, keyFieldName);
				MWYXMLUtil.putValueToMap(map,StringUtils.valueOf(key), obj);
			}
		}
		return map;
	}
	
	
	/**
	 * 带分页的查询
	 * @param conditionStr
	 * @param suffix
	 * @param beanDaoClass
	 * @return
	 * @throws Exception
	 */
	public static Object queryByStringReturnList(String conditionStr,int columnType, String suffix, String rows, String page, Class<?> beanDaoClass) throws Exception{
		Conditions conds = conditionStrToConditions(conditionStr, columnType);
		PaginationArrayList<?> list =  (PaginationArrayList<?>) MWYReflectUtils.invokeMethod("query", beanDaoClass, 
				new Class[] {Conditions.class, String.class, int.class, int.class},
				new Object[]{conds,suffix,Integer.parseInt(rows),Integer.parseInt(page)});
	
		return list;
	}
	
	/**
	 * 带分页的查询
	 * @param conditionStr
	 * @param suffix
	 * @param beanDaoClass
	 * @return
	 * @throws Exception
	 */
	public static Object queryByString (String conditionStr, String suffix, String rows, String page, Class<?> beanDaoClass) throws Exception{
		//wfCaseId = "123",wfCaseId > "123"
		/*Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					conds.add(new Condition(
							MWYReflectUtils.beanNameToColumn(values[0].trim()), 
							coditionInt, values[2].trim()));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+values[2].trim());
				}
			}
		}*/
		Conditions conds = conditionStrToConditions(conditionStr);
		PaginationArrayList<?> list =  (PaginationArrayList<?>) MWYReflectUtils.invokeMethod("query", beanDaoClass, 
				new Class[] {Conditions.class, String.class, int.class, int.class},
				new Object[]{conds,suffix,Integer.parseInt(rows),Integer.parseInt(page)});
	
		return list;
	}
	
	/**
	 * 通用删除方法
	 * @param conditionStr
	 * @param beanDaoClass
	 * @return 返回对象或者集合
	 * @throws Exception 
	 */
	public static boolean deleteByString (String conditionStr, Class<?> beanDaoClass) throws Exception{
		//wfCaseId = "123",wfCaseId > "123"
		/*Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					conds.add(new Condition(
							MWYReflectUtils.beanNameToColumn(values[0].trim()), 
							coditionInt, values[2].trim()));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+values[2].trim());
				}
			}
		}*/
		Conditions conds = conditionStrToConditions(conditionStr);
		Object index = MWYReflectUtils.invokeMethod("delete", beanDaoClass, 
				new Class[] {Conditions.class},
				new Object[]{conds});
		System.out.println("*************已删除"+index+"条记录*******************");
		return Integer.parseInt(String.valueOf(index))>0;
	}
	
	
	/**
	 * 通过简单的条件修改数据库某条或者几条数据
	 * @param conditionStr //wfCaseId = "123",wfCaseId > "123",wfCaseId LIKE %123%  
	 * 						不支持 IN，NOT IN或者条件中带空格的，比如：ORIGINAL LIKE
	 * @param updateFieldMap 需要修改的字段名，key中放入数据库中对应的列名
	 * @param beanDaoClass
	 * @return
	 * @throws Exception
	 */
	public static boolean updateByString(String conditionStr,Map<String, Object> updateFieldMap,
						Class<?> beanDaoClass) throws Exception {
		Conditions conds = conditionStrToConditions(conditionStr);
		Object index = MWYReflectUtils.invokeMethod("update", beanDaoClass,
				new Class[] {Map.class, Conditions.class }, new Object[] {updateFieldMap, conds });
		System.out.println("*************已修改"+index+"条记录*******************");
		return Integer.parseInt("" + index) > 0;
	}
	
	public static boolean updateByString(Object obj,Class<?> beanDaoClass) {
		try {
			MWYReflectUtils.invokeMethod("update", beanDaoClass,
				new Class[] {obj.getClass()}, new Object[] {obj });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 新增bean
	 * @param obj
	 * @param beanDaoClass
	 * @return
	 * @throws Exception 
	 */
	public static void addBeanByMap(Object obj,Class<?> beanDaoClass) throws Exception{
//		try {
//			MWYReflectUtils.invokeMethod("add", beanDaoClass,
//				new Class[] {obj.getClass()}, new Object[] {obj });
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		MWYReflectUtils.invokeMethodSelectExp("add", beanDaoClass,
				new Class[] {obj.getClass()}, new Object[] {obj },true,null);
//		return true;
	}
	
	/**
	 * 新增obj
	 * @param beanName$values  通过数组将数组中对应的0,1 2,3 4,5...set到obj中，数组中偶数坐标是_COLUMNNAME
	 * @param obj
	 * @param beanDaoClass
	 * @return
	 */
	public static boolean addBeanByArrays(Object[] beanName$values,Object obj,Class<?> beanDaoClass){
		
		boolean result = false;
		try {
			MWYReflectUtils.invokeMethodSelectExp("add", beanDaoClass,
					new Class[] {obj.getClass()},
					new Object[] {
					MWYReflectUtils.setBeanByArrays(beanName$values, obj) },true,null);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			MWYLog.error("新增"+obj.getClass()+"失败");
			result = false;
		}
		return result;
	}
	
	/**
	 * 获取Blob字段
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] getBlob(Map<String,Object> map){
		Conditions conds = null;
		byte[] bytes = null; 
		if(map!=null&&!map.isEmpty()){
			conds = Conditions.newAndConditions();
			String tableName = (String) map.get(TABLENAME);
			String blobColumnName = (String) map.get(BLOBCOLUMNNAME);
			for (String key:map.keySet()) {
				if(map.get(key) instanceof Map){
					Map<String,Object> childMap = (Map<String,Object>) map.get(key);
					if(childMap!=null){
						for (String childKey:childMap.keySet()) {
							conds.add(new Condition(key, 
									conditionToInt(childKey), childMap.get(childKey)));
						}
					}
				}
			}
			bytes = BlobHelper.getBlob(tableName, blobColumnName, conds.toString(), conds.getObjectArray());
		}
		if(bytes!=null&&bytes.length<=0) return null;
		System.out.println("-----------bytes.length--------->"+bytes.length);
		return bytes;
	}
	
	public static int setBlob(Map<String,Object> map,byte[] bytes) throws Exception{
		Conditions conds = null;
		int index = 0; 
		if(map!=null&&!map.isEmpty()){
			conds = Conditions.newAndConditions();
			String tableName = (String) map.get(TABLENAME);
			String blobColumnName = (String) map.get(BLOBCOLUMNNAME);
			for (String key:map.keySet()) {
				if(map.get(key) instanceof Map){
					Map<String,Object> childMap = (Map<String,Object>) map.get(key);
					if(childMap!=null){
						for (String childKey:childMap.keySet()) {
							conds.add(new Condition(key, 
									conditionToInt(childKey), childMap.get(childKey)));
						}
					}
				}
			}
			
			try {
				index = BlobHelper.setBlob(tableName, blobColumnName, conds.toString(),bytes, conds.getObjectArray());
			} catch (Exception e) {
				index = 0;
			}
		}
		System.out.println("------MWYHandleDao-----setBlob---已修改Blob------>"+index);
		return index;
	}
	
	
	/**
	 * 获取clob字段
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getClob(Map<String,Object> map){
		Conditions conds = null;
		String str = null; 
		if(map!=null&&!map.isEmpty()){
			conds = Conditions.newAndConditions();
			String tableName = (String) map.get(TABLENAME);
			String blobColumnName = (String) map.get(CLOBCOLUMNNAME);
			for (String key:map.keySet()) {
				if(map.get(key) instanceof Map){
					Map<String,Object> childMap = (Map<String,Object>) map.get(key);
					if(childMap!=null){
						for (String childKey:childMap.keySet()) {
							conds.add(new Condition(key, 
									conditionToInt(childKey), childMap.get(childKey)));
						}
					}
				}
			}
			str = ClobHelper.getClob(tableName, blobColumnName, conds.toString(), conds.getObjectArray());
		}
		return str;
	}
	
	/**
	 * 获取将Blob中的xml转换成map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getBlob_xmlToMap(Map<String,Object> reqMap){
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = null;
		byte[] by = getBlob(reqMap);
		if(by!=null&&by.length>0){
			doc = XMLHelper.getDoc(new ByteArrayInputStream(by));
			NodeList nodeList = XMLHelper.selectNodeList(doc, "//eform/*");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element)nodeList.item(i);
				String key = element.getNodeName();
				String value = XMLHelper.getOwnText(element);
				//map.put(key, value);
				MWYXMLUtil.putValueToMap(map, key, value);
			}
			//还原最初始的模拟提交的form表单数据(将处理的list转为数组)
			for (String key:map.keySet()) {
				Object value = map.get(key);
				if(value instanceof List){
					List list = (List)value;
					if(list!=null&&list.size()>0)
						map.put(key, list.toArray(new String[list.size()]));
				}
			}
		}
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 获得指定xml的节点转换成Map
	 * @param reqMap
	 * @param selectXmPath "//eform/*"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getBlob_xmlToMap(Map<String,Object> reqMap,String selectXmPath){
		Map map = new HashMap();
		Document doc = null;
		byte[] by = getBlob(reqMap);
		if(by!=null){
			doc = XMLHelper.getDoc(new ByteArrayInputStream(by));
			NodeList nodeList = XMLHelper.selectNodeList(doc, selectXmPath);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element)nodeList.item(i);
				String key = element.getNodeName();
				String value = XMLHelper.getOwnText(element);
				//map.put(key, value);
				MWYXMLUtil.putValueToMap(map, key, value);
			}
		}
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 获得指定xml的节点转换成Map
	 * @param reqMap
	 * @param selectXmPath "//eform/*"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static OrderedHashMap<String,Object> getBlob_xmlToOrderedHashMap(Map<String,Object> reqMap,String selectXmPath){
		OrderedHashMap map = new OrderedHashMap();
		Document doc = null;
		byte[] by = null;
		try {
			by = getBlob(reqMap);
			if(by!=null&&by.length>0){
				doc = XMLHelper.getDoc(new ByteArrayInputStream(by));
				NodeList nodeList = XMLHelper.selectNodeList(doc, selectXmPath);
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element)nodeList.item(i);
					String key = element.getNodeName();
					String value = XMLHelper.getOwnText(element);
					//map.put(key, value);
					MWYXMLUtil.putValueToMap(map, key, value);
				}
			}
		} catch (Exception e) {
			MWYLog.error("获取xml电子表单出错或者不存在改电子表单...");
		}
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 
	 * 查询表  指定字段作为map的key和value
	 * 注意： 指定的列名必须和数据库字段一样
	 * @param reMap
	 * @param columnNameKey
	 * @param columnNameValue
	 * @return
	 */
	public static Map<String,Object> getMapByPointColumn(Map<String,Object> reMap,String columnNameKey,String columnNameValue){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from "+reMap.get(TABLENAME));
		Conditions cons = reMap2Conditions(reMap);
		if(cons!=null)
			sql.append(" where "+cons.toString());
		RecordSet recordSet= MWYHandleDaoUtil.sqlExcute(sql.toString(),cons);
		DataSet dataSet = DataSet.convert(recordSet);
		List<RowSet> rowSetList = dataSet.getRows();
		Map<String,Object> map = null;
		if(rowSetList!=null){
			map = new HashMap<String, Object>();
			for (int i = 0; i < rowSetList.size(); i++) {
				OrderedHashMap<String, Object> orderMap = rowSetList.get(i).getRow();
				//MWYReflectUtils.systemPrintMap(orderMap);
				map.put(String.valueOf(orderMap.get(columnNameKey)), orderMap.get(columnNameValue));
			}
		}
		return map;
	}
	
	/**
	 * 
	 * 查询表  指定字段作为map的key和value
	 * 注意： 指定的列名必须和数据库字段一样
	 * @param tableName
	 * @param columnNameKey
	 * @param columnNameValue
	 * @return
	 */
	public static Map<String,Object> getMapByPointColumn(String tableNameAndCondition,String columnNameKey,String columnNameValue){
		String sql = "select * from "+tableNameAndCondition;
		RecordSet recordSet= MWYHandleDaoUtil.sqlExcute(sql);
		DataSet dataSet = DataSet.convert(recordSet);
		List<RowSet> rowSetList = dataSet.getRows();
		Map<String,Object> map = null;
		if(rowSetList!=null){
			map = new HashMap<String, Object>();
			for (int i = 0; i < rowSetList.size(); i++) {
				OrderedHashMap<String, Object> orderMap = rowSetList.get(i).getRow();
				//MWYReflectUtils.systemPrintMap(orderMap);
				map.put(String.valueOf(orderMap.get(columnNameKey)), orderMap.get(columnNameValue));
			}
		}
		return map;
	}
	
	/**
	 * 
	 * 查询表  指定字段作为map的key和value
	 * 注意： 指定的列名必须和数据库字段一样
	 * @param reMap
	 * @param columnNameKey
	 * @param columnNameValue
	 * @return OrderedHashMap<String,Object>
	 */
	public static OrderedHashMap<String,Object> getOrderedHashMapByPointColumn(Map<String,Object> reMap,String columnNameKey,String columnNameValue){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from "+reMap.get(TABLENAME));
		Conditions cons = reMap2Conditions(reMap);
		if(cons!=null)
			sql.append(" where "+cons.toString());
		RecordSet recordSet= MWYHandleDaoUtil.sqlExcute(sql.toString(),cons);
		DataSet dataSet = DataSet.convert(recordSet);
		List<RowSet> rowSetList = dataSet.getRows();
		OrderedHashMap<String,Object> map = null;
		if(rowSetList!=null&&rowSetList.size()>0){
			map = new OrderedHashMap<String, Object>();
			for (int i = 0; i < rowSetList.size(); i++) {
				OrderedHashMap<String, Object> orderMap = rowSetList.get(i).getRow();
				//MWYReflectUtils.systemPrintMap(orderMap);
				map.put(String.valueOf(orderMap.get(columnNameKey)), orderMap.get(columnNameValue));
			}
		}
		return map;
	}
	
	
	public static RecordSet sqlExcute(String sql, Conditions conds){
		if(conds!=null)
			return SQL.execute(sql,conds.getObjectArray());
		else
			return SQL.execute(sql);
	}
	
	public static RecordSet sqlExcute(String sql,int nCommonPageSize,int nCurrentPage,  Conditions conds){
		if(conds!=null)
			return SQL.execute(sql, nCommonPageSize, nCurrentPage,conds.getObjectArray());
		else
			return SQL.execute(sql, nCommonPageSize, nCurrentPage,null);
	}
	
	public static RecordSet sqlExcute(String sql){
		return SQL.execute(sql);
	}
	public static RecordSet sqlExcute(String sql,Object[] obj){
		return SQL.execute(sql,obj);
	}
	
	public static RecordSet sqlExcute(Connection conn,String sql){
		return SQL.execute(conn,sql);
	}
	
	/**
	 * 查询满足条的sql的条数
	 * @param sql
	 * @return
	 */
	public static int getCountRowBySQLExcute(String sql){
		RecordSet recordSet = SQL.execute(sql);
		DataSet dataSet = DataSet.convert(recordSet);
		List<RowSet> rowSetList = dataSet.getRows();
		return rowSetList.size();
	}
	
	
	
	/**
	 * 将指定格式的reMap转换为Conditions
	 * @param reMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Conditions reMap2Conditions(Map<String, Object> reMap) {
		Conditions conds = null;
		if (reMap != null && !reMap.isEmpty()) {
			conds = Conditions.newAndConditions();
			for (String key : reMap.keySet()) {
				if (reMap.get(key) instanceof Map) {
					Map<String, Object> childMap = (Map<String, Object>) reMap.get(key);
					if (childMap != null) {
						for (String childKey : childMap.keySet()) {
							if(key.equals("IN")){
								//System.out.println("走集合");
								conds.add(new Condition(
										MWYReflectUtils.beanNameToColumn(childKey), true,
										(Collection) childMap.get(childKey)));
							}else{
								conds.add(new Condition(
										key,conditionToInt(childKey), childMap
										.get(childKey)));
							}
						}
					}
				}
			}
		}
		return conds;
	}
	
	/**
	 * 将Map转换为条件sql字符串
	 * @param reMap
	 * @return
	 * @throws Exception 
	 *//*
	@SuppressWarnings("unchecked")
	public static String reMap2ConditionsToSql(Map reMap) throws Exception, Exception {
		Conditions conds =  mapToConditions(reMap);
		String sql = null;
		if(conds!=null){
			sql = conds.toString();
			List objs = conds.getObjectList();
			for (int i = 0; i < objs.size(); i++) {
				String value = (String) objs.get(i).getClass().getMethod("toString").invoke(objs.get(i));
				sql = sql.replaceFirst("\\?", "'"+value+"'");
			}
			System.out.println("-----reMap2ConditionsToSql------>>>>"+sql);
		}
		return sql;
	}*/
	
	@SuppressWarnings("unchecked")
	public static String reMap2ConditionsToSql(Map<String,Object> reMap, String andOR, String otherName) throws Exception{
		StringBuilder sql = new StringBuilder();
		if (reMap != null && !reMap.isEmpty()) {
			if(otherName == null) otherName = "";
			else if(!"".equals(otherName)) otherName = otherName+".";
			for (String key : reMap.keySet()) {
				if (reMap.get(key) instanceof Map) {
					Map<String, Object> childMap = (Map<String, Object>) reMap.get(key);
					if (childMap != null) {
						for (String childKey : childMap.keySet()) {

							if(key.equals("IN")|| key.equals("NOT IN")){
								//System.out.println("走集合");
								Collection list = (Collection) childMap.get(childKey);
								if(list!=null&&list.size()>0){
									sql.append(" "+andOR+" ").append(otherName+childKey).append(" "+key+" (");
									sql.append(formateCollectionToSqlString(list,",")).append(") ");
								}
							}else if(key.equals("LIKE")){
								String value = (String) childMap.get(childKey).getClass().
								getMethod("toString").invoke(childMap.get(childKey));
								sql.append(" "+andOR+" ").append(otherName+childKey).append(" "+key).append(" '%"+value+"%'");

							}else if(key.equals("LIKE Collection")){
								Collection list = (Collection) childMap.get(childKey);
								if(list!=null&&list.size()>0){
									int i = 0;
									for (Iterator iterator = list.iterator(); iterator.hasNext();) {
										Object object = (Object) iterator.next();
										String value = (String) object.getClass().getMethod("toString").invoke(object);
										if(i==0)
											sql.append(" "+andOR+" ( ").append(otherName+childKey).append(" like"+" '%"+value+"%'");
										else
											sql.append(" or ").append(otherName+childKey).append(" like"+" '%"+value+"%'");
										i++;
									}
									sql.append(" ) ");
								}
							}else{
								Object value = childMap.get(childKey);
								if(value!=null){
									value = (String) (childMap.get(childKey).getClass().
											getMethod("toString").invoke(childMap.get(childKey)));
									sql.append(" "+andOR+" ").append(otherName+childKey).append(" "+key+" ").append("'"+value+"'");
								}else{
									String temp_key = key;
									if(temp_key.equals("=")){
										temp_key = "is";
									}else if(temp_key.equals("!=")){
										temp_key = "is not";
									}
									sql.append(" "+andOR+" ").append(otherName+childKey).append(" "+temp_key+" ").append("null");
								}


							}
						}
					}
				}
			}
		}
		return sql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String formateCollectionToSqlString(Collection list,String regx) throws Exception{
		StringBuilder sqlB = new StringBuilder();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			String value = (String) object.getClass().getMethod("toString").invoke(object);
			sqlB.append("'").append(value).append("'").append(regx);
		}
		return sqlB.toString().substring(0, sqlB.length()-1);
	}
	
	
	
	/**
	 * @param conditionStr wfCaseId = "123",wfCaseId > "123"
	 * 					   (简单的已经规范，包含in 或者有空格的条件，其他复杂的有待整理)
	 * @return
	 */
	// 待开发 的规法 wfCaseId [=] 123 [_and_] wfCaseId [>] 123 [_and_] wfSysId IN (1,2,3) [_and_] wffName [LIKE] %admin% 
	public static Conditions conditionStrToConditions(String conditionStr){
		//wfCaseId = "123",wfCaseId > "123"
		Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					String fieldValue = values[2].trim();
					if(fieldValue!=null&&fieldValue.equals("null"))
						fieldValue = null;
					conds.add(new Condition(
							MWYReflectUtils.beanNameToColumn(values[0].trim()), 
							coditionInt, fieldValue));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+fieldValue);
				}
			}
			System.out.println("--sql-->"+conds.toString());
		}
		return conds;
	}
	
	/**
	 * @param conditionStr wfCaseId = "123",wfCaseId > "123"
	 * 					   (简单的已经规范，包含in 或者有空格的条件，其他复杂的有待整理)
	 * @param columnType  eg:MWYCoralStaticConfig._BEANNAME 或者数据库字段或者处理相关的
	 * @return
	 */
	public static Conditions conditionStrToConditions(String conditionStr,int columnType){
		//wfCaseId = "123",wfCaseId > "123"
		Conditions conds = null;
		if(conditionStr!=null){
			conds = Conditions.newAndConditions();
			String[] conditionStrs = conditionStr.split(",");
			for (int i = 0; i < conditionStrs.length; i++) {
				System.out.println("conditionStrs["+i+"]--->"+conditionStrs[i]);
				if(!"".equals(conditionStrs[i])){
					String[] values = conditionStrs[i].split(" ");
					int coditionInt = conditionToInt(values[1].trim());
					String fieldValue = values[2].trim();
					if(fieldValue!=null&&fieldValue.equals("null"))
						fieldValue = null;
					if(columnType == MWYCoralStaticConfig._BEANNAME)
						conds.add(new Condition(MWYReflectUtils.beanNameToColumn(values[0].trim()), coditionInt, fieldValue));
					else
						conds.add(new Condition(values[0].trim(), coditionInt, fieldValue));
					System.out.println(MWYReflectUtils.beanNameToColumn(values[0].trim())+","+values[1].trim()+","+fieldValue);
				}
			}
			System.out.println("--sql-->"+conds.toString());
		}
		return conds;
	}
	
	
	/**
	 * 将标记符号转化为Conditions的整型
	 * 	返回值为 -1  的时候以此判断是否存在改条件
	 * 	返回值为 -10  的时候以此判断是是否排序
	 * @param keywords
	 * @return
	 */
	public static int conditionToInt(String keywords) {
		int flag = 0;
		if (keywords.equals("=")) {
			flag = 0;
		} else if (keywords.equals(">")) {
			flag = 1;
		} else if (keywords.equals(">=")) {
			flag = 2;
		} else if (keywords.equals("<")) {
			flag = 3;
		} else if (keywords.equals("<=")) {
			flag = 4;
		} else if (keywords.equals("!=")) {
			flag = 5;
		} else if (keywords.equals("LIKE")) {
			flag = 6;
		} else if (keywords.equals("LEFTLike")) {
			flag = 7;
		} else if (keywords.equals("RIGHTLIKE")) {
			flag = 8;
		} else if (keywords.equals("IN")) {
			flag = 9;
		} else if (keywords.equals("NOT IN")) {
			flag = 10;
		} else if (keywords.equals("ORIGINAL LIKE")) {
			flag = 11;
		} else if (keywords.equals("NOT LIKE")) {
			flag = 12;
		} else if (keywords.equals("NOT ORIGINAL LIKE")) {
			flag = 13;
		} else if (keywords.equals("ORDER BY")) {
			flag = -10;//排序
		} else {
			flag = -1;//超出条件，以此判断是否存在改条件
		}
		return flag;
	}
	
	/**
	 * 将OrderHashMap按照顺序转换为insert sql语句
	 * @param map
	 * @param tablename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String setMapToInsertSql(OrderedHashMap map,String tablename){
		StringBuilder sql = new StringBuilder();
		if(map!=null&&!map.isEmpty()){
			sql.append("insert into ").append(tablename);
			StringBuffer names = new StringBuffer();
			StringBuffer values = new StringBuffer();
			for (int i = 0; i < map.size(); i++) {
				if (i > 0) {
					names.append(",");
					values.append(",");
				}
				names.append(map.getKey(i));
				String value = StringUtils.trimToNull(StringUtils.valueOf( map.getValue(i)));
				value = value!=null?"'"+value+"'":"null";
				values.append(value);
			}
			sql.append(" (");
			sql.append(names);
			sql.append(") values(");
			sql.append(values);
			sql.append(") ");
		}
		return sql.toString();
	}
}
