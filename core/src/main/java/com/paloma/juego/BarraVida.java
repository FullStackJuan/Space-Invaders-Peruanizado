package com.paloma.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.paloma.Constantes;
import com.paloma.util.Recursos;

public class BarraVida implements Dibujable {

    private final int vidaMaxima;
    private int vidaActual;
    private final int tamano;
    private final int espaciado;
    private final int xInicio;
    private final int yInicio;

    private TextureRegion imagenCorazon;
    private final Color color = new Color(0f, 1f, 0f, 1f);
    private final Color colorVacio = new Color(50 / 255f, 50 / 255f, 50 / 255f, 1f);

    public BarraVida() {
        this(3, 20, 10);
    }

    public BarraVida(int vidaMaxima, int tamano, int espaciado) {
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.tamano = tamano;
        this.espaciado = espaciado;
        this.xInicio = Constantes.ANCHO_PANTALLA - 150;
        this.yInicio = 20;

        try {
            imagenCorazon = Recursos.cargarImagen("image/Corazon_healt.png");
        } catch (GdxRuntimeException e) {
            imagenCorazon = null;
        }
    }

    public void perderVida() {
        vidaActual = Math.max(0, vidaActual - 1);
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }

    public void reiniciarVida() {
        vidaActual = vidaMaxima;
    }

    @Override
    public void dibujar(SpriteBatch pantalla) {
        for (int i = 0; i < vidaMaxima; i++) {
            int xPos = xInicio + i * (tamano * 2 + espaciado);
            int x = xPos - tamano;
            int y = yInicio - tamano;

            if (imagenCorazon != null) {
                if (i < vidaActual) {
                    pantalla.draw(imagenCorazon, x, y, tamano * 2, tamano * 2);
                }
            } else {
                pantalla.setColor(i < vidaActual ? color : colorVacio);
                pantalla.draw(Recursos.pixel(), x, y, tamano * 2, tamano * 2);
                pantalla.setColor(Color.WHITE);
            }
        }
    }
}
