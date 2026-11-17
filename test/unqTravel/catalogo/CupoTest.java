package unqTravel.catalogo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;

public class CupoTest extends SetUpTest {

	private Cupo cupo;

	@BeforeEach
	public void crearCupo() {
		cupo = new Cupo();
		cupo.definirTotal(desde, hasta, 10); // 10 lugares por dia, de desde a hasta (inclusive)
	}


	@Test
	public void definirTotalDeUnDia() {
		cupo.definirTotal(hoy, 3);

		assertEquals(3, cupo.getTotal(hoy));
	}

	@Test
	public void definirTotalInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> cupo.definirTotal(null, 3));
		assertThrows(IllegalArgumentException.class, () -> cupo.definirTotal(hoy, null));
		assertThrows(IllegalArgumentException.class, () -> cupo.definirTotal(hoy, -1));

		cupo.reservar(desde, hasta, 6);
		assertThrows(IllegalArgumentException.class, () -> cupo.definirTotal(desde, 5)); // menos de lo reservado
	}

	@Test
	public void hayDisponibilidad() {
		assertTrue(cupo.hayDisponibilidad(desde, hasta, 10));
		assertFalse(cupo.hayDisponibilidad(desde, hasta, 11));
		assertTrue(cupo.hayDisponibilidadIndividual(desde, 10));
		assertThrows(IllegalArgumentException.class, () -> cupo.hayDisponibilidad(desde, hasta, 0));
	}

	@Test
	public void reservar() {
		cupo.reservar(desde, hasta, 4);

		assertEquals(4, cupo.getReservado(desde));
		assertEquals(6, cupo.getDisponibilidad(hasta));
		assertThrows(IllegalArgumentException.class, () -> cupo.reservar(desde, hasta, 7)); // quedan 6
	}

	@Test
	public void restaurar() {
		cupo.reservar(desde, hasta, 4);

		cupo.restaurar(desde, hasta, 3);

		assertEquals(1, cupo.getReservado(desde));
		assertThrows(IllegalArgumentException.class, () -> cupo.restaurar(desde, hasta, 2)); // solo queda 1
	}

	@Test
	public void fechasInvalidasLanzanExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> cupo.hayDisponibilidad(null, hasta, 1));
		assertThrows(IllegalArgumentException.class, () -> cupo.hayDisponibilidad(hasta, desde, 1)); // fin antes que inicio
	}
}