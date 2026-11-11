package notificacion.comprobante;


import java.time.LocalDate;

import reserva.CodigoEstado;
import reserva.Estado;
import reserva.ObservadorReserva;
import reserva.Reserva;

/** ConcreteObserver: al finalizar la reserva genera y registra la factura y el voucher. */
public class GeneradorComprobante implements ObservadorReserva {

    @Override
    public void notificar(Reserva reserva, Estado anterior, Estado nuevo) {
        if (nuevo.codigo() == CodigoEstado.FINALIZADA) {
            LocalDate fecha = LocalDate.now();
            String codigo = reserva.getNumeroReserva();
            reserva.addComprobante(new Factura(codigo, fecha, reserva.getCliente(), reserva.getItemsReserva(), reserva.getCostoTotal()));
            reserva.addComprobante(new Voucher(codigo, fecha, reserva.getCliente(), reserva.getItemsReserva(), reserva.getCostoTotal()));
        }
    }
}
