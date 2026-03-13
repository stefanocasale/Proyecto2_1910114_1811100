//Definición de los posibles elementos o naturalezas que puede tener una carta.
enum class Atributo {
    AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO
}

/**
 * Esta clase representa a un monstruo del juego.
 * Aquí definimos qué información tiene cada carta y las reglas básicas
 * que debe cumplir para ser considerada válida.
 */
class CartaMostro(
    val nombre: String,
    val nivel: Int,
    val atributo: Atributo,
    val poder: Int
) {
    // El bloque 'init' se ejecuta automáticamente al crear cada carta.
    // Sirve para revisar que los datos introducidos tengan sentido.
    init {
        // Regla 1: El nombre no puede estar vacío.
        require(nombre.isNotBlank()) { "La carta debe tener un nombre." }

        // Regla 2: El nivel debe ser un número lógico para el juego (del 1 al 12).
        require(nivel in 1..12) { "El nivel ($nivel) no es válido; debe estar entre 1 y 12." }

        // Regla 3: El poder debe ser siempre un número "redondo", es decir, múltiplo de 50.
        require(poder % 50 == 0) { "El poder ($poder) debe ser múltiplo de 50 (ej: 50, 100, 1050)." }
    }

    /**
     * Esta función permite que, cuando queramos imprimir la carta o verla en una lista,
     * se muestre directamente su nombre en lugar de códigos extraños.
     */
    override fun toString(): String = nombre    
}