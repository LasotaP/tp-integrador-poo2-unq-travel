package unqTravel.reserva.notificacion;

import unqTravel.reserva.Estado;
import unqTravel.reserva.Reserva;

/*
 Rol Observer. Se le informa cada cambio de estado de la reserva, con el estado anterior y el nuevo.
 */
public interface ObservadorReserva {

    void notificar(Reserva reserva, Estado anterior, Estado nuevo);
}
