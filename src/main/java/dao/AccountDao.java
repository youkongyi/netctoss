package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Account;
import entity.Admin;
import util.DBUtil;

public class AccountDao implements Serializable {

	
public List<Account> findByPage(int start,int end) {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from "
					+ "(select rownum rn,a.* from "
					+ "(select * from account_liuchao_table  "
					+ "order by account_id asc) a"
					+ " where rownum<=?) where rn>=? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, end);
			ps.setObject(2, start);
			ResultSet rs = ps.executeQuery();
			List<Account> list = new ArrayList<Account>();
			while(rs.next()) {
				Account a = createAccount(rs);
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
	
	
	public List<Account> findAll() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select account_id,real_name,idcard_no,login_name,status,"
					+ "create_date,last_login_time from account_liuchao_table";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(sql);
			List<Account> list = new ArrayList<Account>();
			while(rs.next()) {
				Account a = createAccount(rs);
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

	private Account createAccount(ResultSet rs) throws SQLException {
		Account a = new Account();
		a.setAccountId(rs.getInt("account_id"));
		a.setRealName(rs.getString("real_name"));
		a.setIdCardNo(rs.getString("idcard_no"));
		a.setLoginName(rs.getString("login_name"));
		a.setStatus(rs.getString("status"));
		a.setCreateDate(rs.getDate("create_date"));
		a.setLastLoginTime(rs.getTimestamp("last_login_time"));
		return a;
	}
	
}
