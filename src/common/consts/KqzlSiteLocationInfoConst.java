package common.consts;

import java.util.HashMap;
import java.util.Map;

public class KqzlSiteLocationInfoConst {
	private Map<String, SiteLocation> subareaSiteLocationInfoMap = new HashMap<String, SiteLocation>();
	
	//用于保存各区地图的名字，站点位置信息
	public class SiteLocation {
		private String mapName;         //地图名
		private String[] locationClassName;   //各站点位置
		public SiteLocation(String mapName, String[] locationClassName) {
			this.mapName = mapName;
			this.locationClassName = locationClassName;
		}
		public String getMapName() {
			return mapName;
		}
		public String[] getLocationClassName() {
			return locationClassName;
		}
	}
	
	public KqzlSiteLocationInfoConst() {
		init();
	}

	private void init() {
		//浦东新区  201
		SiteLocation pudong = new SiteLocation("pudong", new String[] {"qy1","qy2","qy3","qy4","qy5"});
		subareaSiteLocationInfoMap.put("201", pudong);
		
		//黄埔区  202
		SiteLocation huangpu = new SiteLocation("huangpu", new String[] {"qy6","qy7","qy8"});
		subareaSiteLocationInfoMap.put("202", huangpu);
		
		
		  //徐汇区 204 
		  SiteLocation xuhui = new SiteLocation("xuhui", new String[]{"qy49","qy50","qy51"}); 
		  subareaSiteLocationInfoMap.put("204", xuhui);
		 
		
		//静安区  203
		SiteLocation jingan = new SiteLocation("jingan", new String[] {"qy30","qy31","qy32","qy33"});
		subareaSiteLocationInfoMap.put("203", jingan);
		
		//长宁区  205
		SiteLocation changning = new SiteLocation("changning", new String[] {"qy12","qy13","qy14"});
		subareaSiteLocationInfoMap.put("205", changning);
		
		//普陀区  206
		SiteLocation putuo = new SiteLocation("putuo", new String[] {"qy41","qy42"});
		subareaSiteLocationInfoMap.put("206", putuo);
		
		//虹口区  208
		SiteLocation hongkou = new SiteLocation("hongkou", new String[] {"qy24","qy25"});
		subareaSiteLocationInfoMap.put("208", hongkou);
		
		//杨浦区  209
		SiteLocation yangpu = new SiteLocation("yangpu", new String[] {"qy52","qy53","qy54"});
		subareaSiteLocationInfoMap.put("209", yangpu);
		
		//宝山区  210
		SiteLocation baoshan = new SiteLocation("baoshan", new String[] {"qy9","qy10","qy11"});
		subareaSiteLocationInfoMap.put("210", baoshan);
		
		//闵行区  211
		SiteLocation minhang = new SiteLocation("minhang", new String[] {"qy38","qy39","qy40"});
		subareaSiteLocationInfoMap.put("211", minhang);
		
		//嘉定区  212
		SiteLocation jiading = new SiteLocation("jiading", new String[] {"qy26","qy27","qy28","qy29"});
		subareaSiteLocationInfoMap.put("212", jiading);
		
		//金山区  213
		SiteLocation jinshan = new SiteLocation("jinshan", new String[] {"qy34","qy35","qy36","qy37"});
		subareaSiteLocationInfoMap.put("213", jinshan);
		
		//松江区  214
		SiteLocation songjiang = new SiteLocation("songjiang", new String[] {"qy46","qy47","qy48"});
		subareaSiteLocationInfoMap.put("214", songjiang);
		
		//青浦区  215
		SiteLocation qingpu = new SiteLocation("qingpu", new String[] {"qy43","qy44","qy45"});
		subareaSiteLocationInfoMap.put("215", qingpu);
		
		//奉贤区  216
		SiteLocation fengxian = new SiteLocation("fengxian", new String[] {"qy20","qy21","qy22","qy23"});
		subareaSiteLocationInfoMap.put("216", fengxian);
		
		//崇明区  217
		SiteLocation chongming = new SiteLocation("chongming", new String[] {"qy15","qy16","qy17","qy18","qy19"});
		subareaSiteLocationInfoMap.put("217", chongming);
		
	}

	public Map<String, SiteLocation> getSubareaSiteLocationInfoMap() {
		return subareaSiteLocationInfoMap;
	}
	
	
}
