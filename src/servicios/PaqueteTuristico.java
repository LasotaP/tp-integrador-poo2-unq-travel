package servicios;

import java.time.LocalDate;
import java.util.*;

public class PaqueteTuristico implements Servicio {
    private String nombre;
    private String descripcion;
    private double descuento;
    private Cupo cupo; 
    private List<Servicio> servicios; 
    // tiene que tener fecha de inicio fecha de salida.
    // Objeto calendario, para cada dia tiene un cupo. 

    // CONSTRUCTOR
    public PaqueteTuristico (String nombre, String descripcion, double descuento,Cupo cupo){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.servicios = new ArrayList<>();
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
	public Integer cupoDisponible(LocalDate fecha) {
		return this.cupo.getDisponibilidad(fecha);
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
    public boolean hayCupo(LocalDate fecha, Integer cantidad) {
    	return this.cupo.hayDisponibilidad(fecha, cantidad);
    }
    
    public void decrementarCupo(LocalDate fecha,Integer cantidad) {
    	if (!hayCupo(fecha, cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
	    this.cupo.decrementar(fecha, cantidad); 
    }
    
    @Override
	public void aumentarCupo(LocalDate fecha, Integer cantidad) {
		this.servicios.forEach((servicio,cant) -> servicio.decrementarCupo(fecha, cant * cantidad));
		this.cupo.aumentar(fecha, cantidad); 
	}

    // HELPERS
    public void addServicio(Servicio servicio,LocalDate fecha, Integer cantidad){
        // esto va a pasar en la reserva servicio.decrementarCupo(fecha, cantidad * this.cupoDisponible(fecha)); //este hay que sacarlo 
        servicios.add(servicio); 	
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
