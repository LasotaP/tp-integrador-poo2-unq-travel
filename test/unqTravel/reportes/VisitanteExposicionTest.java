package unqTravel.reportes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.PaqueteTuristico;
import unqTravel.catalogo.ServicioIndividual;

class VisitanteExposicionTest extends SetUpTest {

    private VisitanteExposicion visitante;

    @BeforeEach
    void setUpLocal() {
        visitante = new VisitanteExposicion();
    }

    @Test
    void testSinServiciosVisitadosDevuelveListaVacia() {
        List<FilaExposicion> filas = visitante.getFilas();

        assertTrue(filas.isEmpty());
    }

    @Test
    void testAcumulaPorProveedorCalculaPorcentajesYOrdenaDescendente() {
        ServicioIndividual s1 = mock(ServicioIndividual.class);
        when(s1.getProveedor()).thenReturn("Proveedor Minorista");

        ServicioIndividual s2 = mock(ServicioIndividual.class);
        when(s2.getProveedor()).thenReturn("Proveedor Mayorista");

        ServicioIndividual s3 = mock(ServicioIndividual.class);
        when(s3.getProveedor()).thenReturn("Proveedor Mayorista");

        ServicioIndividual s4 = mock(ServicioIndividual.class);
        when(s4.getProveedor()).thenReturn("Proveedor Mayorista");

        visitante.visitarServicio(s1);
        visitante.visitarServicio(s2);
        visitante.visitarServicio(s3);
        visitante.visitarServicio(s4);

        List<FilaExposicion> filas = visitante.getFilas();

        assertEquals(2, filas.size());

        // Primero el de mayor porcentaje (3 de 4 = 75%)
        assertEquals("Proveedor Mayorista", filas.get(0).getProveedor());
        assertEquals(3, filas.get(0).getCantidadServicios());
        assertEquals(75.0, filas.get(0).getPorcentaje(), 0.001);

        // Segundo el de menor porcentaje (1 de 4 = 25%)
        assertEquals("Proveedor Minorista", filas.get(1).getProveedor());
        assertEquals(1, filas.get(1).getCantidadServicios());
        assertEquals(25.0, filas.get(1).getPorcentaje(), 0.001);
    }

    @Test
    void testNoCuentaDosVecesLaMismaInstanciaDeServicio() {
        ServicioIndividual servicioRepetido = mock(ServicioIndividual.class);
        when(servicioRepetido.getProveedor()).thenReturn("Hotel Hilton");

        // Lo visitamos dos veces (por ejemplo, suelto en el catálogo y dentro de un paquete)
        visitante.visitarServicio(servicioRepetido);
        visitante.visitarServicio(servicioRepetido);

        List<FilaExposicion> filas = visitante.getFilas();

        assertEquals(1, filas.size());
        assertEquals("Hotel Hilton", filas.get(0).getProveedor());
        assertEquals(1, filas.get(0).getCantidadServicios());
        assertEquals(100.0, filas.get(0).getPorcentaje(), 0.001);
    }

    @Test
    void testVisitarPaqueteNoSumaDirectamente() {
        PaqueteTuristico paqueteMock = mock(PaqueteTuristico.class);

        visitante.visitarPaquete(paqueteMock);

        assertTrue(visitante.getFilas().isEmpty());
        verifyNoInteractions(paqueteMock);
    }
}