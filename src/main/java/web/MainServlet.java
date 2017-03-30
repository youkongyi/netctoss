package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取请求路径
		String path = request.getServletPath();
		//根据规范处理路径
		if("/findCost.do".equals(path)) {
			findCost(request,response);
		} else if("/toAddCost.do".equals(path)) {
			toAddCost(request,response);
		} else if("/addCost.do".equals(path)) {
			addCost(request,response);
		} else if("/toUpdateCost.do".equals(path)) {
			toUpdateCost(request,response);
		} else if("/updateCost.do".equals(path)) {
			updateCost(request,response);
		} else if("/delete.do".equals(path)) {
			delete(request,response);
		} else if("/toLogin.do".equals(path)) {
			toLogin(request,response);
		} else if("/login.do".equals(path)) {
			login(request,response);
		} else if("/toIndex.do".equals(path)) {
			toIndex(request,response);
		} else if("/logout.do".equals(path)) {
			logout(request,response);
		} else if("/createImg.do".equals(path)) {
			createImg(request,response);
		}
		else {
		
			throw new RuntimeException("查无此页");
		}
		
	}
	
	protected void createImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//生成验证码及图片
		Object[] object = ImageUtil.createImage();
		//将验证码存入session
		String imgcode = (String)object[0];
		HttpSession session = request.getSession();
		session.setAttribute("imgcode", imgcode);
		//将图片输出给浏览器
		BufferedImage img = (BufferedImage)object[1];
		response.setContentType("image/png");
		//目标就是本次访问的那个浏览器
		OutputStream os = response.getOutputStream();
		ImageIO.write(img, "png", os);
		os.close();
		
		
	}
	
	
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//销毁session从而删除其内部数据
		request.getSession().invalidate();
		response.sendRedirect("toLogin.do");
	}
	
	
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String costId =request.getParameter("id");
		CostDao dao = new CostDao();
		dao.delete(new Integer(costId));
		//request.getRequestDispatcher("findCost.do").forward(request, response);
		response.sendRedirect("findCost.do");
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String adminCode = request.getParameter("adminCode");
		String password = request.getParameter("password");
		String code = request.getParameter("code");
		HttpSession session = request.getSession();
		String imgcode = (String)session.getAttribute("imgcode");
		if(code == null||code.equals("")||!code.equalsIgnoreCase(imgcode)) {
			request.setAttribute("error", "验证码错误");
			request.getRequestDispatcher("WEB-INF/main/login.jsp").forward(request, response);
			return;
		}
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		if(a == null) {
			request.setAttribute("adminCode", adminCode);
			request.setAttribute("error", "帐号错误");
			request.getRequestDispatcher("WEB-INF/main/login.jsp").forward(request, response);
		} else if(!a.getPassword().equals(password)){
			//密码错误给予提示，传发
			request.setAttribute("adminCode", adminCode);
			request.setAttribute("error", "密码错误");
			request.getRequestDispatcher("WEB-INF/main/login.jsp").forward(request, response);
		} else {
			//将帐号存入cookie
			Cookie c = new Cookie("adminCode",adminCode);
			response.addCookie(c);
			//也可以将帐号存入session
			session.setAttribute("adminCode", adminCode);
			
			
			//验证通过，重定向到首页
			response.sendRedirect("toIndex.do");
		}
		
	}
	
	protected void toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("WEB-INF/main/login.jsp").forward(request, response);
		//System.out.println(1111111);
		
	}
	
	protected void toIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("WEB-INF/main/index.jsp").forward(request, response);
		
	}
	
	
	protected void updateCost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		String costType = request.getParameter("costType");
		String baseDuration = request.getParameter("baseDuration");
		String baseCost = request.getParameter("baseCost");
		String unitCost = request.getParameter("unitCost");
		String descr = request.getParameter("descr");
		String costId = request.getParameter("costId");
		//封装然后保存这些数据
		Cost cost = new Cost();
		cost.setCostId(new Integer(costId));
		cost.setName(name);
		cost.setCostType(costType);
		cost.setDescr(descr);
		if(baseDuration != null && !baseDuration.equals("")) {
			cost.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost != null && !baseCost.equals("")) {
			cost.setBaseCost(new Double(baseCost));
		}
		if(unitCost != null && !unitCost.equals("")) {
			cost.setUnitCost(new Double(unitCost));
		}
		System.out.println(cost);
		CostDao dao = new CostDao();
		dao.update(cost);
		response.sendRedirect("findCost.do");
	}
	
	
	protected void toUpdateCost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		CostDao dao = new CostDao();
		Cost cost = dao.findById(new Integer(id));
		request.setAttribute("cost", cost);
		request.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(request, response);
		
		
	}
	

	//查询资费
	protected void findCost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		//查询所有的资费
		CostDao dao = new CostDao();
		//List<Cost> list = dao.findAll();
		List<Cost> page = dao.findByPage(1, 5);
		//将其转发给jsp
		//request.setAttribute("costs", list);
		request.setAttribute("costs", page);
		//当前;/netctoss/findCost.do
		//目标;/netctoss/WEB-INF/cost/find.jsp
		request.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(request, response);
	}
	//打开增加资费页面
	protected void toAddCost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//转发到JSP
		request.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(request, response);
		
	}
	
	//增加资费
	protected void addCost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		String costType = request.getParameter("costType");
		String baseDuration = request.getParameter("baseDuration");
		String baseCost = request.getParameter("baseCost");
		String unitCost = request.getParameter("unitCost");
		String descr = request.getParameter("descr");
		//封装然后保存这些数据
		Cost cost = new Cost();
		cost.setName(name);
		cost.setCostType(costType);
		cost.setDescr(descr);
		if(baseDuration != null && !baseDuration.equals("")) {
			cost.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost != null && !baseCost.equals("")) {
			cost.setBaseCost(new Double(baseCost));
		}
		if(unitCost != null && !unitCost.equals("")) {
			cost.setUnitCost(new Double(unitCost));
		}
		CostDao dao = new CostDao();
		dao.save(cost);
		response.sendRedirect("findCost.do");
	}
	
}
