package unqTravel.traslado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

public class TrasladoRecepcionTest extends SetUpTest {

	@Test
	public void elCostoEsCeroConUnaReservaVacia() {
		assertEquals(0.0, trasladoRecepcion.getCostoTraslado(reservaRecepcion));
	}

	@Test
	public void elCostoEsCeroAunqueLaReservaTengaItemsCaros() {
		reservaRecepcion.addItemReserva(new ItemReserva(desde, hasta, 4, vuelo));

		assertEquals(0.0, trasladoRecepcion.getCostoTraslado(reservaRecepcion));
	}

	@Test
	public void elCostoTotalDeLaReservaEsSoloElDeLosServicios() {
		reservaRecepcion.addItemReserva(itemHotel);

		assertEquals(30000.0, reservaRecepcion.getCostoTotal());
		assertEquals(reservaRecepcion.getCostoReserva(), reservaRecepcion.getCostoTotal());
	}
}