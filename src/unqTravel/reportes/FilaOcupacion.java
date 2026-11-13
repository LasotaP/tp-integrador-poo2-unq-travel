package unqTravel.reportes;

import unqTravel.catalogo.Categoria;

public class FilaOcupacion {
    private final Categoria categoria;
    private final int total;
    private final int reservados;

    public FilaOcupacion(Categoria categoria, int total, int reservados) {
        this.categoria = categoria;
        this.total = total;
        this.reservados = reservados;
    }

    public Categoria getCategoria() { return categoria; }
    public int getTotal() { return total; }
    public int getReservados() { return reservados; }
    public int getLibres() { return total - reservados; }
}
