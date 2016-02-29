package com.gzjky.action.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;


public class TestAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952672370753974717L;
	
	private JSONObject jsonObject;
	
	public String getImageUrl() throws IOException{
		TestOutBean result = new TestOutBean();
		List<String> imageUrl = new ArrayList<String>();
		imageUrl.add("http://image.baidu.com/150309101F7.jpg");
		imageUrl.add("http://image.baidu.com/0.jpg");
		result.setName("余廷");
		result.setPassword("yuting0787");
		result.setImageUrl(imageUrl);

		// 将java对象转成json对象
		jsonObject = JSONObject.fromObject(result);// 将list转换为json数组
		
		return SUCCESS;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

}
