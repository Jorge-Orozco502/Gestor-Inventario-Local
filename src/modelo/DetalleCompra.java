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

    //===============================
    //==METODOS==

    //Para calcular el subtotal
    public BigDecimal calcularSubtotal(){
        System.out.println("Subtotal:"+subtotal);
        return subtotal = (costoUnitario * cantidadComprada);
    }







}
