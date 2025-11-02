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

public class Opciones implements Screen {
	private Screen screenAnterior;
    private final Game JUEGO;
    private Stage stage;
    private Skin skin;
    private Musica musicaOpciones;
    
    public Opciones(Game juego, Screen screenAnterior, Musica musica) {
        this.JUEGO = juego;
        this.musicaOpciones = musica;
        this.screenAnterior = screenAnterior;
        this.stage = new Stage();
    }
    
    @Override
    public void show() {
    	
        Gdx.input.setInputProcessor(this.stage);

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label titulo = new Label("Opciones", EstiloTexto.ponerEstiloLabel(60, Color.WHITE));
        titulo.setAlignment(Align.center);

        TextButton controlesBtn = new TextButton("Controles", EstiloTexto.ponerEstiloBoton(skin, 48, Color.PURPLE));
        TextButton sonidoBtn = new TextButton("Sonido", EstiloTexto.ponerEstiloBoton(skin, 48, Color.PURPLE));
        TextButton volverBtn = new TextButton("Volver", EstiloTexto.ponerEstiloBoton(skin,48, Color.PURPLE));
        
        sonidoBtn.addListener(new ListenerBotonTexto("Sonido", new Runnable() {
            @Override
            public void run() {
                JUEGO.setScreen(new ConfiguracionVolumen(JUEGO, screenAnterior, musicaOpciones));
            }
        }));
        
        controlesBtn.addListener(new ListenerBotonTexto("Controles", new Runnable() {
            @Override
            public void run() {
            	  JUEGO.setScreen(new Controles(JUEGO, screenAnterior, musicaOpciones));
            }
        }));
        
     
        volverBtn.addListener(new ListenerBotonTexto("Volver", new Runnable() {
            @Override
            public void run() {
            	  JUEGO.setScreen(screenAnterior);
            }
        }));
        
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();
        tabla.defaults().center();
        tabla.add(titulo).padBottom(30).row();
        tabla.add(sonidoBtn).padBottom(20).row();
        tabla.add(controlesBtn).padBottom(20).row();
        tabla.add(volverBtn);

        this.stage.addActor(tabla);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}


