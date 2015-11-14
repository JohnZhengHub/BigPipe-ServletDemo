package org.opensjp.bigpipe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensjp.bigpipe.concurrent.PageletWorker;
import org.opensjp.bigpipe.utils.Renderer;

/**
 * BigPipe 的简单实现
 * bigPipe 时facebook的一个高新能页面加载算法，将页面分为不同的pagelet,用户发出请求后，服务端会并行的处理这些pagelet.
 * 即服务端并行的执行每个pagelet的业务，并渲染页面，一旦渲染完成就直接返回给前段
 * @author John Zheng
 *
 */
public class BigPipeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public BigPipeServlet() {
        super();
    }
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8"); 
		
		PrintWriter writer = response.getWriter();
		//这里是得到一个渲染的页面框架
		String frameView = Renderer.render("index.ftl");
		//将页面框架返回给前端
		flush(writer,frameView);
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
		completionService.submit(new PageletWorker(1500,"header","pagelets/header.ftl"));	//处理头部
		completionService.submit(new PageletWorker(2000,"sideBar","pagelets/sideBar.ftl"));//处理左边信息
		completionService.submit(new PageletWorker(4000,"mainContent","pagelets/mainContent.ftl"));//处理文章列表
		completionService.submit(new PageletWorker(1000,"footer","pagelets/footer.ftl"));//处理尾部
		
		//如果某个pagelet处理完成则返回给前端
		try {
			for(int i = 0;i < 4; i++){
				Future<String> future = completionService.take();
				String result = future.get();
				flush(writer,result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//最后关闭页脚
		closeHtml(writer);
	}
	/**
	 * 返回给前端
	 * @param writer
	 * @param content
	 */
	private void flush(PrintWriter writer,String content){
		writer.println(content);
		writer.flush();
	}
	
	/**
	 * 关闭页面
	 * @param writer
	 */
	private void closeHtml(PrintWriter writer){
		writer.println("</body>");
		writer.println("</html>");
		writer.flush();
		writer.close();
	}
}
