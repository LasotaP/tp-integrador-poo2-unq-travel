package servicios;

import java.util.ArrayList;
import java.util.List;

public class ServicioIndividual implements Servicio {
    private String id;
    private String nombre;
    private String descripcion;
    private String proveedor;
    private String categoria;
    private double precioBase;
    private double descuento;
    private Integer cupo;
    private List<Atributo<?>> atributos;
    

    // CONSTRUCTOR
    public ServicioIndividual(String id, String nombre, String descripcion, String proveedor, String cat, double precio, double descuento, Integer cupo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.categoria = cat;
        this.precioBase = precio;
        this.descuento = descuento;
        this.atributos = new ArrayList<>();
        this.cupo = cupo;

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
    @Override
	public Integer cupoDisponible() {
    	return this.cupo;
	}
    public boolean hayCupo(Integer cantidad) {
    	return  this.cupo > cantidad;
    }
    // HELPERS
    public void addAtributo(Atributo<?> atributo) {
        atributos.add(atributo);
    }
    public void decrementarCupo(Integer cantidad) {
    	if (!hayCupo(cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
	    this.cupo -= cantidad; 
    }
    
    @Override
	public void aumentarCupo(Integer cantidad) {
		this.cupo += cantidad;
		
	}


    // VALIDACION
    private void validarServicio(){
        if(id.isBlank()) { // REVISAR VALIDACION, PORQUE PUEDE HABER MAS COSAS QUE CHEQUEAR, COMO QUE NO YA NO EXISTA
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

