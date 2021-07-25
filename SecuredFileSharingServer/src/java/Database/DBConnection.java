/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sumit
 */
public class DBConnection {
    
    
  public static   Connection con;
    
    
    
    public static Connection getconnection(){
      try {
          Class.forName("com.mysql.jdbc.Driver");
          con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Secured_file_sharing_server_db", "root","root");
          
          
          
          
      } catch (Exception ex) {
          Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
      }
      return con;
    }
    
    
    
}
