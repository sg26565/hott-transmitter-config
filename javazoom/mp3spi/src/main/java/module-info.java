module javazoom.mp3spi {
    exports javazoom.spi.mpeg.sampled.convert;
    exports javazoom.spi.mpeg.sampled.file;
    exports javazoom.spi.mpeg.sampled.file.tag;

    requires javazoom.spi;
    requires java.desktop;
    requires javazoom.jlayer;
    requires tritonus.share;
}