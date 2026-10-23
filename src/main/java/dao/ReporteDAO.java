
package dao;

import conexion.conexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    
        public List<Object[]> ventas(
            LocalDate desde,
            LocalDate hasta,
            String formaPago) throws SQLException {

        String sql = "SELECT v.numero_venta, v.fecha, c.nombre AS cliente, "
                + "v.subtotal_general, v.total_descuentos, v.total_iva, v.total_final "
                + "FROM venta v "
                + "INNER JOIN cliente c ON c.codigo = v.codigo_cliente "
                + "WHERE v.fecha BETWEEN ? AND "
                + (formaPago.equalsIgnoreCase("TODOS")
                    ? "?"
                    : "? AND v.forma_pago = ?")
                + " ORDER BY v.fecha DESC, v.numero_venta DESC";

        List<Object[]> filas = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion()) {
            if (cn == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(desde));
                ps.setDate(2, Date.valueOf(hasta));

                if (!formaPago.equalsIgnoreCase("TODOS")) {
                    ps.setString(3, formaPago);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        filas.add(new Object[]{
                            rs.getString("numero_venta"),
                            rs.getDate("fecha"),
                            rs.getString("cliente"),
                            rs.getBigDecimal("subtotal_general"),
                            rs.getBigDecimal("total_descuentos"),
                            rs.getBigDecimal("total_iva"),
                            rs.getBigDecimal("total_final"),
                            "Registrada"
                        });
                    }
                }
            }
        }
        return filas;
    }

    public List<Object[]> productosMasVendidos(
            LocalDate desde,
            LocalDate hasta) throws SQLException {

        String sql = "SELECT p.nombre AS producto, "
                + "SUM(d.cantidad_solicitada) AS cantidad, "
                + "SUM(d.total_linea) AS total "
                + "FROM detalle_venta d "
                + "INNER JOIN venta v ON v.numero_venta = d.numero_venta "
                + "INNER JOIN producto p ON p.codigo = d.codigo_producto "
                + "WHERE v.fecha BETWEEN ? AND ? "
                + "GROUP BY p.codigo, p.nombre "
                + "ORDER BY cantidad DESC, total DESC";

        List<Object[]> filas = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion()) {
            if (cn == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(desde));
                ps.setDate(2, Date.valueOf(hasta));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        filas.add(new Object[]{
                            rs.getString("producto"),
                            rs.getBigDecimal("cantidad"),
                            rs.getBigDecimal("total")
                        });
                    }
                }
            }
        }

        return filas;
    }

    public List<Object[]> actividad(
            LocalDate desde,
            LocalDate hasta) throws SQLException {

        String sql = "SELECT m.fecha, m.tipo_movimiento, u.nombre_completo "
                + "FROM movimiento_inventario m "
                + "INNER JOIN usuario u ON u.id_usuario = m.id_usuario "
                + "WHERE m.fecha >= ? "
                + "AND m.fecha < DATE_ADD(?, INTERVAL 1 DAY) "
                + "ORDER BY m.fecha DESC";

        List<Object[]> filas = new ArrayList<>();

        try (Connection cn = conexionBD.obtenerConexion()) {
            if (cn == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(desde));
                ps.setDate(2, Date.valueOf(hasta));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        filas.add(new Object[]{
                            rs.getTimestamp("fecha"),
                            rs.getString("tipo_movimiento"),
                            rs.getString("nombre_completo")
                        });
                    }
                }
            }
        }

        return filas;
    }
    
    
}
