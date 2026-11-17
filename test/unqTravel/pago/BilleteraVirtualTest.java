package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.Reserva;

class BilleteraVirtualTest extends SetUpTest {

    private BilleteraVirtual billetera;
    private BilleteraVirtualAPI apiMock;
    private Reserva reservaMock;



    @Test
    void testProcesarPagoExitoso() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        billetera.procesarPago(reservaMock);

        verify(apiMock).validarSaldoSuficiente(8500.0);
        verify(apiMock).acreditar(8500.0);
        verify(apiMock).notificar();
    }

    @Test
    void testProcesarPagoFallaPorSaldoInsuficiente() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        doThrow(new RuntimeException("Saldo insuficiente en cuenta"))
            .when(apiMock).validarSaldoSuficiente(8500.0);


        assertThrows(RuntimeException.class, () -> {
            billetera.procesarPago(reservaMock);
        });

        verify(apiMock).validarSaldoSuficiente(8500.0);
        verify(apiMock, never()).acreditar(anyDouble());
        verify(apiMock, never()).notificar();
    }

    @Test
    void testProcesarPagoFallaEnAcreditacion() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        // La validación pasa, pero la acreditación falla
        doThrow(new RuntimeException("Error en acreditación en tiempo real"))
            .when(apiMock).acreditar(8500.0);

        assertThrows(RuntimeException.class, () -> {
            billetera.procesarPago(reservaMock);
        });

        verify(apiMock).validarSaldoSuficiente(8500.0);
        verify(apiMock).acreditar(8500.0);
        verify(apiMock, never()).notificar();
    }
    
    
}