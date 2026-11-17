package unqTravel.reportes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.PaqueteTuristico;
import unqTravel.catalogo.ServicioIndividual;

class VisitanteOcupacionTest extends SetUpTest {

    private LocalDate inicio;
    private LocalDate fin;
    private VisitanteOcupacion visitante;

    @BeforeEach
    void setUpLocal() {
        // Rango de 3 días inclusive: hoy, hoy+1 y hoy+2
        inicio = hoy;
        fin = hoy.plusDays(2);
        visitante = new VisitanteOcupacion(inicio, fin);
    }

    @Test
    void testSinServiciosVisitadosDevuelveListaVacia() {
        assertTrue(visitante.getFilas().isEmpty());
    }

    @Test
    void testAcumulaCuposPorCategoriaEnRangoInclusive() {
        ServicioIndividual hotel1 = mock(ServicioIndividual.class);
        when(hotel1.getCategoria()).thenReturn(Categoria.ALOJAMIENTO);
        when(hotel1.cupoTotal(any(LocalDate.class))).thenReturn(10);
        when(hotel1.cupoReservado(any(LocalDate.class))).thenReturn(4);

        ServicioIndividual hotel2 = mock(ServicioIndividual.class);
        when(hotel2.getCategoria()).thenReturn(Categoria.ALOJAMIENTO);
        when(hotel2.cupoTotal(any(LocalDate.class))).thenReturn(5);
        when(hotel2.cupoReservado(any(LocalDate.class))).thenReturn(2);

        ServicioIndividual excursion1 = mock(ServicioIndividual.class);
        when(excursion1.getCategoria()).thenReturn(Categoria.EXCURSION);
        when(excursion1.cupoTotal(any(LocalDate.class))).thenReturn(20);
        when(excursion1.cupoReservado(any(LocalDate.class))).thenReturn(15);

        visitante.visitarServicio(hotel1);
        visitante.visitarServicio(hotel2);
        visitante.visitarServicio(excursion1);

        List<FilaOcupacion> filas = visitante.getFilas();

        assertEquals(2, filas.size());

        // Categoría ALOJAMIENTO: (10 + 5) * 3 días = 45 total | (4 + 2) * 3 días = 18 reservados
        FilaOcupacion filaAlojamiento = filas.get(0);
        assertEquals(Categoria.ALOJAMIENTO, filaAlojamiento.getCategoria());
        assertEquals(45, filaAlojamiento.getTotal());
        assertEquals(18, filaAlojamiento.getReservados());
        assertEquals(27, filaAlojamiento.getLibres());

        // Categoría EXCURSION: 20 * 3 días = 60 total | 15 * 3 días = 45 reservados
        FilaOcupacion filaExcursion = filas.get(1);
        assertEquals(Categoria.EXCURSION, filaExcursion.getCategoria());
        assertEquals(60, filaExcursion.getTotal());
        assertEquals(45, filaExcursion.getReservados());
        assertEquals(15, filaExcursion.getLibres());

        // Verificamos explícitamente que se evaluaron los extremos inclusive (inicio y fin)
        verify(hotel1).cupoTotal(inicio);
        verify(hotel1).cupoTotal(fin);
    }

    @Test
    void testNoDuplicaCuposSiMismaInstanciaSeVisitaDosVeces() {
        ServicioIndividual servicioRepetido = mock(ServicioIndividual.class);
        when(servicioRepetido.getCategoria()).thenReturn(Categoria.VUELO);
        when(servicioRepetido.cupoTotal(any(LocalDate.class))).thenReturn(10);
        when(servicioRepetido.cupoReservado(any(LocalDate.class))).thenReturn(3);

        visitante.visitarServicio(servicioRepetido);
        visitante.visitarServicio(servicioRepetido);

        List<FilaOcupacion> filas = visitante.getFilas();

        assertEquals(1, filas.size());
        // 10 * 3 días = 30 (y no 60)
        assertEquals(30, filas.get(0).getTotal());
        assertEquals(9, filas.get(0).getReservados());
        assertEquals(21, filas.get(0).getLibres());
    }

    @Test
    void testVisitarPaqueteNoSumaDirectamente() {
        PaqueteTuristico paqueteMock = mock(PaqueteTuristico.class);

        visitante.visitarPaquete(paqueteMock);

        assertTrue(visitante.getFilas().isEmpty());
        verifyNoInteractions(paqueteMock);
    }
}