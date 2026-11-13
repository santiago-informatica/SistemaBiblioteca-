package uy.utu.biblioteca;
public class Alumno extends Persona {
 private final String grupo;
 public Alumno(int id,String nombre,String documento,String email,String grupo){super(id,nombre,documento,email);this.grupo=grupo;}
 public String getGrupo(){return grupo;} @Override public String getTipo(){return "Alumno";}
}
