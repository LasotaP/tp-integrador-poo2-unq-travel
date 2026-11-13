package servicios.reportes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import servicios.PaqueteTuristico;
import servicios.ServicioIndividual;
import unqTravel.FilaServicioReservado;

public class ReporteMasReservados implements Visitante, Reporte{
		private List <FilaServicioReservado> filas;
	
	public ReporteMasReservados(List<FilaServicioReservado> filas) {
		this.filas = new ArrayList<>(filas);
	}
	

	@Override
	public void visitarServicio(ServicioIndividual servicio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitarPaquete(PaqueteTuristico paquete) {
		// TODO Auto-generated method stub
		
	}


	@Override
    public String aceptar(Visitante visitor) {
        return visitor.visitarServicio(this);
    }
	public List<FilaServicioReservado> getFilas() {
        return Collections.unmodifiableList(filas);
    }


}
