<%@ page pageEncoding="UTF-8"%>
<%@ include file="/common/hbj_include_init.inc"%>
<%@ page import="ottff.info.*,
			ottff.service.*,
			ottff.common.*,
			fa.sso.IUCPClientService,
			java.util.*,
			net.sf.json.JSONObject
" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<%
	IEventService eventService = (IEventService) currentCtx.Getbeans("eventService");
	IEventProcessService eventProcessService = (IEventProcessService) currentCtx.Getbeans("eventProcessService");
	IEventProcessDetailService eventProcessDetailService = (IEventProcessDetailService) currentCtx.Getbeans("eventProcessDetailService");
	IClassificationService classificationService = (IClassificationService) currentCtx.Getbeans("classificationService");
	IDepartmentService departmentService = (IDepartmentService) currentCtx.Getbeans("departmentService");
	IOttffFileService ottffFileService = (IOttffFileService) currentCtx.Getbeans("ottffFileService");
	IReportTypeService reportTypeService = (IReportTypeService) currentCtx.Getbeans("reportTypeService");
	ICfconsoleService cfconsoleService = (ICfconsoleService) currentCtx.Getbeans("cfconsoleServiceClient");
	PaginationUtil paginationUtil = (PaginationUtil) currentCtx.Getbeans("paginationUtil");
    IOttffTuidanService ottffTuidanService = (IOttffTuidanService) currentCtx.Getbeans("ottffTuidanService");
	IOttffYqapplyService ottffYqapplyService = (IOttffYqapplyService) currentCtx.Getbeans("ottffYqapplyService");
	IOttffYquotaService ottffYquotaService = (IOttffYquotaService) currentCtx.Getbeans("ottffYquotaService");
	IEventCallService eventCallService = (IEventCallService) currentCtx.Getbeans("eventCallService");
	
	//获取短信接口
	IUCPClientService ucpClientService = (IUCPClientService) currentCtx.Getbeans("ucpClientService");
	IEventDuplicateService eventDuplicateService = (IEventDuplicateService) currentCtx.Getbeans("eventDuplicateService");

	// 取得常量
	OttffConst ottffConst = new OttffConst();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 点击Tab
	String tab = StringUtils.getNull2String(request.getParameter("tab"));
	// 事项ID
	String id = StringUtils.getNull2String(request.getParameter("id"));
	//当前退单申请ID
	String ottfftuidanID = StringUtils.getNull2String(request.getParameter("ottfftuidanID"));
	
	//当前延期申请ID
	String ottffyqsqID = StringUtils.getNull2String(request.getParameter("ottffyqsqID"));
	String from = StringUtils.getNull2String(request.getParameter("from"));
	//获取当前页面联系电话
	String phoneNumber = StringUtils.getNull2String(request.getParameter("phoneNumber"));
	//区分事项反馈
	String comefrom = StringUtils.getNull2String(request.getParameter("comefrom"));
	
	//附件类型
	String filetype = StringUtils.getNull2String(request.getParameter("type"));
	// 当前登录用户ID
	String userId = (String)session.getAttribute("currentPersonId");
	// 应反时间
	String feedbackLimitTime = "";
	// 登记时间
	String releaseTime = "";
	// 签收时间
	String signForTime = "";
	// 反馈时间
	String feedbackTime = "";
	// 退回时间
	String sendBackTime = "";
	// 来电日期
	String callTime = "";
	// 回访复核应反时间
	String returnVisitLimitTime = "";
	// 回访复核现场查看日期
	String returnVisitTime = "";
	// 延期承诺时限
	String delayLimitTime = "";
	// 调查时间
	String satisfactionTime = "";
	// 状态
	String status = "0";
	// 举报性质
	String reportType = "1";
	// 是否需要上传反馈附件
	String needAttach = "0";
	// 是否打通电话
	String satisfactionCalled = "1";
	// 拒绝调查
	String satisfactionRefuse = "";
	// 工作人员是否先行联系
	String satisfactionMan = "";
	// 对处理结果是否满意
	String satisfactionResult = "";
	// 对工作态度是否满意
	String satisfactionAttitude = "";
	// 诉求解决
	String eventResult = "";
	// 重新交办
	String redo = "0";
	// 事实认定
	String factConfirm = "";
	// 事实认定说明
	String factConfirmExplain = "";
	// 现场查看
	String sceneCheck = "";
	// 诉求认定
	String appealConfirm = "";
	// 诉求认定说明
	String appealConfirmExplain = "";
	// 先行联系
	String firstContact = "";
	// 未联原因
	String nocontactReason = "";
	// 先行联系人
	String contactMan = "";
	// 联系时间
	String contactTime = "";
	// 答复市民要点
	String replyPoint = "";
	// 落款单位
	String inscribeUnit = "";
	// 市民反馈说明
	String citizenFeedback = "";
	// 答复方式
	String replyStyle = "电话";
	// 答复市民时间
	String replyTime = "";
	// 是否满意
	String replySatisfaction = "满意";
	// 是否公开
	String publicFlag = "是";
	// 现场查看时间
	String sceneTime = "";
	// 联系内容
	String contactContent = "";
	
	//预留字段3--被举报单位名称
	String stExchangeName3="";
	//预留字段4--被举报单位地址
	String stExchangeName4="";
	//预留字段5--联系内容备注
	String stExchangeName5="";

	//是否工业区
	String stFactory="";
	//工业区名称
	String stFactoryName="";
	String stReturnVisitFirstContact=""; //回访复核先行联系
	String stReturnNocontactReason="";//回访复核未联原因
	String stReturnVisitContactMan="";//回访复核先行联系人
	String dtReturnVisitContactTime="";//回访复核联系时间
	String stReturnContactContent="";//回访复核联系内容
	String remarkContactContent="";//回访复核联系内容备注
	String stReturnVisitFactConfirm="";//回访复核事实认定
	String stReturnFactConfirmExplain="";//回访复核事实认定说明
	String stReturnVisitSceneCheck="";//回访复核现场查看
	String stReturnVisitAppealConfirm="";//回访复核诉求认定
	String stReturnAppealExplain="";//回访复核诉求认定说明
	String beReportName ="";//回访复核被举报单位名称
	String beReportAddress = "";//回访复核被举报单位地址
	String stFactory1 = "";//回访复核是否是工业区
	String stFactoryName1="";//回访复核工业区名称
	String stReturnVisitReplyPoint="";//回访复核答复市民要点
	String stReturnVisitInscribeUnit="";//回访复核落款单位
	String stReturnCitizenFeedback="";//回访复核市民反馈说明
	String stReturnVisitReplyStyle="";//回访复核答复方式
	String dtReturnVisitReplyTime="";//回访复核答复市民时间
	String stReturnVisitEventResult="";//回访复核是否解决
    String stReturnReplySatisfaction="";//回访复核是否满意
    String stReturnPublicFlag="";//回访复核是否公开
	String returnVisitJbrName ="";//回访复核反馈人
	String returnVisitFeedback ="";//办结汇总
	String returnVisitFzrName="";//回访复核签发人
	String returnVisitSignForName="";//签收人
	String returnVisitPhoneNumber="";//联系电话
	
	String date1="";//派单时间（处理时限按此时间计算）
	String feedBackSendTime = "";//回访复核派单时间
	String acceptflag="";//主办标志位 0主办 1协办
    String requirement="";//处理描述（当发起回访复核时写明回访复核原因）
    String supervision="";//工单类型标示：0：转送单，1：督办单
    String deptLevel2="";//工单主办单位
    String priority="";//工单优先级标示：0：紧急，1：普通、
    String duLimit="";//督办时限(工作日)5
    String class3="";//内容分类3级
    String class4="";//内容分类4级
    String assignFlag="";//领导指派标志   0：普通单；1：领导指派单
    String backCount="";//已退单次数
    String phoneno="";//来电电话
	String relName="";//工单信息-联系人
    String mobile="";//市民移动电话
    String cardtype="";//市民证件类型
    String gender="";//来电市民性别(男、女、不明)
    String district="";//来电市民所在区域 
    String cardnum="";//市民证件号码
    String email="";//电子邮件
    String ishidden="";//是否匿名（0：否；1：是）
    String ispublic="";//是否公开（0：否；1：是）
    String isrepeat="";//是否重复（0：否；1：是）
    String rewpid="";//重复工单号
    String appealContent="";//市民原信内容
    String backDept1="";//第一次退单部门
    String backTime1="";//第一次退单时间
    String backReason1="";//第一次退单理由
    String backDept2="";//第二次退单部门
    String backTime2="";//第二次退单时
    String backReason2="";//第二次退单理由
    String backDept3="";//第三次退单部门
    String backTime3="";//第三次退单时间
    String backReason3="";//第三次退单理由
	String callbackFlag="";//是否回访复核
	String wpSource="";//工单来源
	String level2Charger="";//二级负责人
	String limitTime="";//办结时间
	String style="";//自办/转办
	String hurryFlag="";//催单
	String returnVisit="";//是否回访复核
	String stExchangeName1="";//预留字段1
	String stExchangeName2="";//预留字段2
	String yjjbStatus="";//有奖举报状态
	String wpCustomerType ="";//客户类型
	String wpServiceType ="";//服务类型
	// 附件数
	int fileNum = 0;
	EventInfo eventInfo = new EventInfo();
	EventProcessInfo eventProcessInfo = new EventProcessInfo();
	EventProcessDetailInfo eventProcessDetailInfo = new EventProcessDetailInfo();
	OttffFileInfo ottffFileInfo = new OttffFileInfo();
	// 附件列表
	List fileList = new ArrayList();
	// 事项流程
	String tuidanmessage="";
	String delaymessage="";
	List processList = new ArrayList();
	List callList = new ArrayList();
	if (!"".equals(id)) {
		eventInfo = eventService.getInfo(id);
		wpCustomerType = StringUtils.getNull2String(eventInfo.getWpCustomerType());// 客户类型
		wpServiceType = StringUtils.getNull2String(eventInfo.getWpServiceType());// 服务类型
		//是否退单
        tuidanmessage=eventService.isTuidan(eventInfo);
		delaymessage=eventService.isDelay(eventInfo);
		if (eventInfo.getFeedbackLimitTime() != null) {
			feedbackLimitTime = sdf4.format(eventInfo.getFeedbackLimitTime());
		}else{
            feedbackLimitTime=eventService.getFeedbackLimitTime(StringUtils.getNull2String(eventInfo.getLimitTime()), 2);
		}
		if (eventInfo.getReleaseTime() != null) {
			releaseTime = sdf2.format(eventInfo.getReleaseTime());
		}
	
		if (eventInfo.getSignForTime() != null) {
			signForTime = sdf2.format(eventInfo.getSignForTime());
		}
		if (eventInfo.getFeedbackTime() != null) {
			feedbackTime = sdf2.format(eventInfo.getFeedbackTime());
		}
		if (eventInfo.getSendBackTime() != null) {
			sendBackTime = sdf2.format(eventInfo.getSendBackTime());
		}
		if (eventInfo.getCallTime() != null) {
			callTime = sdf.format(eventInfo.getCallTime());
		}
		if (eventInfo.getReturnVisitLimitTime() != null) {
			returnVisitLimitTime = sdf.format(eventInfo.getReturnVisitLimitTime());
		}
		if (eventInfo.getReturnVisitTime() != null) {
			returnVisitTime = sdf.format(eventInfo.getReturnVisitTime());
		}
		if (eventInfo.getDelayLimitTime() != null) {
			delayLimitTime = sdf.format(eventInfo.getDelayLimitTime());
		}
		if (eventInfo.getSatisfactionTime() != null) {
			satisfactionTime = sdf.format(eventInfo.getSatisfactionTime());
		}
		status = eventInfo.getStatus();
		reportType = StringUtils.getNull2String(eventInfo.getReportType());
		needAttach = eventInfo.getNeedAttach();
		needAttach=needAttach==null?"0":needAttach;
		if (eventInfo.getSatisfactionCalled() != null && !"".equals(eventInfo.getSatisfactionCalled())) {
			satisfactionCalled = eventInfo.getSatisfactionCalled();
		}
		if (eventInfo.getSatisfactionRefuse() != null && !"".equals(eventInfo.getSatisfactionRefuse())) {
			satisfactionRefuse = eventInfo.getSatisfactionRefuse();
		}
		if (eventInfo.getSatisfactionMan() != null && !"".equals(eventInfo.getSatisfactionMan())) {
			satisfactionMan = eventInfo.getSatisfactionMan();
		}
		if (eventInfo.getSatisfactionResult() != null && !"".equals(eventInfo.getSatisfactionResult())) {
			satisfactionResult = eventInfo.getSatisfactionResult();
		}
		if (eventInfo.getSatisfactionAttitude() != null && !"".equals(eventInfo.getSatisfactionAttitude())) {
			satisfactionAttitude = eventInfo.getSatisfactionAttitude();
		}
		eventResult = StringUtils.getNull2String(eventInfo.getEventResult());
		if (eventInfo.getRedo() != null && !"".equals(eventInfo.getRedo())) {
			redo = eventInfo.getRedo();
		}
		factConfirm = StringUtils.getNull2String(eventInfo.getFactConfirm());// 事实认定
		factConfirmExplain = StringUtils.getNull2String(eventInfo.getFactConfirmExplain());// 事实认定说明
		sceneCheck = StringUtils.getNull2String(eventInfo.getSceneCheck());// 现场查看
		appealConfirm = StringUtils.getNull2String(eventInfo.getAppealConfirm());// 诉求认定
		appealConfirmExplain = StringUtils.getNull2String(eventInfo.getAppealConfirmExplain());// 诉求认定说明
		firstContact = StringUtils.getNull2String(eventInfo.getFirstContact());// 先行联系
		nocontactReason = StringUtils.getNull2String(eventInfo.getNocontactReason());// 未联原因
		contactMan = StringUtils.getNull2String(eventInfo.getContactMan());// 先行联系人
		if (eventInfo.getContactTime() != null) {
			contactTime = sdf3.format(eventInfo.getContactTime());// 联系时间
		}
		replyPoint = StringUtils.getNull2String(eventInfo.getReplyPoint());// 答复市民要点
		inscribeUnit = StringUtils.getNull2String(eventInfo.getInscribeUnit());// 落款单位
		citizenFeedback = StringUtils.getNull2String(eventInfo.getCitizenFeedback());// 市民反馈说明
		if (!"".equals(StringUtils.getNull2String(eventInfo.getReplyStyle()))) {
			replyStyle = eventInfo.getReplyStyle();// 答复方式
		}
		if (eventInfo.getReplyTime() != null) {
			replyTime = sdf3.format(eventInfo.getReplyTime());// 答复市民时间
		}
		if (!"".equals(StringUtils.getNull2String(eventInfo.getReplySatisfaction()))) {
			replySatisfaction = eventInfo.getReplySatisfaction();// 是否满意
		}
		if (!"".equals(StringUtils.getNull2String(eventInfo.getPublicFlag()))) {
			publicFlag = eventInfo.getPublicFlag();// 是否公开
		}
		if (eventInfo.getSceneTime() != null) {
			sceneTime = sdf3.format(eventInfo.getSceneTime());// 现场查看时间
		}
		contactContent = StringUtils.getNull2String(eventInfo.getContactContent());// 联系内容

		stExchangeName3=StringUtils.getNull2String(eventInfo.getStExchangeName3());//被举报单位名称
		stExchangeName4=StringUtils.getNull2String(eventInfo.getStExchangeName4());//被举报单位地址
		stExchangeName5=StringUtils.getNull2String(eventInfo.getStExchangeName5());//联系内容备注

        stFactory = StringUtils.getNull2String(eventInfo.getStFactory());// 是否工业区
		stFactoryName = StringUtils.getNull2String(eventInfo.getStFactoryName());// 工业区名称
		stReturnVisitFirstContact = StringUtils.getNull2String(eventInfo.getStReturnVisitFirstContact());//回访复核先行联系
		stReturnNocontactReason = StringUtils.getNull2String(eventInfo.getStReturnNocontactReason());//回访复核未联原因
		stReturnVisitContactMan = StringUtils.getNull2String(eventInfo.getStReturnVisitContactMan());//回访复核先行联系人
		if(eventInfo.getDtReturnVisitContactTime()!=null){
			dtReturnVisitContactTime = sdf3.format(eventInfo.getDtReturnVisitContactTime());//回访复核答复市民时间
		}
		stReturnContactContent = StringUtils.getNull2String(eventInfo.getStReturnContactContent());//回访复核联系内容
		remarkContactContent = StringUtils.getNull2String(eventInfo.getRemarkContactContent());//回访复核联系内容
		stReturnVisitFactConfirm = StringUtils.getNull2String(eventInfo.getStReturnVisitFactConfirm());//回访复核事实认定
		stReturnFactConfirmExplain = StringUtils.getNull2String(eventInfo.getStReturnFactConfirmExplain());//回访复核事实认定说明
		stReturnVisitSceneCheck = StringUtils.getNull2String(eventInfo.getStReturnVisitSceneCheck());//回访复核现场查看
		stReturnVisitAppealConfirm = StringUtils.getNull2String(eventInfo.getStReturnVisitAppealConfirm());//回访复核诉求认定
		
		beReportName = StringUtils.getNull2String(eventInfo.getBeReportName());//
		beReportAddress = StringUtils.getNull2String(eventInfo.getBeReportAddress());//
		stFactory1 = StringUtils.getNull2String(eventInfo.getFeedBackFactory());//
		stFactoryName1 = StringUtils.getNull2String(eventInfo.getFeedBackFactoryName());//		
		stReturnAppealExplain = StringUtils.getNull2String(eventInfo.getStReturnAppealExplain());//回访复核诉求认定说明
		stReturnVisitReplyPoint = StringUtils.getNull2String(eventInfo.getStReturnVisitReplyPoint());//回访复核答复市民要点
		stReturnVisitInscribeUnit = StringUtils.getNull2String(eventInfo.getStReturnVisitInscribeUnit());//回访复核落款单位
		stReturnCitizenFeedback = StringUtils.getNull2String(eventInfo.getStReturnCitizenFeedback());//回访复核市民反馈说明
		stReturnVisitReplyStyle = StringUtils.getNull2String(eventInfo.getStReturnVisitReplyStyle());//回访复核答复方式
		if(eventInfo.getDtReturnVisitReplyTime()!=null){
			dtReturnVisitReplyTime = sdf3.format(eventInfo.getDtReturnVisitReplyTime());//回访复核答复市民时间
		}
		stReturnVisitEventResult = StringUtils.getNull2String(eventInfo.getStReturnVisitEventResult());//回访复核是否解决
		stReturnReplySatisfaction = StringUtils.getNull2String(eventInfo.getStReturnReplySatisfaction());//回访复核是否满意
		stReturnPublicFlag = StringUtils.getNull2String(eventInfo.getStReturnPublicFlag());//回访复核是否公开
		returnVisitJbrName = StringUtils.getNull2String(eventInfo.getReturnVisitJbrName());//回访复核反馈人
		returnVisitFeedback = StringUtils.getNull2String(eventInfo.getReturnVisitFeedback());//回访复核半截汇总
		returnVisitFzrName = StringUtils.getNull2String(eventInfo.getReturnVisitFzrName());//回访复核签发人
		returnVisitPhoneNumber = StringUtils.getNull2String(eventInfo.getReturnVisitPhoneNumber());//回访复核签发人
		returnVisitSignForName = StringUtils.getNull2String(eventInfo.getReturnVisitSignForName());//回访复核签发人
		
		date1 = StringUtils.getNull2String(eventInfo.getDate1());//派单时间（处理时限按此时间计算）
		feedBackSendTime = StringUtils.getNull2String(eventInfo.getFeedBackSendTime());// 回访复核派单时间
		//if (eventInfo.getFeedBackSendTime() != null) {
			//feedBackSendTime = sdf4.format(eventInfo.getFeedBackSendTime());// 回访复核派单时间
		//}
		acceptflag = StringUtils.getNull2String(eventInfo.getAcceptflag());//主办标志位 0主办 1协办
		requirement = StringUtils.getNull2String(eventInfo.getRequirement());//处理描述（当发起回访复核时写明回访复核原因）
		supervision = StringUtils.getNull2String(eventInfo.getSupervision());//工单类型标示：0：转送单，1：督办单
		deptLevel2 = StringUtils.getNull2String(eventInfo.getDeptLevel2());//工单主办单位
		priority = StringUtils.getNull2String(eventInfo.getPriority());//工单优先级标示：0：紧急，1：普通、
		duLimit = StringUtils.getNull2String(eventInfo.getDuLimit());//督办时限(工作日)5
		class3 = StringUtils.getNull2String(eventInfo.getClass3());//内容分类3级
		class4 = StringUtils.getNull2String(eventInfo.getClass4());//内容分类4级
		assignFlag = StringUtils.getNull2String(eventInfo.getAssignFlag());//领导指派标志   0：普通单；1：领导指派单
		backCount = StringUtils.getNull2String(eventInfo.getBackCount());//已退单次数
		phoneno = StringUtils.getNull2String(eventInfo.getPhoneno());//来电电话
		relName = StringUtils.getNull2String(eventInfo.getRelName());//工单信息-联系人
		mobile = StringUtils.getNull2String(eventInfo.getMobile());//市民移动电话
		cardtype = StringUtils.getNull2String(eventInfo.getCardtype());//市民证件类型
		gender = StringUtils.getNull2String(eventInfo.getGender());//来电市民性别(男、女、不明)
		district = StringUtils.getNull2String(eventInfo.getDistrict());//来电市民所在区域 
		cardnum = StringUtils.getNull2String(eventInfo.getCardnum());//市民证件号码
		email = StringUtils.getNull2String(eventInfo.getEmail());//电子邮件
		ishidden = StringUtils.getNull2String(eventInfo.getIshidden());//是否匿名（0：否；1：是）
		ispublic = StringUtils.getNull2String(eventInfo.getIspublic());//是否公开（0：否；1：是）
		isrepeat = StringUtils.getNull2String(eventInfo.getIsrepeat());//是否重复（0：否；1：是）
		rewpid = StringUtils.getNull2String(eventInfo.getRewpid());//重复工单号
		appealContent = StringUtils.getNull2String(eventInfo.getAppealContent());//市民原信内容
		backDept1 = StringUtils.getNull2String(eventInfo.getBackDept1());//第一次退单部门
		backTime1 = StringUtils.getNull2String(eventInfo.getBackTime1());//第一次退单时间
		backReason1 = StringUtils.getNull2String(eventInfo.getBackReason1());//第一次退单理由
		backDept2 = StringUtils.getNull2String(eventInfo.getBackDept2());//第二次退单部门
		backTime2 = StringUtils.getNull2String(eventInfo.getBackTime2());//第二次退单时
		backReason2 = StringUtils.getNull2String(eventInfo.getBackReason2());//第二次退单理由
		backDept3 = StringUtils.getNull2String(eventInfo.getBackDept3());//第三次退单部门
		backTime3 = StringUtils.getNull2String(eventInfo.getBackTime3());//第三次退单时间
		backReason3 = StringUtils.getNull2String(eventInfo.getBackReason3());//第三次退单理由
		callbackFlag=StringUtils.getNull2String(eventInfo.getCallbackFlag());//是否回访复核
		wpSource=StringUtils.getNull2String(eventInfo.getWpSource());//工单来源
		level2Charger=StringUtils.getNull2String(eventInfo.getLevel2Charger());//二级负责人：
		if(level2Charger==null||"".equals(level2Charger)){
			level2Charger="周健";
		}
		limitTime=StringUtils.getNull2String(eventInfo.getLimitTime());//办结时间
		style=StringUtils.getNull2String(eventInfo.getStyle());//自办/转办
		hurryFlag=StringUtils.getNull2String(eventInfo.getHurryFlag());//催单
		returnVisit=StringUtils.getNull2String(eventInfo.getReturnVisit());
		stExchangeName1=StringUtils.getNull2String(eventInfo.getStExchangeName1());
		stExchangeName2=StringUtils.getNull2String(eventInfo.getStExchangeName2());
		yjjbStatus=StringUtils.getNull2String(eventInfo.getYjjbStatus());
		// 取得附件
		ottffFileInfo.setEventId(id);
		fileList = ottffFileService.findByExample(ottffFileInfo);
		
		for (int i = 0; i < fileList.size(); i++) {			
			ottffFileInfo = (OttffFileInfo) fileList.get(i);
			if ("0".equals(ottffFileInfo.getType())) {
				fileNum = 1;
				break;
			}									
		}

		// 取得事项流程
		Map processQueryMap = new HashMap();
		processQueryMap.put("eventId", id);
		String processHql = eventProcessService.getPaginationHql(processQueryMap, "time", "asc");
		processList = paginationUtil.getListByHql(processHql, 0, paginationUtil.getRecordCountByHql(processHql).intValue());
		//获取电话列表信息
		Map callMap = new HashMap();
		callMap.put("no",eventInfo.getNo());
		String callHql = eventCallService.getPaginationHql(callMap, "createTime", "desc");
		callList = paginationUtil.getListByHql(callHql, 0, paginationUtil.getRecordCountByHql(callHql).intValue());
		
	} else {
		callTime = sdf.format(new Date());
	}
	// 检索条件
	Map queryMap = new HashMap();
	queryMap.put("status", "1");

	// 取得分类
	String hql = classificationService.getPaginationHql(queryMap, "id", "asc");
	List classificationList = paginationUtil.getListByHql(hql, 0, paginationUtil.getRecordCountByHql(hql).intValue());

	// 取得部门
	hql = departmentService.getPaginationHql(queryMap, "orderId", "asc");
	List departmentList = paginationUtil.getListByHql(hql, 0, paginationUtil.getRecordCountByHql(hql).intValue());
	String linkPhone = "";
	for (int i = 0; i < departmentList.size(); i++) {
		DepartmentInfo departmentInfo = (DepartmentInfo) departmentList.get(i);
		if ("1".equals(departmentInfo.getType())) {
			if (departmentInfo.getId().equals(eventInfo.getForwardDepartment())) {
				 linkPhone = departmentInfo.getLinkPhone();
			}
		}
	}

	// 举报性质
	hql = reportTypeService.getPaginationHql(queryMap, "id", "asc");
	List reportTypeList = paginationUtil.getListByHql(hql, 0, paginationUtil.getRecordCountByHql(hql).intValue());
	
	// 取得诉求解决
	String[] eventResultList = ottffConst.getConstArray("EVENT_RESULT");
	List tuidanlist=new ArrayList();
	//退单信息(已申请，已初审，已审批)
	if(!"".equals(id)){
		Map queryMap1 = new HashMap(); 
		queryMap1.put("stEventId",id);
		tuidanlist =ottffTuidanService.findByCriteria(queryMap1);
	}
    
    OttffTuidanInfo ottffTuidanInfo=new OttffTuidanInfo();
	OttffTuidanInfo ottffTuidanInfo2=new OttffTuidanInfo();
	String tuidanfilesize="";
	if (!"".equals(ottfftuidanID)) {
		ottffTuidanInfo2 = ottffTuidanService.getInfo(ottfftuidanID);
		//退单附件个数
		OttffFileInfo ottffFileInfo1=new OttffFileInfo();
		ottffFileInfo1.setSubeventId(ottfftuidanID);
		ottffFileInfo1.setType("2");
		List tuidanfilelist =ottffFileService.findByExample(ottffFileInfo1);
		tuidanfilesize=String.valueOf(tuidanfilelist.size());
	} 
	//延期申请
	OttffYqapplyInfo ottffYqapplyInfo2=new OttffYqapplyInfo();
	if(!"".equals(ottffyqsqID)){
		ottffYqapplyInfo2=ottffYqapplyService.getInfo(ottffyqsqID);
	}
	//延期信息(已申请，已初审，已审批)
	List yqsqlist=new ArrayList();
	if(!"".equals(id)){
		Map queryMap2 = new HashMap(); 
		queryMap2.put("stEventId",id);
		queryMap2.put("orderField","stCreatetime");
		yqsqlist =ottffYqapplyService.findByCriteria(queryMap2);
	}
	//延期申请额度
	OttffYquotaInfo ottffYquotaInfo=ottffYquotaService.findCurrentYQuota();
	String jiezhidate=OttffConst.OLDDATE;
	Date nowDate = sdf.parse(sdf.format(new Date()));
	Date oldDate=sdf.parse(jiezhidate);
	String data="";
	if(eventInfo.getCreateTime()!=null&&oldDate.compareTo(sdf.parse(sdf.format(eventInfo.getCreateTime())))<0){
		data="new";
	}else{
		data="old";
	}

	List<SecurityUser> sortedUserList=cfconsoleService.getUserListByGroupCode("12345dengjiren");
	List<SecurityUser> sortedUserListf=cfconsoleService.getUserListByGroupCode("12345fankuiren");
	List<SecurityUser> sortedUserListq=cfconsoleService.getUserListByGroupCode("12345qianfaren");
	List<SecurityUser> sortedUserListfuze=cfconsoleService.getUserListByGroupCode("12345fuzeren");

	// 根据工单联系电话查找重复件数
	int duplicateByPhoneNumber = eventService.duplicateByPhoneNumberAjax(eventInfo.getPhoneNumber());
	//out.print(duplicateByPhoneNumber);
	
	// 根据事项名称查找重复件数
	int duplicateByName = eventService.duplicateByNameAjax(eventInfo.getName());
	String callerName1=StringUtils.getNull2String(eventInfo.getCallerName());
	if("".equals(relName)){
		relName = callerName1;
	}
	String alreadyTuidanNumSql ="select * from ottff_event t where t.ST_STATUS ='11' and t.st_no ='"+eventInfo.getNo()+"'";
	int alreadyTuidanNum =0;
	//int alreadyTuidanNum = eventService.queryAlreadyTuidanNum(eventInfo.getNo(),alreadyTuidanNumSql);
	//out.println(alreadyTuidanNum+"==alreadyTuidanNum");

	boolean isShowBtn = true;
	if (cfconsoleService.isUserInGroup(userId, "12345view")) {
		isShowBtn = false;
	}
%>

<html>
<head>
<title>事项登记</title>
<%@ include file="/common/hbj_include_meta.inc"%>
<%@ include file="/common/hbj_include_jquery.inc"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/css/contact_style.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/hbj_general.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/tabcss.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath()%>/script/hbj_general.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/script/my97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/eventService.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/engine.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/json2.js"></script>

<style type="text/css">
/* 重复件数提示样式 */
.duplicateTip {
	background:url(/image/general/duplicate.png) no-repeat;
	width:16px;
	height:16px;
	color:rgb(255,255,255);
	text-align:center;
	line-height:16px;
	display:inline-block;
}
</style>
</head>
<body onload="init()">
<base target="_self"/>
<div class="main_border">
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td class="main_title">您现在的位置：12345工单办理&gt; 事项登记</td>
	</tr>
</table>
<form id = "myForm" name="myForm" method="post" action="event_call_bg.jsp">
		<input type="hidden" name ="no" value="<%=eventInfo.getNo()%>"/>
		<input type="hidden" name ="phoneNumber" value="<%=eventInfo.getPhoneNumber()%>"/>
		<input type="hidden" name ="id" value="<%=request.getParameter("id")%>"/>
		<input type="hidden" name="operation"/>
		<input type="hidden" name="result"/>
		
	</form>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2">
			<div class="main_line_list">
				<ul>
					<li>
						<div id="new1" class="<%if ("1".equals(tab)) {%>main_line_list_tabs<%} else {%>main_line_list_tab<%}%>"><span><a href="#" onclick="window.location.href='event_edit.jsp?tab=1&id=<%=id%>&from=<%=from%>&phoneNumber=<%=phoneNumber%>'">登记信息</a></span></div>
					</li>
					<li>
						<div id="new2" class="<%if ("2".equals(tab)) {%>main_line_list_tabs<%} else {%>main_line_list_tab<%}%>"><span><a href="#" onclick="window.location.href='event_edit.jsp?tab=2&id=<%=id%>&from=<%=from%>&phoneNumber=<%=phoneNumber%>'">满意度调查</a></span></div>
					</li>
					<%if("1".equals(hurryFlag)){%>
					<li>
						<div id="new3" class="<%if ("3".equals(tab)) {%>main_line_list_tabs<%} else {%>main_line_list_tab<%}%>"><span><a href="#" onclick="window.location.href='event_edit.jsp?tab=3&id=<%=id%>&from=<%=from%>&phoneNumber=<%=phoneNumber%>'">催单</a></span></div>
					</li>
					<%}%>
				</ul>
			</div>
			<%if(!"".equals(from)){%>
			      <div style="float:right;margin-right:20px;"><input type="button" value="返回列表" onclick="backToFirst('<%=phoneNumber%>')"/></div>
		    <%}%>
			<div class="main_line">
				<div></div>
				<span></span>
				<p></p>
			</div>
		</td>
	</tr>
	<!-- 登记信息Tab -->
	<%if ("1".equals(tab)) {%>
	<tr>
		<td class="main_edit_title">&nbsp;事项登记</td>
		<%if (isShowBtn) {%>
		<td class="main_edit_title" style="text-align:right">&nbsp;
		<%if("".equals(id) || "".equals(from)){%>
			<input type="button" id="btnJudgeDuplicate" class="main_button1" value="重复件判定" onclick="judgeDuplicate('<%=id%>')" <%if (eventDuplicateService.isEventDuplicate(StringUtils.getNull2String(eventInfo.getNo()))) {%>disabled<%}%> />&nbsp;
		<%}%>
		<!--在反馈12345之后，并由总队发起-->
		<%if("5".equals(status)){%>
			<input type="button" id="btnJudgeDuplicate" class="main_button1" value="发起督办" onclick="initGddb('<%=id%>')"/>&nbsp;	
		<%}%>
		</td>
		<%}%>
	</tr>
	<tr>
		<td class="main_edit_body_wrap" colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #abcdf0; border-collapse:collapse;">
			<!-- 事项登记详情form -->
			<form name="form1" method="post" action="event_bg.jsp">
			<!-- 工单信息 -->
			<tr>
				<td class="title2" colspan="4">工单信息</td>
			</tr>
			<tr>
				<td width="18%" class="ltitle">&nbsp;事项编号：</td>
				<td width="32%" class="content"><input type="text" name="no" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getNo())%>" onkeyup="checkNumberAndLine(this,'0')" onafterpaste="checkNumberAndLine(this,'0')" title="只能输入'-'和数字" ></td>
				<td width="18%" class="ltitle"><span style="color: red">*</span>&nbsp;事项名称：</td>
				<td width="32%" class="content">
					<input type="text" name="name" class="main_input12" maxlength="50" style="width:88%" value="<%=StringUtils.getNull2String(eventInfo.getName())%>" onchange="duplicateByName()">
					<span id="duplicateNameNum" class="duplicateTip" style="display:<%if (duplicateByName > 0) {// 有重复件时显示提示%>inline-block<%} else {%>none<%}%>;cursor:hand" title="点击查询事项名称重复件" onclick="queryEventByName()"><%=duplicateByName%></span>
				</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;污染属地：</td>
				<td class="content"><select name="originDepartment" class="main_input12" >
					<option value="">请选择</option>
					<%for (int i = 0; i < departmentList.size(); i++) {
						DepartmentInfo departmentInfo = (DepartmentInfo) departmentList.get(i);
						if ("0".equals(departmentInfo.getType())) {%>
							<option value="<%=departmentInfo.getId()%>" <%if (departmentInfo.getId().equals(eventInfo.getOriginDepartment())) {%>selected<%}%>><%=departmentInfo.getName()%></option>
					<%	}
					}%>
				</select></td>
				<td class="ltitle"><%if(ottffConst.isStringContains("12",status)){%><span style="color: red">*</span><%}%>&nbsp;转发单位：</td>
				<td class="content"><select name="forwardDepartment" class="main_input12">
					<option value="">请选择</option>
					<%for (int i = 0; i < departmentList.size(); i++) {
						DepartmentInfo departmentInfo = (DepartmentInfo) departmentList.get(i);
						if ("1".equals(departmentInfo.getType())) {%>
							<option value="<%=departmentInfo.getId()%>" <%if (departmentInfo.getId().equals(eventInfo.getForwardDepartment())) {%>selected<%}%>><%=departmentInfo.getName()%></option>
					<%	}
					}%>
				</select></td>
			</tr>
			<tr>
				<td class="ltitle">重新交办：</td>
				<td class="content" >
					<input type="radio" name="rdoRedo" value="1" onclick="onRedoChange()" <%if ("1".equals(redo)) {%>checked<%}%>>是
					<input type="radio" name="rdoRedo" value="0" onclick="onRedoChange()" <%if ("0".equals(redo)) {%>checked<%}%>>否
					<input type="hidden" name="redo" value="<%=redo%>">
				</td>
				<td class="ltitle">工单优先：</td>
				<td class="content" >
					<input type="radio" name="priorityRadio" value="1" onclick="radiochange('priorityRadio','priority')" <%if ("1".equals(priority)) {%>checked<%}%> disabled>普通
					<input type="radio" name="priorityRadio" value="0" onclick="radiochange('priorityRadio','priority')" <%if ("0".equals(priority)) {%>checked<%}%> disabled>紧急
					<input type="radio" name="priorityRadio" value="2" onclick="radiochange('priorityRadio','priority')" <%if ("2".equals(priority)) {%>checked<%}%> disabled>次紧急									
					<input type="hidden" name="priority" value="<%=priority%>">
				</td>
				
			</tr>
			<tr>
				<td class="ltitle">客户类型：</td>
				<td class="content" >
					<input type="radio" name="wpCustomerTypeRedo" value="1" onclick="radiochange('wpCustomerTypeRedo','wpCustomerType')" <%if ("1".equals(wpCustomerType)) {%>checked<%}%> disabled>个人
					<input type="radio" name="wpCustomerTypeRedo" value="2" onclick="radiochange('wpCustomerTypeRedo','wpCustomerType')" <%if ("2".equals(wpCustomerType)) {%>checked<%}%> disabled>企业
					<input type="hidden" name="wpCustomerType" value="<%=wpCustomerType%>">
				</td>
				<td class="ltitle">服务类型：</td>
				<td class="content" >
					<input type="radio" name="wpServiceTypeRadio" value="1" onclick="radiochange('wpServiceTypeRadio','wpServiceType')" <%if ("1".equals(wpServiceType)) {%>checked<%}%> disabled>综合服务
					<input type="radio" name="wpServiceTypeRadio" value="0" onclick="radiochange('wpServiceTypeRadio','wpServiceType')" <%if ("2".equals(wpServiceType)) {%>checked<%}%> disabled>一网通办
					<input type="hidden" name="wpServiceType" value="<%=wpServiceType%>">
				</td>
				
			</tr>
			
			<tr>
				<td class="ltitle">领导指派：</td>
				<td class="content" >
					<input type="radio" name="assignFlagRadio" value="1" onclick="radiochange('assignFlagRadio','assignFlag')" <%if ("1".equals(assignFlag)) {%>checked<%}%> disabled>是
					<input type="radio" name="assignFlagRadio" value="0" onclick="radiochange('assignFlagRadio','assignFlag')" <%if ("0".equals(assignFlag)) {%>checked<%}%> disabled>否
					<input type="hidden" name="assignFlag" value="<%=assignFlag%>">
				</td>
				<td class="ltitle">是否公开：</td>
				<td class="content" >
					<input type="radio" name="ispublicRadio" value="1" onclick="radiochange('ispublicRadio','ispublic')" <%if ("1".equals(ispublic)) {%>checked<%}%> disabled>是
					<input type="radio" name="ispublicRadio" value="0" onclick="radiochange('ispublicRadio','ispublic')" <%if ("0".equals(ispublic)) {%>checked<%}%> disabled>否
					<input type="hidden" name="ispublic" value="<%=ispublic%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">是否重复：</td>
				<td class="content" >
					<input type="radio" name="isrepeatRadio" value="1" onclick="radiochange('isrepeatRadio','isrepeat')" <%if ("1".equals(isrepeat)) {%>checked<%}%> disabled>是
					<input type="radio" name="isrepeatRadio" value="0" onclick="radiochange('isrepeatRadio','isrepeat')" <%if ("0".equals(isrepeat)) {%>checked<%}%> disabled>否
					<input type="hidden" name="isrepeat" value="<%=isrepeat%>">
				</td>
				<td class="ltitle">重复工单号：</td>
				<td class="content" >
					<input type="text" name="rewpid" class="main_input12" onblur="checkNum()" maxlength="50" value="<%=rewpid%>" readonly>
			    </td>
			</tr>
			<tr>
				<td class="ltitle">工单办理类型：</td>
				<td class="content">
					<input type="radio" name="supervisionRadio" value="1" onclick="radiochange('supervisionRadio','supervision')" <%if ("1".equals(supervision)) {%>checked<%}%> disabled>督办单
					<input type="radio" name="supervisionRadio" value="0" onclick="radiochange('supervisionRadio','supervision')" <%if ("0".equals(supervision)) {%>checked<%}%> disabled>转送单
					<input type="hidden" name="supervision" value="<%=supervision%>">
				</td>
				<!--如果工单办理类型 为督办件 显示督办时限-->
				<%if("1".equals(supervision)){%>
				<td class="ltitle">督办时限：</td>
				<td class="content">
					<input type="text" name="duLimit" class="main_input12" maxlength="50" value="<%=duLimit%>" readonly>
				</td>
				<%} else {%>
				<td colspan="2">&nbsp;</td>
				<%}%>
			</tr>
			<!--回访复核时，才显示处理描述-->
			<tr>
				<td class="ltitle">回访复核：</td>
				<td class="content" colspan="3">
					<input type="radio" name="callbackFlagRadio" value="Y"  <%if ("Y".equals(callbackFlag)) {%>checked<%}%> onclick="radiochange('callbackFlagRadio','callbackFlag')" disabled>是
					<input type="radio" name="callbackFlagRadio" value="N"  <%if ("N".equals(callbackFlag)) {%>checked<%}%> onclick="radiochange('callbackFlagRadio','callbackFlag')" disabled>否
					<input type="hidden" name="callbackFlag" value="<%=callbackFlag%>">
				</td>
			</tr>
			<%if("Y".equals(callbackFlag)){%>
			<tr>
				<td class="ltitle">处理描述：</td>
				<td class="content" colspan="3">
					<textarea name="requirement" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(requirement)%></textarea>
				</td>
			</tr>
			<%}%>
			<tr>
				<td class="ltitle">工单来源：</td>
				<td class="content" >
					<input type="text" name="wpSource" class="main_input12" maxlength="50" value="<%=wpSource%>" readonly>
				</td>
				<td class="ltitle">&nbsp;来电日期：</td>
				<td class="content"><input type="text" name="callTime" class="Wdate main_input12" maxlength="10" value="<%=callTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" disabled></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;举报性质：</td>
				<td class="content" >
					<%for (int i = 0; i < reportTypeList.size(); i++) {
						ReportTypeInfo reportTypeInfo = (ReportTypeInfo) reportTypeList.get(i);%>
					<input type="radio" name="reportTypeRadio" value="<%=reportTypeInfo.getCode()%>" onclick="onReportTypeChange()" <%if (reportTypeInfo.getCode().equals(reportType)) {%>checked<%}%> disabled><%=reportTypeInfo.getName()%>
					<%}%>
					<input type="hidden" name="reportType" value="<%=reportType%>">
				</td>
				<td  class="ltitle" align="right" nowrap>行业分类：</td>
				<td class="content" >
					<input type="text" name="stExchangeName2" style="width:88%" value="<%=stExchangeName2%>"/><input type="button" id="hyflDialogOpener" name="hyflDialogOpener" class="main_button2" value="">
					<input type="hidden" id="stExchangeName1" name="stExchangeName1" value="<%=stExchangeName1%>">
					<!-- 选择状态的窗口 开始 -->
					<div id="hyflDialog" title="选择行业" style="display:none">
						<iframe name="hyflDialogFrame" id="hyflDialogFrame" src="<%=request.getContextPath()%>/general/common/ottffHyflEUTree.jsp?checkedHyfl=<%=stExchangeName1%>&callbackFunc=selectHYLB" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>
					</div>
					<!-- 选择状态的窗口 结束 -->
				</td>
			</tr>
			<tr>
				<td class="ltitle">12345参考分类：</td>
				<td class="content" >
					<input type="text" name="class3" class="main_input12" maxlength="50" value="<%=class3%>" readonly >
				</td>
				<td class="ltitle">12345参考分类：</td>
				<td class="content" >
					<input type="text" name="class4" class="main_input12" maxlength="50" value="<%=class4%>" readonly>
				</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;污染分类：</td>
				<td class="content"><select name="classification" class="main_input12" onchange="onClassificationChange()">
					<option value="">请选择</option>
					<%for (int i = 0; i < classificationList.size(); i++) {
						ClassificationInfo classificationInfo = (ClassificationInfo) classificationList.get(i);
						if ("0".equals(classificationInfo.getParentId())) {%>
					<option value="<%=classificationInfo.getId()%>" <%if (classificationInfo.getId().equals(eventInfo.getClassification())) {%>selected<%}%>><%=classificationInfo.getName()%></option>
					<%	}
					}%>
				</select></td>
				<td class="ltitle">污染子分类：</td>
				<td class="content"><select name="subclassification" class="main_input12">
					<option value="">请选择</option>
				</select></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;工单联系电话：</td>
				<td class="content">
					<input type="radio" name="rdoPhoneNumber" onclick="onPhoneNumberChange()" disabled>无&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="rdoPhoneNumber" onclick="onPhoneNumberChange()" disabled><input type="text" name="phoneNumberInput" style="width:58%" onkeyup="checkNumberAndLine(this,'1')" onafterpaste="checkNumberAndLine(this,'1')" title="只能输入'-'、','和数字" readonly>
					<input type="button" class="main_button1" value="拨号" onclick="directcall()" />
					<!--<a href="#" onclick="directcall()">拨 号</a>-->
					<span class="duplicateTip" style="display:<%if (duplicateByPhoneNumber > 0) {// 有重复件时显示提示%>inline-block<%} else {%>none<%}%>;cursor:hand" title="点击查询工单联系电话重复件" onclick="queryEventByPhone()"><%=duplicateByPhoneNumber%></span>
				</td>
				<td class="ltitle">工单联系人：</td>
				<td class="content" >
					<input type="text" name="relName" class="main_input12" maxlength="50" value="<%=relName%>" readonly>
				</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;联系地址：</td>
				<td class="content" colspan="3"><input type="radio" name="rdoAddress" onclick="onAddressChange()" disabled>无&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="rdoAddress" onclick="onAddressChange()" disabled><input type="text" name="addressInput" style="width:88%" maxlength="500" readonly></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;诉求地址：</td>
				<td class="content" colspan="3"><input type="radio" name="rdoAppealAddress" onclick="onAppealAddressChange()" disabled>无&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="rdoAppealAddress" onclick="onAppealAddressChange()" disabled><input type="text" name="appealAddressInput" style="width:88%" maxlength="500" readonly></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;内容描述：</td>
				<td class="content" colspan="3"><textarea name="content" rows="4" style="width:100%" onblur="ifjiaoban()" readonly><%=StringUtils.getNull2String(eventInfo.getContent())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;市民原信内容：</td>
				<td class="content" colspan="3"><textarea name="appealContent" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(appealContent)%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">是否主办：</td>
				<td class="content" >
					<input type="radio" name="acceptflagRadio" value="0" onclick="radiochange('acceptflagRadio','acceptflag')" <%if ("0".equals(acceptflag)) {%>checked<%}%> disabled>是
					<input type="radio" name="acceptflagRadio" value="1" onclick="radiochange('acceptflagRadio','acceptflag')" <%if ("1".equals(acceptflag)) {%>checked<%}%> disabled>否
					<input type="hidden" name="acceptflag" value="<%=acceptflag%>">
				</td>
				<%if("1".equals(acceptflag)){%>
				<td class="ltitle">主办单位：</td>
				<td class="content" >
					<input type="text" name="deptLevel2" class="main_input12" maxlength="50" value="<%=deptLevel2%>" disabled>
			    </td>
				<%} else {%>
				<td colspan="2">&nbsp;</td>
				<%}%>
			</tr>
			<tr>
			<td class="ltitle">办理方式：</td>
				<td class="content"colspan="3">
					<input type="radio" name="styleRadio" value="自办" onclick="radiochange('styleRadio','style')" <%if ("自办".equals(style)) {%>checked<%}%> >自办
					<input type="radio" name="styleRadio" value="转办" onclick="radiochange('styleRadio','style')" <%if ("转办".equals(style)) {%>checked<%}%> >转办
					<input type="hidden" name="style" value="<%=style%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;派件时间：</td>
				<td class="content"><input type="text" name="date1" class="Wdate main_input12" maxlength="10" value="<%=date1%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" disabled></td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;应反时间：</td>
				<td class="content"><input type="text" name="feedbackLimitTime" class="Wdate main_input12" maxlength="10" value="<%=feedbackLimitTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入"></td>	
			</tr>
			<tr>
				<td class="ltitle">&nbsp;办结时间：</td>
				<td class="content"><input type="text" name="limitTime" class="Wdate main_input12" maxlength="10" value="<%=limitTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" disabled></td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;负责人：</td>
				<td class="content"><input type="text" name="level2Charger" class="main_input12" value="<%=StringUtils.getNull2String(level2Charger)%>"></td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;登记人：</td>
				<td class="content">
					<select name="registerName" class="main_input12" >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int i=0;i<sortedUserList.size();i++){
								SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(eventInfo.getRegisterName()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="registerName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getRegisterName())%>">-->
				</td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;上传反馈附件：</td>
				<td class="content" >
					<input type="radio" name="needAttachRadio" value="1" onclick="onNeedAttachChange()" <%if ("1".equals(needAttach)) {%>checked<%}%>>是
					<input type="radio" name="needAttachRadio" value="0" onclick="onNeedAttachChange()" <%if ("0".equals(needAttach)) {%>checked<%}%>>否
					<input type="hidden" name="needAttach" value="<%=needAttach%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">备注：</td>
				<td class="content" colspan="3"><textarea name="comment" rows="4" style="width:100%"><%=StringUtils.getNull2String(eventInfo.getComment())%></textarea></td>
			</tr>
			<!-- /工单信息 -->

			<!-- 市民信息 -->
			<tr>
				<td class="title2" colspan="4">市民信息&nbsp;<a href="javascript:;" onclick="sminfo();"><img id="sm" src="<%=request.getContextPath()%>/image/general/Down.png" width="10" height="10"/></a></td>
			</tr>
			<tr id="s1">
				<td class="ltitle">是否匿名：</td>
				<td class="content" colspan="3" >
					<input type="radio" name="ishiddenRadio" value="0" onclick="radiochange('ishiddenRadio','ishidden')" <%if ("0".equals(ishidden)) {%>checked<%}%> disabled>是
					<input type="radio" name="ishiddenRadio" value="1" onclick="radiochange('ishiddenRadio','ishidden')" <%if ("1".equals(ishidden)) {%>checked<%}%> disabled>否
					<input type="hidden" name="ishidden" value="<%=ishidden%>">
				</td>
			</tr>
			<tr id="s2">
				<td class="ltitle">&nbsp;来电人：</td>
				<td class="content"><input type="radio" name="rdoCallerName" onclick="onCallerNameChange()" disabled>匿名&nbsp;<input type="radio" name="rdoCallerName" onclick="onCallerNameChange()" disabled><input type="text" name="callerNameInput" style="width:66%" readonly></td>
				<td class="ltitle">&nbsp;性别：</td>
				<td class="content"><input type="text" name="gender" class="main_input12" value="<%=StringUtils.getNull2String(gender)%>" readonly></td>
			</tr>
			<tr id="s3">
				<td class="ltitle">市民移动电话：</td>
				<td class="content" >
					<input type="text" name="mobile" class="main_input12" maxlength="50" value="<%=mobile%>" readonly>
				</td>
				<td class="ltitle">来电电话：</td>
				<td class="content" >
					<input type="text" name="phoneno" class="main_input12" maxlength="50" value="<%=phoneno%>" readonly>
				</td>
			</tr>
			<tr id="s4">
				<td class="ltitle">证件类型：</td>
				<td class="content" >
					<input type="text" name="cardtype" class="main_input12" maxlength="50" value="<%=cardtype%>" readonly>
				</td>
				<td class="ltitle">证件号码：</td>
				<td class="content" >
					<input type="text" name="cardnum" class="main_input12" maxlength="50" value="<%=cardnum%>" readonly>
				</td>
			</tr>
			<tr id="s5">
				<td class="ltitle">电子邮件：</td>
				<td class="content" >
					<input type="text" name="email" class="main_input12" maxlength="50" value="<%=email%>" readonly>
				</td>
				<td class="ltitle">所在区域：</td>
				<td class="content" >
					<input type="text" name="district" class="main_input12" maxlength="50" value="<%=district%>" readonly>
				</td>
			</tr>
            <!-- /市民信息 -->

			<!-- 历史退单记录 -->
			<%if(!"".equals(backCount)&&!"0".equals(backCount)){%>
			<tr>
				<td class="title2" colspan="4">历史退单记录</td>
			</tr>
			<tr>
				<td class="ltitle">退单次数：</td>
				<td class="content" colspan="3">
					<input type="text" name="backCount" class="main_input11" maxlength="50" value="<%=backCount%>" readonly>
				</td>
			</tr>
			<%if(!"".equals(backDept1)){%>
			<tr>
				<td class="ltitle">第一次退单部门：</td>
				<td class="content">
					<input type="text" name="backDept1" class="main_input12" maxlength="50" value="<%=backDept1%>" readonly>
				</td>
				<td class="ltitle">退单时间：</td>
				<td class="content">
					<input type="text" name="backTime1" class="Wdate main_input12" maxlength="10" value="<%=backTime1%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" readonly>
				</td>
			</tr>
			<tr>
				<td class="ltitle">退单理由：</td>
				<td class="content" colspan="3"><textarea name="backReason1" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(backReason1)%></textarea></td>
			</tr>
			<%}%>
			<%if(!"".equals(backDept2)){%>
			<tr>
				<td class="ltitle">第二次退单部门：</td>
				<td class="content">
					<input type="text" name="backDept2" class="main_input12" maxlength="50" value="<%=backDept2%>" readonly>
				</td>
				<td class="ltitle">退单时间：</td>
				<td class="content">
					<input type="text" name="backTime2" class="Wdate main_input12" maxlength="10" value="<%=backTime2%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" readonly>
				</td>
			</tr>
			<tr>
				<td class="ltitle">退单理由：</td>
				<td class="content" colspan="3"><textarea name="backReason2" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(backReason2)%></textarea></td>
			</tr>
			<%}%>
			<%if(!"".equals(backDept3)){%>
			<tr>
				<td class="ltitle">第三次退单部门：</td>
				<td class="content">
					<input type="text" name="backDept3" class="main_input12" maxlength="50" value="<%=backDept3%>" readonly>
				</td>
				<td class="ltitle">退单时间：</td>
				<td class="content">
					<input type="text" name="backTime3" class="Wdate main_input12" maxlength="10" value="<%=backTime3%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" readonly>
				</td>
			</tr>
			<tr>
				<td class="ltitle">退单理由：</td>
				<td class="content" colspan="3"><textarea name="backReason3" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(backReason3)%></textarea></td>
			</tr>
			<%}
			}%>
			<!-- /历史退单记录 -->

			<!-- 退回信息 -->
			<%if (ottffConst.isStringContains("2,3,5,8,10,21,22",status)) { // 状态为“已反馈”、“退回”、“回访复核已反馈”、“回访复核退回”时显示退回信息%>
			<tr>
				<td class="title2" colspan="4">退回信息</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退回理由：</td>
				<td class="content" colspan="3"><textarea name="sendBack" rows="4" style="width:100%"><%=StringUtils.getNull2String(eventInfo.getSendBack())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退回人：</td>
				<td class="content" colspan="3"><input type="text" name="backPerson" class="main_input12" maxlength="50" value="<%=StringUtils.getNull2String(eventInfo.getBackPerson())%>" ></td>
			</tr>
			<%}%>
			<!-- /退回信息 -->

			<!-- 先行联系 -->
			<%if (ottffConst.isStringContains("1,2,3,4,5,12,6,7,8,10,13,16,19,20,21,22",status)) { // 状态为“已签收”、“已反馈”、“退回”、“已反馈12345”时显示反馈信息%>
			<tr>
				<td class="title2" colspan="4">先行联系</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>先行联系：</td>
				<td class="content">
					<select name="firstContact" class="main_input12" style="width:70%" id="myFirstContact">
						<option value="">请选择</option>
						<option value="已联" <%if ("已联".equals(firstContact)) {%>selected<%}%>>已联</option>
						<option value="未联" <%if ("未联".equals(firstContact)) {%>selected<%}%>>未联</option>
					</select>
					<!--<a href="javascript:void(0);" id="sendMsgDialogOpener" onclick="opensendMsgDialog('<%=StringUtils.getNull2String(eventInfo.getId())%>')" title="发送短信提醒工单联系人">短信发送</a>-->
				</td>
				<td class="ltitle"><span style="color: red">*</span>未联原因：</td>
				<td class="content"><select name="nocontactReason" class="main_input12" id="nocontactReason">
					<option value="">请选择</option>
					<option value="始终关机" <%if ("始终关机".equals(nocontactReason)) {%>selected<%}%>>始终关机</option>
					<option value="始终未接" <%if ("始终未接".equals(nocontactReason)) {%>selected<%}%>>始终未接</option>
					<option value="其他" <%if ("其他".equals(nocontactReason)) {%>selected<%}%>>其他</option>
				</select></td>
			</tr>
			<tr>
				<td class="ltitle">先行联系人：</td>
				<td class="content">
					<!--<select name="contactMan" class="main_input12" >
					<option value="">请选择</option>
					<%if(sortedUserList!=null&&sortedUserList.size()>0){
					for(int i=0;i<sortedUserList.size();i++){
					  SecurityUser user=(SecurityUser)sortedUserList.get(i);
					%>
					 <option value="<%=user.getName()%>" <%if(user.getName().equals(contactMan)){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="contactMan" class="main_input12" value="<%=contactMan%>">
				</td>
				<td class="ltitle">联系时间：</td>
				<td class="content"><input type="text" name="contactTime" class="Wdate main_input12" maxlength="10" value="<%=contactTime%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">联系内容：</td>
				<td class="content" colspan="3"><textarea name="contactContent" rows="3" cols="" style="width:100%" ><%=contactContent%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">联系内容备注：</td>
				<td class="content" colspan="3"><textarea name="stExchangeName5" rows="3" cols="" style="width:100%" ><%=stExchangeName5%></textarea></td>
			</tr>
			<%}%>
			<!-- /先行联系 -->

			<%if (ottffConst.isStringContains("2,3,4,5,6,7,8,10,13,19,20,21,22",status)) { // 状态为“已签收”、“已反馈”、“退回”、“已反馈12345” 6:待签收（回访复核）,7:已签收（回访复核）,8:已反馈（回访复核）10:退回（回访复核）13:未先行联系（回访复核）19:辖区,20:辖区（回访复核）时显示反馈信息%>
			<!-- 确认 -->
			<tr>
				<td class="title2" colspan="4">确认</td>
			</tr>
			<tr>
				<td class="ltitle">事实认定：</td>
				<td class="content"><select name="factConfirm" class="main_input12" >
					<option value="">请选择</option>
					<%if(("1,3,4").contains(eventInfo.getReportType())){%>
						<option value="属实" <%if ("属实".equals(factConfirm)) {%>selected<%}%>>属实</option>
						<option value="部分属实" <%if ("部分属实".equals(factConfirm)) {%>selected<%}%>>部分属实</option>
						<option value="不属实" <%if ("不属实".equals(factConfirm)) {%>selected<%}%>>不属实</option>
					<%}else if("2".equals(eventInfo.getReportType())){%>
						<option value="采纳" <%if ("采纳".equals(factConfirm)) {%>selected<%}%>>采纳</option>
						<option value="部分采纳" <%if ("部分采纳".equals(factConfirm)) {%>selected<%}%>>部分采纳</option>
						<option value="留作参考" <%if ("留作参考".equals(factConfirm)) {%>selected<%}%>>留作参考</option>
						<option value="不予采纳" <%if ("不予采纳".equals(factConfirm)) {%>selected<%}%>>不予采纳</option>
					<%}else{%>
						<option value="有政策信息" <%if ("有政策信息".equals(factConfirm)) {%>selected<%}%>>有政策信息</option>
						<option value="无政策信息" <%if ("无政策信息".equals(factConfirm)) {%>selected<%}%>>无政策信息</option>
						<option value="对政策解释不接受" <%if ("对政策解释不接受".equals(factConfirm)) {%>selected<%}%>>对政策解释不接受</option>
					<%}%>
				</select></td>
				<td class="ltitle">认定说明：</td>
				<td class="content"><input type="text" name="factConfirmExplain" class="main_input12" value="<%=factConfirmExplain%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">现场查看：</td>
				<td class="content"><select name="sceneCheck" class="main_input12" >
					<option value="">请选择</option>
					<option value="是" <%if ("是".equals(sceneCheck)) {%>selected<%}%>>是</option>
					<option value="否" <%if ("否".equals(sceneCheck)) {%>selected<%}%>>否</option>
					<option value="无实际现场" <%if ("无实际现场".equals(sceneCheck)) {%>selected<%}%>>无实际现场</option>
				</select></td>
				<td class="ltitle">查看时间：</td>
				<td class="content"><input type="text" name="sceneTime" class="Wdate main_input12" maxlength="10" value="<%=sceneTime%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">诉求认定：</td>
				<td class="content"><select name="appealConfirm" class="main_input12" >
					<option value="">请选择</option>
					<option value="诉求合理合法" <%if ("诉求合理合法".equals(appealConfirm)) {%>selected<%}%>>诉求合理合法</option>
					<option value="无政策依据" <%if ("无政策依据".equals(appealConfirm)) {%>selected<%}%>>无政策依据</option>
					<option value="诉求过高" <%if ("诉求过高".equals(appealConfirm)) {%>selected<%}%>>诉求过高</option>
					<option value="其他" <%if ("其他".equals(appealConfirm)) {%>selected<%}%>>其他</option>
				</select></td>
				<td class="ltitle">认定说明：</td>
				<td class="content"><input type="text" name="appealConfirmExplain" class="main_input12" value="<%=appealConfirmExplain%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">被举报单位名称：</td>
				<td class="content"><input type="text" name="stExchangeName3" class="main_input12" value="<%=stExchangeName3%>"></td>
				<td class="ltitle">被举报单位地址：</td>
				<td class="content"><input type="text" name="stExchangeName4" class="main_input12" value="<%=stExchangeName4%>"></td>
			</tr>
			<tr>
				<td class="ltitle">是否工业区：</td>
				<td class="content" >
					<input type="radio" name="stFactoryRedo" value="1" onclick="onstFactoryRedoChange()" <%if ("1".equals(stFactory)) {%>checked<%}%> >是
					<input type="radio" name="stFactoryRedo" value="0" onclick="onstFactoryRedoChange()" <%if ("0".equals(stFactory)||"".equals(stFactory)) {%>checked<%}%> >否
					<input type="hidden" name="stFactory" value="<%=stFactory%>">
				</td>
				<td class="ltitle">工业区名称：</td>
				<td class="content"><input type="text" name="stFactoryName" class="main_input12" value="<%=stFactoryName%>" ></td>
			</tr>
			<!-- /确认 -->

			<!-- 答复 -->
			<tr>
				<td class="title2" colspan="4">答复</td>
			</tr>
			<tr>
				<td class="ltitle">办结汇总：</td>
				<td class="content" colspan="3"><textarea name="feedbackContent" rows="6" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(eventInfo.getFeedbackContent())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">答复市民要点：</td>
				<td class="content" colspan="3"><textarea name="replyPoint" rows="6" cols="" style="width:100%" ><%=replyPoint%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">落款单位：</td>
				<td class="content" colspan="3"><input type="text" name="inscribeUnit" class="main_input12" value="<%=inscribeUnit%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">签发人：</td>
				<td class="content">
					<!--<select name="signForName" class="main_input12"  >
					<option value="">请选择</option>
					<%if(sortedUserListq!=null&&sortedUserListq.size()>0){
					for(int i=0;i<sortedUserListq.size();i++){
					  SecurityUser user=(SecurityUser)sortedUserListq.get(i);
					%>
					 <option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(eventInfo.getSignForName()))){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="signForName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getSignForName())%>"/> 
				</td>
				<td class="ltitle">反馈人：</td>
				<td class="content">
					<!--<select name="feedbackName" class="main_input12"  >
					<option value="">请选择</option>
					<%if(sortedUserListf!=null&&sortedUserListf.size()>0){
					for(int i=0;i<sortedUserListf.size();i++){
					  SecurityUser user=(SecurityUser)sortedUserListf.get(i);
					%>
					 <option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(eventInfo.getFeedbackName()))){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="feedbackName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getFeedbackName())%>" />
				</td>
			</tr>
			<%if(ottffConst.isStringContains("2,21",status)){%>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>反馈审核人：</td>
				<td class="content">
					<select name="feedbackShr" id = "feedbackShr" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
						for(int i=0;i<sortedUserList.size();i++){
						  SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						 <option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(eventInfo.getFeedbackShr()))){%>selected<%}%>><%=user.getName()%></option>
						<%}}%>
					</select>
				</td>
			</tr>
			<%}else if(ottffConst.isStringContains("5",status)){%>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>反馈审核人：</td>
				<td class="content"><input type="text" name="feedbackShr" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getFeedbackShr())%>"></td>
			</tr>
			<%}%>
			<tr>
				<td class="ltitle">市民反馈说明：</td>
				<td class="content" colspan="3"><textarea name="citizenFeedback" rows="4" cols="" style="width:100%" ><%=citizenFeedback%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">答复方式：</td>
				<td class="content">
					<input type="radio" name="replyStyleRadio" value="电话" <%if ("电话".equals(replyStyle)) {%>checked<%}%> onclick="onReplyStyleChange()">电话
					<input type="radio" name="replyStyleRadio" value="书面" <%if ("书面".equals(replyStyle)) {%>checked<%}%> onclick="onReplyStyleChange()">书面
					<input type="radio" name="replyStyleRadio" value="约谈" <%if ("约谈".equals(replyStyle)) {%>checked<%}%> onclick="onReplyStyleChange()">约谈
					<!--<input type="radio" name="replyStyleRadio" value="无联系电话" <%if ("无联系电话".equals(replyStyle)) {%>checked<%}%> onclick="onReplyStyleChange()">无联系电话-->
					<input type="hidden" name="replyStyle" value="<%=replyStyle%>">
				</td>
				<td class="ltitle">答复市民时间：</td>
				<td class="content"><input type="text" name="replyTime" class="Wdate main_input12" maxlength="10" value="<%=replyTime%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">是否解决：</td>
				<td class="content" colspan="3">
				<%for (int i = 0; i < eventResultList.length; i++) {
					String[] array = eventResultList[i].split(":");
					if (array.length == 2) {%>
						<input type="radio" name="eventResultRadio" value="<%=array[0]%>"  onclick="onEventResultChange()" <%if (eventResult.equals(array[0])) {%>checked<%}%> ><%=array[1]%>
				<%	}
				}%>
					<input type="hidden" name="eventResult" value="<%=eventResult%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">是否满意：</td>
				<td class="content">
					<input type="radio" name="replySatisfactionRadio" value="满意" <%if ("满意".equals(replySatisfaction)) {%>checked<%}%>  onclick="onReplySatisfactionChange()">满意
					<input type="radio" name="replySatisfactionRadio" value="不满意" <%if ("不满意".equals(replySatisfaction)) {%>checked<%}%> onclick="onReplySatisfactionChange()">不满意
					<input type="radio" name="replySatisfactionRadio" value="认可" <%if ("认可".equals(replySatisfaction)) {%>checked<%}%> onclick="onReplySatisfactionChange()">认可
					<input type="radio" name="replySatisfactionRadio" value="未评价" <%if ("未评价".equals(replySatisfaction)) {%>checked<%}%> onclick="onReplySatisfactionChange()">未评价
					<input type="hidden" name="replySatisfaction" value="<%=replySatisfaction%>">
				</td>
				<td class="ltitle">是否公开：</td>
				<td class="content"><select name="publicFlag" class="main_input12" >
					<option value="是" <%if ("是".equals(publicFlag)) {%>selected<%}%>>是</option>
					<option value="否" <%if ("否".equals(publicFlag)) {%>selected<%}%>>否</option>
				</select></td>
			</tr>
			<!-- /答复 -->
			<%}%>

			<!-- 回访复核登记 -->
			<%if (ottffConst.isStringContains("0,5,6,7,8,10,13,20,17,22",status)&&"1".equals(returnVisit)||("old".equals(data))) { // 状态为“已反馈12345”、“回访复核待签收”、“回访复核已签收”、“回访复核已反馈”、“回访复核退回” ，“未先行联系（回访复核）”时显示回访复核信息 辖区回访复核%>
			<tr>
				<td class="title2" colspan="4">回访复核登记</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;回访复核内容描述：</td>
				<td class="content" colspan="3"><textarea name="returnVisitContent" rows="4" style="width:100%"><%=StringUtils.getNull2String(eventInfo.getReturnVisitContent())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;回访复核派件时间：</td>
				<td class="content"><input type="text" name="feedBackSendTime" class="Wdate main_input12" maxlength="10" value="<%=feedBackSendTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" disabled></td>			
				<td class="ltitle"><span style="color: red">*</span>&nbsp;回访复核应反时间：</td>
				<td class="content"><input type="text" name="returnVisitLimitTime" class="Wdate main_input12" maxlength="10" value="<%=returnVisitLimitTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入"></td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;回访复核登记人：</td>
				<td class="content"><input type="text" name="returnVisitRegisterName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getReturnVisitRegisterName())%>"></td>
			</tr>
			<%}%>
			<!-- /回访复核登记 -->

			<!-- 回访复核先行联系 -->
			<%if (ottffConst.isStringContains("6,7,8,10,13,20,22",status)) { // 状态为“回访复核待签收”、“回访复核已签收”、“回访复核已反馈”、“回访复核退回”时显示回访复核信息%>
			<tr>
				<td class="title2" colspan="4">回访复核先行联系</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;<span style="color: red">*</span>先行联系：</td>
				<td class="content"><select name="stReturnVisitFirstContact" class="main_input12" >
					<option value="">请选择</option>
					<option value="已联" <%if ("已联".equals(stReturnVisitFirstContact)) {%>selected<%}%>>已联</option>
					<option value="未联" <%if ("未联".equals(stReturnVisitFirstContact)) {%>selected<%}%>>未联</option>
				</select></td>
				<td class="ltitle">&nbsp;<span style="color: red">*</span>未联原因：</td>
				<td class="content"><select name="stReturnNocontactReason" class="main_input12" >
					<option value="">请选择</option>
					<option value="始终关机" <%if ("始终关机".equals(stReturnNocontactReason)) {%>selected<%}%>>始终关机</option>
					<option value="始终未接" <%if ("始终未接".equals(stReturnNocontactReason)) {%>selected<%}%>>始终未接</option>
					<option value="其他" <%if ("其他".equals(stReturnNocontactReason)) {%>selected<%}%>>其他</option>
				</select></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;先行联系人：</td>
				<td class="content">
					<!--<select name="stReturnVisitContactMan" class="main_input12" >
					<option value="">请选择</option>
					<%if(sortedUserList!=null&&sortedUserList.size()>0){
					for(int i=0;i<sortedUserList.size();i++){
					SecurityUser user=(SecurityUser)sortedUserList.get(i);
					%>
					<option value="<%=user.getName()%>" <%if(user.getName().equals(stReturnVisitContactMan)){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="stReturnVisitContactMan" class="main_input12" value="<%=stReturnVisitContactMan%>" >
				</td>
				<td class="ltitle">&nbsp;联系时间：</td>
				<td class="content"><input type="text" name="dtReturnVisitContactTime" class="Wdate main_input12" maxlength="10" value="<%=dtReturnVisitContactTime%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;联系内容：</td>
				<td class="content" colspan="3"><textarea name="stReturnContactContent" rows="3" cols="" style="width:100%" ><%=stReturnContactContent%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;联系内容备注：</td>
				<td class="content" colspan="3"><textarea name="remarkContactContent" rows="3" cols="" style="width:100%" ><%=remarkContactContent%></textarea></td>
			</tr>
			<%}%>
			<!-- /回访复核先行联系 -->

			<%if (ottffConst.isStringContains("7,8,10,5,2,19,20,21,22",status)&&"Y".equals(callbackFlag)) { // 状态为“回访复核待签收”、“回访复核已签收”、“回访复核已反馈”、“回访复核退回”时显示回访复核信息%>
			<!-- 回访复核确认 -->
			<tr>
				<td class="title2" colspan="4">回访复核确认</td>
			</tr>
			<tr>
				<td class="ltitle">事实认定：</td>
				<td class="content"><select name="stReturnVisitFactConfirm" class="main_input12" >
					<option value="">请选择</option>
					<%if(("1,3,4").contains(eventInfo.getReportType())){%>
						<option value="属实" <%if ("属实".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>属实</option>
						<option value="部分属实" <%if ("部分属实".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>部分属实</option>
						<option value="不属实" <%if ("不属实".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>不属实</option>
					<%}else if("2".equals(eventInfo.getReportType())){%>
						<option value="采纳" <%if ("采纳".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>采纳</option>
						<option value="部分采纳" <%if ("部分采纳".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>部分采纳</option>
						<option value="留作参考" <%if ("留作参考".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>留作参考</option>
						<option value="不予采纳" <%if ("不予采纳".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>不予采纳</option>
					<%}else{%>
						<option value="有政策信息" <%if ("有政策信息".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>有政策信息</option>
						<option value="无政策信息" <%if ("无政策信息".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>无政策信息</option>
						<option value="对政策解释不接受" <%if ("对政策解释不接受".equals(stReturnVisitFactConfirm)) {%>selected<%}%>>对政策解释不接受</option>
					<%}%>
				</select></td>
				<td class="ltitle">认定说明：</td>
				<td class="content"><input type="text" name="stReturnFactConfirmExplain" class="main_input12" value="<%=stReturnFactConfirmExplain%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">现场查看：</td>
				<td class="content"><select name="stReturnVisitSceneCheck" class="main_input12" >
					<option value="">请选择</option>
					<option value="是" <%if ("是".equals(stReturnVisitSceneCheck)) {%>selected<%}%>>是</option>
					<option value="否" <%if ("否".equals(stReturnVisitSceneCheck)) {%>selected<%}%>>否</option>
					<option value="无实际现场" <%if ("无实际现场".equals(stReturnVisitSceneCheck)) {%>selected<%}%>>无实际现场</option>
				</select></td>
				<td class="ltitle">查看时间：</td>
                <td class="content"><input type="text" name="returnVisitTime" class="Wdate main_input12" maxlength="10" value="<%=returnVisitTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">诉求认定：</td>
				<td class="content"><select name="stReturnVisitAppealConfirm" class="main_input12" >
					<option value="">请选择</option>
					<option value="诉求合理合法" <%if ("诉求合理合法".equals(stReturnVisitAppealConfirm)) {%>selected<%}%>>诉求合理合法</option>
					<option value="无政策依据" <%if ("无政策依据".equals(stReturnVisitAppealConfirm)) {%>selected<%}%>>无政策依据</option>
					<option value="诉求过高" <%if ("诉求过高".equals(stReturnVisitAppealConfirm)) {%>selected<%}%>>诉求过高</option>
					<option value="其他" <%if ("其他".equals(stReturnVisitAppealConfirm)) {%>selected<%}%>>其他</option>
				</select></td>
				<td class="ltitle">认定说明：</td>
				<td class="content"><input type="text" name="stReturnAppealExplain" class="main_input12" value="<%=stReturnAppealExplain%>" ></td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;被举报单位名称：</td>
				<td class="content">
					<input type="text" name="beReportName" class="main_input12" value="<%=beReportName%>">
				</td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;被举报单位地址：</td>
				<td class="content">
					<input type="text" name="beReportAddress" class="main_input12" value="<%=beReportAddress%>">
				</td>			
			</tr>
			<tr>
				<td class="ltitle">是否工业区：</td>
				<td class="content" >
					<input type="radio" name="stFactoryRedo1" value="1" onclick="onstFactoryRedoChange()" <%if ("1".equals(stFactory1)) {%>checked<%}%> >是
					<input type="radio" name="stFactoryRedo1" value="0" onclick="onstFactoryRedoChange()" <%if ("0".equals(stFactory1)||"".equals(stFactory1)) {%>checked<%}%> >否
					<input type="hidden" name="stFactory1" value="<%=stFactory1%>">
				</td>
				<td class="ltitle">工业区名称：</td>
				<td class="content"><input type="text" name="stFactoryName1" class="main_input12" value="<%=stFactoryName1%>" ></td>
			</tr>
			<!-- /回访复核确认 -->

			<!-- 回访复核答复 -->
			<tr>
				<td class="title2" colspan="4">回访复核答复</td>
			</tr>
			<tr>
				<td class="ltitle">办结汇总：<br>（不超过1000字）</td>
				<td class="content" colspan="3"><textarea name="returnVisitFeedback" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=returnVisitFeedback%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">答复市民要点：<br>（不超过500字）</td>
				<td class="content" colspan="3"><textarea name="stReturnVisitReplyPoint" rows="6" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=stReturnVisitReplyPoint%></textarea><span style="color: red">热线话务员在接电中回复市民的参考依据，必须包括处置部门、回复市民时间、处置情况。</span></td>
			</tr>
			<tr>
				<td class="ltitle">落款单位：</td>
				<td class="content" colspan="3"><input type="text" name="stReturnVisitInscribeUnit" class="main_input12" value="<%=stReturnVisitInscribeUnit%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">签发人：</td>
				<td class="content">
					<!--<select name="returnVisitFzrName" class="main_input12"  >
					<option value="">请选择</option>
					<%if(sortedUserListq!=null&&sortedUserListq.size()>0){
					for(int i=0;i<sortedUserListq.size();i++){
					  SecurityUser user=(SecurityUser)sortedUserListq.get(i);
					%>
					 <option value="<%=user.getName()%>" <%if(user.getName().equals(returnVisitFzrName)){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="returnVisitFzrName" class="main_input12" value="<%=returnVisitFzrName%>" >
				</td>
				<td class="ltitle">反馈人：</td>
				<td class="content">
					<!--<select name="returnVisitJbrName" class="main_input12"  >
					<option value="">请选择</option>
					<%if(sortedUserListf!=null&&sortedUserListf.size()>0){
					for(int i=0;i<sortedUserListf.size();i++){
					  SecurityUser user=(SecurityUser)sortedUserListf.get(i);
					%>
					 <option value="<%=user.getName()%>" <%if(user.getName().equals(returnVisitJbrName)){%>selected<%}%>><%=user.getName()%></option>
					<%}}%>
					</select>-->
					<input type="text" name="returnVisitJbrName" class="main_input12" value="<%=returnVisitJbrName%>" >
				</td>
			</tr>
			<%if(ottffConst.isStringContains("8,22",status)){%>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>反馈审核人：</td>
				<td class="content">
					<select name="feedbackShr2" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
						for(int i=0;i<sortedUserList.size();i++){
						  SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						 <option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(eventInfo.getFeedBackShr2()))){%>selected<%}%>><%=user.getName()%></option>
						<%}}%>
					</select>
				</td>
			</tr>
			<%}else if(ottffConst.isStringContains("5",status)){%>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>反馈审核人：</td>
				<td class="content"><input type="text" name="feedbackShr2" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getFeedBackShr2())%>"></td>
			</tr>
			<%}%>
			<tr>
				<td class="ltitle">市民反馈说明：</td>
				<td class="content" colspan="3"><textarea name="stReturnCitizenFeedback" rows="4" cols="" style="width:100%" ><%=stReturnCitizenFeedback%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">答复方式：</td>
				<td class="content">
					<input type="radio" name="stReturnVisitReplyStyleRadio" value="电话" onclick="stReturnVisitReplyStyleChange()" <%if ("电话".equals(stReturnVisitReplyStyle)) {%>checked<%}%> >电话
					<input type="radio" name="stReturnVisitReplyStyleRadio" value="书面" onclick="stReturnVisitReplyStyleChange()" <%if ("书面".equals(stReturnVisitReplyStyle)) {%>checked<%}%> >书面
					<input type="radio" name="stReturnVisitReplyStyleRadio" value="约谈" onclick="stReturnVisitReplyStyleChange()" <%if ("约谈".equals(stReturnVisitReplyStyle)) {%>checked<%}%> >约谈
					<!--<input type="radio" name="stReturnVisitReplyStyleRadio" value="无联系电话" onclick="stReturnVisitReplyStyleChange()" <%if ("无联系电话".equals(stReturnVisitReplyStyle)) {%>checked<%}%> >无联系电话-->
					<input type="hidden" name="stReturnVisitReplyStyle" value="<%=stReturnVisitReplyStyle%>">
				</td>
				<td class="ltitle">答复市民时间：</td>
				<td class="content"><input type="text" name="dtReturnVisitReplyTime" class="Wdate main_input12" maxlength="10" value="<%=dtReturnVisitReplyTime%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr>
				<td class="ltitle">是否解决：</td>
				<td class="content" colspan="3">
					<%for (int i = 0; i < eventResultList.length; i++) {
						String[] array = eventResultList[i].split(":");
						if (array.length == 2) {%>
					<input type="radio" name="stReturnVisitEventResultRadio" value="<%=array[0]%>" onclick="onstReturnVisitEventResultRadioChange()" <%if (stReturnVisitEventResult.equals(array[0])) {%>checked<%}%> ><%=array[1]%>
					<%}
					}%>
					<input type="hidden" name="stReturnVisitEventResult" value="<%=stReturnVisitEventResult%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">是否满意：</td>
				<td class="content">
					<input type="radio" name="stReturnReplySatisfactionRadio" value="满意" onclick="radiochange('stReturnReplySatisfactionRadio','stReturnReplySatisfaction')" <%if ("满意".equals(stReturnReplySatisfaction)) {%>checked<%}%> >满意
					<input type="radio" name="stReturnReplySatisfactionRadio" value="不满意" onclick="radiochange('stReturnReplySatisfactionRadio','stReturnReplySatisfaction')" <%if ("不满意".equals(stReturnReplySatisfaction)) {%>checked<%}%> >不满意
					<input type="radio" name="stReturnReplySatisfactionRadio" value="认可" onclick="radiochange('stReturnReplySatisfactionRadio','stReturnReplySatisfaction')" <%if ("认可".equals(stReturnReplySatisfaction)) {%>checked<%}%> >认可
					<input type="radio" name="stReturnReplySatisfactionRadio" value="未评价" onclick="radiochange('stReturnReplySatisfactionRadio','stReturnReplySatisfaction')" <%if ("未评价".equals(stReturnReplySatisfaction)) {%>checked<%}%> >未评价
					<input type="hidden" name="stReturnReplySatisfaction" value="<%=stReturnReplySatisfaction%>">
				</td>
				<td class="ltitle">是否公开：</td>
				<td class="content"><select name="stReturnPublicFlag" class="main_input12" >
					<option value="是" <%if ("是".equals(stReturnPublicFlag)) {%>selected<%}%>>是</option>
					<option value="否" <%if ("否".equals(stReturnPublicFlag)) {%>selected<%}%>>否</option>
				</select></td>
			</tr>
			<!-- /回访复核答复 -->

			<!-- 回访复核反馈信息 -->
			<tr>
				<td class="title2" colspan="4">回访复核反馈信息</td>
			</tr>
			<tr>
				<td class="ltitle">签收人：</td>
				<td class="content"><input type="text" name="returnVisitSignForName" class="main_input12" value="<%=returnVisitSignForName%>" ></td>
			    <td class="ltitle">联系电话：</td>
				<td class="content"><input type="text" name="returnVisitPhoneNumber" class="main_input12" value="<%=returnVisitPhoneNumber%>" ></td>
			</tr>
			<!-- /回访复核反馈信息 -->
			<%}%>

            <!-- 退单信息 -->
			<%
				if(tuidanlist!=null&&tuidanlist.size()>0){
					for(int i=0;i<tuidanlist.size();i++){
						ottffTuidanInfo=(OttffTuidanInfo)tuidanlist.get(i);
						String applitime1="";
						if (ottffTuidanInfo.getApplyTime() != null) {
							applitime1 = sdf3.format(ottffTuidanInfo.getApplyTime());// 退单时间
						}
			%>
			<tr>
				<td class="title2" colspan="4">第<%=StringUtils.getNull2String(ottffTuidanInfo.getNmNo())%>次退单信息</td>
			</tr>
			<tr>
				<td class="ltitle">退单理由：</td>
				<td class="content"><select name="backClass<%=i+1%>" class="main_input12">
					<option value=""></option>
					<option value=""></option>
					<option value="21" <%if ("21".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非热线受理范围</option>
					<option value="22" <%if ("22".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非本单位职能</option>
					<option value="23" <%if ("23".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非本单位辖区</option>
					<option value="24" <%if ("24".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>因工单要素不全致无法处置</option>
					<option value="25" <%if ("25".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>不当重复派单</option>
					<option value="26" <%if ("26".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>其他</option>
					<!--<option value="11" <%if ("11".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>基本要素不全</option>
					<option value="12" <%if ("12".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>内容表述不清</option>
					<option value="13" <%if ("13".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>派单方向有误</option>
					<option value="14" <%if ("14".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>其他</option>-->
				</select></td>
				<td class="ltitle">退单时间：</td>
				<td class="content" ><input type="text" name="applyTime<%=i+1%>" class="Wdate main_input12" maxlength="10" value="<%=applitime1%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" disabled></td>
			</tr>
			<tr>
				<td class="ltitle">退单说明：</td>
				<td class="content" colspan="3"><textarea name="summary<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffTuidanInfo.getSummary())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">退单申请人：</td>
				<td class="content"><input type="text" name="operator" class="main_input12" value="<%=StringUtils.getNull2String(ottffTuidanInfo.getOperator())%>" ></td>
			</tr>
			<%if(!"".equals(StringUtils.getNull2String(ottffTuidanInfo.getFirstPass()))){%>
			<tr>
				<td class="ltitle">退单初审结果：</td>
				<td class="content" colspan="3"><select name="firstPass<%=i+1%>" class="main_input12">
					<option value="">请选择</option>
					<option value="通过" <%if ("通过".equals(StringUtils.getNull2String(ottffTuidanInfo.getFirstPass()))) {%>selected<%}%>>通过</option>
					<option value="不通过" <%if ("不通过".equals(StringUtils.getNull2String(ottffTuidanInfo.getFirstPass()))) {%>selected<%}%>>不通过</option>
				</select></td>
			</tr>
			<tr >
				<td class="ltitle">退单初审理由：</td>
				<td class="content" colspan="3"><textarea name="firstReasons<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffTuidanInfo.getFirstReasons())%></textarea></td>
			</tr>
			<%}%>
			<tr>
				<td class="ltitle">退单终审结果：</td>
				<td class="content" colspan="3"><select name="isPass<%=i+1%>" class="main_input12">
					<option value="">请选择</option>
					<option value="通过" <%if ("通过".equals(StringUtils.getNull2String(ottffTuidanInfo.getIsPass()))) {%>selected<%}%>>通过</option>
					<option value="不通过" <%if ("不通过".equals(StringUtils.getNull2String(ottffTuidanInfo.getIsPass()))) {%>selected<%}%>>不通过</option>
				</select></td>
			</tr>
			<tr >
				<td class="ltitle">退单终审理由：</td>
				<td class="content" colspan="3"><textarea name="reasons<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffTuidanInfo.getReasons())%></textarea></td>
			</tr>
			<%}
			}%>
			<!-- /退单信息 -->

			<!-- 退单信息（废弃） -->
			<tr id="tuidan" style="display:none">
				<td class="title2" colspan="4">退单信息</td>
			</tr>
			<tr id="tdly" style="display:none">
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退单理由：</td>
				<td class="content" ><select name="backClass" class="main_input11">
					<option value=""></option>
					<option value="21" <%if ("21".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非热线受理范围</option>
					<option value="22" <%if ("22".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非本单位职能</option>
					<option value="23" <%if ("23".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>非本单位辖区</option>
					<option value="24" <%if ("24".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>因工单要素不全致无法处置</option>
					<option value="25" <%if ("25".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>不当重复派单</option>
					<option value="26" <%if ("26".equals(StringUtils.getNull2String(ottffTuidanInfo.getBackClass()))) {%>selected<%}%>>其他</option>
					
					<!--<option value="11" <%if ("11".equals(StringUtils.getNull2String(ottffTuidanInfo2.getBackClass()))) {%>selected<%}%>>基本要素不全</option>
					<option value="12" <%if ("12".equals(StringUtils.getNull2String(ottffTuidanInfo2.getBackClass()))) {%>selected<%}%>>内容表述不清</option>
					<option value="13" <%if ("13".equals(StringUtils.getNull2String(ottffTuidanInfo2.getBackClass()))) {%>selected<%}%>>派单方向有误</option>
					<option value="14" <%if ("14".equals(StringUtils.getNull2String(ottffTuidanInfo2.getBackClass()))) {%>selected<%}%>>其他</option>-->
				</select></td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退单时间：</td>
				<td class="content" ><input type="text" name="applyTime" class="Wdate main_input12" maxlength="10" value="<%if(ottffTuidanInfo2.getApplyTime() != null){out.print(sdf3.format(ottffTuidanInfo2.getApplyTime()));}else{out.print(sdf3.format(new Date()));}%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
			</tr>
			<tr id="tdsm" style="display:none">
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退单说明：</td>
				<td class="content" colspan="3" ><textarea name="summary" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffTuidanInfo2.getSummary())%></textarea></td>
			</tr>
			<tr id="tdsqr" style="display:none">
				<td class="ltitle"><span style="color: red">*</span>&nbsp;退单申请人：</td>
	            <td class="content" >
					<select name="operator" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int i=0;i<sortedUserList.size();i++){
								SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffTuidanInfo2.getOperator()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="operator" class="main_input11" value="<%=StringUtils.getNull2String(ottffTuidanInfo2.getOperator())%>" -->
				</td>
				<td class="ltitle"><span style="color: red">*</span>审核人：</td>
				<td class="content"><input type="text" name="approver" class="main_input12" value="<%=returnVisitSignForName%>" ></td>
			</tr>
			<%if(ottffYquotaInfo!=null){%>
			<tr>
				<td class="title2" colspan="4"><%=StringUtils.getNull2String(ottffYquotaInfo.getYear())%>年<%=StringUtils.getNull2String(ottffYquotaInfo.getSeason())%>全市环保延期申请额度</td>
			</tr>
			<tr>
				<td width="18%" class="ltitle">初始化数量：</td>
				<td width="32%" class="content"><input type="text" name="InitCounts" class="main_input12" value="<%=StringUtils.getNull2String(ottffYquotaInfo.getInitCounts())%>" disabled></td>
				<td width="18%" class="ltitle">已用数量：</td>
				<td width="32%" class="content"><input type="text" name="UsedCounts" class="main_input12"  value="<%=StringUtils.getNull2String(ottffYquotaInfo.getUsedCounts())%>" disabled></td>
			</tr>
			<%}%>
			<!-- 退单信息（废弃） -->

			<!-- 延期申请 new 2015-08-14 -->
			<!-- 区县延期申请 -->
			<%if(yqsqlist!=null&&yqsqlist.size()>0){
				for(int i=0;i<yqsqlist.size();i++){
					OttffYqapplyInfo ottffYqapplyInfo=(OttffYqapplyInfo)yqsqlist.get(i);
					String applitime2="";
					if (ottffYqapplyInfo.getApplyTime() != null) {
						applitime2 = sdf4.format(ottffYqapplyInfo.getApplyTime());// 延期申请时间
					}                  
					if("quxian".equals(ottffYqapplyInfo.getBak1())){
			%>
			<tr>
				<td class="title2" colspan="4">第<%=StringUtils.getNull2String(ottffYqapplyInfo.getNmNo())%>次区县延期申请</td>
			</tr>
			<tr>
				<td class="ltitle">延期申请理由：</td>
				<td class="content">
					<select name="summary<%=i+1%>" class="main_input11">
						<option value=""></option>
						<option value="0" <%if ("0".equals(StringUtils.getNull2String(ottffYqapplyInfo.getSummary()))) {%>selected<%}%>>法定时限</option>
						<option value="1" <%if ("1".equals(StringUtils.getNull2String(ottffYqapplyInfo.getSummary()))) {%>selected<%}%>>特殊疑难</option>
					</select>
				</td>
				<td class="ltitle">延期申请天数：</td>
				<td class="content">
					<select name="delayDays<%=i+1%>" class="main_input11">
						<option value=""></option>
						<option value="15" <%if ("15".equals(StringUtils.getNull2String(ottffYqapplyInfo.getDelayDays()))) {%>selected<%}%>>15个工作日</option>
						<option value="30" <%if ("30".equals(StringUtils.getNull2String(ottffYqapplyInfo.getDelayDays()))) {%>selected<%}%>>30个工作日</option>
					</select>
				</td>
            </tr>
			<tr>
				<td class="ltitle">延期申请日期：</td>
				<td class="content" ><input type="text" name="applyTime<%=i+1%>" class="Wdate main_input12" maxlength="10" value="<%=applitime2%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
				<td class="ltitle">延期申请人：</td>
				<td class="content" ><input type="text" name="qxOperator<%=i+1%>" class="main_input11" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getQxOperator())%>" ></td>
			</tr>
			<tr>
				<td class="ltitle">经办人：</td>
				<td class="content"><input type="text" name="qxHandler<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getQxHandler())%>" ></td>
				<td class="ltitle">负责人：</td>
				<td class="content"><input type="text" name="qxCharger<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getQxCharger())%>" ></td>
			</tr>
            <tr>
				<td class="ltitle">联系电话：</td>
				<td class="content" colspan="3"><input type="text" name="qxContactno<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getQxContactno())%>" ></td>
			</tr>
			<tr >
				<td class="ltitle">备注（阐述申请理由）：</td>
				<td class="content" colspan="3"><textarea name="qxComment<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo.getQxComment())%></textarea></td>
			</tr>
			<%if(!"".equals(StringUtils.getNull2String(ottffYqapplyInfo.getFirstPass()))){// 延期初审%>
			<tr>
				<td class="ltitle">延期初审结果：</td>
				<td class="content" >
					<select name="firstPass<%=i+1%>" class="main_input11">
						<option value="">请选择</option>
						<option value="通过" <%if ("通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getFirstPass()))) {%>selected<%}%>>通过</option>
						<option value="不通过" <%if ("不通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getFirstPass()))) {%>selected<%}%>>不通过</option>
					</select>
				</td>
				<td class="ltitle">联系电话（总队）：</td>
				<td class="content">
					<input type="text" name="contactNumber<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getContactNumber())%>" >
				</td>
			</tr>
			<tr>
				<td class="ltitle">延期初审时间：</td>
				<td class="content" ><input type="text" name="firstDate<%=i+1%>" class="Wdate main_input12" maxlength="10" value="<%if(ottffYqapplyInfo.getFirstDate() != null){out.print(sdf4.format(ottffYqapplyInfo.getFirstDate()));}else{out.print(sdf4.format(new Date()));}%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
				<td class="ltitle">延期申请人（总队）：</td>
				<td class="content" >
					<select name="operator<%=i+1%>" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int j=0;j<sortedUserList.size();j++){
								SecurityUser user=(SecurityUser)sortedUserList.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getOperator()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="operator<%=i+1%>" class="main_input11" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getOperator())%>" -->
				</td>
			</tr>
			<tr>
				<td class="ltitle">经办人（总队）：</td>
				<td class="content">
					<select name="handler<%=i+1%>" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int j=0;j<sortedUserList.size();j++){
								SecurityUser user=(SecurityUser)sortedUserList.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getHandler()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="handler<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getHandler())%>" -->
				</td>
				<td class="ltitle">负责人（总队）：</td>
				<td class="content">
					<select name="charger<%=i+1%>" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserListfuze!=null&&sortedUserListfuze.size()>0){
							for(int j=0;j<sortedUserListfuze.size();j++){
								SecurityUser user=(SecurityUser)sortedUserListfuze.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getCharger()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="charger<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getCharger())%>" -->
				</td>
			</tr>
            <tr >
				<td class="ltitle">备注：</td>
				<td class="content" colspan="3"><textarea name="remark<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo.getRemark())%></textarea></td>
			</tr>
			<%}%>
			<%if(!"".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))){%>
			<tr>
			<td class="ltitle">延期终审结果：</td>
				<td class="content" colspan="3">
					<select name="isPass<%=i+1%>" class="main_input11">
						<option value="">请选择</option>
						<option value="通过" <%if ("通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))) {%>selected<%}%>>通过</option>
						<option value="不通过" <%if ("不通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))) {%>selected<%}%>>不通过</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="ltitle">延期终审理由：</td>
				<td class="content" colspan="3"><textarea name="reasons<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo.getReasons())%></textarea></td>
			</tr>
			<%}%>
			<!-- 区县延期申请 -->

			<!-- 总队延期申请 -->
			<%} else {%>
			<tr>
				<td class="title2" colspan="4">第<%=StringUtils.getNull2String(ottffYqapplyInfo.getNmNo())%>次总队延期申请</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;延期申请理由：</td>
				<td class="content">
					<select name="summary<%=i+1%>" class="main_input11">
						<option value=""></option>
						<option value="0" <%if ("0".equals(StringUtils.getNull2String(ottffYqapplyInfo.getSummary()))) {%>selected<%}%>>法定时限</option>
						<option value="1" <%if ("1".equals(StringUtils.getNull2String(ottffYqapplyInfo.getSummary()))) {%>selected<%}%>>特殊疑难</option>
					</select>
				</td>
				<td class="ltitle">&nbsp;延期申请天数：</td>
				<td class="content">
					<select name="delayDays<%=i+1%>" class="main_input11">
						<option value=""></option>
						<option value="15" <%if ("15".equals(StringUtils.getNull2String(ottffYqapplyInfo.getDelayDays()))) {%>selected<%}%>>15个工作日</option>
						<option value="30" <%if ("30".equals(StringUtils.getNull2String(ottffYqapplyInfo.getDelayDays()))) {%>selected<%}%>>30个工作日</option>
					</select>
				</td>
            </tr>
			<tr>
				<td class="ltitle">&nbsp;延期申请日期：</td>
				<td class="content" ><input type="text" name="applyTime<%=i+1%>" class="Wdate main_input12" maxlength="10" value="<%=applitime2%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
				<td class="ltitle">&nbsp;延期申请人：</td>
				<td class="content" >
					<select name="operator<%=i+1%>" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int j=0;j<sortedUserList.size();j++){
								SecurityUser user=(SecurityUser)sortedUserList.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getOperator()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--input type="text" name="operator<%=i+1%>" class="main_input11" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getOperator())%>" -->
				</td>
			</tr>
			<tr>
				<td class="ltitle">&nbsp;经办人：</td>
				<td class="content">
					<select name="handler<%=i+1%>" class="main_input12" disabled >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int j=0;j<sortedUserList.size();j++){
								SecurityUser user=(SecurityUser)sortedUserList.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getHandler()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
						<!--input type="text" name="handler<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getHandler())%>" -->
				</td>
				<td class="ltitle">&nbsp;负责人：</td>
				<td class="content">
					<select name="charger<%=i+1%>" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserListfuze!=null&&sortedUserListfuze.size()>0){
							for(int j=0;j<sortedUserListfuze.size();j++){
								SecurityUser user=(SecurityUser)sortedUserListfuze.get(j);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo.getCharger()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="charger<%=i+1%>" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getCharger())%>" -->
				</td>
			</tr>
            <tr>
				<td class="ltitle">&nbsp;联系电话：</td>
				<td class="content" colspan="3"><input type="text" name="contactNumber<%=i+1%>" class="main_input11" value="<%=StringUtils.getNull2String(ottffYqapplyInfo.getContactNumber())%>" ></td>
			</tr>
			<tr >
				<td class="ltitle">&nbsp;备注（阐述申请理由）：</td>
				<td class="content" colspan="3"><textarea name="remark<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo.getRemark())%></textarea></td>
			</tr>
			<%if(!"".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))){%>
			<tr>
				<td class="ltitle">延期终审结果：</td>
				<td class="content" colspan="3">
					<select name="isPass<%=i+1%>" class="main_input11">
						<option value="">请选择</option>
						<option value="通过" <%if ("通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))) {%>selected<%}%>>通过</option>
						<option value="不通过" <%if ("不通过".equals(StringUtils.getNull2String(ottffYqapplyInfo.getIsPass()))) {%>selected<%}%>>不通过</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="ltitle">延期终审理由：</td>
				<td class="content" colspan="3"><textarea name="reasons<%=i+1%>" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo.getReasons())%></textarea></td>
			</tr>
			<%}%>
			<!-- /总队延期申请 -->
			<%		}
				}
			}%>
			<!-- /延期申请 new 2015-08-14 -->
			
			<!-- 总队延期申请（废弃） -->
			<tr id="zdyqsq" style="display:none">
				<td class="title2" colspan="4">总队延期申请</td>
			</tr>
			<tr id="yqly" style="display:none">
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>延期申请理由：</td>
				<td class="content">
					<select name="zdsummary" class="main_input11">
						<option value=""></option>
						<option value="0" <%if ("0".equals(StringUtils.getNull2String(ottffYqapplyInfo2.getSummary()))) {%>selected<%}%>>法定时限</option>
						<option value="1" <%if ("1".equals(StringUtils.getNull2String(ottffYqapplyInfo2.getSummary()))) {%>selected<%}%>>特殊疑难</option>
					</select>
				</td>
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>延期申请天数：</td>
				<td class="content">
					<select name="delayDays" class="main_input11">
						<option value=""></option>
						<option value="15" <%if ("15".equals(StringUtils.getNull2String(ottffYqapplyInfo2.getDelayDays()))) {%>selected<%}%>>15个工作日</option>
						<option value="30" <%if ("30".equals(StringUtils.getNull2String(ottffYqapplyInfo2.getDelayDays()))) {%>selected<%}%>>30个工作日</option>
					</select>
				</td>
            </tr>
			<tr id="yqdate" style="display:none">
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>延期申请日期：</td>
				<td class="content" ><input type="text" name="zdapplyTime" class="Wdate main_input12" maxlength="10" value="<%if(ottffYqapplyInfo2.getApplyTime() != null){out.print(sdf4.format(ottffYqapplyInfo2.getApplyTime()));}else{out.print(sdf4.format(new Date()));}%>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" title="输入的格式例如：2010-05-01 14:25。可以通过时间控件来输入，也可以手工输入" ></td>
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%>&nbsp;<%}%>延期申请人：</td>
				<td class="content" >
					<select name="zdoperator" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int i=0;i<sortedUserList.size();i++){
								SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo2.getOperator()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--<input type="text" name="zdoperator" class="main_input11" value="<%=StringUtils.getNull2String(ottffYqapplyInfo2.getOperator())%>" -->
				</td>
			</tr>
			<tr id="yqjbr" style="display:none">
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>经办人：</td>
				<td class="content">
					<select name="handler" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserList!=null&&sortedUserList.size()>0){
							for(int i=0;i<sortedUserList.size();i++){
								SecurityUser user=(SecurityUser)sortedUserList.get(i);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo2.getHandler()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--input type="text" name="handler" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo2.getHandler())%>" -->
				</td>
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>负责人：</td>
				<td class="content">
					<select name="charger" class="main_input12"  >
						<option value="">请选择</option>
						<%if(sortedUserListfuze!=null&&sortedUserListfuze.size()>0){
							for(int i=0;i<sortedUserListfuze.size();i++){
								SecurityUser user=(SecurityUser)sortedUserListfuze.get(i);
						%>
						<option value="<%=user.getName()%>" <%if(user.getName().equals(StringUtils.getNull2String(ottffYqapplyInfo2.getCharger()))){%>selected<%}%>><%=user.getName()%></option>
						<%	}
						}%>
					</select>
					<!--input type="text" name="charger" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo2.getCharger())%>" -->
				</td>
			</tr>
            <tr id="yqdh" style="display:none">
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>联系电话：</td>
				<td class="content" colspan="3"><input type="text" name="contactNumber" class="main_input12" value="<%=StringUtils.getNull2String(ottffYqapplyInfo2.getContactNumber())%>" ></td>
			</tr>
			<tr id="yqbz" style="display:none">
				<td class="ltitle"><%if ("12".contains(status)||"13".contains(status)) {%><span style="color: red">*</span>&nbsp;<%}%>备注（阐述申请理由）：</td>
				<td class="content" colspan="3"><textarea name="remark" rows="4" cols="" style="width:100%" onpropertychange="feedbackMaxLength(this, 500)" ><%=StringUtils.getNull2String(ottffYqapplyInfo2.getRemark())%></textarea></td>
			</tr>
			<!-- /总队延期申请（废弃） -->

			<!-- 延期申请 -->
			<%if ("9".equals(status)) { // 状态为“延期申请待审核”时显示延期申请%>
			<tr>
				<td class="title2" colspan="4">延期申请</td>
			</tr>
			<tr>
				<td class="ltitle">延期理由：</td>
				<td class="content" colspan="3"><textarea name="delayReason" rows="4" style="width:100%" readonly><%=StringUtils.getNull2String(eventInfo.getDelayReason())%></textarea></td>
			</tr>
			<tr>
				<td class="ltitle">经办人：</td>
				<td class="content"><input type="text" name="delayJbrName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getDelayJbrName())%>" readonly></td>
				<td class="ltitle">负责人：</td>
				<td class="content"><input type="text" name="delayFzrName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getDelayFzrName())%>" readonly></td>
			</tr>
			<tr>
				<td class="ltitle">承诺时限：</td>
				<td class="content"><input type="text" name="delayLimitTime" class="Wdate main_input12" maxlength="10" value="<%=delayLimitTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入"></td>
				<td class="ltitle">联系电话：</td>
				<td class="content"><input type="text" name="delayPhoneNumber" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getDelayPhoneNumber())%>" readonly></td>
			</tr>

			<!-- 延期申请审核信息 -->
			<tr>
				<td class="title2" colspan="4">审核信息</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;经办人：</td>
				<td class="content"><input type="text" name="delayZdJbrName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getDelayZdJbrName())%>"></td>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;负责人：</td>
				<td class="content"><input type="text" name="delayZdFzrName" class="main_input12" value="<%=StringUtils.getNull2String(eventInfo.getDelayZdFzrName())%>"></td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;驳回理由：</td>
				<td class="content" colspan="3"><textarea name="delayRejectReason" rows="4" style="width:100%"><%=StringUtils.getNull2String(eventInfo.getDelayRejectReason())%></textarea><span style="color: red">驳回延期申请时请注明理由。</span></td>
			</tr>
			<!-- /延期申请审核信息 -->

			<%}%>
			<!-- /延期申请 -->

			<!-- 隐藏域 -->
			<input type="hidden" name="id" value="<%=id%>"><!-- 事项ID -->
			<input type="hidden" name="status" value="<%=status%>"><!-- 状态 -->
			<input type="hidden" name="operation" value="save"><!-- 操作类型 -->
			<input type="hidden" name="fileNum" value="<%=fileNum%>"><!-- 附件数 -->
			<input type="hidden" name="noUnique"><!-- 编号唯一性标志 -->
			<input type="hidden" name="callerName" value="<%=StringUtils.getNull2String(eventInfo.getCallerName())%>"><!-- 来电人 -->
			<input type="hidden" name="address" value="<%=StringUtils.getNull2String(eventInfo.getAddress())%>"><!-- 联系地址 -->
			<input type="hidden" name="phoneNumber" value="<%=StringUtils.getNull2String(eventInfo.getPhoneNumber())%>"><!-- 工单联系电话 -->
			<input type="hidden" name="appealAddress" value="<%=StringUtils.getNull2String(eventInfo.getAppealAddress())%>"><!-- 诉求地址 -->
			<input type="hidden" name="phoneNo" value="<%=StringUtils.getNull2String(eventInfo.getPhoneNo())%>"><!-- 联系电话 -->
			<input type="hidden" name="delayresult" value=""><!-- 延迟申请审核结果 -->
			<input type="hidden" name="delayEventType" value="<%=StringUtils.getNull2String(eventInfo.getDelayEventType())%>"><!-- 延期工
			单类型 -->
			<input type="hidden" name="stExchangeName3" value="<%=StringUtils.getNull2String(eventInfo.getStExchangeName3())%>"><!--被举报单位名称-->
			<input type="hidden" name="stExchangeNam4" value="<%=StringUtils.getNull2String(eventInfo.getStExchangeName4())%>"><!--被举报单位地址-->
			<input type="hidden" name="feedbackShr" value="<%=StringUtils.getNull2String(eventInfo.getFeedbackShr())%>"><!--反馈审核人-->
			<input type="hidden" name="linkPhone" value="<%=linkPhone%>"><!-- 转发单位联系人电话 -->
			<input type="hidden" name="comefrom" value="<%=comefrom%>"><!-- 事项反馈区分 -->		
			<!-- /隐藏域 -->

			</form>
			<!-- /事项登记详情form -->

			<!-- 附件信息 -->
			<tr>
				<td class="title2" colspan="4">附件信息</td>
			</tr>
			<tr>
				<td class="ltitle">上传附件类型：</td>
				<td class="content" colspan="3">
					<input type="radio" name="filetype" value="0" <%if("0".equals(filetype)||"".equals(filetype)){out.print("checked");}%> onclick="iftuidan();"/>事项登记
					<!--<input type="radio" name="filetype" value="1" <%if("1".equals(filetype)){out.print("checked");}%> onclick="iftuidan();" />事项反馈-->
					<input type="radio" name="filetype" value="2" <%if("2".equals(filetype)){out.print("checked");}%> onclick="iftuidan();"/>退单
				</td>
			</tr>
			
			<!-- 文件上传form -->
			<%if (isShowBtn) {%>
			<form enctype="multipart/form-data" name="fileForm" method="post">
			<tr>
				<td class="ltitle">附件：</td>
				<td class="content" colspan="3">
					<input type="file" name="uploadInput" class="content" style="width:86%">
					<input type="button" class="main_button1" value="上传" onclick="forUpload()" />
				</td>
			</tr>
			</form>
			<%}%>
			<!-- /文件上传form -->

			<!-- 附件列表 -->
			<%if (!"".equals(id)) { // 非新建事项时显示附件列表%>
			<tr>
				<td class="content" colspan="4">
					<table width="100%" border="0" align="center" cellpadding="0" class="main_list_body auto_cut_table" cellspacing="0">
						<tr class="main_list_body_head_left" style="padding:0 4px;">
							<td width="12%">序号</td>
							<td width="30%">文件名</td>
							<td width="12%">附件类型</td>
							<td width="20%">上传人</td>
							<td width="15%">上传时间</td>
							<td width="11%">相关操作</td>
						</tr>
						<%
							for (int loop = 0; loop < fileList.size(); loop++) {
								ottffFileInfo = (OttffFileInfo) fileList.get(loop);
								String typeOfFj = "";
								String upFileType = ottffFileInfo.getType();
								if("0".equals(upFileType)){
									typeOfFj = "事项登记";
								} 
								if("1".equals(upFileType)){
									typeOfFj = "事项反馈";
								}
								if("2".equals(upFileType)){
									typeOfFj = "退单";
								}	
								String createUser = ottffFileInfo.getCreateUser();
								JSONObject jsonObject = JSONObject.fromObject(cfconsoleService.getUserInfo(createUser));
								String userName = (String) jsonObject.get("name");

								String rowClassName = "main_list_body_row1";
								if(loop%2==1)
									rowClassName = "main_list_body_row2";
						%>
						<tr class="<%=rowClassName%>" align="left">
							<td><%=loop + 1%></td>
							<td><%=ottffFileInfo.getFileName()%></td>
							<td><%=typeOfFj%></td>
							<td><%=StringUtils.getNull2String(userName)%></td>
							<td><%=sdf.format(ottffFileInfo.getCreateTime())%></td>
							<td>
								<a href="javascript:forDownload('<%=ottffFileInfo.getId()%>');">下载</a>
								<%if (userId.equals(ottffFileInfo.getCreateUser())) {%>
								<a href="javascript:forFileDelete('<%=ottffFileInfo.getId()%>');">删除</a>
								<%}%>
							</td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>
			<!-- /附件列表 -->
			<!-- /附件信息 -->

			<!-- 流程办理情况 -->
			<tr>
				<td class="title2" colspan="4">流程办理情况</td>
			</tr>
			<tr>
				<td class="content" colspan="4">
					<table width="100%" border="0" align="center" cellpadding="0" class="main_list_body auto_cut_table" cellspacing="0">
						<tr class="main_list_body_head_left" style="padding:0 4px;">
							<td width="25%">环节</td>
							<td width="25%">时间</td>
							<td width="25%">部门</td>
							<td width="25%">描述</td>
						</tr>
						<%if (processList != null && processList.size() > 0) {
							for (int i = 0; i < processList.size(); i++) {
								eventProcessInfo = (EventProcessInfo) processList.get(i);

								String rowClassName = "main_list_body_row1";
								if(i%2==1)
									rowClassName = "main_list_body_row2";
						%>
						<tr class="<%=rowClassName%>" align="left">
							<td><%=eventProcessInfo.getProcessStep()%></td>
							<td><%=sdf2.format(eventProcessInfo.getTime())%></td>
							<td>&nbsp;</td>
							<td>
								<%if (eventProcessInfo.getDescribe() != null 
									&& !"".equals(eventProcessInfo.getDescribe()) 
									&& !"tuidan".equals(eventProcessInfo.getDescribe()) 
									&& !"yqsq".equals(eventProcessInfo.getDescribe())) {%>
									<a href="javascript:;" id="dialogOpener" onclick="openProcessDialog('<%=eventProcessInfo.getId()%>','<%=eventProcessInfo.getDescribe()%>')">查看详情</a>
								<%}else if("tuidan".equals(eventProcessInfo.getDescribe())
									||"yqsq".equals(eventProcessInfo.getDescribe())){%>
									<a href="javascript:;" id="dialogOpener" onclick="openProcessDialog('<%=eventProcessInfo.getSubeventId()%>','<%=eventProcessInfo.getDescribe()%>')">查看详情</a>
								<%}%>
							</td>
						</tr>
						<%}
						} else {%>
						<tr class="main_list_body_row1" align="left">
							<td>事项登记</td>
							<td><%=releaseTime%></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="main_list_body_row2" align="left">
							<td>事项签收</td>
							<td><%=signForTime%></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="main_list_body_row2" align="left">
							<td>事项反馈</td>
							<td><%=feedbackTime%></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="main_list_body_row1" align="left">
							<td>事项退回</td>
							<td><%=sendBackTime%></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>
			<!-- /流程办理情况 -->
			<!--电话拨打情况有数据时显示-->
			<%if(callList.size()>0){%>
			<tr>
				<td class="title2" colspan="4">电话拨打情况</td>
			</tr>
			<tr>
				<td class="content" colspan="4">
					<table width="100%" border="0" align="center" cellpadding="0" class="main_list_body auto_cut_table" cellspacing="0">
						<tr class="main_list_body_head_left" style="padding:0 4px;">
							<td width="25%">环节</td>
							<td width="25%">时间</td>
							<td width="25%">状态</td>
							<td width="25%">拨号</td>
						</tr>
						<%if (callList != null && callList.size() > 0) {
						EventCallInfo eventCallInfo = new EventCallInfo();
							for (int i = 0; i < callList.size(); i++) {
								eventCallInfo = (EventCallInfo) callList.get(i);

								String rowClassName = "main_list_body_row1";
								if(i%2==1)
									rowClassName = "main_list_body_row2";
						%>
						<tr class="<%=rowClassName%>" align="left">
							<td><%=eventCallInfo.getStType()%></td>
							<td><%=sdf2.format(eventCallInfo.getCreateTime())%></td>
							<td>&nbsp;</td>
							<%if(eventCallInfo.getBak1()!=null && !"".equals(eventCallInfo.getBak1())){%>
							<td>
							<input type="button" value ="听录音" onclick="listen('<%=eventCallInfo.getBak1()%>')"/>
								
							</td>
							<%}else{%>
								<td></td>
							<%}%>
						</tr>
						<%
						
							}
						}%> 
					</table>
				</td>
			</tr>
			<%}%>
			<%}%>
		</table></td>
	</tr>

	<!-- 按钮 -->
	<%if ("".equals(from)) {%>
	<tr>
		<td colspan="2"><table border="0" cellspacing="5" align="center">
			<tr>
				<%@ include file="/common/hbj_button_init.inc"%>
				<%if (isShowBtn) {%>
				<%if (ottffConst.isStringContains("0,17",status)) { // 状态为“暂存”%>
				<td>
					<%
					button_text = "暂 存";
					button_name = "saveBtn";
					button_url = "forSave('save')";
					button_title = "事项暂存";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>

				<%}%>
				<%if (ottffConst.isStringContains("2,19,20,8",status)) { // 状态为“已反馈”时显示【暂存】按钮 辖区%>
				<td>
					<%
					button_text = "更 新";
					button_name = "updateBtn";
					button_url = "updateItem('update')";
					button_title = "事项更新";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<%
					button_text = "转发区县";
					button_name = "releaseBtn";
					button_url = "forSave('release')";
					button_title = "事项提交";
					button_width = 67;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("1".equals(status)) { // 状态为“未签收”时显示【更新】按钮%>
				<td>
					<%
					button_text = "更 新";
					button_name = "updateBtn";
					button_url = "forSave('update')";
					button_title = "事项更新";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("0,17",status)&&("".equals(StringUtils.getNull2String(eventInfo.getReturnVisit()))||"N".equals(callbackFlag))) { // 状态为“暂存”、“已反馈”、“退回12345”,”待登记“时显示【提交】按钮%>
				<td>
					<%
					button_text = "提 交";
					button_name = "releaseBtn";
					button_url = "forSave('daidengji')";
					button_title = "事项提交";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("2,8",status)) { // 状态为“已反馈”、“回访复核已反馈”时显示【反馈12345】按钮%>
				<%if(ottffConst.isStringContains("2",status)){%>
				<td>
					<%	
					button_text = "待审核";
					button_name = "forWaitAuditBtn";
					button_url = "forWaitAudit('21','waitAudit')";
					button_title = "待审核";
					button_width = 67;
					%>
					
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if(ottffConst.isStringContains("8",status)){%>
				<td>
					<%	
					button_text = "待审核（回访复核）";
					button_name = "forReturnWaitAuditBtn";
					button_url = "forWaitAudit('22','returnwaitAudit')";
					button_title = "待审核（回访复核）";
					button_width = 130;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if(ottffConst.isStringContains("2",status)){%>
				<td>
					<%	
					button_text = "反馈12345";
					button_name = "feedback12345Btn";
					button_url = "forFeedback12345('2')";
					button_title = "反馈12345";
					button_width = 77;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<%}%>
				<%if(ottffConst.isStringContains("8",status)){%>
				<td>
					<%	
					button_text = "反馈12345";
					button_name = "feedback12345Btn";
					button_url = "forFeedback12345('8')";
					button_title = "反馈12345";
					button_width = 77;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<%}%>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("2,21",status)) { // 状态为“已反馈”时显示【退回】按钮%>
				<td>
					<%	
					button_text = "退至原区县";
					button_name = "sendBackBtn";
					button_url = "forSendBack('3')";
					button_title = "事项退回";
					button_width = 87;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("Y".equals(callbackFlag)&&"17".equals(status)||("old".equals(data)&&"5".equals(status))||(ottffConst.isStringContains("0,17",status)&& !"".equals(StringUtils.getNull2String(eventInfo.getReturnVisit())))) { // 状态为“已反馈12345”时显示【回访复核】按钮%>
				<td>	
					<%
					button_text = "回访复核登记";
					button_name = "returnVisitBtn";
					button_url = "forReturnVisit('returnvisit')";
					button_title = "回访复核登记";
					button_width = 107;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("6".equals(status)) { // 状态为“回访复核待签收”时显示【更新】按钮%>
				<td>	
					<%
					button_text = "更 新";
					button_name = "updateBtn";
					button_url = "forReturnVisit('update2')";
					button_title = "回访复核更新";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("8,22",status)) { // 状态为“回访复核已反馈”时显示【退回】按钮%>
				<td>
					<%	
					button_text = "退至原区县";
					button_name = "sendBackBtn";
					button_url = "forSendBack('10')";
					button_title = "回访复核退回";
					button_width = 87;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("9".equals(status)) { // 状态为“延期申请待审核”时显示【审核通过】【驳回】按钮%>
				<td>	
					<%
					button_text = "审核通过";
					button_name = "agreeBtn";
					button_url = "forDelayAudit('1')";
					button_title = "延期申请审核通过";
					button_width = 67;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>	
					<%
					button_text = "驳 回";
					button_name = "noagreeBtn";
					button_url = "forDelayAudit('0')";
					button_title = "延期申请驳回";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("1".equals(eventInfo.getDelayAgree())) { // 通过延期申请审核时显示【打印延期申请】按钮%>
				<td>	
					<%
					button_text = "打印延期申请单";
					button_name = "printDelayBtn";
					button_url = "forPrintDelay()";
					button_title = "打印延期申请单";
					button_width = 107;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("12",status)) { // 状态为“未先行联系(暂存)”%>
				<td>
					<%
					button_text = "暂存";
					button_name = "zancunfirstcontactBtn";
					button_url = "forSave('zancunfirstcontact')";
					button_title = "暂存(未先行联系)";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("12",status)) { // 状态为“先行联系”%>
				<td>
					<%
					button_text = "提 交";
					button_name = "firstcontactBtn";
					button_url = "forSave('firstcontact')";
					button_title = "先行联系";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("0,1,2,4",status)&&"old".equals(data)) { // 状态为“暂存” 未签收 已签收 已反馈%>
				<td>
					<%	
					button_text = "退回12345";
					button_name = "sendBack12345Btn";
					button_url = "forSave('sendback12345')";
					button_title = "退回12345";
					button_width = 77;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("13",status)) { // 状态为“先行联系(回访复核)”%>
				<td>
					<%
					button_text = "暂 存";
					button_name = "updateBtn";
					button_url = "updateItem('update2')";
					button_title = "事项更新";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<%
					button_text = "提 交";
					button_name = "firstcontactBtn";
					button_url = "forSave('returnfirstcontact')";
					button_title = "先行联系(回访复核)";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("12",status)&&oldDate.compareTo(sdf.parse(sdf.format(eventInfo.getCreateTime())))<0) { // 状态为“退单” “延期申请”%>
				<td>
					<%
					button_text = "退 单";
					button_name = "tuidanBtn";
					button_url = "fortuidan()";
					button_title = "退单申请";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("12,13",status)&&oldDate.compareTo(sdf.parse(sdf.format(eventInfo.getCreateTime())))<0) { // 状态为“退单” “延期申请”%>
				<td>
					<%
					button_text = "延期申请";
					button_name = "yqsqBtn";
					button_url = "foryqsq()";
					button_title = "延期申请";
					button_width = 67;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (ottffConst.isStringContains("21,22",status)) { // 状态为“回访复核已反馈”时显示【退回】按钮%>
				<td>
					<%
					button_text = "更 新";
					button_name = "updateBtn";
					button_url = "updateItem('update')";
					button_title = "事项更新";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<%	
					button_text = "反馈12345";
					button_name = "feedback12345Btn";
					button_url = "forFeedback12345()";
					button_title = "反馈12345";
					button_width = 77;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if ("5".equals(status)&&"".equals(yjjbStatus)){%>
				<td>	
					<%
					button_text = "有奖举报";
					button_name = "yjjbBtn";
					button_url = "foryjjb()";
					button_title = "有奖举报";
					button_width = 67;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (!"".equals(yjjbStatus)) { // 状态不为“暂存”时显示【打印】按钮%>
				<td>	
					<%
					button_text = "有奖举报打印";
					button_name = "printBtn";
					button_url = "foryjjbPrint()";
					button_title = "有奖举报打印";
					button_width = 107;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%if (!"0".equals(status)) { // 状态不为“暂存”时显示【打印】按钮%>
				<td>	
					<%
					button_text = "打 印";
					button_name = "printBtn";
					button_url = "forPrint()";
					button_title = "打印";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<%}%>
				<td>
					<%
					button_text = "返 回";
					button_name = "closeBtn";
					button_url = "forBack()";
					button_title = "返回列表";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
			</tr>
		</table></td>
	</tr>
	<%}%>
	<!-- /按钮 -->
	<!-- /登记信息Tab -->

	<!-- 满意度调查Tab -->
	<%} else if ("2".equals(tab)) {%>
	<tr>
		<td class="main_edit_title">&nbsp;满意度调查</td>
	</tr>
	<tr>
		<td class="main_edit_body_wrap"><table width="100%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #abcdf0; border-collapse:collapse;">
			<form name="form2" method="post" action="event_bg.jsp">
			<tr>
				<td width="18%" class="ltitle"><span style="color: red">*</span>&nbsp;是否打通电话：</td>
				<td class="content" colspan="3">
					<input type="radio" name="satisfactionCalledRadio" value="1" onclick="onSatisfactionRdoChange('Called')" <%if ("1".equals(satisfactionCalled)) {%>checked<%}%>>是
					<input type="radio" name="satisfactionCalledRadio" value="0" onclick="onSatisfactionRdoChange('Called')" <%if ("0".equals(satisfactionCalled)) {%>checked<%}%>>否
					<input type="hidden" name="satisfactionCalled" value="<%=satisfactionCalled%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle"><span style="color: red">*</span>&nbsp;调查时间：</td>
				<td width="32%" class="content"><input type="text" name="satisfactionTime" class="Wdate main_input12" maxlength="10" value="<%=satisfactionTime%>" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" title="输入的格式例如：2010-05-01。可以通过时间控件来输入，也可以手工输入"></td>
				<td width="18%" class="ltitle"><span style="color: red">*</span>&nbsp;回访人：</td>
				<td width="32%" class="content"><input type="text" name="satisfactionName" class="main_input12" maxlength="50" value="<%=StringUtils.getNull2String(eventInfo.getSatisfactionName())%>"></td>
			</tr>
			<tr>
				<td class="ltitle" rowspan="4"><span style="color: red">*</span>&nbsp;评价：</td>
				<td class="content">拒绝调查</td>
				<td class="content" colspan="2">
					<input type="radio" name="satisfactionRefuseRadio" value="1" onclick="onSatisfactionRdoChange('Refuse')" <%if ("1".equals(satisfactionRefuse)) {%>checked<%}%>>是
					<input type="radio" name="satisfactionRefuseRadio" value="0" onclick="onSatisfactionRdoChange('Refuse')" <%if ("0".equals(satisfactionRefuse)) {%>checked<%}%>>否
					<input type="hidden" name="satisfactionRefuse" value="<%=satisfactionRefuse%>">
				</td>
				
			</tr>
			<tr>
				<td class="content">工作人员是否先行联系</td>
				<td class="content" colspan="2">
					<input type="radio" name="satisfactionManRadio" value="1" onclick="onSatisfactionRdoChange('Man')" <%if ("1".equals(satisfactionMan)) {%>checked<%}%>>是
					<input type="radio" name="satisfactionManRadio" value="0" onclick="onSatisfactionRdoChange('Man')" <%if ("0".equals(satisfactionMan)) {%>checked<%}%>>否
					<input type="hidden" name="satisfactionMan" value="<%=satisfactionMan%>">
				</td>
			</tr>
			<tr>
				<td class="content">对处理结果是否满意</td>
				<td class="content" colspan="2">
					<input type="radio" name="satisfactionResultRadio" value="1" onclick="onSatisfactionRdoChange('Result')" <%if ("1".equals(satisfactionResult)) {%>checked<%}%>>满意
					<input type="radio" name="satisfactionResultRadio" value="0" onclick="onSatisfactionRdoChange('Result')" <%if ("0".equals(satisfactionResult)) {%>checked<%}%>>不满意
					<!--<input type="radio" name="satisfactionResultRadio" value="2" onclick="onSatisfactionRdoChange('Result')" <%if ("2".equals(satisfactionResult)) {%>checked<%}%>>不评价-->
					<input type="radio" name="satisfactionResultRadio" value="3" onclick="onSatisfactionRdoChange('Result')" <%if ("3".equals(satisfactionResult)) {%>checked<%}%>>基本满意
					<input type="radio" name="satisfactionResultRadio" value="4" onclick="onSatisfactionRdoChange('Result')" <%if ("4".equals(satisfactionResult)) {%>checked<%}%>>一般
					<input type="hidden" name="satisfactionResult" value="<%=satisfactionResult%>">
				</td>
			</tr>
			<tr>
				<td class="content">对工作态度是否满意</td>
				<td class="content" colspan="2">
					<input type="radio" name="satisfactionAttitudeRadio" value="1" onclick="onSatisfactionRdoChange('Attitude')" <%if ("1".equals(satisfactionAttitude)) {%>checked<%}%>>满意
					<input type="radio" name="satisfactionAttitudeRadio" value="0" onclick="onSatisfactionRdoChange('Attitude')" <%if ("0".equals(satisfactionAttitude)) {%>checked<%}%>>不满意
					<!-- <input type="radio" name="satisfactionAttitudeRadio" value="2" onclick="onSatisfactionRdoChange('Attitude')" <%if ("2".equals(satisfactionAttitude)) {%>checked<%}%>>不评价 -->
					<input type="radio" name="satisfactionAttitudeRadio" value="3" onclick="onSatisfactionRdoChange('Attitude')" <%if ("3".equals(satisfactionAttitude)) {%>checked<%}%>>基本满意
					<input type="radio" name="satisfactionAttitudeRadio" value="4" onclick="onSatisfactionRdoChange('Attitude')" <%if ("4".equals(satisfactionAttitude)) {%>checked<%}%>>一般
					<input type="hidden" name="satisfactionAttitude" value="<%=satisfactionAttitude%>">
				</td>
			</tr>
			<tr>
				<td class="ltitle">备注：</td>
				<td class="content" colspan="3"><textarea name="satisfactionComment" rows="4" style="width:100%"><%=StringUtils.getNull2String(eventInfo.getSatisfactionComment())%></textarea></td>
			</tr>
			<input type="hidden" name="id" value="<%=id%>">
			<input type="hidden" name="operation" value="satisfaction">
			</form>
		</table></td>
	</tr>

	<!-- 按钮 -->
	<%if ("".equals(from)) {%>
	<tr>
		<td><table border="0" cellspacing="5" align="center">
			<tr>
				<%@ include file="/common/hbj_button_init.inc"%>
				<%if (isShowBtn) {%>
				<td>
					<%	
					button_text = "保 存";
					button_name = "saveBtn";
					button_url = "forSatisfaction()";
					button_title = "保存";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<%}%>
				<td>
					<%	
					button_text = "返 回";
					button_name = "closeBtn";
					button_url = "forBack()";
					button_title = "返回列表";
					button_width = 47;
					%>
					<%@ include file="/common/hbj_button_table.inc"%>
				</td>
			</tr>
		</table></td>
	</tr>
	<%}%>
	<!-- /按钮 -->
	<!-- /满意度调查Tab -->

	<!-- 催单Tab -->
	<%}else if("3".equals(tab)){%>
	<tr>
		<td class="main_edit_title">&nbsp;催单</td>
	</tr>
	<tr>
		<td class="main_edit_body_wrap">
			<iframe name="reminder"  src="event_reminder_list.jsp?id=<%=id%>" width="100%" height="300px" frameborder="0" scrolling="no"></iframe>
		</td>
	</tr>
	<%}%>
	<!-- /催单Tab -->

</table>
</div>

<!-- 上传附件用iframe -->
<iframe name="attributeSave" style="display:none"></iframe>
<!-- /上传附件用iframe -->

<!-- 流程详情对话框 -->
<div id="dialog" title="查看详情" style="display:none">
	<iframe name="dialogFrame" id="dialogFrame" src="" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>
</div>
<!-- /流程详情对话框 -->

<!-- 短信发送对话框 -->
<div id="sendMsgDialog" title="短信内容详情" style="display:none">
	<iframe name="sendMsgDialogFrame" id="sendMsgDialogFrame" src="" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>
</div>
<!-- /流程详情对话框 -->

</body>
</html>
<script type="text/javascript">
//拨打电话
function directcall(){
	var phoneNumberInput = document.form1.phoneNumberInput.value;
	if(phoneNumberInput !=""){
		eventService.directcall(phoneNumberInput, callBackdirectcall);
	}else{
		alert("联系电话为空，无法拨打电话...");
	}
	
}
function callBackdirectcall(result){
	//alert(result);
	if(result !=""){//可能的调用接口结果
		document.myForm.action="event_call_bg.jsp";
		document.myForm.result.value= result;
		document.myForm.operation.value ="byPhoneNumber";
		document.myForm.target = "_self";
		document.myForm.submit();
		//alert(str1+"=="+str2+"=="+str3);
		

	}
}
function listen(params){
	eventService.listen(params,callBackListen);
}
function callBackListen(result){

	//window.showModalDialog(result,'','dialogWidth:800px; dialogHeight:400px; status:no;directories:yes;scrollbars:no;resizable:no;help:no');
	
	window.open(result,"_self");
	
}
$("#myFirstContact").bind("change",function(){
         var firstContact = document.form1.firstContact;// 先行联系
		 if(firstContact.value =="已联"){
			document.form1.nocontactReason.value ="";//未联值置空
			$("#nocontactReason").attr("disabled",true);
		 }else{
			$("#nocontactReason").attr("disabled",false);
		 }
            
        });
$(function(){
	var alreadyTuidanNum = "<%=alreadyTuidanNum%>";
	if(alreadyTuidanNum >0){
		alert("已有相同工单编号退单，请留意！");
	}
	// 流程详情对话框
	$("#dialog").dialog({
		autoOpen: false,
		height: 400,
		width: 600,
		resizable: false,
		buttons: {
			"关闭": function(){
				//关闭窗口
				$(this).dialog("close");
			}
		},
		close: function(event, ui) {//为dialog窗口关闭事件绑定刷新内部iframe的函数
			document.frames["dialogFrame"].location.href = "about:blank";
		}
	});

	// 选择行业分类对话框
	$("#hyflDialog").dialog({
		autoOpen: false,
		height: 400,
		width: 600,
		resizable: false,
		buttons: { 
			"确定": function(){
				//执行iframe中的ok方法
				document.frames["hyflDialogFrame"].window.ok();
				
				//关闭窗口
				$(this).dialog("close");
			},
			"取消": function(){
				//关闭窗口
				$(this).dialog("close");
			}
		},
		close: function(event, ui) {//为dialog窗口关闭事件绑定刷新内部iframe的函数
			document.frames["hyflDialogFrame"].location.href = "about:blank";
		}
	});

	$("#hyflDialogOpener").click(function() {
		if(document.frames["hyflDialogFrame"].location.href == "" 
				|| document.frames["hyflDialogFrame"].location.href == "about:blank"){ // 如果是首次打开窗口，需要设置iframe的src
			document.frames["hyflDialogFrame"].location.href = 
				"<%=request.getContextPath()%>/general/common/ottffHyflEUTree.jsp?checkedHyfl=" + document.form1.stExchangeName1.value + "&callbackFunc=selectHYLB";
		}
		$("#hyflDialog").dialog("open");
	});

	// 短信发送内容对话框
	$("#sendMsgDialog").dialog({
		autoOpen: false,
		height: 400,
		width: 600,
		resizable: false,
		buttons: { 
			"确定": function(){
				var phoneNumberInput = document.form1.phoneNumber;
				//执行iframe中的ok方法
				document.frames["sendMsgDialogFrame"].window.ok();		
				//关闭窗口
				$(this).dialog("close");
			},
			"取消": function(){
				//关闭窗口
				$(this).dialog("close");
			}
		},
		close: function(event, ui) {//为dialog窗口关闭事件绑定刷新内部iframe的函数
			document.frames["sendMsgDialogFrame"].location.href = "about:blank";
		}
	});
});

// 打开发送短信内容对话框
function opensendMsgDialog(id) {
	var phoneNumber = document.form1.phoneNumber;
	if (phoneNumber.value == "" || phoneNumber.value == "无") {
		alert("无工单联系人电话，无法发送短信。");
	} else {
		if(document.frames["sendMsgDialogFrame"].location.href == "" 
			|| document.frames["sendMsgDialogFrame"].location.href == "about:blank"){ // 如果是首次打开窗口，需要设置iframe的src
			document.frames["sendMsgDialogFrame"].location.href = "event_sendMsgPage.jsp?history=no&id="+id;
		}
		$("#sendMsgDialog").dialog("open");
	}
}

// 打开流程详情对话框
function openProcessDialog(processId,describe) {
	if(document.frames["dialogFrame"].location.href == "" 
		|| document.frames["dialogFrame"].location.href == "about:blank"){ // 如果是首次打开窗口，需要设置iframe的src
		document.frames["dialogFrame"].location.href = "event_process.jsp?processId=" + processId + "&describe=" + describe;
	}
	$("#dialog").dialog("open");
}

// 页面检查
function check() {
	var no = document.form1.no;
	var name = document.form1.name;
	var originDepartment = document.form1.originDepartment;
	var forwardDepartment = document.form1.forwardDepartment;
	var reportType = document.form1.reportType;
	var classification = document.form1.classification;
	var subclassification = document.form1.subclassification;
	var callerName = document.form1.callerName;
	var callerNameInput = document.form1.callerNameInput;
	var callTime = document.form1.callTime;
	var address = document.form1.address;
	var addressInput = document.form1.addressInput;
	var phoneNumber = document.form1.phoneNumber;

	var phoneNumberInput = document.form1.phoneNumberInput;
	var appealAddress = document.form1.appealAddress;
	var appealAddressInput = document.form1.appealAddressInput;
	var phoneNo = document.form1.phoneNo;
	var phoneNoInput = document.form1.phoneNoInput;
	var content = document.form1.content;
	var feedbackLimitTime = document.form1.feedbackLimitTime;
	var registerName = document.form1.registerName;
	var needAttach = document.form1.needAttach;
	var fileNum = document.form1.fileNum;
    var status=document.form1.status.value;
	var level2Charger=document.form1.level2Charger;
	var stExchangeName2=document.form1.stExchangeName2;

	/**	if (no.value == "")
	{
		alert("请填写事项编号。");
		no.focus();
		return false;
	}
	else
	{
		if (document.form1.noUnique.value > 0)
		{
			alert("该事项编号已被使用。");
			no.focus();
			return false;
		}
	}**/

	if (name.value == "")
	{
		alert("请填写事项名称。");
		name.focus();
		return false;
	}

	/*if (originDepartment.value == "")
	{
		alert("请选择来源单位。");
		originDepartment.focus();
		return false;
	}**/

	if (forwardDepartment.value == ""&&(status=="12"||status=="13"))
	{
		alert("请选择转发单位。");
		forwardDepartment.focus();
		return false;
	}
	if (reportType.value == "")
	{
		alert("请选择举报性质。");
		document.form1.reportTypeRadio[0].focus();
		return false;
	}
    if (name.value == "")
	{
		alert("请填写事项名称。");
		name.focus();
		return false;
	}
	
	if (classification.length > 1 && classification.value == "")
	{
		alert("请选择污染分类。");
		classification.focus();
		return false;
	}

	if (subclassification.length > 1 && subclassification.value == "")
	{
		alert("请选择子分类。");
		subclassification.focus();
		return false;
	}

	/**if (callerName.value == "")
	{
		alert("请填写来电人。");
		callerNameInput.focus();
		return false;
	}

	if (callTime.value == "")
	{
		alert("请填写来电日期。");
		callTime.focus();
		return false;
	}

	if (address.value == "")
	{
		alert("请填写联系地址。");
		addressInput.focus();
		return false;
	}

	if (phoneNumber.value == "")
	{
		alert("请填写工单联系电话。");
		phoneNumberInput.focus();
		return false;
	}

	if (appealAddress.value == "")
	{
		alert("请填写诉求地址。");
		appealAddressInput.focus();
		return false;
	}

	if (phoneNo.value == "")
	{
		alert("请填写联系电话。");
		phoneNoInput.focus();
		return false;
	}

	if (content.value == "")
	{
		alert("请填写内容描述。");
		content.focus();
		return false;
	}**/

	if (feedbackLimitTime.value == "")
	{
		alert("请填写反馈时间");
		feedbackLimitTime.focus();
		return false;
	}

	if (registerName.value == "" )
	{
		alert("请填写登记人");
		registerName.focus();
		return false;
	}

	if (needAttach.value == "")
	{
		alert("请选择是否上传反馈附件。");
		document.form1.needAttachRadio[0].focus();
		return false;
	}

	if(level2Charger.value==""){
	    alert("请填写负责人");
		level2Charger.focus();
		return false;
	}

	//先行联系 验证
    /*if(status=="12"){
		var firstContact = document.form1.firstContact;// 先行联系
		var nocontactReason = document.form1.nocontactReason;// 未联原因
		var contactMan = document.form1.contactMan;// 先行联系人
		var contactTime = document.form1.contactTime;// 联系时间
		var contactContent = document.form1.contactContent;// 联系内容
		var stExchangeName5 = document.form1.stExchangeName5;// 联系内容备注
		
		if(phoneNumber.value!=="无"||phoneNumber.value == null){
			if (firstContact.value == "")
			{
				alert("请选择先行联系。");
				firstContact.focus();
				return false;
			}
			else
			{
				// 先行联系选择未联时，未联原因为必填项
				if (firstContact.value == "未联" && nocontactReason.value == "")
				{
					alert("请选择未联原因。");
					nocontactReason.focus();
					return false;
				}
			}

			if(firstContact.value=="已联"){
				if (contactMan.value == "")
				{
					alert("请填写先行联系人。");
					contactMan.focus();
					return false;
				}

				if (contactTime.value == "")
				{
					alert("请填写联系时间。");
					contactTime.focus();
					return false;
				}

				if (contactContent.value == "")
				{
					alert("请填写联系内容。");
					contactContent.focus();
					return false;
				}

				if (stExchangeName5.value == "")
				{
					alert("请填写联系内容备注。");
					stExchangeName5.focus();
					return false;
				}
			}
		}
	}*/

	/*if(status=="13"){
		var stReturnVisitFirstContact = document.form1.stReturnVisitFirstContact;// 回访复核先行联系
		var stReturnNocontactReason = document.form1.stReturnNocontactReason;// 回访复核未联原因
		var stReturnVisitContactMan = document.form1.stReturnVisitContactMan;// 回访复核先行联系人
		var dtReturnVisitContactTime = document.form1.dtReturnVisitContactTime;// 回访复核联系时间
		var stReturnContactContent = document.form1.stReturnContactContent;// 回访复核联系内容
		var remarkContactContent = document.form1.remarkContactContent;// 回访复核联系内容备注

		if(phoneNumber.value!=="无"||phoneNumber.value == null){
			if (stReturnVisitFirstContact.value == "")
			{
				alert("请选择回访复核先行联系。");
				stReturnVisitFirstContact.focus();
				return false;
			}
			else
			{
				// 先行联系选择未联时，未联原因为必填项
				if (stReturnVisitFirstContact.value == "未联" && stReturnNocontactReason.value == "")
				{
					alert("请选择回访复核未联原因。");
					stReturnNocontactReason.focus();
					return false;
				}
			}

			if(stReturnVisitFirstContact.value=="已联"){
				if (stReturnVisitContactMan.value == "")
				{
					alert("请填写回访复核先行联系人。");
					stReturnVisitContactMan.focus();
					return false;
				}

				if (dtReturnVisitContactTime.value == "")
				{
					alert("请填写回访复核联系时间。");
					dtReturnVisitContactTime.focus();
					return false;
				}

				if (stReturnContactContent.value == "")
				{
					alert("请填写回访复核联系内容。");
					stReturnContactContent.focus();
					return false;
				}

				if (remarkContactContent.value == "")
				{
					alert("请填写回访复核联系内容备注。");
					remarkContactContent.focus();
					return false;
				}
			}
		}
	}*/

	return true;
}

// 检查事项编号唯一性
function readonlyNoUnique(no) {
	var id = document.form1.id.value;
	eventService.readonlyNoUniqueAjax(no.value, id, callBackreadonlyNoUnique);
}

// 检查事项编号唯一性回调函数
function callBackreadonlyNoUnique(datas) {
	document.form1.noUnique.value = datas;
	if (datas > 0)
	{
		alert("该事项编号已被使用。");
		document.form1.no.focus();
	}
}

// 保存
function forSave(flag) { 
	//alert(flag);
	onCallerNameChange();
	onAddressChange();
	onPhoneNumberChange();
	onAppealAddressChange();
	//onPhoneNoChange();
	var status = "";
    //alert(flag);
	var forwardDepartment = document.form1.forwardDepartment;
	if(flag=="firstcontact"&&forwardDepartment.value=="402883e73ab61cb7013ab986dcf80001"){
		alert("请申请退单。");
		return false;
	}
	if (flag == "save"||flag=="zancunfirstcontact") { // 暂存
		if (document.form1.noUnique.value > 0) {
			alert("该事项编号已被使用。");
			document.form1.no.focus();
			return;
		}
		if(flag=="zancunfirstcontact"){ //未先行联系的暂存
			status="12";
		}else{
			status = "17";
		}
		if(flag == "save"){
			status="0";
		}
	} else { // 发布、更新、退回12345
		if (flag == "sendback12345") { // 退回12345
			if (!confirm("确定要退回12345？")) {
				return;
			}

			status = "11";
		} else { // 发布、更新
			// 已反馈事项转发前要确认 辖区转发事项
			if ("<%=status%>" == "2"||"<%=status%>" == "19"||"<%=status%>" == "20"||"<%=status%>" == "8") {
				if (!confirm("确定要转发事项？")) {
					return;
				}
			}
			if("<%=status%>" == "8"||"<%=status%>" == "20"){
				status = "6";//待签收（回访复核）
			}else{
				status = "1";//未签收
			}
		}

		if (!check()){
			return;
		}
	}

	//提交之后 是未先行联系状态 事项登记环节
	if(flag=="daidengji"){
	  status="12";
	}

	if(flag=="returnfirstcontact"){//回访复核先行联系
	  status="6";//回访复核待签收
	}
	
	document.form1.operation.value = flag;
	document.form1.status.value = status;
	document.form1.forwardDepartment.value = forwardDepartment.value;
	document.form1.submit();
}

function  updateItem(flag){
    onCallerNameChange();
	onAddressChange();
	onPhoneNumberChange();
	onAppealAddressChange();
	document.form1.operation.value = flag;
	document.form1.submit();
}

//待审核
function forWaitAudit(status,operation){
	if (confirm("是否确认送审核？"))
	{
		document.form1.operation.value = operation;
		document.form1.status.value = status;
		document.form1.submit();
	}
}

// 反馈12345
function forFeedback12345(status) {
	if(status=='2'){
		//var feedbackShr = document.form1.feedbackShr;
		var feedbackShr = document.getElementById("feedbackShr");
		if (feedbackShr.value == "")
		{
			alert("请填写反馈审核人。");
			feedbackShr.focus();
			return;
		}
	}
	if(status=='8'){
		//回访复核反馈审核人验证
		var feedbackShr2 = document.form1.feedbackShr2;
		if (feedbackShr2.value == "")
		{
			alert("请填写反馈审核人。");
			feedbackShr2.focus();
			return;
		}
	}
	document.getElementById("feedback12345Btn").disabled=true;
	
	if (confirm("确定要反馈12345？"))
	{
		document.form1.operation.value = "feedback12345";
		document.form1.status.value = "5";
		document.form1.submit();
	}
}

// 退回12345
function forSendback12345() {
	if (confirm("确定要退回12345？"))
	{
		document.form1.operation.value = "sendback12345";
		document.form1.status.value = "11";
		document.form1.submit();
	}
}

// 退回
function forSendBack(status) {
	var sendBack = document.form1.sendBack;
	var backPerson = document.form1.backPerson;

	if (sendBack.value == "")
	{
		alert("请填写退回理由。");
		sendBack.focus();
		return;
	}
	if (backPerson.value == "")
	{
		alert("请填写退回申请人。");
		backPerson.focus();
		return;
	}

	document.form1.operation.value = "sendBack";
	document.form1.status.value = status;
	document.form1.submit();
}

// 回访复核
function forReturnVisit(flag) {
	var returnVisitContent = document.form1.returnVisitContent;
	var returnVisitLimitTime = document.form1.returnVisitLimitTime;
	var returnVisitRegisterName = document.form1.returnVisitRegisterName;
	if (returnVisitContent.value == "")
	{
		alert("请输入回访复核内容描述。");
		returnVisitContent.focus();
		return;
	}

	if (returnVisitLimitTime.value == "")
	{
		alert("请输入回访复核应反时间。");
		returnVisitLimitTime.focus();
		return;
	}

	if (returnVisitRegisterName.value == "")
	{
		alert("请输入回访复核登记人。");
		returnVisitRegisterName.focus();
		return;
	}
  
	document.form1.operation.value = flag;
	//alert(flag);
	if(flag=="returnfirstcontact"){
		document.form1.status.value = "13";
    }
	if(flag=="returnvisit"){
		document.form1.status.value = "13";
	}
    document.form1.submit();
} 

// 延期申请审核
function forDelayAudit(result) {
	// 驳回时要输入驳回理由
	if (result == "0")
	{
		var delayRejectReason = document.form1.delayRejectReason;
		if (delayRejectReason.value == "")
		{
			alert("请填写驳回理由");
			delayRejectReason.focus();
			return;
		}
	}
	else
	{
		var delayZdJbrName = document.form1.delayZdJbrName;
		var delayZdFzrName = document.form1.delayZdFzrName;

		if (delayZdJbrName.value == "")
		{
			alert("请填写经办人。");
			delayZdJbrName.focus();
			return;
		}

		if (delayZdFzrName.value == "")
		{
			alert("请填写负责人。");
			delayZdFzrName.focus();
			return;
		}
	}

	document.form1.delayresult.value = result;
	document.form1.operation.value = "delayaudit";
	if (document.form1.delayEventType.value == "1") { // 普通工单状态变更为“已签收”
		document.form1.status.value = "4";
	} else if (document.form1.delayEventType.value == "2") { // 回访复核单状态变更为“回访复核已签收”
		document.form1.status.value = "7";
	}
	
	document.form1.submit();
}

// 满意度调查
function forSatisfaction() {
	// 是否打通电话
	var satisfactionCalled = document.form2.satisfactionCalled;
	// 调查时间
	var satisfactionTime = document.form2.satisfactionTime;
	// 回访人
	var satisfactionName = document.form2.satisfactionName;
	// 拒绝调查
	var satisfactionRefuse = document.form2.satisfactionRefuse;
	// 工作人员是否先行联系
	var satisfactionMan = document.form2.satisfactionMan;
	// 对处理结果是否满意
	var satisfactionResult = document.form2.satisfactionResult;
	// 对工作态度是否满意
	var satisfactionAttitude = document.form2.satisfactionAttitude;

	if (satisfactionCalled.value == "")
	{
		alert("请选择是否打通电话。");
		document.form2.satisfactionCalledRadio[0].focus();
		return;
	}

	if (satisfactionTime.value == "")
	{
		alert("请填写调查时间。");
		satisfactionTime.focus();
		return;
	}

	if (satisfactionName.value == "")
	{
		alert("请填写回访人。");
		satisfactionName.focus();
		return;
	}

	if (satisfactionCalled.value == "1") {
		if (satisfactionRefuse.value == "") {
			alert("请填写拒绝调查。");
			document.form2.satisfactionRefuseRadio[0].focus();
			return;
		}
		
		if (satisfactionRefuse.value != "1") {
			if (satisfactionMan.value == "")
			{
				alert("请选择工作人员是否沟通。");
				document.form2.satisfactionManRadio[0].focus();
				return;
			}

			if (satisfactionResult.value == "")
			{
				alert("请选择对处理结果是否满意。");
				document.form2.satisfactionResultRadio[0].focus();
				return;
			}

			if (satisfactionAttitude.value == "")
			{
				alert("请选择对工作态度是否满意。");
				document.form2.satisfactionAttitudeRadio[0].focus();
				return;
			}
		}
	}
	
	document.form2.submit();
}

// 打印延期申请单
function forPrintDelay() {
	var url = "event_delay_print.jsp?id=<%=id%>";
	var printWindow = window.open(
		url,
		"printCase",
		"height=640, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
	printWindow.print();
}

// 打印
function forPrint() {
	var url = "event_print.jsp?id=<%=id%>";
	var printWindow = window.open(
		url,
		"printCase",
		"height=640, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
	printWindow.print();
}

// 返回
function forBack() {
	window.location.href = "event_list.jsp?history=yes";
}

// 上传文件
function forUpload() {
    var filetypes=document.getElementsByName("filetype");
	var filetype="";
	for(var i=0;i<filetypes.length;i++){
		if(filetypes[i].checked==true){
			filetype=filetypes[i].value;
			break;
		}
	}
	//alert(filetype);
	var tuidanfilesize="<%=tuidanfilesize%>";
	//alert(tuidanfilesize);
 	document.form1.operation.value = "upload";
	if(filetype == "2"){
		if(tuidanfilesize=="3"){
			alert("退单附件最多只能上传3个");
			return false;
		}
		document.form1.action="<%=basePath%>ottff/tuidan/event_tuidan_bg.jsp?id=<%=id%>&ottfftuidanID=<%=ottfftuidanID%>&type="+filetype;
	}else{
		document.form1.action="event_bg.jsp";
	}
	document.form1.target = "attributeSave";
	document.form1.submit();
}

// 下载文件
function forDownload(fileId) {
	document.form1.operation.value = "download";
	document.form1.action="event_bg.jsp?fileId=" + fileId;
	document.form1.target = "_self";
	document.form1.submit();
}

// 删除文件
function forFileDelete(fileId) {
	if (confirm("确定要删除附件吗？"))
	{
		document.form1.operation.value = "filedelete";
		document.form1.action="event_bg.jsp?ottfftuidanID=<%=ottfftuidanID%>&fileId=" + fileId;
		document.form1.target = "_self";
		document.form1.submit();
	}
}

// 选择举报性质
function onReportTypeChange() {
	var reportTypeRadio = document.form1.reportTypeRadio;
	var reportType = document.form1.reportType;

	for (var i = 0; i < reportTypeRadio.length; i++)
	{
		if (reportTypeRadio[i].checked)
		{
			reportType.value = reportTypeRadio[i].value;
		}
	}
}

// 选择是否上传附件
function onNeedAttachChange() {
	var needAttachRadio = document.form1.needAttachRadio;
	var needAttach = document.form1.needAttach;

	for (var i = 0; i < needAttachRadio.length; i++)
	{
		if (needAttachRadio[i].checked)
		{
			needAttach.value = needAttachRadio[i].value;
		}
	}
}

// 选择分类
function onClassificationChange() {
	var classification = document.form1.classification;
	var subclassification = document.form1.subclassification;
	var index = 1;

	delAllOption(subclassification);
	subclassification.value = "";

	if (classification.value != "")
	{
		<%for (int i = 0; i < classificationList.size(); i++) {
			ClassificationInfo info = (ClassificationInfo) classificationList.get(i);%>
			if ("<%=info.getParentId()%>" == classification.value)
			{
				subclassification.options.add(new Option("<%=info.getName()%>","<%=info.getId()%>"));
				if ("<%=StringUtils.getNull2String(eventInfo.getSubclassification())%>" != "")
				{
					if ("<%=info.getId()%>" == "<%=StringUtils.getNull2String(eventInfo.getSubclassification())%>")
					{
						subclassification.options[index].selected = "selected";
					}
				}
				else
				{
					subclassification.options[0].selected = "selected";
				}
				index++;
			}
		<%}%>
	}
}

// 删除选项
function delAllOption(obj) {
	for (var i = obj.length - 1; i > 0; i--) {
		obj.remove(i);
	}
}

// 选择来电人
function onCallerNameChange() {
	var rdoCallerName = document.form1.rdoCallerName;
	var callerName = document.form1.callerName;
	var callerNameInput = document.form1.callerNameInput;

	if (rdoCallerName[0].checked)
	{
		callerNameInput.disabled = true;
		callerName.value = "匿名";
	}
	else
	{
		callerNameInput.disabled = false;
		callerName.value = callerNameInput.value;
	}
}

// 选择联系地址
function onAddressChange() {
	var rdoAddress = document.form1.rdoAddress;
	var address = document.form1.address;
	var addressInput = document.form1.addressInput;

	if (rdoAddress[0].checked)
	{
		addressInput.disabled = true;
		address.value = "无";
	}
	else
	{
		addressInput.disabled = false;
		address.value = addressInput.value;
	}
}

// 选择工单联系电话
function onPhoneNumberChange() {
	var rdoPhoneNumber = document.form1.rdoPhoneNumber;
	var phoneNumber = document.form1.phoneNumber;
	var phoneNumberInput = document.form1.phoneNumberInput;

	if (rdoPhoneNumber[0].checked)
	{
		phoneNumberInput.disabled = true;
		phoneNumber.value = "无";
	}
	else
	{
		phoneNumberInput.disabled = false;
		phoneNumber.value = phoneNumberInput.value;
	}
}

// 选择诉求地址
function onAppealAddressChange() {
	var rdoAppealAddress = document.form1.rdoAppealAddress;
	var appealAddress = document.form1.appealAddress;
	var appealAddressInput = document.form1.appealAddressInput;

	if (rdoAppealAddress[0].checked)
	{
		appealAddressInput.disabled = true;
		appealAddress.value = "无";
	}
	else
	{
		appealAddressInput.disabled = false;
		appealAddress.value = appealAddressInput.value;
	}
}

// 选择联系电话
function onPhoneNoChange() {
	var rdoPhoneNo = document.form1.rdoPhoneNo;
	var phoneNo = document.form1.phoneNo;
	var phoneNoInput = document.form1.phoneNoInput;

	if (rdoPhoneNo[0].checked)
	{
		phoneNoInput.disabled = true;
		phoneNo.value = "无";
	}
	else
	{
		phoneNoInput.disabled = false;
		phoneNo.value = phoneNoInput.value;
	}
}

// 选择重新交办
function onRedoChange() {
	var rdoRedo = document.form1.rdoRedo;
	var redo = document.form1.redo;

	for (var i = 0; i < rdoRedo.length; i++)
	{
		if (rdoRedo[i].checked)
		{
			redo.value = rdoRedo[i].value;
		}
	}
}

// 选择是否打通电话
function onSatisfactionCalledChange() {
	var rdo = document.form2.satisfactionCalledRadio;

	changeSatisfactionRdoStatus("Refuse", rdo[1].checked, "0", "1"); // 拒绝调查
	onSatisfactionRefuseChange();
}

// 选择拒绝调查
function onSatisfactionRefuseChange() {
	var rdo = document.form2.satisfactionRefuseRadio;

	if (rdo[0].disabled)
	{
		changeSatisfactionRdoStatus("Man", true, "0", "1"); // 工作人员是否沟通
		changeSatisfactionRdoStatus("Result", true, "2", "2"); // 对处理结果是否满意
		changeSatisfactionRdoStatus("Attitude", true, "2", "2"); // 对工作态度是否满意
	}
	else
	{
		changeSatisfactionRdoStatus("Man", rdo[0].checked, "0", "1"); // 工作人员是否沟通
		changeSatisfactionRdoStatus("Result", rdo[0].checked, "2", "2"); // 对处理结果是否满意
		changeSatisfactionRdoStatus("Attitude", rdo[0].checked, "2", "2"); // 对工作态度是否满意
	}
}

// 更改满意度调查答案单选框状态
function changeSatisfactionRdoStatus(s,status,value,index) {
	var rdo = eval("document.form2.satisfaction" + s + "Radio");
	for (var i = 0; i < rdo.length; i++)
	{
		rdo[i].disabled = status;
	}

	/*if (status)
	{
		rdo[index].checked = true;
		changeSatisfaction(s,value);
	}*/
}

// 更改满意度调查答案
function changeSatisfaction(s,value) {
	var obj = eval("document.form2.satisfaction" + s);
	obj.value = value;
}

// 选择满意度调查答案
function onSatisfactionRdoChange(s) {
	var rdo = eval("document.form2.satisfaction" + s + "Radio");
	var obj = eval("document.form2.satisfaction" + s);

	for (var i = 0; i < rdo.length; i++)
	{
		if (rdo[i].checked)
		{
			obj.value = rdo[i].value;
			break;
		}
	}

	if (s == "Called")
	{
		onSatisfactionCalledChange();
	}

	if (s == "Refuse")
	{
		onSatisfactionRefuseChange();
	}
}

// 数字和“-”检查
function checkNumberAndLine(obj, flag) {
	if (flag == 0)
	{
		obj.value = obj.value.replace(/[^\d-－]/g,'').replace(/[－]/g,'-');
	}
	else if (flag == 1)
	{
		obj.value = obj.value.replace(/[^\d-－,，]/g,'').replace(/[－]/g,'-').replace(/[，]/g,',');
	}
}

// 页面初始化
function init() {
	if ("<%=tab%>" == "1")
	{
		// 初始化来电人
		initRdoStatus("rdoCallerName","callerName","callerNameInput","匿名");

		// 初始化联系地址
		initRdoStatus("rdoAddress","address","addressInput","无");
		initRdoStatus("rdoPhoneNumber","phoneNumber","phoneNumberInput","无");
		// 诉求地址初始化
		initRdoStatus("rdoAppealAddress","appealAddress","appealAddressInput","无");

		// 初始化联系电话
		//initRdoStatus("rdoPhoneNo","phoneNo","phoneNoInput","无");

		// 选择分类
		onClassificationChange();

		// 初始化单选框状态
		function initRdoStatus(rdo,obj,input,flag) {
			var _rdo = eval("document.form1." + rdo);
			var _obj = eval("document.form1." + obj);
			var _input = eval("document.form1." + input);
			

			if (_obj.value == flag||_obj.value =="0")
			{
				_rdo[0].checked = true;
				_input.disabled = true;
			}
			else
			{
				_rdo[1].checked = true;
				_input.disabled = false;
				_input.value = _obj.value;
			}
		}
		//ifjiaoban();
		sminfo();
	}
	else if("<%=tab%>" == "2")
	{
		// 选择是否打通电话
		onSatisfactionCalledChange();
		// 选择拒绝调查
		onSatisfactionRefuseChange();
	}
	var ottfftuidanID="<%=ottfftuidanID%>";
	if(ottfftuidanID!=null&&ottfftuidanID!=""){
		displaytuidan();
	}
}

//根据内容描述是否包含重新交办 自动选择 是否重新交办
function ifjiaoban() {
	var content = document.form1.content.value;
	var rdoRedo = document.form1.rdoRedo;
	var redo = document.form1.redo;
	if (content.search("重新交办") > 0) {
		for (var i = 0; i < rdoRedo.length; i++) {
			if (rdoRedo[i].value == "1") {
				redo.value = rdoRedo[i].value;
				rdoRedo[i].checked = "true";
			}
		}
	}
}

//根据电话号码 查询历次来电及办理情况
function queryEventByPhone(){
	var rdoPhoneNo = document.form1.phoneNumberInput.value;
	if(rdoPhoneNo==""){
		alert("请填写联系电话");
		return false;
	}
	var url="event_list_phone.jsp?phoneNumber="+encodeURI(rdoPhoneNo) + "&notequalid=" + document.form1.id.value;
	window.open(url,"newwindow",'height=700, width=1024, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
}



// 根据事项名称查询重复件
function queryEventByName() {
	var name = document.form1.name.value;
	var url = "event_list_name.jsp?name=" + encodeURI(name) + "&notequalid=" + document.form1.id.value;
	window.open(url,"newwindow",'height=700, width=1024, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
}

// 重复件判定
function judgeDuplicate(id) {
	var url = "event_judge_duplicate.jsp?id=" + id + "&opened=yes";
	window.open(url, "newwindow", "height=700, width=1024, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
}

//返回上一级列表
function backToFirst(phoneNumber){
	window.location.href="<%=from%>" + "?history=yes&phoneNumber="+phoneNumber;
}

//显示退单信息
function fortuidan(){
	/*if (!check()) {
		return;
	}*/
	var tuidanmessage="<%=tuidanmessage%>";
	/*if(tuidanmessage!="success"){
		alert(tuidanmessage);
		return false;
	}*/
	var forwardDepartment = document.form1.forwardDepartment;
	if(forwardDepartment.value==""){
		alert("请选择转发单位。");
		return false;
	}
	var tuidan=document.getElementById("tuidan");
	var backClass = document.form1.backClass;// 退单理由
	var summary = document.form1.summary;// 退单说明
	var operator = document.form1.operator;// 退单申请人
	var approver = document.form1.approver;// 审核人 
	if(tuidan.style.display=="none"){
		tuidan.style.display='block';
		var tdly=document.getElementById("tdly");
		tdly.style.display='block';
		var tdsm=document.getElementById("tdsm");
		tdsm.style.display='block';
		var tdsqr=document.getElementById("tdsqr");
		tdsqr.style.display='block';
		alert("请填写退单信息");
		return false;
	}else{
		if(backClass.value==""){
			alert("请填写退单理由");
			return false;
		}
		if(summary.value==""){
			alert("请填写退单说明");
			return false;
		}
		if(operator.value==""){
			alert("请填写退单申请人");
			return false;
		}
		if(approver.value==""){
			alert("请填写审核人");
			return false;
		}
		//提交到退单页面
		document.form1.action="tuidan/event_tuidan_bg.jsp?id=<%=id%>&operation=tuidan&tuidantype=zongdui&ottfftuidanID=<%=ottfftuidanID%>&forwardDepartment="+forwardDepartment.value;
		document.form1.submit();
	}
}

//显示退单信息
function displaytuidan(){
	var tuidan=document.getElementById("tuidan");
	tuidan.style.display='block';
	var tdly=document.getElementById("tdly");
	tdly.style.display='block';
	var tdsm=document.getElementById("tdsm");
	tdsm.style.display='block';
	var tdsqr=document.getElementById("tdsqr");
	tdsqr.style.display='block';
}

function iftuidan(){
	var filetypes=document.getElementsByName("filetype");
	var filetype="";
	for(var i=0;i<filetypes.length;i++){
		if(filetypes[i].checked==true){
			filetype=filetypes[i].value;
			break;
		}
	}
	var status="<%=status%>";
	if(filetype=="2"&&status=="12"){
		displaytuidan();
	}else{
		var tuidan=document.getElementById("tuidan");
		tuidan.style.display='none';
		var tdly=document.getElementById("tdly");
		tdly.style.display='none';
		var tdsm=document.getElementById("tdsm");
		tdsm.style.display='none';
		var tdsqr=document.getElementById("tdsqr");
		tdsqr.style.display='none';
	}
}

//延期申请
function foryqsq(){
	//if (!check()) {
			//	return;
		//	}
	//var delaymessage="<%=delaymessage%>";
	 //if(delaymessage!="success"){
	 //alert(delaymessage);
	 // return false;
	 //}
	var zdyqsq=document.getElementById("zdyqsq");
	if(zdyqsq.style.display=="none"){
		displayyqInfo();
		alert("请填写延期信息");
		return false;
	}else{
		var flag=yzyqinfo();
		if(flag==true){
			//提交到延期页面
			document.form1.action="yqsq/event_yqsq_bg.jsp?id=<%=id%>&operation=yqsq&yqsqtype=zongdui&ottffyqsqID=<%=ottffyqsqID%>";
			document.form1.submit();
		}
	}
}

//显示延期信息
function displayyqInfo(){
	var zdyqsq = document.getElementById("zdyqsq");
	zdyqsq.style.display = 'block';
	var yqly = document.getElementById("yqly");
	yqly.style.display = 'block';
	var yqdate = document.getElementById("yqdate");
	yqdate.style.display = 'block';
	var yqjbr = document.getElementById("yqjbr");
	yqjbr.style.display = 'block';
	var yqdh = document.getElementById("yqdh");
	yqdh.style.display = 'block';
	var yqbz = document.getElementById("yqbz");
	yqbz.style.display = 'block';
}

//隐藏延期信息
function hiddenyqInfo(){
	var zdyqsq = document.getElementById("zdyqsq");
	zdyqsq.style.display = 'none';
	var yqly = document.getElementById("yqly");
	yqly.style.display = 'none';
	var yqdate = document.getElementById("yqdate");
	yqdate.style.display = 'none';
	var yqjbr = document.getElementById("yqjbr");
	yqjbr.style.display = 'none';
	var yqdh = document.getElementById("yqdh");
	yqdh.style.display = 'none';
	var yqbz = document.getElementById("yqbz");
	yqbz.style.display = 'none';
}

//验证延期信息
function yzyqinfo(){
	var zdsummary = document.form1.zdsummary;// 延期原因
	var delayDays = document.form1.delayDays;// 延期申请天数
	var zdapplyTime = document.form1.zdapplyTime;//延期申请日期
	var zdoperator = document.form1.zdoperator;//延期申请人
	var handler = document.form1.handler;//延期经办人
	var charger = document.form1.charger;//延期负责人
	var contactNumber = document.form1.contactNumber;//延期联系电话
	var remark = document.form1.remark;//延期备注
	if(zdsummary.value==""){
		alert("请填写延期申请理由");
		return false;
	}
	if(delayDays.value==""){
		alert("请填写延期申请天数");
		return false;
	}
	if(zdoperator.value==""){
		alert("请填写延期申请人");
		return false;
	}
	if(handler.value==""){
		alert("请填写延期经办人");
		return false;
	}
	if(charger.value==""){
		alert("请填写延期负责人");
		return false;
	}
	if(contactNumber.value==""){
		alert("请填写联系电话");
		return false;
	}
	if(remark.value==""){
		alert("请填写备注");
		return false;
	}
	return true;
}

// 选择诉求解决
function onEventResultChange() {
	var eventResultRadio = document.form1.eventResultRadio;
	var eventResult = document.form1.eventResult;

	for (var i = 0; i < eventResultRadio.length; i++) {
		if (eventResultRadio[i].checked) {
			eventResult.value = eventResultRadio[i].value;
			break;
		}
	}
}

// 选择是否满意
function onReplySatisfactionChange() {
	var replySatisfactionRadio = document.form1.replySatisfactionRadio;// 是否满意选项
	var replySatisfaction = document.form1.replySatisfaction;// 是否满意

	for (var i = 0; i < replySatisfactionRadio.length; i++)
	{
		if (replySatisfactionRadio[i].checked)
		{
			replySatisfaction.value = replySatisfactionRadio[i].value;
			break;
		}
	}
}

//选择是否工业区
function onstFactoryRedoChange(){
	var stFactoryRedo = document.form1.stFactoryRedo;
	var stFactory = document.form1.stFactory;
    var stFactoryName = document.form1.stFactoryName;
	for (var i = 0; i < stFactoryRedo.length; i++)
	{
		if (stFactoryRedo[i].checked)
		{
			stFactory.value = stFactoryRedo[i].value;
			if(stFactory.value=="0"){
				stFactoryName.disabled = true;
				stFactoryName.value="";
			}else{
				stFactoryName.disabled = false;
			}
		}
	}
}

function stReturnVisitReplyStyleChange(){
    var stReturnVisitReplyStyleRadio = document.form1.stReturnVisitReplyStyleRadio;// 答复方式选项
	var stReturnVisitReplyStyle = document.form1.stReturnVisitReplyStyle;// 答复方式
	var dtReturnVisitReplyTime = document.form1.dtReturnVisitReplyTime;// 答复市民时间

	for (var i = 0; i < stReturnVisitReplyStyleRadio.length; i++)
	{
		if (stReturnVisitReplyStyleRadio[i].checked)
		{
			stReturnVisitReplyStyle.value = stReturnVisitReplyStyleRadio[i].value;
			if (stReturnVisitReplyStyleRadio[i].value == "无联系电话")
			{
				dtReturnVisitReplyTime.disabled = true;
			}
			else
			{
				dtReturnVisitReplyTime.disabled = false;
			}

			break;
		}
	}
}

//回访复核是否解决
function onstReturnVisitEventResultRadioChange(){
	var stReturnVisitEventResultRadio = document.form1.stReturnVisitEventResultRadio;
	var stReturnVisitEventResult = document.form1.stReturnVisitEventResult;

	for (var i = 0; i < stReturnVisitEventResultRadio.length; i++) {
		if (stReturnVisitEventResultRadio[i].checked) {
			stReturnVisitEventResult.value = stReturnVisitEventResultRadio[i].value;
			break;
		}
	}
}

function radiochange(radioName,hiddenName){

	var radio=document.getElementsByName(radioName);
	var hiddeninput=document.getElementsByName(hiddenName);

	for (var i = 0; i < radio.length; i++) {
		if (radio[i].checked) {
			hiddeninput[0].value = radio[i].value;
			break;
		}
	}
}

// 限制反馈内容在maxlength字以内
function feedbackMaxLength(obj, maxlength) {
	if(obj.value.length > maxlength){
		alert("录入内容不能超过" + maxlength + "字。");
		obj.value = obj.value.substring(0, maxlength);
	}
}
// 选择答复方式
function onReplyStyleChange() {
	var replyStyleRadio = document.form1.replyStyleRadio;// 答复方式选项
	var replyStyle = document.form1.replyStyle;// 答复方式
	var replyTime = document.form1.replyTime;// 答复市民时间

	for (var i = 0; i < replyStyleRadio.length; i++)
	{
		if (replyStyleRadio[i].checked)
		{
			replyStyle.value = replyStyleRadio[i].value;
			if (replyStyleRadio[i].value == "无联系电话")
			{
				replyTime.disabled = true;
			}
			else
			{
				replyTime.disabled = false;
			}

			break;
		}
	}
}

// 状态选择后的回调函数
function selectHYLB(id, name) {
	document.form1.stExchangeName1.value = id;
	document.form1.stExchangeName2.value = name;
}

var flag=false;

function sminfo(){
	var css="none";
	var url="<%=request.getContextPath()%>/image/general/";
	if(flag==false){
		url=url+"Down.png";
	}else{
		url=url+"Up.png";
		css="block";
	}
	var sm= document.getElementById("sm");
	sm.src=url;
	for(var i=1;i<6;i++){
		var info= document.getElementById("s"+i);
		info.style.display =css;
	}
	if(flag==false){
		flag=true;
	}else{
		flag=false;
	}
}

//有奖举报
function foryjjb(){
	if (confirm("是否确认有奖举报上报？"))
	{
		document.form1.operation.value = "yjjb";
		document.form1.action="event_bg.jsp?yjjbStatus=1&yjjbtype=zongdui";
		document.form1.submit();
	}
}

// 有奖举报打印
function foryjjbPrint(){
	var url="event_yjjb_print.jsp?id=<%=id%>";
	var printWindow = window.open(
		url,
		"printCase",
		"height=640, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
	printWindow.print();
}

// 事项名称重复提醒
function duplicateByName() {
	eventService.duplicateByNameAjax(document.form1.name.value, callBackDuplicateByName);
}

// 事项名称重复提醒回调函数
function callBackDuplicateByName(num) {
	var span = document.getElementById("duplicateNameNum");
	if (num > 0) {
		span.style.display = "inline-block";
		span.innerHTML = num;
	} else {
		span.style.display = "none";
	}
}

// 是否显示判定重复件按钮
function disabledJudgeDuplicate() {
	document.getElementById("btnJudgeDuplicate").disabled = true;
}
//发起督办
function initGddb(id){
	window.location.href="list/event_gddb_edit.jsp?id="+id;
}
function checkNum(){
	alert("bbb");
}
</script>
