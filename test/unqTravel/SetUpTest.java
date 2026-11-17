package unqTravel;

import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;

import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.Cupo;
import unqTravel.catalogo.Direccion;
import unqTravel.catalogo.PaqueteTuristico;
import unqTravel.catalogo.ServicioIndividual;
import unqTravel.catalogo.atributos.AtributoDinamico;
import unqTravel.catalogo.atributos.TipoBooleano;
import unqTravel.catalogo.atributos.TipoFecha;
import unqTravel.catalogo.atributos.TipoNumerico;
import unqTravel.catalogo.atributos.TipoTexto;
import unqTravel.catalogo.busqueda.CatalogoServicios;
import unqTravel.comprobante.GeneradorComprobante;
import unqTravel.pago.BilleteraVirtual;
import unqTravel.pago.BilleteraVirtualAPI;
import unqTravel.pago.TarjetaDeCredito;
import unqTravel.pago.TarjetaDeCreditoAPI;
import unqTravel.pago.TransferenciaBancaria;
import unqTravel.pago.TransferenciaBancariaAPI;
import unqTravel.reportes.formato.FormatoCSV;
import unqTravel.reportes.formato.FormatoHTML;
import unqTravel.reportes.formato.FormatoTextoPlano;
import unqTravel.reserva.Cliente;
import unqTravel.reserva.CodigoEstado;
import unqTravel.reserva.ItemReserva;
import unqTravel.reserva.Reserva;
import unqTravel.reserva.notificacion.Fidelizacion;
import unqTravel.reserva.notificacion.MailSender;
import unqTravel.reserva.notificacion.NotificadorEmail;
import unqTravel.traslado.MetodoTraslado;
import unqTravel.traslado.PlataformaTransporteAPI;
import unqTravel.traslado.TrasladoEstandar;
import unqTravel.traslado.TrasladoRecepcion;
import unqTravel.traslado.TrasladoVIP;
import unqTravel.traslado.TrasladoVIPAPI;

public abstract class SetUpTest {

	// ================= RESERVA =================
	protected Reserva reservaRecepcion;
	protected Reserva reservaVip;
	protected Reserva reservaEstandar;
	
    // ================= FECHAS =================
    protected LocalDate hoy;
    protected LocalDate desde;  // hoy + 10
    protected LocalDate hasta;  // hoy + 15

    // ================= MOCKS =================
    protected MailSender mailSender;
    protected PlataformaTransporteAPI plataformaTransporte;
    protected TrasladoVIPAPI trasladoVipApi;
    protected TarjetaDeCreditoAPI tarjetaApi;
    protected TransferenciaBancariaAPI transferenciaApi;
    protected BilleteraVirtualAPI billeteraApi;

    // ================= DIRECCIONES =================
    protected Direccion direccionHotel;
    protected Direccion direccionMuseo;
    protected Direccion direccionAeropuerto;

    // ================= SERVICIOS INDIVIDUALES =================

    protected ServicioIndividual hotel;
    protected ServicioIndividual excursion;
    protected ServicioIndividual vuelo;
    protected ServicioIndividual autoCompacto;
    protected ServicioIndividual tourAgotado;

    // ================= PAQUETES =================
    protected PaqueteTuristico paqueteEscapada;
    protected PaqueteTuristico paqueteGranTour;

    // ================= ATRIBUTOS DINAMICOS =================

    protected AtributoDinamico capacidadMaxima;         // obligatorio, numerico, "4"      (en hotel)
    protected AtributoDinamico politicaMascotas;        // opcional,    booleano, "true"   (en hotel)
    protected AtributoDinamico ratingEstrellas;         // obligatorio, numerico, "4.5"    (en hotel)
    protected AtributoDinamico fechaApertura;           // obligatorio, fecha,    sin valor
    protected AtributoDinamico notaInterna;             // opcional,    texto,    sin valor
    protected AtributoDinamico superficieInvalida;      // obligatorio, numerico, "abc" (inconsistente con su tipo)

    // ================= CATALOGO Y SISTEMA =================

    protected CatalogoServicios catalogo;
    protected UnqTravel sistema;

    // ================= CLIENTE Y RESERVA =================
    protected Cliente cliente;
    protected ItemReserva itemHotel;       
    protected ItemReserva itemExcursion;   
    protected Reserva reserva;             

    // ================= METODOS DE TRASLADO =================
    protected MetodoTraslado trasladoRecepcion;   
    protected MetodoTraslado trasladoEstandar;    
    protected MetodoTraslado trasladoVip;         

    // ================= METODOS DE PAGO =================
    protected TarjetaDeCredito tarjeta;
    protected TransferenciaBancaria transferencia;
    protected BilleteraVirtual billetera;

    // ================= OBSERVADORES =================
    protected NotificadorEmail notificadorEmail;
    protected Fidelizacion fidelizacion;
    protected GeneradorComprobante generadorComprobante;

    // ================= FORMATOS DE REPORTE =================
    protected FormatoTextoPlano formatoTexto;
    protected FormatoCSV formatoCsv;
    protected FormatoHTML formatoHtml;

    @BeforeEach
    public void setUp() {
        // ---- Fechas ----
        hoy = LocalDate.now();
        desde = hoy.plusDays(10);
        hasta = hoy.plusDays(15);

        // ---- Mocks ----
        mailSender = mock(MailSender.class);
        plataformaTransporte = mock(PlataformaTransporteAPI.class);
        trasladoVipApi = mock(TrasladoVIPAPI.class);
        tarjetaApi = mock(TarjetaDeCreditoAPI.class);
        transferenciaApi = mock(TransferenciaBancariaAPI.class);
        billeteraApi = mock(BilleteraVirtualAPI.class);

        // ---- Direcciones ----
        direccionHotel = new Direccion("Av. Corrientes", 1234);
        direccionMuseo = new Direccion("Defensa", 1000);
        direccionAeropuerto = new Direccion("Autopista Tte. Gral. Richieri", 0);

        // ---- Servicios individuales ----
        hotel = new ServicioIndividual("HOT-001", "Noche de Hotel Central", "Hotel en el centro",
                direccionHotel, "Operador Sur", Categoria.ALOJAMIENTO, 15000, cupoConTotal(10), 0.0);
        excursion = new ServicioIndividual("EXC-001", "Excursion Guiada Museo", "Visita guiada",
                direccionMuseo, "Operador Norte", Categoria.EXCURSION, 4000, cupoConTotal(20), 0.0);
        vuelo = new ServicioIndividual("VUE-001", "Vuelo Buenos Aires - Roma", "Vuelo directo",
                direccionAeropuerto, "Operador Norte", Categoria.VUELO, 100000, cupoConTotal(5), 0.10);
        autoCompacto = new ServicioIndividual("AUT-001", "Alquiler Auto Compacto", "Auto compacto con seguro",
                direccionAeropuerto, "Operador Sur", Categoria.ALQUILER_DE_AUTO, 8000, cupoConTotal(3), 0.0);
        tourAgotado = new ServicioIndividual("EXC-002", "Tour Gastronomico", "Degustacion en el centro",
                direccionMuseo, "Operador Norte", Categoria.EXCURSION, 6000, cupoConTotal(0), 0.0);

        // ---- Atributos dinamicos ----
        capacidadMaxima = new AtributoDinamico("CapacidadMaxima", true, new TipoNumerico());
        capacidadMaxima.setValor("4");
        politicaMascotas = new AtributoDinamico("PoliticaMascotas", false, new TipoBooleano());
        politicaMascotas.setValor("true");
        ratingEstrellas = new AtributoDinamico("RatingEstrellas", true, new TipoNumerico());
        ratingEstrellas.setValor("4.5");
        fechaApertura = new AtributoDinamico("FechaApertura", true, new TipoFecha());
        notaInterna = new AtributoDinamico("NotaInterna", false, new TipoTexto());
        superficieInvalida = new AtributoDinamico("Superficie", true, new TipoNumerico());
        superficieInvalida.setValor("abc");

        hotel.agregarAtributoDinamico(capacidadMaxima);
        hotel.agregarAtributoDinamico(politicaMascotas);
        hotel.agregarAtributoDinamico(ratingEstrellas);

        // ---- Paquetes ----
        paqueteEscapada = new PaqueteTuristico("Pack Escapada Urbana", Categoria.PAQUETE,
                "Hotel + excursion", 0.10);
        paqueteEscapada.addServicio(hotel);
        paqueteEscapada.addServicio(excursion);

        paqueteGranTour = new PaqueteTuristico("Kit Gran Tour", Categoria.PAQUETE,
                "Escapada + vuelo", 0.05);
        paqueteGranTour.addServicio(paqueteEscapada);
        paqueteGranTour.addServicio(vuelo);

        // ---- Catalogo y sistema ----
        catalogo = new CatalogoServicios();
        catalogo.addServicio(hotel);
        catalogo.addServicio(excursion);
        catalogo.addServicio(vuelo);
        catalogo.addServicio(autoCompacto);
        catalogo.addServicio(tourAgotado);
        catalogo.addServicio(paqueteEscapada);
        catalogo.addServicio(paqueteGranTour);
        sistema = new UnqTravel(catalogo);

        // ---- Cliente ----
        cliente = new Cliente("Juan Perez", "juan.perez@mail.com");
        itemHotel = new ItemReserva(desde, hasta, 2, hotel);
        itemExcursion = new ItemReserva(desde, hasta, 2, excursion);

        // ---- Metodos de traslado ----
        trasladoRecepcion = new TrasladoRecepcion();
        trasladoEstandar = new TrasladoEstandar(plataformaTransporte, hotel);
        trasladoVip = new TrasladoVIP(trasladoVipApi);

        // ---- Metodos de pago ----
        tarjeta = new TarjetaDeCredito("4111111111111111", "123", hoy.plusYears(2), tarjetaApi);
        transferencia = new TransferenciaBancaria("0000003100012345678901", "juan.perez.alias", transferenciaApi);
        billetera = new BilleteraVirtual(billeteraApi);

        // ---- Observadores ----
        notificadorEmail = new NotificadorEmail(mailSender);
        fidelizacion = new Fidelizacion(mailSender);
        generadorComprobante = new GeneradorComprobante();

        // ---- Formatos de reporte ----
        formatoTexto = new FormatoTextoPlano();
        formatoCsv = new FormatoCSV();
        formatoHtml = new FormatoHTML();
        
        // ---- Reserva ----
        reservaRecepcion = new Reserva(cliente, "1", new TrasladoRecepcion()); // Reserva traslado recepcion
        reservaVip = new Reserva(cliente, "2", new TrasladoVIP(trasladoVipApi)); // Reserva traslado VIP
        reservaEstandar = new Reserva(cliente, "3", new TrasladoEstandar(plataformaTransporte, autoCompacto)); // Reserva traslado estandar
        

    }
    

    // ================= HELPERS =================

     // LE HACEMOS CONSTRUCTOR A CUPO ???
    protected Cupo cupoConTotal(int cantidad) {
        Cupo cupo = new Cupo();
        cupo.definirTotal(hoy, hoy.plusDays(30), cantidad);
        return cupo;
    }

}