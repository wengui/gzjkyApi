package com.gzjkyApi.action.historyAction;

import java.util.List;

public class ModelMap {
	
	/**
	 * 返回页面总记录条数
	 */
	public int recordTotal;
	
	/**
	 * 返回页面的json list对象
	 */
	public List<?> outBeanList;
	
	
	public int getRecordTotal() {
		return recordTotal;
	}

	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
	}

	public List<?> getOutBeanList() {
		return outBeanList;
	}

	public void setOutBeanList(List<?> outBeanList) {
		this.outBeanList = outBeanList;
	}
}
