package pantallas;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import audios.Musica;
import estilos.EstiloTexto;
import estilos.ListenerBotonTexto;
import io.github.some.Principal;

public class Controles implements Screen {
	private Screen screenAnterior;
    private final Game JUEGO;
    private Stage stage;
    private Skin skin;
    private Musica musicaControles;

    public Controles(Game juego,  Screen screenAnterior, Musica musica) {
        this.JUEGO = juego;
        this.musicaControles = musica;
        this.screenAnterior = screenAnterior;
    }

    @Override
    public void show() {
    	this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label titulo = new Label("CONTROLES", EstiloTexto.ponerEstiloLabel(70, Color.WHITE));
        titulo.setAlignment(Align.center);

        Label movimientoTitulo = new Label("Movimiento:", EstiloTexto.ponerEstiloLabel(60, Color.ORANGE));
        Label combateTitulo = new Label("Combate:", EstiloTexto.ponerEstiloLabel(60, Color.ORANGE));
        Label opcionesTitulo = new Label("Opciones:", EstiloTexto.ponerEstiloLabel(60, Color.ORANGE));

        Label w = new Label("W: Saltar", EstiloTexto.ponerEstiloLabel(48, Color.LIGHT_GRAY));
        Label a = new Label("A: Moverse a la izquierda", EstiloTexto.ponerEstiloLabel(48, Color.LIGHT_GRAY));
        Label d = new Label("D: Moverse a la derecha", EstiloTexto.ponerEstiloLabel(48, Color.LIGHT_GRAY));

        Label m = new Label("M: Atacar", EstiloTexto.ponerEstiloLabel(48, Color.LIGHT_GRAY));

        Label p = new Label("P: Pausar / Opciones", EstiloTexto.ponerEstiloLabel(48, Color.LIGHT_GRAY));

        TextButton volverBtn = new TextButton("Volver", EstiloTexto.ponerEstiloBoton(skin, 48, Color.RED));
        
        volverBtn.addListener(new ListenerBotonTexto("Volver", new Runnable() {
            @Override
            public void run() {
            	JUEGO.setScreen(new Opciones(JUEGO, screenAnterior, musicaControles));
            }
        }));

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.top().padTop(30);

        tabla.add(titulo).colspan(1).padBottom(40).row();

        tabla.add(movimientoTitulo).left().padBottom(10).row();
        tabla.add(w).left().padBottom(5).row();
        tabla.add(a).left().padBottom(5).row();
        tabla.add(d).left().padBottom(5).row();

        tabla.add(combateTitulo).left().padBottom(10).row();
        tabla.add(m).left().padBottom(25).row();

        tabla.add(opcionesTitulo).left().padBottom(10).row();
        tabla.add(p).left().padBottom(40).row();

        tabla.add(volverBtn).center();

        this.stage.addActor(tabla);
        this.stage.addActor(tabla);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.draw();
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
    	this.stage.dispose();
    	this.skin.dispose();
    
}}