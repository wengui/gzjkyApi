package com.gzjkyApi.action.equipmentAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.bean.extend.PatientDeviceInfoBean;
import com.gzjky.dao.readdao.EquipmentAndPatientReadMapper;
import com.gzjkyApi.action.ApiBaseAction;
import com.gzjkyApi.bean.equipment.EquipmentOutputBean;

import net.sf.json.JSONObject;

/**
 * 设备信息取得API
 * @author tom
 *
 */
public class QueryEquipmentAction extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860312037140301385L;
	@Autowired
	private EquipmentAndPatientReadMapper equipmentAndPatientReadMapper;

	private JSONObject result;
	
	public String doExec() throws Exception{
		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		
		// 将前台传来的参数copy到javabean中，方便处理
		HttpServletRequest request = ServletActionContext.getRequest();
		//BeanUtils.populate(param, request.getParameterMap());
		
		List<PatientDeviceInfoBean> patientDeviceInfoList=null;
		
		patientDeviceInfoList= equipmentAndPatientReadMapper.queryMemberBindDevice(1);

		EquipmentOutputBean outBean = new EquipmentOutputBean();
		outBean.setEquipmentList(patientDeviceInfoList);
		// 将出力Bean转成json对象
		result = JSONObject.fromObject(outBean);// 将list转换为json对象
		
		return SUCCESS;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}


	
}
