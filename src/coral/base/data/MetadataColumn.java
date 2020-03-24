package coral.base.data;

import java.io.Serializable;

public class MetadataColumn implements Serializable {

	private static final long serialVersionUID = -1509987739925220368L;

	private String name;

	private boolean pk;

	private String type;

	private int length;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
