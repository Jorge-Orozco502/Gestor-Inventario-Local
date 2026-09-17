package modelo;

import java.time.LocalDate;

public class Compra {
    //Atributos
    private int numeroCompra;
    private String nitProveedor;
    private LocalDate fechaCompra;
    private String nombreProveedor;
    private double totalCompra;
    private String formaPago;

    //Constructor
    public Compra(int numeroCompra, String nitProveedor, LocalDate fechaCompra, String nombreProveedor, double totalCompra, String formaPago) {
        this.numeroCompra = numeroCompra;
        this.nitProveedor = nitProveedor;
        this.fechaCompra = fechaCompra;
        this.nombreProveedor = nombreProveedor;
        this.totalCompra = totalCompra;
        this.formaPago = formaPago;
    }
    //Get y Set
    public int getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(int numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }






    //=============
}