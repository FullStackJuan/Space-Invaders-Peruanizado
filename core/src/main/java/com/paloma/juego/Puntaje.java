//Importa los recursos necesitados por la clase
package com.paloma.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paloma.util.EfectoSonido;
import com.paloma.util.Recursos;
//Llama a dibujable para tener una posicion, color y tamaño en pantalla
public class Puntaje implements Dibujable {

    private static final Color BLANCO = new Color(1f, 1f, 1f, 1f);

    private int puntos = 0;
    private final BitmapFont fuente;
    private final EfectoSonido sonidoPuntaje;
    private String texto;
    //Declaración de puntaje, crea una fuente, toma un sonido y escribe su valor
    public Puntaje() {
        fuente = Recursos.crearFuente(20);
        sonidoPuntaje = new EfectoSonido("sounds/sound-of-collision.wav");
        escribir();
    }
    //Suma los puntos del jugador
    public void sumar() {
        puntos += 1;
        sonidoPuntaje.reproducir();
        escribir();
    }
    //Reinicio de puntaje
    public void reiniciarPuntaje() {
        puntos = 0;
        escribir();
    }
    //Escribe el puntaje en pantalla dando tamaño y color
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
