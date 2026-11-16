package unqTravel.catalogo.busqueda;

import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.Servicio;

public class CriterioCategoria implements CriterioBusqueda{

	private Categoria categoria;
	
	public CriterioCategoria(Categoria categoria) {
		this.categoria = categoria;
		validar();
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.getCategoria() == categoria;
	}

	private void validar() {
		if(categoria == null) {
			throw new IllegalArgumentException("Categoria invalida");
		}
	}
	
}
