package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrabajoFabricacion {
    //Atributos
    private String numeroTrabajo;
    private LocalDate fecha;
    private String descripcion;
    //Asociación y composición
    private ProductoTerminado productoTerminado;
    private Usuario usuarioResponsable;
    private List<MaterialUtilizado> detalleMateriales;

    //Constructor
    public TrabajoFabricacion(String numeroTrabajo, LocalDate fecha, String descripcion,
                              ProductoTerminado productoTerminado, Usuario usuarioResponsable, List<MaterialUtilizado> detalleMateriales) {
        setNumeroTrabajo(numeroTrabajo);
        setFecha(fecha);
        this.descripcion = descripcion;
        setProductoTerminado(productoTerminado);
        setUsuarioResponsable(usuarioResponsable);
        this.detalleMateriales = (detalleMateriales != null) ? new ArrayList<>(detalleMateriales) : new ArrayList<>();
    }

    //Constructor vacio
    public TrabajoFabricacion(){
        this.detalleMateriales = new ArrayList<>();
    }

    //GET y SET
    public String getNumeroTrabajo() {
        return numeroTrabajo;
    }

    public void setNumeroTrabajo(String numeroTrabajo) {
        if (numeroTrabajo == null || numeroTrabajo.trim().isEmpty()){
            throw new IllegalArgumentException("El número de trabajo no puede estar vacío.");
        }
        this.numeroTrabajo = numeroTrabajo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null){
            throw new IllegalArgumentException("La fecha del trabajo de fabricación es obligatoria.");
        }
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ProductoTerminado getProductoTerminado() {
        return productoTerminado;
    }

    public void setProductoTerminado(ProductoTerminado productoTerminado) {
        if (productoTerminado == null){
            throw new IllegalArgumentException("El producto terminado no puede ser nulo.");
        }
        this.productoTerminado = productoTerminado;
    }

    public Usuario getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(Usuario usuarioResponsable) {
        if(usuarioResponsable == null){
            throw new IllegalArgumentException("El usuario responsable es obligatorio.");
        }
        this.usuarioResponsable = usuarioResponsable;
    }

    public List<MaterialUtilizado> getDetalleMateriales() {
        return new ArrayList<>(detalleMateriales);
    }

    public void setDetalleMateriales(List<MaterialUtilizado> detalleMateriales) {
        this.detalleMateriales = (detalleMateriales != null) ? new ArrayList<>(detalleMateriales) : new ArrayList<>();
    }

    //=========================================

    //Metodo para agregar material utilizado al trabajo de fabricación
    public void agregarMaterial(MaterialUtilizado material){
        if (material != null){
            this.detalleMateriales.add(material);
        }
    }

    //Metodo para calcular el costo total de los materiales consumidos en este trabajo de fabricación
    public double calcularCostoTotalMateriales(){
        double total = 0.0;
        for (MaterialUtilizado mu: detalleMateriales){
            total += mu.calcularCosto();
        }
        return total;
    }

    //Metodo para confirmar el trabajo de fabricacion y poder registrar dicho movimiento en el inventario
    public MovimientoInventario confirmarTrabajo(){
        //salida de materia priba, entrada de producto terminado
        MovimientoInventario movimiento = new MovimientoInventario();
        return movimiento;
    }

    //Metodo para sobreescribir
    @Override
    public String toString(){
        return "Trabajo No." + numeroTrabajo + "-Fecha:" + fecha;
    }

    //Metodo para comparar variables en espacio de memoria
    @Override
    public boolean equals(Object o){
        if (this == o)  return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrabajoFabricacion that = (TrabajoFabricacion) o;
        return Objects.equals(numeroTrabajo, that.numeroTrabajo);
    }

    @Override
    public int hashCode(){
        return Objects.hash(numeroTrabajo);
    }

}
