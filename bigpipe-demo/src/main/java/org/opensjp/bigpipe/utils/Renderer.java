package org.opensjp.bigpipe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 模仿页面的渲染器.
 * 这里只是获取对应的页面
 * @author 
 *
 */
public class Renderer {
	/**
	 * 模范页面的渲染，这里主要是获取对应的页面信息．
	 * @param viewPath
	 * @return
	 */
	public static String render(String viewPath){
		String absolutePath = Renderer.class.getClassLoader().getResource(viewPath).getPath();
		File file = new File(absolutePath);
		StringBuilder contentBuilder = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
				contentBuilder.append(str + "\n");
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return contentBuilder.toString();
	}
}
