/**
 * Project: Coral
 * Source file: FileWatcher.java
 * Create At 2013-9-16 上午10:33:11
 * Create By 龚云
 */
package coral.base.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 龚云
 * 
 */
public class FileWatcher extends IntervalThread {

	/**
	 * @param duration
	 * @param timeUnit
	 */
	public FileWatcher(long duration, TimeUnit timeUnit) {
		super(duration, timeUnit);
	}

	public synchronized void addFile(File f) {
		try {
			InnerFile inf = new InnerFile(f);
			if (!fileMap.containsKey(inf.path)) {
				fileMap.put(inf.path, inf);
			} else {
				InnerFile old = fileMap.get(inf.path);
				checkModified(old);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addFile(String file) {
		addFile(new File(file));
	}

	public boolean addListener(FileListener listener) {
		return listeners.add(listener);
	}

	public synchronized boolean removeFile(File f) {
		try {
			InnerFile inf = new InnerFile(f);
			if (fileMap.containsKey(inf.path)) {
				if (fileMap.remove(inf.path) != null)
					return true;
			}
			return false;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean removeListener(FileListener listener) {
		return listeners.remove(listener);
	}

	protected void checkModified(InnerFile inf) {
		long oModified = inf.lastModified;
		long nModified = inf.file.lastModified();
		if (oModified != nModified) {
			String oldDate = logSdf.format(new Date(oModified)) + "("
					+ oModified + ")";
			String newDate = logSdf.format(new Date(nModified)) + "("
					+ nModified + ")";
			log.info("File " + inf.file + " changed " + oldDate + " --> "
					+ newDate);
			inf.lastModified = nModified;
			invokeModified(inf);
		}
	}

	/**
	 * @see coral.base.util.IntervalThread#doInterval()
	 */
	@Override
	protected void doInterval() throws InterruptedException {
		for (Entry<String, InnerFile> entry : fileMap.entrySet()) {
			InnerFile inf = entry.getValue();
			checkModified(inf);
		}
	}

	protected void invokeModified(InnerFile inf) {
		for (FileListener listener : listeners) {
			listener.onModified(inf.file);
		}
	}

	private Map<String, InnerFile> fileMap = new ConcurrentHashMap<String, InnerFile>();
	private List<FileListener> listeners = Collections
			.synchronizedList(new ArrayList<FileListener>());

	private static Log log = LogFactory.getLog(FileWatcher.class);
	private SimpleDateFormat logSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");

	private static class InnerFile {
		InnerFile(File file) throws IOException {
			if (!file.exists())
				throw new FileNotFoundException(file.getAbsolutePath());
			this.path = file.getCanonicalPath();
			this.file = file;
			lastModified = this.file.lastModified();
		}

		File file;
		long lastModified;
		String path;
	}
}
