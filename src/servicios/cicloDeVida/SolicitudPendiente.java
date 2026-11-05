package servicios.cicloDeVida;

import java.time.LocalDate;

import servicios.Servicio;

public class SolicitudPendiente extends Estado{
	
	@Override
	public String getEstado() {
		return "Solicitud pendiente";
	}
	
	@Override
	public void confirmar(Reserva reserva) {
		this.addServicio(reserva, null, null, null);  //
		reserva.decrementarCupo();  //le preguntamos a servicio que este delega calendario
		reserva.setEstado(new ConfirmadaPagada());
		
	}
	
	@Override
	public void cancelar(Reserva reserva) {
		reserva.setEstado(new Cancelada());
	}
	@Override
	public void addServicio(Reserva reserva, Servicio servicio, LocalDate dia,Integer cupo) {
		forEach (servicio.hayCupo(dia, cupo)) {
			
		};
	}
	
	
	
}
