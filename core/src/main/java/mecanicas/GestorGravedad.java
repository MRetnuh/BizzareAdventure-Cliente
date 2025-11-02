package mecanicas;

import personajes.Personaje;
import niveles.NivelBase;
import com.badlogic.gdx.math.Rectangle;

public class GestorGravedad {

    private static final float GRAVEDAD = -500;

    public static void aplicarGravedad(Personaje personaje, float delta, NivelBase nivel) {

    	if(personaje.getVida() <= 0) {
    		return;
    	}
    		
        boolean estaSobreElSuelo = nivel.detectarColision(
            new Rectangle(personaje.getX(), personaje.getY() - 1, 16, 16)
        );

        personaje.guardarPosicionAnterior();

        if (!estaSobreElSuelo) {
            personaje.setVelocidadCaida(personaje.getVelocidadCaida() + GRAVEDAD * delta);
            personaje.setY(personaje.getY() + personaje.getVelocidadCaida() * delta);
        } else {
            personaje.setVelocidadCaida(0);
        }

        if (personaje.getY() < -190) {
            personaje.reducirVida();
        }
    }
}
