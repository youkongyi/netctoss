package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	public void destroy() {
		

	}
	
	//tomcat启动时自动调用此方法，传入的是HttpServletRequest
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		String[] paths = new String[]{"/toLogin.do","/createImg.do","/login.do"};
		String p = request.getServletPath();
		//当前的路径
		for(String path : paths) {
			if(p.equals(path)) {
				arg2.doFilter(arg0, arg1);
				return;
			}
		}
		//从session中获取帐号
		HttpSession session = request.getSession();
		Object adminCode = session.getAttribute("adminCode");
		if(adminCode == null) {
			//没登录，重定向到登录页面
			response.sendRedirect("/netctoss/toLogin.do");
		} else {
			arg2.doFilter(arg0, arg1);
			
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
