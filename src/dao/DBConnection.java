package dao;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

 public class DBConnection{
     private static final String URL ="jdbc:sqlserver://localhost;databaseName=BankManagementSystem;encrypt=false;trustServerCertificate=true";
     private static final String USER = "mahmoud";
     private static final String PASSWORD ="123456";

    public static Connection getConnection() throws SQLException{

        return DriverManager.getConnection(URL ,USER , PASSWORD);
    }



 }
