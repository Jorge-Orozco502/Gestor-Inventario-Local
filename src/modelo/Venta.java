package modelo;

import java.time.LocalDate;

public class Venta {
    //Atributos
    private int numeroVenta;
    private LocalDate fechaVenta;
    private String nombreCliente;
    private String usuarioResponsable;
    private double totalGeneral;
    private String formaPago;

    //Constructor
    public Venta(int numeroVenta, LocalDate fechaVenta, String nombreCliente, String usuarioResponsable, double totalGeneral, String formaPago) {
        this.numeroVenta = numeroVenta;
        this.fechaVenta = fechaVenta;
        this.nombreCliente = nombreCliente;
        this.usuarioResponsable = usuarioResponsable;
        this.totalGeneral = totalGeneral;
        this.formaPago = formaPago;
    }
    //Get y Set
    public int getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(int numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public double getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(double totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }



}
