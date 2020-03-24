package com.base;

/**
 * @author ：zj
 * @date ：Created in 2020/1/12 19:01
 * @description：数据库查询条件对象 包含三个参数： 分别为参数名、参数值、查询条件表达式
 * @modified By：
 * @version: $
 * 参考：https://www.cnblogs.com/songhaipeng/p/3328279.html
 */
public class QueryParameter {
    /**
     * ??枚举？？
     */
    public enum QueryOperateType {
        Equal, CharIn
    }

    public String ParameterName;
    public Object ParameterValue;
    public QueryOperateType ParameterType;

    public QueryParameter() {
    }

    public QueryParameter(String parameterName, Object parameterValue, QueryOperateType parameterType) {
        ParameterName = parameterName;
        ParameterValue = parameterValue;
        ParameterType = parameterType;
    }


    public String getParameterName() {
        return ParameterName;
    }

    public void setParameterName(String parameterName) {
        ParameterName = parameterName;
    }

    public Object getParameterValue() {
        return ParameterValue;
    }

    public void setParameterValue(Object parameterValue) {
        ParameterValue = parameterValue;
    }

    public QueryOperateType getParameterType() {
        return ParameterType;
    }

    public void setParameterType(QueryOperateType parameterType) {
        ParameterType = parameterType;
    }
}
