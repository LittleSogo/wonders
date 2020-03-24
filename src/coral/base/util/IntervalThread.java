/**
 * Project: Coral
 * Source file: FileWatcher.java
 * Create At 2013-9-16 上午10:09:31
 * Create By 龚云
 */
package coral.base.util;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 龚云
 * 
 */
public abstract class IntervalThread {

	public IntervalThread(long duration, TimeUnit timeUnit) {
		interval = timeUnit.toMillis(duration);
	}

	public int getPriority() {
		return priority;
	}

	public WatcherState getState() {
		return state;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public IntervalThread setDaemon(boolean daemon) {
		this.daemon = daemon;
		return this;
	}

	public IntervalThread setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public void start(final boolean startImmediately) {
		try {
			lock.lock();
			if (changeState(WatcherState.START)) {
				if (intervalThread != null && intervalThread.isAlive()) {
					changeState(WatcherState.ERROR);
					logger.error("Last start thread does'n stopped.");
				}
				intervalThread = null;
				intervalThread = new Thread(new Runnable() {
					@Override
					public void run() {
						logger.info(name + " thread started.");
						boolean firstStarted = false;
						try {
							while (state == WatcherState.START) {
								if (!firstStarted && startImmediately) {
									doInterval();
								} else {
									doInterval();
								}
								Thread.sleep(interval);
							}
						} catch (InterruptedException e) {
							logger.info(name + " thread stopped.");
						}
					}
				});
				intervalThread.setName(name);
				intervalThread.setPriority(priority);
				intervalThread.setDaemon(daemon);
				intervalThread.start();
			}
		} finally {
			lock.unlock();
		}
	}

	public void stop() {
		try {
			lock.lock();
			if (changeState(WatcherState.STOP)) {
				intervalThread.interrupt();
				intervalThread = null;
			}
		} finally {
			lock.unlock();
		}
	}

	protected synchronized boolean changeState(WatcherState state) {
		WatcherState oldState = this.state;
		boolean changed = false;
		String stateLog = "(" + oldState.name() + ") --> (" + state + ")";
		switch (state) {
			case START: {
				if (this.state == WatcherState.STOP) {
					this.state = state;
					changed = true;
				}
				break;
			}
			case STOP: {
				if (this.state == WatcherState.START) {
					this.state = state;
					changed = true;
				}
				break;
			}
			case ERROR: {
				this.state = state;
				changed = true;
			}
		}
		if (changed) {
			logger.debug(name + " change state success " + stateLog);
		} else {
			logger.error(name + " change state failure " + stateLog);
		}
		return changed;
	}

	abstract void doInterval() throws InterruptedException;

	private boolean daemon = false;
	private transient long interval = -1;
	private Thread intervalThread = null;
	private ReentrantLock lock = new ReentrantLock();
	private String name = getClass().getSimpleName() + "-"
			+ UUID.randomUUID().toString() + ":";
	private int priority = Thread.NORM_PRIORITY;
	private WatcherState state = WatcherState.STOP;

	private static Log logger = LogFactory.getLog(IntervalThread.class);

	private static enum WatcherState {
		ERROR, START, STOP;
	}

}
