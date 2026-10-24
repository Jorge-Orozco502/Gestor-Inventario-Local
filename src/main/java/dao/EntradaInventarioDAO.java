package dao;

import conexion.conexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EntradaInventarioDAO {

    public double registrarEntrada(
            String codigoProducto,
            double cantidad,
            int idUsuario,
            String referencia,
            String motivo) throws SQLException {

        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona un producto.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (idUsuario <= 0) {
            throw new IllegalArgumentException(
                    "Selecciona un usuario responsable."
            );
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Escribe el motivo de la entrada."
            );
        }

        String consultaStock
                = "SELECT existencia_actual "
                + "FROM producto "
                + "WHERE codigo = ? "
                + "FOR UPDATE";

        String actualizarStock
                = "UPDATE producto "
                + "SET existencia_actual = ? "
                + "WHERE codigo = ?";

        String insertarMovimiento
                = "INSERT INTO movimiento_inventario "
                + "(fecha, codigo_producto, tipo_movimiento, cantidad, "
                + "existencia_anterior, existencia_nueva, id_usuario, "
                + "referencia_documento, motivo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = conexionBD.obtenerConexion()) {
            if (cn == null) {
                throw new SQLException(
                        "No se pudo conectar con la base de datos."
                );
            }

            boolean autoCommitOriginal = cn.getAutoCommit();

            try {
                cn.setAutoCommit(false);

                double existenciaAnterior;

                try (PreparedStatement ps
                        = cn.prepareStatement(consultaStock)) {

                    ps.setString(1, codigoProducto.trim());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se encontró el producto seleccionado."
                            );
                        }

                        existenciaAnterior
                                = rs.getDouble("existencia_actual");
                    }
                }

                double existenciaNueva
                        = existenciaAnterior + cantidad;

                try (PreparedStatement ps
                        = cn.prepareStatement(actualizarStock)) {

                    ps.setDouble(1, existenciaNueva);
                    ps.setString(2, codigoProducto.trim());

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo actualizar la existencia del producto."
                        );
                    }
                }

                try (PreparedStatement ps
                        = cn.prepareStatement(insertarMovimiento)) {

                    ps.setTimestamp(
                            1,
                            Timestamp.valueOf(LocalDateTime.now())
                    );
                    ps.setString(2, codigoProducto.trim());
                    ps.setString(3, "ENTRADA_AJUSTE");
                    ps.setDouble(4, cantidad);
                    ps.setDouble(5, existenciaAnterior);
                    ps.setDouble(6, existenciaNueva);
                    ps.setInt(7, idUsuario);

                    String referenciaLimpia
                            = referencia == null ? "" : referencia.trim();

                    ps.setString(
                            8,
                            referenciaLimpia.isEmpty()
                            ? null
                            : referenciaLimpia
                    );
                    ps.setString(9, motivo.trim());

                    ps.executeUpdate();
                }

                cn.commit();
                return existenciaNueva;

            } catch (SQLException | RuntimeException ex) {
                cn.rollback();
                throw ex;

            } finally {
                cn.setAutoCommit(autoCommitOriginal);
            }
        }
    }

}
