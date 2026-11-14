package unqTravel.catalogo;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

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

	public void definirTotal(LocalDate fecha, Integer cantidad) {
	    validarTotal(fecha, cantidad);
	    total.put(fecha, cantidad);
	}
	
	public void definirTotal(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
	    List<LocalDate> fechas = dias(fecIni,fecFin).toList();
	    fechas.forEach(f -> validarTotal(f, cantidad));
	    fechas.forEach(f -> total.put(f, cantidad));
	}
	
	public boolean hayDisponibilidad(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
		validarCantidad(cantidad);
		return dias(fecIni,fecFin).allMatch(f -> hayDisponibilidadIndividual(f, cantidad));
	}
	
	public boolean hayDisponibilidadIndividual(LocalDate fecha, Integer cantidad) {
		return this.getDisponibilidad(fecha) >= cantidad;
	}
	
	public void reservar(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
    	if (!hayDisponibilidad(fecIni, fecFin,cantidad)) {
	        throw new IllegalArgumentException("No hay cupo suficiente");
	    }
		
    	dias(fecIni,fecFin).forEach(f -> reservado.merge(f, cantidad, Integer::sum));
		
    }
	
	public void restaurar(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
		validarCantidad(cantidad);
	    List<LocalDate> fechas = dias(fecIni, fecFin).toList();
	    if (fechas.stream().anyMatch(f -> getReservado(f) < cantidad)) {
	        throw new IllegalArgumentException("No se puede restaurar más de lo reservado");
	    }
	    fechas.forEach(f -> reservado.merge(f, -cantidad, Integer::sum));
		
	}
	
	private void validarTotal(LocalDate fecha, Integer cantidad) {
	    if (fecha == null || cantidad == null) {
	        throw new IllegalArgumentException("La fecha y la cantidad son obligatorias");
	    }
	    if (cantidad < 0) {
	        throw new IllegalArgumentException("El cupo total no puede ser negativo");
	    }
	    if (cantidad < getReservado(fecha)) {
	        throw new IllegalArgumentException("No se puede bajar el cupo total por debajo de lo ya reservado");
	    }
	}
	
	private Stream<LocalDate> dias(LocalDate fecIni, LocalDate fecFin) {
		if (fecIni == null || fecFin == null) {
			throw new IllegalArgumentException("Las fechas son obligatorias");
		}
		if (fecFin.isBefore(fecIni)) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
		}
		return fecIni.datesUntil(fecFin.plusDays(1));
	}
	
	private void validarCantidad(Integer cantidad) {
	    if (cantidad == null || cantidad <= 0) {
	        throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
	    }
	}
}
