package unqTravel.catalogo.busqueda;

import java.time.LocalDate;

import unqTravel.catalogo.Servicio;

public class CriterioDisponibilidad implements CriterioBusqueda{
	private LocalDate fechaFiltro;
	private Integer cantidad;
	
	public CriterioDisponibilidad(LocalDate fechaFiltro, Integer cantidad) {
		this.fechaFiltro = fechaFiltro;
		this.cantidad = cantidad;
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.hayCupo(LocalDate.now(), fechaFiltro, cantidad);
	}
	
	
	
}
