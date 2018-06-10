module HoTT.Model {
    requires java.xml;
    requires kotlin.stdlib;
    requires java.management;
    requires java.logging;
    requires java.desktop;
    requires tornadofx;
    requires fluent.hc;
    requires httpcore;
    requires javafx.graphics;
    requires java.xml.bind;
    requires java.prefs;
    requires javafx.controls;
    requires javafx.web;
    requires jssc;

    exports de.treichels.hott.logging;
    exports de.treichels.hott.messages;
    exports de.treichels.hott.model;
    exports de.treichels.hott.model.enums;
    exports de.treichels.hott.model.firmware;
    exports de.treichels.hott.model.serial;
    exports de.treichels.hott.model.voice;
    exports de.treichels.hott.util;
}