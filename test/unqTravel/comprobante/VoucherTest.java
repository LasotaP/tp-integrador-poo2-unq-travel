package unqTravel.comprobante;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

class VoucherTest extends SetUpTest {

    @Test
    void testCreacionVoucherConFechaFinExitosa() {
        List<ItemReserva> items = List.of(itemHotel, itemExcursion);
        double monto = 22000.0;

        // Reutilizamos hoy, cliente, itemHotel, itemExcursion y hasta (hoy + 15) de SetUpTest
        Voucher voucher = new Voucher("200", hoy, cliente, items, monto, hasta);

        assertEquals("VOU200", voucher.codigo);
        assertEquals(hoy, voucher.getFecha());
        assertEquals(cliente, voucher.getCliente());
        assertEquals(items, voucher.getItems());
        assertEquals(monto, voucher.getMonto());
        assertEquals(hasta, voucher.getFecFin());
    }

    @Test
    void testCreacionVoucherSinFechaFinExitosa() {
        List<ItemReserva> items = List.of(itemHotel);
        double monto = 15000.0;

        Voucher voucher = new Voucher("201", hoy, cliente, items, monto);

        assertEquals("VOU201", voucher.codigo);
        assertEquals(hoy, voucher.getFecha());
        assertEquals(cliente, voucher.getCliente());
        assertEquals(items, voucher.getItems());
        assertEquals(monto, voucher.getMonto());
        assertNull(voucher.getFecFin());
    }

    @Test
    void testCreacionVoucherConFechaNulaLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> {
            new Voucher("202", null, cliente, Collections.emptyList(), 5000.0, hasta);
        });
    }

    @Test
    void testCreacionVoucherConClienteNuloLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> {
            new Voucher("203", hoy, null, Collections.emptyList(), 5000.0);
        });
    }
}