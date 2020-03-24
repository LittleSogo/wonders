package coral.widget.utils;

public interface WidgetPage {

	/**
	 * 
	 * @return 当前页号
	 */
	int getCurrentPage();

	/**
	 * 
	 * @return 排序的字段
	 */
	String getOrder();

	/**
	 * 
	 * @return 每页大小
	 */
	int getPageSize();

	/**
	 * 
	 * @return SQL的额外条件
	 */
	String getSuffix();

}
