package servicios;

import java.util.ArrayList;
import java.util.List;

public class ServicioIndividual implements Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private String proveedor;
    private String categoria;
    private double precioBase;
    private double descuento;
    private List<Atributo<?>> atributos;

    // CONSTRUCTOR
    public ServicioIndividual(int id, String nombre, String descripcion, String proveedor, String cat, double precio, double descuento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.categoria = cat;
        this.precioBase = precio;
        this.descuento = descuento;
        this.atributos = new ArrayList<>();

        validarServicio();
    }

    // GETTERS
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double getPrecioBaseCalculado() {
        return precioBase * (1 - (descuento / 100));
    }

    // HELPERS
    public void addAtributo(Atributo<?> atributo) {
        atributos.add(atributo);
    }


    // VALIDACION
    private void validarServicio(){
        if(id < 0) { // REVISAR VALIDACION, PORQUE PUEDE HABER MAS COSAS QUE CHEQUEAR, COMO QUE NO YA NO EXISTA
            throw new IllegalArgumentException("Error: id invalido");
        }
        if(nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Error: nombre invalido");
        }
        if(descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Error: descripcion invalida");
        }
        if(proveedor == null || proveedor.isBlank()) {
            throw new IllegalArgumentException("Error: proveedor invalido");
        }
        if(categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Error: categoria invalida");
        }
        if(precioBase < 0.0) {
            throw new IllegalArgumentException("Error: precio invalido");
        }
        if(descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("Error: descuento invalido");
        }
    }
}

