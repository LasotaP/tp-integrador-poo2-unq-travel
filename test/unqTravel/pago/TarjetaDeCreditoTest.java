package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.Cliente;
import unqTravel.reserva.Reserva;

class TarjetaDeCreditoTest extends SetUpTest {

    private Reserva reservaMock;
    private Cliente clienteMock;

    @BeforeEach
    void setUpLocal() {
        reservaMock = mock(Reserva.class);
        clienteMock = mock(Cliente.class);
    }

    @Test
    void testProcesarPagoExitoso() {
        when(reservaMock.getCostoTotal()).thenReturn(15000.0);
        when(reservaMock.getNumeroReserva()).thenReturn("RES-123");
        when(reservaMock.getCliente()).thenReturn(clienteMock); 
        when(reservaMock.getItemsReserva()).thenReturn(Collections.emptyList());

        tarjeta.procesarPago(reservaMock);

        verify(tarjetaApi).validarDatos(anyString(), anyString(), any(LocalDate.class));
        verify(tarjetaApi).preAutorizacion(15000.0);
        verify(tarjetaApi).transferir(15000.0);

        verify(reservaMock).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaEnValidacionDatos() {
        doThrow(new RuntimeException("Datos de tarjeta inválidos"))
            .when(tarjetaApi).validarDatos(anyString(), anyString(), any(LocalDate.class));

        assertThrows(RuntimeException.class, () -> {
            tarjeta.procesarPago(reservaMock);
        });

        verify(tarjetaApi).validarDatos(anyString(), anyString(), any(LocalDate.class));
        verify(tarjetaApi, never()).preAutorizacion(anyDouble());
        verify(tarjetaApi, never()).transferir(anyDouble());
        verify(reservaMock, never()).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaEnPreAutorizacion() {
        when(reservaMock.getCostoTotal()).thenReturn(15000.0);

        doThrow(new RuntimeException("Fondos insuficientes o pre-autorización rechazada"))
            .when(tarjetaApi).preAutorizacion(15000.0);

        assertThrows(RuntimeException.class, () -> {
            tarjeta.procesarPago(reservaMock);
        });

        verify(tarjetaApi).validarDatos(anyString(), anyString(), any(LocalDate.class));
        verify(tarjetaApi).preAutorizacion(15000.0);
        verify(tarjetaApi, never()).transferir(anyDouble());
        verify(reservaMock, never()).addComprobante(any());
    }
}