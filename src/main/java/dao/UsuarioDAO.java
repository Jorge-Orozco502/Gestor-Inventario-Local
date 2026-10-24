package dao;

import conexion.conexionBD;
import modelo.Usuario;
import modelo.Administrador;
import modelo.Empleado;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // CREATE (Inserta el usuario y cifra la contraseña automáticamente con BCrypt)
    public boolean insertarUsuario(Usuario usuario, String tipoUsuario) {
    if (usuario == null || tipoUsuario == null ||
        tipoUsuario.trim().isEmpty()) {
        return false;
    }

    String passwordPlano = usuario.getContrasenaHash();
    System.out.println("DEBUG: contraseña recibida: " + passwordPlano);

    if (passwordPlano == null || passwordPlano.isEmpty()) {
        return false;
    }

    // Generar el hash con jBCrypt
    String passwordHash = BCrypt.hashpw(
        passwordPlano,
        BCrypt.gensalt(10)
    );

    String sql = "INSERT INTO usuario "
        + "(tipo_usuario, nombre_usuario, contrasena_hash, "
        + "nombre_completo, activo, fecha_creacion) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = conexionBD.obtenerConexion()) {

        if (conexion == null) {
            System.out.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, tipoUsuario.trim().toUpperCase());
            pstmt.setString(2, usuario.getNombreUsuario());
            pstmt.setString(3, passwordHash);
            pstmt.setString(4, usuario.getNombreCompleto());
            pstmt.setBoolean(5, usuario.isActivo());

            LocalDate fecha = usuario.getFechaCreacion() != null
                ? usuario.getFechaCreacion()
                : LocalDate.now();

            pstmt.setDate(6, java.sql.Date.valueOf(fecha));

            int filasAfectadas = pstmt.executeUpdate();

            return filasAfectadas > 0;
        }

    } catch (SQLException e) {
        System.out.println(
            "Error al registrar el usuario: " + e.getMessage()
        );
        return false;
    }
}

    // READ (Listar todos)
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

    // READ (Buscar por ID)
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

    // UPDATE (Actualiza el usuario asegurando que si cambia la contraseña, también se cifre)
    public boolean actualizarUsuario(Usuario usuario) {
    if (usuario == null) {
        return false;
    }

    String passwordIngresada = usuario.getContrasenaHash();

    String sql;

    if (passwordIngresada != null && !passwordIngresada.isEmpty()) {
        // Se ingresó una contraseña nueva.
        sql = "UPDATE usuario SET "
            + "tipo_usuario = ?, "
            + "nombre_usuario = ?, "
            + "contrasena_hash = ?, "
            + "nombre_completo = ?, "
            + "activo = ? "
            + "WHERE id_usuario = ?";
    } else {
        // Campo vacío: conservar la contraseña actual.
        sql = "UPDATE usuario SET "
            + "tipo_usuario = ?, "
            + "nombre_usuario = ?, "
            + "nombre_completo = ?, "
            + "activo = ? "
            + "WHERE id_usuario = ?";
    }

    try (Connection conexion = conexionBD.obtenerConexion()) {
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            int posicion = 1;

            String tipoUsuario;

            if (usuario instanceof Administrador) {
                tipoUsuario = "ADMINISTRADOR";
            } else if (usuario instanceof Empleado) {
                tipoUsuario = "EMPLEADO";
            } else {
                return false;
            }

            pstmt.setString(posicion++, tipoUsuario);
            pstmt.setString(posicion++, usuario.getNombreUsuario());

            if (passwordIngresada != null && !passwordIngresada.isEmpty()) {
                String passwordHash = BCrypt.hashpw(
                    passwordIngresada,
                    BCrypt.gensalt(10)
                );

                pstmt.setString(posicion++, passwordHash);
            }

            pstmt.setString(posicion++, usuario.getNombreCompleto());
            pstmt.setBoolean(posicion++, usuario.isActivo());
            pstmt.setInt(posicion, usuario.getIdUsuario());

            return pstmt.executeUpdate() > 0;
        }

    } catch (SQLException e) {
        System.out.println(
            "Error al actualizar el usuario: " + e.getMessage()
        );
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
    
    // VALIDAR USUARIO (Compara la contraseña plana escrita en el login contra el hash de la BD de forma segura)
    
public boolean validarUsuario(String nombreUsuario, String contrasenaIngresada) {
    String sql = "SELECT contrasena_hash FROM usuario "
               + "WHERE nombre_usuario = ? AND activo = 1";

    if (nombreUsuario == null || contrasenaIngresada == null) {
        return false;
    }

    try (Connection conexion = conexionBD.obtenerConexion()) {
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                String hashAlmacenado =
                    rs.getString("contrasena_hash");

                if (hashAlmacenado == null ||
                    hashAlmacenado.isEmpty()) {
                    return false;
                }

                boolean coincide = BCrypt.checkpw(
                    contrasenaIngresada,
                    hashAlmacenado
                );

                System.out.println(
                    "Contraseña correcta: " + coincide
                );

                return coincide;
            }
        }
    } catch (SQLException e) {
        System.out.println(
            "Error al validar usuario: " + e.getMessage()
        );
        return false;
    } catch (IllegalArgumentException e) {
        System.out.println(
            "Hash BCrypt inválido: " + e.getMessage()
        );
        return false;
    }
}
public void probarHash() {
    String hash = "$2a$10$yMQIyYT.93tcDUORoAu2WO/GcSdCC5/4BUDJxaxhRsqYmCcLSZl3.";

    System.out.println(
        "¿La contraseña es jos?: " +
        BCrypt.checkpw("jos", hash)
    );
}
public void probarBCrypt() {
    String contrasena = "jos";

    String hash = BCrypt.hashpw(
        contrasena,
        BCrypt.gensalt(10)
    );

    System.out.println("Hash generado: " + hash);
    System.out.println(
        "¿Coincide con jos?: " +
        BCrypt.checkpw(contrasena, hash)
    );
}
public String obtenerTipoUsuario(String nombreUsuario, String contrasena) {
    String sql = "SELECT tipo_usuario FROM usuario "
               + "WHERE nombre_usuario = ? AND activo = TRUE";

    try (Connection conexion = conexionBD.obtenerConexion()) {
        if (conexion == null) {
            return null;
        }

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tipoUsuario = rs.getString("tipo_usuario");

                    if (validarUsuario(nombreUsuario, contrasena)) {
                        return tipoUsuario;
                    }
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener el tipo de usuario: "
                + e.getMessage());
    }

    return null;
}


}