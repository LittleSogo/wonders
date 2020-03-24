/**
 * Project: Coral
 * Source file: SeqExecuteTimerTask.java
 * Create At 2013-12-9 下午05:03:37
 * Create By 龚云
 */
package coral.base.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import wfc.service.log.Log;

/**
 * @author 龚云
 * 
 */
public class SeqExecutor {

	public SeqExecutor() {
		name = "SeqExe-" + serialNumber();
	}

	public SeqExecutor(String name) {
		this.name = Azzert.trimedNotEmpty(name, "name");
	}

	public void cancel() {
		stopped = true;
		thread.interrupt();
		queue.clear();
	}

	public boolean isStopped() {
		return stopped;
	}

	public void schedule(Runnable runner) {
		Azzert.notNull(runner, "runner");
		queue.add(runner);
	}

	public void start() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Runnable runner = null;
					while ((runner = queue.take()) != null) {
						runner.run();
					}
				} catch (InterruptedException e) {
					if (stopped) {
						Log.info(name + " is stopped.");
					} else {
						Log.error(e);
					}
				}
			}
		});
		thread.setName(name);
		// thread.setDaemon(true);
		thread.start();
	}

	private Thread thread;
	private String name;
	private BlockingQueue<Runnable> queue = new LinkedBlockingDeque<Runnable>();
	private boolean stopped = false;

	private static synchronized int serialNumber() {
		return ++nextSerialNumber;
	}

	private static int nextSerialNumber = 0;

}
