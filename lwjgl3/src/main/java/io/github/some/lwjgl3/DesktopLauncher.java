package io.github.some.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.some.Principal;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // Fullscreen borderless sin usar 'var'
        Graphics.DisplayMode display = Lwjgl3ApplicationConfiguration.getDisplayMode();
        config.setWindowedMode(display.width, display.height);
        config.setResizable(false);

        config.setTitle("BizzareAdventure (Cliente)");
        config.useVsync(true);

        new Lwjgl3Application(new Principal(), config);
    }
}
