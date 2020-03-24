package coral.base.data;

import java.util.ArrayList;

import wfc.service.util.OrderedHashSet;

public class DataPage extends ArrayList<DataRow> {

	private static final long serialVersionUID = 8433247291236495158L;

	private int pageIndex;

	private int pageSize;

	private int totalCount;

	private OrderedHashSet<String> pkList = new OrderedHashSet<String>();

	private OrderedHashSet<String> fieldList = new OrderedHashSet<String>();

	public DataPage(int pageIndex, int pageSize, int totalCount) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}

	public int getCurrentPageSize() {
		return this.size();
	}

	public OrderedHashSet<String> getFieldList() {
		return fieldList;
	}

	public int getNextPageIndex() {
		return pageIndex + 1;
	}

	public int getNextStartPageIndex() {
		return getNextPageIndex() * pageSize;
	}

	public int getPageCount() {
		return (int) Math.ceil((double) totalCount / pageSize);
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public OrderedHashSet<String> getPkList() {
		return pkList;
	}

	public int getPrePageIndex() {
		return pageIndex - 1;
	}

	public int getPreStartPageIndex() {
		return getPrePageIndex() * pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

}
