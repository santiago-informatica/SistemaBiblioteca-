package uy.utu.biblioteca;
public abstract class Persona {
 protected int id; protected String nombre, documento, email;
 public Persona(int id,String nombre,String documento,String email){this.id=id;this.nombre=nombre;this.documento=documento;this.email=email;}
 public int getId(){return id;} public String getNombre(){return nombre;} public String getDocumento(){return documento;} public String getEmail(){return email;}
}