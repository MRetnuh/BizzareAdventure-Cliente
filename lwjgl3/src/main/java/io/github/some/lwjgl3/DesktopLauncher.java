package io.github.some.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.some.Principal; // Tu clase principal en core

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // Ejecutar en pantalla completa
        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        config.setTitle("Mi Juego");

        new Lwjgl3Application(new Principal(), config);
    }
}