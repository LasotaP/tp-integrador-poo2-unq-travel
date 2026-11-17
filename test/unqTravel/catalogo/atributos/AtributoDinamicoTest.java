package unqTravel.catalogo.atributos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;

public class AtributoDinamicoTest extends SetUpTest {

	@Test
	public void constructorYGetters() {
		assertEquals("CapacidadMaxima", capacidadMaxima.getNombre());
		assertTrue(capacidadMaxima.esObligatorio());
		assertFalse(politicaMascotas.esObligatorio());
	}

	@Test
	public void nombreInvalidoLanzaExcepcion() {
		assertThrows(ServicioInvalidoException.class, () -> new AtributoDinamico(" ", true, new TipoTexto()));
	}

	@Test
	public void tipoNuloLanzaExcepcion() {
		assertThrows(ServicioInvalidoException.class, () -> new AtributoDinamico("Rating", true, null));
	}

	@Test
	public void setValorYTieneValor() {
		assertFalse(fechaApertura.tieneValor());

		fechaApertura.setValor("2026-11-12");

		assertTrue(fechaApertura.tieneValor());
		assertEquals("2026-11-12", fechaApertura.getValor());
	}

	@Test
	public void esValidoSinValorDependeDeLaObligatoriedad() {
		assertFalse(fechaApertura.esValido()); // obligatorio y sin valor
		assertTrue(notaInterna.esValido());    // opcional y sin valor
	}

	@Test
	public void esValidoConValorDependeDelTipo() {
		assertTrue(capacidadMaxima.esValido());       // "4" es numerico
		assertFalse(superficieInvalida.esValido());   // "abc" no es numerico
	}

}