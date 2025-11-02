package audios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class EfectoSonido {
    private static Sound efecto;
    
    public static void reproducir(String nombreArchivo, float volumen) {
    	efecto = Gdx.audio.newSound(Gdx.files.internal("sonidos/" + nombreArchivo + ".mp3"));
    	efecto.play(volumen);
    }

    public void liberar() {
        efecto.dispose();
    }
}
