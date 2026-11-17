package unqTravel.catalogo.busqueda;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;

public class CriteriosSimplesTest extends SetUpTest {

	// ---- Nombre ----

	@Test
	public void criterioNombre() {
		CriterioNombre criterio = new CriterioNombre("HOTEL"); // no distingue mayusculas

		assertTrue(criterio.cumpleCriterio(hotel));
		assertFalse(criterio.cumpleCriterio(excursion));
	}

	@Test
	public void criterioNombreInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new CriterioNombre(" "));
	}

	// ---- Precio ----

	@Test
	public void criterioPrecio() {
		CriterioPrecio criterio = new CriterioPrecio(15000);

		assertTrue(criterio.cumpleCriterio(hotel));   // 15.000 <= 15.000
		assertFalse(criterio.cumpleCriterio(vuelo));  // usa el precio base: 100.000
	}

	// ---- Categoria ----

	@Test
	public void criterioCategoria() {
		CriterioCategoria criterio = new CriterioCategoria(Categoria.ALOJAMIENTO);

		assertTrue(criterio.cumpleCriterio(hotel));
		assertFalse(criterio.cumpleCriterio(excursion));
	}

	@Test
	public void criterioCategoriaNulaLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new CriterioCategoria(null));
	}

	// ---- Disponibilidad ----

	@Test
	public void criterioDisponibilidad() {
		CriterioDisponibilidad criterio = new CriterioDisponibilidad(desde, 1);

		assertTrue(criterio.cumpleCriterio(hotel));
		assertFalse(criterio.cumpleCriterio(tourAgotado)); // cupo 0
	}

	@Test
	public void criterioDisponibilidadInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new CriterioDisponibilidad(null, 1));
		assertThrows(IllegalArgumentException.class, () -> new CriterioDisponibilidad(hoy.minusDays(1), 1));
		assertThrows(IllegalArgumentException.class, () -> new CriterioDisponibilidad(desde, 0));
	}
}