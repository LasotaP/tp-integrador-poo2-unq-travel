package servicios;

import java.util.HashMap;
import java.util.Map;

public class PaqueteTuristico implements Servicio {
    private String nombre;
    private String descripcion;
    private double descuento;
    private Integer cupo; 
    private Map<Servicio, Integer> servicios;

    // CONSTRUCTOR
    public PaqueteTuristico (String nombre, String descripcion, double descuento,Integer cupo){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.servicios = new HashMap<>();
        this.cupo = cupo;
        validarPaquete();
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
	public Integer cupoDisponible() {
		return this.cupo;
	}
    @Override
    public double getPrecioBaseCalculado() {
        return getPrecioBaseTotal() * (1 - (descuento / 100));
    }

    private double getPrecioBaseTotal() {
        return servicios.entrySet()
        				.stream()
                        .mapToDouble(s -> s.getKey().getPrecioBaseCalculado() * s.getValue())
                        .sum();
   
    }
    public boolean hayCupo(Integer cantidad) {
    	return this.cupo > cantidad;
    }
    
    public void decrementarCupo(Integer cantidad) {
    	if (!hayCupo(cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
	    this.cupo -= cantidad; 
    }
    
    @Override
	public void aumentarCupo(Integer cantidad) {
		this.servicios.forEach((servicio,cant) -> servicio.decrementarCupo(cant * cantidad));
		
		this.cupo += cantidad;
	}

    // HELPERS
    public void addServicio(Servicio servicio, Integer cantidad){
        servicio.decrementarCupo(cantidad * this.cupoDisponible());
    	servicios.put(servicio,cantidad);
    }

    // VALIDACION
    private void validarPaquete() {
        if(nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Error: nombre invalido");
        }
        if(descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Error: descripcion invalida");
        }
        if(descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("Error: descuento invalido");
        }
    }

}
