package modelo;

public class MaterialUtilizado {
    //Atributos
    private MateriaPrima materiaPrima;
    private double cantidadUtilizada;

    //Constructor
    public MaterialUtilizado(MateriaPrima materiaPrima, double cantidadUtilizada){
        setMateriaPrima(materiaPrima);
        setCantidadUtilizada(cantidadUtilizada);
    }

    //Constructor Vacio
    public MaterialUtilizado(){
    }

    //GET y SET
    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        if( materiaPrima == null){
            throw new IllegalArgumentException("La materia prima no puede ser nula.");
        }
        this.materiaPrima = materiaPrima;
    }

    public double getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(double cantidadUtilizada) {
        if(cantidadUtilizada < 0){
            throw new IllegalArgumentException("La cantidad utilizada no puede ser negativa.");
        }
        this.cantidadUtilizada = cantidadUtilizada;
    }

    //========================

    @Override
    public String toString(){
        return "Material Utilizado{"    +
                "materia prima="    +   (materiaPrima != null ? materiaPrima.getNombre() : "N/A")   +
                ", cantidad utilizada=" +   cantidadUtilizada   +
                '}';
    }
}
