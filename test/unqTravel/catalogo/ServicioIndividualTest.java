package unqTravel.catalogo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.atributos.ServicioInvalidoException;
import unqTravel.reportes.Visitante;

public class ServicioIndividualTest extends SetUpTest {

	@Test
	public void constructorYGetters() {
		assertEquals("HOT-001", hotel.getId());
		assertEquals("Noche de Hotel Central", hotel.getNombre());
		assertEquals("Hotel en el centro", hotel.getDescripcion());
		assertEquals("Operador Sur", hotel.getProveedor());
		assertEquals(Categoria.ALOJAMIENTO, hotel.getCategoria());
		assertEquals("Av. Corrientes", hotel.getDireccion().getCalle());
		assertEquals(1234, hotel.getDireccion().getNumero());
		assertEquals(15000.0, hotel.getPrecioBase());
	}

	@Test
	public void precioFinalAplicaElDescuentoPromocional() {
		assertEquals(0.10, vuelo.getDescuentoPromocional());
		assertEquals(90000.0, vuelo.getPrecioFinal(), 0.001);
		assertEquals(15000.0, hotel.getPrecioFinal(), 0.001); // sin descuento
	}

	@Test
	public void constructorInvalidoLanzaExcepcion() {
		Cupo cupo = cupoConTotal(1);
		assertThrows(ServicioInvalidoException.class, () -> new ServicioIndividual(" ", "n", "d", direccionHotel, "p", Categoria.VUELO, 1, cupo, 0));
		assertThrows(ServicioInvalidoException.class, () -> new ServicioIndividual("i", "n", "d", direccionHotel, "p", null, 1, cupo, 0));
		assertThrows(ServicioInvalidoException.class, () -> new ServicioIndividual("i", "n", "d", direccionHotel, "p", Categoria.VUELO, 1, null, 0));
		assertThrows(ServicioInvalidoException.class, () -> new ServicioIndividual("i", "n", "d", direccionHotel, "p", Categoria.VUELO, 0, cupo, 0));
		assertThrows(ServicioInvalidoException.class, () -> new ServicioIndividual("i", "n", "d", direccionHotel, "p", Categoria.VUELO, 1, cupo, 1.5));
	}

	@Test
	public void agregarAtributoDinamico() {
		assertEquals(3, hotel.getAtributosDinamicos().size()); // los del SetUp

		hotel.agregarAtributoDinamico(notaInterna);
		hotel.agregarAtributoDinamico(notaInterna); // mismo nombre: reemplaza, no duplica

		assertEquals(4, hotel.getAtributosDinamicos().size());
		assertThrows(ServicioInvalidoException.class, () -> hotel.agregarAtributoDinamico(null));
	}

	@Test
	public void validarServicio() {
		assertTrue(hotel.validarServicio());
		assertTrue(excursion.validarServicio()); // sin atributos dinamicos

		hotel.agregarAtributoDinamico(superficieInvalida);

		assertFalse(hotel.validarServicio());
	}

	@Test
	public void contiene() {
		assertTrue(hotel.contiene(hotel));
		assertFalse(hotel.contiene(excursion));
	}

	@Test
	public void manejoDeCupo() {
		assertEquals(10, hotel.cupoDisponible(desde));
		assertTrue(hotel.hayCupo(desde, hasta, 10));
		assertFalse(hotel.hayCupo(desde, hasta, 11));

		hotel.reservarCupo(desde, hasta, 4);
		assertEquals(6, hotel.cupoDisponible(desde));
		assertEquals(4, hotel.cupoReservado(desde));
		assertEquals(10, hotel.cupoTotal(desde));

		hotel.restaurarCupo(desde, hasta, 4);
		assertEquals(10, hotel.cupoDisponible(desde));
	}

	@Test
	public void aceptar() {
		Visitante visitante = mock(Visitante.class);

		hotel.aceptar(visitante);

		verify(visitante).visitarServicio(hotel);
	}
}