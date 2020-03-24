package com.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/1/14 9:29
 * @description：公司公共dao
 * @modified By：
 * @version: $
 */


public class WfcDao {

    public static void main(String[] args) {

        QueryDataByParams queryDataByParams = new QueryDataByParams();

        QueryParameter queryParameter = new QueryParameter();

        queryParameter.setParameterName("tName");
        queryParameter.setParameterValue("范特西");
        queryParameter.setParameterType(QueryParameter.QueryOperateType.Equal);

        List<QueryParameter> Parameters = new ArrayList<QueryParameter>();

        Parameters.add(queryParameter);

        QueryCondition queryCondition = new QueryCondition("TPerson",Parameters);

        queryDataByParams.queryByPropertys(queryCondition);
    }

}
