package servicios;

import java.time.*;
import java.util.*;

public class Cupo {
	
	private Map<LocalDate, Integer> total = new HashMap<>();
	private Map<LocalDate, Integer> reservado = new HashMap<>();
	
	
	public boolean hayDisponibilidad(LocalDate fecha, Integer cantidad) {
		return this.total.get(fecha) - this.reservado.get(fecha) >= cantidad;
	}
	
	public Integer getDisponibilidad(LocalDate fecha) {
		return this.total.get(fecha) - this.reservado.get(fecha);
	}
	
	public Integer getReservado(LocalDate fecha) {
		return this.reservado.get(fecha);
	}
	
	public void decrementar(LocalDate fecha,Integer cantidad) {
    	if (!hayDisponibilidad(fecha,cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
    	this.total.put(fecha, this.getDisponibilidad(fecha) - cantidad);
    	this.reservado.put(fecha, this.getReservado(fecha) + cantidad);
    }
    
	public void aumentar(LocalDate fecha, Integer cantidad) {
		this.total.put(fecha, this.getDisponibilidad(fecha) + cantidad);
		
	}
}
