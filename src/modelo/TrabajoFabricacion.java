package modelo;

import java.time.LocalDate;

public class TrabajoFabricacion {
    //Atributos
    private String numeroTrabajo;
    private LocalDate fecha;
    private String descripcion;

    //Constructor
        public TrabajoFabricacion(String numeroTrabajo, LocalDate fecha, String descripcion,
                                  ProductoTerminado productoTerminado, Usuario usuarioResponsable) {
        this.numeroTrabajo = numeroTrabajo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.productoTerminado = productoTerminado;
    }

    //Get y Set


}
