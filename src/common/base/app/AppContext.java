package common.base.app;

import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import wfc.service.config.Config;

public class AppContext {
	
	public static String appPath;

	public static String classesPath;

	public static String webRootPath;  // 基底网址
	
	public static String databaseName;
	
	public static ApplicationContext currentContext;
	
	public static boolean isContextReady() {
		return currentContext != null;
	}

	public static Object getBean(String id) {
		return currentContext.getBean(id);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, ?> getBean(Class<?> clazz) {
		return currentContext.getBeansOfType(clazz);
	}

	public static Object getSingleBean(Class<?> clazz) {
		Map<String, ?> map = getBean(clazz);
		return map.values().iterator().next();
	}

	private static void init() {
		try {
			String classespath = StringUtils.trimToEmpty(Config
					.get("coral.classespath"));
			String defaultClassesPath = AppContext.class.getResource("/")
					.toURI().getPath();
			AppContext.classesPath = classespath.isEmpty() ? defaultClassesPath
					: classespath;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	static {
		init();
	}
}