package mecanicas;

import audios.Musica;
import personajes.Personaje;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GestorDerrota {

    private boolean gameOver1 = false;
    private boolean gameOver2 = false;

    public boolean isGameOver1() { return gameOver1; }
    public boolean isGameOver2() { return gameOver2; }

    public void manejarMuerteJugador(Personaje personaje, boolean esJugador1,
                                     Musica musicaPartida, Stage stageHUD) {
        if (personaje.getVida() > 0) return;

        if ((esJugador1 && !gameOver1) || (!esJugador1 && !gameOver2)) {
            if (esJugador1) gameOver1 = true;
            else gameOver2 = true;

            if (gameOver1 && gameOver2) {
                musicaPartida.cambiarMusica("Derrota");
                personaje.morir(stageHUD);
            }
        }
    }

    public boolean partidaTerminada() {
        return gameOver1 && gameOver2;
    }

    public void resetear() {
        gameOver1 = false;
        gameOver2 = false;
    }
}
