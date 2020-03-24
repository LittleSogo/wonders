/**
 * Project: Coral
 * Source file: CommandLineUtils.java
 * Create At 2014-2-26 上午11:01:54
 * Create By 龚云
 */
package coral.base.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 龚云
 * 
 */
public class CommandLineUtils {

	public static Queue<String> processArgs(String[] args) {
		Pattern p1 = Pattern.compile("(-[a-zA-Z_]+)=(.*)");
		Pattern p2 = Pattern.compile("^['\"](.*)['\"]$");
		Queue<String> queue = new LinkedList<String>();
		for (String str1 : args) {
			Matcher m1 = p1.matcher(str1);
			if (m1.matches()) {
				queue.add(m1.group(1));
				String str2 = m1.group(2);
				Matcher m2 = p2.matcher(str2);
				if (m2.matches())
					queue.add(m2.group(1));
				else
					queue.add(str2);
			} else {
				queue.add(str1);
			}
		}
		return queue;
	}

}
