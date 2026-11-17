package unqTravel.comprobante;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.ItemReserva;

class ComprobanteTest extends SetUpTest {

    @Test
    void testCreacionFacturaExitosaYGetters() {
        List<ItemReserva> items = List.of(itemHotel);
        double monto = 15000.0;

        Factura factura = new Factura("123", hoy, cliente, items, monto);

        // Verificamos el prefijo agregado por Factura y los atributos heredados de Comprobante
        assertEquals("FAC123", factura.codigo);
        assertEquals(hoy, factura.getFecha());
        assertEquals(cliente, factura.getCliente());
        assertEquals(items, factura.getItems());
        assertEquals(monto, factura.getMonto());
    }

    @Test
    void testCreacionVoucherExitosoConYSinFechaFin() {
        List<ItemReserva> items = List.of(itemHotel);
        LocalDate fecFin = hoy.plusDays(5);
        double monto = 20000.0;

        // Voucher con fecha de fin
        Voucher voucherConFin = new Voucher("456", hoy, cliente, items, monto, fecFin);
        assertEquals("VOU456", voucherConFin.codigo);
        assertEquals(fecFin, voucherConFin.getFecFin());
        assertEquals(monto, voucherConFin.getMonto());

        // Voucher sin fecha de fin (sobrecarga)
        Voucher voucherSinFin = new Voucher("789", hoy, cliente, items, 10000.0);
        assertEquals("VOU789", voucherSinFin.codigo);
        assertNull(voucherSinFin.getFecFin());
    }

    @Test
    void testComprobanteLanzaExcepcionSiFechaEsNula() {
        List<ItemReserva> items = Collections.emptyList();
        
        // Validamos que el Objects.requireNonNull de la clase abstracta Comprobante funcione
        assertThrows(NullPointerException.class, () -> {
            new Factura("123", null, cliente, items, 1000.0);
        });
    }

    @Test
    void testComprobanteLanzaExcepcionSiClienteEsNulo() {
        List<ItemReserva> items = Collections.emptyList();
        
        assertThrows(NullPointerException.class, () -> {
            new Factura("123", hoy, null, items, 1000.0);
        });
    }
}