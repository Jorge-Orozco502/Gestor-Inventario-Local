package modelo;

public class Proveedor {
    //Atributos
    private String codigoProveedor;
    private String nombreProveedor;
    private String nitProveedor;
    private String telefono;
    private String direccion;
    private String estado;

    //Constructor


    public Proveedor(String codigoProveedor, String nombreProveedor, String nitProveedor, String telefono, String direccion, String estado) {
        this.codigoProveedor = codigoProveedor;
        this.nombreProveedor = nombreProveedor;
        this.nitProveedor = nitProveedor;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
    }

    //Metodo GET

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEstado() {
        return estado;
    }

    //Metodo SET

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }



}
