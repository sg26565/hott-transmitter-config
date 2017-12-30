fun main(args: Array<String>) {
    assert(OpenFoo() != OpenFoo("bar"))
}

open class OpenFoo(
        var bar: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OpenFoo) return false

        if (bar != other.bar) return false

        return true
    }

    override fun hashCode(): Int {
        return bar?.hashCode() ?: 0
    }
}
