
package modelo;


public class Producto {
    private double codigoProducto; //ver cuantos digitos
    private String nombre;
    private String categoria;
    private String descripcion;
    private String unidadMedida;
    private double precioCompra;
    private double precioVenta;
    private int existenciaActual;
    private int existenciaMinima;
    private boolean estadoProducto;

   
  //Constructor

    public Producto(double codigoProducto, String nombre, String categoria, String descripcion, String unidadMedida, double precioCompra, double precioVenta, int existenciaActual, int existenciaMinima, boolean estadoProducto) {
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


    // Metodo GET

    public double getCodigoProducto() {
        return codigoProducto;
    }
    public String getNombre() {
        return nombre;
    }
    public String getCategoria(){return categoria; }
    public String getDescripcion(){return descripcion;}
    public String getUnidadMedida(){return unidadMedida;}
    public double getPrecioCompra() {
        return precioCompra;
    }
    public double getPrecioVenta(){return precioVenta;}
    public int getExistenciaActual(){return existenciaActual;}
    public int getExistenciaMinima(){return existenciaMinima;}
    public boolean getEstadoProducto(){return estadoProducto;}

    // Metodo SET
    public void setCodigoProducto(double codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setCategoria(String categoria){this.categoria = categoria; }
    public void setDescripcion(String descripcion){this.descripcion = descripcion;}
    public void setUnidadMedida(String unidadMedida){this.unidadMedida = unidadMedida;}
    public void setPrecioCompra(double precioCompra){this.precioCompra = precioCompra;}
    public void setPrecioVenta(double precioVenta){this.precioVenta = precioVenta;}
    public void setExistenciaActual(int existenciaActual){this.existenciaActual= existenciaActual;}
    public void setExistenciaMinima(int existenciaMinima){this.existenciaMinima= existenciaMinima;}
    public void setEstadoProducto(boolean estadoProducto){this.estadoProducto = estadoProducto;}
    
    
    
}
