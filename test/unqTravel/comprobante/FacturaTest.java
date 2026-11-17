package unqTravel.comprobante;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

class FacturaTest extends SetUpTest {

    @Test
    void testCreacionFacturaExitosa() {
        List<ItemReserva> items = List.of(itemHotel, itemExcursion);
        double monto = 19000.0;

        Factura factura = new Factura("100", hoy, cliente, items, monto);

        assertEquals("FAC100", factura.codigo);
        assertEquals(hoy, factura.getFecha());
        assertEquals(cliente, factura.getCliente());
        assertEquals(items, factura.getItems());
        assertEquals(monto, factura.getMonto());
    }

    @Test
    void testCreacionFacturaConFechaNulaLanzaExcepcion() {
        List<ItemReserva> items = Collections.emptyList();

        assertThrows(NullPointerException.class, () -> {
            new Factura("100", null, cliente, items, 5000.0);
        });
    }

    @Test
    void testCreacionFacturaConClienteNuloLanzaExcepcion() {
        List<ItemReserva> items = Collections.emptyList();

        assertThrows(NullPointerException.class, () -> {
            new Factura("100", hoy, null, items, 5000.0);
        });
    }
}