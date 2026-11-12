package uy.utu.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySqlLibroDAO implements LibroDAO {

 @Override
 public List<Libro> listar(String filtro) throws Exception {

  List<Libro> libros = new ArrayList<>();

  String sql = """
                SELECT id, titulo, autor, isbn, stock
                FROM libros
                WHERE titulo LIKE ? OR autor LIKE ?
                """;

  try (
          Connection conexion = Database.conectar();
          PreparedStatement consulta = conexion.prepareStatement(sql)
  ) {

   consulta.setString(1, "%" + filtro + "%");
   consulta.setString(2, "%" + filtro + "%");

   try (ResultSet resultado = consulta.executeQuery()) {

    while (resultado.next()) {

     String titulo = resultado.getString("titulo");
     String autor = resultado.getString("autor");

     // Comprobar los caracteres recibidos desde MySQL
     System.out.println("TITULO RECIBIDO: " + titulo);
     System.out.println("AUTOR RECIBIDO: " + autor);

     Libro libro = new Libro(
             resultado.getInt("id"),
             titulo,
             autor,
             resultado.getString("isbn"),
             resultado.getInt("stock")
     );

     libros.add(libro);
    }
   }
  }

  return libros;
 }

 @Override
 public void crear(Libro libro) throws Exception {

  String sql = """
                INSERT INTO libros(titulo, autor, isbn, stock)
                VALUES (?, ?, ?, ?)
                """;

  try (
          Connection conexion = Database.conectar();
          PreparedStatement consulta = conexion.prepareStatement(sql)
  ) {

   consulta.setString(1, libro.titulo());
   consulta.setString(2, libro.autor());
   consulta.setString(3, libro.isbn());
   consulta.setInt(4, libro.stock());

   consulta.executeUpdate();
  }
 }

 @Override
 public void actualizar(Libro libro) throws Exception {

  String sql = """
                UPDATE libros
                SET titulo = ?, autor = ?, isbn = ?, stock = ?
                WHERE id = ?
                """;

  try (
          Connection conexion = Database.conectar();
          PreparedStatement consulta = conexion.prepareStatement(sql)
  ) {

   consulta.setString(1, libro.titulo());
   consulta.setString(2, libro.autor());
   consulta.setString(3, libro.isbn());
   consulta.setInt(4, libro.stock());
   consulta.setInt(5, libro.id());

   consulta.executeUpdate();
  }
 }

 @Override
 public void eliminar(int id) throws Exception {

  String sql = "DELETE FROM libros WHERE id = ?";

  try (
          Connection conexion = Database.conectar();
          PreparedStatement consulta = conexion.prepareStatement(sql)
  ) {

   consulta.setInt(1, id);
   consulta.executeUpdate();
  }
 }
}