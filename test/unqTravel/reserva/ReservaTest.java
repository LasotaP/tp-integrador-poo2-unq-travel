package unqTravel.reserva;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;
import unqTravel.reserva.notificacion.ObservadorReserva;

/**
 * Datos del SetUpTest que se usan aca:
 *  - itemHotel     = 2 x hotel ($15.000)      = $30.000, cupo del hotel: 10
 *  - itemExcursion = 2 x excursion ($4.000)   = $8.000
 *  - reservaRecepcion (traslado $0), reservaVip, reservaEstandar (sobre autoCompacto)
 */
class ReservaTest extends SetUpTest {

    // usamos la de recepcion.
    @BeforeEach
    void iniciarReserva() {
        reserva = reservaRecepcion;
    }
 
    // ---------- construccion ----------

    @Test
    void unaReservaNuevaEstaPendienteYSinItems() {
        assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, reserva.codigoEstado());
        assertTrue(reserva.getItemsReserva().isEmpty());
        assertEquals("1", reserva.getNumeroReserva());
        assertSame(cliente, reserva.getCliente());
    }

    @Test
    void noSePuedeCrearUnaReservaSinCliente() {
        assertThrows(NullPointerException.class, () -> new Reserva(null, "9", trasladoRecepcion));
    }

    @Test
    void noSePuedeCrearUnaReservaSinMetodoDeTraslado() {
        assertThrows(NullPointerException.class, () -> new Reserva(cliente, "9", null));
    }

    // ---------- items ----------

    @Test
    void sePuedenAgregarItemsMientrasEstaPendiente() {
        reserva.addItemReserva(itemHotel);
        reserva.addItemReserva(itemExcursion);

        assertEquals(List.of(itemHotel, itemExcursion), reserva.getItemsReserva());
    }

    @Test
    void sePuedeQuitarUnItemMientrasEstaPendiente() {
        reserva.addItemReserva(itemHotel);
        reserva.addItemReserva(itemExcursion);

        reserva.quitarItemReserva(itemHotel);

        assertEquals(List.of(itemExcursion), reserva.getItemsReserva());
    }

    @Test
    void quitarUnItemDeUnaReservaVaciaEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> reserva.quitarItemReserva(itemHotel));
    }

    @Test
    void quitarUnItemQueNoEstaEnLaReservaEsInvalido() {
        reserva.addItemReserva(itemHotel);

        assertThrows(IllegalArgumentException.class, () -> reserva.quitarItemReserva(itemExcursion));
    }

    @Test
    void noSePuedeAgregarUnItemUnaVezConfirmada() {
        reserva.confirmar();

        assertThrows(OperacionInvalidaException.class, () -> reserva.addItemReserva(itemHotel));
        assertTrue(reserva.getItemsReserva().isEmpty());
    }

    @Test
    void noSePuedeQuitarUnItemUnaVezConfirmada() {
        reserva.addItemReserva(itemHotel);
        reserva.confirmar();

        assertThrows(OperacionInvalidaException.class, () -> reserva.quitarItemReserva(itemHotel));
        assertEquals(1, reserva.getItemsReserva().size());
    }

    @Test
    void laListaDeItemsExpuestaEsInmodificable() {
        assertThrows(UnsupportedOperationException.class, () -> reserva.getItemsReserva().add(itemHotel));
    }

    // ---------- costos ----------

    @Test
    void elCostoDeLaReservaEsLaSumaDeLosItems() {
        reserva.addItemReserva(itemHotel);      // 30.000
        reserva.addItemReserva(itemExcursion);  //  8.000

        assertEquals(38000.0, reserva.getCostoReserva(), 0.001);
    }

    @Test
    void elCostoDeLaReservaIncluyeElDescuentoPromocionalDeLosServicios() {
        reserva.addItemReserva(new ItemReserva(desde, hasta, 1, vuelo)); // 100.000 con 10 %

        assertEquals(90000.0, reserva.getCostoReserva(), 0.001);
    }

    @Test
    void elCostoDeUnaReservaVaciaEsCero() {
        assertEquals(0.0, reserva.getCostoReserva(), 0.001);
    }

    @Test
    void conTrasladoRecepcionElCostoTotalEsElDeLosServicios() {
        reserva.addItemReserva(itemHotel);
        reserva.addItemReserva(itemExcursion);

        assertEquals(38000.0, reserva.getCostoTotal(), 0.001);
    }

    @Test
    void conTrasladoVipElCostoTotalSumaElCostoCalculadoPorLaApiSobreElValorDeLaReserva() {
        when(trasladoVipApi.calcularCosto(38000.0)).thenReturn(5000.0);
        reservaVip.addItemReserva(itemHotel);
        reservaVip.addItemReserva(itemExcursion);

        assertEquals(43000.0, reservaVip.getCostoTotal(), 0.001);
        verify(trasladoVipApi).calcularCosto(38000.0);
    }

    @Test
    void conTrasladoEstandarElCostoTotalSumaLaEstimacionPorPasajerosYDireccion() {
        when(plataformaTransporte.estimarTraslado(3, direccionAeropuerto)).thenReturn(1500.0);
        reservaEstandar.addItemReserva(new ItemReserva(desde, hasta, 3, autoCompacto)); // 3 x 8.000

        assertEquals(25500.0, reservaEstandar.getCostoTotal(), 0.001);
    }

    @Test
    void sePuedeCambiarElMetodoDeTraslado() {
        when(trasladoVipApi.calcularCosto(30000.0)).thenReturn(4000.0);
        reserva.addItemReserva(itemHotel);
        assertEquals(30000.0, reserva.getCostoTotal(), 0.001); // recepcion

        reserva.setMetodoDeTraslado(trasladoVip);

        assertEquals(34000.0, reserva.getCostoTotal(), 0.001);
    }

    @Test
    void noSePuedeCambiarElMetodoDeTrasladoPorNulo() {
        assertThrows(NullPointerException.class, () -> reserva.setMetodoDeTraslado(null));
    }

    // ---------- consultas ----------

    @Test
    void cuentaLosPasajerosSoloDeLosItemsQueContienenAlServicio() {
        reserva.addItemReserva(itemHotel);                                      // 2 en hotel
        reserva.addItemReserva(itemExcursion);                                  // 2 en excursion
        reserva.addItemReserva(new ItemReserva(desde, hasta, 3, paqueteEscapada)); // 3 en hotel + excursion

        assertEquals(5, reserva.getCantidadPasajeros(hotel));
        assertEquals(5, reserva.getCantidadPasajeros(excursion));
        assertEquals(0, reserva.getCantidadPasajeros(vuelo));
    }

    @Test
    void cuentaLosPasajerosDeServiciosDentroDePaquetesAnidados() {
        reserva.addItemReserva(new ItemReserva(desde, hasta, 4, paqueteGranTour)); // Gran Tour > Escapada > hotel, y vuelo

        assertEquals(4, reserva.getCantidadPasajeros(hotel));
        assertEquals(4, reserva.getCantidadPasajeros(vuelo));
        assertEquals(0, reserva.getCantidadPasajeros(autoCompacto));
    }

    @Test
    void laFechaDeFinDeLaReservaEsLaMasTardiaDeSusItems() {
        reserva.addItemReserva(new ItemReserva(desde, desde.plusDays(2), 1, hotel));
        reserva.addItemReserva(itemExcursion); // termina en 'hasta'
        reserva.addItemReserva(new ItemReserva(desde, desde.plusDays(1), 1, vuelo));

        assertEquals(hasta, reserva.getFechaFinReserva());
    }

    @Test
    void unaReservaSinItemsNoTieneFechaDeFin() {
        assertNull(reserva.getFechaFinReserva());
    }

    @Test
    void elReembolsoSoloIncluyeLosItemsQueNoEmpezaron() {
        reserva.addItemReserva(new ItemReserva(hoy.minusDays(5), hoy.minusDays(2), 1, hotel)); // ya disfrutado
        reserva.addItemReserva(new ItemReserva(hoy, hoy.plusDays(1), 2, excursion));           // empieza hoy: 8.000
        reserva.addItemReserva(itemHotel);                                                     // futuro: 30.000

        assertEquals(38000.0, reserva.getRembolso(), 0.001);
    }

    @Test
    void siTodoYaFueDisfrutadoElReembolsoEsCero() {
        reserva.addItemReserva(new ItemReserva(hoy.minusDays(5), hoy.minusDays(2), 1, hotel));

        assertEquals(0.0, reserva.getRembolso(), 0.001);
    }

    @Test
    void filtraLosItemsQueCaenEnUnPeriodo() {
        ItemReserva lejano = new ItemReserva(desde.plusDays(30), desde.plusDays(35), 1, excursion);
        reserva.addItemReserva(itemHotel);
        reserva.addItemReserva(lejano);

        assertEquals(List.of(itemHotel), reserva.itemsDeReservaEntre(desde.plusDays(2), desde.plusDays(3)));
        assertEquals(List.of(lejano), reserva.itemsDeReservaEntre(desde.plusDays(31), desde.plusDays(32)));
        assertTrue(reserva.itemsDeReservaEntre(hoy.minusDays(30), hoy.minusDays(20)).isEmpty());
    }

    // ---------- estaPagada ----------

    @Test
    void unaReservaPendienteNoEstaPagada() {
        assertFalse(reserva.estaPagada());
    }

    @Test
    void unaReservaConfirmadaEstaPagada() {
        reserva.confirmar();

        assertTrue(reserva.estaPagada());
    }

    @Test
    void unaReservaFinalizadaEstaPagada() {
        reserva.confirmar();
        reserva.hacerCheckIn();
        reserva.iniciarViaje();
        reserva.finalizarViaje();

        assertTrue(reserva.estaPagada());
    }

    @Test
    void unaReservaCanceladaNoEstaPagada() {
        reserva.confirmar();
        reserva.cancelar();

        assertFalse(reserva.estaPagada());
    }

    // ---------- ciclo de vida y cupo ----------

    @Test
    void recorreElCicloDeVidaCompleto() {
        reserva.confirmar();
        assertEquals(CodigoEstado.CONFIRMADA, reserva.codigoEstado());

        reserva.hacerCheckIn();
        assertEquals(CodigoEstado.EN_CHECKIN, reserva.codigoEstado());

        reserva.iniciarViaje();
        assertEquals(CodigoEstado.EN_CURSO, reserva.codigoEstado());

        reserva.finalizarViaje();
        assertEquals(CodigoEstado.FINALIZADA, reserva.codigoEstado());
    }

    @Test
    void confirmarBloqueaElCupoDeTodosSusItemsEnTodosLosDiasDelPeriodo() {
        reserva.addItemReserva(itemHotel);      // 2 de 10
        reserva.addItemReserva(itemExcursion);  // 2 de 20

        reserva.confirmar();

        assertEquals(8, hotel.cupoDisponible(desde));
        assertEquals(8, hotel.cupoDisponible(hasta));
        assertEquals(10, hotel.cupoDisponible(hasta.plusDays(1)));
        assertEquals(18, excursion.cupoDisponible(desde));
    }

    @Test
    void confirmarUnPaqueteBloqueaElCupoDeCadaUnoDeSusServicios() {
        reserva.addItemReserva(new ItemReserva(desde, hasta, 2, paqueteEscapada));

        reserva.confirmar();

        assertEquals(8, hotel.cupoDisponible(desde));
        assertEquals(18, excursion.cupoDisponible(desde));
    }

    @Test
    void cancelarUnaReservaConfirmadaLiberaElCupo() {
        reserva.addItemReserva(itemHotel);
        reserva.confirmar();

        reserva.cancelar();

        assertEquals(10, hotel.cupoDisponible(desde));
    }

    @Test
    void siUnServicioNoTieneCupoSeDeshaceLoYaReservadoYLaReservaSigueEnPendiente() {
        reserva.addItemReserva(itemHotel);
        reserva.addItemReserva(new ItemReserva(desde, hasta, 1, tourAgotado)); // cupo 0
        ObservadorReserva observador = mock(ObservadorReserva.class);
        reserva.agregarObservador(observador);

        assertThrows(IllegalArgumentException.class, () -> reserva.confirmar());

        assertEquals(10, hotel.cupoDisponible(desde)); // se deshizo la reserva del hotel
        assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, reserva.codigoEstado());
        verifyNoInteractions(observador);
    }

    @Test
    void siFallaElPrimerItemNoSeReservaNada() {
        reserva.addItemReserva(new ItemReserva(desde, hasta, 1, tourAgotado));
        reserva.addItemReserva(itemHotel);

        assertThrows(IllegalArgumentException.class, () -> reserva.confirmar());

        assertEquals(10, hotel.cupoDisponible(desde));
        assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, reserva.codigoEstado());
    }

    // ---------- observadores ----------

    @Test
    void notificaATodosLosObservadoresConElEstadoAnteriorYElNuevo() {
        ObservadorReserva o1 = mock(ObservadorReserva.class);
        ObservadorReserva o2 = mock(ObservadorReserva.class);
        reserva.agregarObservador(o1);
        reserva.agregarObservador(o2);

        reserva.confirmar();

        verify(o1).notificar(same(reserva), isA(SolicitudPendiente.class), isA(Confirmada.class));
        verify(o2).notificar(same(reserva), isA(SolicitudPendiente.class), isA(Confirmada.class));
    }

    @Test
    void notificaCadaUnaDeLasTransiciones() {
        ObservadorReserva observador = mock(ObservadorReserva.class);
        reserva.agregarObservador(observador);

        reserva.confirmar();
        reserva.hacerCheckIn();
        reserva.iniciarViaje();
        reserva.finalizarViaje();

        verify(observador).notificar(same(reserva), isA(SolicitudPendiente.class), isA(Confirmada.class));
        verify(observador).notificar(same(reserva), isA(Confirmada.class), isA(EnCheckIn.class));
        verify(observador).notificar(same(reserva), isA(EnCheckIn.class), isA(EnCurso.class));
        verify(observador).notificar(same(reserva), isA(EnCurso.class), isA(Finalizada.class));
        verifyNoMoreInteractions(observador);
    }

    @Test
    void notificaLaCancelacion() {
        ObservadorReserva observador = mock(ObservadorReserva.class);
        reserva.agregarObservador(observador);

        reserva.cancelar();

        verify(observador).notificar(same(reserva), isA(SolicitudPendiente.class), isA(Cancelada.class));
    }

    @Test
    void unObservadorQuitadoDejaDeSerNotificado() {
        ObservadorReserva observador = mock(ObservadorReserva.class);
        reserva.agregarObservador(observador);
        reserva.quitarObservador(observador);

        reserva.confirmar();

        verifyNoInteractions(observador);
    }

    @Test
    void noSePuedeAgregarUnObservadorNulo() {
        assertThrows(NullPointerException.class, () -> reserva.agregarObservador(null));
    }

    @Test
    void unaOperacionInvalidaNoNotificaNada() {
        ObservadorReserva observador = mock(ObservadorReserva.class);
        reserva.agregarObservador(observador);

        assertThrows(OperacionInvalidaException.class, () -> reserva.hacerCheckIn());

        verifyNoInteractions(observador);
        assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, reserva.codigoEstado());
    }
}
