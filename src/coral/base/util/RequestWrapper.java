package coral.base.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import wfc.service.util.OrderedHashMap;

/**
 * HttpServletRequest对象包装器，方便操作二进制表单和普通表单，获取附件方式
 * 
 * @author 龚云
 * @see FileItem
 * @see RequestField
 */
public class RequestWrapper implements Serializable {

	public RequestWrapper() {

	}

	/**
	 * 对RequestWrapper(HttpServletRequest req, String encode) throws
	 * UnsupportedEncodingException方法的重载，默认指定了encode为UTF-8
	 * 
	 * @param req
	 *            待包装的HttpServletRequest对象
	 * @throws UnsupportedEncodingException
	 *             若根据指定编码encode解析错误会导致此异常
	 */
	public RequestWrapper(HttpServletRequest req) throws UnsupportedEncodingException {
		this(req, "UTF-8");
	}

	/**
	 * 根据传入的HttpServletRequest和编码encode构造内部参数缓存集合，并自动记录表单是否是multipart类型
	 * 
	 * @param req
	 *            待包装的HttpServletRequest对象
	 * @param encode
	 *            指定解析的编码
	 * @throws UnsupportedEncodingException
	 *             若根据指定编码encode解析错误会导致此异常
	 */
	@SuppressWarnings("unchecked")
	public RequestWrapper(HttpServletRequest req, String encode) throws UnsupportedEncodingException {
		req.setCharacterEncoding(encode);
		this.request = req;
		multipartContent = isMultipartContent(req);
		if (multipartContent) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(req);
				for (FileItem item : items) {
					String fieldName = item.getFieldName();
					List<RequestField> values = params.get(fieldName);
					if (values == null) {
						values = new ArrayList<RequestField>();
						params.put(fieldName, values);
					}
					if (item.isFormField()) {
						values.add(RequestField.create(fieldName, item.getString(encode)));
					} else {
						values.add(RequestField.create(fieldName, item));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (String k : (Set<String>) req.getParameterMap().keySet()) {
				List<RequestField> values = new ArrayList<RequestField>();
				String[] vals = req.getParameterValues(k);
				params.put(k, values);
				for (String val : vals) {
					values.add(RequestField.create(k, val));
				}
			}
		}
	}

	public RequestWrapper(Map<String, String[]> map) {
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (values != null && values.length > 0) {
				List<RequestField> fields = new ArrayList<RequestField>();
				for (String value : values) {
					RequestField requestField = new RequestField();
					requestField.setNormal(true);
					requestField.setName(key);
					requestField.setValue(value);
					fields.add(requestField);
				}
				this.params.put(key, fields);
			} else {
				RequestField requestField = new RequestField();
				requestField.setNormal(true);
				requestField.setName(key);
				requestField.setValue(null);
				List<RequestField> fields = new ArrayList<RequestField>();
				fields.add(requestField);
				this.params.put(key, fields);
			}
		}
	}

	/**
	 * 添加参数，遇到已存在的情况会抛出异常
	 * 
	 * @param key
	 * @param value
	 */
	public void addParam(String key, String value) {
		addParam(key, value, false);
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 * @param replace
	 *            若为false，在遇到已存在的情况会抛出异常，否则替换
	 */
	public void addParam(String key, String value, boolean replace) {
		if (StringUtils.trimToEmpty(key).isEmpty())
			throw new RuntimeException("key不能为空");
		if (this.params.containsKey(key) && !replace)
			throw new RuntimeException("已经存在同名的key:" + key);
		List<RequestField> l = new ArrayList<RequestField>();
		RequestField r = new RequestField();
		r.setNormal(true);
		r.setName(key);
		r.setValue(value);
		l.add(r);
		this.params.put(key, l);
	}

	/**
	 * 将maps中的值添加到参数中，遇到已存在的情况会抛出异常
	 * 
	 * @param maps
	 */
	public void addParams(Map<String, String[]> maps) {
		for (Entry<String, String[]> entry : maps.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			List<RequestField> l = new ArrayList<RequestField>();
			for (String value : values) {
				RequestField r = new RequestField();
				r.setNormal(true);
				r.setName(key);
				r.setValue(value);
				l.add(r);
			}
			if (this.params.containsKey(key))
				throw new RuntimeException("已经存在同名的key:" + key);
			this.params.put(key, l);
		}
	}

	/**
	 * 更具key判断request中对应的第一个value是否为null或空字符串， 本方法仅对普通parameter作判断
	 * 
	 * @param keys
	 *            需要作判断的key数组
	 * @return List<String>：若value判断为null的则将对应的传入的key加入此集合
	 */
	public List<String> getEmptyParameterKeys(String[] keys) {
		List<String> emptyKeys = new ArrayList<String>();
		for (String key : keys) {
			String o = getParameter(key);
			if (o == null || "".equals(o))
				emptyKeys.add(key);
		}
		return emptyKeys;
	}

	/**
	 * 获取文件，若key对应的文件有多个，只取第一个
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return {@link FileItem}
	 */
	public FileItem getFileItem(String key) {
		List<FileItem> l = getFileItems(key);
		if (l.size() > 0)
			return l.get(0);
		return null;
	}

	/**
	 * 获取文件集合
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return List<FileItem>：文件集合，{@link FileItem}
	 */
	public List<FileItem> getFileItems(String key) {
		List<RequestField> l = getRequestFields(key);
		List<FileItem> ls = new ArrayList<FileItem>();
		for (int i = 0; i < l.size(); i++) {
			RequestField o = l.get(i);
			if (o.isNormal())
				continue;
			FileItem item = o.getFileItem();
			if (item.isFormField())
				continue;
			ls.add(item);
		}
		return ls;
	}

	/**
	 * 更具key判断request中对应的第一个value是否为null， 本方法仅对普通parameter作判断
	 * 
	 * @param keys
	 *            需要作判断的key数组
	 * @return List<String>：若value判断为null的则将对应的传入的key加入此集合
	 */
	public List<String> getNullParameterKeys(String[] keys) {
		List<String> nullKeys = new ArrayList<String>();
		for (String key : keys) {
			String o = getParameter(key);
			if (o == null)
				nullKeys.add(key);
		}
		return nullKeys;
	}

	/**
	 * 获得key所对应的第一个普通参数的值
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return
	 */
	public String getParameter(String key) {
		List<String> l = getParameterValueList(key);
		if (l.size() > 0)
			return l.get(0);
		return null;
	}

	/**
	 * 获得参数对应的boolean值，若无key对应的参数，则返回null或defaultVal（由withoutNull决定），
	 * 若转换失败返回defaultVal
	 * 
	 * @param key
	 *            需要获取值的key
	 * @param defaultVal
	 *            默认值
	 * @param withoutNull
	 *            若无key对应的参数是否返回null
	 * @return
	 */
	public Boolean getParameterBoolean(String key, Boolean defaultVal, boolean withoutNull) {
		String val = getParameter(key);
		if (val == null) {
			if (withoutNull)
				return defaultVal;
			return null;
		}
		try {
			return Boolean.parseBoolean(val);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return defaultVal;
	}

	/**
	 * 获得参数对应的int值，若无key对应的参数，则返回null或defaultVal（由withoutNull决定），
	 * 若转换失败返回defaultVal
	 * 
	 * @param key
	 *            需要获取值的key
	 * @param defaultVal
	 *            默认值
	 * @param withoutNull
	 *            若无key对应的参数是否返回null
	 * @return
	 */
	public Integer getParameterInt(String key, Integer defaultVal, boolean withoutNull) {
		String val = getParameter(key);
		if (val == null) {
			if (withoutNull)
				return defaultVal;
			return null;
		}
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return defaultVal;
	}

	/**
	 * 获得参数对应的long值，若无key对应的参数，则返回null或defaultVal（由withoutNull决定），
	 * 若转换失败返回defaultVal
	 * 
	 * @param key
	 *            需要获取值的key
	 * @param defaultVal
	 *            默认值
	 * @param withoutNull
	 *            若无key对应的参数是否返回null
	 * @return
	 */
	public Long getParameterLong(String key, Long defaultVal, boolean withoutNull) {
		String val = getParameter(key);
		if (val == null) {
			if (withoutNull)
				return defaultVal;
			return null;
		}
		try {
			return Long.parseLong(val);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return defaultVal;
	}

	/**
	 * 获得key所对应的所有普通参数的值的集合
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return List<String>：普通参数的值的集合List<String>
	 */
	public List<String> getParameterValueList(String key) {
		List<RequestField> l = getRequestFields(key);
		List<String> ls = new ArrayList<String>();
		for (RequestField o : l) {
			if (o.isNormal())
				ls.add(o.getValue());
		}
		return ls;
	}

	/**
	 * 获得key所对应的所有普通参数的值数组
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return 普通参数的值数组
	 */
	public String[] getParameterValues(String key) {
		return getParameterValueList(key).toArray(new String[] {});
	}

	/**
	 * 获得每个key所对应的第一个普通参数的值，构成数组
	 * 
	 * @param keys
	 *            需要获取值的key数组
	 * @return
	 */
	public String[] getParameters(String[] keys) {
		List<String> vals = new ArrayList<String>();
		for (String key : keys) {
			String o = getParameter(key);
			vals.add(o);
		}
		return vals.toArray(new String[] {});
	}

	/**
	 * 获得每个key所对应的所有普通参数的所有值，构成数组集合
	 * 
	 * @param keys
	 *            需要获取值的key数组
	 * @return List<String[]>：每个List的项为对应的那个key所对应的所有值的String数组
	 */
	public List<String[]> getParametersValueList(String[] keys) {
		List<String[]> vals = new ArrayList<String[]>();
		for (String key : keys) {
			String[] values = getParameterValues(key);
			vals.add(values);
		}
		return vals;
	}

	/**
	 * 获得每个key所对应的所有普通参数的所有值，构成二维数组
	 * 
	 * @param keys
	 *            需要获取值的key数组
	 * @return 第一维为每个key所对应的一组值，第二位为每个key所对应的所有值的数组
	 */
	public String[][] getParametersValues(String[] keys) {
		List<String[]> vals = getParametersValueList(keys);
		String[][] results = new String[vals.size()][];
		for (int i = 0; i < vals.size(); i++) {
			String[] values = vals.get(i);
			String[] subValues = Arrays.copyOf(values, values.length);
			results[i] = subValues;
		}
		return results;
	}

	/**
	 * 返回缓存的request中的所有参数集合
	 * 
	 * @return OrderedHashMap<String,
	 *         List<RequestField>>：Map的V为List<RequestField>类型
	 */
	public OrderedHashMap<String, List<RequestField>> getParams() {
		return params;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 获取key所对应的第一个找到的值
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return 值被包装为coral.base.util.RequestWrapper.RequestField类型
	 */
	public RequestField getRequestField(String key) {
		List<RequestField> l = getRequestFields(key);
		if (l.size() > 0)
			return l.get(0);
		return null;
	}

	/**
	 * 获取key所对应的所有值
	 * 
	 * @param key
	 *            需要获取值的key
	 * @return List<RequestField>:
	 *         值被包装为coral.base.util.RequestWrapper.RequestField类型
	 */
	public List<RequestField> getRequestFields(String key) {
		List<RequestField> l = params.get(key);
		if (l == null)
			return new ArrayList<RequestField>();
		return l;
	}

	/**
	 * 对应的request内容是否是multipart类型
	 * 
	 * @return 是multipart返回true，否则false
	 */
	public boolean isMultipartContent() {
		return multipartContent;
	}

	/**
	 * 更具key判断request中对应的value是否为null或空字符串， 本方法仅对普通parameter作判断<br>
	 * 注：若key对应的value存在多个值，只取第一个
	 * 
	 * @param keys
	 *            需要作判断的key数组
	 * @return keys数组中只要有一个判断为null或空字符串则返回true，否则false
	 */
	public boolean isParametersEmpty(String[] keys) {
		boolean b = false;
		for (String key : keys) {
			String o = getParameter(key);
			b |= (o == null || "".equals(o));
		}
		return b;
	}

	/**
	 * 更具key判断request中对应的value是否为null，区别于isRequestFieldsNull方法，
	 * 本方法仅对普通parameter作判断<br>
	 * 注：若key对应的value存在多个值，只取第一个
	 * 
	 * @param keys
	 *            需要作判断的key数组
	 * @return keys数组中只要有一个判断为null则返回true，否则false
	 */
	public boolean isParametersNull(String[] keys) {
		boolean b = false;
		for (String key : keys) {
			String o = getParameter(key);
			b |= (o == null);
		}
		return b;
	}

	/**
	 * 更具key判断request中对应的value是否为null，区别于isParametersNull方法，
	 * 本方法也对multipart中的附件做判断，而后者仅对普通parameter作判断<br>
	 * 注：若key对应的value存在多个值，只取第一个
	 * 
	 * @param keys
	 *            需要作判断的key数组
	 * @return keys数组中只要有一个判断为null则返回true，否则false
	 */
	public boolean isRequestFieldsNull(String[] keys) {
		boolean b = false;
		for (String key : keys) {
			RequestField o = getRequestField(key);
			b |= (o == null);
		}
		return b;
	}

	/**
	 * 添加参数，遇到已存在的情况会抛出异常
	 */
	public void mergeParams(RequestWrapper wrapper, MergeConflictMode mode) {
		OrderedHashMap<String, List<RequestField>> extParams = wrapper.getParams();
		for (int i = 0; i < extParams.size(); i++) {
			String paramName = extParams.getKey(i);
			List<RequestField> fields = extParams.getValue(i);
			List<RequestField> existFields = this.params.get(paramName);
			boolean conflict = existFields != null;
			if (conflict) {
				switch (mode) {
					case ERROR:
						throw new RuntimeException("已经存在同名的参数:" + paramName);
					case MERGE:
						existFields.addAll(fields);
						break;
					case OVERRIDE:
						this.params.put(paramName, fields);
						break;
					case IGNORE:
						break;
				}
			} else {
				this.params.put(paramName, fields);
			}
		}
	}

	/**
	 * 删除参数名和其对应的值
	 * 
	 * @param keys
	 */
	public void removeByKey(String... keys) {
		for (String key : keys) {
			this.params.remove(key);
		}
	}

	/**
	 * 重命名参数名
	 * 
	 * @param oldKey
	 *            旧名称
	 * @param newKey
	 *            新名称
	 */
	public void renameKey(String oldKey, String newKey) {
		List<RequestField> l = this.params.get(oldKey);
		if (l != null)
			this.params.put(newKey, l);
		this.removeByKey(oldKey);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Map<String, List<RequestField>> params = this.getParams();
		for (Entry<String, List<RequestField>> entry : params.entrySet()) {
			String key = entry.getKey();
			if (sb.length() != 0)
				sb.append(SystemConstant.LINE_SEPARATOR);
			List<RequestField> rList = entry.getValue();
			if (rList.size() > 0)
				sb.append(key).append(":[");
			for (int i = 0; i < rList.size(); i++) {
				RequestField r = rList.get(i);
				if (i != 0)
					sb.append(", ");
				if (r.isNormal())
					sb.append(r.getValue());
				else
					sb.append(r.getFileItem().toString());
			}
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 内部参数集合
	 */
	OrderedHashMap<String, List<RequestField>> params = new OrderedHashMap<String, List<RequestField>>();

	private HttpServletRequest request;

	/**
	 * 对应的request内容是否是multipart的
	 */
	private boolean multipartContent = false;

	/**
	 * 工具方法：判断某个req是否是multipart类型
	 * 
	 * @param req
	 *            待包装的HttpServletRequest对象
	 * @return 是multipart返回true，否则false
	 */
	public static boolean isMultipartContent(HttpServletRequest req) {
		return ServletFileUpload.isMultipartContent(req);
	}

	private static final long serialVersionUID = -4400666416717751678L;

	public static enum MergeConflictMode {

		ERROR, MERGE, IGNORE, OVERRIDE;

	}

	/**
	 * RequestWrapper对象所使用的值的包装器
	 * 
	 * @author 龚云
	 */
	public static class RequestField implements Serializable {

		private RequestField() {
		}

		public FileItem getFileItem() {
			return fileItem;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		/**
		 * 判断此包装器对应的内部值是附件{@link FileItem}还是普通String值
		 * 
		 * @return 若为普通String值返回true，否则false
		 */
		public boolean isNormal() {
			return normal;
		}

		public void setFileItem(FileItem fileItem) {
			this.fileItem = fileItem;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNormal(boolean normal) {
			this.normal = normal;
		}

		public void setValue(String value) {
			this.value = value;
		}

		private FileItem fileItem = null;

		private String name = "";

		private String value = null;

		private boolean normal = true;

		/**
		 * 工厂方法：创建附件类型的值包装器
		 * 
		 * @param name
		 *            参数名称
		 * @param f
		 *            参数值，{@link FileItem}
		 * @return
		 */
		public static RequestField create(String name, FileItem f) {
			RequestField r = new RequestField();
			r.name = name;
			r.normal = false;
			r.fileItem = f;
			return r;
		}

		/**
		 * 工厂方法：创建普通类型的值包装器
		 * 
		 * @param name
		 *            参数名称
		 * @param val
		 *            参数值
		 * @return
		 */
		public static RequestField create(String name, String val) {
			RequestField r = new RequestField();
			r.name = name;
			r.normal = true;
			r.value = val;
			return r;
		}

		private static final long serialVersionUID = -2114586419544213722L;

	}

}
