package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.reserva.Cliente;
import unqTravel.reserva.Reserva;

class TransferenciaBancariaTest {

    private TransferenciaBancaria transferencia;
    private TransferenciaBancariaAPI apiMock;
    private Reserva reservaMock;

    @BeforeEach
    void setUp() {
        apiMock = mock(TransferenciaBancariaAPI.class);
        reservaMock = mock(Reserva.class);

        transferencia = new TransferenciaBancaria("0123456789012345678901", "viajes.unq", apiMock);
    }

    @Test
    void testProcesarPagoExitoso() {
    	
    	Cliente clienteMock = mock(Cliente.class);
        when(reservaMock.getCliente()).thenReturn(clienteMock);
        when(reservaMock.getCostoTotal()).thenReturn(12000.0);

        transferencia.procesarPago(reservaMock);

        verify(apiMock).validarDatos("0123456789012345678901", "viajes.unq");
        verify(apiMock).ejecutar(12000.0);
        
        verify(reservaMock).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaPorDatosInvalidos() {
        doThrow(new RuntimeException("CBU o Alias inválido"))
            .when(apiMock).validarDatos(anyString(), anyString());

        assertThrows(RuntimeException.class, () -> {
            transferencia.procesarPago(reservaMock);
        });

        verify(apiMock).validarDatos("0123456789012345678901", "viajes.unq");
        verify(apiMock, never()).ejecutar(anyDouble());
        verify(reservaMock, never()).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaEnEjecucion() {
        when(reservaMock.getCostoTotal()).thenReturn(12000.0);

        doThrow(new RuntimeException("Error en transferencia bancaria"))
            .when(apiMock).ejecutar(12000.0);

        assertThrows(RuntimeException.class, () -> {
            transferencia.procesarPago(reservaMock);
        });

        verify(apiMock).validarDatos("0123456789012345678901", "viajes.unq");
        verify(apiMock).ejecutar(12000.0);
        verify(reservaMock, never()).addComprobante(any());
    }
}