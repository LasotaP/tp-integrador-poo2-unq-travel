package notificacion.comprobante;

import java.time.Clock;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import reserva.CodigoEstado;
import reserva.Estado;
import reserva.ObservadorReserva;
import reserva.Reserva;

/** ConcreteObserver: al finalizar la reserva genera y registra la factura y el voucher. */
public class GeneradorComprobante implements ObservadorReserva {

    private static final Set<CodigoEstado> ESTADOS_QUE_FACTURAN = EnumSet.of(CodigoEstado.FINALIZADA);

    private final RegistroComprobantes registro;
    private final Clock reloj;
    private int secuencia;

    public GeneradorComprobante(RegistroComprobantes registro, Clock reloj) {
        this.registro = Objects.requireNonNull(registro, "El registro no puede ser nulo");
        this.reloj = Objects.requireNonNull(reloj, "El reloj no puede ser nulo");
    }

    @Override
    public void estadoCambiado(Reserva reserva, Estado anterior, Estado nuevo) {
        Optional.of(nuevo.codigo())
                .filter(ESTADOS_QUE_FACTURAN::contains)
                .ifPresent(codigo -> generar(reserva));
    }

    private void generar(Reserva reserva) {
        secuencia++;
        LocalDate fecha = LocalDate.now(reloj);
        registro.registrar(new Factura("FAC-" + secuencia, fecha, reserva.getCliente(), reserva.getServicios()));
        registro.registrar(new Voucher("VOU-" + secuencia, fecha, reserva.getCliente(), reserva.getServicios()));
    }
}
