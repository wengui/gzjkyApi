package com.gzjkyApi.action.equipmentAction;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.bean.extend.UserinfoAndPatientinfoBean;
import com.gzjky.bean.gen.Equipment;
import com.gzjky.bean.gen.EquipmentAndPatient;
import com.gzjky.bean.gen.PatientInfo;
import com.gzjky.bean.gen.UserInfo;
import com.gzjky.dao.readdao.EquipmentAndPatientReadMapper;
import com.gzjky.dao.readdao.EquipmentReadMapper;
import com.gzjky.dao.writedao.EquipmentAndPatientWriteMapper;
import com.gzjkyApi.action.ApiBaseAction;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONObject;

/**
 * 设备绑定信息取得API
 * 
 * @author tom
 *
 */
public class EquipmentBindAction extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2724022739767028409L;

	@Autowired
	private EquipmentReadMapper equipmentReadMapper;
	@Autowired
	private EquipmentAndPatientReadMapper equipmentAndPatientReadMapper;
	@Autowired
	EquipmentAndPatientWriteMapper equipmentAndPatientWriteMapper;

	private JSONObject result;
	String state = "";

	public String doExec() throws Exception {
		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		//TODO 
		int paitentId = 1;
		// 页面参数取得
		HttpServletRequest request = ServletActionContext.getRequest();
		// 设备序列号
		String deviceSid = request.getParameter("deviceSid");

		Equipment equ = equipmentReadMapper.selectByPrimaryNum(deviceSid);
		List<PatientInfo> patientInfoList = null;
		// 设备关联patient信息取得
		patientInfoList = equipmentAndPatientReadMapper.selectByEquipNum(deviceSid);
		
		// 序列号不存在
		if(equ == null){
			state = "1";// 序列号不存在
		}else{
			int deviceRer=  equ.getEquipmentversion();
			// 108的设备可以绑定两个患者，其他的只能绑定一个患者
			if(deviceRer == 108){
				if(patientInfoList.size() >1){
					state = "2";// 该设备已绑定两个账户
				}else if(patientInfoList.size() == 1){
					if(paitentId == patientInfoList.get(0).getId()){
						state = "3";// 你已经绑定过该设备
					}else{
						// 设备进行绑定
						String updateSate = deviceBind(request,"0",equ.getId());
						if("0".equals(updateSate)){
							state = "5";// 绑定成功
						}else{
							state = "6";// 绑定失败
						}
					}
				}else{
					// 设备进行绑定
					String updateSate = deviceBind(request,"0",equ.getId());
					if("0".equals(updateSate)){
						state = "5";// 绑定成功
					}else{
						state = "6";// 绑定失败
					}
				}
			}else{
				if(patientInfoList.size() >0){
					// 该设备已绑定一个账户
					state = "4";
				}else{
					// 设备进行绑定
					String updateSate = deviceBind(request,"0",equ.getId());
					if("0".equals(updateSate)){
						state = "5";// 绑定成功
					}else{
						state = "6";// 绑定失败
					}
				}
				
			}
		}
		
		EquipmentBindOutputBean modelMap = new EquipmentBindOutputBean();
		modelMap.setState(state);
		// 将java对象转成json对象
		result = JSONObject.fromObject(modelMap);// 将list转换为json数组
		return SUCCESS;
	}

	/*	
	 * 设备绑定
	 * @param request
	 * @param 设备已绑定用户数
	 * @param 设备id
	 */
	public String deviceBind(HttpServletRequest request,String count,int eId) {
		
		String state = "1";// 绑定失败
		// 设备序列号
		String device_ver= request.getParameter("deviceRer");
		String nickname=request.getParameter("deviceNickname");
		
		// 获取当前Patient信息
		UserinfoAndPatientinfoBean userinfoAndPatientinfo = (UserinfoAndPatientinfoBean) ActionContext.getContext()
				.getSession().get("Patient");
		// 获取当前登录用户信息
		UserInfo userInfo = (UserInfo) ActionContext.getContext().getSession().get("user");

		EquipmentAndPatient quipmentAndPatient = new EquipmentAndPatient();
		// 是否删除flag
		quipmentAndPatient.setIsdelete(false);
		// patientID TODO
		//quipmentAndPatient.setPatientid(Integer.parseInt(userinfoAndPatientinfo.getPid()));
		quipmentAndPatient.setPatientid(1);
		// 设备ID
		quipmentAndPatient.setEquipmentid(eId);
		// 创建者 TODO
		//quipmentAndPatient.setCreator(String.valueOf(userInfo.getId()));
		quipmentAndPatient.setCreator("222222");
		// 创建时间
		DateTime now = new DateTime(new Date());
		quipmentAndPatient.setCreatedon(now);
		//Patienttype
		if("108".equals(device_ver)){
			if(count.equals("1")){
				quipmentAndPatient.setPatienttype(2);
			}
			else
			{
				quipmentAndPatient.setPatienttype(1);
			}
		}
		else{
			quipmentAndPatient.setPatienttype(1);
		}
		//设备别名设置
		quipmentAndPatient.setNickname(nickname);

		if ((equipmentAndPatientWriteMapper.insertSelective(quipmentAndPatient)) == 1) {
			state = "0";// 绑定成功
		}		
		return state;
	}
	
	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

}
