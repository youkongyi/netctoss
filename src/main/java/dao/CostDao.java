package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cost;
import util.DBUtil;

public class CostDao implements Serializable {
	
	public List<Cost> findByPage(int start,int end) {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from "
					+ "(select rownum rn,a.* from "
					+ "(select * from cost_liuchao_table order by cost_id asc) a) "
					+ " where rn between ? and ? ";
			String sql1 = "select * from "
					+ "(select rownum rn,a.* from "
					+ "(select * from cost_liuchao_table  "
					+ "order by cost_id asc) a"
					+ " where rownum<=?) where rn>=? ";
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setObject(1, end);
			ps.setObject(2, start);
			ResultSet rs = ps.executeQuery();
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()) {
				Cost c = createCost(rs);
				list.add(c);
			}
			return list;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败",e);
		}finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	
	public List<Cost> findAll() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from cost_liuchao_table "
					+ "order by cost_id";
			Statement smt = conn.createStatement();	
			ResultSet rs = smt.executeQuery(sql);
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()) {
				Cost cost = createCost(rs);
				list.add(cost);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败", e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	public void delete(int costId) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "delete cost_liuchao_table "
					+ "where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, costId);
			ps.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("删除失败");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	private Cost createCost(ResultSet rs) throws SQLException {
		Cost cost = new Cost();
		cost.setCostId(rs.getInt("cost_id"));
		cost.setName(rs.getString("name"));
		cost.setBaseDuration(rs.getInt("base_duration"));
		cost.setBaseCost(rs.getDouble("base_cost"));
		cost.setUnitCost(rs.getDouble("unit_cost"));
		cost.setStatus(rs.getString("status"));
		cost.setDescr(rs.getString("descr"));
		cost.setCreatime(rs.getTimestamp("creatime"));
		cost.setStartime(rs.getTimestamp("startime"));
		cost.setCostType(rs.getString("cost_type"));
		return cost;
	}

	public void save(Cost c) {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into cost_liuchao_table "
					+ "values(cost_seq_liuchao.nextval,"
					+"?,?,?,?,'1',?,sysdate,null,?) ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getName());
			//setInt()和setDouble()不允许传入null，
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("增加资费失败",e);
		} finally {
			DBUtil.closeConnection(conn);
		}
		
	}
	
	public void update(Cost c) {
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update cost_liuchao_table "
					+ " set name=?,base_duration=?,base_cost=?,unit_cost=?,descr=?,cost_type=? "
					+" where cost_id=? ";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getName());
			//setInt()和setDouble()不允许传入null，
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.setObject(7, c.getCostId());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("修改资费失败",e);
		} finally {
			DBUtil.closeConnection(conn);
		}
		
	}
	
		public static void main(String[] args) {
		CostDao dao = new CostDao();
		Cost c = dao.findById(100);
		if(c != null) {
			System.out.println(c);
		}
	}
	
		public Cost findById(int id) {
			Connection  conn = null;
			try {
				conn = DBUtil.getConnection();
				String sql = "select * from cost_liuchao_table "
						+ "where cost_id=?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					return createCost(rs);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("根据ID查询资费失败",e);
			} finally {
				DBUtil.closeConnection(conn);
			}
			return null;
		}
		
}
