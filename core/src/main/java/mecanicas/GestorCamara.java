package mecanicas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import personajes.Personaje;

public class GestorCamara {
	 public static void actualizar(OrthographicCamera camara, Personaje p1, Personaje p2, float anchoMapa, float alturaMapa) {
		  float centroX;
	        float centroY;

	        boolean vivo1 = p1.getVida() > 0;
	        boolean vivo2 = p2.getVida() > 0;

	        if (vivo1 && vivo2) {
	            centroX = (p1.getX() + p2.getX()) / 2f + p1.getWidth() / 2f;
	            centroY = (p1.getY() + p2.getY()) / 2f + p1.getHeight() / 2f;
	        } else if (vivo1) {
	            centroX = p1.getX() + p1.getWidth() / 2f;
	            centroY = p1.getY() + p1.getHeight() / 2f;
	        } else if (vivo2) {
	            centroX = p2.getX() + p2.getWidth() / 2f;
	            centroY = p2.getY() + p2.getHeight() / 2f;
	        } else {
	            return;
	        }

	        float halfWidth = camara.viewportWidth / 2f;
	        float halfHeight = camara.viewportHeight / 2f;

	        centroX = MathUtils.clamp(centroX, halfWidth, anchoMapa - halfWidth);
	        centroY = MathUtils.clamp(centroY, halfHeight, alturaMapa - halfHeight);

	        if (anchoMapa < camara.viewportWidth) {
	            centroX = anchoMapa / 2f;
	        }
	        if (alturaMapa < camara.viewportHeight) {
	            centroY = alturaMapa / 2f;
	        }

	        camara.position.set(centroX, centroY, 0);

	        camara.position.x = Math.round(camara.position.x);
	        camara.position.y = Math.round(camara.position.y);
	        camara.update();
	    }
	}

