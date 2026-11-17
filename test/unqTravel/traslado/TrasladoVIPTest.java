package unqTravel.traslado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

public class TrasladoVIPTest extends SetUpTest {

	@Test
	public void reservaSinItemsConsultaLaApiConCero() {
		when(trasladoVipApi.calcularCosto(0.0)).thenReturn(1500.0);

		assertEquals(1500.0, trasladoVip.getCostoTraslado(reservaVip));

		verify(trasladoVipApi).calcularCosto(0.0);
	}

	@Test
	public void elCostoTotalDeLaReservaSumaServiciosMasTrasladoVip() {
		reservaVip.addItemReserva(itemHotel);
		when(trasladoVipApi.calcularCosto(30000.0)).thenReturn(4500.0);

		assertEquals(34500.0, reservaVip.getCostoTotal());

		verify(trasladoVipApi).calcularCosto(30000.0);
	}

	@Test
	public void elCostoUsaElPrecioFinalConDescuentoPromocional() {
		reservaVip.addItemReserva(new ItemReserva(desde, hasta, 1, vuelo));
		when(trasladoVipApi.calcularCosto(90000.0)).thenReturn(13500.0);

		assertEquals(13500.0, trasladoVip.getCostoTraslado(reservaVip));
	}
}