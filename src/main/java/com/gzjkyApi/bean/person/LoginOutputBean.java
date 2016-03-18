package com.gzjkyApi.bean.person;

import java.util.List;

import com.gzjky.bean.extend.UserinfoAndPatientinfoBean;

public class LoginOutputBean {

	//用户信息
	private String user;
	//患者list
	private List<UserinfoAndPatientinfoBean> PatientList;
	//当前用户信息
	private UserinfoAndPatientinfoBean Patient;
	//当前patientid
	private String PatientID;
	//上次登录时间
	private String online;
	
	//错误信息
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<UserinfoAndPatientinfoBean> getPatientList() {
		return PatientList;
	}

	public void setPatientList(List<UserinfoAndPatientinfoBean> patientList) {
		PatientList = patientList;
	}

	public UserinfoAndPatientinfoBean getPatient() {
		return Patient;
	}

	public void setPatient(UserinfoAndPatientinfoBean patient) {
		Patient = patient;
	}

	public String getPatientID() {
		return PatientID;
	}

	public void setPatientID(String patientID) {
		PatientID = patientID;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}



	
}
