package unqTravel.reportes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;

class ReportesYFilasTest extends SetUpTest {

    // ================= FILAS =================

    @Test
    void testFilaExposicionGetters() {
        FilaExposicion fila = new FilaExposicion("Aerolíneas Argentinas", 4, 50.0);

        assertEquals("Aerolíneas Argentinas", fila.getProveedor());
        assertEquals(4, fila.getCantidadServicios());
        assertEquals(50.0, fila.getPorcentaje());
    }

    @Test
    void testFilaOcupacionGettersYCalculoDeLibres() {
        FilaOcupacion fila = new FilaOcupacion(Categoria.ALOJAMIENTO, 20, 7);

        assertEquals(Categoria.ALOJAMIENTO, fila.getCategoria());
        assertEquals(20, fila.getTotal());
        assertEquals(7, fila.getReservados());
        assertEquals(13, fila.getLibres());
    }

    @Test
    void testFilaServicioReservadoGetters() {
        // Reutilizamos 'hotel' de SetUpTest
        FilaServicioReservado fila = new FilaServicioReservado(hotel, 12, 15000.0);

        assertEquals(hotel, fila.getServicio());
        assertEquals(12, fila.getUnidadesVendidas());
        assertEquals(15000.0, fila.getPrecioPromedio());
    }

    // ================= REPORTES =================

    @Test
    void testReporteOcupacionCupoEncapsulamientoYVisitor() {
        FilaOcupacion fila = new FilaOcupacion(Categoria.ALOJAMIENTO, 10, 4);
        List<FilaOcupacion> listaOriginal = new ArrayList<>(List.of(fila));

        // Reutilizamos 'desde' y 'hasta' de SetUpTest
        ReporteOcupacionCupo reporte = new ReporteOcupacionCupo(desde, hasta, listaOriginal);

        // Modificamos la lista original para comprobar copia defensiva
        listaOriginal.clear();

        assertEquals(desde, reporte.getDesde());
        assertEquals(hasta, reporte.getHasta());
        assertEquals("Ocupación y cupo disponible (" + desde + " a " + hasta + ")", reporte.getTitulo());
        assertEquals(1, reporte.getFilas().size());
        assertEquals(fila, reporte.getFilas().get(0));

        // Verificamos vista de solo lectura
        assertThrows(UnsupportedOperationException.class, () -> reporte.getFilas().add(fila));

        // Verificamos double dispatch del Visitor
        FormatoReporte formatoMock = mock(FormatoReporte.class);
        when(formatoMock.formatearOcupacion(reporte)).thenReturn("REPORTE_OCUPACION");

        assertEquals("REPORTE_OCUPACION", reporte.aceptar(formatoMock));
        verify(formatoMock).formatearOcupacion(reporte);
    }

    @Test
    void testReporteMasReservadosEncapsulamientoYVisitor() {
        FilaServicioReservado fila = new FilaServicioReservado(hotel, 5, 10000.0);
        List<FilaServicioReservado> listaOriginal = new ArrayList<>(List.of(fila));

        ReporteMasReservados reporte = new ReporteMasReservados(listaOriginal);
        listaOriginal.clear();

        assertEquals("Servicios más reservados", reporte.getTitulo());
        assertEquals(1, reporte.getFilas().size());
        assertEquals(fila, reporte.getFilas().get(0));
        assertThrows(UnsupportedOperationException.class, () -> reporte.getFilas().add(fila));

        FormatoReporte formatoMock = mock(FormatoReporte.class);
        when(formatoMock.formatearMasReservados(reporte)).thenReturn("REPORTE_MAS_RESERVADOS");

        assertEquals("REPORTE_MAS_RESERVADOS", reporte.aceptar(formatoMock));
        verify(formatoMock).formatearMasReservados(reporte);
    }

    @Test
    void testReporteExposicionProveedorEncapsulamientoYVisitor() {
        FilaExposicion fila = new FilaExposicion("Proveedor A", 3, 75.0);
        List<FilaExposicion> listaOriginal = new ArrayList<>(List.of(fila));

        ReporteExposicionProveedor reporte = new ReporteExposicionProveedor(listaOriginal);
        listaOriginal.clear();

        assertEquals("Exposición por proveedor", reporte.getTitulo());
        assertEquals(1, reporte.getFilas().size());
        assertEquals(fila, reporte.getFilas().get(0));
        assertThrows(UnsupportedOperationException.class, () -> reporte.getFilas().add(fila));

        FormatoReporte formatoMock = mock(FormatoReporte.class);
        when(formatoMock.formatearExposicion(reporte)).thenReturn("REPORTE_EXPOSICION");

        assertEquals("REPORTE_EXPOSICION", reporte.aceptar(formatoMock));
        verify(formatoMock).formatearExposicion(reporte);
    }
}