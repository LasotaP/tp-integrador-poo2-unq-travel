package unqTravel;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.catalogo.Categoria;
import unqTravel.reportes.FilaExposicion;
import unqTravel.reportes.FilaOcupacion;
import unqTravel.reportes.FilaServicioReservado;
import unqTravel.reportes.ReporteExposicionProveedor;
import unqTravel.reportes.ReporteMasReservados;
import unqTravel.reportes.ReporteOcupacionCupo;
import unqTravel.reserva.ItemReserva;

public class UnqTravelTest extends SetUpTest {

	@Test
	public void catalogoNuloLanzaExcepcion() {
		assertThrows(NullPointerException.class, () -> new UnqTravel(null));
	}

	@Test
	public void generarReporteServiciosMasReservados() {
		reservaRecepcion.addItemReserva(itemHotel);
		reservaRecepcion.addItemReserva(itemExcursion);
		reservaVip.addItemReserva(itemHotel);
		reservaEstandar.addItemReserva(new ItemReserva(desde, hasta, 3, autoCompacto)); // queda pendiente: no cuenta
		reservaRecepcion.confirmar();
		reservaVip.confirmar();
		sistema.addReserva(reservaRecepcion);
		sistema.addReserva(reservaVip);
		sistema.addReserva(reservaEstandar);

		ReporteMasReservados reporte = sistema.generarReporteServiciosMasReservados(desde, hasta);

		List<FilaServicioReservado> filas = reporte.getFilas();
		assertEquals(2, filas.size());
		assertEquals(hotel, filas.get(0).getServicio());   // ordenado por unidades: hotel 4, excursion 2
		assertEquals(4, filas.get(0).getUnidadesVendidas());
		assertEquals(15000.0, filas.get(0).getPrecioPromedio(), 0.001);
		assertEquals(excursion, filas.get(1).getServicio());
		assertEquals(2, filas.get(1).getUnidadesVendidas());
	}

	@Test
	public void generarReporteOcupacion() {
		reservaRecepcion.addItemReserva(itemHotel);
		reservaRecepcion.confirmar(); // reserva 2 lugares por dia en el hotel

		ReporteOcupacionCupo reporte = sistema.generarReporteOcupacion(desde, hasta);

		assertEquals(desde, reporte.getDesde());
		assertEquals(hasta, reporte.getHasta());
		List<FilaOcupacion> filas = reporte.getFilas();
		assertEquals(4, filas.size()); // alojamiento, excursion, vuelo y auto
		assertEquals(Categoria.ALOJAMIENTO, filas.get(0).getCategoria());
		assertEquals(60, filas.get(0).getTotal());      // 10 lugares x 6 dias
		assertEquals(12, filas.get(0).getReservados()); // 2 lugares x 6 dias
		assertEquals(48, filas.get(0).getLibres());
	}

	@Test
	public void generarReporteExposicion() {
		ReporteExposicionProveedor reporte = sistema.generarReporteExposicion();

		List<FilaExposicion> filas = reporte.getFilas(); // ordenadas de mayor a menor porcentaje
		assertEquals(2, filas.size());
		assertEquals("Operador Norte", filas.get(0).getProveedor()); // excursion, vuelo y tourAgotado
		assertEquals(3, filas.get(0).getCantidadServicios());
		assertEquals(60.0, filas.get(0).getPorcentaje(), 0.001);
		assertEquals("Operador Sur", filas.get(1).getProveedor());   // hotel y autoCompacto
		assertEquals(40.0, filas.get(1).getPorcentaje(), 0.001);
	}

	@Test
	public void periodoInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> sistema.generarReporteServiciosMasReservados(null, hasta));
		assertThrows(IllegalArgumentException.class, () -> sistema.generarReporteServiciosMasReservados(hasta, desde));
		assertThrows(IllegalArgumentException.class, () -> sistema.generarReporteOcupacion(desde, null));
		assertThrows(IllegalArgumentException.class, () -> sistema.generarReporteOcupacion(hasta, desde));
	}
}