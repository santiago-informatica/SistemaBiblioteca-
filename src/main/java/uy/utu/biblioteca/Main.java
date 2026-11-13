
package uy.utu.biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main {

 private static final BibliotecaService servicio = new ServicioBiblioteca();
 private static Usuario usuario;

 // Permite actualizar la tabla de libros desde la pantalla de préstamos.
 private static Runnable refrescarLibros = () -> {};

 public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
   try {
    if (!Database.comprobar()) {
     mensaje("No se pudo conectar con MySQL. Verificá Docker y las variables DB_URL, DB_USER y DB_PASSWORD.");
     return;
    }

    UsuarioDAO ud = new UsuarioDAO();
    ud.asegurarAdministradorInicial();
    usuario = login(ud);

    if (usuario != null) {
     mostrar(ud);
    }
   } catch (Exception e) {
    mensaje("No se pudo iniciar el sistema: " + e.getMessage());
   }
  });
 }

 private static Usuario login(UsuarioDAO dao) {
  JTextField u = new JTextField("admin");
  JPasswordField p = new JPasswordField();
  Object[] f = {"Usuario:", u, "Contraseña:", p};

  while (true) {
   if (JOptionPane.showConfirmDialog(
           null, f, "Inicio de sesión",
           JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
    return null;
   }

   try {
    Usuario x = dao.autenticar(
            u.getText().trim(),
            new String(p.getPassword())
    );

    if (x != null) {
     return x;
    }

    mensaje("Usuario o contraseña incorrectos.");
   } catch (Exception e) {
    mensaje("Error al iniciar sesión: " + e.getMessage());
    return null;
   }
  }
 }

 private static void mostrar(UsuarioDAO ud) {
  JFrame v = new JFrame(
          "Sistema de Biblioteca — " +
                  usuario.nombre() + " (" + usuario.rol() + ")"
  );

  v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  v.setSize(1050, 650);
  v.setLocationRelativeTo(null);

  JTabbedPane tabs = new JTabbedPane();
  tabs.addTab("Libros", panelLibros());
  tabs.addTab("Alumnos", panelAlumnos());
  tabs.addTab("Préstamos", panelPrestamos());
  tabs.addTab("Reportes", panelReportes());
  tabs.addTab("Estado", panelEstado());

  if ("ADMIN".equals(usuario.rol())) {
   tabs.addTab("Usuarios", panelUsuarios(ud));
  }

  v.add(tabs);
  v.setVisible(true);
 }

 private static JPanel base() {
  JPanel p = new JPanel(new BorderLayout(8, 8));
  p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  return p;
 }

 // ==================== LIBROS ====================

 private static JPanel panelLibros() {
  JPanel p = base();

  JTextField filtro = new JTextField(18);
  JTable t = new JTable();
  JPanel top = new JPanel();

  JButton buscar = new JButton("Buscar");
  JButton nuevo = new JButton("Nuevo");
  JButton editar = new JButton("Editar");
  JButton borrar = new JButton("Eliminar");

  top.add(new JLabel("Filtro:"));
  top.add(filtro);

  for (JButton b : new JButton[]{buscar, nuevo, editar, borrar}) {
   top.add(b);
  }

  borrar.setEnabled("ADMIN".equals(usuario.rol()));

  p.add(top, BorderLayout.NORTH);
  p.add(new JScrollPane(t));

  Runnable cargar = () -> {
   try {
    List<Libro> ls = servicio.libros(filtro.getText());

    DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor", "ISBN", "Stock"}, 0
    );

    for (Libro x : ls) {
     m.addRow(new Object[]{
             x.id(),
             x.titulo(),
             x.autor(),
             x.isbn(),
             x.stock()
     });
    }

    t.setModel(m);
   } catch (Exception e) {
    error(e);
   }
  };

  // Guardamos la función para poder actualizar esta tabla desde préstamos.
  refrescarLibros = cargar;

  buscar.addActionListener(e -> cargar.run());
  nuevo.addActionListener(e -> formLibro(null, cargar));

  editar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0) {
    formLibro(
            new Libro(
                    (int) t.getValueAt(r, 0),
                    (String) t.getValueAt(r, 1),
                    (String) t.getValueAt(r, 2),
                    (String) t.getValueAt(r, 3),
                    (int) t.getValueAt(r, 4)
            ),
            cargar
    );
   }
  });

  borrar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0 && confirmar()) {
    try {
     servicio.eliminarLibro((int) t.getValueAt(r, 0));
     cargar.run();
    } catch (Exception ex) {
     error(ex);
    }
   }
  });

  cargar.run();
  return p;
 }

 private static void formLibro(Libro l, Runnable recargar) {
  JTextField a = new JTextField(l == null ? "" : l.titulo());
  JTextField b = new JTextField(l == null ? "" : l.autor());
  JTextField c = new JTextField(l == null ? "" : l.isbn());
  JTextField d = new JTextField(l == null ? "0" : String.valueOf(l.stock()));

  Object[] f = {
          "Título", a,
          "Autor", b,
          "ISBN", c,
          "Stock", d
  };

  if (JOptionPane.showConfirmDialog(
          null, f,
          l == null ? "Nuevo libro" : "Editar libro",
          JOptionPane.OK_CANCEL_OPTION
  ) == JOptionPane.OK_OPTION) {
   try {
    servicio.guardarLibro(
            l == null ? 0 : l.id(),
            a.getText(),
            b.getText(),
            c.getText(),
            d.getText()
    );

    recargar.run();
   } catch (Exception e) {
    error(e);
   }
  }
 }

 // ==================== ALUMNOS ====================

 private static JPanel panelAlumnos() {
  JPanel p = base();

  JTextField filtro = new JTextField(18);
  JTable t = new JTable();
  JPanel top = new JPanel();

  JButton buscar = new JButton("Buscar");
  JButton nuevo = new JButton("Nuevo");
  JButton editar = new JButton("Editar");
  JButton borrar = new JButton("Eliminar");

  top.add(new JLabel("Filtro:"));
  top.add(filtro);

  for (JButton b : new JButton[]{buscar, nuevo, editar, borrar}) {
   top.add(b);
  }

  borrar.setEnabled("ADMIN".equals(usuario.rol()));

  p.add(top, BorderLayout.NORTH);
  p.add(new JScrollPane(t));

  Runnable cargar = () -> {
   try {
    List<Alumno> ls = servicio.alumnos(filtro.getText());

    DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Documento", "Email", "Grupo"}, 0
    );

    for (Alumno x : ls) {
     m.addRow(new Object[]{
             x.getId(),
             x.getNombre(),
             x.getDocumento(),
             x.getEmail(),
             x.getGrupo()
     });
    }

    t.setModel(m);
   } catch (Exception e) {
    error(e);
   }
  };

  buscar.addActionListener(e -> cargar.run());
  nuevo.addActionListener(e -> formAlumno(null, cargar));

  editar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0) {
    formAlumno(
            new Alumno(
                    (int) t.getValueAt(r, 0),
                    (String) t.getValueAt(r, 1),
                    (String) t.getValueAt(r, 2),
                    (String) t.getValueAt(r, 3),
                    (String) t.getValueAt(r, 4)
            ),
            cargar
    );
   }
  });

  borrar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0 && confirmar()) {
    try {
     servicio.eliminarAlumno((int) t.getValueAt(r, 0));
     cargar.run();
    } catch (Exception ex) {
     error(ex);
    }
   }
  });

  cargar.run();
  return p;
 }

 private static void formAlumno(Alumno a, Runnable recargar) {
  JTextField n = new JTextField(a == null ? "" : a.getNombre());
  JTextField d = new JTextField(a == null ? "" : a.getDocumento());
  JTextField e1 = new JTextField(a == null ? "" : a.getEmail());
  JTextField g = new JTextField(a == null ? "" : a.getGrupo());

  Object[] f = {
          "Nombre", n,
          "Documento", d,
          "Email", e1,
          "Grupo", g
  };

  if (JOptionPane.showConfirmDialog(
          null, f,
          a == null ? "Nuevo alumno" : "Editar alumno",
          JOptionPane.OK_CANCEL_OPTION
  ) == JOptionPane.OK_OPTION) {
   try {
    servicio.guardarAlumno(
            a == null ? 0 : a.getId(),
            n.getText(),
            d.getText(),
            e1.getText(),
            g.getText()
    );

    recargar.run();
   } catch (Exception e) {
    error(e);
   }
  }
 }

 // ==================== PRÉSTAMOS ====================

 private static JPanel panelPrestamos() {
  JPanel p = base();

  JTextField filtro = new JTextField(15);
  JTable t = new JTable();
  JPanel top = new JPanel();

  JButton buscar = new JButton("Buscar");
  JButton nuevo = new JButton("Registrar préstamo");
  JButton dev = new JButton("Registrar devolución");
  JButton borrar = new JButton("Eliminar préstamo");

  top.add(filtro);
  top.add(buscar);
  top.add(nuevo);
  top.add(dev);
  top.add(borrar);

  borrar.setEnabled("ADMIN".equals(usuario.rol()));

  p.add(top, BorderLayout.NORTH);
  p.add(new JScrollPane(t));

  Runnable cargar = () -> {
   try {
    List<Prestamo> ls = servicio.prestamos(filtro.getText());

    DefaultTableModel m = new DefaultTableModel(
            new String[]{
                    "ID", "Alumno", "Libro", "Usuario",
                    "Fecha préstamo", "Fecha devolución", "Estado"
            }, 0
    );

    for (Prestamo x : ls) {
     m.addRow(new Object[]{
             x.id(),
             x.alumno(),
             x.libro(),
             x.usuario(),
             x.fechaPrestamo(),
             x.fechaDevolucion(),
             x.estado()
     });
    }

    t.setModel(m);
   } catch (Exception e) {
    error(e);
   }
  };

  buscar.addActionListener(e -> cargar.run());

  // Registrar préstamo.
  nuevo.addActionListener(e -> {
   try {
    List<Alumno> al = servicio.alumnos("");
    List<Libro> li = servicio.libros("");

    if (al.isEmpty()) {
     mensaje("Debe haber alumnos cargados.");
     return;
    }

    // Solo mostramos libros que tengan stock disponible.
    List<Libro> disponibles = li.stream()
            .filter(x -> x.stock() > 0)
            .toList();

    if (disponibles.isEmpty()) {
     mensaje("No hay libros con ejemplares disponibles.");
     return;
    }

    JComboBox<String> ca = new JComboBox<>();

    for (Alumno x : al) {
     ca.addItem(x.getId() + " - " + x.getNombre());
    }

    JComboBox<String> cl = new JComboBox<>();

    for (Libro x : disponibles) {
     cl.addItem(
             x.id() + " - " + x.titulo() +
                     " (stock " + x.stock() + ")"
     );
    }

    Object[] f = {
            "Alumno", ca,
            "Libro", cl
    };

    if (JOptionPane.showConfirmDialog(
            p, f, "Nuevo préstamo",
            JOptionPane.OK_CANCEL_OPTION
    ) == JOptionPane.OK_OPTION) {

     int aid = Integer.parseInt(
             ((String) ca.getSelectedItem()).split(" - ")[0]
     );

     int lid = Integer.parseInt(
             ((String) cl.getSelectedItem()).split(" - ")[0]
     );

     servicio.crearPrestamo(aid, lid, usuario.id());

     // Actualizamos préstamos y libros.
     cargar.run();
     refrescarLibros.run();
    }

   } catch (Exception ex) {
    error(ex);
   }
  });

  // Registrar devolución.
  dev.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0) {
    try {
     servicio.devolver((int) t.getValueAt(r, 0));

     // Actualizamos préstamos y libros.
     cargar.run();
     refrescarLibros.run();

    } catch (Exception ex) {
     error(ex);
    }
   } else {
    mensaje("Seleccioná un préstamo.");
   }
  });

  // Eliminar préstamo.
  borrar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0 && confirmar()) {
    try {
     servicio.eliminarPrestamo((int) t.getValueAt(r, 0));

     // Actualizamos préstamos y libros.
     cargar.run();
     refrescarLibros.run();

    } catch (Exception ex) {
     error(ex);
    }
   } else if (r < 0) {
    mensaje("Seleccioná un préstamo.");
   }
  });

  cargar.run();
  return p;
 }

 // ==================== REPORTES ====================

 private static JPanel panelReportes() {
  JPanel p = base();
  JTable t = new JTable();

  JButton b = new JButton("Generar reporte de préstamos por libro");

  p.add(b, BorderLayout.NORTH);
  p.add(new JScrollPane(t));

  b.addActionListener(e -> {
   try {
    DefaultTableModel m = new DefaultTableModel(
            new String[]{
                    "Libro",
                    "Total préstamos (COUNT)",
                    "Activos (SUM)"
            }, 0
    );

    for (String[] x : servicio.reporte()) {
     m.addRow(x);
    }

    t.setModel(m);
   } catch (Exception ex) {
    error(ex);
   }
  });

  return p;
 }

 // ==================== ESTADO DE CONEXIÓN ====================

 private static JPanel panelEstado() {
  JPanel p = base();

  JLabel estado = new JLabel("Estado sin comprobar");
  JButton b = new JButton("Comprobar conexión");

  b.addActionListener(e ->
          estado.setText(
                  Database.comprobar()
                          ? "Conexión con MySQL: OK"
                          : "No se pudo conectar con MySQL"
          )
  );

  p.add(estado, BorderLayout.CENTER);
  p.add(b, BorderLayout.SOUTH);

  return p;
 }

 // ==================== USUARIOS ====================

 private static JPanel panelUsuarios(UsuarioDAO dao) {
  JPanel p = base();
  JTable t = new JTable();

  JButton cargar = new JButton("Actualizar");
  JButton crear = new JButton("Crear usuario");
  JButton activar = new JButton("Activar/desactivar seleccionado");

  JPanel top = new JPanel();
  top.add(cargar);
  top.add(crear);
  top.add(activar);

  p.add(top, BorderLayout.NORTH);
  p.add(new JScrollPane(t));

  Runnable refresh = () -> {
   try {
    DefaultTableModel m = new DefaultTableModel(
            new String[]{
                    "ID", "Nombre", "Documento",
                    "Email", "Usuario", "Rol", "Activo"
            }, 0
    );

    for (Usuario u : dao.listar()) {
     m.addRow(new Object[]{
             u.id(),
             u.nombre(),
             u.documento(),
             u.email(),
             u.username(),
             u.rol(),
             u.activo()
     });
    }

    t.setModel(m);
   } catch (Exception e) {
    error(e);
   }
  };

  cargar.addActionListener(e -> refresh.run());

  crear.addActionListener(e -> {
   JTextField n = new JTextField();
   JTextField d = new JTextField();
   JTextField em = new JTextField();
   JTextField us = new JTextField();

   JPasswordField pw = new JPasswordField();

   JComboBox<String> rol = new JComboBox<>(
           new String[]{"EMPLEADO", "ADMIN"}
   );

   Object[] f = {
           "Nombre", n,
           "Documento", d,
           "Email", em,
           "Usuario", us,
           "Contraseña", pw,
           "Rol", rol
   };

   if (JOptionPane.showConfirmDialog(
           p, f, "Crear usuario",
           JOptionPane.OK_CANCEL_OPTION
   ) == JOptionPane.OK_OPTION) {
    try {
     Validador.requerido(n.getText(), "nombre");
     Validador.email(em.getText());

     if (pw.getPassword().length < 6) {
      throw new ValidacionException(
              "La contraseña debe tener al menos 6 caracteres."
      );
     }

     dao.crear(
             n.getText().trim(),
             d.getText().trim(),
             em.getText().trim(),
             us.getText().trim(),
             new String(pw.getPassword()),
             (String) rol.getSelectedItem()
     );

     refresh.run();

    } catch (Exception ex) {
     error(ex);
    }
   }
  });

  activar.addActionListener(e -> {
   int r = t.getSelectedRow();

   if (r >= 0) {
    try {
     int id = (int) t.getValueAt(r, 0);
     boolean act = (boolean) t.getValueAt(r, 6);

     if (id == usuario.id()) {
      throw new ValidacionException(
              "No podés desactivar tu propio usuario."
      );
     }

     dao.cambiarEstado(id, !act);
     refresh.run();

    } catch (Exception ex) {
     error(ex);
    }
   }
  });

  refresh.run();
  return p;
 }

 // ==================== MENSAJES Y VALIDACIONES ====================

 private static boolean confirmar() {
  return JOptionPane.showConfirmDialog(
          null,
          "¿Confirmás la eliminación?",
          "Confirmar",
          JOptionPane.YES_NO_OPTION
  ) == JOptionPane.YES_OPTION;
 }

 private static void error(Exception e) {
  mensaje(
          e instanceof ValidacionException
                  ? e.getMessage()
                  : "No se pudo completar la operación. " +
                    "Revisá los datos o si existen relaciones asociadas. " +
                    "Detalle: " + e.getMessage()
  );
 }

 private static void mensaje(String s) {
  JOptionPane.showMessageDialog(
          null,
          s,
          "Sistema Biblioteca",
          JOptionPane.INFORMATION_MESSAGE
  );
 }
}
