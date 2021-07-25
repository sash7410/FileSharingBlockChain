/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sumit
 */
public class DBQuery {
    Connection con;
    Statement st;
    ResultSet rs;
    public int add_user(String email,String mob,String id)
            
    {
        int i=0;
        try {
            
            String q="insert into user_details values('"+id+"','"+email+"','"+mob+"')";
            con=DBConnection.getconnection();
            st=con.createStatement();
            i=st.executeUpdate(q);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
    
    public String get_id(String e){
       String id="";
        try {
            
            String q="select * from user_details where email='"+e+"' ";
            con=DBConnection.getconnection();
            st=con.createStatement();
            rs=st.executeQuery(q);
            if(rs.next())
            {
           id=rs.getString("id");
            }
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
}
