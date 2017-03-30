package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Admin;
import util.DBUtil;

public class AdminDao implements Serializable{
	
	public List<Admin> findByPage(int start,int end) {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from "
					+ "(select rownum rn ,e.* "
					+ "from (select * from admin_info_liuchao "
					+ "order by admin_id asc) e) "
					+ "where rn between ? and ? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, start);
			ps.setObject(2, end);
			ResultSet rs = ps.executeQuery();
			List<Admin> list = new ArrayList<Admin>();
			while(rs.next()) {
				Admin a = createAdmin(rs);
				list.add(a);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败");
		}finally {
			DBUtil.closeConnection(conn);
		}
	}
	public List<Admin> findAll() {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql ="select * from admin_info_liuchao ";
					
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			List<Admin> list = new ArrayList<Admin>();
			while(rs.next()) {
				Admin a = createAdmin(rs);
				list.add(a);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败",e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	public Admin findByCode(String adminCode) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from admin_info_liuchao "
					+ "where admin_code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, adminCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Admin a = createAdmin(rs);
				return a;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("根据账户查询管理员失败",e);
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}

	private Admin createAdmin(ResultSet rs) throws SQLException {
		Admin a = new Admin();
		a.setAdminId(rs.getInt("admin_id"));
		a.setAdminCode(rs.getString("admin_code"));
		a.setPassword(rs.getString("password"));
		a.setName(rs.getString("name"));
		a.setTelephone(rs.getString("telephone"));
		a.setEmail(rs.getString("email"));
		a.setEnrolldate(rs.getTimestamp("enrolldate"));
		return a;
	}
	public static void main(String[] args) {
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode("caocao");
		if(a != null) {
			System.out.println(a);
		}
	}
}
