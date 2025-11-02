package pantallas;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import audios.Musica;
import estilos.EstiloTexto;
import estilos.ListenerBotonTexto;
import io.github.some.Principal;

public class ConfiguracionVolumen implements Screen {
	private Screen screenAnterior;
    private final Game JUEGO;
    private Stage stage;
    private Skin skin;
    private Musica musicaConfig;
    
    public ConfiguracionVolumen(Game juego, Screen screenAnterior, Musica musica) {
    	this.screenAnterior = screenAnterior;
        this.JUEGO = juego;
        this.musicaConfig = musica;
        this.stage = new Stage();
    }

    @Override
    public void show() {
    	
        Gdx.input.setInputProcessor(this.stage);

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label titulo = new Label("Sonido", EstiloTexto.ponerEstiloLabel(60, Color.WHITE));
        titulo.setAlignment(Align.center);

        Label volumenLabel = new Label("Volumen: " + (int)(this.musicaConfig.getVolumen() * 100) + "%", EstiloTexto.ponerEstiloLabel(36, Color.WHITE));

        Slider volumenSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumenSlider.setValue(this.musicaConfig.getVolumen());
        volumenSlider.addListener(event -> {
            float nuevoVolumen = volumenSlider.getValue();
            this.musicaConfig.setVolumen(nuevoVolumen);
            volumenLabel.setText("Volumen: " + (int)(nuevoVolumen * 100) + "%");
            return false;
        });

        TextButton volverBtn = new TextButton("Volver", EstiloTexto.ponerEstiloBoton(this.skin, 48, Color.RED));
        volverBtn.addListener(new ListenerBotonTexto("Volver", new Runnable() {
            @Override
            public void run() {
            	JUEGO.setScreen(new Opciones(JUEGO, screenAnterior, musicaConfig));
            }
        }));
        
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        tabla.add(titulo).padBottom(30).row();
        tabla.add(volumenLabel).padBottom(10).row();
        tabla.add(volumenSlider).width(300).padBottom(30).row();
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
