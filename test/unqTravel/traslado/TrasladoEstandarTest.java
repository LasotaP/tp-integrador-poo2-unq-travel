package unqTravel.traslado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

public class TrasladoEstandarTest extends SetUpTest {

	@Test
	public void reservaSinItemsConsultaConCeroPasajeros() {
		when(plataformaTransporte.estimarTraslado(0, direccionHotel)).thenReturn(0.0);

		assertEquals(0.0, trasladoEstandar.getCostoTraslado(reservaRecepcion));

		verify(plataformaTransporte).estimarTraslado(0, direccionHotel);
	}

	@Test
	public void soloCuentaLosPasajerosDelServicioConfigurado() {
		reservaRecepcion.addItemReserva(itemHotel);
		reservaRecepcion.addItemReserva(itemExcursion);
		when(plataformaTransporte.estimarTraslado(2, direccionHotel)).thenReturn(1000.0);

		assertEquals(1000.0, trasladoEstandar.getCostoTraslado(reservaRecepcion));

		verify(plataformaTransporte).estimarTraslado(2, direccionHotel);
	}

	@Test
	public void sumaLosPasajerosDeVariosItemsDelMismoServicio() {
		reservaRecepcion.addItemReserva(itemHotel); // 2 pasajeros
		reservaRecepcion.addItemReserva(new ItemReserva(desde.plusDays(1), hasta, 3, hotel));
		when(plataformaTransporte.estimarTraslado(5, direccionHotel)).thenReturn(2500.0);

		assertEquals(2500.0, trasladoEstandar.getCostoTraslado(reservaRecepcion));
	}

	@Test
	public void cuentaLosPasajerosDeUnPaqueteQueContieneAlServicio() {
		// paqueteEscapada contiene al hotel, asi que sus pasajeros viajan al hotel
		reservaRecepcion.addItemReserva(new ItemReserva(desde, hasta, 4, paqueteEscapada));
		when(plataformaTransporte.estimarTraslado(4, direccionHotel)).thenReturn(3200.0);

		assertEquals(3200.0, trasladoEstandar.getCostoTraslado(reservaRecepcion));
	}

	// ---- Integracion con Reserva.getCostoTotal ----

	@Test
	public void elCostoTotalDeLaReservaSumaServiciosMasTraslado() {
		// reservaEstandar tiene como destino el autoCompacto (direccionAeropuerto)
		reservaEstandar.addItemReserva(new ItemReserva(desde, hasta, 3, autoCompacto));
		when(plataformaTransporte.estimarTraslado(3, direccionAeropuerto)).thenReturn(5000.0);

		assertEquals(29000.0, reservaEstandar.getCostoTotal());
	}
}