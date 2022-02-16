package ru.netology.graphics.image;

public class SymbolSchema implements TextColorSchema {
    @Override
    public char convert(int color) {
        return color < 28 ? '▇' : color < 56 ? '●' : color < 84 ? '◉' : color < 112 ?
                '◍' : color < 140 ? '◎' : color < 168 ? '○' : color < 196 ? '☉' : color < 224 ? '◌' : '-';
    }
}
