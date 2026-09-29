package com.paloma;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.paloma.juego.Bala;
import com.paloma.juego.BarraVida;
import com.paloma.juego.Enemigo;
import com.paloma.juego.Jugador;
import com.paloma.juego.Puntaje;
import com.paloma.util.EfectoSonido;
import com.paloma.util.Recursos;
import java.util.ArrayList;
import java.util.List;

public class JuegoPaloma extends ApplicationAdapter {

    public static final String TITULO = "La Venganza de la Paloma";
    public static final int FPS = 60;
    private static final int INTERVALO_APARICION_ENEMIGOS = 2000;

    private static final int ANCHO = Constantes.ANCHO_PANTALLA;
    private static final int ALTO = Constantes.ALTO_PANTALLA;

    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Bala> balas = new ArrayList<>();
    private long ultimaAparicion;

    private OrthographicCamera camara;
    private SpriteBatch pantalla;
    private Puntaje puntaje;
    private BarraVida barraVida;
    private TextureRegion imagenFondo;
    private TextureRegion imagenEnemigo;
    private EfectoSonido sonidoLaser;
    private EfectoSonido sonidoExplosion;
    private Music musica;
    private Jugador jugador;

    private boolean juegoTerminado = false;
    private boolean silenciado = false;
    private boolean pausado = false;

    private long inicioMs;
    private long tiempoTranscurridoSeg = 0;
    private long tiempoTotalPausado = 0;
    private long inicioPausa = 0;

    private BitmapFont fuenteGrande;
    private BitmapFont fuenteMediana;
    private BitmapFont fuentePequena;
    private final GlyphLayout medidor = new GlyphLayout();

    @Override
    public void create() {
        ultimaAparicion = TimeUtils.millis();

        camara = new OrthographicCamera();
        camara.setToOrtho(true, ANCHO, ALTO);
        pantalla = new SpriteBatch();
        puntaje = new Puntaje();
        barraVida = new BarraVida();

        try {
            imagenFondo = Recursos.cargarImagen("image/Fondo_meme_palacio_de_gobierno.png");
        } catch (GdxRuntimeException e) {
            System.out.println("No se pudo cargar el fondo: " + e.getMessage());
            imagenFondo = null;
        }

        try {
            sonidoLaser = new EfectoSonido("sounds/lazer-gun-432285.wav");
            sonidoExplosion = new EfectoSonido("sounds/explosion-under-snow-sfx-230505.wav");
        } catch (GdxRuntimeException e) {
            sonidoLaser = null;
            sonidoExplosion = null;
        }

        try {
            musica = Gdx.audio.newMusic(Gdx.files.internal("sounds/Triciclo Perú.wav"));
            Recursos.registrar(musica);
            musica.setVolume(0.5f);
            musica.setLooping(true);
            musica.play();
        } catch (GdxRuntimeException e) {
            musica = null;
        }

        try {
            imagenEnemigo = Recursos.cargarImagen("image/Alan_Garcia_muerto_meme.png");
        } catch (GdxRuntimeException e) {
            System.out.println("No se pudo cargar la imagen del enemigo: " + e.getMessage());
            imagenEnemigo = null;
        }

        jugador = new Jugador(sonidoLaser);
        inicioMs = TimeUtils.millis();

        fuenteGrande = Recursos.crearFuente(64);
        fuenteMediana = Recursos.crearFuente(36);
        fuentePequena = Recursos.crearFuente(24);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int tecla) {
                procesarEvento(tecla, true);
                return true;
            }

            @Override
            public boolean keyUp(int tecla) {
                procesarEvento(tecla, false);
                return true;
            }
        });
    }

    private void procesarEvento(int tecla, boolean presionada) {
        if (presionada && tecla == Keys.P && !juegoTerminado) {
            alternarPausa();
        }

        if (presionada && tecla == Keys.M) {
            alternarSilencio();
        }

        if (juegoTerminado && presionada && tecla == Keys.ENTER) {
            reiniciarJuego();
        }

        if (!juegoTerminado && !pausado) {
            jugador.manejarMovimiento(tecla, presionada);

            if (presionada && tecla == Keys.SPACE) {
                disparar();
            }
        }
    }

    private void alternarPausa() {
        pausado = !pausado;
        if (pausado) {
            if (musica != null) musica.pause();
            inicioPausa = TimeUtils.millis();
        } else {
            if (musica != null) musica.play();
            long duracionPausa = TimeUtils.millis() - inicioPausa;
            tiempoTotalPausado += duracionPausa;
            ultimaAparicion += duracionPausa;
        }
    }

    private void alternarSilencio() {
        silenciado = !silenciado;
        float volumen = silenciado ? 0f : 1.0f;
        if (musica != null) musica.setVolume(silenciado ? 0f : 0.5f);
        if (sonidoLaser != null) sonidoLaser.setVolumen(volumen);
        if (sonidoExplosion != null) sonidoExplosion.setVolumen(volumen);
        puntaje.getSonidoPuntaje().setVolumen(volumen);
    }

    private void reiniciarJuego() {
        juegoTerminado = false;
        enemigos.clear();
        balas.clear();
        puntaje.reiniciarPuntaje();

        barraVida.reiniciarVida();

        pausado = false;
        if (musica != null) musica.play();

        jugador.reiniciarPosicion();

        inicioMs = TimeUtils.millis();
        ultimaAparicion = TimeUtils.millis();
        tiempoTranscurridoSeg = 0;
        tiempoTotalPausado = 0;
    }

    private void disparar() {
        jugador.disparar();
        Bala bala = new Bala();

        float balaX = jugador.getX() + jugador.getTamano() / 2 - bala.getAncho() / 2;
        float balaY = jugador.getY() - bala.getAlto();

        bala.disparar(balaX, balaY);
        balas.add(bala);
    }

    @Override
    public void render() {
        if (juegoTerminado) {
            dibujarJuegoTerminado();
            return;
        }

        if (!pausado) {
            actualizar();
        }
        dibujar();
    }

    private void actualizar() {
        long tiempoActual = TimeUtils.millis();
        int puntos = puntaje.getPuntos();

        int maxEnemigos = Math.min(10, 4 + puntos / 2);
        long intervaloActual = Math.max(600, INTERVALO_APARICION_ENEMIGOS - puntos * 120);
        float factorVelocidad = 1.0f + (puntos / 3) * 0.25f;

        if (enemigos.size() < maxEnemigos && tiempoActual - ultimaAparicion > intervaloActual) {
            enemigos.add(new Enemigo(imagenEnemigo, factorVelocidad));
            ultimaAparicion = tiempoActual;
        }

        for (Enemigo enemigo : new ArrayList<>(enemigos)) {
            enemigo.actualizar();
            if (enemigo.getY() > ALTO) {
                enemigos.remove(enemigo);
            }
        }

        jugador.actualizar();

        for (Bala bala : new ArrayList<>(balas)) {
            bala.actualizar();
            if (!bala.estaVisible()) {
                balas.remove(bala);
            }
        }

        colisionBalaEnemigo();
        colisionEnemigoJugador();
    }

    private void colisionBalaEnemigo() {
        for (Bala bala : new ArrayList<>(balas)) {
            if (!bala.estaVisible()) {
                continue;
            }

            for (Enemigo enemigo : new ArrayList<>(enemigos)) {
                if (bala.colisionaCon(enemigo)) {
                    balas.remove(bala);
                    enemigos.remove(enemigo);
                    if (sonidoExplosion != null) {
                        sonidoExplosion.reproducir();
                    }
                    puntaje.sumar();
                    break;
                }
            }
        }
    }

    private void colisionEnemigoJugador() {
        for (Enemigo enemigo : new ArrayList<>(enemigos)) {
            if (enemigo.colisionaCon(jugador)) {
                if (sonidoExplosion != null) {
                    sonidoExplosion.reproducir();
                }
                barraVida.perderVida();
                enemigos.remove(enemigo);

                if (!barraVida.estaVivo()) {
                    tiempoTranscurridoSeg = (TimeUtils.millis() - inicioMs - tiempoTotalPausado) / 1000;
                    juegoTerminado = true;
                } else {
                    jugador.reiniciarPosicion();
                }
                break;
            }
        }
    }

    private void dibujar() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        pantalla.setProjectionMatrix(camara.combined);
        pantalla.begin();

        if (imagenFondo != null) {
            pantalla.draw(imagenFondo, 0, 0, ANCHO, ALTO);
        }

        for (Enemigo enemigo : enemigos) {
            enemigo.dibujar(pantalla);
        }

        for (Bala bala : balas) {
            if (bala.estaVisible()) {
                bala.dibujar(pantalla);
            }
        }

        jugador.dibujar(pantalla);
        puntaje.dibujar(pantalla);
        barraVida.dibujar(pantalla);

        if (silenciado) {
            fuentePequena.setColor(new Color(150 / 255f, 150 / 255f, 150 / 255f, 1f));
            fuentePequena.draw(pantalla, "MUTE", ANCHO - 60, 10);
        }

        if (pausado) {
            dibujarCentrado(fuenteGrande, "PAUSA", Color.BLACK, ALTO / 2 - 30);
            dibujarCentrado(fuentePequena, "Presiona P para continuar",
                    new Color(200 / 255f, 200 / 255f, 200 / 255f, 1f), ALTO / 2 + 30);
        }

        pantalla.end();
    }

    private void dibujarJuegoTerminado() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        pantalla.setProjectionMatrix(camara.combined);
        pantalla.begin();

        dibujarCentrado(fuenteGrande, "JUEGO TERMINADO", Color.RED, ALTO / 2 - 100);
        dibujarCentrado(fuenteMediana, "Puntaje: " + puntaje.getPuntos(), Color.WHITE, ALTO / 2 - 20);
        dibujarCentrado(fuenteMediana, "Tiempo: " + tiempoTranscurridoSeg + " s", Color.WHITE, ALTO / 2 + 30);
        dibujarCentrado(fuenteMediana, "Presiona ENTER para reiniciar", Color.GREEN, ALTO / 2 + 80);

        pantalla.end();
    }

    private void dibujarCentrado(BitmapFont fuente, String texto, Color color, float y) {
        fuente.setColor(color);
        medidor.setText(fuente, texto);
        fuente.draw(pantalla, medidor, ANCHO / 2f - (int) medidor.width / 2, y);
    }

    @Override
    public void dispose() {
        pantalla.dispose();
        Recursos.liberarTodo();
    }
}
