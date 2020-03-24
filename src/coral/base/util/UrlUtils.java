/**
 * Project: Coral
 * Source file: UrlUtils.java
 * Create At 2013-10-16 下午03:34:50
 * Create By 龚云
 */
package coral.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author 龚云
 * 
 */
public class UrlUtils {

	public static boolean checkHttpUrl(String urlStr) {
		return Pattern.matches(HTTP_URL_PATTERN, urlStr);
	}

	public static boolean checkUrl(String urlStr) {
		return Pattern.matches(URL_PATTERN, urlStr);
	}

	public static String generateUrlParam(String paramName, String paramValue,
			String encode) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(URLEncoder.encode(paramName, encode));
			sb.append("=");
			sb.append(URLEncoder.encode(paramValue, encode));
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getPathWithQuery(URL url) {
		String path = StringUtils.trimToEmpty(url.getPath());
		String query = StringUtils.trimToEmpty(url.getQuery());
		if (!query.isEmpty())
			path += "?" + query;
		return path;
	}

	public static String getUrl(String baseUrl,
			Map<String, List<String>> params, String urlEncode)
			throws UnsupportedEncodingException {
		if (params == null)
			return baseUrl;
		baseUrl = StringUtils.trimToEmpty(baseUrl);
		StringBuilder sb = new StringBuilder(baseUrl);
		if (!baseUrl.contains("?"))
			sb.append("?");
		for (Entry<String, List<String>> entry : params.entrySet()) {
			String paramName = entry.getKey();
			List<String> paramValues = entry.getValue();
			if (paramValues != null) {
				for (String paramValue : paramValues) {
					sb.append("&");
					sb.append(URLEncoder.encode(paramName, urlEncode));
					sb.append("=");
					sb.append(URLEncoder.encode(paramValue, urlEncode));
				}
			}
		}
		return sb.toString();
	}

	private static final String HTTP_URL_PATTERN = "http(s)?\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
	private static final String URL_PATTERN = "\\w*\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";

}
