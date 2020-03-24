/**
 * Project: GdPlatform
 * Source file: SpringBeanUtils.java
 * Create At 2011-11-23 上午10:42:54
 * Create By 龚云
 */
package coral.base.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.aop.TargetSource;

/**
 * Spring的Bean工具类
 * 
 * @author 龚云
 * 
 */
public class SpringBeanUtils {

	/**
	 * 首先o必须是Spring动态代理后的类（必须是{@link Proxy}的子类），此方法尝试寻找动态代理前的原始实现类的对象
	 * 
	 * @param o
	 *            Spring动态代理后的类
	 * @return 原始实现类的对象
	 */
	public static Object getProxyTarget(Object o) {
		if (o != null && o instanceof Proxy) {
			Class<?> oClass = o.getClass();
			try {
				Method m = oClass.getDeclaredMethod("getTargetSource",
						new Class[] {});
				Object returnVal = m.invoke(o, new Object[] {});
				if (returnVal != null && returnVal instanceof TargetSource) {
					TargetSource targetSource = (TargetSource) returnVal;
					return targetSource.getTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 等同于instance of运算符，加入了Spring动态代理的判断，内部调用{@link #getProxyTarget(Object)}
	 * 
	 * @param o
	 *            待测试的对象
	 * @param klass
	 *            标杆类
	 * @return
	 */
	public static boolean isInstanceOf(Object o, Class<?> klass) {
		if (o == null && klass == null)
			return true;
		if (o == null || klass == null)
			return false;
		Class<?> oClass = o.getClass();
		if (klass.isAssignableFrom(oClass))
			return true;
		Object target = getProxyTarget(o);
		if (klass.isAssignableFrom(target.getClass()))
			return true;
		return false;
	}
}
