package common.utils.construction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.ReflectUtil;
import net.sf.json.JSONObject;
import wfc.service.util.OrderedHashMap;

import common.utils.log.MWYLog;
import common.utils.string.StringUtils;

/**
 * 反射核心封装核心类
 * @author mywaysaty
 */
public class MWYReflectUtils extends ReflectUtil{
	
	public static final String _PUBLIC_STATIC_FINAL = "public static final";
	public static final String _PRIVATE = "private";
	
	public static final Integer _BEANNAME = 1;
	public static final Integer _COLUMNNAME = 2;
	public static final Integer _UPPER_BEANNAME = 3;
	
	
	public static final String _KEY = "key";
	public static final String _VALUE = "value";
	/**
	 * 通过权限控制符获取满足条件类属性
	 * @param coditions 包含的权限修饰符
	 * @param filterBeanNames 需要过滤掉的属性名
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Field> getAllFields(String[] coditions,String[] filterBeanNames, Class<?> cls){
        Field[] field = cls.getDeclaredFields();
        List<Field>  list = null;
        Map<String, String> coditionsMap = 
        		coditions==null?null:(Map<String, String>)objectToMap(coditions, coditions);
        Map<String, String> filterBeanNamesMap = 
        		filterBeanNames==null?null:(Map<String, String>)objectToMap(filterBeanNames, filterBeanNames);
        
        if(field!=null){
        	if(field.length>0) list = new ArrayList<Field>();
        	for (int i = 0; i < field.length; i++) {
        		// 权限修饰符
        		int mo = field[i].getModifiers();
        		String priv = Modifier.toString(mo);
        		// 属性类型
        		Class<?> type = field[i].getType();
        		if(coditionsMap!=null) 
        			if(coditionsMap.get(priv) ==null ) continue;
        		if(filterBeanNamesMap!=null) 
        			if(filterBeanNamesMap.get(field[i].getName()) !=null) continue;
        		
        		//System.out.println("--mo-->"+mo+", --priv-->"+priv + ", --typeName-->" + type.getName() + ", --fieldName-->"+ field[i].getName() + ";");
        		list.add(field[i]);
        	}
        }
		return list;
	}
	
	
	/**
	 * 通过权限控制符获取满足条件类属性
	 * @param coditions
	 * @param selectBeanNames 指定类属性名
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Field> getSelectFields(String[] coditions,String[] selectBeanNames, Class<?> cls){
        Field[] field = cls.getDeclaredFields();
        List<Field>  list = null;
        Map<String, String> coditionsMap = 
        		coditions==null?null:(Map<String, String>)objectToMap(coditions, coditions);
        Map<String, String> filterBeanNamesMap = 
        	selectBeanNames==null?null:(Map<String, String>)objectToMap(selectBeanNames, selectBeanNames);
        
        if(field!=null){
        	if(field.length>0) list = new ArrayList<Field>();
        	for (int i = 0; i < field.length; i++) {
        		// 权限修饰符
        		int mo = field[i].getModifiers();
        		String priv = Modifier.toString(mo);
        		// 属性类型
        		Class<?> type = field[i].getType();
        		if(coditionsMap!=null) 
        			if(coditionsMap.get(priv) ==null ) continue;
        		if(filterBeanNamesMap!=null) 
        			if(filterBeanNamesMap.get(field[i].getName()) !=null) list.add(field[i]);;
        		//System.out.println("--mo-->"+mo+", --priv-->"+priv + ", --typeName-->" + type.getName() + ", --fieldName-->"+ field[i].getName() + ";");
        	}
        }
		return list;
	}
	
	
	/**
	 * 将静态字符串转换为类属性
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String ColumnToBeanName(String str) {
		String resultStr = "";
		List list = split(str, "_");
		for (int i = 0; i < list.size(); i++) {
			String part = (String) list.get(i);
			if (part.length() >= 1) {
				String firstStr;
				if ("".equals(resultStr))
					firstStr = part.substring(0, 1).toLowerCase();
				else
					firstStr = part.substring(0, 1).toUpperCase();
				String followingStr = part.substring(1, part.length())
						.toLowerCase();
				resultStr = (new StringBuilder(String.valueOf(resultStr)))
						.append(firstStr).append(followingStr).toString();
			}
		}
		return resultStr;
	}
	
	/**
	 * 将类属性转换为数据库字段
	 * @param str
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String beanNameToColumn(String str) {
		String resultStr = "";
		Character character =null;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if(character.valueOf(ch).isLowerCase(ch)){
				resultStr = (new StringBuilder(String.valueOf(resultStr))).append(ch).toString();
			}else{
				resultStr = (new StringBuilder(String.valueOf(resultStr))).append("_"+ch).toString();
			}
		}
		return resultStr.toUpperCase();
	}
	
	@SuppressWarnings("unchecked")
	public static String getClassName(String str) {
		String resultStr = "";
		List list = split(str, "_");
		if(str.indexOf("_", 0)<0){
			//System.out.println("执行单个...");
			String firstStr = str.substring(0, 1).toUpperCase();
			String followingStr = str.substring(1, str.length());
			return firstStr+followingStr;
		}else{
			for (int i = 0; i < list.size(); i++) {
				String part = (String) list.get(i);
				if (part.length() >= 1) {
					String firstStr = part.substring(0, 1).toUpperCase();
					String followingStr = part.substring(1, part.length())
					.toLowerCase();
					resultStr = (new StringBuilder(String.valueOf(resultStr)))
					.append(firstStr).append(followingStr).toString();
				}
			}
			return resultStr;
		}

	}
	/**
	 * 获取类属性 name
	 * @param fields
	 * @return
	 */
	public static List<String> getFieldNamesList(List<Field> fields){
		List<String> list = null;
		if(fields!=null&&fields.size()>0) list = new ArrayList<String>();
		for (Field field : fields) {
			list.add(field.getName());
		}
		return list;
	}
	
	/**
	 * 获取类属性 name
	 * @param fields
	 * @return
	 */
	public static List<String> getFieldNamesList(List<Field> fields,int fieldType){
		List<String> list = null;
		if(fields!=null&&fields.size()>0) list = new ArrayList<String>();
		for (Field field : fields) {
			String value = "";
			if(fieldType==_UPPER_BEANNAME) value = field.getName().replace("_", "").toUpperCase();
			else value = field.getName();
			list.add(value);
		}
		return list;
	}
	public static Map<String,String> getFieldNamesMap(List<Field> fields){
		Map<String,String> map = null;
		if(fields!=null&&fields.size()>0) map = new HashMap<String, String>();
		for (Field field : fields) {
			map.put(field.getName(),field.getName());
		}
		return map;
	}
	
	/**
	 * 得到以filedName去掉下划线并且转换为大写的key，value为满足条件真实值beanName
	 * @param beanNameFieldList
	 * @param columnNameFieldList
	 * @param upCasecolumnNameFieldList
	 * @param keyType  ke的格式 
	 * @param valueType
	 * @return
	 */
	public static Map<String,String> getFieldNamesMap2(List<String> beanNameFieldList,
			List<String> columnNameFieldList, List<String> upCasecolumnNameFieldList, 
			int keyType, int valueType){
		Map<String,String> map = null;
		if(beanNameFieldList!=null&&beanNameFieldList.size()>0){
			map = new HashMap<String, String>();
			for (String beanName : beanNameFieldList) {
				String key = "";
				String value = "";
				int index = upCasecolumnNameFieldList.indexOf(beanName.replace("_", "").toUpperCase());
				if(keyType==_BEANNAME) key = beanName; //原有beanName
				else if(keyType== _COLUMNNAME) key = columnNameFieldList.get(index); //数据库字段对应的
				else if(keyType== _UPPER_BEANNAME) key = upCasecolumnNameFieldList.get(index); //去掉下划线转为大写的bean或者column

				if(valueType==_BEANNAME) value = beanName;
				else if(valueType== _COLUMNNAME) value = columnNameFieldList.get(index);
				else if(valueType== _UPPER_BEANNAME) value = upCasecolumnNameFieldList.get(index);

				map.put(key,value);
			}
		}
		return map;
	}
	
	public static String[] getFieldNamesArray(List<Field> fields){
		return (String[]) getFieldNamesList(fields).toArray(new String[fields.size()]);
	}
	/**
	 * 获取属性与其类型的Map
	 */
	public static Map<String,Class<?>> getFiledNameAndClassMap(List<Field> fields){
		Map<String, Class<?>> map = null;
		if(fields!=null&&fields.size()>0) map = new HashMap<String, Class<?>>();
		for (Field field : fields) {
			map.put(field.getName(),field.getType());
		}
		return map;
		
	}
	
	/**
	 * 通过修饰符条件获取相关属性
	 */
	public static List<String> getBeanNameList(String[] conditions, String[] filterBeanNames, Class<?> cls){
		return getFieldNamesList(getAllFields(conditions, filterBeanNames, cls));
	}
	public static Map<String,String> getBeanNameMap(String[] conditions, String[] filterBeanNames,Class<?> cls){
		return getFieldNamesMap(getAllFields(conditions, filterBeanNames, cls));
	}
	public static String[] getBeanNameArray(String[] conditions, String[] filterBeanNames,Class<?> cls){
		return getFieldNamesArray(getAllFields(conditions, filterBeanNames, cls));
	}
	
	/**
	 * 得到以filedName去掉下划线并且转换为大写的key，value为满足条件真实值beanName
	 * @param
	 * @param
	 * @param cls
	 * @return
	 */
	public static Map<String,String> getSelectFormateBeanMap(Class<?> cls,int keyType,int valueType){
		List<String> beanNameFieldList = getBeanNameList(new String[]{_PRIVATE}, null, cls);
		List<String> columnNameFieldList = getBeanNameList(new String[]{_PUBLIC_STATIC_FINAL}, null, cls);
		List<String> upCasecolumnNameFieldList = new ArrayList<String>();
		for (int i = 0; i < columnNameFieldList.size(); i++) {
			upCasecolumnNameFieldList.add(columnNameFieldList.get(i).replace("_", "").toUpperCase());
		}
		return getFieldNamesMap2(beanNameFieldList,columnNameFieldList,upCasecolumnNameFieldList,keyType,valueType);
	}
	
	/*********************************************************/
	public static List<String> getBeanNameListBySelect(String[] conditions, String[] selectBeanNames, Class<?> cls){
		return getFieldNamesList(getAllFields(conditions, selectBeanNames, cls));
	}
	public static Map<String,String> getBeanNameMapBySelect(String[] conditions, String[] selectBeanNames,Class<?> cls){
		return getFieldNamesMap(getAllFields(conditions, selectBeanNames, cls));
	}
	public static String[] getBeanNameArrayBySelect(String[] conditions, String[] selectBeanNames,Class<?> cls){
		return getFieldNamesArray(getSelectFields(conditions, selectBeanNames, cls));
	}
	
	/**
	 * 通过条件调用指定类方法(自动判断是否有返回值)
	 * 	如果方法参数为空invokeMethod(.. ,.. ,null,null)
	 * @param methodName
	 * @param
	 * @param parameterTypes
	 * @param args
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static Object invokeMethod(String methodName, Object classNameOrCls,
			Class<?>[] parameterTypes, Object[] args) throws Exception {
		Class<?> cls = null;
		Object obj = null;
		if (classNameOrCls!=null&&classNameOrCls.getClass().getName().equals("java.lang.String")) {
			try {
				//System.out.println(classNameOrCls.getClass().getName());
				cls = Class.forName((String) classNameOrCls);
			} catch (ClassNotFoundException e1) {
				throw new Exception("找不到该实体类...");
			}
		} else if (classNameOrCls!=null&&classNameOrCls.getClass().getName().equals(
				"java.lang.Class")) {
			//System.out.println(classNameOrCls.getClass().getName());
			cls = (Class<?>) classNameOrCls;
		} else {
			throw new Exception("实体类ClassNameOrCls参数传入错误...");
		}
		try {
			Method method = cls.getMethod(methodName, parameterTypes);
			if (method.getReturnType().getName().equals("void"))
				method.invoke(cls.newInstance(), args);
			else obj = method.invoke(cls.newInstance(), args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 通过条件调用指定类方法(自动判断是否有返回值)
	 * 	如果方法参数为空invokeMethod(.. ,.. ,null,null)
	 * @param methodName
	 * @param
	 * @param parameterTypes
	 * @param args
	 * @param isThrowException	执行方法时是否抛出异常
	 * @param excuteExceptionMsg 执行时异常替换异常信息（前提是isThrowException = false），如果为null默认打印异常信息
	 * @return
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public static Object invokeMethodSelectExp(String methodName, Object classNameOrCls,
			Class<?>[] parameterTypes, Object[] args,boolean isThrowException,
			String excuteExceptionMsg) throws Exception {
		Class<?> cls = null;
		Object obj = null;
		if (classNameOrCls!=null&&classNameOrCls.getClass().getName().equals("java.lang.String")) {
			try {
				//System.out.println(classNameOrCls.getClass().getName());
				cls = Class.forName((String) classNameOrCls);
			} catch (ClassNotFoundException e1) {
				throw new Exception("找不到该实体类...");
			}
		} else if (classNameOrCls!=null&&classNameOrCls.getClass().getName().equals(
				"java.lang.Class")) {
			//System.out.println(classNameOrCls.getClass().getName());
			cls = (Class<?>) classNameOrCls;
		} else {
			throw new Exception("实体类ClassNameOrCls参数传入错误...");
		}
		if(isThrowException){
			Method method = cls.getMethod(methodName, parameterTypes);
			if (method.getReturnType().getName().equals("void"))
				method.invoke(cls.newInstance(), args);
			else obj = method.invoke(cls.newInstance(), args);
		}else{
			try {
				Method method = cls.getMethod(methodName, parameterTypes);
				if (method.getReturnType().getName().equals("void"))
					method.invoke(cls.newInstance(), args);
				else obj = method.invoke(cls.newInstance(), args);
			} catch (Exception e) {
				if(excuteExceptionMsg==null) e.printStackTrace();
				else MWYLog.error(excuteExceptionMsg);
			}
		}
		return obj;
	}
	
	/**
	 * 通过条件调用指定类方法(自动判断是否有返回值)，自动判断是否需要初始化
	 * 	如果方法参数为空invokeMethod(.. ,.. ,null,null)
	 * @param methodName
	 * @param
	 * @param parameterTypes
	 * @param args
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static <T> Object invokeMethodAutoInstance(String methodName,
			Object classNameOrClsOrobj, Class<?>[] parameterTypes, Object[] args)
			throws Exception {
		Class<?> cls = null;
		Object obj = null;
		Object instanceObj = null;
		if (classNameOrClsOrobj != null) {
			if (classNameOrClsOrobj.getClass().getName().equals(
					"java.lang.String")) {
				try {
					cls = Class.forName((String) classNameOrClsOrobj);
				} catch (ClassNotFoundException e1) {
					cls = classNameOrClsOrobj.getClass();
					instanceObj = classNameOrClsOrobj;
				}
			} else if (classNameOrClsOrobj.getClass().getName().equals("java.lang.Class")) {
				cls = (Class<?>) classNameOrClsOrobj;
			} else {
				cls = classNameOrClsOrobj.getClass();
				instanceObj = classNameOrClsOrobj;
//				MWYLog.info("对象classNameOrClsOrobj传入时已经被实例化过了！");
			}
		}else{
			throw new Exception("实体类ClassNameOrCls参数传入错误...");
		}

		try {
//			MWYLog.info("classNameOrClsOrobj--->cls--->"+cls.getName());
			Method method = cls.getMethod(methodName, parameterTypes);
			boolean isStatic = Modifier.isStatic(method.getModifiers());
//			MWYLog.info("判断方法是否静态的isStatic---->"+isStatic);
			if(!isStatic&&instanceObj==null){
				MWYLog.info("传入对象classNameOrClsOrobj开始实例化......");
				instanceObj = cls.newInstance();
			}
			
			if (method.getReturnType().getName().equals("void")){
				if(isStatic){
					method.invoke(null, args);
				}else
					method.invoke(instanceObj, args);
			}else{
				//判断方法是否静态的
				if(isStatic)
					obj = method.invoke(null, args);
				else
					obj = method.invoke(instanceObj, args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	

	/**
	 * @param obj 操作的对象
	 * @param att 操作的属性
	 * */
	public static Object getter(Object obj, String att) {
		Method method = null;
		try {
			method = obj.getClass().getMethod("get" + getClassName(att));
			//System.out.println(method.invoke(obj));
			return method.invoke(obj);
		} catch (Exception e) {
			//throw new Exception("获取属性值异常...");
			return null;
		}
	}
 
    /**
     * @param obj 操作的对象
     * @param att 操作的属性
     * @param value 设置的值
     * @param type 参数的属性
     * */
    public static void setter(Object obj, String att, Object value,
            Class<?> type) {
        try {
            Method method = obj.getClass().getMethod("set" + getClassName(att), type);
            method.invoke(obj, value);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    /**
     * 返回对象的class
     * @param objects
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Class[] getClasses(Object... objects) {
		if (objects == null) {
			return null;
		}
		Class[] result = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[i] = objects[i].getClass();
			}
		}
		return result;
	}
    
    
    public static Map<String,Object> beanToMap(Object obj){
		return beanToMap(obj,null);
	}
    /**
     * 将bean属性和值转换为Map
     */
	public static Map<String,Object> beanToMap(Object obj,String[] filterBeanNames){
		
		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		Map<String,Object> map = new HashMap<String, Object>();
		//System.out.println("---------------------beanToMap--------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
				//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			map.put(keys[i], getter(obj, keys[i]));
		}
		//System.out.println("---------------------beanToMap-------- end ---------------------");
		return map.isEmpty()?null:map;
		/*return beanToMapBykeyFormat(obj, filterBeanNames, _BEANNAME, false);*/
	}
	
	/**
     * 将bean属性和值转换为Map
     */
	public static Map<String,Object> beanToMapBySelect(Object obj,String[] selectBeanNames){
		
		String[] keys = getBeanNameArrayBySelect(new String[]{MWYReflectUtils._PRIVATE}, selectBeanNames, obj.getClass());
		Map<String,Object> map = new HashMap<String, Object>();
		//System.out.println("---------------------beanToMap--------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
				//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			map.put(keys[i], getter(obj, keys[i]));
		}
		//System.out.println("---------------------beanToMap-------- end ---------------------");
		return map.isEmpty()?null:map;
		/*return beanToMapBykeyFormat(obj, filterBeanNames, _BEANNAME, false);*/
	}
	
	/**
	 * 将bean属性和值转换为Map
	 * @param obj
	 * @param filterBeanNames
	 * @return
	 */
	public static Map<String,Object> beanToMapString(Object obj,String[] filterBeanNames){

		/*String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		Map<String,String> map = new HashMap<String, String>();
		System.out.println("---------------------beanToMapString--------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			if(getter(obj, keys[i])!=null)
				System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			map.put(keys[i], ""+getter(obj, keys[i]));
		}
		System.out.println("---------------------beanToMapString-------- end ---------------------");
		return map.isEmpty()?null:map;*/
		return beanToMapBykeyFormat(obj, filterBeanNames, _BEANNAME,true);
	}
	
	/**
	 * 指定beanName Key
	 * 三种类型 _UPPER_BEANNAME， _COLUMNNAME，_BEANNAME 默认是  _BEANNAME
	 * _COLUMNNAME 慎用
	 * @param obj
	 * @param filterBeanNames
	 * @param key2Type
	 * @param isToString 是否都转换成String
	 * @return
	 */
	public static Map<String,Object> beanToMapBykeyFormat(Object obj,String[] filterBeanNames,Integer key2Type,boolean isToString){

		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		Map<String,Object> map = new HashMap<String, Object>();
		//System.out.println("---------------------beanToMapBykeyFormat--key2Type="+key2Type+", isToString="+false+"------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
				//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			
			Object valueObj = getter(obj, keys[i]);
			
			if(isToString)
				valueObj = String.valueOf(valueObj);
			
			if(key2Type==_UPPER_BEANNAME)
				map.put(keys[i].toUpperCase(), valueObj);
			else if(key2Type==_COLUMNNAME)
				map.put(beanNameToColumn(keys[i]), valueObj);
			else
				map.put(keys[i], valueObj);
		}
		//System.out.println("---------------------beanToMapBykeyFormat-------- end ---------------------");
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 指定beanName Key
	 * key2Type 三种类型 _UPPER_BEANNAME， _COLUMNNAME，_BEANNAME 默认是  _BEANNAME
	 * _COLUMNNAME 慎用
	 * @param obj
	 * @param selectBeanNames 选择的属性
	 * @param key2Type
	 * @param isToString 是否都转换成String
	 * @return
	 */
	public static Map<String,Object> beanToMapBySelectAndkeyFormat(Object obj,String[] selectBeanNames,Integer key2Type,boolean isToString){

		String[] keys = getBeanNameArrayBySelect(new String[]{MWYReflectUtils._PRIVATE}, selectBeanNames, obj.getClass());
		Map<String,Object> map = new HashMap<String, Object>();
		//System.out.println("---------------------beanToMapBySelectAndkeyFormat--key2Type="+key2Type+", isToString="+false+"------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
				//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			
			Object valueObj = getter(obj, keys[i]);
			
			if(isToString)
				valueObj = String.valueOf(valueObj);
			
			if(key2Type==_UPPER_BEANNAME)
				map.put(keys[i].toUpperCase(), valueObj);
			else if(key2Type==_COLUMNNAME)
				map.put(beanNameToColumn(keys[i]), valueObj);
			else
				map.put(keys[i], valueObj);
		}
		//System.out.println("---------------------beanToMapBySelectAndkeyFormat-------- end ---------------------");
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 格式化Map中的value
	 * @param map
	 * @param pointFormateMap key代表map重要格式的value的值，value代表转换后的值
	 */
	public static void formateMapPointValue(Map<String,Object> map,Map<String,Object> pointFormateMap){
		if(map!=null){
			for(String key:map.keySet()){
				if(pointFormateMap.containsKey(map.get(key))){
					map.put(key, pointFormateMap.get(map.get(key)));
				}
			}
		}
	}
	
	/**
	 * 将map中的key格式换成Column,筛选得到新的Map
	 * @param map
	 * @param obj
	 * @param filterColumnNames
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> formateMapKeyBySelectType(Map<String,Object> map,
			Object obj,String[] filterColumnNames,int keyType){
		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterColumnNames, obj.getClass());
		Map<String,String> columnMap = getSelectFormateBeanMap( obj.getClass(),_UPPER_BEANNAME,_COLUMNNAME);
		Map<String,Object> formateResultMap= null;
		if(keys!=null){
			formateResultMap = new HashMap<String, Object>();
			Set filterColumnSet = objectToSet(filterColumnNames);
			
			for (int i = 0; i < keys.length; i++) {
				String beanName = keys[i];
				String upCaseKey = keys[i].replace("_", "").toUpperCase();
				String columnName = columnMap.get(upCaseKey);
				Map<Integer,String> resultFormateMap = objectToMap(
						new Integer[]{_BEANNAME,_COLUMNNAME,_UPPER_BEANNAME},
						new String[]{beanName,columnName,upCaseKey});
				if(filterColumnSet==null||!filterColumnSet.contains(columnName)){
					if(map.containsKey(upCaseKey)){
						formateResultMap.put(resultFormateMap.get(keyType), map.get(upCaseKey));
						//System.out.println(upCaseKey+"******map.containsKey(upCaseKey)********"+map.get(upCaseKey));
					
					}
					if(map.containsKey(beanName)){
						formateResultMap.put(resultFormateMap.get(keyType), map.get(beanName));
						//System.out.println(beanName+"******map.containsKey(beanName)********"+ map.get(beanName));
					}
					if(map.containsKey(columnName)){
						formateResultMap.put(resultFormateMap.get(keyType), map.get(columnName));
						//System.out.println(columnName+"******map.containsKey(columnName)********"+ map.get(columnName));
					}
				}
			}
		}
		MWYLog.info("*****---formateResultMap.size-------->"+formateResultMap.size());
		return formateResultMap;
	}
	
	/**
	 * 格式化list得到list中的对象某些属性的值用fgFlag的值隔开，
	 * 如果为空的时候用replaceNullValue取代它的值
	 * eg:
	 * String replaceNullValue = "**";
	 * String fgFlag = ", ";
	 * Map<String,Object> reqMap =MWYReflectUtils.objectToMap(
	 *		new String[]{WappHpUnit.WF_ADDRESS,WappHpUnit.WF_POST,WappHpUnit.WF_SYSID},
	 *		new String[]{"WFADDRESS","WFPOST","WFSYSID"});//为得到返回值中的Map的对应的key
	 * @param reqMap
	 * @param list
	 * @param fgFlag
	 * @param replaceNullValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getSomeValueByFGFlag(Map<String,Object> reqMap,List list,
			String fgFlag,String replaceNullValue){
		Map<String,Object> resultMap =new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			for (String key:reqMap.keySet()) {
				Object fieldValue = MWYReflectUtils.getter(list.get(i),key);
				String name = (String) reqMap.get(key);//要映射对应的字段名
				String result = (String) resultMap.get(name);
				if(result==null){
					if(fieldValue==null||"".equals(fieldValue))
						resultMap.put(name, replaceNullValue);
					else
						resultMap.put(name, fieldValue);
				}else{
					if(fieldValue==null||"".equals(fieldValue))
						resultMap.put(name, resultMap.get(name)+fgFlag+replaceNullValue);
					else
						resultMap.put(name,resultMap.get(name)+fgFlag + fieldValue);
				}
			}
			
		}
		//MWYReflectUtils.systemPrintMap(resultMap);
		return resultMap;
	}
	
	/**
	 * 将指定的bean放入map里面
	 * @param map
	 * @param obj
	 * @param filterBeanNames
	 * @param key2Type
	 * @param isToString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> putBeanToMap(Map<String,Object> map,Object obj,String[] filterBeanNames,Integer key2Type,boolean isToString){
		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		//System.out.println("---------------------putBeanToMap--key2Type="+key2Type+", isToString="+isToString+"------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
			//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			
			Object valueObj = getter(obj, keys[i]);
			
			if(isToString)
				valueObj = String.valueOf(valueObj);
			
			if(key2Type==_UPPER_BEANNAME)
				map.put(keys[i].toUpperCase(), valueObj);
			else if(key2Type==_COLUMNNAME)
				map.put(beanNameToColumn(keys[i]), valueObj);
			else
				map.put(keys[i], valueObj);
		}
		//System.out.println("---------------------putBeanToMap-------- end ---------------------");
		return map;
		
	}
	
	/**
	 * 将指定的bean放入map里面
	 * @param map
	 * @param obj
	 * @param filterBeanNames
	 * @param filterBeanValue
	 * @param key2Type
	 * @param isToString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> putBeanToMap(Map<String,Object> map,Object obj,String[] filterBeanNames,Object[] filterBeanValue,Integer key2Type,boolean isToString){
		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		Set filterBeanValueSet = objectToSet(filterBeanValue);
		for (int i = 0; i < keys.length; i++) {
			Object valueObj = getter(obj, keys[i]);
			//过滤掉相关值
			if(filterBeanValueSet!=null&&filterBeanValueSet.contains(valueObj)) continue;
			if(isToString)
				valueObj = String.valueOf(valueObj);
			if(key2Type==_UPPER_BEANNAME)
				map.put(keys[i].toUpperCase(), valueObj);
			else if(key2Type==_COLUMNNAME)
				map.put(beanNameToColumn(keys[i]), valueObj);
			else
				map.put(keys[i], valueObj);
		}
		return map;
		
	}
	
	/**
	 * 将指定的bean放入map里面
	 * @param map
	 * @param obj
	 * @param filterBeanNames
	 * @param key2Type
	 * @param isToString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static OrderedHashMap<String,Object> putBeanToOrderedHashMap(OrderedHashMap<String,Object> map,Object obj,String[] filterBeanNames,Integer key2Type,boolean isToString){
		String[] keys = getBeanNameArray(new String[]{MWYReflectUtils._PRIVATE}, filterBeanNames, obj.getClass());
		//System.out.println("---------------------putBeanToMap--key2Type="+key2Type+", isToString="+isToString+"------begin---------------------");
		for (int i = 0; i < keys.length; i++) {
			//if(getter(obj, keys[i])!=null)
			//System.out.println(getter(obj, keys[i]).getClass().getName()+" "+keys[i]+"= " +getter(obj, keys[i])+";");
			
			Object valueObj = getter(obj, keys[i]);
			
			if(isToString)
				valueObj = String.valueOf(valueObj);
			
			if(key2Type==_UPPER_BEANNAME)
				map.put(keys[i].toUpperCase(), valueObj);
			else if(key2Type==_COLUMNNAME)
				map.put(beanNameToColumn(keys[i]), valueObj);
			else
				map.put(keys[i], valueObj);
		}
		//System.out.println("---------------------putBeanToMap-------- end ---------------------");
		return map;
		
	}
	
	/**
	 * toString实体类(一般都是测试调用)
	 */
	public static void systemPrintBean(Object obj){
		List<Field> fieldList = getAllFields(new String[]{"private"}, null, obj.getClass());
		System.out.println("**********systemPrint  "+obj.getClass().getName()+"  *********");
		for (Field field : fieldList) {
			System.out.println(field.getType().getName()+" "+field.getName()+" = "+getter(obj, getClassName(field.getName())));
			//System.out.println(MWYReflectUtils.getClassName(field.getName()));
		}
		System.out.println("********************************************");
	}
	
	/**
	 * toString实体类(一般都是测试调用)
	 */
	public static void systemPrintBean(Object obj,String messageFlag){
		List<Field> fieldList = getAllFields(new String[]{"private"}, null, obj.getClass());
		System.out.println("*******************************>"+messageFlag+"<******begin********************************");
		System.out.println("**********systemPrint  "+obj.getClass().getName()+"  *********");
		for (Field field : fieldList) {
			System.out.println(field.getType().getName()+" "+field.getName()+" = "+getter(obj, getClassName(field.getName())));
			//System.out.println(MWYReflectUtils.getClassName(field.getName()));
		}
		System.out.println("********************************************");
		System.out.println("*******************************>"+messageFlag+"<******end********************************");
		
	}
	
	/**
	 * 将map转换成 net.sf.json.JSONObject
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject mapToJSONObject(Map<String,Object> map) throws Exception{
		JSONObject jsonObject = null;
		if(map!=null&&!map.isEmpty()){
			
			jsonObject = new JSONObject();
			if(map instanceof OrderedHashMap){
				for (int i = 0; i < map.size(); i++) {
					OrderedHashMap<String, Object> orderedHashMap = 
										(OrderedHashMap<String, Object>) map;
					jsonObject.put(orderedHashMap.getKey(i), orderedHashMap
										.get(orderedHashMap.getKey(i)));
					//System.out.println(orderedHashMap.getKey(i)+"-->"+orderedHashMap.get(orderedHashMap.getKey(i)));
					
				}
			}else{
				for(String key:map.keySet()){
					jsonObject.put(key, map.get(key));
				}
			}
			
		}
		return jsonObject;
	}
	
	/**
	 * 将json字符串转换为Map
	 * eg: String jsonString = "{\"dtWorkflowReady\":1346229380343,\"stWorkflowName\":\"test\",\"stWorkflowId\":\"6701f041-b658-4228-898e-0b9942329835\",\"nmWorkflowVer\":1,\"stWorkflowCode\":\"test\"},{\"dtWorkflowReady\":1346229380343,\"stWorkflowName\":\"test\",\"stWorkflowId\":\"6701f041-b658-4228-898e-0b9942329835\",\"nmWorkflowVer\":1,\"stWorkflowCode\":\"test\"}";
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map toMap(String jsonString) throws Exception {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        //MWYReflectUtils.systemPrintMap(result);
        return result;
    }
	
	
	/**
	 * 将json字符串转换为OrderedHashMap
	 * eg: String jsonString = "{\"dtWorkflowReady\":1346229380343,\"stWorkflowName\":\"test\",\"stWorkflowId\":\"6701f041-b658-4228-898e-0b9942329835\",\"nmWorkflowVer\":1,\"stWorkflowCode\":\"test\"},{\"dtWorkflowReady\":1346229380343,\"stWorkflowName\":\"test\",\"stWorkflowId\":\"6701f041-b658-4228-898e-0b9942329835\",\"nmWorkflowVer\":1,\"stWorkflowCode\":\"test\"}";
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static OrderedHashMap toOrderedHashMap(String jsonString) throws Exception {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
		OrderedHashMap result = new OrderedHashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        //MWYReflectUtils.systemPrintMap(result);
        return result;
    }
	
	/**
	 * 打印map
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public static void systemPrintMap(Map map){

		Set keSet=map.entrySet();  
		System.out.println("--------------------------systemPrintMap-----begin-------------------------------------");
		for(Iterator itr=keSet.iterator();itr.hasNext();){  
			Map.Entry me=(Map.Entry)itr.next();  
			Object key=me.getKey();  
			Object value =me.getValue();  
			Object[] values =new String[1];
			if(value instanceof Map){
				Map tempMap = (Map)value;
				for (Object objKey : tempMap.keySet()) {
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap***parentMap**pkey= "+key+"***childMap**name= "+objKey+", value= "+tempMap.get(objKey));
				}
			}else{
				if(value instanceof String[]){  
					values=(String[])value;
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap**String[]**name= "+key+", value= "+Arrays.toString(values));  
				}else if(value instanceof List){
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap****List****name= "+key+", value= "+((List)value).toString());  
				}else{
					values[0]= value==null?null: value.toString();  
					//values[0]= value;
					for(int k=0;k<values.length;k++){  
						System.out.println("****MWYReflectUtils--Method-->systemPrintMap********name= "+key+", value= "+values[k]);  
					} 
				}  
			}

		}
		System.out.println("--------------------------systemPrintMap------end--------------------------------------");
	}
	
	/**
	 * 带分割线的打印map，messageFlag提示信息开始分割线
	 * @param map
	 * @param messageFlag
	 */
	@SuppressWarnings("unchecked")
	public static void systemPrintMap(Map map,String messageFlag){
		System.out.println("*******************************>"+messageFlag+"<******begin********************************");
		Set keSet=map.entrySet();  
		System.out.println("--------------------------systemPrintMap-----begin-------------------------------------");
		for(Iterator itr=keSet.iterator();itr.hasNext();){  
			Map.Entry me=(Map.Entry)itr.next();  
			Object key=me.getKey();  
			Object value =me.getValue();  
			Object[] values =new String[1];
			if(value instanceof Map){
				Map tempMap = (Map)value;
				for (Object objKey : tempMap.keySet()) {
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap***parentMap**pkey= "+key+"***childMap**name= "+objKey+", value= "+tempMap.get(objKey));
				}
			}else{
				if(value instanceof String[]){  
					values=(String[])value;
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap**String[]**name= "+key+", value= "+Arrays.toString(values));  
				}else if(value instanceof List){
					System.out.println("****MWYReflectUtils--Method-->systemPrintMap****List****name= "+key+", value= "+((List)value).toString());  
				}else{
					values[0]= value==null?null: value.toString();  
					//values[0]= value;
					for(int k=0;k<values.length;k++){  
						System.out.println("****MWYReflectUtils--Method-->systemPrintMap********name= "+key+", value= "+values[k]);  
					} 
				}  
			}

		}
		System.out.println("--------------------------systemPrintMap------end--------------------------------------");
		System.out.println("*******************************>"+messageFlag+"<******end********************************");
		
	}
	
	@SuppressWarnings("unchecked")
	public static void systemPrintOrderedHashMap(OrderedHashMap orderHashMap){
		systemPrintOrderedHashMap(orderHashMap, null);
	}
	
	/**
	 * 打印任意匹配数据
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public static void systemPrintObject(Object obj,String messageFlag){
		if(obj instanceof Map){
			systemPrintMap((Map)obj, messageFlag);
		}else if(obj instanceof OrderedHashMap){
			systemPrintOrderedHashMap((OrderedHashMap)obj, messageFlag);
		}else{
			systemPrintBean(obj, messageFlag);
		}
	}
	
	/**
	 * 打印任意匹配数据
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public static void systemPrintObject(Object obj){
		if(obj instanceof Map){
			systemPrintMap((Map)obj);
		}else if(obj instanceof OrderedHashMap){
			systemPrintOrderedHashMap((OrderedHashMap)obj);
		}else{
			systemPrintBean(obj);
		}
	}
	
	/**
	 * 带分割线的打印map，messageFlag提示信息开始分割线
	 * @param orderHashMap
	 * @param messageFlag
	 */
	@SuppressWarnings("unchecked")
	public static void systemPrintOrderedHashMap(OrderedHashMap orderHashMap,String messageFlag){
		if(messageFlag!=null)
			System.out.println("*******************************>"+messageFlag+"<******begin********************************");
		System.out.println("--------------------------systemPrintOrderedHashMap-----begin-------------------------------------");
		if(orderHashMap!=null){
			for (int i = 0; i < orderHashMap.size(); i++) {
				Object key=orderHashMap.getKey(i);  
				Object value =orderHashMap.getValue(i);  
				Object[] values =new String[1];
				if(value instanceof Map){
					Map tempMap = (Map)value;
					for (Object objKey : tempMap.keySet()) {
						System.out.println("****MWYReflectUtils--Method-->systemPrintOrderedHashMap***parentMap**pkey= "+key+"***childMap**name= "+objKey+", value= "+tempMap.get(objKey));
					}
				}else{
					if(value instanceof String[]){  
						values=(String[])value;
						System.out.println("****MWYReflectUtils--Method-->systemPrintOrderedHashMap**String[]**name= "+key+", value= "+Arrays.toString(values));  
					}else if(value instanceof List){
						System.out.println("****MWYReflectUtils--Method-->systemPrintOrderedHashMap****List****name= "+key+", value= "+((List)value).toString());  
					}else{
						values[0]= value==null?null: value.toString();  
						//values[0]= value;
						for(int k=0;k<values.length;k++){  
							System.out.println("****MWYReflectUtils--Method-->systemPrintOrderedHashMap********name= "+key+", value= "+values[k]);  
						} 
					}  
				}
				
			}
		}
		System.out.println("--------------------------systemPrintOrderedHashMap------end--------------------------------------");
		System.out.println("*******************************>"+messageFlag+"<******end********************************");
		
	}
	
   /**
    * 通过map修改Bean属性值
    * @param obj
    * @param map
    * @return
    */
    public static Object setBeanByMap(Object obj, Map<String,Object> map){
    	if(map!=null&&!map.isEmpty()){
			Map<String, Class<?>> beanNameClassMap = MWYReflectUtils
					.getFiledNameAndClassMap(MWYReflectUtils.getAllFields(
							new String[] { "private" }, null, obj.getClass()));
    		for (String key : beanNameClassMap.keySet()) {
    			
    			Object valueObj = map.get(key);
    			if(valueObj ==null)
    				valueObj = map.get(key.toUpperCase());
    			System.out.println("------------key="+key+"-----value="+valueObj);
    			if(valueObj!=null){
    				//判断类型是否匹配
    				if(!valueObj.getClass().equals(beanNameClassMap.get(key))){//常用类型自动转换
    					//map.put(key,objectToAnyType(valueObj, beanNameClassMap.get(key)));
    					setter(obj, key, objectToAnyType(valueObj, beanNameClassMap.get(key)), beanNameClassMap.get(key));
    					continue;
    				}
    				setter(obj, key, valueObj, beanNameClassMap.get(key));
    				//System.out.println("---已设置为-getter-->"+getter(obj, key));
    			}
			}
    	}
		return obj;
    }
    
    /**
     * 通过map修改Bean属性值
     * @param obj
     * @param map
     * @return
     */
     public static Object setBeanByMap(Object obj, Map<String,Object> map,int valueType){
     	if(map!=null&&!map.isEmpty()){
 			Map<String, Class<?>> beanNameClassMap = MWYReflectUtils
 					.getFiledNameAndClassMap(MWYReflectUtils.getAllFields(
 							new String[] { "private" }, null, obj.getClass()));
 			Map<String, String> beanColumnFieldMap = getSelectFormateBeanMap(obj.getClass(), _BEANNAME, valueType);
     		for (String key : beanNameClassMap.keySet()) {
     			Object valueObj = map.get(beanColumnFieldMap.get(key));
     			MWYLog.info("--key="+key+"--value="+valueObj);
     			if(valueObj!=null){
     				//判断类型是否匹配
     				if(!valueObj.getClass().equals(beanNameClassMap.get(key))){//常用类型自动转换
     					setter(obj, key, objectToAnyType(valueObj, beanNameClassMap.get(key)), beanNameClassMap.get(key));
     					continue;
     				}
     				setter(obj, key, valueObj, beanNameClassMap.get(key));
     			}
 			}
     	}
 		return obj;
     }
     
     /**
      * 通过数组将数组中对应的0,1 2,3 4,5...set到obj中，数组中偶数坐标是_COLUMNNAME
      * @param obj
      * @param beanName$values
      * @return
      */
     @SuppressWarnings("unchecked")
	public static Object setBeanByArrays(Object[] beanName$values,Object obj){
    	 return setBeanByMap(obj, MWYReflectUtils.objectToMap(beanName$values),_COLUMNNAME);
     }
     
     /**
      * 通过数组将数组中对应的0,1 2,3 4,5...set到obj中
      * @param obj
      * @param beanName$values
      * @param valueType  _BEANNAME, _COLUMNNAME,_UPPER_BEANNAME
      * @return
      */
     @SuppressWarnings("unchecked")
	public static Object setBeanByArrays(Object[] beanName$values,Object obj,Integer valueType){
    	 return setBeanByMap(obj, MWYReflectUtils.objectToMap(beanName$values), valueType);
     }
     
	/*--------------------------------其它所需方法---------------------------------------------*/
    /**
     * @param str
     * @param ch
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List split(String str, String ch) {
		if (str == null)
			return null;
		ArrayList al = new ArrayList();
		str = (new StringBuilder(String.valueOf(str))).append(ch).toString();
		int begin = 0;
		for (int end = str.indexOf(ch, begin); end >= 0; end = str.indexOf(ch,
				begin)) {
			al.add(str.substring(begin, end));
			begin = end + ch.length();
		}
		return al;
	}
	
	/**
	 * object数组转换为Map,前提是keys 和values数组长度必须一样，否则返回为null
	 * @param keys
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map objectToMap(Object[] keys,Object[] values){
		Map map = null;
		if(keys!=null&&values!=null){
			if(keys.length==values.length){
				map = new HashMap();
				for (int i = 0; i < keys.length; i++) {
					map.put(keys[i], values[i]);
				}
			}
		}
		return map;
	}
	
	/**
	 * 将固定的数组的格式转换为Map
	 * keyANDvalues格式为  
	 * new Object[]{key1,value1,key2,value2...}
	 * @param keyANDvalues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map objectToMap(Object[] keyANDvalues){
		Map map = null;
		if(keyANDvalues!=null){
			map = new HashMap();
			for (int i = 0; i < keyANDvalues.length; i+=2) {
				map.put(keyANDvalues[i], keyANDvalues[i+1]);
			}
		}
		systemPrintObject(map);
		return map;
	}
	
	/**
	 * 将固定的数组的格式转换为Map
	 * keyANDvalues格式为  
	 * new Object[]{key1,value1,key2,value2...}
	 * @param keyANDvalues
	 * @param filterValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map objectToMapFilerValue(Object[] keyANDvalues,Object[] filterValues){
		Map map = null;
		if(keyANDvalues!=null){
			map = new HashMap();
			List filterValuesList = null;
			for (int i = 0; i < keyANDvalues.length; i+=2) {
				if(filterValues!=null&&filterValues.length>0){
					if(filterValuesList==null)
						filterValuesList = Arrays.asList(filterValues);
					if(!filterValuesList.contains(keyANDvalues[i+1]))
						map.put(keyANDvalues[i], keyANDvalues[i+1]);
				}else{
					map.put(keyANDvalues[i], keyANDvalues[i+1]);
				}
			}
		}
//		systemPrintObject(map);
		return map;
	}
	
	/**
	 * 将固定的数组的格式转换为Map
	 * keyANDvalues格式为  
	 * new Object[]{key1,value1,key2,value2...}
	 * @param keyANDvalues
	 * @param filterValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static OrderedHashMap objectToOrderedHashMapFilerValue(Object[] keyANDvalues,Object[] filterValues){
		OrderedHashMap map = null;
		if(keyANDvalues!=null){
			map = new OrderedHashMap();
			List filterValuesList = null;
			for (int i = 0; i < keyANDvalues.length; i+=2) {
				if(filterValues!=null&&filterValues.length>0){
					if(filterValuesList==null)
						filterValuesList = Arrays.asList(filterValues);
					if(!filterValuesList.contains(keyANDvalues[i+1]))
						map.put(keyANDvalues[i], keyANDvalues[i+1]);
				}else{
					map.put(keyANDvalues[i], keyANDvalues[i+1]);
				}
			}
		}
//		systemPrintObject(map);
		return map;
	}
	
	public static final Integer _objectToMap_GROUPCOUNT_2 = 2;//默认值为2的一个key一个value的数组
	public static final Integer _objectToMap_GROUPCOUNT_3 = 3;//三个条件的Map常适用条件查询
	/**
	 * 将固定的数组的格式转换为Map
	 * keyANDvalues格式为  
	 * new Object[]{key1,value1,key2,value2...}
	 * 或者new Object[]{key1,singn,value1,key2,singn2,value2...}
	 * @param keyANDvalues
	 * @param groupCount 目前的值只能为2或者3
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map objectToMap(Object[] keyANDvalues,Integer groupCount){
		Map map = null;
		if(keyANDvalues!=null){
			map = new HashMap();
			groupCount = groupCount==null?2:groupCount;
			MWYLog.info("groupCount="+groupCount);
			for (int i = 0; i < keyANDvalues.length; i+=groupCount) {
				if(groupCount==3)
					map.putAll(put_key_sign_valueToMap(keyANDvalues[i], keyANDvalues[i+1], keyANDvalues[i+2]));
				else map.put(keyANDvalues[i], keyANDvalues[i+1]);
			}
		}
		systemPrintObject(map);
		return map;
	}
	
	public static final String _specialObjectToMap_Three_Map = "[KEY1,SIGN1,VALUE1,KEY2,SIGN2,VALUE3...]";//三个条件的Map常适用条件查询
	@SuppressWarnings("unchecked")
	public static Map specialObjectToMap(Object[] keyANDvalues){
		Map map = null;
		if(keyANDvalues!=null){
			map = new HashMap();
			for (int i = 0; i < keyANDvalues.length; i+=2) {
				Object key = keyANDvalues[i];
				Object value = keyANDvalues[i+1];
				if(_specialObjectToMap_Three_Map.equals(key)&&value instanceof Object[])
					map.putAll(objectToMap((Object[])value, _objectToMap_GROUPCOUNT_3));
				else map.put(key, keyANDvalues[i+1]);
			}
		}
		return map;
	}
	
	/**
	 *  object数组转换为Set
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set objectToSet(Object[] fields) {
		Set sets = null;
		if (fields != null) {
			sets = new HashSet();
			for (int i = 0; i < fields.length; i++) {
				sets.add(fields[i]);
			}
		}
		return sets;
	}
	
	/**
	 * object数组转换为OrderedHashMap,前提是keys 和values数组长度必须一样，否则返回为null
	 * @param keys
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static OrderedHashMap objectToOrderedHashMap(Object[] keys,Object[] values){
		OrderedHashMap map = null;
		if(keys!=null&&values!=null){
			if(keys.length==values.length){
				map = new OrderedHashMap();
				for (int i = 0; i < keys.length; i++) {
					map.put(keys[i], values[i]);
				}
			}
		}
		return map;
	}
	/**
	 * object数组转换为Map,前提是keys 和values数组长度必须一样，否则返回为null
	 * @param keys
	 * @param values
	 * @param filterValues //过滤掉的obj的value  null,""??
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map objectToMap(Object[] keys,Object[] values,Object[] filterValues){
		Map map = null;
		if(keys!=null&&values!=null){
			if(keys.length==values.length){
				map = new HashMap();
				List filterValuesList = null;
				for (int i = 0; i < keys.length; i++) {
					if(filterValues!=null&&filterValues.length>0){
						if(filterValuesList==null)
							filterValuesList = Arrays.asList(filterValues);
						if(!filterValuesList.contains(values[i]))
							map.put(keys[i], values[i]);
					}
					/*for (int j = 0; j < filterValues.length; j++) {
						if(values[i]==filterValues[j]) break;
						if(j==(filterValues.length-1)) map.put(keys[i], values[i]);
						
					}*/
					
				}
			}
		}
		if(map.isEmpty()) return null;
		return map;
	}
	
	/**
	 * 将某个不确定的值转换为List
	 * @param obj
	 * @return
	 */
	public static Object objORobjsToList(Object obj){
		if(obj!=null){
			if(obj instanceof Object[]){
				return Arrays.asList((Object[]) obj);
			}else{
				if("".equals(obj))return obj;
				return Arrays.asList(new Object[]{obj} );
			}
		}
		return null;
	}
	/**
	 * 将某个不确定的值转换为List
	 * @param obj
	 * @param filterValues 过滤数组中的value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object objORobjsToList(Object obj,String... filterValues){
		if(obj!=null){
			List list = null;
			List filterValuesList = null;
			if(obj instanceof Object[]){
				Object[] objs = (Object[]) obj;
				list = new ArrayList();
				for (int i = 0; i < objs.length; i++) {
					if(filterValues!=null){
						if(filterValuesList==null)//此处用list存储特殊值
							filterValuesList = Arrays.asList(filterValues);
						if(!filterValuesList.contains(objs[i]))
							list.add(objs[i]);
					}
				}
				if(list.size()>0) return list;
				else return null;
			}else{
				if("".equals(obj))return obj;
				return Arrays.asList(new Object[]{obj} );
			}
		}
		return null;
	}
	
	
	/**********************************Map 操作 begin**********************************************/
	/**
	 * 适用于数据库，存放条件的map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> put_keys_signs_valuesToMap(String[] keys,String[] signs, Object[] values){
		Map<String,Object> map =objectToMap(keys, ArraysToMapArrays(signs, values));
		return map.isEmpty()?null:map;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> put_key_sign_valueToMap(String key,String sign, Object value){
		Map<String,Object> map =objectToMap(new String[]{key}, ArraysToMapArrays(new String[]{sign}, new Object[]{value}));
		return map.isEmpty()?null:map;
	}
	@SuppressWarnings("unchecked")
	public static Map<String,Object> put_key_sign_valueToMap(Object key,Object sign, Object value){
		Map<String,Object> map =objectToMap(new Object[]{key}, ArraysToMapArrays(new Object[]{sign}, new Object[]{value}));
		return map.isEmpty()?null:map;
	}
	
	public static Map<String,Object> put_key_signs_valuesToMap(String key,String[] signs, Object[] values){
		Map<String,Object> map =new HashMap<String, Object>();
		map.put(key, objectToMap(signs, values));
		return map.isEmpty()?null:map;
	}
	public static Map<String,Object> put_key_sign_valuesToMap(String key,String sign, Object... values){
		Map<String,Object> map =new HashMap<String, Object>();
		Map<String,Object> childMap =new HashMap<String, Object>();
		childMap.put(sign, values);
		map.put(key, childMap);
		return map.isEmpty()?null:map;
	}
	
	public static Map<String,Object> put_key_sign_valuesToMap(String key,String sign, Collection values){
		Map<String,Object> map =new HashMap<String, Object>();
		Map<String,Object> childMap =new HashMap<String, Object>();
		childMap.put(sign, values);
		map.put(key, childMap);
		return map.isEmpty()?null:map;
	}
	
	/**
	 * 将OrderedHashMap转换为List
	 * @param <T>
	 * @param map
	 * @param keyOrValue    值为 key或者value
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> orderedHashMapToList(OrderedHashMap map,
			String keyOrValue) throws Exception {
		return orderedHashMapToList(map, keyOrValue, null, null);
	}
	/**
	 * 将OrderedHashMap转换为List,调用指定类方法处理list中将要保存的值
	 * @param <T>
	 * @param map
	 * @param keyOrValue
	 * @param classNameOrCls
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> orderedHashMapToList(OrderedHashMap map,
			String keyOrValue, Object classNameOrCls, String methodName)
			throws Exception {
		List<T> list = new ArrayList<T>();
		if (map != null) {
			for (int i = 0; i < map.size(); i++) {
				T t = null;
				if ("KEY".equals(keyOrValue == null ? "" : keyOrValue.trim().toUpperCase())) {
					t = (T) map.getKey(i);
				} else {
					t = (T) map.getValue(i);
				}
				if (methodName != null && classNameOrCls != null) {
//					t = (T) MWYReflectUtils.invokeMethod(methodName,
//							classNameOrCls, new Class<?>[] { String.class },
//							new Object[] { t });
					t = (T) MWYReflectUtils.invokeMethodAutoInstance(methodName,
							classNameOrCls, new Class<?>[] { t.getClass() },
							new Object[] { t });
				}
				list.add(t);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 将Map的key或者value装换成数组
	 * @param map
	 * @param keyORvalue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object[] MapToArrays(Map map, String keyORvalue){
		List list = new ArrayList();
		if(map!=null){
			for(Object key : map.keySet()){
				if(keyORvalue.equals(_KEY)){
					list.add(key);
				}else if(keyORvalue.equals(_VALUE)){
					list.add(map.get(key));
				}
			}
		}
		if(list.size()>0) return list.toArray();
		return null;
	}
	
	/**
	 * keys values都存放到多个map
	 * @param keys
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object[] ArraysToMapArrays(Object[] keys, Object[] values){
		List list = new ArrayList();
		Map map = null;
		if(keys!=null&&values!=null){
			if(keys.length==values.length){
				for (int i = 0; i < keys.length; i++) {
					map = new HashMap();
					map.put(keys[i], values[i]);
					list.add(map);
				}
			}
		}
		if(list.size()>0) return list.toArray();
		return null;
	}
	/**
	 * keys values都存放到一个map
	 * @param keys
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object[] ArraysToMapArray(Object[] keys, Object[] values){
		List list = new ArrayList();
		Map map = null;
		if(keys!=null&&values!=null){
			if(keys.length==values.length){
				map = new HashMap();
				for (int i = 0; i < keys.length; i++) {
					map.put(keys[i], values[i]);
				}
				list.add(map);
			}
		}
		if(list.size()>0) return list.toArray();
		return null;
	}
	
	/**
	 * 获取Map 的中间值 集sign
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getMapSign(Map map,String key){
		Object obj = map.get(key);
		List list = new ArrayList();
		if(obj instanceof Map){
			Map tempMap = (Map)obj;
			if(tempMap!=null&&!tempMap.isEmpty()){
				Set set = tempMap.keySet();
				if(set.size()==1){
					for(Object objKey :tempMap.keySet()){
						return objKey;
					}
				}else{
					for(Object objKey :tempMap.keySet()){
						list.add(objKey);
					}
					
				}
			}
		}
		if(list.size()>0) return list.toArray();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Object getMapSignValue(Map map,String key){
		Object obj = map.get(key);
		List list = new ArrayList();
		if(obj instanceof Map){
			Map tempMap = (Map)obj;
			if(tempMap!=null&&!tempMap.isEmpty()){
				Set set = tempMap.keySet();
				if(set.size()==1){
					for(Object objKey :tempMap.keySet()){
						return objKey;
					}
				}else{
					for(Object objKey :tempMap.keySet()){
						list.add(tempMap.get(objKey));
					}
					
				}
			}
		}
		if(list.size()>0) return list.toArray();
		return null;
	}
	
	
	
	/*************************************Map 操作 end*******************************************/
	
	
	/**
	 * 将map转换成list 其中     String[]{key,value}
	 * @param map
	 * @return
	 */
	public static List<String[]> mapToList(Map<String,Object> map){
		List<String[]> list = new ArrayList<String[]>();
		if(map!=null&&!map.isEmpty()){
			for(String key:map.keySet()){
				String value = String.valueOf(map.get(key));
				list.add(new String[]{key,value});
			}
		}
		return list.size()>0?list:null;
	}
	
	
	
	/**
	 * 自动转换常用类型，如果不在条件内的返回本身 要不断完善
	 * 
	 * @param obj
	 * @param toClass
	 *            //需要转换成的类型
	 * @return
	 */
	public static Object objectToAnyType(Object obj, Class toClass) {
//		MWYLog.info("**objectToAnyType-->**待转的obj.class=" + obj.getClass() + " ,要转换成的toClass.class=" + toClass);
		try {
			if (!obj.getClass().equals(toClass)) {
//			if (obj.getClass().equals(String.class) && !"".equals(obj)) {
				if (obj instanceof String && !"".equals(obj)){
					if (toClass.equals(int.class)){
//					MWYLog.info(obj.getClass()+"--->"+int.class);
						obj = Integer.parseInt(String.valueOf(obj));
					}else if (toClass.equals(Integer.class)){
//					MWYLog.info(obj.getClass()+"--->"+Integer.class);
						obj = Integer.valueOf(String.valueOf(obj));
					}else if (toClass.equals(Long.class)){
//					MWYLog.info(obj.getClass()+"--->"+Long.class);
						obj = Long.valueOf(String.valueOf(obj));
					}else if (toClass.equals(long.class)){
//					MWYLog.info(obj.getClass()+"--->"+long.class);
						obj = Long.parseLong(String.valueOf(obj));
					}else if (toClass.equals(Float.class)){
//					MWYLog.info(obj.getClass()+"--->"+Float.class);
						obj = Float.valueOf(String.valueOf(obj));
					}else if (toClass.equals(float.class)){
//					MWYLog.info(obj.getClass()+"--->"+float.class);
						obj = Float.parseFloat(String.valueOf(obj));
					}else if (toClass.equals(Double.class)){
//					MWYLog.info(obj.getClass()+"--->"+Float.class);
						obj = Double.valueOf(String.valueOf(obj));
					}else if (toClass.equals(BigDecimal.class)){
//					MWYLog.info(obj.getClass()+"--->"+BigDecimal.class);
						obj = BigDecimal.valueOf(Double.valueOf(String.valueOf(obj)));
					}else if (toClass.equals(Timestamp.class)) {
						try {
							Long dateTime = Long.parseLong(String.valueOf(obj));
							obj = new Timestamp(dateTime);
						} catch (NumberFormatException e) { 
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								obj = new Timestamp(sdf.parse(String.valueOf(obj)).getTime());
							} catch (ParseException ex) {
								try {
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									obj = new Timestamp(sdf.parse(String.valueOf(obj)).getTime());
								} catch (ParseException exx) {
									throw new RuntimeException(exx);
								}
							}
							
						}
					}else if(toClass.equals(Date.class)){
						try {
							Long dateTime = Long.parseLong(String.valueOf(obj));
							obj =new Date(dateTime);
						} catch (NumberFormatException e) { 
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								obj = new Date(sdf.parse(String.valueOf(obj)).getTime());
							} catch (ParseException ex) {
								try {
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									obj = new Date(sdf.parse(String.valueOf(obj)).getTime());
								} catch (ParseException exx) {
									throw new RuntimeException(exx);
								}
							}
						}
					}
				}else{
					String valueStr = StringUtils.trimToEmpty(obj);
					if(toClass.equals(String.class)){
//					MWYLog.info(obj.getClass()+"--->"+String.class);
						obj = StringUtils.valueOf(obj);
					}else if (toClass.equals(BigDecimal.class)&&!"".equals(valueStr)){
						MWYLog.info(obj.getClass()+"--->"+BigDecimal.class);
						if(obj instanceof Integer || obj instanceof Long)
							obj = BigDecimal.valueOf(Long.valueOf(String.valueOf(obj)));
						else
							obj = BigDecimal.valueOf(Double.valueOf(String.valueOf(obj)));
					}else if (toClass.equals(Integer.class)&&!"".equals(valueStr)){
//					MWYLog.info(obj.getClass()+"--->"+Integer.class);
						obj = Integer.valueOf(String.valueOf(obj));
					}else if (toClass.equals(Long.class)&&!"".equals(valueStr)){
//					MWYLog.info(obj.getClass()+"--->"+Long.class);
						obj = Long.valueOf(String.valueOf(obj));
					}else if (toClass.equals(Float.class)&&!"".equals(valueStr)){
//					MWYLog.info(obj.getClass()+"--->"+Float.class);
						obj = Float.valueOf(String.valueOf(obj));
					}else if (toClass.equals(Double.class)&&!"".equals(valueStr)){
//					MWYLog.info(obj.getClass()+"--->"+Float.class);
						obj = Double.valueOf(String.valueOf(obj));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MWYLog.error("类型转换失败，返回为null...");
			obj = null;
		}
		//MWYLog.info("---已转换为--->" + obj + " ,已转换成的toClass.class="+ obj.getClass());
		return obj;
	}

	/**
	 * 比较fromMap 和replaceMap， replaceMap中没有fromMap其中的某些元素，将添加上
	 * 如果replaceFlag=true,replaceMap将替换掉已存在的key对应的value
	 * 如果replaceFlag=false,replaceMap将不替换掉已存在的key对应的value
	 * 
	 * @param fromMap
	 * @param replaceMap
	 * @param replaceFlag
	 */
	@SuppressWarnings("unchecked")
	public static void fromMap1replaceMap2(Map fromMap, Map replaceMap,
			boolean replaceFlag) {
		if (fromMap != null) {
			for (Object fromKey : fromMap.keySet()) {
				if (replaceMap.containsKey(fromKey)) {
					if (replaceFlag){
						
						replaceMap.put(fromKey, fromMap.get(fromKey)==null?"":fromMap.get(fromKey));
						System.out.println("************fromMap1replaceMap2*****replaceFlag="+replaceFlag+"******--replaceMap已替换------->key="+fromKey+", value="+fromMap.get(fromKey));
						
					}
				} else{
					replaceMap.put(fromKey, fromMap.get(fromKey)==null?"":fromMap.get(fromKey));
					System.out.println("************fromMap1replaceMap2*****replaceFlag="+replaceFlag+"******--replaceMap已添加------->key="+fromKey+", value="+fromMap.get(fromKey));
				}
			}
		}
	}
	
	/**
	 * fromMap 移除所有removeMap里面的元素得到一个新的字Map
	 * @param fromMap
	 * @param removeMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map fromMap1removeMap2(Map fromMap, Map removeMap) {
		Map map = null;
		if (fromMap != null&&removeMap!=null) {
			map = new HashMap();
			for (Object fromKey : fromMap.keySet()) {
				if (!removeMap.containsKey(fromKey)) 
					map.put(fromKey, fromMap.get(fromKey));
			}
		}else if(fromMap != null&&removeMap==null) return fromMap;

		return map!=null&&map.isEmpty()?null:map;
	}
	
	/**
	 * 在map中移除指定的key
	 * @param map
	 * @param pointKeys 可以位String的数组 数组中的元素多个参数用指定的符号隔开
	 */
	public static void removePointKeys(Map map,Object pointKeys,String regex){
		
		if(pointKeys!=null){
			if(pointKeys instanceof String[]){
				String[] pointKeysStrs = (String[]) pointKeys;
				if(pointKeysStrs!=null){
					for (int i = 0; i < pointKeysStrs.length; i++) {
						removePointKeys(map, pointKeysStrs[i], regex);
					}
				}
				
			}else if(pointKeys instanceof String){
				removePointKeys(map, (String)pointKeys, regex);
			}
		}
		
	}
	/**
	 * 移除指定 在map中移除指定的key
	 * @param map
	 * @param pointKey
	 * @param regex
	 */
	@SuppressWarnings("unchecked")
	public static void removePointKeys(Map map,String pointKey,String regex){
		if(pointKey!=null){
			String[] pointKeys = pointKey.split(regex);
			if(pointKeys!=null){
				for (int i = 0; i < pointKeys.length; i++) {
					String key =pointKeys[i];
					if(key!=null&&!key.equals("")){
						map.remove(key.trim());
						//System.out.println("****MWYReflectUtils*******removePointKeys*****已移除Key*************----->"+key.trim());
					}
				}
				
			}
		}
	}
	
	/**
	 * 得到选择的子集Map
	 * @param map
	 * @param selectKeys
	 * @return
	 */
	public static Map<String,Object> getMapbySelectKeys(Map<String,Object> map,String... selectKeys){
		Map<String,Object> selectMap = new HashMap<String, Object>();
		if(selectKeys!=null){
			for (int i = 0; i < selectKeys.length; i++) {
				selectMap.put(selectKeys[i],map.get(selectKeys[i]));
			}
		}
		return selectMap;
	}
	
	/**
	 * 从map中找到指定的key对应的value返回
	 * @param map
	 * @param keys
	 * @return
	 */
	public static String[] mapGetValueByStrs(Map map, String[] keys){
		String[] values = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			values[i] = (String) map.get(keys[i]);
		}
		return values;
	}
	
}
