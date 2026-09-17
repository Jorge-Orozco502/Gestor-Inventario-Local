package modelo;



public class DetalleCompra {
    //Atributos
    private int cantidadComprada;
    private double costoUnitario;
    private double subtotal;

    //Constructor
    public DetalleCompra(Producto producto, int cantidadComprada, double costoUnitario) {
        this.producto = producto;
        this.cantidadComprada = cantidadComprada;
        this.costoUnitario = costoUnitario;
    }

    public double calcularSubtotal(){
        System.out.println("Subtotal:"+subtotal);
        return subtotal = (costoUnitario * cantidadComprada);
    }







}
