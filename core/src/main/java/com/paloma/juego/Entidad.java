//Importación de recursos
package com.paloma.juego;

import com.badlogic.gdx.math.Rectangle;
//Inicio de clase abstracta "Entidad" la cual sera clave para el manejo de distintas clases dentro del proyecto
//Asignando valores de movimiento, velocidad, posicion
public abstract class Entidad implements Dibujable {

    protected float x;
    protected float y;
    protected int ancho;
    protected int alto;

    protected Entidad(float x, float y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public abstract void actualizar();

    public Rectangle getRectangulo() {
        return new Rectangle((int) x, (int) y, ancho, alto);
    }

    public boolean colisionaCon(Entidad otra) {
        return getRectangulo().overlaps(otra.getRectangulo());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
