package servicios.catalogo;

import java.util.List;

import servicios.Servicio;

public class CriterioAnd implements CriterioBusqueda{
	
	private List<CriterioBusqueda> criterios;
	
	public CriterioAnd(List<CriterioBusqueda> criterios) {
		this.criterios = criterios;
		validar();
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return criterios.stream()
						.allMatch(c -> c.cumpleCriterio(servicio));
	}
	public void validar() {
		if (this.criterios == null || this.criterios.isEmpty()) {
			throw new IllegalArgumentException("Error: El conector AND necesita al menos un criterio");
		}
	}
}
