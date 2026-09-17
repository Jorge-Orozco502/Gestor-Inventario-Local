
package modelo;


public class Producto {
    //Atributos
    private double codigoProducto; //ver cuantos digitos
    private String nombre;
    private String categoria;
    private String descripcion;
    private String unidadMedida;
    private double precioCompra;
    private double precioVenta;
    private int existenciaActual;
    private int existenciaMinima;
    private EstadoProducto estadoProducto;

   
  //Constructor

    public Producto(double codigoProducto, String nombre, String categoria, String descripcion, String unidadMedida, double precioCompra, double precioVenta, int existenciaActual, int existenciaMinima, EstadoProducto estadoProducto) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.existenciaActual = existenciaActual;
        this.existenciaMinima = existenciaMinima;
        this.estadoProducto = estadoProducto;
    }

    //Get y Set
    public double getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(double codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getExistenciaActual() {
        return existenciaActual;
    }

    public void setExistenciaActual(int existenciaActual) {
        this.existenciaActual = existenciaActual;
    }

    public int getExistenciaMinima() {
        return existenciaMinima;
    }

    public void setExistenciaMinima(int existenciaMinima) {
        this.existenciaMinima = existenciaMinima;
    }

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    //Metodo Calcular el valor del inventario
    public double valorInventario(){
        double valorInventario = (precioCompra * existenciaActual);
        return valorInventario;
    }





    //======
}
