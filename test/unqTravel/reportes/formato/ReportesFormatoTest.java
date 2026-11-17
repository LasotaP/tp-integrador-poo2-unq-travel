package unqTravel.reportes.formato;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.*;
import unqTravel.reportes.*;

class ReportesFormatoTest extends SetUpTest {

    private FormatoCSV formatoCSV;
    private FormatoHTML formatoHTML;

    private ReporteMasReservados reporteMasReservados;
    private ReporteOcupacionCupo reporteOcupacion;
    private ReporteExposicionProveedor reporteExposicion;

    @BeforeEach
    void setUpLocal() {
        formatoCSV = new FormatoCSV();
        formatoHTML = new FormatoHTML();
        formatoTexto = new FormatoTextoPlano();

        Servicio servicioMock = mock(Servicio.class);
        when(servicioMock.getNombre()).thenReturn("Hotel Spa");

        FilaServicioReservado filaServicio = new FilaServicioReservado(servicioMock, 4, 15000.50);
        reporteMasReservados = new ReporteMasReservados(List.of(filaServicio));

        FilaOcupacion filaOcupacion = new FilaOcupacion(Categoria.ALOJAMIENTO, 20, 5);
        reporteOcupacion = new ReporteOcupacionCupo(desde, hasta, List.of(filaOcupacion));

        FilaExposicion filaExposicion = new FilaExposicion("Aerolíneas", 3, 40.0);
        reporteExposicion = new ReporteExposicionProveedor(List.of(filaExposicion));
    }

    // ================= TESTS DE FORMATO CSV =================

    @Test
    void testFormatoCSVMasReservados() {
        String csv = formatoCSV.formatearMasReservados(reporteMasReservados);

        assertTrue(csv.startsWith("servicio,unidades,precioPromedio"));
        assertTrue(csv.contains("Hotel Spa,4,15000.50"));
    }

    @Test
    void testFormatoCSVOcupacion() {
        String csv = formatoCSV.formatearOcupacion(reporteOcupacion);

        assertTrue(csv.startsWith("categoria,total,reservados,libres"));
        assertTrue(csv.contains("ALOJAMIENTO,20,5,15"));
    }

    @Test
    void testFormatoCSVExposicion() {
        String csv = formatoCSV.formatearExposicion(reporteExposicion);

        assertTrue(csv.startsWith("proveedor,servicios,porcentaje"));
        assertTrue(csv.contains("Aerolíneas,3,40.0"));
    }

    @Test
    void testFormatoCSVConCaracteresEspecialesEscapaCorrectamente() {
        // Probamos un proveedor con comas y comillas para forzar la cobertura del método esc()
        FilaExposicion filaEspecial = new FilaExposicion("Proveedor, \"Global\"", 2, 100.0);
        ReporteExposicionProveedor reporteEspecial = new ReporteExposicionProveedor(List.of(filaEspecial));

        String csv = formatoCSV.formatearExposicion(reporteEspecial);
        
        assertTrue(csv.contains("\"Proveedor, \"\"Global\"\"\",2,100.0"));
    }
}