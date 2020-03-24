package coral.base.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import wfc.service.util.OrderedHashMap;

public class SubmitData implements Serializable {

	private static final long serialVersionUID = 4742161708600358378L;

	private List<SubmitDataItem> submitDataItemList;

	public SubmitData() {
		submitDataItemList = new ArrayList<SubmitDataItem>();
	}

	public SubmitData(HttpServletRequest request) {
		try {
			RequestWrapper requestWrapper = new RequestWrapper(request);
			init(requestWrapper);
		} catch (UnsupportedEncodingException e) {
		}
	}

	public SubmitData(RequestWrapper requestWrapper) {
		init(requestWrapper);
	}

	private void init(RequestWrapper requestWrapper) {
		OrderedHashMap<String, List<RequestWrapper.RequestField>> ohm = requestWrapper
				.getParams();
		for (int i = 0; i < ohm.size(); i++) {
			List<RequestWrapper.RequestField> list = ohm.getValue(i);
			for (RequestWrapper.RequestField field : list) {
				SubmitDataItem submitDataItem = new SubmitDataItem();
				if (field.isNormal()) {
					submitDataItem.setFile(false);
					submitDataItem.setName(field.getName());
					submitDataItem.setValue(field.getValue());
				} else {
					submitDataItem.setFile(true);
					submitDataItem.setName(field.getName());
					FileItem fileItem = field.getFileItem();
					submitDataItem.setFilename(fileItem.getName());
					submitDataItem.setContent(fileItem.get());
				}
				getSubmitDataItemList().add(submitDataItem);
			}
		}
	}

	public List<SubmitDataItem> getSubmitDataItemList() {
		if (submitDataItemList == null) {
			submitDataItemList = new ArrayList<SubmitDataItem>();
		}
		return submitDataItemList;
	}

	public String[] getValues(String name) {
		List<String> valueList = new ArrayList<String>();
		for (SubmitDataItem submitDataItem : submitDataItemList) {
			if (!submitDataItem.isFile()
					&& submitDataItem.getName().equals(name)) {
				valueList.add(submitDataItem.getValue());
			}
		}
		if (valueList.isEmpty()) {
			return null;
		} else {
			return valueList.toArray(new String[] {});
		}
	}

	public String getValue(String name) {
		String[] values = getValues(name);
		if (values != null && values.length > 0) {
			return values[0];
		} else {
			return null;
		}
	}

	public SubmitDataItem[] getFileItems(String name) {
		List<SubmitDataItem> itemList = new ArrayList<SubmitDataItem>();
		for (SubmitDataItem submitDataItem : submitDataItemList) {
			if (!submitDataItem.isFile()
					&& submitDataItem.getName().equals(name)) {
				itemList.add(submitDataItem);
			}
		}
		if (itemList.isEmpty()) {
			return null;
		} else {
			return itemList.toArray(new SubmitDataItem[] {});
		}
	}

	public SubmitDataItem getFileItem(String name) {
		SubmitDataItem[] submitDataItems = getFileItems(name);
		if (submitDataItems != null && submitDataItems.length > 0) {
			return submitDataItems[0];
		} else {
			return null;
		}
	}

}
