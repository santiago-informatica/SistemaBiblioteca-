package uy.utu.biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public List<Alumno> listar(String filtro) throws SQLException {

        List<Alumno> alumnos = new ArrayList<>();

        String sql = """
            SELECT id, nombre, documento, email, grupo
            FROM alumnos
            WHERE nombre LIKE ?
               OR documento LIKE ?
               OR grupo LIKE ?
            ORDER BY nombre
            """;

        try (Connection conexion = Database.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            String buscar = "%" + filtro + "%";

            consulta.setString(1, buscar);
            consulta.setString(2, buscar);
            consulta.setString(3, buscar);

            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    alumnos.add(new Alumno(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("documento"),
                            resultado.getString("email"),
                            resultado.getString("grupo")
                    ));
                }
            }
        }

        return alumnos;
    }

    public void crear(Alumno alumno) throws SQLException {

        String sql = """
            INSERT INTO alumnos(nombre, documento, email, grupo)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexion = Database.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, alumno.getNombre());
            consulta.setString(2, alumno.getDocumento());
            consulta.setString(3, alumno.getEmail());
            consulta.setString(4, alumno.getGrupo());

            consulta.executeUpdate();
        }
    }

    public void actualizar(Alumno alumno) throws SQLException {

        String sql = """
            UPDATE alumnos
            SET nombre = ?, documento = ?, email = ?, grupo = ?
            WHERE id = ?
            """;

        try (Connection conexion = Database.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, alumno.getNombre());
            consulta.setString(2, alumno.getDocumento());
            consulta.setString(3, alumno.getEmail());
            consulta.setString(4, alumno.getGrupo());
            consulta.setInt(5, alumno.getId());

            consulta.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM alumnos WHERE id = ?";

        try (Connection conexion = Database.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, id);
            consulta.executeUpdate();
        }
    }
}