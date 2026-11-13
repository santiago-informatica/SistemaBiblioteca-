package uy.utu.biblioteca;
public abstract class Persona {
 protected final int id; protected final String nombre,documento,email;
 protected Persona(int id,String nombre,String documento,String email){this.id=id;this.nombre=nombre;this.documento=documento;this.email=email;}
 public int getId(){return id;} public String getNombre(){return nombre;} public String getDocumento(){return documento;} public String getEmail(){return email;}
 public abstract String getTipo();
}
