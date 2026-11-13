package uy.utu.biblioteca;
import java.util.List;
public interface Repository<T> { List<T> listar(String filtro)throws Exception; void crear(T entidad)throws Exception; void actualizar(T entidad)throws Exception; void eliminar(int id)throws Exception; }
