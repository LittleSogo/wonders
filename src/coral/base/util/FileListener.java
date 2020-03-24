/**
 * Project: Coral
 * Source file: FileListener.java
 * Create At 2013-9-16 下午02:00:39
 * Create By 龚云
 */
package coral.base.util;

import java.io.File;

/**
 * @author 龚云
 * 
 */
public interface FileListener {

	void onModified(File f);

}
