package uy.utu.biblioteca;
import java.time.LocalDate;
public record Prestamo(int id,String alumno,String libro,String usuario,LocalDate fechaPrestamo,LocalDate fechaDevolucion,String estado) {}
