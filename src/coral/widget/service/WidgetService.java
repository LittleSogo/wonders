package coral.widget.service;

import org.json.JSONObject;

import wfc.service.database.Conditions;
import coral.base.data.DataPage;
import coral.base.data.MetadataTable;
import coral.base.data.Transformer4Request;
import coral.widget.data.DataSet;
import coral.widget.utils.ExtHelper;
import coral.widget.utils.ExtHelper.Page;

/**
 * 通用界面帮助接口
 * 
 * @author 龚云
 * 
 */
public interface WidgetService {

	/**
	 * 根据参数查找表
	 * 
	 * @param metadataTable
	 *            表元数据
	 * @param tf4r
	 *            查询参数
	 * @param page
	 *            分页信息
	 * @return
	 */
	DataPage query(MetadataTable metadataTable, Transformer4Request tf4r,
			Page page);

	/**
	 * 根据表名、字段名、查询条件、附加查询条件等查询
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldNames
	 *            字段名
	 * @param conds
	 *            查询条件
	 * @param suffix
	 *            附加查询条件
	 * @param pks
	 *            主键数组
	 * @param page
	 *            分页信息
	 * @return
	 */
	DataPage query(String tableName, String fieldNames, Conditions conds,
			String suffix, String[] pks, ExtHelper.Page page);

	/**
	 * 将数据集转换为JSON对象
	 * 
	 * @param dataSet
	 * @return
	 */
	JSONObject toJson(DataSet dataSet);

}
