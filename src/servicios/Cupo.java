package servicios;

import java.time.*;
import java.util.*;

public class Cupo {
	// Este es calendario
	
	private Map<LocalDate, Integer> total = new HashMap<>();
	private Map<LocalDate, Integer> reservado = new HashMap<>();

	// GETTERS
	public Integer getTotal(LocalDate fecha) {
		return this.total.getOrDefault(fecha, 0);
	}
	
	public Integer getReservado(LocalDate fecha) {
		return this.reservado.getOrDefault(fecha, 0);
	}
	
	public Integer getDisponibilidad(LocalDate fecha) {
		return this.getTotal(fecha) - this.getReservado(fecha);
	}

	
	public boolean hayDisponibilidad(LocalDate fecha, Integer cantidad) {
		return this.getDisponibilidad(fecha) >= cantidad;
	}
	
	
	
	public void decrementar(LocalDate fecha,Integer cantidad) {
    	if (!hayDisponibilidad(fecha,cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
    	this.reservado.put(fecha, this.getReservado(fecha) + cantidad);
    }
    
	public void aumentar(LocalDate fecha, Integer cantidad) {
		this.total.put(fecha, this.getTotal(fecha) + cantidad);
		
	}
}
