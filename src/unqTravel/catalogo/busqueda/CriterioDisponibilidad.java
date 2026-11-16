package unqTravel.catalogo.busqueda;

import java.time.LocalDate;

import unqTravel.catalogo.Servicio;

public class CriterioDisponibilidad implements CriterioBusqueda{
	private LocalDate fechaFiltro;
	private Integer cantidad;
	
	public CriterioDisponibilidad(LocalDate fechaFiltro, Integer cantidad) {
		this.fechaFiltro = fechaFiltro;
		this.cantidad = cantidad;
		
		validar();
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.hayCupo(LocalDate.now(), fechaFiltro, cantidad);
	}
	
	private void validar() {
		if(fechaFiltro == null) {
			throw new IllegalArgumentException("Fecha no puede ser nula");
		}
		if(fechaFiltro.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha del filtro no puede ser menor a hoy");
		}
		if(cantidad == null || cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
		}
	}
	
}
