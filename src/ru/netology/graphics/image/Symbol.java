package ru.netology.graphics.image;

public class Symbol implements TextColorSchema{

    @Override
    public char convert(int color) {
        char[] symbols = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
        /*color < 32 ? '#' :color < 64 ? '$': color < 96 ? '@': color < 128 ?
                '%': color < 160 ? '*' : color < 192 ? '+' : color < 224 ? '-' : '\'' ;*/
    }
}
