package servicios;

import java.time.LocalDate;
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
    private Cupo cupo;
    private List<Atributo> atributos;
    
    // CONSTRUCTOR
    public ServicioIndividual(String id, String nombre, String descripcion, String proveedor, String cat, double precio, double descuento, Cupo cupo) {
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
	public Integer cupoDisponible(LocalDate fecha) {
    	return this.cupo.getDisponibilidad(fecha);
	}
    public boolean hayCupo(LocalDate fecha,Integer cantidad) {
    	return  this.cupo.hayDisponibilidad(fecha,cantidad);
    }
    // HELPERS
    public void addAtributo(Atributo atributo) {
        atributos.add(atributo);
    }
    public void decrementarCupo(LocalDate fecha, Integer cantidad) {
    	this.getCupo().decrementar(fecha,cantidad);
    }
    public void aumentarCupo(LocalDate fecha, Integer cantidad) {
    	this.getCupo().aumentar(fecha, cantidad);
    }
    
    
    public Cupo getCupo() {
    	return this.cupo;
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

