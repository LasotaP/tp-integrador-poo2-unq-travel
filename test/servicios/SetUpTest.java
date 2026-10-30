package servicios;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetUpTest {

	protected Servicio entradaCerro;
	protected Servicio almuerzoCerro;
	protected Servicio skiNieve;
	protected Servicio snowboardNieve;
	
	protected PaqueteTuristico paqueteInvi;
	
	@BeforeEach
	void setUP() {
		entradaCerro = new ServicioIndividual(1, "Entrada al cerro", "Entrada al cerro", "Cerro Otto", "Entretenimiento", 3000.0, 0);
		almuerzoCerro = new ServicioIndividual(2, "Almuerzo en cerro", "Almuerzo ejecutivo", "Cerro Otto", "Alimentacion", 1500.0, 0);
		skiNieve = new ServicioIndividual(3, "Equipo de Ski", "Equipo de Ski", "Cerro Otto", "Entretenimiento", 5000.0, 0);
		snowboardNieve = new ServicioIndividual(4, "Equipo de Snow", "Equipo de Snow", "Cerro Otto", "Entretenimiento", 3000.0, 0);
		
	}
	

}
