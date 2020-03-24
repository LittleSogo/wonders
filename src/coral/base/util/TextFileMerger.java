/**
 * Project: Coral
 * Source file: FileUtils.java
 * Create At 2013-9-11 下午04:35:47
 * Create By 龚云
 */
package coral.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 龚云
 * 
 */
public class TextFileMerger implements Closeable {

	public TextFileMerger(File dest, String destCharset, int bufferSize)
			throws UnsupportedEncodingException, FileNotFoundException {
		if (bufferSize > 0)
			this.bufferSize = bufferSize;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(dest), destCharset), this.bufferSize);
		init(bw, true);
	}

	public TextFileMerger(OutputStream dest, String destCharset,
			boolean closeDestStream, int bufferSize)
			throws UnsupportedEncodingException {
		if (bufferSize > 0)
			this.bufferSize = bufferSize;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dest,
				destCharset), this.bufferSize);
		init(bw, closeDestStream);
	}

	public TextFileMerger(String dest, String destCharset, int bufferSize)
			throws UnsupportedEncodingException, FileNotFoundException {
		this(new File(dest), destCharset, bufferSize);
	}

	public TextFileMerger(Writer writer, boolean closeWriter, int bufferSize) {
		if (bufferSize > 0)
			this.bufferSize = bufferSize;
		init(new BufferedWriter(writer, this.bufferSize), closeWriter);
	}

	public void addContent(String content) {
		add(new BufferedReader(new StringReader(content), bufferSize), true);
	}

	public void addFile(File file, String charset)
			throws UnsupportedEncodingException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charset), bufferSize);
		add(br, true);
	}

	public void addFile(String fileName, String charset)
			throws UnsupportedEncodingException, FileNotFoundException {
		addFile(new File(fileName), charset);
	}

	public void addInputStream(InputStream is, String charset,
			boolean closeStream) throws UnsupportedEncodingException {
		add(new BufferedReader(new InputStreamReader(is, charset), bufferSize),
				closeStream);
	}

	public void addReader(Reader reader, boolean closeStream) {
		add(new BufferedReader(reader, bufferSize), closeStream);
	}

	public void close() throws IOException {
		try {
			for (InnerBufferedReader reader : readers) {
				reader.close();
			}
		} finally {
			if (bw != null && closeDest)
				bw.close();
		}
	}

	public String getLineSep() {
		return lineSep;
	}

	public synchronized boolean merge() throws IOException {
		try {
			if (merging)
				throw new RuntimeException(
						"Has been merged, cannot merge again.");
			merging = true;
			for (InnerBufferedReader reader : readers) {
				write(reader);
			}
			return true;
		} finally {
			if (bw != null && closeDest)
				bw.close();
		}
	}

	public TextFileMerger setLineSep(String lineSep)
			throws UnsupportedEncodingException {
		this.lineSep = lineSep;
		return this;
	}

	protected void add(BufferedReader reader, boolean needClose) {
		InnerBufferedReader ibr = new InnerBufferedReader(reader, needClose);
		readers.add(ibr);
	}

	protected void init(BufferedWriter writer, boolean closeWriter) {
		this.bw = writer;
		this.closeDest = closeWriter;
	}

	protected void write(InnerBufferedReader reader) throws IOException {
		try {
			BufferedReader br = reader.bufferedReader;
			char[] buf = new char[bufferSize];
			int charRead = br.read(buf);
			while (charRead != -1) {
				bw.write(buf, 0, charRead);
				charRead = br.read(buf);
			}
			bw.flush();
		} finally {
			reader.close();
		}
	}

	TextFileMerger setBw(BufferedWriter bw) {
		this.bw = bw;
		return this;
	}

	private int bufferSize = defaultCharBufferSize;
	private BufferedWriter bw;
	private boolean closeDest = false;
	private String lineSep = SystemConstant.LINE_SEPARATOR;
	private boolean merging = false;
	private List<InnerBufferedReader> readers = new ArrayList<InnerBufferedReader>();

	private static int defaultCharBufferSize = 8192;

	private static class InnerBufferedReader {
		public InnerBufferedReader(BufferedReader bufferedReader,
				boolean needClose) {
			this.bufferedReader = bufferedReader;
			this.needClose = needClose;
		}

		public void close() {
			try {
				if (needClose && bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public BufferedReader bufferedReader;

		public boolean needClose;
	}
}
