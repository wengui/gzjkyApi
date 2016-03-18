package com.gzjkyApi.action.equipmentAction;

import java.util.List;

import com.gzjky.bean.extend.PatientDeviceInfoBean;

public class EquipmentOutputBean {

	public List<PatientDeviceInfoBean> equipmentList;

	public List<PatientDeviceInfoBean> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<PatientDeviceInfoBean> equipmentList) {
		this.equipmentList = equipmentList;
	}
	
}
