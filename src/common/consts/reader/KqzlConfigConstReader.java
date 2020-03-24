package common.consts.reader;

import common.consts.KqzlConfigConst;

public class KqzlConfigConstReader {

	private String healthInfluence;
	private String measures;
	private String indexImgSrcUrl; // 首页上的 实景照片的读取路径设置
	private String indexImgBasePath; // 首页上的 实景照片的读取物理路径设置

	public KqzlConfigConstReader() {
		initKqzlConfigInfo();
	}

	private void initKqzlConfigInfo() {
		KqzlConfigConst kqzlConfiConst = new KqzlConfigConst();
		this.healthInfluence = kqzlConfiConst
				.getConstName("kqzl.healthInfluence");
		this.measures = kqzlConfiConst.getConstName("kqzl.measures");
		this.indexImgSrcUrl = kqzlConfiConst
				.getConstName("kqzl.indexImgSrcUrl");
		this.indexImgBasePath = kqzlConfiConst
				.getConstName("kqzl.indexImgBasePath");
	}

	/**
	 * 根据grade来得到 对健康影响
	 * 
	 * @param grade
	 *            从 1 开始
	 * @return
	 */
	public String getHealthInfluenceByAqi(int aqi) {
		String result = "";
		String[] influence = this.healthInfluence.split(";");
		int i = getReaderNum(aqi);

		result = influence[i];
		return result;
	}

	private int getReaderNum(int aqi) {
		int i = 0;
		if (aqi >= 0 && aqi <= 50) {
			i = 0;
		} else if (aqi <= 100) {
			i = 1;
		} else if (aqi <= 150) {
			i = 2;
		} else if (aqi <= 200) {
			i = 3;
		} else if (aqi <= 300) {
			i = 4;
		} else if (aqi > 300) {
			i = 5;
		}
		return i;
	}

	public String getMeasureByAqi(int aqi) {
		String result = "";
		String[] measures = this.measures.split(";");
		int i = getReaderNum(aqi);

		result = measures[i];
		return result;
	}

	public String getIndexImgSrcUrl() {
		return indexImgSrcUrl;
	}

	public String getIndexImgBasePath() {
		return indexImgBasePath;
	}

}
