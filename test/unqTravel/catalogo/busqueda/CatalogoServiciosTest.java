package unqTravel.catalogo.busqueda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reportes.Visitante;

public class CatalogoServiciosTest extends SetUpTest {

	@Test
	public void addServicio() {
		CatalogoServicios nuevo = new CatalogoServicios();
		CriterioBusqueda todos = servicio -> true;
		assertTrue(nuevo.buscar(todos).isEmpty());

		nuevo.addServicio(hotel);

		assertEquals(List.of(hotel), nuevo.buscar(todos));
	}

	@Test
	public void buscarDevuelveSoloLosQueCumplenElCriterio() {
		CriterioBusqueda criterio = mock(CriterioBusqueda.class);
		when(criterio.cumpleCriterio(hotel)).thenReturn(true);
		when(criterio.cumpleCriterio(excursion)).thenReturn(true);

		assertEquals(List.of(hotel, excursion), catalogo.buscar(criterio));

	}

	@Test
	public void aceptarLeDaElVisitanteATodosLosServicios() {
		Visitante visitante = mock(Visitante.class);
		CatalogoServicios nuevo = new CatalogoServicios();
		nuevo.addServicio(hotel);
		nuevo.addServicio(paqueteEscapada); // contiene hotel y excursion

		nuevo.aceptar(visitante);

		verify(visitante, times(2)).visitarServicio(hotel); // directo y dentro del paquete
		verify(visitante).visitarServicio(excursion);
		verify(visitante).visitarPaquete(paqueteEscapada);
	}
}