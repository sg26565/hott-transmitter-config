package de.treichels.hott.mz32

class Hash(val size: Long, val hash: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hash

        if (size != other.size) return false
        if (hash != other.hash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + hash.hashCode()
        return result
    }

    override fun toString(): String {
        return "Hash(size=$size, hash='$hash')"
    }
}
