package modelo;

import java.math.BigDecimal;

public class DetalleCompra {
    //Atributos
    private double cantidadComprada;
    private double costoUnitario;
    private double subtotal;

    //Constructor
    public DetalleCompra(Producto producto, double cantidadComprada, double costoUnitario) {
        this.cantidadComprada = cantidadComprada;
        this.costoUnitario = costoUnitario;
        this.producto = producto;
    }
    //Get y Set
    public double getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(double cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    //===============================
    //==METODOS==

    //Para calcular el subtotal
    public BigDecimal calcularSubtotal(){
        System.out.println("Subtotal:"+subtotal);
        return subtotal = (costoUnitario * cantidadComprada);
    }



}
