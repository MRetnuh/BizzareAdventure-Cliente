package mecanicas;

import com.badlogic.gdx.Game;
import audios.Musica;
import input.InputController;
import niveles.NivelBase;
import pantallas.Opciones;
import personajes.Personaje;
import juego.Partida;

public class GestorInputs {

    public static void procesarInputs(Personaje personaje1, Personaje personaje2, InputController inputController,
    Musica musicaPartida, NivelBase nivelActual, float delta, Game juego, Partida partidaActual) {

        if (personaje1.getVida() > 0) {
            personaje1.setMoviendoDerecha(inputController.getDerecha1());
            personaje1.setMoviendoIzquierda(inputController.getIzquierda1());
            personaje1.setEstaSaltando(inputController.getSaltar1());

            if (inputController.getAtacar1()) {
                personaje1.iniciarAtaque(musicaPartida.getVolumen(), delta, nivelActual);
                inputController.setAtacarFalso1();
            }
            if (inputController.getOpciones1()) {
                juego.setScreen(new Opciones(juego, partidaActual, musicaPartida));
                inputController.setOpcionesFalso1();
            }
        }
        else {
            inputController.setAtacarFalso1();
            inputController.setOpcionesFalso1();
        }

        if (personaje2.getVida() > 0) {
            personaje2.setMoviendoDerecha(inputController.getDerecha2());
            personaje2.setMoviendoIzquierda(inputController.getIzquierda2());
            personaje2.setEstaSaltando(inputController.getSaltar2());

            if (inputController.getAtacar2()) {
                personaje2.iniciarAtaque(musicaPartida.getVolumen(), delta, nivelActual);
                inputController.setAtacarFalso2();
            }
            if (inputController.getOpciones2()) {
                juego.setScreen(new Opciones(juego, partidaActual, musicaPartida));
                inputController.setOpcionesFalso2();
            }
        }
        else {
            inputController.setAtacarFalso2();
            inputController.setOpcionesFalso2();
        }
    }
}