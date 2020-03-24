package coral.base.data;

import java.io.Serializable;
import java.util.ArrayList;

public class MetadataTable extends ArrayList<MetadataColumn> implements Serializable {

	private static final long serialVersionUID = 487980212632051670L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
