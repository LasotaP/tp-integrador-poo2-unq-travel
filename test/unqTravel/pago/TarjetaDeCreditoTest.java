package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.reserva.Cliente;
import unqTravel.reserva.Reserva;

class TarjetaDeCreditoTest {

    private TarjetaDeCredito tarjeta;
    private TarjetaDeCreditoAPI apiMock;
    private Reserva reservaMock;
    private LocalDate fechaVencimiento;
    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        apiMock = mock(TarjetaDeCreditoAPI.class);
        reservaMock = mock(Reserva.class);
        fechaVencimiento = LocalDate.of(2028, 12, 31);
        clienteMock = mock(Cliente.class);


        tarjeta = new TarjetaDeCredito("4500123456789010", "123", fechaVencimiento, apiMock);
    }

    @Test
    void testProcesarPagoExitoso() {
       
        when(reservaMock.getCostoTotal()).thenReturn(15000.0);
        when(reservaMock.getNumeroReserva()).thenReturn("RES-123");
        when(reservaMock.getCliente()).thenReturn(clienteMock); 
        when(reservaMock.getItemsReserva()).thenReturn(Collections.emptyList());

      
        tarjeta.procesarPago(reservaMock);

        
        verify(apiMock).validarDatos("4500123456789010", "123", fechaVencimiento);
        verify(apiMock).preAutorizacion(15000f);
        verify(apiMock).transferir(15000f);


        verify(reservaMock).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaEnValidacionDatos() {

        doThrow(new RuntimeException("Datos de tarjeta inválidos"))
            .when(apiMock).validarDatos(anyString(), anyString(), any(LocalDate.class));


        assertThrows(RuntimeException.class, () -> {
            tarjeta.procesarPago(reservaMock);
        });

        verify(apiMock).validarDatos(anyString(), anyString(), any(LocalDate.class));
        verify(apiMock, never()).preAutorizacion(anyFloat());
        verify(apiMock, never()).transferir(anyFloat());
        verify(reservaMock, never()).addComprobante(any());
    }

    @Test
    void testProcesarPagoFallaEnPreAutorizacion() {
        when(reservaMock.getCostoTotal()).thenReturn(15000.0);

        doThrow(new RuntimeException("Fondos insuficientes o pre-autorización rechazada"))
            .when(apiMock).preAutorizacion(15000f);

        assertThrows(RuntimeException.class, () -> {
            tarjeta.procesarPago(reservaMock);
        });

        // Verificamos que pasó por validación y pre-autorización, pero se cortó antes de transferir
        verify(apiMock).validarDatos(anyString(), anyString(), any(LocalDate.class));
        verify(apiMock).preAutorizacion(15000f);
        verify(apiMock, never()).transferir(anyFloat());
        verify(reservaMock, never()).addComprobante(any());
    }
}