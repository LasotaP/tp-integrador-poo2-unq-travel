package servicios.catalogo;

import java.util.List;

import servicios.Servicio;

public class CriterioOr implements CriterioBusqueda {
	
	private List<CriterioBusqueda> criterios;
	
	public CriterioOr(List<CriterioBusqueda> criterios) {
		this.criterios = criterios;
		validar();
	}
	
	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return criterios.stream()
						.anyMatch(c -> c.cumpleCriterio(servicio));
	}
	public void validar() {
		if (this.criterios == null || this.criterios.isEmpty()) {
			throw new IllegalArgumentException("Error: El conector OR necesita al menos un criterio");
		}
	}
	
}

