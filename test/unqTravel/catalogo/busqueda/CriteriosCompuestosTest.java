package unqTravel.catalogo.busqueda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;

public class CriteriosCompuestosTest extends SetUpTest {

	@Test
	public void criterioAnd() {
		// Alojamiento AND precio <= 20.000
		CriterioAnd criterio = new CriterioAnd(List.of(
				new CriterioCategoria(Categoria.ALOJAMIENTO), new CriterioPrecio(20000)));

		assertTrue(criterio.cumpleCriterio(hotel));
		assertFalse(criterio.cumpleCriterio(excursion)); // barato, pero no es alojamiento
	}

	@Test
	public void criterioOr() {
		// nombre contiene "hotel" OR categoria Vuelo
		CriterioOr criterio = new CriterioOr(List.of(
				new CriterioNombre("hotel"), new CriterioCategoria(Categoria.VUELO)));

		assertTrue(criterio.cumpleCriterio(hotel));
		assertTrue(criterio.cumpleCriterio(vuelo));
		assertFalse(criterio.cumpleCriterio(excursion));
	}

	@Test
	public void criterioNot() {
		CriterioNot criterio = new CriterioNot(new CriterioCategoria(Categoria.EXCURSION));

		assertTrue(criterio.cumpleCriterio(hotel));
		assertFalse(criterio.cumpleCriterio(excursion));
	}

	@Test
	public void criteriosInvalidosLanzanExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new CriterioAnd(List.of()));
		assertThrows(IllegalArgumentException.class, () -> new CriterioAnd(null));
		assertThrows(IllegalArgumentException.class, () -> new CriterioOr(List.of()));
		assertThrows(IllegalArgumentException.class, () -> new CriterioOr(null));
		assertThrows(IllegalArgumentException.class, () -> new CriterioNot(null));
	}

	@Test
	public void criteriosAnidadosSobreElCatalogo() {
		// (Alojamiento OR Excursion) AND NOT nombre contiene "museo"
		CriterioBusqueda criterio = new CriterioAnd(List.of(
				new CriterioOr(List.of(
						new CriterioCategoria(Categoria.ALOJAMIENTO),
						new CriterioCategoria(Categoria.EXCURSION))),
				new CriterioNot(new CriterioNombre("museo"))));

		assertEquals(List.of(hotel, tourAgotado), catalogo.buscar(criterio));
	}
}