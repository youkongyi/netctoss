<%@page pageEncoding="utf-8"%>
<!-- 
	EL默认从4ge隐含对象中取值: page request session application
	不包括cookie，并且cookie不是隐含对象
	若使用EL从cookie中取值，语法如下：cookie.参数名.value
 -->
<img src="images/logo.png" alt="logo" class="left"/>
<%-- <span>欢迎您,${cookie.adminCode.value }</span>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<span>欢迎你,${adminCode }&nbsp;&nbsp;&nbsp;&nbsp;</span>
<a href="/netctoss/logout.do">[退出]</a> 