/**
 * Project: Coral
 * Source file: AjaxReturnMessage.java
 * Create At 2014-4-9 下午03:46:40
 * Create By 龚云
 */
package coral.widget.data;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 龚云
 * 
 */
public class AjaxReturnMessage {

	/**
	 * 将异常转换为指定的Ext的JSON错误消息格式
	 * 
	 * @param e 异常
	 * @param title 错误消息标题
	 * @return
	 */
	public static String catchExceptionToString(Exception e, String title) {
		return AjaxReturnMessage.catchExceptionToString(e, title, "");
	}

	/**
	 * 将异常转换为指定的Ext的JSON错误消息格式
	 * 
	 * @param e 异常
	 * @param title 错误消息标题
	 * @param script 后续执行的脚本
	 * @return
	 */
	public static String catchExceptionToString(Exception e, String title,
			String script) {
		return catchExceptionToString(e, title, script, null);
	}

	/**
	 * 将异常转换为指定的Ext的JSON错误消息格式
	 * 
	 * @param e 异常
	 * @param title 错误消息标题
	 * @param script 后续执行的脚本
	 * @param params 供前台使用的参数
	 * @return
	 */
	public static String catchExceptionToString(Exception e, String title,
			String script, JSONObject params) {
		return catchExceptionToString(e, title, script, params, true);
	}

	/**
	 * 将异常转换为指定的Ext的JSON错误消息格式
	 * 
	 * @param e 异常
	 * @param title 错误消息标题
	 * @param script 后续执行的脚本
	 * @param params 供前台使用的参数
	 * @param showMsg 是否显示消息框
	 * @return
	 */
	public static String catchExceptionToString(Exception e, String title,
			String script, JSONObject params, boolean showMsg) {
		String msg = StringUtils.trimToEmpty(e.getMessage());
		if (msg.isEmpty()) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			msg = writer.toString();
		}
		return AjaxReturnMessage.toJsonExceptionObj(msg, title, script, params)
				.toString();
	}

	/**
	 * 构建成功消息的JSON格式对象
	 */
	public static JSONObject success() {
		return success(null, null, null, false);
	}

	/**
	 * 构建成功消息的JSON格式对象
	 * 
	 * @param params 供前台使用的JSON参数
	 */
	public static JSONObject success(JSONObject params) {
		return success(null, null, params, false);
	}

	/**
	 * 构建成功消息的JSON格式对象
	 * 
	 * @param message 成功消息
	 * @param script 后续执行脚本
	 * @return
	 */
	public static JSONObject success(String message, String script) {
		return success(message, script, null);
	}

	/**
	 * 构建成功消息的JSON格式对象
	 * 
	 * @param message 成功消息
	 * @param script 后续执行脚本
	 * @param params 供前台使用的JSON参数
	 * @return
	 */
	public static JSONObject success(String message, String script,
			JSONObject params) {
		return success(message, script, params, true);
	}

	/**
	 * 构建成功消息的JSON格式对象
	 * 
	 * @param message 成功消息
	 * @param script 后续执行脚本
	 * @param params 供前台使用的JSON参数
	 * @param showMsg 是否显示消息框
	 * @return
	 */
	public static JSONObject success(String message, String script,
			JSONObject params, boolean showMsg) {
		JSONObject jso = new JSONObject();
		if (script == null)
			script = "";
		try {
			jso.put(SUCCESS_MSG, message);
			jso.put(SCRIPT, script);
			jso.put(TYPE, SUCCESS);
			jso.put(SHOW_MSG, showMsg);
			if (params != null)
				jso.put(JSON_PARAMS, params);
		} catch (JSONException e) {
			jso = createErrorJson(showMsg);
		}
		return jso;
	}

	/**
	 * 构建错误消息的JSON格式对象
	 */
	public static JSONObject toJsonErrorObj() {
		return toJsonErrorObj(null, null, null, null, false);
	}

	/**
	 * 构建错误消息的JSON格式对象
	 * 
	 * @param errMsg 错误消息
	 * @return
	 */
	public static JSONObject toJsonErrorObj(String errMsg) {
		return toJsonErrorObj(errMsg, null, null, null, true);
	}

	/**
	 * 构建错误消息的JSON格式对象
	 * 
	 * @param errMsg 错误消息
	 * @param params 供前台使用的JSON参数
	 * @return
	 */
	public static JSONObject toJsonErrorObj(String errMsg, JSONObject params) {
		return toJsonErrorObj(errMsg, null, null, params, true);
	}

	/**
	 * 构建错误消息的JSON格式对象
	 * 
	 * @param errMsg 错误消息
	 * @param title 错误消息标题
	 * @param script 后续执行脚本
	 * @return
	 */
	public static JSONObject toJsonErrorObj(String errMsg, String title,
			String script) {
		return toJsonErrorObj(errMsg, title, script, null);
	}

	/**
	 * 构建错误消息的JSON格式对象
	 * 
	 * @param errMsg 错误消息
	 * @param title 错误消息标题
	 * @param script 后续执行脚本
	 * @param params 供前台使用的JSON参数
	 * @return
	 */
	public static JSONObject toJsonErrorObj(String errMsg, String title,
			String script, JSONObject params) {
		return toJsonErrorObj(errMsg, title, script, params, true);
	}

	/**
	 * 构建错误消息的JSON格式对象
	 * 
	 * @param errMsg 错误消息
	 * @param title 错误消息标题
	 * @param script 后续执行脚本
	 * @param params 供前台使用的JSON参数
	 * @param showMsg 是否显示消息框
	 * @return
	 */
	public static JSONObject toJsonErrorObj(String errMsg, String title,
			String script, JSONObject params, boolean showMsg) {
		JSONObject jso = new JSONObject();
		try {
			if (!StringUtils.trimToEmpty(title).isEmpty())
				jso.put(TITLE, title);
			jso.put(ERROR_MSG, errMsg);
			jso.put(SCRIPT, script);
			jso.put(TYPE, ERROR);
			jso.put(SHOW_MSG, showMsg);
			if (params != null)
				jso.put(JSON_PARAMS, params);
		} catch (JSONException e) {
			jso = createErrorJson(showMsg);
		}
		return jso;
	}

	/**
	 * 
	 * 构建异常消息的JSON格式对象
	 * 
	 * @param errMsg 异常消息
	 * @param title 异常消息标题
	 * @param script 后续执行脚本
	 * @return
	 */
	public static JSONObject toJsonExceptionObj(String errMsg, String title,
			String script) {
		return toJsonExceptionObj(errMsg, title, script, null);
	}

	/**
	 * 
	 * 构建异常消息的JSON格式对象
	 * 
	 * @param errMsg 异常消息
	 * @param title 异常消息标题
	 * @param script 后续执行脚本
	 * @param params 返回供前台使用的JSON参数
	 * @return
	 */
	public static JSONObject toJsonExceptionObj(String errMsg, String title,
			String script, JSONObject params) {
		return toJsonExceptionObj(errMsg, title, script, params, true);
	}

	/**
	 * 
	 * 构建异常消息的JSON格式对象
	 * 
	 * @param errMsg 异常消息
	 * @param title 异常消息标题
	 * @param script 后续执行脚本
	 * @param params 返回供前台使用的JSON参数
	 * @param showMsg 是否显示消息框
	 * @return
	 */
	public static JSONObject toJsonExceptionObj(String errMsg, String title,
			String script, JSONObject params, boolean showMsg) {
		JSONObject jso = new JSONObject();
		try {
			if (!StringUtils.trimToEmpty(title).isEmpty())
				jso.put(TITLE, title);
			jso.put(ERROR_MSG, errMsg);
			jso.put(SCRIPT, script);
			jso.put(TYPE, EXCEPTION);
			jso.put(SHOW_MSG, showMsg);
			if (params != null)
				jso.put(JSON_PARAMS, params);
		} catch (JSONException e) {
			jso = createErrorJson(showMsg);
		}
		return jso;
	}

	/**
	 * 执行Json验证对象
	 * 
	 * @param isValid 是否验证成功
	 * @param errMsg 验证失败的错误消息
	 * @return
	 */
	public static JSONObject toJsonValidObj(boolean isValid, String errMsg) {
		JSONObject jso = new JSONObject();
		try {
			jso.put(VALID, isValid);
			jso.put(ERROR_MSG, errMsg);
			jso.put(TYPE, VALIDATE);
		} catch (JSONException e) {
			jso = createErrorJson(true);
		}
		return jso;
	}

	/**
	 * 对于Ext的iframe形式的仿Ajax的返回消息的包装
	 * 
	 * @param result response结果
	 * @return
	 */
	public static String wrapMultipartFormResponse(String result) {
		return "<div>" + StringEscapeUtils.escapeHtml(result) + "</div>";
	}

	/**
	 * 创建错误消息的JSON对象
	 * 
	 * @return
	 */
	private static JSONObject createErrorJson(boolean showMsg) {
		JSONObject jso = new JSONObject();
		try {
			jso.put(ERROR_MSG, "服务器异常：构造验证消息对象时出现异常！");
			jso.put(TYPE, ERROR);
			jso.put(SHOW_MSG, showMsg);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return jso;
	}

	/**
	 * 返回的消息类型：错误
	 */
	private static final String ERROR = "ERROR";

	/**
	 * 错误消息
	 */
	private static final String ERROR_MSG = "errMsg";

	/**
	 * 返回的消息类型：异常
	 */
	private static final String EXCEPTION = "EXCEPTION";

	/**
	 * 返回的附加参数
	 */
	private static final String JSON_PARAMS = "params";

	/**
	 * 后续执行的脚本
	 */
	private static final String SCRIPT = "script";

	/**
	 * 是否显示消息框
	 */
	private static final String SHOW_MSG = "showMsg";

	/**
	 * 返回的消息类型：成功
	 */
	private static final String SUCCESS = "SUCCESS";

	/**
	 * 成功消息
	 */
	private static final String SUCCESS_MSG = "msg";

	/**
	 * 消息标题
	 */
	private static final String TITLE = "title";

	/**
	 * 返回的消息类型
	 */
	private static final String TYPE = "type";

	/**
	 * 校验是否成功
	 */
	private static final String VALID = "valid";

	/**
	 * 返回的消息类型：校验
	 */
	private static final String VALIDATE = "VALIDATE";

}
