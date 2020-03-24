package com.base;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.base.QueryParameter;



/**
 * @author ：zj
 * @date ：Created in 2020/1/12 19:08
 * @description：条件查询构造器
 * @modified By：
 * @version: $
 */
public class QueryDataByParams {

    public String errorCode;

    public QueryDataByParams() {
    }

    public QueryDataByParams(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 构造查询条件
     * @param queryCondition
     * @param <T>
     * @return
     * querySymbols为配置文件中预设的值
     */
    public  <T> List<T> queryByPropertys(QueryCondition queryCondition) {
        //String querySymbols = RequestContext.getServletContext().getInitParameter("QuerySymbols");
        String querySymbols ="this";
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select model \n");
        sqlBuffer.append("from " + queryCondition.ModelName + " as model \n");

        boolean first = true;
        for (int pi = 0; pi < queryCondition.Parameters.size(); pi++) {
            if (first) {
                sqlBuffer.append("where ");
                first = false;
            } else {
                sqlBuffer.append("and ");
            }
            if (queryCondition.Parameters.get(pi).getParameterType() == QueryParameter.QueryOperateType.Equal) {
                sqlBuffer.append("model."
                        + queryCondition.Parameters.get(pi).getParameterName()
                        + " = "
                        + querySymbols
                        + queryCondition.Parameters.get(pi)
                        .getParameterValue() + " \n");
            } else if (queryCondition.Parameters.get(pi).getParameterType() == QueryParameter.QueryOperateType.CharIn) {
                sqlBuffer.append("InStr(model."
                        + queryCondition.Parameters.get(pi).getParameterName()
                        + " , "
                        + querySymbols
                        + queryCondition.Parameters.get(pi)
                        .getParameterName() + " ) > 0 \n");
            }

        }
        List<T> list = new ArrayList<T>();
        System.out.println(sqlBuffer.toString()+"=====>><<==========");
        try {

            EntityManagerFactory entityManagerFactory  = Persistence.createEntityManagerFactory("tt");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            //EntityManager emEntityManager = EntityManagerHelper.getEntityManager();
            Query queryObject = entityManager.createQuery(sqlBuffer.toString());
            for (int li = 0; li < queryCondition.Parameters.size(); li++) {
                queryObject.setParameter(queryCondition.Parameters.get(li).getParameterName(),
                        queryCondition.Parameters.get(li).getParameterValue());
            }
            list = queryObject.getResultList();
            entityManager.close();

        } catch (RuntimeException re) {
            errorCode += "CM000006";
            throw re;
        }
        return list;
    }


}
