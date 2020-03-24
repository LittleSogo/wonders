/**
 * Project: Coral
 * Source file: MultThreadSeqExecutor.java
 * Create At 2014-1-28 下午05:16:53
 * Create By 龚云
 */
package coral.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龚云
 * 
 */
public class MultiThreadSeqExecutor {

	public MultiThreadSeqExecutor(int threadCount) {
		name = "MultiSeqExe-" + serialNumber();
	}

	public MultiThreadSeqExecutor(String name, int threadCount) {
		this.name = Azzert.trimedNotEmpty(name, "name");
		for (int i = 0; i < threadCount; i++) {
			seList.add(new SeqExecutor(this.name + "-" + (i + 1)));
		}
	}

	public void cancel() {
		for (SeqExecutor se : seList) {
			se.cancel();
		}
	}

	public synchronized void schedule(Runnable runner) {
		SeqExecutor se = seList.get(seqNo);
		se.schedule(runner);
		seqNo = (seqNo + 1) % seList.size();
	}

	public void start() {
		for (SeqExecutor se : seList) {
			se.start();
		}
	}

	private int seqNo = 0;
	private String name;
	private List<SeqExecutor> seList = new ArrayList<SeqExecutor>();

	private static synchronized int serialNumber() {
		return ++nextSerialNumber;
	}

	private static int nextSerialNumber = 0;

}
