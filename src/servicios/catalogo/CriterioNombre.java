package servicios.catalogo;

import servicios.Servicio;

public class CriterioNombre implements CriterioBusqueda{
	private String texto;
	
	public CriterioNombre(String texto) {
		this.texto = texto;
	}

	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.getNombre().toLowerCase().contains(texto.toLowerCase());
	}
	 
}
