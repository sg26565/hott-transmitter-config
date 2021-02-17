package de.treichels.hott

import tornadofx.launch
import tornadofx.App

fun main(args: Array<String>) {
    launch<SaveModels>(args)
}

// Example from https://github.com/edvin/tornadofx/wiki/Type-Safe-Builders
// https://edvin.gitbooks.io/tornadofx-guide/content/part1/1_Why_TornadoFX.html
// https://stackoverflow.com/questions/50976849/invoke-function-when-item-in-combobox-is-selected-tornadofx
// https://github.com/edvin/tornadofx/issues/355

class SaveModels : App(MyView::class)
