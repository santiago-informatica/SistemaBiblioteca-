package uy.utu.biblioteca;
import java.sql.*;import java.util.*;
public class MySqlLibroDAO implements LibroDAO {
 public List<Libro> listar(String f)throws Exception {List<Libro> a=new ArrayList<>();try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement("SELECT * FROM libros WHERE titulo LIKE ? OR autor LIKE ?")){p.setString(1,"%"+f+"%");p.setString(2,"%"+f+"%");try(ResultSet r=p.executeQuery()){while(r.next())a.add(new Libro(r.getInt("id"),r.getString("titulo"),r.getString("autor"),r.getString("isbn"),r.getInt("stock")));}}return a;}
 public void crear(Libro l)throws Exception{try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement("INSERT INTO libros(titulo,autor,isbn,stock) VALUES(?,?,?,?)")){p.setString(1,l.titulo());p.setString(2,l.autor());p.setString(3,l.isbn());p.setInt(4,l.stock());p.executeUpdate();}}
 public void actualizar(Libro l)throws Exception{try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement("UPDATE libros SET titulo=?,autor=?,isbn=?,stock=? WHERE id=?")){p.setString(1,l.titulo());p.setString(2,l.autor());p.setString(3,l.isbn());p.setInt(4,l.stock());p.setInt(5,l.id());p.executeUpdate();}}
 public void eliminar(int id)throws Exception{try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement("DELETE FROM libros WHERE id=?")){p.setInt(1,id);p.executeUpdate();}}
}