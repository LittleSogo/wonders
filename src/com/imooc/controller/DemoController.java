package com.imooc.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.ReturnData;
import com.imooc.model.TPerson;
import com.imooc.service.PersonService;
import com.wonders.shiro.controller.BaseController;
import common.utils.MWYHandleDaoUtil;
import coral.base.util.RequestWrapper;
import coral.widget.data.DataSet;
import coral.widget.data.RowSet;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.model.People;
import com.imooc.service.DemoService;
import wfc.facility.tool.autocode.PaginationArrayList;
import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.database.RecordSet;
import wfc.service.util.OrderedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author zj
 *
 *	测试是否搭建成功
 *
 * 继承baseController 用于捕获异常处理
 */


@Controller
public class DemoController extends BaseController {
	
		@Resource
		private DemoService demoService;

		@Resource
		private PersonService personService;


	/**
	 * 在这里添加权限验证
	 *
	 *
	 * @param request
	 * @param tPerson
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequiresRoles({"vip"}) //没有的话 AuthorizationException
	@RequestMapping("/test")
	@ResponseBody
	public Object test(HttpServletRequest request,TPerson tPerson) throws UnsupportedEncodingException {

		//RequestWrapper requestWrapper = new RequestWrapper(request);

		//requestWrapper.getParameter("limit");

		//参数
		String departmentname = request.getParameter("departmentname");

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select * from t_person t where 1=1 ");

		if (!"".equals(departmentname)&& departmentname!=null){
			stringBuffer.append(" and t.t_name like '%"+departmentname+"%'");
		}

		String limit = request.getParameter("limit");

		String offset = request.getParameter("offset");

		//Conditions conds = Conditions.newAndConditions();

		//conds.add(new Condition("t_name",Condition.OT_LIKE,departmentname));

		int pageValue = Integer.parseInt(offset)/Integer.parseInt(limit)+1;

		RecordSet recordSet = MWYHandleDaoUtil.sqlExcute(stringBuffer.toString(), Integer.parseInt(limit), pageValue, null);

		PaginationArrayList paginationArrayList = new PaginationArrayList(recordSet.TOTAL_RECORD_COUNT, recordSet.COMMON_PAGE_SIZE, recordSet.CURRENT_PAGE);

		DataSet convert = DataSet.convert(recordSet);

		List<RowSet> rowSetList = convert.getRows();
		//存值
		for (RowSet rowSet : rowSetList) {
			OrderedHashMap oldrowMap = rowSet.getRow();
			OrderedHashMap rowMap = new OrderedHashMap();
			rowMap.putAll(oldrowMap);
			paginationArrayList.add(rowMap);
		}

		ReturnData<TPerson> peopleData = new ReturnData<TPerson>();

		peopleData.setTotal(recordSet.TOTAL_RECORD_COUNT);

		peopleData.setRows(paginationArrayList);

		return  peopleData;
	}

	/*//得到总页数
		ReturnData<TPerson> peopleData = new ReturnData<TPerson>();
		tPerson.getPageNumber();

	int totle = personService.getAll().size();
		peopleData.setTotal(totle);
	//得到user数据对象
	List<TPerson> plist = this.personService.getPaginationList(stringBuffer.toString(),tPerson.getPageNumber(),tPerson.getOffset());
		peopleData.setRows(plist);*/

	/*@RequestMapping("/test")
	@ResponseBody
	public Object test(HttpServletRequest request, HttpServletResponse response,TPerson tPerson){
		ReturnData<TPerson> peopleData = new ReturnData<TPerson>();
		//得到总页数
		int totle = personService.getAll().size();
		peopleData.setTotal(totle);
		//得到user数据对象
		List<TPerson> plist = personService.findByPage(tPerson);
		peopleData.setRows(plist);
		return  peopleData;
	}*/

	@RequestMapping("/list")
	@ResponseBody
	public List<People> getListBySql(){
		String sql ="select * from people limit 0,5";
		return demoService.findListBySql(sql);
	}

}
