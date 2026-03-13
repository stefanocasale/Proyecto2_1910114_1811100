class CartaMostro(
    val nombre: String,
    val nivel: Int,
    val atributo: Atributo,
    val poder: Int
) {
    init {
        require(nivel in 1..12) { "Nivel debe estar entre 1 y 12" } [cite: 3]
        require(poder % 50 == 0) { "Poder debe ser múltiplo de 50" } [cite: 3]
    }

    override fun toString(): String = nombre
}

enum class Atributo {
    AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO
}