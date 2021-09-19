package de.treichels.hott

import javafx.fxml.FXML
import javafx.scene.control.Label

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var murksText: Label

    @FXML
    private fun onHugo123ButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
        murksText.text = ""
    }

    @FXML
    private fun onOtto123ButtonClick() {
        murksText.text = "Murks"
        welcomeText.text = ""
    }
}