package com.gzjkyApi.action.person;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.base.util.date.DateUtil;
import com.gzjky.base.util.password.PwdUtil;
import com.gzjky.bean.extend.PatientAndDoctorHospitalBean;
import com.gzjky.bean.extend.PatientAndEquipmentBean;
import com.gzjky.bean.extend.UserinfoAndPatientinfoBean;
import com.gzjky.bean.gen.Onlines;
import com.gzjky.bean.gen.UserAndPhone;
import com.gzjky.bean.gen.UserInfo;
import com.gzjky.dao.readdao.OnlinesReadMapper;
import com.gzjky.dao.readdao.UserAndPatientReadMapper;
import com.gzjky.dao.readdao.UserAndPhoneReadMapper;
import com.gzjky.dao.readdao.UserInfoReadMapper;
import com.gzjky.dao.writedao.OnlinesWriteMapper;
import com.gzjky.dao.writedao.UserAndPhoneWriteMapper;
import com.gzjkyApi.action.ApiBaseAction;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONObject;


public class login extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952672370753974717L;

	@Autowired
	private UserInfoReadMapper userInfoReadMapper;
	@Autowired
	private OnlinesWriteMapper onlinesWriteMapper;
	@Autowired
	private OnlinesReadMapper onlinesReadMapper;
	@Autowired
	private UserAndPatientReadMapper userAndPatientReadMapper;
	
	@Autowired
	private UserAndPhoneReadMapper userAndPhoneReadMapper;
	@Autowired
	private UserAndPhoneWriteMapper userAndPhoneWriteMapper;
	

	private JSONObject result;

	public String doExec() throws Exception {

		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		HttpServletRequest request = ServletActionContext.getRequest();

		// 用户名
		String loginId = request.getParameter("username");
		// 邮箱
		String passwd = request.getParameter("password");
		// 邮箱
		String deviceid = request.getParameter("deviceid");
		
		
		// 通过用户名，手机，邮箱查找用户信息
		UserInfo userInfo = userInfoReadMapper.selectBy(loginId, loginId, loginId);
		// 调用业务逻辑组件的valid方法进行check
		// 用户名check

		// 验证用户输入的用户名和密码是否正确
		if (userInfo != null) {
			if (PwdUtil.ComparePasswords(userInfo.getPassword(), passwd)) {

				LoginOutputBean out=new LoginOutputBean();
				// Online表数据插入
				Onlines record = new Onlines();
				Date now = new Date();
				InetAddress addr = null;
				String ip = "";
				addr = InetAddress.getLocalHost();
				ip = addr.getHostAddress().toString();// 获得本机IP
				// IP地址
				record.setIpadddress(ip);
				// 登录时间
				record.setLogintime(new DateTime(now));
				// 更新时间
				record.setUpdatetime(new DateTime(now));
				// UserID
				record.setUserid(userInfo.getId());

				Onlines historyOnline = new Onlines();
				historyOnline = onlinesReadMapper.selectByUserID(userInfo.getId());
				if (historyOnline == null) {
					onlinesWriteMapper.insertSelective(record);
				} else {
					onlinesWriteMapper.updateByByUserId(record, userInfo.getId());
				}

				// storage中设置userInfoBean用
				out.setUser(userInfo.getChinesename());
				//ActionContext.getContext().getSession().put("user", userInfo);
				List<UserinfoAndPatientinfoBean> userinfoAndPatientinfoList = null;
				userinfoAndPatientinfoList = userAndPatientReadMapper
						.selectUserAndPatientinfoByUserId(userInfo.getId());
				//  storage中中添加PatientList信息用
				//ActionContext.getContext().getSession().put("PatientList", userinfoAndPatientinfoList);
				out.setPatientList(userinfoAndPatientinfoList);
				//  storage中添加默认Patient信息用
				if (userinfoAndPatientinfoList.size() != 0) {
					out.setPatient(userinfoAndPatientinfoList.get(0));
					out.setPatientID(userinfoAndPatientinfoList.get(0).getPid());
//					ActionContext.getContext().getSession().put("Patient", userinfoAndPatientinfoList.get(0));
//					ActionContext.getContext().getSession().put("PatientID",
//							userinfoAndPatientinfoList.get(0).getPid());
				}
				// storage中存储上次登录时间用
				out.setOnline(DateUtil.formatYMDHMS(historyOnline.getLogintime().toDate()));
//				ActionContext.getContext().getSession().put("online",
//						DateUtil.formatYMDHMS(historyOnline.getLogintime().toDate()));
				// 将java对象转成json对象
				
				
				UserAndPhone userAndPhone = userAndPhoneReadMapper.selectByPrimaryKey(loginId);
				if(userAndPhone!=null){
					UserAndPhone userandphone= new UserAndPhone();
					userandphone.setUserdevice(deviceid);
					userandphone.setUsername(loginId);
					userAndPhoneWriteMapper.insert(userandphone);
				}
				result = JSONObject.fromObject(out);// 将list转换为json数组
				
				

			} else {
				// 设置 error内容
				LoginMessageOutputBean outMessage= new LoginMessageOutputBean();
				outMessage.setMessage("用户名或密码错误!");
				// 将java对象转成json对象
				result = JSONObject.fromObject(outMessage);// 将list转换为json数组


			}
		} else {
			// 设置 error内容
			LoginMessageOutputBean outMessage= new LoginMessageOutputBean();
			outMessage.setMessage("该用户不存在!");
			// 将java对象转成json对象
			result = JSONObject.fromObject(outMessage);// 将list转换为json数组


		}
		
		System.out.println(result);
		return "success";

	}
	
	public String doExecAuto() throws Exception {

		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		HttpServletRequest request = ServletActionContext.getRequest();

		// 用户名
		String loginId = request.getParameter("username");
		// 邮箱
		String deviceid = request.getParameter("deviceid");

		
		LoginMessageOutputBean out=new LoginMessageOutputBean();
		// 通过用户名，手机，邮箱查找用户信息
		UserAndPhone userAndPhone = userAndPhoneReadMapper.selectByPrimaryKey(loginId);
		// 调用业务逻辑组件的valid方法进行check
		// 用户名check

		// 验证用户输入的用户名和密码是否正确
		if (userAndPhone != null) {
			if (deviceid.equals(userAndPhone.getUserdevice())) {
				
				
			} else {
				// 设置 error内容
				out.setMessage("请重新登录!");


			}
		} else {
			// 设置 error内容
			out.setMessage("请重新登录!");



		}
		// 将java对象转成json对象
		result = JSONObject.fromObject(out);// 将list转换为json数组

		System.out.println(result);
		
		
		return "success";

	}
	
	

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}
}
