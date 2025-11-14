package interfaces;

public interface GameController {
    void conectar(int numPlayer);
    void empezar(int p1Id, int p2Id);
    void asignarNivel(int indice);
    void perder();
    void actualizarEstado(String[] datos);
    void volverAlMenu();
    void animar(int idJugador, boolean izquierda, boolean derecha, boolean saltar);
    void actualizarMovimientoEnemigos(String[] datos);
    void actualizarBalasEnemigos(String[] datos);
}
