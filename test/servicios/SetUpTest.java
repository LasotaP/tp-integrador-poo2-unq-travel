package servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetUpTest {

	// SERVICIOS INDIVIDUALES - CREACION
	protected ServicioIndividual entradaCerro;
	protected ServicioIndividual almuerzoCerro;
	protected ServicioIndividual skiNieve;
	protected ServicioIndividual snowboardNieve;
	
	// PAQUETES TURISTICOS - CREACION
	protected PaqueteTuristico paqueteInvi;
	
	// CUPO - CREACION
	protected Cupo cupoEntrada;
	protected Cupo cupoAlmuerzo;
	protected Cupo cupoSkiNieve;
	protected Cupo cupoSnowboardNieve;
	
	// ATRIBUTOS - CREACION
	protected Atributo estrellasCalidad;
	protected Atributo aceptaMascota;
	protected Atributo eslogan;
	protected Atributo fechaInaguracion;
	
	
	
	@BeforeEach
	void setUP() {

		// ASIGNACION DE ATRIBUTOS
		estrellasCalidad = new Atributo("Calidad en estrellas", true, new TipoNumerico(3));
		aceptaMascota = new Atributo("Mascotas", false, new TipoBooleano(true));
		eslogan = new Atributo("Eslogan", false, new TipoTexto("Nuestro eslogan"));
		fechaInaguracion = new Atributo("Fecha de inaguracion", false, new TipoFecha(LocalDate.of(1988, 10, 10)));
		
		// ASIGNACION DE CUPOS
		cupoEntrada = new Cupo();
		cupoAlmuerzo = new Cupo();
		cupoSkiNieve = new Cupo();
		cupoSnowboardNieve = new Cupo();
		
		// ASIGNACION DE SERVICIOS INDIVIDUALES
		entradaCerro = new ServicioIndividual("1", "Entrada al cerro", "Entrada al cerro", "Cerro Otto", "Entretenimiento", 3000.0, 0, cupoEntrada);
		almuerzoCerro = new ServicioIndividual("2", "Almuerzo en cerro", "Almuerzo ejecutivo", "Cerro Otto", "Alimentacion", 1500.0, 0, cupoAlmuerzo);
		skiNieve = new ServicioIndividual("3", "Equipo de Ski", "Equipo de Ski", "Cerro Otto", "Entretenimiento", 5000.0, 0, cupoSkiNieve);
		snowboardNieve = new ServicioIndividual("4", "Equipo de Snow", "Equipo de Snow", "Cerro Otto", "Entretenimiento", 3000.0, 0, cupoSnowboardNieve);
		
		// ASIGNACION DE CUPOS A SERVICIOS
		entradaCerro.aumentarCupo(LocalDate.of(1999, 3, 30), 2);
		almuerzoCerro.aumentarCupo(LocalDate.of(1999, 3, 30), 2);
		skiNieve.aumentarCupo(LocalDate.of(1999, 3, 30), 2);
		snowboardNieve.aumentarCupo(LocalDate.of(1999, 3, 30), 2);
		
	}
	
	

}
