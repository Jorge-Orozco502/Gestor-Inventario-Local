package dao;

import conexion.conexionBD;
import modelo.Producto;
import modelo.ProductoTerminado;
import modelo.EstadoProducto;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    //CREATE
    public boolean insertarProducto(Producto producto){
        String sql = "INSERT INTO producto (codigo, nombre, categoria, descripcion, unidad_medida, precio_compra, precio_venta, existencia_actual, existencia_minima, estado_producto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)){

            if (conexion == null) return false;

            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getCategoria());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setString(5, producto.getUnidadMedida());
            pstmt.setBigDecimal(6, producto.getPrecioCompra());
            pstmt.setBigDecimal(7, producto.getPrecioVenta());
            pstmt.setDouble(8, producto.getExistenciaActual());
            pstmt.setDouble(9, producto.getExistenciaMinima());
            pstmt.setString(10, producto.getEstadoProducto().name());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println("Error al registrar el producto:" +e.getMessage());
            return false;
        }
    }

//READ
    public List<Producto> listarProductos(){
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection conexion = conexionBD.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String descripcion = rs.getString("descripcion");
                String unidadMedida = rs.getString("unidad_medida");
                BigDecimal precioCompra = rs.getBigDecimal("precio_compra");
                BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
                double existenciaActual = rs.getDouble("existencia_actual");
                double existenciaMinima = rs.getDouble("existencia_minima");
                EstadoProducto estadoProducto = EstadoProducto.valueOf(rs.getString("estado_producto"));

                Producto producto = new ProductoTerminado(
                        codigo, nombre, categoria, descripcion, unidadMedida,
                        precioCompra, precioVenta, existenciaActual, existenciaMinima, estadoProducto
                );
                listaProductos.add(producto);
            }
        } catch (SQLException e){
            System.out.println("Error al listar los productos:" + e.getMessage());
        }
        return listaProductos;
    }

 //UPDTATE
 public boolean actualizarProducto(Producto producto){
        String sql = "UPDATE producto SET nombre = ?, categoria = ?, descripcion = ?, unidad_medida = ?, precio_compra = ?, precio_venta = ?, existencia_actual = ?, existencia_minima = ?, estado_producto = ? WHERE codigo = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)){

            if(conexion == null) return false;

            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setString(4, producto.getUnidadMedida());
            pstmt.setBigDecimal(5, producto.getPrecioCompra());
            pstmt.setBigDecimal(6, producto.getPrecioVenta());
            pstmt.setDouble(7, producto.getExistenciaActual());
            pstmt.setDouble(8, producto.getExistenciaMinima());
            pstmt.setString(9, producto.getEstadoProducto().name());
            pstmt.setString(10, producto.getCodigo());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e){
            System.out.println("Error al actualizar el producto:" + e.getMessage());
            return false;
        }
 }

//DELETE
    public boolean eliminarProducto(String codigoProducto){
        String sql = "DELETE FROM producto WHERE codigo = ?";

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)){

            if (conexion == null) return false;

            pstmt.setString(1, codigoProducto);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e){
            System.out.println("Error al eliminar el producto:" + e.getMessage());
            return false;
        }
    }
}
