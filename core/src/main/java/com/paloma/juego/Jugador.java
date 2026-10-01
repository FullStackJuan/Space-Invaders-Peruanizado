//Importación de recursos necesarios
package com.paloma.juego;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.paloma.Constantes;
import com.paloma.util.EfectoSonido;
import com.paloma.util.Recursos;
//declaracion de la clase jugador y configuracion de su funcionamiento
public class Jugador extends Entidad {

    private static final Color COLOR = new Color(1f, 0f, 0f, 1f);

    private final int inicialX;
    private final int inicialY;
    private final int anchoPantalla;
    private final int altoPantalla;
    private final int velocidad;
    private final int tamano;
    private final EfectoSonido sonidoDisparo;

    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean moverArriba;
    private boolean moverAbajo;

    private TextureRegion imagen;
    //Metodo Constructor, provee tamaño, posicion, velocidad, color y dimensiones de la pantalla para
    //Determinar su rango de movimiento
    public Jugador(EfectoSonido sonidoDisparo) {
        this(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA, 400, 300, 5, 80, 60, sonidoDisparo);
    }

    public Jugador(int anchoPantalla, int altoPantalla, int x, int y, int velocidad,
                   int ancho, int alto, EfectoSonido sonidoDisparo) {
        super(x, y, ancho, alto);
        this.inicialX = x;
        this.inicialY = y;
        this.anchoPantalla = anchoPantalla;
        this.altoPantalla = altoPantalla;
        this.velocidad = velocidad;
        this.tamano = ancho;
        this.sonidoDisparo = sonidoDisparo;

        try {
            imagen = Recursos.cargarImagen("image/Paloma_meme_1.png");
        } catch (GdxRuntimeException e) {
            System.out.println("No se pudo cargar la imagen del jugador: " + e.getMessage());
            imagen = null;
        }
    }
    //Detecta las entradas del usuario para darle movimiento en pantalla en funcion a la tecla presionada
    public void manejarMovimiento(int tecla, boolean presionada) {
        switch (tecla) {
            case Keys.A -> moverIzquierda = presionada;
            case Keys.D -> moverDerecha = presionada;
            case Keys.W -> moverArriba = presionada;
            case Keys.S -> moverAbajo = presionada;
            default -> { }
        }
    }
    //Toma la detección de la tecla y va cambiando su posición en las coordenadas
    //Así como tambien detecta si el jugador ya se encuentra en los limites de la pantalla y impide el movimiento
    //Fuera de los limites
    @Override
    public void actualizar() {
        if (moverIzquierda) x -= velocidad;
        if (moverDerecha) x += velocidad;
        if (moverArriba) y -= velocidad;
        if (moverAbajo) y += velocidad;

        if (x < 0) {
            x = 0;
        } else if (x > anchoPantalla - ancho) {
            x = anchoPantalla - ancho;
        }

        if (y < 0) {
            y = 0;
        } else if (y > altoPantalla - alto) {
            y = altoPantalla - alto;
        }
    }
    //Logica de dibujo, disparo y reinicio del jugador
    @Override
    public void dibujar(SpriteBatch pantalla) {
        if (imagen != null) {
            pantalla.draw(imagen, x, y, ancho, alto);
        } else {
            pantalla.setColor(COLOR);
            pantalla.draw(Recursos.pixel(), x, y, ancho, alto);
            pantalla.setColor(Color.WHITE);
        }
    }

    public void disparar() {
        if (sonidoDisparo != null) {
            sonidoDisparo.reproducir();
        }
    }

    public void reiniciarPosicion() {
        x = inicialX;
        y = inicialY;
        moverIzquierda = false;
        moverDerecha = false;
        moverArriba = false;
        moverAbajo = false;
    }

    // Igual que en main.py: el hitbox del jugador usa tamano x tamano.
    @Override
    public Rectangle getRectangulo() {
        return new Rectangle((int) x, (int) y, tamano, tamano);
    }

    public int getTamano() {
        return tamano;
    }
}
