package unqTravel.reserva.notificacion;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import unqTravel.reserva.CodigoEstado;
import unqTravel.reserva.Estado;
import unqTravel.reserva.Reserva;

/** ConcreteObserver: avisa por mail al cliente solo en CONFIRMADA, EN_CURSO y FINALIZADA. */
public class NotificadorEmail implements ObservadorReserva {

    private static final Set<CodigoEstado> ESTADOS_NOTIFICABLES =
            EnumSet.of(CodigoEstado.CONFIRMADA, CodigoEstado.EN_CURSO, CodigoEstado.FINALIZADA);

    private final MailSender mailSender;

    public NotificadorEmail(MailSender mailSender) {
        this.mailSender = Objects.requireNonNull(mailSender, "El MailSender no puede ser nulo");
    }

    @Override
    public void notificar(Reserva reserva, Estado anterior, Estado nuevo) {
        CodigoEstado codigo = nuevo.codigo();
        if (ESTADOS_NOTIFICABLES.contains(codigo)) {
        	notificarMail(reserva, codigo);
        }
    }

    private void notificarMail(Reserva reserva, CodigoEstado codigo) {
        mailSender.enviarMail(reserva.getCliente().getEmail(),
                "Estado de tu reserva",
                "Tu reserva paso a estado " + codigo,
                null);
    }
}
