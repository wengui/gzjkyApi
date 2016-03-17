package com.gzjkyApi.action.person;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.bean.extend.PatientAndDoctorHospitalBean;
import com.gzjky.bean.extend.PatientAndEquipmentBean;
import com.gzjky.dao.readdao.EquipmentAndPatientReadMapper;
import com.gzjky.dao.readdao.PatientAndHospitalDoctorReadMapper;
import com.gzjkyApi.action.ApiBaseAction;

import net.sf.json.JSONArray;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class getEquipment extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952672370753974717L;

	
	@Autowired
	private EquipmentAndPatientReadMapper equipmentAndPatientReadMapper;

	// 前台对应的参数bean
	private LoginOutputBean param = new LoginOutputBean();

	private JSONArray result;

	public String doExec() throws Exception {

		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());

		// 将前台传来的参数copy到javabean中，方便处理
		// 病人名
		// 测量时间
		HttpServletRequest request = ServletActionContext.getRequest();
		BeanUtils.populate(param, request.getParameterMap());

		// 用户名
		//UserinfoAndPatientinfoBean userinfoAndPatientinfo = (UserinfoAndPatientinfoBean)ActionContext.getContext().getSession().get("Patient");
		//用户设备信息取得

		
		List<PatientAndEquipmentBean> equipmentList=null;
		equipmentList = equipmentAndPatientReadMapper.selectByPatientId(Integer.parseInt("40"));


		// 将出力Bean转成json对象

		//result = JSONObject.fromObject(nanoCheckerReportList);// 将list转换为json数组

		// 将java对象转成json对象
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		result = JSONArray.fromObject(equipmentList, jsonConfig);

		return SUCCESS;
	
		
	}


	
	
	public JSONArray getResult() {
		return result;
	}

	public void setResult(JSONArray result) {
		this.result = result;
	}
}
