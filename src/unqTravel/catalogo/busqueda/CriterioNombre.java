package unqTravel.catalogo.busqueda;

import unqTravel.catalogo.Servicio;

public class CriterioNombre implements CriterioBusqueda{
	private String texto;
	
	public CriterioNombre(String texto) {
		this.texto = texto;
		validar();
	}

	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.getNombre().toLowerCase().contains(texto.toLowerCase());
	}
	
	private void validar() {
		if(texto == null || texto.isBlank()) {
			throw new IllegalArgumentException("Texto invalido");
		}
	}
}
