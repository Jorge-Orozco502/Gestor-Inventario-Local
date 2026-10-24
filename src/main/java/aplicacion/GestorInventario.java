
package aplicacion;
import modelo.*;
import dao.UsuarioDAO;

public class GestorInventario {


    public static void main(String[] args) {
 
    
            java.awt.EventQueue.invokeLater(()->
        new ui.FrmMenuInicio().setVisible(true)
        );
        
        new UsuarioDAO().probarHash();
        new UsuarioDAO().probarBCrypt();
        
    
    }
    
    
    
}
