//Importación de clases y recursos
package com.paloma.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paloma.util.Recursos;
//Atributos, se usa la clase entidad para decirle a la bala que herede sus elementos, uno de ellos es su posicion (x, y)
public class Bala extends Entidad {

    private static TextureRegion imagenPorDefecto;

    private final TextureRegion imagen;
    private final int velocidad;
    private boolean visible = false;
    //Constructores, inicializa una nueva instacia de bullet dando velocidad, tamaño y posición
    public Bala() {
        this(null, 10);
    }

    public Bala(TextureRegion imagen, int velocidad) {
        super(0, 0, 0, 0);
        this.velocidad = velocidad;

        if (imagen != null) {
            this.imagen = imagen;
        } else {
            if (imagenPorDefecto == null) {
                imagenPorDefecto = Recursos.colorSolido(new Color(128 / 255f, 0f, 128 / 255f, 1f), 6, 16);
            }
            this.imagen = imagenPorDefecto;
        }
        this.ancho = this.imagen.getRegionWidth();
        this.alto = this.imagen.getRegionHeight();
    }
    //activacion y movimiento de bullet administrando sus fisicas y logicas
    public void disparar(float x, float y) {
        visible = true;
        this.x = x;
        this.y = y;
    }

    @Override
    public void actualizar() {
        if (!visible) {
            return;
        }
        y -= velocidad;
        if (y + alto < 0) {
            visible = false;
        }
    }
    //Dibujado de la balla, detecta cuando sale de la pantalla y "desaparece"
    @Override
    public void dibujar(SpriteBatch pantalla) {
        if (visible) {
            pantalla.draw(imagen, (int) x, (int) y, ancho, alto);
        }
    }

    public boolean estaVisible() {
        return visible;
    }
}
