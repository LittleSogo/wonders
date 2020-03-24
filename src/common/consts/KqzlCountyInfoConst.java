package common.consts;

import java.util.HashMap;
import java.util.Map;

public class KqzlCountyInfoConst {
	private Map<String, String> countys = new HashMap<String, String>();
	
	public KqzlCountyInfoConst() {
		init();
	}

	private void init() {
		countys.put("201", "浦东新区");
		countys.put("202", "黄浦区");
		countys.put("203", "静安区");
		countys.put("204", "徐汇区");
		countys.put("205", "长宁区");
		countys.put("206", "普陀区");
		countys.put("208", "虹口区");
		countys.put("209", "杨浦区");
		countys.put("210", "宝山区");
		countys.put("211", "闵行区");
		countys.put("212", "嘉定区");
		countys.put("213", "金山区");
		countys.put("214", "松江区");
		countys.put("215", "青浦区");
		countys.put("216", "奉贤区");
		countys.put("217", "崇明区");
	}

	public Map<String, String> getCountys() {
		return countys;
	}
	
}
