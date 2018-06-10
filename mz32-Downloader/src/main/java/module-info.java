module HoTT.mz32Downloader {
    requires kotlin.stdlib;
    requires tornadofx;
    requires HoTT.Model;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.logging.log4j.jcl;
    requires org.apache.logging.log4j;
    requires controlsfx;
    requires javafx.controls;
    requires java.prefs;
    requires lzma.sdk;

    exports de.treichels.hott.mz32;
}