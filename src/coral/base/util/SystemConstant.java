package coral.base.util;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import wfc.service.config.Config;

public class SystemConstant {

	/**
	 * 系统默认编码
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String DATABASE_CHARSET = Config.get("db.charset") == null ? Charset
			.defaultCharset().name()
			: Config.get("db.charset");

	/**
	 * 系统换行符
	 */
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * 单点登录相关常量
	 */
	public static final class SSO {
		// 单点登录默认令牌参数名
		public static final String PARAM_TOKEN_NAME = "sso_ctk";
		// 单点登录URL中token经过DES解码后的byte数组对应的字符编码的参数名
		public static final String PARAM_TOKEN_ENCODING = "sso_ctkEnc";
		// 默认系统单点登录令牌种子
		public static final String DEFAULT_TOKEN_SEED = "Coral";
		// 单点登录时间、用户账号分隔符
		public static final String TOKEN_SPLIT_CHAR = "|";
		// 单点登录超时时间wfc配置key
		public static final String SSO_TOKEN_KEY = "sso.token";
		// 单点登录超密钥wfc配置key
		public static final String TIME_OUT_KEY = "sso.timeout";
		// 单点登录时间戳超时时间：30分钟
		public static final long TIME_OUT = getSSOTimeoutCfg();

		static long getSSOTimeoutCfg() {
			String timeOutStr = StringUtils.trimToEmpty(Config
					.get(TIME_OUT_KEY));
			long defaultTimeout = TimeUnit.MINUTES.toMillis(30);// 30分钟
			long timeout = defaultTimeout;
			if (!timeOutStr.isEmpty()) {
				try {
					timeout = TimeUnit.MINUTES.toMillis(Long
							.parseLong(timeOutStr));
				} catch (NumberFormatException e) {
					timeout = defaultTimeout;
				}
			}
			return timeout;
		}
	}

	/**
	 * 字典常量
	 * 
	 * @author 龚云
	 * 
	 */
	public static final class DIC {
		// 是
		public static final String YES = "是";
		// 否
		public static final String NO = "否";
	}

}
