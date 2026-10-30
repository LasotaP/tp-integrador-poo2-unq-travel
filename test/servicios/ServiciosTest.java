package servicios;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServiciosTest extends SetUpTest{

	@Test
	void testGettersServicioIndividual() {
		assertEquals("Entrada al cerro", entradaCerro.getNombre());
	}

}
