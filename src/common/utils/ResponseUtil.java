package common.utils;

import coral.base.util.SystemConstant;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
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
}
