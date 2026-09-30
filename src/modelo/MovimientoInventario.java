package modelo;

import java.time.LocalDate;

public class MovimientoInventario {
    //Atributos
    private int idMovimiento;
    private LocalDate fecha;
    private TipoMovimiento tipoMovimiento;
    private double cantidad;
    private doube existenciaAnterior;
    private double existenciaNueva;
    private String referenciaDocumento;
    private String motivo;

    //Constructor
    public MovimientoInventario(int idMovimiento, LocalDate fecha, TipoMovimiento tipoMovimiento, double cantidad, doube existenciaAnterior, double existenciaNueva, String referenciaDocumento, String motivo) {
        this.idMovimiento = idMovimiento;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.existenciaAnterior = existenciaAnterior;
        this.existenciaNueva = existenciaNueva;
        this.referenciaDocumento = referenciaDocumento;
        this.motivo = motivo;
    }

    //Get y Set
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    public doube getExistenciaAnterior() {
        return existenciaAnterior;
    }

    public void setExistenciaAnterior(doube existenciaAnterior) {
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


}
