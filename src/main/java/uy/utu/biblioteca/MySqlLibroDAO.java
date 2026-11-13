package uy.utu.biblioteca;
import java.sql.*;import java.util.*;
public class MySqlLibroDAO implements LibroDAO {
 public List<Libro> listar(String f)throws SQLException{List<Libro> out=new ArrayList<>();String q="SELECT id,titulo,autor,isbn,stock FROM libros WHERE titulo LIKE ? OR autor LIKE ? OR isbn LIKE ? ORDER BY titulo";try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement(q)){String x="%"+(f==null?"":f.trim())+"%";p.setString(1,x);p.setString(2,x);p.setString(3,x);try(ResultSet r=p.executeQuery()){while(r.next())out.add(new Libro(r.getInt("id"),r.getString("titulo"),r.getString("autor"),r.getString("isbn"),r.getInt("stock")));}}return out;}
 public void crear(Libro l)throws SQLException{String q="INSERT INTO libros(titulo,autor,isbn,stock) VALUES(?,?,?,?)";try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement(q)){p.setString(1,l.titulo());p.setString(2,l.autor());p.setString(3,l.isbn());p.setInt(4,l.stock());p.executeUpdate();}}
 public void actualizar(Libro l)throws SQLException{String q="UPDATE libros SET titulo=?,autor=?,isbn=?,stock=? WHERE id=?";try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement(q)){p.setString(1,l.titulo());p.setString(2,l.autor());p.setString(3,l.isbn());p.setInt(4,l.stock());p.setInt(5,l.id());if(p.executeUpdate()==0)throw new SQLException("No existe el libro indicado.");}}
 public void eliminar(int id)throws SQLException{try(Connection c=Database.conectar();PreparedStatement p=c.prepareStatement("DELETE FROM libros WHERE id=?")){p.setInt(1,id);p.executeUpdate();}}
}
