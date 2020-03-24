package coral.base.data;

import coral.base.util.RequestWrapper;
import coral.base.util.RequestWrapper.MergeConflictMode;
import coral.base.util.RequestWrapper.RequestField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import wfc.service.database.Conditions;
import wfc.service.util.CloneHelper;
import wfc.service.util.OrderedHashMap;

public class Transformer4RequestWrapper extends WfcTransformer4Base {

	public Transformer4RequestWrapper(RequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
	}

	@Override
	public String getParameter(String name) {
		return requestWrapper.getParameter(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		return requestWrapper.getParameterValues(name);
	}

	public RequestWrapper getRequestWrapper() {
		return requestWrapper;
	}

	/**
	 * @see wfc.facility.tool.autocode.Transformer4Base#toBean(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object toBean(Object bean, Class<?> clazz) {
		return super.toBean(CloneHelper.deepClone(bean), clazz);
	}

	public Map<Class<?>, Object> toBean4Pojos(Map<Class<?>, Object> pojoCls2BeanMap) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(pojoCls2BeanMap);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(false, null);
		return toBean4Pojos(t4rMap, tableMap, pojoCls2BeanMap);
	}

	public Map<Class<?>, Object> toBean4Pojos(MergeConflictMode mode, Map<Class<?>, Object> pojoCls2BeanMap) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(pojoCls2BeanMap);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(true, mode);
		return toBean4Pojos(t4rMap, tableMap, pojoCls2BeanMap);
	}

	public Map<Class<?>, Object> toBean4Pojos(Class... classes) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(classes);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(false, null);
		return toBean4Pojos(t4rMap, tableMap, null);
	}

	public Map<Class<?>, Object> toBean4Pojos(MergeConflictMode mode, Class... classes) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(classes);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(true, mode);
		return toBean4Pojos(t4rMap, tableMap, null);
	}

	public Map<Class<?>, Conditions> toConditions4Pojos(Class... classes) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(classes);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(false, null);
		return toConditionsByPojos(t4rMap, tableMap);
	}

	public Map<Class<?>, Conditions> toConditions4Pojos(MergeConflictMode mode, Class... classes) {
		Map<String, Class<?>> tableMap = getTableNames4Pojos(classes);
		Map<String, Transformer4RequestWrapper> t4rMap = toTransformerByDotInName(true, mode);
		return toConditionsByPojos(t4rMap, tableMap);
	}

	public Map<String, Transformer4RequestWrapper> toTransformerByDotInName() {
		return toTransformerByDotInName(false, null);
	}

	public Map<String, Transformer4RequestWrapper> toTransformerByDotInName(MergeConflictMode mode) {
		return toTransformerByDotInName(true, mode);
	}

	public Map<String, Transformer4RequestWrapper> toTransformerByDotInName(boolean mergeNonDotParams,
			MergeConflictMode mode) {
		Map<String, RequestWrapper> wrapperMap = new HashMap<String, RequestWrapper>();
		RequestWrapper nonDotWrapper = new RequestWrapper();
		wrapperMap.put(StringUtils.EMPTY, nonDotWrapper);

		OrderedHashMap<String, List<RequestField>> params = requestWrapper.getParams();
		for (int i = 0; i < params.size(); i++) {
			String paramName = params.getKey(i);
			String tableName = StringUtils.EMPTY;
			if (paramName.contains(".")) {
				tableName = StringUtils.substringBefore(paramName, ".");
				paramName = StringUtils.substringAfter(paramName, ".");
			}
			RequestWrapper wrapper = wrapperMap.get(tableName);
			if (wrapper == null) {
				wrapper = new RequestWrapper();
				wrapperMap.put(tableName, wrapper);
			}
			wrapper.getParams().put(paramName, params.getValue(i));
		}

		Map<String, Transformer4RequestWrapper> t4rMap = new HashMap<String, Transformer4RequestWrapper>();
		for (Map.Entry<String, RequestWrapper> entry : wrapperMap.entrySet()) {
			String tableName = entry.getKey();
			RequestWrapper wrapper = entry.getValue();

			if (!StringUtils.EMPTY.equals(tableName) && mergeNonDotParams) {
				wrapper.mergeParams(nonDotWrapper, mode);
			}
			Transformer4RequestWrapper t4r = new Transformer4RequestWrapper(wrapper);
			t4rMap.put(tableName, t4r);
		}
		return t4rMap;
	}

	private Table getJPATableAnno(Class<?> klass) {
		Table table = klass.getAnnotation(Table.class);
		if (table == null || StringUtils.trimToEmpty(table.name()).isEmpty())
			throw new IllegalArgumentException("添加的类必须标注javax.persistence.Table，且表名不能为空");
		return table;
	}

	private Map<String, Class<?>> getTableNames4Pojos(Map<Class<?>, Object> pojoCls2BeanMap) {
		Map<String, Class<?>> tableMap = new HashMap<String, Class<?>>();
		if (pojoCls2BeanMap == null)
			return tableMap;
		for (Map.Entry<Class<?>, Object> entry : pojoCls2BeanMap.entrySet()) {
			Table table = getJPATableAnno(entry.getKey());
			tableMap.put(table.name(), entry.getKey());
		}
		return tableMap;
	}

	private Map<String, Class<?>> getTableNames4Pojos(Class<?>... classes) {
		Map<String, Class<?>> tableMap = new HashMap<String, Class<?>>();
		if (classes == null)
			return tableMap;
		for (Class<?> klass : classes) {
			Table table = getJPATableAnno(klass);
			tableMap.put(table.name(), klass);
		}
		return tableMap;
	}

	private Map<Class<?>, Object> toBean4Pojos(Map<String, Transformer4RequestWrapper> t4rMap,
			Map<String, Class<?>> tableMap, Map<Class<?>, Object> pojoCls2BeanMap) {
		Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
		for (Map.Entry<String, Transformer4RequestWrapper> entry : t4rMap.entrySet()) {
			String tableName = entry.getKey();
			Transformer4RequestWrapper t4r = entry.getValue();
			Class<?> pojoClass = tableMap.get(tableName);
			if (pojoClass == null)
				continue;

			Object bean = pojoCls2BeanMap == null ? null : pojoCls2BeanMap.get(pojoClass);
			bean = bean == null ? t4r.toBean(pojoClass) : t4r.toBean(bean, pojoClass);
			beanMap.put(pojoClass, bean);
		}
		return beanMap;
	}

	private Map<Class<?>, Conditions> toConditionsByPojos(Map<String, Transformer4RequestWrapper> t4rMap,
			Map<String, Class<?>> tableMap) {
		Map<Class<?>, Conditions> condsMap = new HashMap<Class<?>, Conditions>();
		for (Map.Entry<String, Transformer4RequestWrapper> entry : t4rMap.entrySet()) {
			String tableName = entry.getKey();
			Transformer4RequestWrapper t4r = entry.getValue();
			Class<?> pojoClass = tableMap.get(tableName);
			if (pojoClass == null)
				continue;

			condsMap.put(pojoClass, t4r.toConditions(pojoClass, tableName));
		}
		return condsMap;
	}

	private RequestWrapper requestWrapper;

}
