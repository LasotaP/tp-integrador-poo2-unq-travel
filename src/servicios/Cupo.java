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

	public boolean hayDisponibilidad(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
		return fecIni.datesUntil(fecFin).allMatch(f -> hayDisponibilidadInidividual(f, cantidad));
	}
	
	public boolean hayDisponibilidadInidividual(LocalDate fecha, Integer cantidad) {
		return this.getDisponibilidad(fecha) >= cantidad;
	}
	
	public void reservar(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
    	if (!hayDisponibilidad(fecIni, fecFin,cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
		
    	fecIni.datesUntil(fecFin).forEach(f -> reservado.merge(f, cantidad, Integer::sum));
		
    }
    
	public void restaurar(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
		fecIni.datesUntil(fecFin).forEach(f -> reservado.merge(f, -(cantidad), Integer::sum));
		
	}
}
