package com.balancedbytes.game.roborally;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.game.roborally.db.RoboRallyDb;

public class User {
	
	private String fName;
	private String fSalt;
	private String fPassword;

	public void setName(String name) {
		fName = name;
	}
	
	public String getName() {
		return fName;
	}
	
	public String getSalt() {
		return fSalt;
	}
	
	public void setSalt(String salt) {
		fSalt = salt;
	}
	
	public String getPassword() {
		return fPassword;
	}
	
	public void setPassword(String password) {
		fPassword = password;
	}
	
    public static User findByName(String name) throws SQLException {
        try (Connection c = RoboRallyDb.getConnection()){
            PreparedStatement ps = c.prepareStatement(
        		"SELECT (name,salt,password) FROM users WHERE name = ?"
            );
            ps.setString(1, name);
            User user = null;
            try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	            	user = new User();
	            	user.setName(rs.getString(1)); 
	            	user.setSalt(rs.getString(2));
	            	user.setPassword(rs.getString(3));
	            }
            }
            return user;
		}
    }

}
