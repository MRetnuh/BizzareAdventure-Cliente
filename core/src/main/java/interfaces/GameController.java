package interfaces;

public interface GameController {
    void conectar(int numPlayer);
    void empezar(int p1Id, int p2Id);
    void perder();
    void actualizarEstado(String[] datos);
    void volverAlMenu();
}
