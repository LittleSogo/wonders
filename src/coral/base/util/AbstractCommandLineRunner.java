/**
 * Project: Coral
 * Source file: AbstractCommandLineRunner.java
 * Create At 2014-2-26 上午11:04:50
 * Create By 龚云
 */
package coral.base.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * @author 龚云
 * 
 */
public class AbstractCommandLineRunner {

	public AbstractCommandLineRunner(String[] args, OutputStream os) {
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}

	protected void checkEmpty(String cmd, String opt) {
		if (opt.isEmpty()) {
			throw new EmptyCommandException("参数" + cmd + "未指定值");
		}
	}

	protected void err(String str) {
		writeln("Error: " + str);
	}

	protected Boolean getBoolean(String opt, boolean dft) {
		if ("y".equalsIgnoreCase(opt))
			return true;
		if ("n".equalsIgnoreCase(opt))
			return false;
		if ("true".equalsIgnoreCase(opt))
			return false;
		if ("false".equalsIgnoreCase(opt))
			return false;
		return dft;
	}

	protected String getCharset(String enc) {
		try {
			if (Charset.isSupported(enc))
				return enc;
			throw new EmptyCommandException("不支持的编码格式：" + enc);
		} catch (Exception e) {
			throw new EmptyCommandException("不支持的编码格式：" + enc, e);
		}
	}

	protected String getExistFile(String path) {
		try {
			File file = null;
			if (path.contains(":"))
				file = new File(path);
			file = new File(System.getProperty("user.dir"), path);
			if (!file.exists()) {
				err("找不到文件：" + file.getCanonicalPath());
			}
			return file.getCanonicalPath();
		} catch (IOException e) {
			throw new EmptyCommandException(e);
		}
	}

	protected String getFile(String path) {
		try {
			if (path.contains(":"))
				return new File(path).getCanonicalPath();
			return new File(System.getProperty("user.dir"), path)
					.getCanonicalPath();
		} catch (IOException e) {
			throw new EmptyCommandException(e);
		}
	}

	protected void info(String str) {
		writeln("Info: " + str);
	}

	protected void writeln(String content) {
		try {
			bw.write(content);
			bw.write(LINE_SEP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected BufferedWriter bw;

	public static final String LINE_SEP = System.getProperty("line.separator");

	public static class EmptyCommandException extends RuntimeException {

		public EmptyCommandException(String msg) {
			super(msg);
		}

		public EmptyCommandException(String msg, Throwable cause) {
			super(msg, cause);
		}

		public EmptyCommandException(Throwable cause) {
			super(cause);
		}

		private static final long serialVersionUID = 1L;

	}
}
