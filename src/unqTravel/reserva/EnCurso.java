package unqTravel.reserva;

import java.time.LocalDate;

import unqTravel.comprobante.Voucher;


public class EnCurso extends Estado {

    @Override
    public void finalizarViaje(Reserva reserva) {
        reserva.cambiarEstado(new Finalizada());
    }

    @Override
    public void cancelar(Reserva reserva) {
    	reserva.addComprobante(new Voucher(reserva.getNumeroReserva(), LocalDate.now(), reserva.getCliente(), reserva.getItemsReserva(), reserva.getRembolso() , reserva.getFechaFinReserva()));    	
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CURSO;
    }


}
