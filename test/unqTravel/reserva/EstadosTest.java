package unqTravel.reserva;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import unqTravel.SetUpTest;
import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.Cupo;
import unqTravel.catalogo.ServicioIndividual;
import unqTravel.comprobante.Comprobante;
import unqTravel.comprobante.Voucher;

/**
 * Tests del patron State. Cada estado se prueba a traves de la Reserva (su contexto):
 *  - solo permite las operaciones validas y lanza OperacionInvalidaException en el resto,
 *  - las transiciones validas llegan al estado correcto,
 *  - efectos de cada estado: cupo (real, se consulta en el hotel) y vouchers de reembolso.
 *
 * 'reserva' es un spy de reservaVip (traslado VIP, tasas = $500 por stub) con itemHotel cargado
 * (2 x $15.000 = $30.000). El spy permite verificar los comprobantes registrados.
 */
class EstadosTest extends SetUpTest {

    @BeforeEach
    void iniciarReservaConItem() {
        when(trasladoVipApi.calcularCosto(anyDouble())).thenReturn(500.0);
        reserva = spy(reservaVip);
        reserva.addItemReserva(itemHotel);
    }

    // ---------- helpers ----------

    /** Lleva la reserva al estado pedido por el camino valido. */
    private void llevarA(CodigoEstado destino) {
        switch (destino) {
            case SOLICITUD_PENDIENTE:
                break;
            case CONFIRMADA:
                reserva.confirmar();
                break;
            case EN_CHECKIN:
                reserva.confirmar();
                reserva.hacerCheckIn();
                break;
            case EN_CURSO:
                reserva.confirmar();
                reserva.hacerCheckIn();
                reserva.iniciarViaje();
                break;
            case FINALIZADA:
                reserva.confirmar();
                reserva.hacerCheckIn();
                reserva.iniciarViaje();
                reserva.finalizarViaje();
                break;
            case CANCELADA:
                reserva.cancelar();
                break;
        }
        assertEquals(destino, reserva.codigoEstado());
        clearInvocations(reserva);
    }

    private Map<String, Runnable> operaciones() {
        Map<String, Runnable> ops = new LinkedHashMap<>();
        ops.put("addItemReserva", () -> reserva.addItemReserva(itemExcursion));
        ops.put("quitarItemReserva", () -> reserva.quitarItemReserva(itemHotel));
        ops.put("confirmar", () -> reserva.confirmar());
        ops.put("hacerCheckIn", () -> reserva.hacerCheckIn());
        ops.put("iniciarViaje", () -> reserva.iniciarViaje());
        ops.put("finalizarViaje", () -> reserva.finalizarViaje());
        ops.put("cancelar", () -> reserva.cancelar());
        return ops;
    }

    /** Toda operacion que NO este en 'permitidas' debe lanzar OperacionInvalidaException sin cambiar el estado. */
    private void assertSoloPermite(CodigoEstado estado, String... permitidas) {
        Set<String> ok = Set.of(permitidas);
        operaciones().forEach((nombre, operacion) -> {
            if (!ok.contains(nombre)) {
                OperacionInvalidaException e = assertThrows(OperacionInvalidaException.class,
                        () -> operacion.run(),
                        nombre + " deberia ser invalida en " + estado);
                assertTrue(e.getMessage().contains(estado.name()),
                        "El mensaje deberia mencionar el estado " + estado + ": " + e.getMessage());
                assertEquals(estado, reserva.codigoEstado(), nombre + " no deberia cambiar el estado");
            }
        });
    }

    private Voucher voucherRegistrado() {
        ArgumentCaptor<Comprobante> captor = ArgumentCaptor.forClass(Comprobante.class);
        verify(reserva).addComprobante(captor.capture());
        assertTrue(captor.getValue() instanceof Voucher, "El comprobante deberia ser un Voucher");
        return (Voucher) captor.getValue();
    }

    // =====================================================================
    @Nested
    class SolicitudPendienteTest {

        @Test
        void tieneElCodigoSolicitudPendiente() {
            assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, new SolicitudPendiente().codigo());
        }

        @Test
        void soloPermiteAgregarQuitarConfirmarYCancelar() {
            assertSoloPermite(CodigoEstado.SOLICITUD_PENDIENTE,
                    "addItemReserva", "quitarItemReserva", "confirmar", "cancelar");
        }

        @Test
        void agregaYQuitaItems() {
            reserva.addItemReserva(itemExcursion);
            assertEquals(2, reserva.getItemsReserva().size());

            reserva.quitarItemReserva(itemExcursion);
            assertEquals(1, reserva.getItemsReserva().size());
            assertSame(itemHotel, reserva.getItemsReserva().get(0));
        }

        @Test
        void agregarItemsNoTocaElCupo() {
            assertEquals(10, hotel.cupoDisponible(desde));
        }

        @Test
        void confirmarBloqueaElCupoYPasaAConfirmada() {
            reserva.confirmar();

            assertEquals(CodigoEstado.CONFIRMADA, reserva.codigoEstado());
            assertEquals(8, hotel.cupoDisponible(desde));
        }

        @Test
        void siNoHayCupoConfirmarFallaDeshaceLoReservadoYSigueEnPendiente() {
            reserva.addItemReserva(new ItemReserva(desde, hasta, 1, tourAgotado));

            assertThrows(IllegalArgumentException.class, () -> reserva.confirmar());

            assertEquals(CodigoEstado.SOLICITUD_PENDIENTE, reserva.codigoEstado());
            assertEquals(10, hotel.cupoDisponible(desde));
        }

        @Test
        void cancelarPasaACanceladaSinTocarElCupoNiGenerarComprobantes() {
            reserva.cancelar();

            assertEquals(CodigoEstado.CANCELADA, reserva.codigoEstado());
            assertEquals(10, hotel.cupoDisponible(desde));
            verify(reserva, never()).addComprobante(any());
        }
    }

    // =====================================================================
    @Nested
    class ConfirmadaTest {

        @BeforeEach
        void confirmar() {
            llevarA(CodigoEstado.CONFIRMADA);
        }

        @Test
        void tieneElCodigoConfirmada() {
            assertEquals(CodigoEstado.CONFIRMADA, new Confirmada().codigo());
        }

        @Test
        void soloPermiteCheckInYCancelar() {
            assertSoloPermite(CodigoEstado.CONFIRMADA, "hacerCheckIn", "cancelar");
        }

        @Test
        void hacerCheckInPasaAEnCheckInSinLiberarCupo() {
            reserva.hacerCheckIn();

            assertEquals(CodigoEstado.EN_CHECKIN, reserva.codigoEstado());
            assertEquals(8, hotel.cupoDisponible(desde));
        }

        @Test
        void cancelarLiberaElCupoYPasaACancelada() {
            reserva.cancelar();

            assertEquals(CodigoEstado.CANCELADA, reserva.codigoEstado());
            assertEquals(10, hotel.cupoDisponible(desde));
            assertEquals(10, hotel.cupoDisponible(hasta));
        }

        @Test
        void cancelarNoGeneraVoucherDeReembolso() {
            reserva.cancelar();

            verify(reserva, never()).addComprobante(any());
        }
    }

    // =====================================================================
    @Nested
    class EnCheckInTest {

        @BeforeEach
        void hacerCheckIn() {
            llevarA(CodigoEstado.EN_CHECKIN);
        }

        @Test
        void tieneElCodigoEnCheckIn() {
            assertEquals(CodigoEstado.EN_CHECKIN, new EnCheckIn().codigo());
        }

        @Test
        void soloPermiteIniciarViajeYCancelar() {
            assertSoloPermite(CodigoEstado.EN_CHECKIN, "iniciarViaje", "cancelar");
        }

        @Test
        void iniciarViajePasaAEnCurso() {
            reserva.iniciarViaje();

            assertEquals(CodigoEstado.EN_CURSO, reserva.codigoEstado());
        }

        @Test
        void cancelarLiberaElCupoYPasaACancelada() {
            reserva.cancelar();

            assertEquals(CodigoEstado.CANCELADA, reserva.codigoEstado());
            assertEquals(10, hotel.cupoDisponible(desde));
        }

        @Test
        void cancelarReembolsaServiciosYTasas() {
            reserva.cancelar();

            Voucher voucher = voucherRegistrado();
            // servicios: 2 x 15.000 = 30.000 + tasas de traslado: 500
            assertEquals(30500.0, voucher.getMonto(), 0.001);
            assertSame(cliente, voucher.getCliente());
            assertEquals(reserva.getItemsReserva(), voucher.getItems());
            assertNotNull(voucher.getFecha());
        }
    }

    // =====================================================================
    @Nested
    class EnCursoTest {

        /** Servicio con cupo cargado en el pasado, para poder reservar un item que ya se disfruto. */
        private ServicioIndividual spa;

        @BeforeEach
        void iniciarViaje() {
            Cupo cupoConPasado = new Cupo();
            cupoConPasado.definirTotal(hoy.minusDays(10), hoy.plusDays(30), 5);
            spa = new ServicioIndividual("SPA-001", "Spa del hotel", "Circuito de spa", direccionHotel,
                    "Operador Sur", Categoria.EXCURSION, 3000, cupoConPasado, 0.0);

            // Hay que agregarlo antes de salir de PENDIENTE.
            reserva.addItemReserva(new ItemReserva(hoy.minusDays(5), hoy.minusDays(2), 1, spa)); // $3.000, ya disfrutado
            llevarA(CodigoEstado.EN_CURSO);
        }

        @Test
        void tieneElCodigoEnCurso() {
            assertEquals(CodigoEstado.EN_CURSO, new EnCurso().codigo());
        }

        @Test
        void soloPermiteFinalizarViajeYCancelar() {
            assertSoloPermite(CodigoEstado.EN_CURSO, "finalizarViaje", "cancelar");
        }

        @Test
        void finalizarViajePasaAFinalizada() {
            reserva.finalizarViaje();

            assertEquals(CodigoEstado.FINALIZADA, reserva.codigoEstado());
        }

        @Test
        void cancelarPasaACancelada() {
            reserva.cancelar();

            assertEquals(CodigoEstado.CANCELADA, reserva.codigoEstado());
        }

        @Test
        void cancelarReembolsaSoloLosServiciosNoDisfrutadosYSinTasas() {
            reserva.cancelar();

            Voucher voucher = voucherRegistrado();
            // Solo el hotel futuro (30.000). No entra el spa ya disfrutado (3.000) ni las tasas (500).
            assertEquals(30000.0, voucher.getMonto(), 0.001);
            assertNotEquals(reserva.getCostoTotal(), voucher.getMonto(), 0.001);
            assertEquals(hasta, voucher.getFecFin());
            assertSame(cliente, voucher.getCliente());
        }

        @Test
        void cancelarNoLiberaElCupo() {
            reserva.cancelar();

            assertEquals(8, hotel.cupoDisponible(desde));
        }
    }

    // =====================================================================
    @Nested
    class FinalizadaTest {

        @BeforeEach
        void finalizar() {
            llevarA(CodigoEstado.FINALIZADA);
        }

        @Test
        void tieneElCodigoFinalizada() {
            assertEquals(CodigoEstado.FINALIZADA, new Finalizada().codigo());
        }

        @Test
        void esTerminalYNoPermiteNingunaOperacion() {
            assertSoloPermite(CodigoEstado.FINALIZADA);
        }

        @Test
        void elMensajeDeLaExcepcionIndicaOperacionYEstado() {
            OperacionInvalidaException e = assertThrows(OperacionInvalidaException.class, () -> reserva.cancelar());

            assertEquals("No se puede cancelar en estado FINALIZADA", e.getMessage());
        }
    }

    // =====================================================================
    @Nested
    class CanceladaTest {

        @BeforeEach
        void cancelar() {
            llevarA(CodigoEstado.CANCELADA);
        }

        @Test
        void tieneElCodigoCancelada() {
            assertEquals(CodigoEstado.CANCELADA, new Cancelada().codigo());
        }

        @Test
        void esTerminalYNoPermiteNingunaOperacion() {
            assertSoloPermite(CodigoEstado.CANCELADA);
        }

        @Test
        void noSePuedeCancelarDosVecesNiReconfirmar() {
            OperacionInvalidaException e1 = assertThrows(OperacionInvalidaException.class, () -> reserva.cancelar());
            OperacionInvalidaException e2 = assertThrows(OperacionInvalidaException.class, () -> reserva.confirmar());

            assertEquals("No se puede cancelar en estado CANCELADA", e1.getMessage());
            assertEquals("No se puede confirmar en estado CANCELADA", e2.getMessage());
        }
    }
}
