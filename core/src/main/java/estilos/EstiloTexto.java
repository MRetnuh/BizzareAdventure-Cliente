package estilos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class EstiloTexto {


    private static BitmapFont generarFuente(int tamaño) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/fuentePixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = tamaño;
        BitmapFont fuente = generator.generateFont(parameter);
        generator.dispose();
        return fuente;
    }

    public static Label.LabelStyle ponerEstiloLabel(int tamaño, Color color) {
        BitmapFont fuente = generarFuente(tamaño);
        return new Label.LabelStyle(fuente, color);
    }

    public static TextButton.TextButtonStyle ponerEstiloBoton(Skin skin, int tamaño, Color color) {
        BitmapFont fuente = generarFuente(tamaño);
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle();
        estilo.fontColor = color;
        estilo.overFontColor = Color.CYAN;
        estilo.font = fuente;
        return estilo;
    }
}
