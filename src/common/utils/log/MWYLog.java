package common.utils.log;


import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LocationInfo;

import wfc.service.config.ConfigValue;
import wfc.service.log.Debug;

import common.utils.config.MWYConfigValue;
/**
 * 
 * @author mywaystay
 *
 */
public class MWYLog{
	private static String LOGGER_NAME = "#MWYLOGGER#";
	
	private static Logger logger = null;

	static {
		logger = LogManager.getLogger(LOGGER_NAME);
		print("获得 log4j 配置：" + logger.getAllAppenders().hasMoreElements());
		if (!logger.getAllAppenders().hasMoreElements()) {
			logger.setAdditivity(false);
			Layout layout = new PatternLayout(
					"%c %d{yyyy-MM-dd HH:mm:ss,SSS} -%-5p- %m%n");
			Appender appender = new ConsoleAppender(layout);
			logger.addAppender(appender);
			logger.setLevel(Level.DEBUG);
		}
	}
	
	public void MWYLog(){
		
	}
	
	

	public static void log(Object message, Priority priority, Class<?> clazz) {
		LocationInfo info = new LocationInfo(new Throwable(), clazz.getName());
		String extra = "[" + info.getClassName() + "." + info.getMethodName()
				+ "," + info.getLineNumber() + "] " ;
		logger.log(priority, extra + "====>" + message);

	}

	private static void log(Object message, Priority priority) {
		if ("on".equalsIgnoreCase(ConfigValue.getLogDetailSwitch()))
			log(message, priority, MWYLog.class);
		else
			logger.log(priority, message);
	}

	public static void debug(Object message) {
		log(message, Priority.DEBUG);
	}

	public static void debug(Throwable message) {
		debug(Debug.show(message));
	}

	public static void debug(boolean message) {
		debug(String.valueOf(message));
	}

	public static void debug(char message) {
		debug(String.valueOf(message));
	}

	public static void debug(double message) {
		debug(String.valueOf(message));
	}

	public static void debug(float message) {
		debug(String.valueOf(message));
	}

	public static void debug(short message) {
		debug(String.valueOf(message));
	}

	public static void debug(int message) {
		debug(String.valueOf(message));
	}

	public static void debug(long message) {
		debug(String.valueOf(message));
	}

	public static void info(Object message) {
		log(message, Priority.INFO);
	}

	public static void info(Throwable message) {
		info(Debug.show(message));
	}

	public static void info(boolean message) {
		info(String.valueOf(message));
	}

	public static void info(char message) {
		info(String.valueOf(message));
	}

	public static void info(double message) {
		info(String.valueOf(message));
	}

	public static void info(float message) {
		info(String.valueOf(message));
	}

	public static void info(short message) {
		info(String.valueOf(message));
	}

	public static void info(int message) {
		info(String.valueOf(message));
	}

	public static void info(long message) {
		info(String.valueOf(message));
	}

	public static void warn(Object message) {
		log(message, Priority.WARN);
	}

	public static void warn(Throwable message) {
		warn(Debug.show(message));
	}

	public static void warn(boolean message) {
		warn(String.valueOf(message));
	}

	public static void warn(char message) {
		warn(String.valueOf(message));
	}

	public static void warn(double message) {
		warn(String.valueOf(message));
	}

	public static void warn(float message) {
		warn(String.valueOf(message));
	}

	public static void warn(short message) {
		warn(String.valueOf(message));
	}

	public static void warn(int message) {
		warn(String.valueOf(message));
	}

	public static void warn(long message) {
		warn(String.valueOf(message));
	}

	public static void error(Object message) {
		log(message, Priority.ERROR);
	}

	public static void error(Throwable message) {
		error(Debug.show(message));
	}

	public static void error(boolean message) {
		error(String.valueOf(message));
	}

	public static void error(char message) {
		error(String.valueOf(message));
	}

	public static void error(double message) {
		error(String.valueOf(message));
	}

	public static void error(float message) {
		error(String.valueOf(message));
	}

	public static void error(short message) {
		error(String.valueOf(message));
	}

	public static void error(int message) {
		error(String.valueOf(message));
	}

	public static void error(long message) {
		error(String.valueOf(message));
	}

	public static void fatal(Object message) {
		log(message, Priority.FATAL);
	}

	public static void fatal(Throwable message) {
		fatal(Debug.show(message));
	}

	public static void fatal(boolean message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(char message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(double message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(float message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(short message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(int message) {
		fatal(String.valueOf(message));
	}

	public static void fatal(long message) {
		fatal(String.valueOf(message));
	}

	public static void SystemPrint(Object message) {
		if (("ON".equals(MWYConfigValue.getMWYLogDetailSwitch()))||
				(!"OFF".equals(MWYConfigValue.getMWYLogDetailSwitch()))){
			System.out.println(message);
		}
	}

	public static void SystemPrint(Object message, boolean isPrintLogFlag) {
		if (isPrintLogFlag)
			MWYLog.info(message);
		else
			SystemPrint(message);
	}

	public static void main(String[] args) {
		startWriterTestThread("线程 #1", 1, (int) (10.0D * Math.random()), 50);
		startWriterTestThread("线程 #2", 2, (int) (10.0D * Math.random()), 50);
		startWriterTestThread("线程 #3", 3, (int) (10.0D * Math.random()), 50);
		startWriterTestThread("线程 #4", 4, (int) (10.0D * Math.random()), 50);
		startWriterTestThread("线程 #5", 5, (int) (10.0D * Math.random()), 50);
	}

	private static void startWriterTestThread(final String name,
			final int method, final int delay, final int count) {
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < count; i++) {
					switch (method) {
					case 1:
						MWYLog.debug(name + " , " + i);
						break;
					case 2:
						MWYLog.info(name + " , " + i);
						break;
					case 3:
						MWYLog.warn(name + " , " + i);
						break;
					case 4:
						MWYLog.error(name + " , " + i);
						break;
					case 5:
						MWYLog.fatal(name + " , " + i);
					}
					try {
						Thread.sleep(delay);
					} catch (InterruptedException localInterruptedException) {
					}
				}
			}
		}).start();
	}

	private static void print(String str) {
		System.out.println("########## " + str + " ##########");
	}
}