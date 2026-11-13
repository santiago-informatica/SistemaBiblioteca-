package uy.utu.biblioteca;
import java.util.List;
public interface BibliotecaService {
 List<Libro> libros(String filtro)throws Exception;
 List<Alumno> alumnos(String filtro)throws Exception;
 List<Prestamo> prestamos(String filtro)throws Exception;
 void guardarLibro(int id,String titulo,String autor,String isbn,String stock)throws Exception;
 void guardarAlumno(int id,String nombre,String documento,String email,String grupo)throws Exception;
 void eliminarLibro(int id)throws Exception;
 void eliminarAlumno(int id)throws Exception;
 void crearPrestamo(int alumnoId,int libroId,int usuarioId)throws Exception;
 void devolver(int prestamoId)throws Exception;
 void eliminarPrestamo(int prestamoId)throws Exception;
 List<String[]> reporte()throws Exception;
}
