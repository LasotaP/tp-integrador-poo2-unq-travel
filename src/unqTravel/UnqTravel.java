package unqTravel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import reserva.ItemReserva;
import reserva.Reserva;
import servicios.Servicio;
import servicios.reportes.Reporte;
import servicios.reportes.ReporteMasReservados;
import servicios.reportes.ReporteOcupacionCupo;

public class UnqTravel {
	private List<Reserva> reservas;
	
	public UnqTravel() {
		this.reservas = new ArrayList<>();
	}
	  public Reporte generarReporteServiciosMasReservados(LocalDate desde, LocalDate hasta) {

	        // agrupa los ítems vendidos por servicio.
	        // El resultado es un mapa: cada Servicio apunta a la lista de ItemReserva que lo compraron.
	        Map<Servicio, List<ItemReserva>> porServicio = reservas.stream()

	            // Me quedo solo con las reservas que cuentan como venta
	            // (descarto canceladas y pendientes)
	            .filter(r-> r.estaPagada())

	            // Convierto "una secuencia de reservas" en "una secuencia de ítems":
	            // junta todos los ítems de todas las reservas en un único flujo
	            .flatMap(r -> r.getItemsReserva().stream())

	            // Me quedo solo con los ítems cuya fecha de inicio cae dentro del período [desde, hasta]
	            .filter(i -> i.estaEnPeriodo(desde,hasta))

	            // Agrupo los ítems según su servicio
	            .collect(Collectors.groupingBy(ItemReserva::getServicio));

	        // PASO 2: armar una fila del reporte por cada servicio
	        List<FilaServicioReservado> filas = new ArrayList<>();
	        for (Map.Entry<Servicio, List<ItemReserva>> e : porServicio.entrySet()) {

	            // Total de unidades vendidas de este servicio (suma de las cantidades de sus ítems)
	            int unidades = e.getValue().stream().mapToInt(ItemReserva::getCantidadPasajeros).sum();

	            // Total de plata cobrada por este servicio (suma del costo de sus ítems)
	            double recaudado = e.getValue().stream().mapToDouble(ItemReserva::costoTotalaItem).sum();

	            // Fila: servicio, unidades y precio promedio cobrado (plata total / unidades)
	            filas.add(new FilaServicioReservado(e.getKey(), unidades, recaudado / unidades));
	        }

	        // ordena de mayor a menor cantidad de unidades
	        // (compara por unidades y .reversed() invierte el orden)
	        filas.sort(Comparator.comparingInt(FilaServicioReservado::getUnidadesVendidas).reversed());

	        // devuelve el reporte con los datos ya armados
	        return new ReporteMasReservados(filas);
	    }
	    
	    
	public void addReserva(Reserva reserva) {
		this.reservas.add(reserva);
	}

	public ReporteOcupacionCupo generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {
		
	}

}
