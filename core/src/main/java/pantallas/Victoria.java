package pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import estilos.EstiloTexto;
import red.HiloCliente;

public class Victoria implements Screen {

    private final Game game;
    private Stage stage;
    private Image imagen;

    private Label texto1;  // cargo
    private Label texto2;  // nombre

    private HiloCliente hiloCliente;
    private int indice = 1;

    public Victoria(Game game, HiloCliente hiloCliente) {
        this.game = game;
        this.hiloCliente = hiloCliente;
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        imagen = new Image(getDrawable(indice));
        imagen.setFillParent(true);
        imagen.getColor().a = 0;

        texto1 = new Label("", EstiloTexto.ponerEstiloLabel(60, Color.WHITE));
        texto1.getColor().a = 0;

        texto2 = new Label("", EstiloTexto.ponerEstiloLabel(60, Color.WHITE));
        texto2.getColor().a = 0;

        colocarTextos();

        stage.addActor(imagen);
        stage.addActor(texto1);
        stage.addActor(texto2);

        mostrarAnimacion();
    }

    private TextureRegionDrawable getDrawable(int num){
        return new TextureRegionDrawable(
                new Texture(Gdx.files.internal("imagenes/fondos/creditos_" + num + ".png"))
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /** Fade In → Espera → Fade Out */
    private void mostrarAnimacion(){

        imagen.addAction(Actions.sequence(
                Actions.fadeIn(3f),
                Actions.delay(3f),
                Actions.fadeOut(3f),
                Actions.run(this::siguienteImagen)
        ));

        texto1.addAction(Actions.sequence(
                Actions.fadeIn(1f),
                Actions.delay(3f),
                Actions.fadeOut(5f)
        ));

        texto2.addAction(Actions.sequence(
                Actions.fadeIn(1f),
                Actions.delay(3f),
                Actions.fadeOut(5f)
        ));
    }

    private void siguienteImagen(){
        indice++;

        if (indice > 5){
            return;
        }

        imagen.setDrawable(getDrawable(indice));
        colocarTextos();
        mostrarAnimacion();
    }

    private void colocarTextos(){

        texto1.setText("");
        texto2.setText("");

        float ancho = Gdx.graphics.getWidth();
        float alto  = Gdx.graphics.getHeight();

        switch(indice){

            // ------------------------------------------------------------------------
            case 1:
                texto1.setText("Programador y desarrollador de sonido:");
                texto2.setText("Eduardo Orsi");

                texto1.setAlignment(Align.center);
                texto2.setAlignment(Align.center);

                texto1.setPosition(ancho * 0.50f, alto * 0.57f, Align.center);
                texto2.setPosition(ancho * 0.50f, alto * 0.50f, Align.center);
                break;

            // ------------------------------------------------------------------------
            case 2:
                texto1.setText("Desarrollador de personajes:");
                texto2.setText("Eynar Mejia");

                texto1.setAlignment(Align.left);
                texto2.setAlignment(Align.left);

                texto1.setPosition(ancho * 0.0000001f, alto * 0.82f, Align.center);
                texto2.setPosition(ancho * 0.0000001f, alto * 0.75f, Align.center);

                break;

            // ------------------------------------------------------------------------
            case 3:
                texto1.setText("Co-Programador:");
                texto2.setText("Kevin De Groote");

                texto1.setAlignment(Align.center);
                texto2.setAlignment(Align.center);

                texto1.setPosition(ancho * 0.50f, alto * 0.57f, Align.center);
                texto2.setPosition(ancho * 0.50f, alto * 0.50f, Align.center);
                break;

            // ------------------------------------------------------------------------
            case 4:
                texto1.setText("Creador de la portada:");
                texto2.setText("Juan Benito Suarez Dominguez (Lokevas)");

                texto1.setAlignment(Align.center);
                texto2.setAlignment(Align.center);

                texto1.setPosition(ancho * 0.50f, alto * 0.57f, Align.center);
                texto2.setPosition(ancho * 0.50f, alto * 0.50f, Align.center);
                break;

            // ------------------------------------------------------------------------
            case 5:
                texto1.setText("Apoyo Emocional:");
                texto2.setText("Bang (usuario de Discord)");

                texto1.setAlignment(Align.center);
                texto2.setAlignment(Align.center);

                texto1.setPosition(ancho * 0.50f, alto * 0.57f, Align.center);
                texto2.setPosition(ancho * 0.50f, alto * 0.50f, Align.center);
                break;
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
