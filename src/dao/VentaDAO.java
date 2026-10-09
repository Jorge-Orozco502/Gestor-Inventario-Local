package dao;

import conexion.conexionBD;
import modelo.Venta;
import modelo.DetalleVenta;
import modelo.FormaPago;
import modelo.Cliente;
import modelo.Usuario;
import modelo.Administrador;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    //CREATE
    public boolean insertarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO venta (numero_venta, fecha, codigo_cliente, id_usuario_responsable, forma_pago, subtotal_general, total_descuentos, total_iva, total_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (numero_venta, codigo_producto, cantidad_solicitada, precio_unitario, subtotal, iva_linea, total_linea) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = null;

        try {
            conexion = conexionBD.obtenerConexion();
            if (conexion == null) return false;
            conexion.setAutoCommit(false);

            try (PreparedStatement pstmtVenta = conexion.prepareStatement(sqlVenta)) {
                pstmtVenta.setString(1, venta.getNumeroVenta());
                pstmtVenta.setDate(2, java.sql.Date.valueOf(venta.getFecha()));
                pstmtVenta.setString(3, venta.getCliente() != null ? venta.getCliente().getCodigoCliente() : "CF");
                pstmtVenta.setInt(4, venta.getUsuarioResponsable() != null ? venta.getUsuarioResponsable().getIdUsuario() : 1);
                pstmtVenta.setString(5, venta.getFormaPago().name());
                pstmtVenta.setBigDecimal(6, venta.getSubtotalGeneral());
                pstmtVenta.setBigDecimal(7, venta.getTotalDescuentos());
                pstmtVenta.setBigDecimal(8, venta.getTotalIva());
                pstmtVenta.setBigDecimal(9, venta.getTotalFinal());
                pstmtVenta.executeUpdate();
            }

            if (venta.getDetallesVenta() != null && !venta.getDetallesVenta().isEmpty()) {
                try (PreparedStatement pstmtDetalle = conexion.prepareStatement(sqlDetalle)) {
                    for (DetalleVenta detalle : venta.getDetallesVenta()) {
                        pstmtDetalle.setString(1, venta.getNumeroVenta());
                        pstmtDetalle.setString(2, detalle.getProducto() != null ? detalle.getProducto().getCodigo() : "");
                        pstmtDetalle.setDouble(3, detalle.getCantidadSolicitada());
                        pstmtDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                        pstmtDetalle.setBigDecimal(5, detalle.getSubtotal());
                        pstmtDetalle.setBigDecimal(6, detalle.getIvaLinea());
                        pstmtDetalle.setBigDecimal(7, detalle.getTotalLinea());
                        pstmtDetalle.addBatch();
                    }
                    pstmtDetalle.executeBatch();
                }
            }
            conexion.commit();
            return true;
        } catch (SQLException e){
            System.out.println("Error al registrar la venta (Rollback ejecutado): " + e.getMessage());
            if (conexion != null){
                try {
                    conexion.rollback(); //revetir cambios por si hay fallo
                } catch (SQLException ex){
                    System.out.println("Error al realizar rollback:" + ex.getMessage());
                }
            }
        } finally {
            if(conexion != null){
                try{
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e){
                    System.out.println("Error al cerrar la conexión:" + e.getMessage());
                }
            }
        }
        return false;
    }

    //READ
    public List<Venta> listarVentas(){
        List<Venta> listaVentas = new ArrayList<>();
        String sql = "SELECT * FROM venta";

        try(Connection conexion = conexionBD.obtenerConexion();
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                String numVenta = rs.getString("numero_venta");
                Date fechaSql= rs.getDate("fecha");
                LocalDate fecha = fechaSql != null ? fechaSql.toLocalDate() : LocalDate.now();

                BigDecimal subtotalGeneral = rs.getBigDecimal("subtotal_general");
                BigDecimal totalDescuentos = rs.getBigDecimal("total_descuentos");
                BigDecimal totalIva = rs.getBigDecimal("total_iva");
                BigDecimal totalFinal = rs.getBigDecimal("total_final");

                FormaPago formaPago = FormaPago.valueOf(rs.getString("forma_pago"));

                Cliente cliente = new Cliente();
                cliente.setCodigoCliente(rs.getString("codigo_cliente"));

                Usuario usuarioTemp = new Administrador("sistema", "hash", "Usuario Sistema");
                usuarioTemp.setIdUsuario(rs.getInt("id_usuario_responsable"));

                //instancia y asigna valores recuperados
                Venta venta = new Venta(
                        numVenta,
                        fecha,
                        formaPago,
                        subtotalGeneral,
                        totalDescuentos,
                        totalIva,
                        totalFinal,
                        cliente,
                        usuarioTemp
                );

                listaVentas.add(venta);
                System.out.println("Venta encontrada -> No:" + numVenta + ",Fecha:" + fecha + ",Total:" + totalFinal);
            }
        } catch (SQLException e){
            System.out.println("Error al listar las ventas:" + e.getMessage());
        }
        return listaVentas;
    }

    //UPDATE
    public boolean actualizarVenta(Venta venta){
        String sql = "UPDATE venta SET forma_pago = ?, subtotal_general = ?, total_descuentos = ?, total_iva = ?, total_final = ? WHERE numero_venta = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
            PreparedStatement pstmt = conexion.prepareStatement(sql)){
            pstmt.setString(1, venta.getFormaPago().name());
            pstmt.setBigDecimal(2, venta.getSubtotalGeneral());
            pstmt.setBigDecimal(3, venta.getTotalDescuentos());
            pstmt.setBigDecimal(4, venta.getTotalIva());
            pstmt.setBigDecimal(5, venta.getTotalFinal());
            pstmt.setString(6, venta.getNumeroVenta());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e){
            System.out.println("Error al actualizar la venta:" + e.getMessage());
            return false;
        }
    }

    //DELETE
    public boolean eliminarVenta(String numeroVenta){
        String sqlDetalles = "DELETE FROM detalle_venta WHERE numero_venta= ?";
        String sqlVenta = "DELETE FROM venta WHERE numero_venta = ?";

        Connection conexion = null;
        try{
            conexion = conexionBD.obtenerConexion();
            if(conexion == null) return false;
            conexion.setAutoCommit(false);

            try(PreparedStatement pstmtDetalles = conexion.prepareStatement(sqlDetalles)){
                pstmtDetalles.setString(1, numeroVenta);
                pstmtDetalles.executeUpdate();
            }

            try(PreparedStatement pstmtVenta = conexion.prepareStatement(sqlVenta)){
                pstmtVenta.setString(1, numeroVenta);
                pstmtVenta.executeUpdate();
            }
            conexion.commit();
            return true;
        } catch (SQLException e){
            System.out.println("Error al eliminar la venta (Rollback ejecutado):" + e.getMessage());
            if (conexion != null){
                try{
                    conexion.rollback();
                } catch (SQLException ex){
                    System.out.println("Error al hacer rollback:" + ex.getMessage());
                }
            }
        } finally {
            if (conexion != null){
                try{
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e){
                    System.out.println("Error al cerrar conexion:" + e.getMessage());
                }
            }
        }
        return false;
    }
}
