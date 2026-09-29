package com.paloma.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.List;

public final class Recursos {

    private static final List<Disposable> cargados = new ArrayList<>();
    private static TextureRegion pixel;

    private Recursos() {
    }

    public static TextureRegion cargarImagen(String ruta) {
        Texture textura = new Texture(Gdx.files.internal(ruta));
        textura.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        cargados.add(textura);
        TextureRegion region = new TextureRegion(textura);
        region.flip(false, true);
        return region;
    }

    public static TextureRegion colorSolido(Color color, int ancho, int alto) {
        Pixmap pixmap = new Pixmap(ancho, alto, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        cargados.add(textura);
        return new TextureRegion(textura);
    }

    public static TextureRegion pixel() {
        if (pixel == null) {
            pixel = colorSolido(Color.WHITE, 1, 1);
        }
        return pixel;
    }

    public static BitmapFont crearFuente(int tamano) {
        BitmapFont fuente = new BitmapFont(true);
        fuente.getData().setScale(tamano / 24f);
        fuente.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        cargados.add(fuente);
        return fuente;
    }

    public static void registrar(Disposable recurso) {
        cargados.add(recurso);
    }

    public static void liberarTodo() {
        for (Disposable recurso : cargados) {
            recurso.dispose();
        }
        cargados.clear();
        pixel = null;
    }
}
