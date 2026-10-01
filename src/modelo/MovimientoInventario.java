package modelo;

import java.time.LocalDateTime;

public class MovimientoInventario {
    //Atributos
    private int idMovimiento;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private double cantidad;
    private double existenciaAnterior;
    private double existenciaNueva;
    private String referenciaDocumento;
    private String motivo;
    private Usuario usuarioResponsable;

    //Constructor
    public MovimientoInventario(int idMovimiento, LocalDateTime fecha, TipoMovimiento tipoMovimiento, double cantidad, double existenciaAnterior, double existenciaNueva, String referenciaDocumento, String motivo, Usuario usuarioResponsable) {
        this.idMovimiento = idMovimiento;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.existenciaAnterior = existenciaAnterior;
        this.existenciaNueva = existenciaNueva;
        this.referenciaDocumento = referenciaDocumento;
        this.motivo = motivo;
        this.usuarioResponsable = usuarioResponsable;
    }

    //Constructor asignando fecha y hora actual por defecto
    public MovimientoInventario(){
        this.fecha = LocalDateTime.now();
    }

    //Get y Set
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getExistenciaAnterior() {
        return existenciaAnterior;
    }

    public void setExistenciaAnterior(double existenciaAnterior) {
        this.existenciaAnterior = existenciaAnterior;
    }

    public double getExistenciaNueva() {
        return existenciaNueva;
    }

    public void setExistenciaNueva(double existenciaNueva) {
        this.existenciaNueva = existenciaNueva;
    }

    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    public void setReferenciaDocumento(String referenciaDocumento) {
        this.referenciaDocumento = referenciaDocumento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(Usuario usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
