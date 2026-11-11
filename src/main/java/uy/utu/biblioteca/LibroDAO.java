package uy.utu.biblioteca;
import java.sql.*;import java.util.*;
public interface LibroDAO { List<Libro> listar(String filtro)throws Exception; void crear(Libro l)throws Exception; void actualizar(Libro l)throws Exception; void eliminar(int id)throws Exception; }