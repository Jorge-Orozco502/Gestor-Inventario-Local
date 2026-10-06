package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Administrador extends Usuario {

    private List<String> accionesPermitidas;

    /**
     * Constructor
     * @param nombreUsuario     Es el nombre de usuario para el acceso.
     * @param contrasenaHash    La contraseña cifrada del administrador.
     * @param nombreCompleto     Es el nombre completo del administrador.
     */
    public Administrador(String nombreUsuario, String contrasenaHash, String  nombreCompleto){
        super(nombreUsuario, contrasenaHash, nombreCompleto);
        this.accionesPermitidas = new ArrayList<>();
        inicializarPermisosPorDefecto();
    }

    /**
     * Constructor vacío
     */
    public Administrador(){
        super();
        this.accionesPermitidas = new ArrayList<>();
        inicializarPermisosPorDefecto();
    }


    //Lista de acciones permitidas que están predeterminadas para el usuario Administrador
    private void inicializarPermisosPorDefecto(){
        this.accionesPermitidas.add("GESTIONAR_PRODUCTOS");
        this.accionesPermitidas.add("GESTIONAR_PROVEEDORES");
        this.accionesPermitidas.add("GESTIONAR_CLIENTES");
        this.accionesPermitidas.add("REGISTRAR_COMPRAS");
        this.accionesPermitidas.add("REGISTRAR_VENTAS");
        this.accionesPermitidas.add("CONSULTAR_REPORTES");
        this.accionesPermitidas.add("GESTIONAR_USUARIOS");
        this.accionesPermitidas.add("REALIZAR_MODIFICACIONES");
    }

    /**
     * Verificar si el Administrador tiene alguna acción permitida
     *
     * @param accion Es la acción a consultar
     * @return true Devuelve este valor si la acción está permitida
     */
    public boolean tienePermiso(String accion){
        Objects.requireNonNull(accion, "La acción a verificar no puede ser nula.");
        return this.accionesPermitidas.contains(accion.trim().toUpperCase());
    }

    //GET y SET
    public List<String> getAccionesPermitidas(){
        return Collections.unmodifiableList(accionesPermitidas);
    }

    public void setAccionesPermitidas(List<String> accionesPermitidas){
        Objects.requireNonNull(accionesPermitidas, "La lista de acciones permitidas no puede ser nula.");
        this.accionesPermitidas = new ArrayList<>(accionesPermitidas);
    }

    //Métodos toString
    @Override
    public String toString(){
        return "Administrador{" +
                "nombreUsuario='" + getNombreUsuario() + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", totalAccionesPermitidas=" + accionesPermitidas.size() +
                '}';
    }

}
