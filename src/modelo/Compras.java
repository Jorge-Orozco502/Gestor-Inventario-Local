package modelo;

public class Compras {
    //Atributos
    private int numeroCompra;
    private String nitProveedor;
    private String fecha;
    private double porcentajeDescuento;
    private double total;
    private String formaPago;
    private double IVA;

    //Constructor


    public Compras(int numeroCompra, String nitProveedor, String fecha, double porcentajeDescuento, double total, String formaPago, double IVA) {
        this.numeroCompra = numeroCompra;
        this.nitProveedor = nitProveedor;
        this.fecha = fecha;
        this.porcentajeDescuento = porcentajeDescuento;
        this.total = total;
        this.formaPago = formaPago;
        this.IVA = IVA;
    }
    //Metodo Get -lectura
    public int getNumeroCompra(){return numeroCompra;}
    public String getNitProveedor(){return nitProveedor;}
    public String getFecha(){return fecha;}
    public double getPorcentajeDescuento(){return porcentajeDescuento;}
    public double getTotal(){return total;}
    public String getFormaPago(){return formaPago;}
    public double getIVA(){return IVA;}

    //Metodo Set -escritura
    public void setNumeroCompra(int numeroCompra){
        this.numeroCompra = numeroCompra;
    }





    //*//
}