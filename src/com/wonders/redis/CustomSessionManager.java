package com.wonders.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author ：zj
 * @date ：Created in 2020/3/24 21:57
 * @description：自定义的session管理对象
 * @modified By：
 * @version: $
 */
@Component
public class CustomSessionManager extends DefaultSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest servletRequest = null;

        if (sessionKey instanceof WebSessionKey){
            servletRequest = ((WebSessionKey) sessionKey).getServletRequest();
        }

        /**
         * 获取到的sessionid为null
         */

        if (servletRequest!=null && sessionId !=null){
            Session session  = (Session) servletRequest.getAttribute(sessionId.toString());
            if(session!=null){
                return session;
            }

        }
        Session session = super.retrieveSession(sessionKey);
        if (servletRequest!=null && sessionId !=null){
            servletRequest.setAttribute(sessionId.toString(),session);
        }


        return session;
    }
}
