package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;

public class Voucher extends Comprobante {

    public Voucher(String numero, LocalDate fecha, Cliente cliente, List<Servicio> servicios) {
        super(numero, fecha, cliente);
    }
}

