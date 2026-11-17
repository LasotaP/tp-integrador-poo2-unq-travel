package unqTravel.catalogo.atributos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;

public class TiposValorTest extends SetUpTest {

	@Test
	public void tipoTextoEsConsistente() {
		assertTrue(new TipoTexto().esConsistente("hola"));
		assertFalse(new TipoTexto().esConsistente("  "));
		assertFalse(new TipoTexto().esConsistente(null));
	}

	@Test
	public void tipoNumericoEsConsistente() {
		assertTrue(new TipoNumerico().esConsistente("4"));
		assertTrue(new TipoNumerico().esConsistente("4.5"));
		assertFalse(new TipoNumerico().esConsistente("abc"));
	}

	@Test
	public void tipoBooleanoEsConsistente() {
		assertTrue(new TipoBooleano().esConsistente("true"));
		assertTrue(new TipoBooleano().esConsistente("FALSE"));
		assertFalse(new TipoBooleano().esConsistente("si"));
	}

	@Test
	public void tipoFechaEsConsistente() {
		assertTrue(new TipoFecha().esConsistente("2026-11-12"));;
		assertFalse(new TipoFecha().esConsistente("12/11/2026"));
		assertFalse(new TipoFecha().esConsistente(null));
	}
}