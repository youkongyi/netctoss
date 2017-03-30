package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AccountDao;
import dao.AdminDao;
import dao.CostDao;
import entity.Account;
import entity.Admin;
import entity.Cost;

public class AccountServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取Servlet的路径
		String path = request.getServletPath();
		if("/findAccount.lc".equals(path)) {
			findAccount(request,response);
		} else if("/findAdmin.lc".equals(path)) {
			findAdmin(request,response);
		} else if("/findPage.lc".equals(path)) {
			findPage(request,response);
		} else if("/accountPage.lc".equals(path)) {
			accountPage(request,response);
		}
		else {
			throw new RuntimeException("查无此页");
		}
		
	}
	
	protected void findPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String page = (String)request.getParameter("page");
//			System.out.println(page);
			int start = (Integer.parseInt(page)-1)*5+1;
			int end = Integer.parseInt(page)*5;
			CostDao dao = new CostDao();
			List<Cost> list = dao.findByPage(start, end);
//			for(Cost c : list) {
//				System.out.println(c);
//			}
			request.setAttribute("costs", list);
			request.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(request, response);
		}
	
	protected void accountPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String page = (String)request.getParameter("page");
//		System.out.println(page);
		int start = (Integer.parseInt(page)-1)*5+1;
		int end = Integer.parseInt(page)*5;
		AccountDao dao = new AccountDao();
		List<Account> list = dao.findByPage(start, end);
//		for(Account c : list) {
//			System.out.println(c);
//		}
		request.setAttribute("accounts", list);
		request.getRequestDispatcher("WEB-INF/account/find.jsp").forward(request, response);
	}
	
	
	protected void findAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//创建dao查询admin
		AdminDao dao = new AdminDao();
		List<Admin> list = dao.findAll();
		//将查询到的数据绑定到request
		request.setAttribute("admins", list);
		//获取转发器并转发
		request.getRequestDispatcher("WEB-INF/main/find.jsp").forward(request, response);
		
	}
	
	
	
	protected void findAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AccountDao dao = new AccountDao();
		List<Account> list = dao.findByPage(1, 5);
		request.setAttribute("accounts", list);
		//获取请求发送者，发送给JSP
		request.getRequestDispatcher("WEB-INF/account/find.jsp").forward(request, response);
		
	}

	
	
}
