package common.utils.string;

import java.util.Map;



/**
 * @author mywaystay
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{
	
	
	/**
	 * 将obj转换为String，如果为null的话返回null,
	 * 区别于String.valueOf(obj)-->[obj为null的时候返回"null"字符串]
	 * @param obj
	 * @return
	 */
	public static String valueOf(Object obj){
		return obj==null?null:String.valueOf(obj);
	}

	/**
	 * 得到map中key对应的值，如果为null返回"",其他的转换为String
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String trim(Map map,Object key){
		return trimToEmpty(map.get(key));
	}
	
	@SuppressWarnings("unchecked")
	public static String trim(Map map,String key){
		return trimToEmpty(map.get(key));
	}

	public static String trimToEmpty(Object obj){
		return obj==null?"":trimToEmpty(String.valueOf(obj));
	}

	@SuppressWarnings("unchecked")
	public static String trim(Map map,Object[] keys){
		Object result = null;
		for (int i = 0; i < keys.length; i++) {
			if(map.containsKey(keys[i])){
				result = map.get(keys[i]);
				break;
			}
		}
		return trimToEmpty(result);
	}
	
	@SuppressWarnings("unchecked")
	public static String trim(Map map,String[] keys){
		Object result = null;
		for (int i = 0; i < keys.length; i++) {
			if(map.containsKey(keys[i])){
				result = map.get(keys[i]);
				break;
			}
		}
		return trimToEmpty(result);
	}

	/**
	 * 按最小长度用指定字符串补全缺少的长度 <br/>
	 * eg:completeStrByPointLength(String.valueOf(1),4,"0")-->return "0001"
	 * @param value
	 * @param minStrLength
	 * @param replaceStr
	 * @return
	 */
	public static String completeStrByPointLength(String value,Integer minStrLength,String replaceStr){
		minStrLength = minStrLength==null?0:minStrLength;
		int valueLength = value.length();
		int index = minStrLength-valueLength;
		if(index>0){
			for (int i = 0; i <index; i++) {
				value = replaceStr+value;
			}
		}
		return value;
	}

	/**
	 * 通过月份得到季度
	 * @param month
	 * @return
	 */
	public static String getQuarterInMonth(int month){
		String quarters[] = {"1","2", "3", "4" };  
		if (month >= 1 && month <= 3)  
			return quarters[0];  
		else if (month >= 4 && month <= 6)  
			return quarters[1];  
		else if (month >= 7 && month <= 9)  
			return quarters[2];  
		else  
			return quarters[3];  
	}

}
