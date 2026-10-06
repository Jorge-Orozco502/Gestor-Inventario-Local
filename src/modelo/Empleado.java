package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Empleado extends Usuario {

    private List<String> accionesPermitidas;

    /**
     * Constructor
     *
     * @param nombreUsuario     El nombre del usuario empleado
     * @param contrasenaHash    Es la contraseña cifrada del empleado
     * @param nombreCompleto    Es el nombre completo del empleado
     */
    public Empleado(String nombreUsuario, String contrasenaHash,String nombreCompleto){
        super(nombreUsuario, contrasenaHash, nombreCompleto);
        this.accionesPermitidas = new ArrayList<>();
        inicializarPermisosPorDefecto();
    }

    //Constructor vacío
    public Empleado(){
        super();
        this.accionesPermitidas = new ArrayList<>();
        inicializarPermisosPorDefecto();
    }

    //Listado de acciones permitidas para el Empleado
    private void inicializarPermisosPorDefecto(){
        this.accionesPermitidas.add("REGISTRAR_VENTAS");
        this.accionesPermitidas.add("CONSULTAR_INVENTARIO");
        this.accionesPermitidas.add("GESTIONAR_CLIENTES");
        this.accionesPermitidas.add("CONSULTAR_PRODUCTOS");
    }

    //GET y SET
    public List<String> getAccionesPermitidas() {
        return Collections.unmodifiableList(accionesPermitidas);
    }

    public void setAccionesPermitidas(List<String> accionesPermitidas) {
        Objects.requireNonNull(accionesPermitidas, "La lista de acciones permitidas no puede ser nula.");
        this.accionesPermitidas = new ArrayList<>(accionesPermitidas);
    }

    //=======================================

    /**
     * Verificar si el empleado tiene alguna acción permitida
     *
     * @param accion    Es la accion a verificar
     * @return true      Devolvera este valor si la acción está permitida
     */
    public boolean tienePermiso(String accion){
        Objects.requireNonNull(accion, "La acción a verificar no puede ser nula.");
        return this.accionesPermitidas.contains(accion.trim().toUpperCase());
    }

    //Metodo toString
    @Override
    public String toString(){
        return "Empleado{" +
                "nombreUsuario='" + getNombreUsuario() + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", totalAccionesPermitidas=" + accionesPermitidas.size() +
                '}';
    }

}
