package enemigos;

import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import audios.EfectoSonido;
import proyectiles.Proyectil;
import niveles.NivelBase;
import personajes.Personaje;
import personajes.TipoAtaque;

public class EnemigoTirador extends EnemigoBase {

    public EnemigoTirador(String nombre, float x, float y) {
        super(nombre, 100, "Disparo", 1, TipoEnemigo.TIRADOR);
        setPosition(x, y);
        super.puntoInicialX = x;
    }

    @Override
    protected void cargarTexturas() {
        Array<TextureRegion> framesDerecha = new Array<>();
        for (int i = 1; i <= 4; i++) {
            framesDerecha.add(new TextureRegion(new Texture(Gdx.files.internal(
                    "imagenes/personajes/enemigo/Enemigo_Moviendose_Derecha_" + i + ".png"))));
        }
        super.animDerecha = new Animation<>(0.1f, framesDerecha, Animation.PlayMode.LOOP);

        Array<TextureRegion> framesIzquierda = new Array<>();
        for (int i = 1; i <= 4; i++) {
            framesIzquierda.add(new TextureRegion(new Texture(Gdx.files.internal(
                    "imagenes/personajes/enemigo/Enemigo_Moviendose_Izquierda_" + i + ".png"))));
        }
        super.animIzquierda = new Animation<>(0.1f, framesIzquierda, Animation.PlayMode.LOOP);

        super.quietaDerecha = new TextureRegion(new Texture(Gdx.files.internal(
                "imagenes/personajes/enemigo/enemigo_quieto_derecha.png")));
        super.quietaIzquierda = new TextureRegion(new Texture(Gdx.files.internal(
                "imagenes/personajes/enemigo/enemigo_quieto_izquierda.png")));
    }
    
    @Override
    public void actualizarIA(float delta, Personaje jugador1, Personaje jugador2, float volumen, NivelBase nivel){
        super.seleccionarObjetivo(jugador1, jugador2);

        if (super.objetivoActual != null) {
            super.estaMoviendose = false;
            super.tiempoDisparo += delta;
            this.rangoVision = 500;

            super.mirandoDerecha = super.objetivoActual.getX() > super.getX();
            super.frame = super.mirandoDerecha ? super.quietaDerecha : super.quietaIzquierda;

            if (super.tiempoDisparo >= super.COOLDOWNDISPARO) {
                dispararHaciaObjetivo(volumen);
                super.tiempoDisparo = 0;
            }
        } else {
            super.estaMoviendose = true;
            super.rangoVision = 250;
            super.patrullar(delta, nivel);
        }

        Iterator<Proyectil> it = super.balas.iterator();
        while (it.hasNext()) {
            Proyectil b = it.next();
            b.mover(delta, nivel, this);
            if (!b.isActivo()) it.remove();
        }
    }

    private void dispararHaciaObjetivo(float volumen) {
        if (super.objetivoActual == null) return;
        if (super.objetivoActual.getVida() <= 0) {
            super.objetivoActual = null;
            return;
        }
        boolean objetivoALaDerecha = super.objetivoActual.getX() > super.getX();
        super.moviendoDerecha = objetivoALaDerecha;

        String ruta = objetivoALaDerecha ?
                "imagenes/personajes/enemigo/ataque/Bala_Derecha.png" :
                "imagenes/personajes/enemigo/ataque/Bala_Izquierda.png";

        disparar(ruta, volumen);
    }
    
    private void disparar(String ruta, float volumen) {
        super.balas.add(new Proyectil(getX(), getY() + 16, super.moviendoDerecha, ruta));
        EfectoSonido.reproducir(super.nombreAtaque, volumen);
    }

}
