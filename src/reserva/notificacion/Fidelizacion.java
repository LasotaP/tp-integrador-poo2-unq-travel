package reserva.notificacion;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import reserva.CodigoEstado;
import reserva.Estado;
import reserva.ObservadorReserva;
import reserva.Reserva;


/** ConcreteObserver (Club de Viajeros): ante una cancelacion, envia un cupon del 5 %. */
public class Fidelizacion implements ObservadorReserva {

    private static final Set<CodigoEstado> ESTADOS_QUE_PREMIAN = EnumSet.of(CodigoEstado.CANCELADA);

    private final MailSender mailSender;

    public Fidelizacion(MailSender mailSender) {
        this.mailSender = Objects.requireNonNull(mailSender, "El MailSender no puede ser nulo");
    }

    @Override
    public void estadoCambiado(Reserva reserva, Estado anterior, Estado nuevo) {
        Optional.of(nuevo.codigo())
                .filter(ESTADOS_QUE_PREMIAN::contains)
                .ifPresent(codigo -> enviarCupon(reserva));
    }

    private void enviarCupon(Reserva reserva) {
        mailSender.enviarMail(reserva.getCliente().getEmail(),
                "Tu cupon de descuento del 5%",
                "Lamentamos que hayas cancelado tu reserva. Te regalamos un cupon del 5% de descuento para tu proxima reserva.",
                null);
    }
}
