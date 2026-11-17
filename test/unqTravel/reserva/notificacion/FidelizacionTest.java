package unqTravel.reserva.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import unqTravel.SetUpTest;
import unqTravel.reserva.Cancelada;
import unqTravel.reserva.Confirmada;
import unqTravel.reserva.EnCheckIn;
import unqTravel.reserva.EnCurso;
import unqTravel.reserva.Estado;
import unqTravel.reserva.Finalizada;
import unqTravel.reserva.SolicitudPendiente;

/** Usa fidelizacion, mailSender (mock), cliente y reservaRecepcion del SetUpTest. */
class FidelizacionTest extends SetUpTest {

    private void assertCancelacionDesde(Estado anterior) {
        fidelizacion.notificar(reservaRecepcion, anterior, new Cancelada());

        ArgumentCaptor<String> titulo = ArgumentCaptor.forClass(String.class); // spy  para guardar string  que usare en el asserTrue
        ArgumentCaptor<String> mensaje = ArgumentCaptor.forClass(String.class); // spy para guardar string  que usare en el asserTrue
        verify(mailSender).enviarMail(eq(cliente.getEmail()), titulo.capture(), mensaje.capture(), isNull());
        assertTrue(titulo.getValue().contains("5%"));
        assertTrue(mensaje.getValue().contains("5%"));
    }

    @Test
    void noSePuedeCrearSinMailSender() {
        assertThrows(NullPointerException.class, () -> new Fidelizacion(null));
    } 

    // ---------- cancelacion: SI envia cupon (desde cualquier estado) ----------

    @Test
    void enviaCuponSiSeCancelaUnaReservaPendiente() {
        assertCancelacionDesde(new SolicitudPendiente());
    }

    @Test
    void enviaCuponSiSeCancelaUnaReservaConfirmada() {
        assertCancelacionDesde(new Confirmada());
    }

    @Test
    void enviaCuponSiSeCancelaUnaReservaEnCheckIn() {
        assertCancelacionDesde(new EnCheckIn());
    }

    @Test
    void enviaCuponSiSeCancelaUnaReservaEnCurso() {
        assertCancelacionDesde(new EnCurso());
    }

    // ---------- cualquier otra transicion: NO envia nada ----------

    @Test
    void noEnviaNadaCuandoSeConfirma() {
        fidelizacion.notificar(reservaRecepcion, new SolicitudPendiente(), new Confirmada());

        verifyNoInteractions(mailSender);
    }

    @Test
    void noEnviaNadaCuandoSeHaceElCheckIn() {
        fidelizacion.notificar(reservaRecepcion, new Confirmada(), new EnCheckIn());

        verifyNoInteractions(mailSender);
    }

    @Test
    void noEnviaNadaCuandoSeIniciaElViaje() {
        fidelizacion.notificar(reservaRecepcion, new EnCheckIn(), new EnCurso());

        verifyNoInteractions(mailSender);
    }

    @Test
    void noEnviaNadaCuandoSeFinaliza() {
        fidelizacion.notificar(reservaRecepcion, new EnCurso(), new Finalizada());

        verifyNoInteractions(mailSender);
    }
}
