package reserva;

import java.time.LocalDate;

import notificacion.comprobante.Voucher;

public class EnCheckIn extends Estado {


    @Override
    public void iniciarViaje(Reserva reserva) {
        reserva.cambiarEstado(new EnCurso());
    }

    @Override
    public void cancelar(Reserva reserva) {
    	reserva.restaurarServicios();
    	reserva.addComprobante(new Voucher(reserva.getNumeroReserva(), LocalDate.now(),reserva.getCliente(),reserva.getItemsReserva(), reserva.getCostoTotal()));
    	
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CHECKIN;
    }
    
}
