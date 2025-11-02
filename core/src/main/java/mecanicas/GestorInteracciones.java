package mecanicas;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import jugadores.Jugador;
import niveles.NivelBase;
import personajes.Personaje;

public class GestorInteracciones {

    public static void procesarGolpeCaja(Personaje personaje, Jugador jugador, boolean esJugador1, NivelBase nivel,
    Stage stage, GestorHUD gestorHUD, Jugador[] jugadores) {

        if (!personaje.getEstaAtacando()) return;

        Rectangle hitboxOriginal = personaje.getHitbox();
        boolean cajaRota;

        if(personaje.getEstaSaltando()) {
            Rectangle hitboxAumentada = new Rectangle(
                    hitboxOriginal.x,
                    hitboxOriginal.y - (hitboxOriginal.height - 12.0f),
                    hitboxOriginal.width,
                    20.0f);
            cajaRota = nivel.destruirCajaEnHitbox(hitboxAumentada);
        }

        final float ALTURA_REDUCIDA = 10.0f;
        Rectangle hitboxReducida = new Rectangle(
                hitboxOriginal.x,
                hitboxOriginal.y + (hitboxOriginal.height - ALTURA_REDUCIDA),
                hitboxOriginal.width,
                ALTURA_REDUCIDA
        );

        cajaRota = nivel.destruirCajaEnHitbox(hitboxReducida);

        if (!cajaRota) {
            return;
        }
        if (jugadores[0].getPersonajeElegido().getVida() <= 0 && jugadores[1].getPersonajeElegido().getVida() > 0) {
            jugadores[0].getPersonajeElegido().setPosicion(
                    jugadores[1].getPersonajeElegido().getX(),
                    jugadores[1].getPersonajeElegido().getY()
            );
            jugadores[0].getPersonajeElegido().aumentarVida();
        } else if (jugadores[1].getPersonajeElegido().getVida() <= 0 && jugadores[0].getPersonajeElegido().getVida() > 0) {
            jugadores[1].getPersonajeElegido().setPosicion(
                    jugadores[0].getPersonajeElegido().getX(),
                    jugadores[0].getPersonajeElegido().getY()
            );
            jugadores[1].getPersonajeElegido().aumentarVida();
        } else {
            // Cambiar personaje
            stage.getActors().removeValue(jugador.getPersonajeElegido(), true);
            stage.addActor(jugador.cambiarPersonaje(
                    personaje.getX(), personaje.getY()
            ));
        }

        gestorHUD.actualizar();
    }
}
