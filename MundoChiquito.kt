fun leerMazo(): List<CartaMostro> {
    val listaDeCartas = mutableListOf<CartaMostro>()
    
    val archivo = java.io.File("deck.csv")
    
    if (!archivo.exists()) {
        println("Error: No se encontró el archivo deck.csv")
        return listaDeCartas
    }

    // Leemos las líneas del archivo
    val lineas = archivo.readLines()

    // Procesamos saltando el encabezado
    for (i in 1 until lineas.size) {
        val linea = lineas[i].trim()
        
        if (linea.isNotEmpty()) {
            val columnas = linea.split(",")
            
            if (columnas.size == 4) {
                try {
                    val nombre = columnas[0].trim()
                    val nivel = columnas[1].trim().toInt()
                    
                    // Conversión al Enum Atributo
                    val atributoStr = columnas[2].trim().uppercase()
                    val atributo = Atributo.valueOf(atributoStr)
                    
                    val poder = columnas[3].trim().toInt()

                    // Creamos la instancia: el init de CartaMostro validará nivel y poder
                    val nuevaCarta = CartaMostro(nombre, nivel, atributo, poder)
                    listaDeCartas.add(nuevaCarta)
                    
                } catch (e: Exception) {
                    // Si algo falla en la línea (formato o validación), avisamos y seguimos
                    println("Error en línea $i del CSV: ${e.message}")
                }
            }
        }
    }
    
    return listaDeCartas
}