/**
 * Project: Coral
 * Source file: FileUtils.java
 * Create At 2013-9-13 下午01:59:16
 * Create By 龚云
 */
package coral.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.lf5.util.StreamUtils;

/**
 * @author 龚云
 * 
 */
public class FileUtils {

	public static String getText(String file, String charset)
			throws IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(baos);
			StreamUtils.copy(is, bos);
			bos.flush();
			return new String(baos.toByteArray(), charset);
		} finally {
			if (is != null)
				is.close();
		}
	}
	
}
