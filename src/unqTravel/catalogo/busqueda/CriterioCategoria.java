package unqTravel.catalogo.busqueda;

import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.Servicio;

public class CriterioCategoria implements CriterioBusqueda{

	private Categoria categoria;
	
	public CriterioCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.getCategoria() == categoria;
	}

}
