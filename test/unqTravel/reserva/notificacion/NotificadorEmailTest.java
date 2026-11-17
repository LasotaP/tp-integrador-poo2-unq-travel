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
import unqTravel.reserva.Finalizada;
import unqTravel.reserva.SolicitudPendiente;

/** Usa notificadorEmail, mailSender (mock), cliente y reservaRecepcion del SetUpTest. */
class NotificadorEmailTest extends SetUpTest {

    private void assertMailEnviadoConEstado(String estado) {
        ArgumentCaptor<String> mensaje = ArgumentCaptor.forClass(String.class);
        verify(mailSender).enviarMail(eq(cliente.getEmail()), anyString(), mensaje.capture(), isNull());
        assertTrue(mensaje.getValue().contains(estado),
                "El mensaje deberia mencionar " + estado + " pero fue: " + mensaje.getValue());
    }

    @Test
    void noSePuedeCrearSinMailSender() {
        assertThrows(NullPointerException.class, () -> new NotificadorEmail(null));
    }

    // ---------- estados que SI notifican ----------

    @Test
    void notificaAlClienteCuandoLaReservaPasaAConfirmada() {
        notificadorEmail.notificar(reservaRecepcion, new SolicitudPendiente(), new Confirmada());

        assertMailEnviadoConEstado("CONFIRMADA");
    }

    @Test
    void notificaAlClienteCuandoLaReservaPasaAEnCurso() {
        notificadorEmail.notificar(reservaRecepcion, new EnCheckIn(), new EnCurso());

        assertMailEnviadoConEstado("EN_CURSO");
    }

    @Test
    void notificaAlClienteCuandoLaReservaPasaAFinalizada() {
        notificadorEmail.notificar(reservaRecepcion, new EnCurso(), new Finalizada());

        assertMailEnviadoConEstado("FINALIZADA");
    }

    @Test
    void enviaUnSoloMailPorCambioDeEstado() {
        notificadorEmail.notificar(reservaRecepcion, new SolicitudPendiente(), new Confirmada());

        verify(mailSender, times(1)).enviarMail(anyString(), anyString(), anyString(), any());
    }

    // ---------- estados que NO notifican ----------

    @Test
    void noNotificaCuandoPasaAEnCheckIn() {
        notificadorEmail.notificar(reservaRecepcion, new Confirmada(), new EnCheckIn());

        verifyNoInteractions(mailSender);
    }

    @Test
    void noNotificaCuandoPasaACancelada() {
        notificadorEmail.notificar(reservaRecepcion, new Confirmada(), new Cancelada());

        verifyNoInteractions(mailSender);
    }

    @Test
    void noNotificaCuandoPasaAPendiente() {
        notificadorEmail.notificar(reservaRecepcion, new Cancelada(), new SolicitudPendiente());

        verifyNoInteractions(mailSender);
    }
}
