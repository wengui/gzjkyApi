package com.gzjky.action.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;

import com.gzjky.action.ApiBaseAction;

import net.sf.json.JSONObject;


public class TestAction extends ApiBaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952672370753974717L;
	
	// 前台对应的参数bean
	private TestInputBean param = new TestInputBean();
	
	private JSONObject result;
	
	public String doExec() throws Exception{
		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		
		// 将前台传来的参数copy到javabean中，方便处理
		HttpServletRequest request = ServletActionContext.getRequest();
		BeanUtils.populate(param, request.getParameterMap());
		
		// 向前台返回值
		TestOutBean testOutBean = new TestOutBean();
		List<String> imageUrl = new ArrayList<String>();
		imageUrl.add("http://image.baidu.com/150309101F7.jpg");
		imageUrl.add("http://image.baidu.com/0.jpg");
		testOutBean.setName("余廷");
		testOutBean.setPassword("yuting0787");
		testOutBean.setImageUrl(imageUrl);

		// 将出力Bean转成json对象
		result = JSONObject.fromObject(testOutBean);// 将list转换为json数组
		
		return SUCCESS;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}
}
