// Elementos disponibles
enum class Atributo {
    AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO
}

// Instancia de cada carta
class CartaMostro(
    val nombre: String,         
    val nivel: Int,
    val atributo: Atributo,
    val poder: Int
) {
    // Verificación para los datos
    init {
        // El nombre no puede estar vaccío
        require(nombre.isNotBlank()) { "La carta debe tener un nombre." }

        // El nivel debe estar entre 1 y 12
        require(nivel in 1..12) { "El nivel ($nivel) no es válido; debe estar entre 1 y 12." }

        // El poder debe ser múltiplo de 50
        require(poder % 50 == 0) { "El poder ($poder) debe ser múltiplo de 50 (ej: 50, 100, 1050)." }
    }

    /**
     * Esta función permite que, cuando queramos imprimir la carta o verla en una lista,
     * se muestre directamente su nombre en lugar de códigos extraños.
     */
    override fun toString(): String = nombre    
}