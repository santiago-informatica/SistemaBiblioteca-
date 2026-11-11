package uy.utu.biblioteca;
public class Alumno extends Persona {
 private String grupo;
 public Alumno(int id,String n,String d,String e,String g){super(id,n,d,e);grupo=g;}
 public String getGrupo(){return grupo;}
}