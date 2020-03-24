package coral.base.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.json.JSONObject;

import wfc.service.util.OrderedHashMap;
import wfc.service.util.OrderedHashSet;

public class DataRow extends OrderedHashMap<String, Object> {

	private static final long serialVersionUID = -570516748807096427L;

	private OrderedHashSet<String> pkList = new OrderedHashSet<String>();

	public OrderedHashSet<String> getPkList() {
		return pkList;
	}

	public void set(String fieldName, Object obj) {
		if (obj == null || obj instanceof String || obj instanceof BigDecimal
				|| obj instanceof Timestamp || obj instanceof Number) {
			if (obj instanceof Number)
				obj = new BigDecimal(obj + "");
			put(fieldName, obj);
		} else {
			put(fieldName, obj.getClass().getName());
			// throw new DataException("不支持的数据类型：" + obj.getClass().getName());
		}
	}

	public Object get(String fieldName) {
		return super.get(fieldName);
	}

	public String getString(String fieldName) {
		return (String) get(fieldName);
	}

	public BigDecimal getBigDecimal(String fieldName) {
		return (BigDecimal) get(fieldName);
	}

	public Timestamp getTimestamp(String fieldName) {
		return (Timestamp) get(fieldName);
	}

	public JSONObject toJson() {
		try {
			JSONObject json = new JSONObject();
			for (int i = 0; i < this.size(); i++) {
				String fieldName = this.getKey(0);
				Object value = this.get(fieldName);
				if (value == null) {
					continue;
				}
				if (value instanceof Timestamp) {
					Timestamp ts = (Timestamp) value;
					json.put(fieldName, ts.getTime());
				} else {
					json.put(fieldName, value);
				}
			}
			return json;
		} catch (Exception ex) {
			throw new DataException(ex);
		}
	}

}
