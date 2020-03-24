package common.base.app;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

import wfc.service.config.Config;
import wfc.service.database.DB;
import wfc.service.log.Debug;
import wfc.service.log.Log;

public class AppDispatcherServlet extends
		org.springframework.web.servlet.DispatcherServlet {

	private static final long serialVersionUID = -8986975374796579061L;

	@SuppressWarnings("unchecked")
	@Override
	protected WebApplicationContext createWebApplicationContext(
			WebApplicationContext parent) throws BeansException {
		System.out.println("11111111111111111111111");
		AppContext.currentContext = parent;
		ServletContext servletContext = this.getServletContext();
		String appPath = StringUtils.trimToEmpty(Config.get("coral.apppath"));
		AppContext.appPath = appPath.isEmpty() ? servletContext
				.getRealPath("/") : appPath;
		AppContext.webRootPath = servletContext.getContextPath();
		AppContext.databaseName = DB.getDatabaseName();
		Log.debug("appPath = " + AppContext.appPath);
		Log.debug("webRootPath = " + AppContext.webRootPath);
		Log.debug("databaseName = " + AppContext.databaseName);
		Log.debug(Debug.show(Config.getConfigProperties()));
		// 初始化
		Class<?> coralInitClass = null;
		{
			if (AppContext.currentContext.containsBean("coralInitializer")) {
				Initializer initializer = (Initializer) AppContext.currentContext
						.getBean("coralInitializer");
				initializer.init();
				if (AopUtils.isAopProxy(initializer))
					coralInitClass = AopUtils.getTargetClass(initializer);
				else
					coralInitClass = initializer.getClass();
			}
		}
		Map<String, Initializer> initializerMap = AppContext.currentContext
				.getBeansOfType(Initializer.class);
		for (Initializer initializer : initializerMap.values()) {
			if (AopUtils.isAopProxy(initializer)) {
				if (coralInitClass != null
						&& coralInitClass.isAssignableFrom(AopUtils
								.getTargetClass(initializer))) {
					continue;
				}
			} else if (coralInitClass != null
					&& coralInitClass.isAssignableFrom(initializer.getClass())) {
				continue;
			}
			initializer.init();
		}
		return parent;
	}
}

