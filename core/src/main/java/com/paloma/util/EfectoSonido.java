package com.paloma.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class EfectoSonido {

    private final Sound sonido;
    private float volumen = 1.0f;

    public EfectoSonido(String ruta) {
        sonido = Gdx.audio.newSound(Gdx.files.internal(ruta));
        Recursos.registrar(sonido);
    }

    public void reproducir() {
        sonido.play(volumen);
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }
}
