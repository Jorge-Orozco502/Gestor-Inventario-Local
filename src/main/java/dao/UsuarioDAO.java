package dao;

import conexion.conexionBD;
import modelo.Usuario;
import modelo.Administrador;
import modelo.Empleado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    // CREATE
    public boolean insertarUsuario(Usuario usuario, String tipoUsuario) {
        if (usuario == null) {
            System.out.println("Error: El objeto usuario no puede ser nulo.");
            return false;
        }
        if (tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            System.out.println("Error: El tipo de usuario es obligatorio.");
            return false;
        }

        String sql = "INSERT INTO usuario (tipo_usuario, nombre_usuario, contrasena_hash, nombre_completo, activo, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (conexion == null) return false;

            pstmt.setString(1, tipoUsuario.toUpperCase());
            pstmt.setString(2, usuario.getNombreUsuario());
            pstmt.setString(3, usuario.getContrasenaHash());
            pstmt.setString(4, usuario.getNombreCompleto());
            pstmt.setBoolean(5, usuario.isActivo());
            pstmt.setDate(6, java.sql.Date.valueOf(usuario.getFechaCreacion() != null ? usuario.getFechaCreacion() : LocalDate.now()));

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Usuario> listarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection conexion = conexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String tipoUsuario = rs.getString("tipo_usuario");
                String nombreUsuario = rs.getString("nombre_usuario");
                String contrasenaHash = rs.getString("contrasena_hash");
                String nombreCompleto = rs.getString("nombre_completo");
                boolean activo = rs.getBoolean("activo");
                Date fechaSql = rs.getDate("fecha_creacion");
                LocalDate fechaCreacion = fechaSql != null ? fechaSql.toLocalDate() : LocalDate.now();

                Usuario usuario;
                if ("ADMINISTRADOR".equalsIgnoreCase(tipoUsuario)) {
                    usuario = new Administrador(nombreUsuario, contrasenaHash, nombreCompleto);
                } else {
                    usuario = new Empleado(nombreUsuario, contrasenaHash, nombreCompleto);
                }

                usuario.setIdUsuario(idUsuario);
                usuario.setActivo(activo);
                usuario.setFechaCreacion(fechaCreacion);

                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    }

    //Metodo buscar a un usuario especifico por su Id
    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (conexion == null) return null;
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tipoUsuario = rs.getString("tipo_usuario");
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String contrasenaHash = rs.getString("contrasena_hash");
                    String nombreCompleto = rs.getString("nombre_completo");
                    boolean activo = rs.getBoolean("activo");
                    Date fechaSql = rs.getDate("fecha_creacion");
                    LocalDate fechaCreacion = fechaSql != null ? fechaSql.toLocalDate() : LocalDate.now();

                    Usuario usuario;
                    if ("ADMINISTRADOR".equalsIgnoreCase(tipoUsuario)) {
                        usuario = new Administrador(nombreUsuario, contrasenaHash, nombreCompleto);
                    } else {
                        usuario = new Empleado(nombreUsuario, contrasenaHash, nombreCompleto);
                    }

                    usuario.setIdUsuario(idUsuario);
                    usuario.setActivo(activo);
                    usuario.setFechaCreacion(fechaCreacion);

                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario por ID: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Error: El objeto usuario a actualizar no puede ser nulo.");
            return false;
        }

        String sql = "UPDATE usuario SET nombre_usuario = ?, contrasena_hash = ?, nombre_completo = ?, activo = ? WHERE id_usuario = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (conexion == null) return false;

            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasenaHash());
            pstmt.setString(3, usuario.getNombreCompleto());
            pstmt.setBoolean(4, usuario.isActivo());
            pstmt.setInt(5, usuario.getIdUsuario());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (conexion == null) return false;

            pstmt.setInt(1, idUsuario);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
        }
    }
}
