package servicios.reportes;

import servicios.PaqueteTuristico;
import servicios.ServicioIndividual;

public interface Visitante {
    void visitarServicio(ServicioIndividual servicio);
    void visitarPaquete(PaqueteTuristico paquete);
}
