package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import niveles.NivelBase;
import personajes.Personaje;
import personajes.TipoAtaque;

public class EnemigoPerseguidor extends EnemigoBase {
	

    public EnemigoPerseguidor(String nombre, float x, float y) {
        super(nombre, 100, "EspadaCorte", 1, TipoEnemigo.PERSEGUIDOR);
        setPosition(x, y);
        super.puntoInicialX = x;
    }

    @Override
    protected void cargarTexturas() {
        Array<TextureRegion> framesDerecha = new Array<>();
        for (int i = 1; i <= 4; i++) {
            framesDerecha.add(new TextureRegion(new Texture(Gdx.files.internal(
                    "imagenes/personajes/leone/leone_derecha_moviendose_" + i + ".png"))));
        }
        super.animDerecha = new Animation<>(0.1f, framesDerecha, Animation.PlayMode.LOOP);

        Array<TextureRegion> framesIzquierda = new Array<>();
        for (int i = 1; i <= 4; i++) {
            framesIzquierda.add(new TextureRegion(new Texture(Gdx.files.internal(
                    "imagenes/personajes/leone/leone_izquierda_moviendose_" + i + ".png"))));
        }
        super.animIzquierda = new Animation<>(0.1f, framesIzquierda, Animation.PlayMode.LOOP);

        super.quietaDerecha = new TextureRegion(new Texture(Gdx.files.internal(
                "imagenes/personajes/leone/leone_derecha_(detenida).png")));
        super.quietaIzquierda = new TextureRegion(new Texture(Gdx.files.internal(
                "imagenes/personajes/leone/leone_izquierda_(detenida).png")));
    }
    @Override
    public void actualizarIA(float delta, Personaje jugador1, Personaje jugador2, float volumen, NivelBase nivel){
        seleccionarObjetivo(jugador1, jugador2);
        if (super.objetivoActual == null) {
        	super.patrullar(delta, nivel);
        	super.velocidad = 100;
        }
        else {
    	super.velocidad = 400;
        perseguir(delta, nivel);
        }
    }

    private void perseguir(float delta, NivelBase nivel) {
        if (super.objetivoActual == null) {
        	return;
        } 
        float direccion = super.objetivoActual.getX() > getX() ? 1 : -1;
        float nuevaX = getX() + direccion * super.velocidad * delta;

        Rectangle hitbox = new Rectangle(nuevaX, getY(), getWidth(), getHeight());

        if (!nivel.detectarColision(hitbox)) {
            aplicarMovimiento(nuevaX, getY(), delta, 10000, 1000);
        }

        super.mirandoDerecha = direccion > 0;
        super.estaMoviendose = true;
    }

}
