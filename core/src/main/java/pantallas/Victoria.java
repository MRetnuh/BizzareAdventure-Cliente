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

    private Label texto1;  
    private Label texto2;  

    private HiloCliente hiloCliente;
    private int indice = 0;  // AHORA EMPIEZA EN 0 (intro)

    public Victoria(Game game, HiloCliente hiloCliente) {
        this.game = game;
        this.hiloCliente = hiloCliente;
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        imagen = new Image();
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

    private void mostrarAnimacion(){

        // INTRO (0) y OUTRO (6 y 7): solo texto
        if (indice == 0 || indice == 6 || indice == 7) {

            texto1.addAction(Actions.sequence(
                    Actions.fadeIn(3f),
                    Actions.delay(3f),
                    Actions.fadeOut(3f)
            ));

            texto2.addAction(Actions.sequence(
                    Actions.fadeIn(3f),
                    Actions.delay(3f),
                    Actions.fadeOut(3f),
                    Actions.run(this::siguiente)
            ));
            return;
        }

        // IMÁGENES (1–5)
        imagen.addAction(Actions.sequence(
                Actions.fadeIn(3f),
                Actions.delay(3f),
                Actions.fadeOut(3f),
                Actions.run(this::siguiente)
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

    private void siguiente(){
        indice++;

        // OUTRO
        if (indice == 6){
            texto1.setText("Gracias por jugar");
            texto2.setText("");
            centrar();
            mostrarAnimacion();
            return;
        }

        if (indice == 7){
            texto1.setText("Profesor, por favor apruebenos");
            texto2.setText("");
            centrar();
            mostrarAnimacion();
            return;
        }

        // FIN TOTAL CLIENTE (queda pantalla negra)
        if (indice > 7){
            return;
        }

        // INTRO O IMÁGENES
        if (indice >= 1 && indice <= 5) {
            imagen.setDrawable(getDrawable(indice));
        }

        colocarTextos();
        mostrarAnimacion();
    }

    private void centrar() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        texto1.setAlignment(Align.center);
        texto2.setAlignment(Align.center);

        texto1.setPosition(w * 0.5f, h * 0.52f, Align.center);
        texto2.setPosition(w * 0.5f, h * 0.45f, Align.center);
    }

    private void colocarTextos(){

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        texto1.setText("");
        texto2.setText("");

        switch(indice){

            case 0: // INTRO --------------------------
                texto1.setText("Un juego hecho por");
                texto2.setText("3 pibes de la 35");
                centrar();
                break;

            case 1:
                texto1.setText("Programador y desarrollador de sonido:");
                texto2.setText("Eduardo Orsi");
                centrar();
                break;

            case 2:
                texto1.setText("Desarrollador de personajes:");
                texto2.setText("Eynar Mejia");

                texto1.setAlignment(Align.left);
                texto2.setAlignment(Align.left);

                texto1.setPosition(w * 0.0000001f, h * 0.82f, Align.center);
                texto2.setPosition(w * 0.0000001f, h * 0.75f, Align.center);
                break;

            case 3:
                texto1.setText("Co-Programador:");
                texto2.setText("Kevin De Groote");
                centrar();
                break;

            case 4:
                texto1.setText("Creador de la portada:");
                texto2.setText("Juan Benito Suarez Dominguez (Lokevas)");
                centrar();
                break;

            case 5:
                texto1.setText("Apoyo Emocional:");
                texto2.setText("Bang (usuario de Discord)");
                centrar();
                break;
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
