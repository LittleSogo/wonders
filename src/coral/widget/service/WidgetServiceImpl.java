package coral.widget.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wfc.service.database.Conditions;
import coral.base.data.DataDao;
import coral.base.data.DataPage;
import coral.base.data.MetadataService;
import coral.base.data.MetadataTable;
import coral.base.data.Transformer4Request;
import coral.widget.data.DataSet;
import coral.widget.utils.ExtHelper;
import coral.widget.utils.ExtHelper.Page;
import coral.widget.utils.MetaDataJsonConverter;

/**
 * 通用界面帮助类
 * 
 * @author 龚云
 * 
 */
@Service
@Transactional
public class WidgetServiceImpl implements WidgetService {

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
	@Override
	public DataPage query(MetadataTable metadataTable,
			Transformer4Request tf4r, Page page) {
		Conditions conds = tf4r.toConditions(metadataTable);
		String table = metadataTable.getName();
		DataDao dao = new DataDao(table);
		DataPage dataPage = dao.query(conds, page.getSuffix(), page
				.getPageSize(), page.getCurrentPage());
		dataPage.getPkList().clear();
		dataPage.getPkList().addAll(metadataService.getPkList(metadataTable));
		return dataPage;
	}

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
	@Override
	public DataPage query(String tableName, String fieldNames,
			Conditions conds, String suffix, String[] pks, ExtHelper.Page page) {
		DataDao dao = new DataDao(tableName);
		DataPage dataPage = dao.query(fieldNames, conds, suffix, page
				.getPageSize(), page.getCurrentPage());
		if (pks != null) {
			dataPage.getPkList().addArray(pks);
		}
		return dataPage;
	}

	/**
	 * 将数据集转换为JSON对象
	 * 
	 * @param dataSet
	 * @return
	 */
	@Override
	public JSONObject toJson(DataSet dataSet) {
		return MetaDataJsonConverter.convertToJson(dataSet);
	}

	@Autowired
	private MetadataService metadataService;
}
