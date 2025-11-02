package mecanicas;

import com.badlogic.gdx.scenes.scene2d.Stage;

import audios.Musica;
import enemigos.EnemigoBase;
import jugadores.Jugador;
import niveles.NivelBase;
import proyectiles.Proyectil;

public class GestorEnemigos {
    public static void actualizar(float delta, NivelBase nivel, Jugador[] jugadores, Stage stage, Musica musica) {
        for (EnemigoBase enemigo : nivel.getEnemigos()) {
            if (enemigo.getVida() > 0) {
                for (Proyectil b : enemigo.getBalas()) {
                    if (!stage.getActors().contains(b, true)) {
                        stage.addActor(b);
                    }
                }
                enemigo.actualizarIA(delta, jugadores[0].getPersonajeElegido(), jugadores[1].getPersonajeElegido(), musica.getVolumen(), nivel);
            }
        }
	    }
    }

