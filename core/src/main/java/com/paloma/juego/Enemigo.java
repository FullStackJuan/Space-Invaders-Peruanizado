package com.paloma.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.paloma.Constantes;
import com.paloma.util.Recursos;

public class Enemigo extends Entidad {

    private static final Color COLOR = new Color(0f, 1f, 0f, 1f);

    private final TextureRegion imagen;
    private final int pasoY = 35;
    private float velocidadX;

    public Enemigo(TextureRegion imagen, float factorVelocidad) {
        super(0, 0, 50, 50);
        this.imagen = imagen;
        this.x = MathUtils.random(0, Constantes.ANCHO_PANTALLA - ancho);
        this.y = MathUtils.random(50, 200);
        float velocidadBase = 2.0f;

        int direccion = MathUtils.randomBoolean() ? -1 : 1;
        this.velocidadX = direccion * velocidadBase * factorVelocidad;
    }

    @Override
    public void actualizar() {
        x += velocidadX;

        if (x <= 0) {
            x = 0;
            velocidadX = Math.abs(velocidadX);
            y += pasoY;
        } else if (x + ancho >= Constantes.ANCHO_PANTALLA) {
            x = Constantes.ANCHO_PANTALLA - ancho;
            velocidadX = -Math.abs(velocidadX);
            y += pasoY;
        }
    }

    public void reiniciarPosicion() {
        x = MathUtils.random(0, Constantes.ANCHO_PANTALLA - ancho);
        y = MathUtils.random(50, 200);
    }

    @Override
    public void dibujar(SpriteBatch pantalla) {
        if (imagen != null) {
            pantalla.draw(imagen, (int) x, (int) y, ancho, alto);
        } else {
            pantalla.setColor(COLOR);
            pantalla.draw(Recursos.pixel(), (int) x, (int) y, ancho, alto);
            pantalla.setColor(Color.WHITE);
        }
    }
}
