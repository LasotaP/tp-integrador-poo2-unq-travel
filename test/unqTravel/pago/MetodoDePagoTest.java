package unqTravel.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.Reserva;

class MetodoDePagoTest extends SetUpTest {

    private MetodoDePago metodoDePagoStub;
    private Reserva reservaMock;

    @BeforeEach
    void setUpLocal() {
        // mock de reserva para controlar las respuestas de costos, número y cliente
        reservaMock = mock(Reserva.class);
        when(reservaMock.getCliente()).thenReturn(cliente);
        when(reservaMock.getNumeroReserva()).thenReturn("RES-TEST-01");
        when(reservaMock.getCostoTotal()).thenReturn(5000.0);

        // implementación concreta stub de la clase abstracta MetodoDePago
        metodoDePagoStub = new MetodoDePago() {
            @Override
            public void validarDatos(Reserva reserva) {
           //simulacion
            }

            @Override
            public void ejecutarTransaccion(Reserva reserva) {
            	//simulacion
            }
        };
    }

    @Test
    void testTemplateMethodEjecutaPasosEnOrdenCorrecto() {
        metodoDePagoStub.procesarPago(reservaMock);

        // Verificamos q se crea la factura
        // y agrega a la reserva
        verify(reservaMock).addComprobante(any());
    }

    @Test
    void testReservarFondosPorDefectoNoHaceNadaPeroTieneCobertura() {
    
        assertDoesNotThrow(() -> {
            metodoDePagoStub.reservarFondos(reservaMock);
        });
    }

    @Test
    void testNotificarResultadoCreaFacturaYAgregaAReserva() {
        metodoDePagoStub.notificarResultado(reservaMock);

        verify(reservaMock).addComprobante(any());
        verify(reservaMock).getNumeroReserva();
        verify(reservaMock).getCliente();
        verify(reservaMock).getCostoTotal();
    }
}