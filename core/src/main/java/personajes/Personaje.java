package personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import audios.EfectoSonido;
import niveles.NivelBase;
import proyectiles.Proyectil;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Personaje extends Actor {
    protected float velocidad;
    private String nombre;
    private int vida;
    private Texture texturaDerrota;
    private Image imagenDerrota;
    private Stage stage;
    private boolean estaAtacando = false;
    private boolean estaSaltando = false;
    private boolean moviendoIzquierda = false;
    private float prevX, prevY;
    private float estadoTiempo = 0f;
    private float velocidadCaida = 0;
    private float tiempoAtaque = 0f;
    private TipoAtaque tipoAtaque;
    private boolean disparoRealizado = false;
    private NivelBase nivel;

    protected TextureRegion frameMuerte;
    protected String nombreAtaque;
    protected TextureRegion frame;
    protected Animation<TextureRegion> animDerecha;
    protected Animation<TextureRegion> animIzquierda;
    protected Animation<TextureRegion> animAtaqueDerecha;
    protected Animation<TextureRegion> animAtaqueIzquierda;
    protected TextureRegion quietaDerecha;
    protected TextureRegion quietaIzquierda;
    protected boolean mirandoDerecha = true;
    protected boolean estaMoviendose = false;
    protected boolean moviendoDerecha = false;
    protected ArrayList<Proyectil> balas = new ArrayList<>();
    protected float tiempoDisparo = 0f;
    protected final float COOLDOWNDISPARO = 1.0f;

    public Personaje(String nombre, int velocidad, String nombreAtaque, int vida, TipoAtaque tipoAtaque) {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.nombreAtaque = nombreAtaque;
        this.vida = vida;
        this.cargarTexturas();
        // Aseguramos que haya versiones "izquierda" si no fueron creadas explícitamente
        asegurarAnimaciones();
        this.tipoAtaque = tipoAtaque;
        super.setX(200);
        super.setY(930);
        if (this.quietaDerecha != null) {
            setSize(this.quietaDerecha.getRegionWidth(), this.quietaDerecha.getRegionHeight());
        }
    }

    protected abstract void cargarTexturas();

    /**
     * Si no se cargaron las animaciones/frames de la izquierda, las creamos
     * girando (flip) las regiones de la derecha. Esto evita que la izquierda
     * no se muestre por falta de recursos.
     */
    private void asegurarAnimaciones() {
        // quieta
        if (quietaDerecha != null && quietaIzquierda == null) {
            TextureRegion copy = new TextureRegion(quietaDerecha);
            copy.flip(true, false);
            quietaIzquierda = copy;
        }

        // animaciones de movimiento
        if (animDerecha != null && animIzquierda == null) {
            ArrayList<TextureRegion> frames = new ArrayList<>();
            for (TextureRegion tr : animDerecha.getKeyFrames()) {
                TextureRegion r = new TextureRegion(tr);
                r.flip(true, false);
                frames.add(r);
            }
            animIzquierda = new Animation<>(animDerecha.getFrameDuration(), frames.toArray(new TextureRegion[0]));
            animIzquierda.setPlayMode(animDerecha.getPlayMode());
        }

        // animacion ataque
        if (animAtaqueDerecha != null && animAtaqueIzquierda == null) {
            ArrayList<TextureRegion> frames = new ArrayList<>();
            for (TextureRegion tr : animAtaqueDerecha.getKeyFrames()) {
                TextureRegion r = new TextureRegion(tr);
                r.flip(true, false);
                frames.add(r);
            }
            animAtaqueIzquierda = new Animation<>(animAtaqueDerecha.getFrameDuration(), frames.toArray(new TextureRegion[0]));
            animAtaqueIzquierda.setPlayMode(animAtaqueDerecha.getPlayMode());
        }

        // frame muerte
        if (frameMuerte != null) {
            // opcional: si no tenés versión izquierda y querés usar la misma (no estrictamente necesario)
        }
    }

    public void cargarUbicaciones(float x, float y) {
        super.setX(x);
        super.setY(y);
    }

    public void morir(Stage stage) {
        this.stage = stage;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose();

        Image fondoNegro = new Image(blackTexture);
        fondoNegro.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fondoNegro.getColor().a = 0;
        this.stage.addActor(fondoNegro);

        fondoNegro.addAction(Actions.fadeIn(0.5f));

        this.texturaDerrota = new Texture(Gdx.files.internal("imagenes/fondos/GameOver.png"));
        this.imagenDerrota = new Image(this.texturaDerrota);
        this.imagenDerrota.setSize(200, 50);
        this.imagenDerrota.setOrigin(this.imagenDerrota.getWidth() / 2f, this.imagenDerrota.getHeight() / 2f);
        this.imagenDerrota.setPosition(
                (Gdx.graphics.getWidth() - this.imagenDerrota.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - this.imagenDerrota.getHeight()) / 2f
        );
        this.imagenDerrota.setScale(0.1f);
        this.imagenDerrota.getColor().a = 0;

        this.stage.addActor(this.imagenDerrota);

        this.imagenDerrota.addAction(Actions.sequence(
                Actions.delay(0.3f),
                Actions.parallel(
                        Actions.fadeIn(0.5f),
                        Actions.scaleTo(2.5f, 2.5f, 2f, Interpolation.pow2Out)
                )
        ));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        }, 8);
    }

    // ---------------------------
    // ANIMACIONES CORREGIDAS 👇
    // ---------------------------
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.vida <= 0) {
            if (frameMuerte != null) batch.draw(frameMuerte, getX(), getY());
            return;
        }

        // Selección del frame según estado
        if (this.estaAtacando) {
            frame = mirandoDerecha
                    ? animAtaqueDerecha.getKeyFrame(tiempoAtaque, false)
                    : animAtaqueIzquierda.getKeyFrame(tiempoAtaque, false);
        } else if (this.estaMoviendose) {
            frame = mirandoDerecha
                    ? animDerecha.getKeyFrame(estadoTiempo, true)
                    : animIzquierda.getKeyFrame(estadoTiempo, true);
        } else {
            estadoTiempo = 0f; // Reinicia tiempo cuando no se mueve
            frame = mirandoDerecha ? quietaDerecha : quietaIzquierda;
        }

        if (frame != null)
            batch.draw(frame, getX(), getY());

        // Dibujar proyectiles
        for (Proyectil p : balas) {
            if (p.isActivo())
                p.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        if (this.vida <= 0) return;

        super.act(delta);

        // Actualizar proyectiles
        Iterator<Proyectil> it = balas.iterator();
        while (it.hasNext()) {
            Proyectil p = it.next();
            p.mover(delta, nivel, this);
            if (!p.isActivo()) it.remove();
        }

        // Manejo de animaciones de ataque
        if (this.estaAtacando) {
            this.tiempoAtaque += delta;
            boolean finAtaque = (mirandoDerecha && animAtaqueDerecha.isAnimationFinished(tiempoAtaque))
                    || (!mirandoDerecha && animAtaqueIzquierda.isAnimationFinished(tiempoAtaque));
            if (finAtaque) {
                this.estaAtacando = false;
                this.tiempoAtaque = 0f;
            }
        } else if (this.estaMoviendose) {
            this.estadoTiempo += delta;
        } else {
            this.estadoTiempo = 0f;
        }
    }

    // ---------------------------
    // RESTO DEL CÓDIGO IGUAL 👇
    // ---------------------------

    public void atacar(float delta) {
        if (this.tipoAtaque == TipoAtaque.MELEE) {
            if (this.estaAtacando) {
                this.tiempoAtaque += delta;
                if (this.tiempoAtaque >= this.animAtaqueDerecha.getAnimationDuration()) {
                    this.estaAtacando = false;
                    this.tiempoAtaque = 0f;
                }
            }
        } else {
            if (this.estaAtacando) {
                this.tiempoAtaque += delta;
                if ((this.mirandoDerecha && this.animAtaqueDerecha.isAnimationFinished(this.tiempoAtaque)) ||
                        (!this.mirandoDerecha && this.animAtaqueIzquierda.isAnimationFinished(this.tiempoAtaque))) {
                    this.estaAtacando = false;
                    this.tiempoAtaque = 0f;
                    disparoRealizado = false;
                }
            }
        }
    }

    public void iniciarAtaque(float volumen, float delta, NivelBase nivel) {
        this.nivel = nivel;
        if (!this.estaAtacando) {
            this.estaAtacando = true;
            this.tiempoAtaque = 0f;

            if (this.tipoAtaque == TipoAtaque.DISTANCIA) {
                this.tiempoAtaque += 0.5;
                String ruta = mirandoDerecha
                        ? "imagenes/personajes/enemigo/ataque/Bala_Derecha.png"
                        : "imagenes/personajes/enemigo/ataque/Bala_Izquierda.png";

                Proyectil nuevaBala = new Proyectil(getX(), getY() + 16, this.mirandoDerecha, ruta, true);
                this.balas.add(nuevaBala);
            }

            EfectoSonido.reproducir(this.nombreAtaque, volumen);
            Iterator<Proyectil> it = this.balas.iterator();
            while (it.hasNext()) {
                Proyectil b = it.next();
                b.mover(delta, nivel, this);
                if (!b.isActivo()) it.remove();
            }
        }
    }
    
    public void aplicarMovimiento(float nuevoX, float nuevoY, float delta, int mapWidth, int mapHeight) {
        this.estaMoviendose = nuevoX != super.getX() || nuevoY != super.getY();
        this.mirandoDerecha = nuevoX > super.getX() || nuevoX == super.getX() && this.mirandoDerecha;

        float anchoSprite = getWidth();
        float altoSprite = getHeight();

        nuevoX = Math.max(0, Math.min(nuevoX, mapWidth - anchoSprite));

        nuevoY = Math.min(nuevoY, mapHeight - altoSprite);

        setX(nuevoX);
        setY(nuevoY);
    }

    public void guardarPosicionAnterior() {
        this.prevX = super.getX();
        this.prevY = super.getY();
    }

    public float getVelocidadCaida() {
        return velocidadCaida;
    }

    public void setVelocidadCaida(float v) {
        this.velocidadCaida = v;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void reducirVida() {
        this.vida--;
        if (this.vida <= 0) {
            this.vida = 0;
            detenerMovimiento();
        }
    }

    public void aumentarVida() {
        this.vida++;
    }

    public void detenerMovimiento() {
        setMoviendoDerecha(false);
        setMoviendoIzquierda(false);
        setEstaSaltando(false);
        setEstaAtacando(false);
    }

    public boolean getEstaAtacando() {
        return this.estaAtacando;
    }

    public void setEstaAtacando(boolean atacando) {
        this.estaAtacando = atacando;
    }

    public void setMoviendoDerecha(boolean moviendo) {
        this.moviendoDerecha = moviendo;
    }
    
    public void setMirandoDerecha(boolean derecha) {
    	this.mirandoDerecha = derecha;
    }
    
    public boolean getMirandoDerecha() {
    	return this.mirandoDerecha;
    }

    public void setMoviendoIzquierda(boolean moviendo) {
        this.moviendoIzquierda = moviendo;
    }

    public void setEstaSaltando(boolean moviendo) {
        this.estaSaltando = moviendo;
    }

    public void setEstaMoviendose(boolean valor) {
        this.estaMoviendose = valor;
    }

    public TipoAtaque getTipoAtaque() {
        return this.tipoAtaque;
    }

    public ArrayList<Proyectil> getBalas() {
        return this.balas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getVida() {
        return this.vida;
    }

    public Rectangle getHitbox() {
        return new Rectangle(super.getX(), super.getY(), 32, 32);
    }

    public float getNuevaX(float delta) {
        float tempX = getX();
        if (this.moviendoDerecha) tempX += this.velocidad * delta;
        if (this.moviendoIzquierda) tempX -= this.velocidad * delta;
        return tempX;
    }

    public float getNuevaY(float delta) {
        float tempY = super.getY();
        if (this.estaSaltando) tempY += this.velocidad * delta;
        return tempY;
    }

    public float getPrevY() {
        return this.prevY;
    }

    public void setY(float prevY) {
        super.setY(prevY);
    }

    public boolean getEstaSaltando() {
        return this.estaSaltando;
    }

    public void setPosicion(float x, float y) {
        super.setX(x);
        super.setY(y);
    }
}
