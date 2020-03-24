/**
 * Project: PrototypeFrame
 * Source file: EasyUIHelper.java
 * Create At 2012-7-27 下午04:56:03
 * Create By 龚云
 */
package coral.widget.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import wfc.service.log.Log;
import coral.base.util.RequestWrapper;
import coral.base.util.SqlUtils;
import coral.base.util.SystemConstant;

/**
 * @author 龚云
 * 
 */
public class EasyUIHelper {

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
		 * @param pageNo 第几页
		 * @param pageSize 每页大小
		 * @param sort 排序的字段
		 * @param dir 排序字段的方向
		 * @param suffix SQL的额外条件
		 */
		protected Page(String pageNo, String pageSize, String sort, String dir,
				String suffix) {
			try {
				this.pageSize = Integer.parseInt(pageSize);
			} catch (Exception ex) {
				Log.debug("Not found page size.");
			}
			if (this.pageSize > MAX_PAGE_SIZE) {
				this.pageSize = MAX_PAGE_SIZE;
			}
			try {
				currentPage = Integer.parseInt(pageNo);
			} catch (Exception ex) {
				Log.debug("Not found page no.");
			}
			if (!StringUtils.trimToEmpty(suffix).isEmpty())
				this.suffix = suffix;
			sort = StringUtils.trimToEmpty(sort);
			dir = StringUtils.trimToEmpty(dir);
			if (!StringUtils.trimToEmpty(sort).isEmpty()
					&& !StringUtils.trimToEmpty(dir).isEmpty()) {
				if (sort.contains(",") && dir.contains(",")) {
					String[] sorts = sort.split(",");
					String[] dirs = dir.split(",");
					StringBuilder sb = new StringBuilder();
					if (sorts.length == dirs.length) {
						for (int i = 0; i < sorts.length; i++) {
							String subSort = StringUtils.trimToEmpty(sorts[i]);
							String subDir = StringUtils.trimToEmpty(dirs[i]);
							if (SqlUtils.checkSqlSymbol(subSort)
									&& SqlUtils.checkSqlSymbol(subDir)) {
								if (sb.length() > 0)
									sb.append(", ");
								sb.append(subSort).append(" ").append(subDir);
							}
						}
					}
					if (sb.length() > 0)
						this.order = " ORDER BY " + sb.toString();
				} else {
					if (SqlUtils.checkSqlSymbol(sort)
							&& SqlUtils.checkSqlSymbol(dir))
						this.order = " ORDER BY " + sort + ' ' + dir;
				}
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
		private String order = "";

		int pageSize = Integer.MAX_VALUE / 2;
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
		return new Page(req.getParameter(PAGE_NO), req.getParameter(PAGE_SIZE),
				req.getParameter(SORT_PARAM), req.getParameter(DIR_PARAM), req
						.getParameter(SUFFIX_PARAM));
	}

	/**
	 * 解析请求的参数构建分页信息
	 * 
	 * @param wrapper HttpServletRequest对象包装器
	 * @return
	 */
	public static Page getPage(RequestWrapper wrapper) {
		return new Page(wrapper.getParameter(PAGE_NO), wrapper
				.getParameter(PAGE_SIZE), wrapper.getParameter(SORT_PARAM),
				wrapper.getParameter(DIR_PARAM), wrapper
						.getParameter(SUFFIX_PARAM));
	}

	/**
	 * 向response输出结果
	 * 
	 * @param res
	 * @param result 结果
	 * @throws IOException
	 */
	public static void writeResponse(HttpServletResponse res, String result)
			throws IOException {
		writeResponse(res, result, SystemConstant.DEFAULT_CHARSET);
	}

	public static void writeFormResponse(HttpServletResponse res,
			String result, String charset) throws IOException {
		result = StringEscapeUtils.escapeHtml(result);
		res.setContentType("text/html;charset="
				+ SystemConstant.DEFAULT_CHARSET);
		res.setCharacterEncoding(SystemConstant.DEFAULT_CHARSET);
		res.getWriter().write(result);
	}

	/**
	 * 向response输出结果，若前台提交形式为IFrame而非ajax，则需要转义html代码
	 * 
	 * @param res
	 * @param result 结果
	 * @throws IOException
	 */
	public static void writeFormResponse(HttpServletResponse res, String result)
			throws IOException {
		writeFormResponse(res, result, SystemConstant.DEFAULT_CHARSET);
	}

	/**
	 * 向response输出结果
	 * 
	 * @param res
	 * @param result 结果
	 * @param charset 编码
	 * @throws IOException
	 */
	public static void writeResponse(HttpServletResponse res, String result,
			String charset) throws IOException {
		res.setContentType("text/plain;charset=" + charset);
		res.setCharacterEncoding(charset);
		res.getWriter().write(result);
	}

	/**
	 * EasyUI查询参数：排序方向
	 */
	public static final String DIR_PARAM = "order";

	/**
	 * EasyUI最大每页大小
	 */
	public static final int MAX_PAGE_SIZE = 100;

	/**
	 * EasyUI查询参数：第几页
	 */
	public static final String PAGE_NO = "page";

	/**
	 * EasyUI查询参数：每页大小
	 */
	public static final String PAGE_SIZE = "rows";

	/**
	 * EasyUI查询参数：排序字段
	 */
	public static final String SORT_PARAM = "sort";

	/**
	 * EasyUI查询参数：SQL额外条件
	 */
	public static final String SUFFIX_PARAM = "q.s";

}
