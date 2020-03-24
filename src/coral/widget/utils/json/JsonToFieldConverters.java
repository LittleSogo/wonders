package coral.widget.utils.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换器收集者：使用若干个{@link JsonToFieldConverter}完成转换。
 * 
 * @author 龚云
 * 
 */
public class JsonToFieldConverters {

	private static List<Class<? extends JsonToFieldConverter>> DEFAULT_CONVERTERS_CLASSES = getDefaultTemplateConverterClasses();

	/**
	 * 获取默认的转换器集合
	 * 
	 * @return 转换器集合{@link List}<{@link Class}<? extends
	 *         {@link JsonToFieldConverter}>>
	 */
	@SuppressWarnings("unchecked")
	public static List<Class<? extends JsonToFieldConverter>> getDefaultTemplateConverterClasses() {
		List<Class<? extends JsonToFieldConverter>> classList = new ArrayList();
		classList.add(NullConverter.class);
		classList.add(StringConverter.class);
		classList.add(IntegerConverter.class);
		classList.add(LongConverter.class);
		classList.add(DoubleConverter.class);
		classList.add(FloatConverter.class);
		classList.add(BooleanConverter.class);
		classList.add(CharacterConverter.class);
		classList.add(ByteConverter.class);
		classList.add(ShortConverter.class);
		classList.add(BigDecimalConverter.class);
		classList.add(DateConverter.class);
		classList.add(NumberConverter.class);
		return classList;
	}

	public static JsonToFieldConverters newInstance() {
		return newInstance(DEFAULT_CONVERTERS_CLASSES);
	}

	/**
	 * 指定自定义的转换器类型集合，自定义的转换器必须继承自 {@link JsonToFieldConverter}，本方法使用无参的构造函数构造
	 * {@link JsonToFieldConverter}示例对象集合
	 * 
	 * @param converterClasses
	 *            转换器集合{@link List}<{@link Class}<? extends
	 *            {@link JsonToFieldConverter}>>
	 * @return
	 */
	public static JsonToFieldConverters newInstance(
			List<Class<? extends JsonToFieldConverter>> converterClasses) {
		JsonToFieldConverters _this = new JsonToFieldConverters();
		_this.converterClasses = converterClasses;
		_this.initConverters();
		return _this;
	}

	/**
	 * 指定自定义的转换器集合，自定义的转换器必须继承自 {@link JsonToFieldConverter}
	 * 
	 * @param jsonToFieldConverterArr
	 *            转换器集合 {@link JsonToFieldConverter}[]
	 * @return
	 */
	public static JsonToFieldConverters newInstance(
			JsonToFieldConverter[] jsonToFieldConverterArr) {
		JsonToFieldConverters _this = new JsonToFieldConverters();
		_this.jsonToFieldConverterArr = jsonToFieldConverterArr;
		_this.initConverters();
		return _this;
	}

	/**
	 * 若使用转换器类型集合方式构造本对象，那么此属性将保存初始的转换器集合
	 */
	private List<Class<? extends JsonToFieldConverter>> converterClasses;

	/**
	 * 若使用转换器对象集合的方式构造本对象，那么此属性将保存初始的转换器对象集合
	 */
	private JsonToFieldConverter[] jsonToFieldConverterArr;

	/**
	 * 转换器类型对应实例的结合，用于替换转换器时使用
	 */
	private Map<Class<? extends JsonToFieldConverter>, JsonToFieldConverter> converterMap = new ConcurrentHashMap<Class<? extends JsonToFieldConverter>, JsonToFieldConverter>();

	/**
	 * 第一个转换器
	 */
	private JsonToFieldConverter firstConvert;

	/**
	 * 最后一个转换器
	 */
	private JsonToFieldConverter lastConvert;

	private JsonToFieldConverters() {
	}

	/**
	 * 获取入口转换器
	 * 
	 * @return 入口转换器
	 */
	public JsonToFieldConverter getConverter() {
		if (firstConvert != null)
			return firstConvert;
		throw new RuntimeException("找不到转换器，请至少指定一个转换器。");
	}

	/**
	 * 初始化转换器集合<br>
	 * 1.若converterClasses私有域不为空则调用initConverters(List<Class<? extends
	 * JsonToFieldConverter>> converterClasses)初始化<br>
	 * 2.若jsonToFieldConverterArr私有域不为空则调用initConverters(JsonToFieldConverter[]
	 * jsonToFieldConverterArr)
	 */
	public void initConverters() {
		if (converterClasses != null && !converterClasses.isEmpty())
			initConverters(converterClasses);
		else if (jsonToFieldConverterArr != null
				&& jsonToFieldConverterArr.length > 0)
			initConverters(jsonToFieldConverterArr);
	}

	/**
	 * 根据converterClasses实例化出转换器实例{@link JsonToFieldConverter}
	 * 集合并调用initConverters(JsonToFieldConverter[] jsonToFieldConverterArr)
	 * 方法完成初始化
	 * 
	 * @param converterClasses
	 *            转换器类型集合{@link List}<{@link Class}<? extends
	 *            {@link JsonToFieldConverter}>>
	 */
	protected void initConverters(
			List<Class<? extends JsonToFieldConverter>> converterClasses) {
		List<JsonToFieldConverter> converterList = new ArrayList<JsonToFieldConverter>();
		for (Class<? extends JsonToFieldConverter> converterClass : converterClasses) {
			try {
				converterList.add(converterClass.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException("无法实例化转换器：找不到转换器" + converterClass
						+ "的无参构造函数。");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("无法实例化转换器：找不到转换器" + converterClass
						+ "的无参构造函数。");
			}
		}
		initConverters(converterList.toArray(new JsonToFieldConverter[] {}));
	}

	/**
	 * 根据转换器集合jsonToFieldConverterArr，初始化转换器以及它们之间的关系，按照自然顺序组织它们前后关系，形成类似链表的结构
	 * 
	 * @param jsonToFieldConverterArr
	 *            转换器实例数组
	 */
	protected void initConverters(JsonToFieldConverter[] jsonToFieldConverterArr) {
		converterMap.clear();
		for (int i = 0; i < jsonToFieldConverterArr.length - 1; i++) {
			JsonToFieldConverter converter = jsonToFieldConverterArr[i];
			if (i == 0)
				firstConvert = converter;
			Class<? extends JsonToFieldConverter> converterClass = converter
					.getClass();
			if (!converterMap.containsKey(converterClass)) {
				converterMap.put(converterClass, converter);
			}
			if (i == jsonToFieldConverterArr.length - 1) {
				lastConvert = converter;
				break;
			}
			JsonToFieldConverter nextConverter = jsonToFieldConverterArr[i + 1];
			converter.setNext(nextConverter);
		}
	}

	/**
	 * 替换掉oldConverterClass类型的转换器实例为newJsonToFieldConverter
	 * 
	 * @param oldConverterClass
	 *            需要替换掉的转换器类型{@link Class}<? extends
	 *            {@link JsonToFieldConverter}>
	 * @param newJsonToFieldConverter
	 *            新的转换器实例
	 * @return
	 */
	public boolean replaceConverter(
			Class<? extends JsonToFieldConverter> oldConverterClass,
			JsonToFieldConverter newJsonToFieldConverter) {
		if (converterMap.containsKey(oldConverterClass)) {
			Class<? extends JsonToFieldConverter> newConverterClass = newJsonToFieldConverter
					.getClass();
			JsonToFieldConverter oldConverter = converterMap
					.get(oldConverterClass);
			if (converterMap.containsKey(newConverterClass)) {
				JsonToFieldConverter newConverterClassOldConverter = converterMap
						.get(newConverterClass);
				if (newConverterClassOldConverter == firstConvert)
					newConverterClassOldConverter.getNext().setPrevious(null);

				if (newConverterClassOldConverter == lastConvert)
					newConverterClassOldConverter.getPrevious().setNext(null);

				if (newConverterClassOldConverter != firstConvert
						&& newConverterClassOldConverter != lastConvert)
					newConverterClassOldConverter.getPrevious().setNext(
							newConverterClassOldConverter.getNext());
				converterMap.remove(newConverterClass);
			}
			if (oldConverter == firstConvert)
				firstConvert = newJsonToFieldConverter;
			if (oldConverter == lastConvert)
				lastConvert = newJsonToFieldConverter;
			if (oldConverter != firstConvert)
				newJsonToFieldConverter.setPrevious(oldConverter.getPrevious());
			if (oldConverter != lastConvert)
				newJsonToFieldConverter.setNext(oldConverter.getNext());
			converterMap.remove(oldConverterClass);
			converterMap.put(newConverterClass, newJsonToFieldConverter);
			return true;
		}
		return false;
	}

	/**
	 * 获取所有转换器数量
	 * 
	 * @return 转换器数量
	 */
	public int size() {
		return converterMap.size();
	}
}
