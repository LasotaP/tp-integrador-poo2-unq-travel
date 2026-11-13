package unqTravel.reportes;

import unqTravel.catalogo.PaqueteTuristico;
import unqTravel.catalogo.ServicioIndividual;

public interface Visitante {
    void visitarServicio(ServicioIndividual servicio);
    void visitarPaquete(PaqueteTuristico paquete);
}
