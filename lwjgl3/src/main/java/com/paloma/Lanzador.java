package com.paloma;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Lanzador {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(JuegoPaloma.TITULO);
        config.setWindowedMode(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA);
        config.setResizable(false);
        config.setForegroundFPS(JuegoPaloma.FPS);
        config.useVsync(false);
        new Lwjgl3Application(new JuegoPaloma(), config);
    }
}
