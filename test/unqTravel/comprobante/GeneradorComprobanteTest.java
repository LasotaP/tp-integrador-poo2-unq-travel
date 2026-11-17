package unqTravel.comprobante;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.CodigoEstado;
import unqTravel.reserva.Estado;
import unqTravel.reserva.Reserva;

class GeneradorComprobanteTest extends SetUpTest {

    private GeneradorComprobante generador;
    private Reserva reservaMock;
    private Estado estadoAnteriorMock;
    private Estado estadoFinalizadaMock;
    private Estado estadoPendienteMock;

    @BeforeEach
    void setUpLocal() {
        generador = new GeneradorComprobante();
        
        reservaMock = mock(Reserva.class);
        estadoAnteriorMock = mock(Estado.class);
        
        estadoFinalizadaMock = mock(Estado.class);
        when(estadoFinalizadaMock.codigo()).thenReturn(CodigoEstado.FINALIZADA);

        estadoPendienteMock = mock(Estado.class);
        when(estadoPendienteMock.codigo()).thenReturn(CodigoEstado.SOLICITUD_PENDIENTE);

        // Configuramos datos básicos de la reserva mock para que no fallen al crear Factura/Voucher
        when(reservaMock.getCliente()).thenReturn(cliente);
        when(reservaMock.getNumeroReserva()).thenReturn("RES-999");
        when(reservaMock.getCostoTotal()).thenReturn(25000.0);
        when(reservaMock.getItemsReserva()).thenReturn(List.of(itemHotel));
    }
    @Test
    void testNotificarConEstadoFinalizadaGeneraFacturaYVoucher() {
        // Ejecutamos el método notificar del Observer con el estado FINALIZADA
        generador.notificar(reservaMock, estadoAnteriorMock, estadoFinalizadaMock);

        // Verificamos que se hayan agregado EXACTAMENTE dos comprobantes a la reserva (Factura y Voucher)
        verify(reservaMock, times(2)).addComprobante(any(Comprobante.class));
        
        // Verificamos que se consultaron los datos básicos de la reserva necesarios para la generación
        verify(reservaMock, atLeastOnce()).getCliente();
        verify(reservaMock, atLeastOnce()).getNumeroReserva();
        verify(reservaMock, atLeastOnce()).getCostoTotal();
        verify(reservaMock, atLeastOnce()).getItemsReserva();
    }

    @Test
    void testNotificarConOtroEstadoNoGeneraComprobantes() {
        generador.notificar(reservaMock, estadoAnteriorMock, estadoPendienteMock);
        verify(reservaMock, never()).addComprobante(any(Comprobante.class));
    }
}