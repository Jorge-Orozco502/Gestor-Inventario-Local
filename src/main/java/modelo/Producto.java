
package modelo;

import java.math.BigDecimal;

public abstract class Producto {
    //Atributos
    private String codigo; //ver cuantos digitos
    private String nombre;
    private String categoria;
    private String descripcion;
    private String unidadMedida;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private double existenciaActual;
    private double existenciaMinima;
    private EstadoProducto estadoProducto;

  //Constructor
    public Producto(String codigo, String nombre, String categoria, String descripcion, String unidadMedida,
                    BigDecimal precioCompra, BigDecimal precioVenta, double existenciaActual,
                    double existenciaMinima, EstadoProducto estadoProducto) {

        setCodigo(codigo);
        setNombre(nombre);
        setCategoria(categoria);
        setDescripcion(descripcion);
        setUnidadMedida(unidadMedida);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);
        setExistenciaActual(existenciaActual);
        setExistenciaMinima(existenciaMinima);
        setEstadoProducto(estadoProducto);
    }

    //Constructor Vacio
    public Producto(){
    }

    //Get y Set
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El código del producto no puede ser nulo o vacío.");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()){
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía.");
        }
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()){
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        if (unidadMedida == null || unidadMedida.trim().isEmpty()){
            throw new IllegalArgumentException("La unidad de medida no puede ser nula o vacía.");
        }
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        if (precioCompra == null || precioCompra.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio de compra no puede ser nulo ni negativo.");
        }
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        if (precioVenta == null || precioVenta.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio de venta no puede ser nulo ni negativo.");
        }
        this.precioVenta = precioVenta;
    }

    public double getExistenciaActual() {
        return existenciaActual;
    }

    public void setExistenciaActual(double existenciaActual) {
        if (existenciaActual < 0){
            throw new IllegalArgumentException("La existencia actual no puede ser negativa!");
        }
        this.existenciaActual = existenciaActual;
    }

    public double getExistenciaMinima() {
        return existenciaMinima;
    }

    public void setExistenciaMinima(double existenciaMinima) {
        if (existenciaMinima < 0){
            throw new IllegalArgumentException("La existencia mínima no puede ser negativa!");
        }
        this.existenciaMinima = existenciaMinima;
    }

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        if (estadoProducto == null){
            throw new IllegalArgumentException("El estado del producto no puede ser nulo.");
        }
        this.estadoProducto = estadoProducto;
    }

    //=========================================================================

    //Metodo Calcular el valor del inventario con base a lo comprado
    public BigDecimal calcularValorInventario(){
        if (precioCompra == null){
            return BigDecimal.ZERO;
        }
        BigDecimal existencia = BigDecimal.valueOf(existenciaActual);
        return existencia.multiply(precioCompra);
    }

    //Metodo para verificar el stock bajo
    public boolean tieneStockBajo(){
        return this.existenciaActual <= this.existenciaMinima;
    }

    //Metodo para verificar si el producto está agotado
    public boolean estaAgotado(){
        return existenciaActual <= 0;
    }

    //Metodo para aumentar el stock
    public void aumentarExistencia(double cantidad){
        if (cantidad <= 0){
            throw new IllegalArgumentException ("La cantidad debe ser mayor a cero!");
        }
        existenciaActual += cantidad;
    }

    //Metodo para poder disminuir el stock
    public boolean disminuirExistencia(double cantidad){
        if (cantidad <= 0){
            throw new IllegalArgumentException ("La cantidad debe ser mayor a cero!");
        }

        if (cantidad > existenciaActual){
            return false;
        }

        existenciaActual -= cantidad;
        return true;
    }

    //Método Abstracto para poder indicar si el producto es vendible o no según la subclase
    public abstract boolean esVendible();

}
