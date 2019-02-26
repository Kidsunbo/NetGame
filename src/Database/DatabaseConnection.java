package Database;

import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.Properties;


public class DatabaseConnection {


    private static String user = "hxf879";
    private static String passwd = "123789";
    private static String url = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk:5432/hxf879";

    private static JSONObject setting = null;
//    static {
//        try {
//            setting = new JSONObject(new BufferedReader(new InputStreamReader(new FileInputStream(new File("db_set.json")))));
//            user = setting.getString("username");
//            passwd = setting.getString("password");
//            url = setting.getString("url");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


    public static boolean checkUser(String username){
        //System.out.println("Connecting to database...");
        try(Connection conn = DriverManager.getConnection(url,user,passwd);
        ){
            String sql;

            Statement stmt= conn.createStatement();
            sql = "SELECT username FROM accounting";
            ResultSet rs =stmt.executeQuery(sql);
            while(rs.next()){
                if(username.equals(rs.getString("username"))){
                    return true;
                }
            }

            return false;
        }
        catch(SQLException e){
            System.out.println("Cannot connect to Database");
        }
        return false;
    }

    public static boolean checkLogin(String username,String password){
        //System.out.println("Connecting to database...");
        try(Connection conn = DriverManager.getConnection(url,user,passwd);Statement stmt= conn.createStatement();){

            String sql;


            sql = "SELECT username,password FROM accounting";
            ResultSet rs =stmt.executeQuery(sql);
            while(rs.next()){
                if(username.equals(rs.getString("username"))
                &&password.equals(rs.getString("password"))){
                    return true;
                }
            }
            return false;
        }
        catch(SQLException e){
            System.out.println("Cannot connect to Database");
        }
        return false;
    }

    public static void main(String[] args) {
        checkUser("");
    }
}
