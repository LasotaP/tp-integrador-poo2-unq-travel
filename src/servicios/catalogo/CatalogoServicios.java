package servicios.catalogo;

import java.util.List;
import java.util.stream.Collectors;

import servicios.Servicio;

public class CatalogoServicios {
	
	private List<Servicio> servicios;
	
	public void addServicio(Servicio servicio) {
		servicios.add(servicio);
	}
	
	public List<Servicio> buscar(CriterioBusqueda criterio){
		return servicios.stream()
						.filter(s -> criterio.cumpleCriterio(s))
						.collect(Collectors.toList());
	}
}
