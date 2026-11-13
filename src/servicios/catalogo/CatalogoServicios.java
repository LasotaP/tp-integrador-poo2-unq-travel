package servicios.catalogo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import servicios.Servicio;
import servicios.reportes.Visitante;

public class CatalogoServicios {
	
	private List<Servicio> servicios = new ArrayList<>();
	
	public void addServicio(Servicio servicio) {
		servicios.add(servicio);
	}
	
	public List<Servicio> buscar(CriterioBusqueda criterio){
		return servicios.stream()
						.filter(s -> criterio.cumpleCriterio(s))
						.collect(Collectors.toList());
	}
	
	
	// Punto de entrada del Visitor sobre el catálogo: recibe un visitante y se lo
	// "presenta" a cada uno de los servicios que contiene. El catálogo no sabe qué
	// hace el visitante (contar cupos, contar proveedores, etc.): solo lo reparte.
	public void aceptar(Visitante v) {

	    // Para cada elemento del catálogo (servicio individual o paquete) llamo a su
	    // aceptar(v). Ahí cada uno se identifica ante el visitante:
	    //  - ServicioIndividual -> v.visitarServicio(this)
	    //  - PaqueteTuristico   -> v.visitarPaquete(this) y luego reenvía la visita
	    //                          a los servicios que contiene (recursión)
	    // Esto es double dispatch: el catálogo no necesita preguntar de qué tipo es
	    // cada elemento (sin instanceof), porque cada uno elige qué método del
	    // visitante invocar.
	    servicios.forEach(s -> s.aceptar(v));
	}
	
}
