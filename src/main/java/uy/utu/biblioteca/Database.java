package uy.utu.biblioteca;
import java.sql.*;
public final class Database {
 private Database() {}
 public static Connection conectar() throws SQLException {
  String url=System.getenv("DB_URL"), user=System.getenv("DB_USER"), pass=System.getenv("DB_PASSWORD");
  if(url==null||url.isBlank()||user==null||pass==null) throw new SQLException("Faltan DB_URL, DB_USER o DB_PASSWORD. Configuralas en IntelliJ o en el entorno.");
  return DriverManager.getConnection(url,user,pass);
 }
 public static boolean comprobar() { try(Connection c=conectar(); PreparedStatement s=c.prepareStatement("SELECT 1")){s.executeQuery();return true;}catch(SQLException e){return false;} }
}
