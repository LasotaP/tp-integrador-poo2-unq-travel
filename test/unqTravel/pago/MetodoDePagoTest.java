
package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unqTravel.comprobante.Comprobante;
import unqTravel.comprobante.Factura;
import unqTravel.reserva.Cliente;
import unqTravel.reserva.ItemReserva;
import unqTravel.reserva.Reserva;

@ExtendWith(MockitoExtension.class)
class MetodoDePagoTest {

    @Mock
    private Reserva reserva;

    @Test
    void procesarPagoEjecutaLosPasosEnElOrdenCorrecto() {

        List<String> pasos = new ArrayList<>();

        MetodoDePago metodo = new MetodoDePago() {

            @Override
            public void validarDatos(Reserva reserva) {
                pasos.add("validar");
            }

            @Override
            public void reservarFondos(Reserva reserva) {
                pasos.add("reservar");
            }

            @Override
            public void ejecutarTransaccion(Reserva reserva) {
                pasos.add("ejecutar");
            }

            @Override
            public void notificarResultado(Reserva reserva) {
                pasos.add("notificar");
            }
        };

        metodo.procesarPago(reserva);

        assertEquals(
            List.of("validar", "reservar", "ejecutar", "notificar"),
            pasos
        );
    }

    @Test
    void procesarPagoSeInterrumpeSiFallaLaValidacion() {

        List<String> pasos = new ArrayList<>();

        MetodoDePago metodo = new MetodoDePago() {

            @Override
            public void validarDatos(Reserva reserva) {
                pasos.add("validar");
                throw new IllegalArgumentException("Datos inválidos");
            }

            @Override
            public void reservarFondos(Reserva reserva) {
                pasos.add("reservar");
            }

            @Override
            public void ejecutarTransaccion(Reserva reserva) {
                pasos.add("ejecutar");
            }

            @Override
            public void notificarResultado(Reserva reserva) {
                pasos.add("notificar");
            }
        };

        assertThrows(
            IllegalArgumentException.class,
            () -> metodo.procesarPago(reserva)
        );

        assertEquals(List.of("validar"), pasos);
    }

    @Test
    void notificarResultadoGeneraUnaFacturaYLaAgregaALaReserva() {

        Cliente cliente = mock(Cliente.class);
        ItemReserva item = mock(ItemReserva.class);
        List<ItemReserva> items = List.of(item);

        when(reserva.getNumeroReserva()).thenReturn("123");
        when(reserva.getCliente()).thenReturn(cliente);
        when(reserva.getItemsReserva()).thenReturn(items);
        when(reserva.getCostoTotal()).thenReturn(15000.0);

        MetodoDePago metodo = new MetodoDePago() {

            @Override
            public void validarDatos(Reserva reserva) {}

            @Override
            public void ejecutarTransaccion(Reserva reserva) {}
        };

        metodo.notificarResultado(reserva);

        ArgumentCaptor<Comprobante> captor =
            ArgumentCaptor.forClass(Comprobante.class);

        verify(reserva).addComprobante(captor.capture());

        Comprobante comprobante = captor.getValue();

        assertInstanceOf(Factura.class, comprobante);
        assertEquals(15000.0, comprobante.getMonto());
        assertEquals(cliente, comprobante.getCliente());
        assertEquals(items, comprobante.getItems());
        assertEquals(LocalDate.now(), comprobante.getFecha());
    }
}
