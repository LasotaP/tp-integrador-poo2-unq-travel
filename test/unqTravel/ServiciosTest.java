package unqTravel;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ServiciosTest extends SetUpTest{

	
	// CUPOS
	@Test
	void testCupos() {
		assertEquals(2, entradaCerro.cupoDisponible(LocalDate.of(1999, 3, 30)));
		assertTrue(entradaCerro.hayCupo(LocalDate.of(1999, 3, 30),2));
		
		//decremento cupo y verifico nuevamente
		entradaCerro.decrementarCupo(LocalDate.of(1999, 3, 30), 1);
		assertTrue(entradaCerro.hayCupo(LocalDate.of(1999, 3, 30),1));
		
		// ERROR al querer decrementar el cupo
		assertThrows(IllegalArgumentException.class,
				() -> entradaCerro.decrementarCupo(LocalDate.of(1999, 3, 30), 2));
	}
	
	
	// SERVICIOS INDIVIDULES Y PAQUETES TURISTICOS
	@Test
	void testGettersServiciosIndividuales() {
		assertEquals("Entrada al cerro", entradaCerro.getNombre());
		assertEquals("Entrada al cerro", entradaCerro.getDescripcion());
		assertEquals(3000.0, entradaCerro.getPrecioBaseCalculado());
		
	}
	
	

}