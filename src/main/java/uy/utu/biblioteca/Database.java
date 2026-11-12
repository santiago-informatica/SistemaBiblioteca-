package uy.utu.biblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

 public static Connection conectar() throws SQLException {

  String url = System.getenv("DB_URL");
  String user = System.getenv("DB_USER");
  String pass = System.getenv("DB_PASSWORD");

  if (url == null || user == null || pass == null) {
   throw new SQLException(
           "Faltan las variables DB_URL, DB_USER o DB_PASSWORD."
   );
  }

  if (!url.contains("useUnicode=true")) {
   url += url.contains("?") ? "&" : "?";
   url += "useUnicode=true&characterEncoding=UTF-8";
  }

  try {
   return DriverManager.getConnection(url, user, pass);
  } catch (SQLException e) {
   throw new SQLException(
           "No se pudo conectar con MySQL: " + e.getMessage(), e
   );
  }
 }
}