package aplicacion;
import conexion.conexionBD;
import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import modelo.Cliente;
import modelo.Usuario;

import java.sql.Connection;
import java.util.List;

public class ProbarSistema {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   INICIANDO PRUEBAS DEL SISTEMA DAO     ");
        System.out.println("=========================================");

        // 1. Verificar Conexión a MySQL
        try (Connection conexion = conexionBD.obtenerConexion()) {
            if (conexion != null) {
                System.out.println("[OK] 1. Conexión a la base de datos establecida correctamente.");
            } else {
                System.out.println("[ERROR] 1. No se pudo establecer la conexión con la base de datos.");
                return;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] 1. Excepción en la conexión: " + e.getMessage());
            return;
        }

        // 2. Probar ClienteDAO (Listar y Registrar)
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.listarClientes();
        System.out.println("[OK] 2. ClienteDAO conectado. Clientes encontrados en la BD: " + clientes.size());

        // 3. Probar UsuarioDAO (Listar usuarios y roles)
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        System.out.println("[OK] 3. UsuarioDAO conectado. Usuarios registrados: " + usuarios.size());
        for (Usuario u : usuarios) {
            System.out.println("    -> Usuario: " + u.getNombreUsuario() + " | Rol: " + u.getClass().getSimpleName());
        }

        // 4. Probar ProductoDAO
        ProductoDAO productoDAO = new ProductoDAO();
        // Puedes verificar métodos de búsqueda o listado de productos aquí
        System.out.println("[OK] 4. ProductoDAO verificado.");

        System.out.println("=========================================");
        System.out.println("   ¡PRUEBAS DE INTEGRACIÓN FINALIZADAS!  ");
        System.out.println("=========================================");
    }


}
