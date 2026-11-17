package unqTravel.reserva.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import unqTravel.SetUpTest;
import unqTravel.comprobante.Comprobante;
import unqTravel.comprobante.Factura;
import unqTravel.comprobante.Voucher;
import unqTravel.reserva.CodigoEstado;
import unqTravel.reserva.Reserva;

/**
 * Reserva + los tres observadores reales del SetUpTest (notificadorEmail, generadorComprobante,
 * fidelizacion). Solo el MailSender es un mock. Verifica que los subsistemas reaccionan de
 * forma independiente a cada cambio de estado.
 */ 
class ReservaNotificacionIntegracionTest extends SetUpTest {

    @BeforeEach
    void armarReservaConObservadores() {
        reserva = spy(reservaRecepcion); // spy: permite verificar los comprobantes registrados
        reserva.addItemReserva(itemHotel); // 2 x $15.000 = $30.000, traslado recepcion = $0

        reserva.agregarObservador(notificadorEmail);
        reserva.agregarObservador(generadorComprobante);
        reserva.agregarObservador(fidelizacion);
    }

    private List<String> mensajesEnviados(int cantidadEsperada) {
        ArgumentCaptor<String> mensajes = ArgumentCaptor.forClass(String.class);
        verify(mailSender, times(cantidadEsperada))
                .enviarMail(eq(cliente.getEmail()), anyString(), mensajes.capture(), isNull());
        return mensajes.getAllValues();
    }

    @Test
    void elCicloCompletoEnviaMailEnConfirmadaEnCursoYFinalizadaYNadaMas() {
        reserva.confirmar();
        reserva.hacerCheckIn();   // no notifica
        reserva.iniciarViaje();
        reserva.finalizarViaje();

        List<String> mensajes = mensajesEnviados(3);
        assertTrue(mensajes.get(0).contains("CONFIRMADA"));
        assertTrue(mensajes.get(1).contains("EN_CURSO"));
        assertTrue(mensajes.get(2).contains("FINALIZADA"));
    }

    @Test
    void alFinalizarSeGeneranLaFacturaYElVoucher() {
        reserva.confirmar();
        reserva.hacerCheckIn();
        reserva.iniciarViaje();
        verify(reserva, never()).addComprobante(any());

        reserva.finalizarViaje();

        ArgumentCaptor<Comprobante> comprobantes = ArgumentCaptor.forClass(Comprobante.class);
        verify(reserva, times(2)).addComprobante(comprobantes.capture());
        assertTrue(comprobantes.getAllValues().get(0) instanceof Factura);
        assertTrue(comprobantes.getAllValues().get(1) instanceof Voucher);
        comprobantes.getAllValues().forEach(c -> {
            assertEquals(30000.0, c.getMonto(), 0.001);
            assertSame(cliente, c.getCliente());
        });
    }

    @Test
    void cancelarUnaReservaConfirmadaEnviaElCuponLiberaElCupoYNoGeneraComprobantes() {
        reserva.confirmar();   // mail de CONFIRMADA
        assertEquals(8, hotel.cupoDisponible(desde));

        reserva.cancelar();    // cupon de Fidelizacion; NotificadorEmail NO envia nada por CANCELADA

        List<String> mensajes = mensajesEnviados(2);
        assertTrue(mensajes.get(0).contains("CONFIRMADA"));
        assertTrue(mensajes.get(1).contains("5%"));
        assertEquals(10, hotel.cupoDisponible(desde));
        verify(reserva, never()).addComprobante(any());
    }

    @Test
    void cancelarUnaReservaPendienteSoloEnviaElCupon() {
        reserva.cancelar();

        List<String> mensajes = mensajesEnviados(1);
        assertTrue(mensajes.get(0).contains("5%"));
    }

    @Test
    void siFallaLaConfirmacionNingunSubsistemaReacciona() {
        Reserva sinCupo = spy(reservaVip);
        sinCupo.addItemReserva(new unqTravel.reserva.ItemReserva(desde, hasta, 1, tourAgotado));
        sinCupo.agregarObservador(notificadorEmail);
        sinCupo.agregarObservador(fidelizacion);

        assertThrows(IllegalArgumentException.class, () -> sinCupo.confirmar());

        assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, sinCupo.codigoEstado());
        verifyNoInteractions(mailSender);
    }
}
