package unqTravel.catalogo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.atributos.ServicioInvalidoException;
import unqTravel.reportes.Visitante;

public class PaqueteTuristicoTest extends SetUpTest {


	@Test
	public void constructorInvalidoLanzaExcepcion() {
		assertThrows(ServicioInvalidoException.class, () -> new PaqueteTuristico(" ", Categoria.PAQUETE, "d", 0.1));
		assertThrows(ServicioInvalidoException.class, () -> new PaqueteTuristico("n", Categoria.PAQUETE, null, 0.1));
		assertThrows(ServicioInvalidoException.class, () -> new PaqueteTuristico("n", Categoria.PAQUETE, "d", 1.5));
	}

	@Test
	public void addServicioInvalidoLanzaExcepcion() {
		assertThrows(ServicioInvalidoException.class, () -> paqueteEscapada.addServicio(null));
		// granTour ya contiene a paqueteEscapada: agregarlo adentro generaria un ciclo
		assertThrows(ServicioInvalidoException.class, () -> paqueteEscapada.addServicio(paqueteGranTour));
	}

	@Test
	public void preciosConPaquetesAnidados() {
		// escapada: (15.000 + 4.000) con 10% de descuento
		assertEquals(19000.0, paqueteEscapada.getPrecioBase(), 0.001);
		assertEquals(17100.0, paqueteEscapada.getPrecioFinal(), 0.001);
		// granTour: (17.100 de la escapada + 90.000 del vuelo) con 5% de descuento
		assertEquals(107100.0, paqueteGranTour.getPrecioBase(), 0.001);
		assertEquals(101745.0, paqueteGranTour.getPrecioFinal(), 0.001);
	}

	@Test
	public void cupoDisponibleEsElMenorDeSusServicios() {
		assertEquals(10, paqueteEscapada.cupoDisponible(desde)); // hotel 10, excursion 20
		assertEquals(0, new PaqueteTuristico("Vacio", Categoria.PAQUETE, "d", 0).cupoDisponible(desde));
	}

	@Test
	public void hayCupo() {
		assertTrue(paqueteEscapada.hayCupo(desde, hasta, 10));
		assertFalse(paqueteEscapada.hayCupo(desde, hasta, 11));
		assertFalse(new PaqueteTuristico("Vacio", Categoria.PAQUETE, "d", 0).hayCupo(desde, hasta, 1));
	}

	@Test
	public void reservarYRestaurarCupo() {
		paqueteEscapada.reservarCupo(desde, hasta, 2);

		assertEquals(8, hotel.cupoDisponible(desde));
		assertEquals(18, excursion.cupoDisponible(desde));
		assertThrows(IllegalArgumentException.class, () -> paqueteEscapada.reservarCupo(desde, hasta, 9)); // el hotel tiene 8

		paqueteEscapada.restaurarCupo(desde, hasta, 2);

		assertEquals(10, hotel.cupoDisponible(desde));
		assertEquals(20, excursion.cupoDisponible(desde));
	}

	@Test
	public void contiene() {
		assertTrue(paqueteGranTour.contiene(paqueteGranTour));
		assertTrue(paqueteGranTour.contiene(hotel)); // anidado dentro de la escapada
		assertFalse(paqueteGranTour.contiene(autoCompacto));
	}

	@Test
	public void validarServicio() {
		assertTrue(paqueteEscapada.validarServicio());
		assertFalse(new PaqueteTuristico("Vacio", Categoria.PAQUETE, "d", 0).validarServicio());

		hotel.agregarAtributoDinamico(superficieInvalida); // un servicio interno invalido

		assertFalse(paqueteEscapada.validarServicio());
	}

	@Test
	public void aceptarRecorreElPaqueteYSusServicios() {
		Visitante visitante = mock(Visitante.class);

		paqueteGranTour.aceptar(visitante);

		verify(visitante).visitarPaquete(paqueteGranTour);
		verify(visitante).visitarPaquete(paqueteEscapada);
		verify(visitante).visitarServicio(hotel);
		verify(visitante).visitarServicio(excursion);
		verify(visitante).visitarServicio(vuelo);
	}
}