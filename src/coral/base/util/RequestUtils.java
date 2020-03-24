/**
 * Project: Coral
 * Source file: RequestUtils.java
 * Create At 2013-9-13 下午04:07:29
 * Create By 龚云
 */
package coral.base.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author 龚云
 * 
 */
public class RequestUtils {

	public static String getServerHost(HttpServletRequest req) {
		String scheme = req.getScheme();
		String serverHost = null;
		String forwardHost = StringUtils.trimToEmpty(req
				.getHeader("X-Forwarded-Host"));
		if (!forwardHost.isEmpty()) {
			if (forwardHost.contains(",")) {
				String[] forwardHosts = forwardHost.split(",");
				if (forwardHosts.length > 0)
					forwardHost = StringUtils.trimToEmpty(forwardHosts[0]);
			}
			serverHost = forwardHost;
		} else {
			serverHost = req.getServerName() + ":" + req.getServerPort();
		}
		return scheme + "://" + serverHost;
	}

}
