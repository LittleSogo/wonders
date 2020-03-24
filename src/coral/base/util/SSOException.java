/**
 * Project: GdPlatform
 * Source file: SSOException.java
 * Create At 2012-11-2 下午03:44:32
 * Create By 龚云
 */
package coral.base.util;

import org.springframework.core.NestedRuntimeException;

/**
 * @author 龚云
 * 
 */
public class SSOException extends NestedRuntimeException {

	public static enum TYPE {
		// 算法错误
		NO_ALGORITHM,
		// 超时
		TIME_OUT,
		// 令牌解密错误
		DECRYPT_ERROR,
		// 令牌加密错误
		ENCRYPT_ERROR,
		// 令牌格式错误
		TOKEN_FORMAT_ERROR,
		// 未知
		UNKNOW,
		// 字符编码错误
		UNSUPPORTED_ENCODING;
	}

	public SSOException(String msg) {
		super(msg);
	}

	public SSOException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SSOException(TYPE type) {
		this(type.name());
		this.type = type;
	}

	public SSOException(TYPE type, Throwable cause) {
		this(type.name(), cause);
		this.type = type;
	}

	public TYPE getType() {
		return type;
	}

	private TYPE type = TYPE.UNKNOW;

	private static final long serialVersionUID = -1607826110106942222L;

}
