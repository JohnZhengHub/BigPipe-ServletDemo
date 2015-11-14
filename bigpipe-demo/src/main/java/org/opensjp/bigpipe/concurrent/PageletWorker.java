package org.opensjp.bigpipe.concurrent;

import java.util.concurrent.Callable;

import org.opensjp.bigpipe.utils.Renderer;
/**
 * 执行每个pagelet的业务逻辑　
 * @author John Zheng
 *
 */
public class PageletWorker implements Callable<String> {
	//模拟完成业务逻辑的运行时间（pagelet所需要的数据，渲染页面等的总时间）
	private int runtime;
	//pagelet视图模板
	private String pageletViewPath;
	private String pageletKey;
	/**
	 * 创建一个pagelet 执行器．执行pagelet的业务逻辑和渲染页面．这里只是模拟．
	 * @param runtime
	 * @param pageletViewPath
	 */
	public PageletWorker(int runtime,String pageletKey, String pageletViewPath) {
		this.runtime = runtime;
		this.pageletKey = pageletKey;
		this.pageletViewPath = pageletViewPath;
	}

	public String call() throws Exception {
		//模仿业务逻辑的处理和相关数据获取时间
		Thread.sleep(runtime);
		//模仿页面渲染过程
		String result = Renderer.render(pageletViewPath);
		result = buildJsonResult(result);
		return result;
	}

	/**
     * 将结果转化为json形式　
     * @param result
     * @return
     */
	private String buildJsonResult(String result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"application/javascript\">")
                .append("\nreplace(\"")
                .append(pageletKey)
                .append("\",\'")
                .append(result.replaceAll("\n","")).append("\');\n</script>");
        return (String) sb.toString();
    }
}
