package coral.base.util;

import wfc.facility.tool.autocode.Transformer4Base;

public class Transformer4SubmitData extends Transformer4Base {

	private SubmitData submitData;

	public Transformer4SubmitData(SubmitData submitData) {
		this.submitData = submitData;
	}

	@Override
	public String getParameter(String name) {
		return submitData.getValue(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		return submitData.getValues(name);
	}

}
