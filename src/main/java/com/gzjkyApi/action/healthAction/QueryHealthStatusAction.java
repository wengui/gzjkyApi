package com.gzjkyApi.action.healthAction;

import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.bean.extend.QueryBPandHROutputBean;
import com.gzjky.dao.readdao.BloodPressureHistoryReadMapper;
import com.gzjkyApi.action.ApiBaseAction;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONObject;

/**
 * 患者生活习惯
 * @author yuting
 *
 */
public class QueryHealthStatusAction extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2957143535668658323L;
	
	@Autowired
	private BloodPressureHistoryReadMapper bloodPressureHistoryReadMapper;
	
	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	private JSONObject result;
	
	public String doExec() throws Exception{
		

		try {
			//int patientID = Integer.parseInt(ActionContext.getContext().getSession().get("PatientID").toString());
			
			// 患者ID要从session里取得
			QueryBPandHROutputBean outputBean = bloodPressureHistoryReadMapper.selectBPandHRresult(1);
			
			// 将出力Bean转成json对象
			result = JSONObject.fromObject(outputBean);// 将list转换为json对象
			
			return SUCCESS;
			
		} catch (Exception e) {
			return null;
		}
	}

}