package unqTravel.reportes.formato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.ServicioIndividual;
import unqTravel.reportes.FilaExposicion;
import unqTravel.reportes.FilaOcupacion;
import unqTravel.reportes.FilaServicioReservado;
import unqTravel.reportes.ReporteExposicionProveedor;
import unqTravel.reportes.ReporteMasReservados;
import unqTravel.reportes.ReporteOcupacionCupo;

public class FormatosReporteTest extends SetUpTest {

	private ReporteMasReservados masReservados;
	private ReporteOcupacionCupo ocupacion;
	private ReporteExposicionProveedor exposicion;

	@BeforeEach
	public void armarReportes() {
		masReservados = new ReporteMasReservados(List.of(
				new FilaServicioReservado(hotel, 4, 15000.0),
				new FilaServicioReservado(excursion, 2, 4000.0)));
		ocupacion = new ReporteOcupacionCupo(desde, hasta, List.of(
				new FilaOcupacion(Categoria.ALOJAMIENTO, 60, 12)));
		exposicion = new ReporteExposicionProveedor(List.of(
				new FilaExposicion("Operador Norte", 3, 60.0)));
		
		
	}

	// ---- Texto plano (usa el separador de linea del sistema: se verifica con contains) ----

	@Test
	public void textoPlanoOcupacion() {
		String salida = ocupacion.aceptar(formatoTexto);

		assertTrue(salida.contains(ocupacion.getTitulo()));
		assertTrue(salida.contains("ALOJAMIENTO | total: 60 | reservados: 12 | libres: 48"));
	}

	@Test
	public void textoPlanoExposicion() {
		String salida = exposicion.aceptar(formatoTexto);

		assertTrue(salida.contains("Exposición por proveedor"));
		assertTrue(salida.contains("Operador Norte | servicios: 3 | 60.0% del catálogo"));
	}

	// ---- CSV ----

	@Test
	public void csvMasReservados() {
		assertEquals("servicio,unidades,precioPromedio\n"
				+ "Noche de Hotel Central,4,15000.00\n"
				+ "Excursion Guiada Museo,2,4000.00\n", masReservados.aceptar(formatoCsv));
	}

	@Test
	public void csvExposicion() {
		assertEquals("proveedor,servicios,porcentaje\nOperador Norte,3,60.0\n", exposicion.aceptar(formatoCsv));
	}


	// ---- HTML ----

	@Test
	public void htmlMasReservados() {
		String salida = masReservados.aceptar(formatoHtml);

		assertTrue(salida.contains("<h1>Servicios más reservados</h1>"));
		assertTrue(salida.contains("<th>Servicio</th><th>Unidades</th><th>Precio promedio</th>"));
		assertTrue(salida.contains("<tr><td>Noche de Hotel Central</td><td>4</td><td>$15000.00</td></tr>"));
		assertTrue(salida.endsWith("</table>\n"));
	}

	@Test
	public void htmlOcupacion() {
		String salida = ocupacion.aceptar(formatoHtml);

		assertTrue(salida.contains("<tr><td>ALOJAMIENTO</td><td>60</td><td>12</td><td>48</td></tr>"));
	}

	@Test
	public void htmlExposicion() {
		String salida = exposicion.aceptar(formatoHtml);

		assertTrue(salida.contains("<tr><td>Operador Norte</td><td>3</td><td>60.0%</td></tr>"));
	}

}