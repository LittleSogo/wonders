package common.base.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApplicationCtx
  implements ServletContextListener
{
  private static ApplicationContext springContext;

  public Object Getbeans(String name)
  {
    Object o = null;
    try {
      o = springContext.getBean(name);
    } catch (Exception e) {
      System.out.println("Spring获取bean出错 Bean name is " + name);
    }
    return o;
  }

  public void contextDestroyed(ServletContextEvent sce)
  {
  }

  public void contextInitialized(ServletContextEvent sce)
  {
    springContext = WebApplicationContextUtils.getWebApplicationContext(sce
      .getServletContext());
  }

  public static ApplicationContext getApplicationContext() {
    return springContext;
  }
}