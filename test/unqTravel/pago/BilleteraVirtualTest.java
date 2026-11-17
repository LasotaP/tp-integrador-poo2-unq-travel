package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.Reserva;

class BilleteraVirtualTest extends SetUpTest {

    private Reserva reservaMock;

    @BeforeEach
    void setUpLocal() {
        reservaMock = mock(Reserva.class);
    }

    @Test
    void testProcesarPagoExitoso() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        // Usamos 'billetera' y 'billeteraApi' que ya provee SetUpTest
        billetera.procesarPago(reservaMock);

        verify(billeteraApi).validarSaldoSuficiente(8500.0);
        verify(billeteraApi).acreditar(8500.0);
        verify(billeteraApi).notificar();
    }

    @Test
    void testProcesarPagoFallaPorSaldoInsuficiente() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        doThrow(new RuntimeException("Saldo insuficiente en cuenta"))
            .when(billeteraApi).validarSaldoSuficiente(8500.0);

        assertThrows(RuntimeException.class, () -> {
            billetera.procesarPago(reservaMock);
        });

        verify(billeteraApi).validarSaldoSuficiente(8500.0);
        verify(billeteraApi, never()).acreditar(anyDouble());
        verify(billeteraApi, never()).notificar();
    }

    @Test
    void testProcesarPagoFallaEnAcreditacion() {
        when(reservaMock.getCostoTotal()).thenReturn(8500.0);

        // La validación pasa, pero la acreditación falla
        doThrow(new RuntimeException("Error en acreditación en tiempo real"))
            .when(billeteraApi).acreditar(8500.0);

        assertThrows(RuntimeException.class, () -> {
            billetera.procesarPago(reservaMock);
        });

        verify(billeteraApi).validarSaldoSuficiente(8500.0);
        verify(billeteraApi).acreditar(8500.0);
        verify(billeteraApi, never()).notificar();
    }
}