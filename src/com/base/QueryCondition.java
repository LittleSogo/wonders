package com.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/1/12 19:05
 * @description：查询对象实体
 * @modified By：
 * @version: $
 *
 * 包含查询实体名称以及查询参数对象
 */
public class QueryCondition {
    public String ModelName;
    public List<QueryParameter> Parameters = new ArrayList<QueryParameter>();

    public QueryCondition() {

    }

    public QueryCondition(String modelName) {
        this.ModelName = modelName;
    }

    public QueryCondition(String modelName, List<QueryParameter> parameters) {
        this.ModelName = modelName;
        this.Parameters = parameters;
    }

    public void add(QueryParameter queryParameter) {
        this.Parameters.add(queryParameter);
    }

    public String getModelName() {
        return ModelName;
    }

    public List<QueryParameter> getParameters() {
        return Parameters;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public void setParameters(List<QueryParameter> parameters) {
        Parameters = parameters;
    }

}
