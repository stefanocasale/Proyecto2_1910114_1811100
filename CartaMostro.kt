// Elementos disponibles
enum class Atributo {
    AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO
}

// Instancia de cada carta
class CartaMostro(
    private val _nombre: String,         
    private val _nivel: Int,
    private val _atributo: Atributo,
    private val _poder: Int
) {
    // Propiedades públicas de solo lectura
    val nombre: String get() = _nombre
    val nivel: Int get() = _nivel
    val atributo: Atributo get() = _atributo
    val poder: Int get() = _poder

    // Verificación para los datos
    init {
        // El nombre no puede estar vacío
        require(_nombre.isNotBlank()) { "La carta debe tener un nombre." }

        // El nivel debe estar entre 1 y 12
        require(_nivel in 1..12) { "El nivel ($_nivel) no es válido; debe estar entre 1 y 12." }

        // El poder debe ser múltiplo de 50
        require(_poder % 50 == 0) { "El poder ($_poder) debe ser múltiplo de 50 (ej: 50, 100, 1050)." }
    }

    /**
     * Esta función permite que, cuando queramos imprimir la carta o verla en una lista,
     * se muestre directamente su nombre en lugar de códigos extraños.
     */
    override fun toString(): String = _nombre    
}