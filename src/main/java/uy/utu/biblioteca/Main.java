package uy.utu.biblioteca;
import javax.swing.*;import java.awt.*;import java.util.*;
public class Main {
 public static void main(String[] args){SwingUtilities.invokeLater(()->{JFrame f=new JFrame("Sistema de Biblioteca");f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);f.setSize(650,420);f.setLocationRelativeTo(null);
 JTabbedPane tabs=new JTabbedPane();JPanel p=new JPanel(new BorderLayout());JTable tabla=new JTable();p.add(new JScrollPane(tabla),BorderLayout.CENTER);JButton cargar=new JButton("Cargar libros");p.add(cargar,BorderLayout.SOUTH);
 cargar.addActionListener(e->{try{var datos=new MySqlLibroDAO().listar("");String[][] rows=new String[datos.size()][4];for(int i=0;i<datos.size();i++){Libro l=datos.get(i);rows[i]=new String[]{l.titulo(),l.autor(),l.isbn(),String.valueOf(l.stock())};}tabla.setModel(new javax.swing.table.DefaultTableModel(rows,new String[]{"Título","Autor","ISBN","Stock"}));}catch(Exception ex){JOptionPane.showMessageDialog(f,ex.getMessage());}});
 tabs.add("Libros",p);tabs.add("Alumnos y préstamos",new JLabel("Módulos en desarrollo",SwingConstants.CENTER));f.add(tabs);f.setVisible(true);});}
}