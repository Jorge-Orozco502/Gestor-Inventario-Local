package dao;

import conexion.conexionBD;
import modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

  //CREATE
  public boolean insertarCliente(Cliente cliente){
      String sql = "INSERT INTO cliente (codigo, nombre, nit_cf, telefono, direccion) VALUES (?, ?, ?, ?, ?)";

      try (Connection conexion = conexionBD.obtenerConexion();
           PreparedStatement pstmt = conexion.prepareStatement(sql)){

          if ( conexion == null) return false;

          pstmt.setString(1, cliente.getCodigoCliente());
          pstmt.setString(2, cliente.getNombreCliente());
          pstmt.setString(3, cliente.getNitCF());
          pstmt.setString(4, cliente.getTelefono());
          pstmt.setString(5, cliente.getDireccion());
          pstmt.executeUpdate();
          return true;
      } catch (SQLException e){
          System.out.println("Error al registrar el cliente:" + e.getMessage());
          return false;
      }
  }

  //READ
  public List<Cliente> listarClientes(){
      List<Cliente> listaClientes = new ArrayList<>();
      String sql = "SELECT * FROM cliente";

      try (Connection conexion = conexionBD.obtenerConexion();
           Statement stmt = conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql)){

          while (rs.next()){
              String codigo = rs.getString("codigo");
              String nombre = rs.getString("nombre");
              String nitCf = rs.getString("nit_cf");
              String telefono = rs.getString("telefono");
              String direccion = rs.getString("direccion");

              Cliente cliente = new Cliente(codigo, nombre, telefono, nitCf, direccion);
              listaClientes.add(cliente);
          }
      } catch (SQLException e){
          System.out.println("Error al listar los clientes:" + e.getMessage());
      }
      return listaClientes;
  }

//UPDATE
    public boolean actualizarCliente(Cliente cliente){
      String sql = "UPDATE cliente SET nombre = ?, nit_cf = ?, telefono = ?, direccion = ? WHERE codigo = ?";

      try (Connection conexion = conexionBD.obtenerConexion();
           PreparedStatement pstmt = conexion.prepareStatement(sql)){

          if (conexion == null) return false;

          pstmt.setString(1, cliente.getNombreCliente());
          pstmt.setString(2, cliente.getNitCF());
          pstmt.setString(3, cliente.getTelefono());
          pstmt.setString(4, cliente.getDireccion());
          pstmt.setString(5, cliente.getCodigoCliente());

          int filasAfectadas = pstmt.executeUpdate();
          return filasAfectadas > 0;
      } catch (SQLException e){
          System.out.println("Error al actualizar el cliente:" + e.getMessage());
          return false;
      }
    }

  //DELETE
  public boolean eliminarCliente(String codigoCliente){
      String sql = "DELETE FROM cliente WHERE codigo = ?";

      try (Connection conexion = conexionBD.obtenerConexion();
           PreparedStatement pstmt = conexion.prepareStatement(sql)){

          if (conexion == null) return false;

          pstmt.setString(1, codigoCliente);
          int  filasAfectadas = pstmt.executeUpdate();
          return filasAfectadas > 0;
      } catch (SQLException e){
          System.out.println("Error al eliminar el cliente:" + e.getMessage());
          return false;
      }
  }
}
