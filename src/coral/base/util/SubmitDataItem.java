package coral.base.util;

import java.io.Serializable;

public class SubmitDataItem implements Serializable {

	private static final long serialVersionUID = -4987963388661828900L;

	private boolean file;

	private String name;

	private String value;

	private String filename;

	private byte[] content;

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
