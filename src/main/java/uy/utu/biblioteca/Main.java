package uy.utu.biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.setProperty("file.encoding", "UTF-8");

        SwingUtilities.invokeLater(() -> {

            JFrame ventana = new JFrame("Sistema de Biblioteca");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(800, 500);
            ventana.setLocationRelativeTo(null);

            JTabbedPane pestañas = new JTabbedPane();

            // PESTAÑA DE LIBROS
            JPanel panelLibros = new JPanel(new BorderLayout());

            JTable tablaLibros = new JTable();
            JButton cargarLibros = new JButton("Cargar libros");

            panelLibros.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
            panelLibros.add(cargarLibros, BorderLayout.SOUTH);

            cargarLibros.addActionListener(e -> {
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

                    tablaLibros.setModel(new DefaultTableModel(
                            filas,
                            new String[]{"Título", "Autor", "ISBN", "Stock"}
                    ));

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            ventana, ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            // PESTAÑA DE ALUMNOS
            JPanel panelAlumnos = new JPanel(new BorderLayout());

            JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JTextField campoBusqueda = new JTextField(20);
            JButton buscarAlumnos = new JButton("Buscar");
            JButton cargarAlumnos = new JButton("Mostrar todos");

            barraBusqueda.add(new JLabel("Buscar alumno:"));
            barraBusqueda.add(campoBusqueda);
            barraBusqueda.add(buscarAlumnos);
            barraBusqueda.add(cargarAlumnos);

            JTable tablaAlumnos = new JTable();

            panelAlumnos.add(barraBusqueda, BorderLayout.NORTH);
            panelAlumnos.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);

            Runnable mostrarAlumnos = () -> {
                try {
                    List<Alumno> datos =
                            new AlumnoDAO().listar(campoBusqueda.getText().trim());

                    String[][] filas = new String[datos.size()][5];

                    for (int i = 0; i < datos.size(); i++) {
                        Alumno alumno = datos.get(i);

                        filas[i] = new String[]{
                                String.valueOf(alumno.getId()),
                                alumno.getNombre(),
                                alumno.getDocumento(),
                                alumno.getEmail(),
                                alumno.getGrupo()
                        };
                    }

                    tablaAlumnos.setModel(new DefaultTableModel(
                            filas,
                            new String[]{
                                    "ID", "Nombre", "Documento",
                                    "Email", "Grupo"
                            }
                    ));

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            ventana, ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            };

            buscarAlumnos.addActionListener(e -> mostrarAlumnos.run());

            cargarAlumnos.addActionListener(e -> {
                campoBusqueda.setText("");
                mostrarAlumnos.run();
            });

            pestañas.addTab("Libros", panelLibros);
            pestañas.addTab("Alumnos", panelAlumnos);

            ventana.add(pestañas);
            ventana.setVisible(true);
        });
    }
}