package unqTravel.reserva;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;

/** itemHotel = desde..hasta, 2 pasajeros, hotel ($15.000, cupo 10). */
class ItemReservaTest extends SetUpTest {

    // ---------- construccion y getters ----------

    @Test
    void unItemConoceSusDatos() {
        assertEquals(desde, itemHotel.getFechaInicio());
        assertEquals(hasta, itemHotel.getFechaFin());
        assertEquals(2, itemHotel.getCantidadPasajeros());
        assertSame(hotel, itemHotel.getServicio());
    }

    @Test
    void fechaDeInicioNulaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(null, hasta, 1, hotel));
    }

    @Test
    void fechaDeFinNulaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(desde, null, 1, hotel));
    }

    @Test
    void fechaDeFinAnteriorALaDeInicioEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(hasta, desde, 1, hotel));
    }

    @Test
    void sePuedeReservarUnSoloDia() {
        assertDoesNotThrow(() -> new ItemReserva(desde, desde, 1, hotel));
    }

    @Test
    void cantidadNulaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(desde, hasta, null, hotel));
    }

    @Test
    void cantidadCeroEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(desde, hasta, 0, hotel));
    }

    @Test
    void cantidadNegativaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(desde, hasta, -2, hotel));
    }

    @Test
    void servicioNuloEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new ItemReserva(desde, hasta, 1, null));
    }

    // ---------- cupo ----------

    @Test
    void reservarCupoDescuentaLaCantidadEnCadaDiaDelPeriodo() {
        itemHotel.reservarCupo();

        assertEquals(8, hotel.cupoDisponible(desde));
        assertEquals(8, hotel.cupoDisponible(desde.plusDays(2)));
        assertEquals(8, hotel.cupoDisponible(hasta));
        assertEquals(10, hotel.cupoDisponible(desde.minusDays(1)));
        assertEquals(10, hotel.cupoDisponible(hasta.plusDays(1)));
    }

    @Test
    void restaurarCupoDevuelveLaCantidadReservada() {
        itemHotel.reservarCupo();

        itemHotel.restaurarCupo();

        assertEquals(10, hotel.cupoDisponible(desde));
        assertEquals(10, hotel.cupoDisponible(hasta));
    }

    @Test
    void noSePuedeRestaurarCupoQueNoSeReservo() {
        assertThrows(IllegalArgumentException.class, () -> itemHotel.restaurarCupo());
    }

    @Test
    void noSePuedeReservarCupoSiNoHayDisponibilidad() {
        ItemReserva item = new ItemReserva(desde, hasta, 1, tourAgotado);

        assertThrows(IllegalArgumentException.class, () -> item.reservarCupo());
    }

    // ---------- costo ----------

    @Test
    void elCostoDelItemEsElPrecioFinalDelServicioPorLaCantidad() {
        assertEquals(30000.0, itemHotel.costoTotalaItem(), 0.001); // 15.000 x 2
    }

    @Test
    void elCostoDelItemUsaElPrecioConDescuentoPromocionalDelServicio() {
        ItemReserva item = new ItemReserva(desde, hasta, 2, vuelo); // 100.000 con 10 % = 90.000

        assertEquals(180000.0, item.costoTotalaItem(), 0.001);
    }

    // ---------- periodo ----------

    @Test
    void estaEnPeriodoSiElPeriodoLoContiene() {
        assertTrue(itemHotel.estaEnPeriodo(desde.minusDays(2), hasta.plusDays(2)));
    }

    @Test
    void estaEnPeriodoSiElPeriodoEstaContenidoEnElItem() {
        assertTrue(itemHotel.estaEnPeriodo(desde.plusDays(1), desde.plusDays(2)));
    }

    @Test
    void estaEnPeriodoSiSoloSolapaAlComienzo() {
        assertTrue(itemHotel.estaEnPeriodo(desde.minusDays(5), desde.plusDays(1)));
    }

    @Test
    void estaEnPeriodoSiSoloSolapaAlFinal() {
        assertTrue(itemHotel.estaEnPeriodo(hasta.minusDays(1), hasta.plusDays(5)));
    }
 
    @Test
    void losBordesDelPeriodoSonInclusivos() {
        assertTrue(itemHotel.estaEnPeriodo(desde.minusDays(5), desde));
        assertTrue(itemHotel.estaEnPeriodo(hasta, hasta.plusDays(5)));
    }

    @Test
    void noEstaEnPeriodoSiTerminaAntesDeQueEmpiece() {
        assertFalse(itemHotel.estaEnPeriodo(desde.minusDays(5), desde.minusDays(1)));
    }

    @Test
    void noEstaEnPeriodoSiEmpiezaDespuesDeQueTermine() {
        assertFalse(itemHotel.estaEnPeriodo(hasta.plusDays(1), hasta.plusDays(5)));
    }
}
