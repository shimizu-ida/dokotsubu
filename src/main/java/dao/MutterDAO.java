package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;


public class MutterDAO {
	private final String JDBC_URL = "jdbc:mariadb://localhost:3306/dokotsubu";
    private final String DB_USER = "root";
    private final String DB_PASS = "insource.2015it";

    public List<Mutter> findAll() {
        List<Mutter> list = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try(Connection conn = DriverManager.getConnection(
                JDBC_URL, DB_USER, DB_PASS)) {
			
			String sql = """
					select id,name,text from mutter order by id desc
					""";
			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String text = rs.getString("text");
				
				Mutter m = new Mutter(id, name, text);
				list.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
    
    public boolean create(Mutter m) {
    	
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try(Connection conn = DriverManager.getConnection(
                JDBC_URL, DB_USER, DB_PASS)) {
			
			String sql = """
					insert into mutter(name, text) values(?,?)
					""";
			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, m.getUserName());
			pStmt.setString(2, m.getText());
			
			//ResultSet rs = pStmt.executeQuery();
			int res = pStmt.executeUpdate();
			if(res != 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
}
