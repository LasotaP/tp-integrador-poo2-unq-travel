package unqTravel;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import reserva.ItemReserva;
import reserva.Reserva;
import servicios.Servicio;
import servicios.catalogo.CatalogoServicios;
import servicios.reportes.FilaServicioReservado;
import servicios.reportes.Reporte;
import servicios.reportes.ReporteExposicionProveedor;
import servicios.reportes.ReporteMasReservados;
import servicios.reportes.ReporteOcupacionCupo;
import servicios.reportes.VisitanteOcupacion;
import servicios.reportes.VisitanteExposicion;




public class UnqTravel {
	private List<Reserva> reservas;
	private final CatalogoServicios catalogo;
	
	public UnqTravel(CatalogoServicios catalogo) {
		this.reservas = new ArrayList<>();
		this.catalogo = Objects.requireNonNull(catalogo, "El catálogo no puede ser nulo");
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
	    
	    
	// Registra una reserva en el sistema para que pueda ser tenida en cuenta
	// en los reportes (por ejemplo, el de servicios más reservados).
	public void addReserva(Reserva reserva) {
	    this.reservas.add(reserva);
	}

	// Genera el reporte de ocupación y cupo disponible por categoría
	// para el período [desde, hasta] (ambas fechas inclusive).
	public ReporteOcupacionCupo generarReporteOcupacion(LocalDate desde, LocalDate hasta) {

	    // Creo el visitor que va a recorrer el catálogo y acumular, por cada categoría,
	    // el cupo total y el cupo reservado de cada día del período.
	    VisitanteOcupacion v = new VisitanteOcupacion(desde, hasta);

	    // El catálogo le "da paso" al visitor con cada uno de sus servicios.
	    // Los paquetes reenvían la visita a los servicios que contienen, así que
	    // el visitor termina pasando por todos los servicios individuales.
	    catalogo.aceptar(v);

	    // El visitor ya tiene los datos calculados: los empaqueto en un reporte.
	    // Todavía no tiene formato: eso se elige después con reporte.aceptar(formato).
	    return new ReporteOcupacionCupo(desde, hasta, v.getFilas());
	}

	// Genera el reporte de exposición por proveedor: qué porcentaje del catálogo
	// depende de cada operador, para medir el riesgo de concentración.
	public ReporteExposicionProveedor generarReporteExposicion() {

	    // Visitor que cuenta cuántos servicios individuales aporta cada proveedor.
	    VisitanteExposicion v = new VisitanteExposicion();

	    // Recorre todo el catálogo (incluyendo lo que hay dentro de los paquetes).
	    catalogo.aceptar(v);

	    // Cada fila trae proveedor, cantidad de servicios y porcentaje sobre el total.
	    return new ReporteExposicionProveedor(v.getFilas());
	}
	
	

}
