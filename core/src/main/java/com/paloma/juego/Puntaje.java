package com.paloma.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paloma.util.EfectoSonido;
import com.paloma.util.Recursos;

public class Puntaje implements Dibujable {

    private static final Color BLANCO = new Color(1f, 1f, 1f, 1f);

    private int puntos = 0;
    private final BitmapFont fuente;
    private final EfectoSonido sonidoPuntaje;
    private String texto;

    public Puntaje() {
        fuente = Recursos.crearFuente(20);
        sonidoPuntaje = new EfectoSonido("sounds/sound-of-collision.wav");
        escribir();
    }

    public void sumar() {
        puntos += 1;
        sonidoPuntaje.reproducir();
        escribir();
    }

    public void reiniciarPuntaje() {
        puntos = 0;
        escribir();
    }

    public void escribir() {
        texto = "Score: " + puntos;
    }

    @Override
    public void dibujar(SpriteBatch pantalla) {
        fuente.setColor(BLANCO);
        fuente.draw(pantalla, texto, 20, 20);
    }

    public int getPuntos() {
        return puntos;
    }

    public EfectoSonido getSonidoPuntaje() {
        return sonidoPuntaje;
    }
}
