package uy.utu.biblioteca;
import java.util.*;
/** Segunda implementación de LibroDAO, útil para pruebas sin MySQL. */
public class MemoryLibroDAO implements LibroDAO {
 private final Map<Integer,Libro> datos=new LinkedHashMap<>(); private int secuencia=1;
 public List<Libro> listar(String filtro){String f=filtro==null?"":filtro.toLowerCase();return datos.values().stream().filter(x->x.titulo().toLowerCase().contains(f)||x.autor().toLowerCase().contains(f)||x.isbn().toLowerCase().contains(f)).toList();}
 public void crear(Libro l){datos.put(secuencia,new Libro(secuencia,l.titulo(),l.autor(),l.isbn(),l.stock()));secuencia++;}
 public void actualizar(Libro l){if(!datos.containsKey(l.id()))throw new IllegalArgumentException("Libro inexistente");datos.put(l.id(),l);}
 public void eliminar(int id){datos.remove(id);}
}
