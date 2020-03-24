package coral.widget.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import coral.base.util.RequestWrapper;
import coral.base.util.SqlUtils;

/**
 * ExtGrid的后台帮助类
 * 
 * @author 龚云
 * 
 */
public class ExtHelper {

	/**
	 * ExtGrid的分页帮助类
	 * 
	 * @author 龚云
	 * 
	 */
	public static class Page implements WidgetPage {
		/**
		 * ExtGrid的分页帮助类
		 * 
		 * @param startStr 请求的开始记录号
		 * @param limitStr 请求的结束记录号
		 * @param sort 排序的字段
		 * @param dir 排序字段的方向
		 * @param suffix SQL的额外条件
		 */
		protected Page(String startStr, String limitStr, String sort,
				String dir, String suffix) {
			int start = 0;
			if (!StringUtils.trimToEmpty(startStr).isEmpty())
				try {
					start = Integer.parseInt(startStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			int limit = Integer.MAX_VALUE / 2;
			if (!StringUtils.trimToEmpty(limitStr).isEmpty())
				try {
					limit = Integer.parseInt(limitStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			start++;
			pageSize = limit;
			currentPage = start / pageSize + (start % pageSize == 0 ? 0 : 1);

			if (!StringUtils.trimToEmpty(suffix).isEmpty())
				this.suffix = suffix;
			sort = StringUtils.trimToEmpty(sort);
			dir = StringUtils.trimToEmpty(dir);
			if (!sort.isEmpty() && !dir.isEmpty()
					&& SqlUtils.checkSqlSymbol(sort)
					&& SqlUtils.checkSqlSymbol(dir)) {
				this.order = " ORDER BY " + sort + ' ' + dir;
			}
		}

		/**
		 * @see coral.widget.utils.WidgetPage#getCurrentPage()
		 */
		@Override
		public int getCurrentPage() {
			return currentPage;
		}

		/**
		 * @see coral.widget.utils.WidgetPage#getOrder()
		 */
		@Override
		public String getOrder() {
			return order;
		}

		/**
		 * @see coral.widget.utils.WidgetPage#getPageSize()
		 */
		@Override
		public int getPageSize() {
			return pageSize;
		}

		/**
		 * @see coral.widget.utils.WidgetPage#getSuffix()
		 */
		@Override
		public String getSuffix() {
			if (suffix.isEmpty() && !order.isEmpty())
				return this.order;
			return suffix;
		}

		int currentPage = 1;
		int pageSize = Integer.MAX_VALUE / 2;

		private String order = "";
		private String suffix = "";
	}

	/**
	 * 解析请求的参数构建分页信息
	 * 
	 * @param req 请求的参数中解析项为：{@link ExtHelper}的
	 *            START_PARAM,LIMIT_PARAM,SORT_PARAM,DIR_PARAM,SUFFIX_PARAM
	 * @return
	 */
	public static Page getPage(HttpServletRequest req) {
		return new Page(req.getParameter(START_PARAM), req
				.getParameter(LIMIT_PARAM), req.getParameter(SORT_PARAM), req
				.getParameter(DIR_PARAM), req.getParameter(SUFFIX_PARAM));
	}

	/**
	 * 解析请求的参数构建分页信息
	 * 
	 * @param requestWrapper HttpServletRequest对象包装器
	 * @return
	 */
	public static Page getPage(RequestWrapper requestWrapper) {
		return new Page(requestWrapper.getParameter(START_PARAM),
				requestWrapper.getParameter(LIMIT_PARAM), requestWrapper
						.getParameter(SORT_PARAM), requestWrapper
						.getParameter(DIR_PARAM), requestWrapper
						.getParameter(SUFFIX_PARAM));
	}

	/**
	 * 构建分页信息
	 * 
	 * @param startStr 请求的开始记录号
	 * @param limitStr 请求的结束记录号
	 * @param sort 排序的字段
	 * @param dir 排序字段的方向
	 * @param suffix SQL的额外条件
	 * @return
	 */
	public static Page getPage(String startStr, String limitStr, String sort,
			String dir, String suffix) {
		return new Page(startStr, limitStr, sort, dir, suffix);
	}

	/**
	 * 向相应消息流输出错误消息
	 * 
	 * @param res
	 * @param result 输出的消息
	 * @throws Exception
	 */
	public static void writeString(HttpServletResponse res, String result)
			throws IOException {
		res.setContentType("text/html");
		res.getWriter().write(result);
	}

	/**
	 * Ext查询参数：开始记录号
	 */
	public static final String START_PARAM = "q.start";

	/**
	 * Ext查询参数：结束记录号
	 */
	public static final String LIMIT_PARAM = "q.limit";

	/**
	 * Ext查询参数：表名
	 */
	public static final String TABLE_PARAM = "q.t";

	/**
	 * Ext查询参数：排序字段
	 */
	public static final String SORT_PARAM = "q.sort";

	/**
	 * Ext查询参数：排序方向
	 */
	public static final String DIR_PARAM = "q.dir";

	/**
	 * Ext查询参数：SQL额外条件
	 */
	public static final String SUFFIX_PARAM = "q.s";
}
