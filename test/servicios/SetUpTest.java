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
	protected Cupo cupoEntrada;
	protected Cupo cupoAlmuerzo;
	protected Cupo cupoSkiNieve;
	protected Cupo SnowBoardNieve; 
	
	@BeforeEach
	void setUP() {
		SnowBoardNieve = new Cupo();
		entradaCerro = new ServicioIndividual(1, "Entrada al cerro", "Entrada al cerro", "Cerro Otto", "Entretenimiento", 3000.0, 0, cupoEntrada);
		almuerzoCerro = new ServicioIndividual(2, "Almuerzo en cerro", "Almuerzo ejecutivo", "Cerro Otto", "Alimentacion", 1500.0, 0, cupoAlmuerzo);
		skiNieve = new ServicioIndividual(3, "Equipo de Ski", "Equipo de Ski", "Cerro Otto", "Entretenimiento", 5000.0, 0, cupoSkiNieve);
		snowboardNieve = new ServicioIndividual(4, "Equipo de Snow", "Equipo de Snow", "Cerro Otto", "Entretenimiento", 3000.0, 0, SnowBoardNieve);
		
	}
	
	

}
