module javazoom.vorbisspi {
    exports javazoom.spi.vorbis.sampled.convert;
    exports javazoom.spi.vorbis.sampled.file;

    requires javazoom.spi;
    requires java.desktop;
    requires tritonus.share;
    requires jorbis;
}