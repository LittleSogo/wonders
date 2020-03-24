package coral.widget.utils.json;

/**
 * 转换器超类，将JSON对象中的值转换为指定类型的对象
 * 
 * @author 龚云
 * 
 */
public abstract class JsonToFieldConverter {

	private JsonToFieldConverter next;
	private JsonToFieldConverter previous;

	public JsonToFieldConverter() {
	}

	/**
	 * 指定本转换器的下一个转换器
	 * 
	 * @param next
	 *            下一个转换器
	 */
	public JsonToFieldConverter(JsonToFieldConverter next) {
		this.next = next;
	}

	/**
	 * 将JSON对象中的值jsonValue转换为指定类型fieldType的对象
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	public abstract Object convertField(Object jsonValue, Class<?> fieldType);

	/**
	 * 获取下一个转换器
	 * 
	 * @return
	 */
	public JsonToFieldConverter getNext() {
		return next;
	}

	/**
	 * 获取前一个转换器
	 * 
	 * @return
	 */
	public JsonToFieldConverter getPrevious() {
		return previous;
	}

	/**
	 * 指定下一个转换器继续执行转换，若没有下一个则抛出{@link RuntimeException}
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	protected Object next(Object jsonValue, Class<?> fieldType) {
		if (this.next == null) {
			throw new RuntimeException("值转换错误，值（" + jsonValue + "）的类型（"
					+ jsonValue.getClass() + "）无法转换为目标类型（" + fieldType + "）");
		}
		return this.next.convertField(jsonValue, fieldType);
	}

	/**
	 * 设置下一个转换器
	 * 
	 * @param next
	 *            下一个转换器
	 */
	public void setNext(JsonToFieldConverter next) {
		this.next = next;
		if (next != null && next.getPrevious() != this)
			next.setPrevious(this);
	}

	/**
	 * 设置上一个转换器
	 * 
	 * @param previous
	 *            上一个转换器
	 */
	public void setPrevious(JsonToFieldConverter previous) {
		this.previous = previous;
		if (previous != null && previous.getNext() != this)
			previous.setNext(this);
	}

}
