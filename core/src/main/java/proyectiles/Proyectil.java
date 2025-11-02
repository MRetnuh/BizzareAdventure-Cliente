package proyectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import juego.Partida;
import niveles.NivelBase;
import personajes.Personaje;

public class Proyectil extends Actor {
    private float velocidad;
    private boolean haciaDerecha;
    private Texture textura;
    private boolean activa = true;
    private final int WIDTH = 16;
    private final int HEIGHT = 16;

    public Proyectil(float inicialX, float inicialY, boolean haciaDerecha, String ruta) {
        super.setX(inicialX);
        super.setY(inicialY);

        this.setWidth(this.WIDTH);
        this.setHeight(this.HEIGHT);

        this.haciaDerecha = haciaDerecha;
        this.velocidad = 400;
        this.textura = new Texture(Gdx.files.internal(ruta));
    }

    @Override
    public void act(float delta) {
        if (!this.activa) return;

        float movimiento = this.velocidad * delta;
        if (this.haciaDerecha) {
            super.setX(getX() + movimiento);
        } else {
            super.setX(getX() - movimiento);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.activa) {
            batch.draw(this.textura, super.getX(), super.getY(), super.getWidth(), super.getHeight());
        }
    }

    public Rectangle getHitbox() {
        return new Rectangle(super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    public void desactivar() {
        this.activa = false;
        this.remove();
    }

    public void mover(float delta, NivelBase nivel, Personaje personaje) {
        if (!activa) return;

        act(delta); 
        if (nivel != null && nivel.detectarColision(getHitbox()) || getX() >= personaje.getX() + 600 || getX() <= personaje.getX() - 600) {
            desactivar();
        }
    }

    public boolean isActivo() {
        return activa;
    }
}