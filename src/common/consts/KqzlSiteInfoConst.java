package common.consts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class KqzlSiteInfoConst {
	private Map<String, Map<String, String>> kqzlSiteInfoMap = new HashMap<String, Map<String, String>>();

	public KqzlSiteInfoConst() {
		init();
	}

	private void init() {
		
		// 浦东周浦、浦东川沙、浦东张江、浦东惠南、浦东监测站
		Map<String, String> pudongStrs = new LinkedHashMap<String, String>();
		pudongStrs.put("88", "浦东周浦");
		pudongStrs.put("193", "浦东川沙");
		pudongStrs.put("195", "浦东张江");
		pudongStrs.put("217", "浦东惠南");
		pudongStrs.put("228", "浦东监测站");
		kqzlSiteInfoMap.put("201", pudongStrs); // 浦东新区

		// 人民广场、卢湾师专附小、黄浦瑞金
		Map<String, String> huangpuStrs = new LinkedHashMap<String, String>();
		huangpuStrs.put("179", "人民广场");
		huangpuStrs.put("185", "卢湾师专附小");
		huangpuStrs.put("186", "黄浦瑞金");
		kqzlSiteInfoMap.put("202", huangpuStrs); // 黄浦区

		// 静安监测站、静安七一中学、静安和田中学、静安市北高新
		Map<String, String> jinganStrs = new LinkedHashMap<String, String>();
		jinganStrs.put("183", "静安监测站");
		jinganStrs.put("184", "静安七一中学");
		jinganStrs.put("211", "静安和田中学");
		jinganStrs.put("263", "静安市北高新");
		kqzlSiteInfoMap.put("203", jinganStrs); // 静安区

		// 徐汇华泾 的数据没有！！！
		// 徐家汇、徐汇上师大、徐汇华泾
		Map<String, String> xuhuiStrs = new LinkedHashMap<String, String>();
		xuhuiStrs.put("80", "徐家汇");
		xuhuiStrs.put("207", "徐汇上师大");
		xuhuiStrs.put("1", "徐汇华泾");
		kqzlSiteInfoMap.put("204", xuhuiStrs); // 徐汇区
		
		// 长宁仙霞、长宁程桥、长宁华阳
		Map<String, String> changningStrs = new LinkedHashMap<String, String>();
		changningStrs.put("175", "长宁仙霞");
		changningStrs.put("233", "长宁程桥");
		changningStrs.put("262", "长宁华阳");
		kqzlSiteInfoMap.put("205", changningStrs); // 长宁区

		// 普陀监测站、普陀桃浦
		Map<String, String> putuoStrs = new LinkedHashMap<String, String>();
		putuoStrs.put("201", "普陀监测站");
		putuoStrs.put("260", "普陀桃浦");
		kqzlSiteInfoMap.put("206", putuoStrs); // 普陀区

		// 虹口嘉兴、虹口凉城
		Map<String, String> hongkouStrs = new LinkedHashMap<String, String>();
		hongkouStrs.put("79", "虹口嘉兴");
		hongkouStrs.put("215", "虹口凉城");
		kqzlSiteInfoMap.put("208", hongkouStrs); // 虹口区

		//// 杨浦四漂、杨浦监测站、杨浦新江湾城
		Map<String, String> yangpuStrs = new LinkedHashMap<String, String>();
		yangpuStrs.put("209", "杨浦四漂");
		yangpuStrs.put("210", "杨浦监测站");
		yangpuStrs.put("253", "杨浦新江湾城");
		kqzlSiteInfoMap.put("209", yangpuStrs); // 杨浦区

		// 宝山杨行、宝山罗店、宝山庙行
		Map<String, String> baoshanStrs = new LinkedHashMap<String, String>();
		baoshanStrs.put("172", "宝山杨行");
		baoshanStrs.put("266", "宝山罗店");
		baoshanStrs.put("267", "宝山庙行");
		kqzlSiteInfoMap.put("210", baoshanStrs); // 宝山区

		// 闵行莘庄、闵行江川、闵行浦江
		Map<String, String> minhangStrs = new LinkedHashMap<String, String>();
		minhangStrs.put("187", "闵行莘庄");
		minhangStrs.put("188", "闵行江川");
		minhangStrs.put("231", "闵行浦江");
		kqzlSiteInfoMap.put("211", minhangStrs); // 闵行区

		// 嘉定监测站、嘉定天华学院、嘉定方泰、嘉定南翔
		Map<String, String> jiadingStrs = new LinkedHashMap<String, String>();
		jiadingStrs.put("180", "嘉定监测站");
		jiadingStrs.put("238", "嘉定天华学院");
		jiadingStrs.put("250", "嘉定方泰");
		jiadingStrs.put("279", "嘉定南翔");
		kqzlSiteInfoMap.put("212", jiadingStrs); // 嘉定区

		// 金山朱泾、金山新城、金山廊下、金山枫泾
		Map<String, String> jinshanStrs = new LinkedHashMap<String, String>();
		jinshanStrs.put("218", "金山朱泾");
		jinshanStrs.put("248", "金山新城");
		jinshanStrs.put("268", "金山廊下");
		jinshanStrs.put("269", "金山枫泾");
		kqzlSiteInfoMap.put("213", jinshanStrs); // 金山区
		
		// 缺少数据：松江景德路！！！
		 // 松江景德路、松江图书馆、松江中辰路，表里显示的是 松江中山！！！
		Map<String, String> songjiangStrs = new LinkedHashMap<String, String>();
		songjiangStrs.put("2", "松江景德路");
		songjiangStrs.put("87", "松江图书馆");
		songjiangStrs.put("239", "松江中辰路");
		kqzlSiteInfoMap.put("214", songjiangStrs); // 松江区
		
		// 青浦夏阳、青浦朱家角、青浦盈浦
		Map<String, String> qingpuStrs = new LinkedHashMap<String, String>();
		qingpuStrs.put("204", "青浦夏阳");
		qingpuStrs.put("219", "青浦朱家角");
		qingpuStrs.put("261", "青浦盈浦");
		kqzlSiteInfoMap.put("215", qingpuStrs); // 青浦区

		// 奉贤南桥、奉贤海湾、奉贤南桥新城、奉贤奉浦
		Map<String, String> fengxianStrs = new LinkedHashMap<String, String>();
		fengxianStrs.put("76", "奉贤南桥");
		fengxianStrs.put("243", "奉贤海湾");
		fengxianStrs.put("264", "奉贤南桥新城");
		fengxianStrs.put("265", "奉贤奉浦");
		kqzlSiteInfoMap.put("216", fengxianStrs); // 奉贤区

		// 崇明城桥、崇明现代农业园区、崇明绿华、崇明森林公园，崇明上实东滩
		Map<String, String> chongmingStrs = new LinkedHashMap<String, String>();
		chongmingStrs.put("176", "崇明城桥");
		chongmingStrs.put("237", "崇明现代农业园区");
		chongmingStrs.put("247", "崇明绿华");
		chongmingStrs.put("249", "崇明森林公园");
		chongmingStrs.put("276", "崇明上实东滩");
		kqzlSiteInfoMap.put("217", chongmingStrs); // 崇明区

	}

	public Map<String, Map<String, String>> getKqzlSiteInfoMap() {
		return kqzlSiteInfoMap;
	}

}
