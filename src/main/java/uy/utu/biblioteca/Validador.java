package uy.utu.biblioteca;
import java.util.regex.Pattern;
public final class Validador {
 private static final Pattern EMAIL=Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
 private Validador(){}
 public static String requerido(String s,String campo)throws ValidacionException{if(s==null||s.isBlank())throw new ValidacionException("El campo "+campo+" es obligatorio.");return s.trim();}
 public static void email(String s)throws ValidacionException{if(s==null||!EMAIL.matcher(s.trim()).matches())throw new ValidacionException("El email no tiene un formato válido.");}
 public static void stock(int n)throws ValidacionException{if(n<0)throw new ValidacionException("El stock no puede ser negativo.");}
}
