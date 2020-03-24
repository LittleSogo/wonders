/**
 * Project: GdPlatform
 * Source file: ResourceUtils.java
 * Create At 2013-7-4 下午04:41:51
 * Create By 龚云
 */
package coral.base.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author 龚云
 * 
 */
public class ResourceUtils {

	public static File getFile(String path) throws FileNotFoundException {
		URL url = getResource(path);
		try {
			return new File(url.toURI().getPath());
		} catch (URISyntaxException e) {
			return new File(url.getFile());
		}
	}

	public static InputStream getResourceStream(String path) throws IOException {
		return getResource(path).openStream();
	}

	protected static URL getResource(String path) throws FileNotFoundException {
		URL url = ResourceUtils.class.getResource(path);
		if (url == null)
			throw new FileNotFoundException(path);
		return url;
	}

}
