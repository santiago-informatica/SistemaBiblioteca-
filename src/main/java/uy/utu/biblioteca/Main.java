package uy.utu.biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

 public static void main(String[] args) {

  System.setProperty("file.encoding", "UTF-8");

  SwingUtilities.invokeLater(() -> {

   JFrame ventana = new JFrame("Sistema de Biblioteca");
   ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   ventana.setSize(650, 420);
   ventana.setLocationRelativeTo(null);

   JTabbedPane pestañas = new JTabbedPane();

   JPanel panelLibros = new JPanel(new BorderLayout());

   JTable tabla = new JTable();

   JButton cargar = new JButton("Cargar libros");

   panelLibros.add(new JScrollPane(tabla), BorderLayout.CENTER);
   panelLibros.add(cargar, BorderLayout.SOUTH);

   cargar.addActionListener(e -> {

    try {

     List<Libro> datos = new MySqlLibroDAO().listar("");

     String[][] filas = new String[datos.size()][4];

     for (int i = 0; i < datos.size(); i++) {

      Libro libro = datos.get(i);

      filas[i] = new String[]{
              libro.titulo(),
              libro.autor(),
              libro.isbn(),
              String.valueOf(libro.stock())
      };
     }

     tabla.setModel(new DefaultTableModel(
             filas,
             new String[]{"Título", "Autor", "ISBN", "Stock"}
     ));

    } catch (Exception ex) {

     JOptionPane.showMessageDialog(
             ventana,
             ex.getMessage(),
             "Error",
             JOptionPane.ERROR_MESSAGE
     );
    }
   });

   pestañas.addTab("Libros", panelLibros);

   pestañas.addTab(
           "Alumnos y préstamos",
           new JLabel("Módulos en desarrollo", SwingConstants.CENTER)
   );

   ventana.add(pestañas);
   ventana.setVisible(true);
  });
 }
}