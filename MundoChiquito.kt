/**
 * Lee y procesa un archivo CSV para extraer la información de los nodos.
 * Implementa validaciones de dominio para asegurar la integridad de los datos cargados.
 */
fun leerMazo(): List<CartaMostro> {
    val listaDeCartas = mutableListOf<CartaMostro>()
    val conteoNombres = mutableMapOf<String, Int>()
    
    // Acceso al sistema de archivos local para obtener el recurso de datos
    val archivo = java.io.File("deck.csv")
    if (!archivo.exists()) {
        println("Error: Archivo de datos no encontrado.")
        return listaDeCartas
    }

    val lineas = archivo.readLines()
    
    // Procesamiento iterativo de las filas, omitiendo la línea de encabezado
    for (i in 1 until lineas.size) {
        val columnas = lineas[i].split(",").map { it.trim() }
        
        // Verificación de integridad estructural (número de columnas esperado)
        if (columnas.size == 4) {
            try {
                val nombre = columnas[0]
                
                // Limita la cantidad de ejemplares por identificador único
                val copiasActuales = conteoNombres.getOrDefault(nombre, 0)
                if (copiasActuales >= 3) {
                    continue 
                }

                // Instanciación del objeto de datos con casting de tipos y normalización de Enums
                val c = CartaMostro(
                    nombre, 
                    columnas[1].toInt(), 
                    Atributo.valueOf(columnas[2].uppercase()), 
                    columnas[3].toInt()
                )
                
                listaDeCartas.add(c)
                conteoNombres[nombre] = copiasActuales + 1
                
            } catch (e: Exception) {
                // Captura de excepciones por errores de formato en el CSV o fallos en validaciones internas
                println("Error en la entrada de datos - línea $i: ${e.message}")
            }
        }
    }

    // Validación de rango para la colección total procesada
    if (listaDeCartas.size < 40 || listaDeCartas.size > 60) {
        println("Aviso: La colección total no cumple con los límites de tamaño estándar.")
    }

    return listaDeCartas
}

/**
 * Construye una representación de grafo basada en listas de adyacencia.
 * Los nodos representan tipos únicos y las aristas las relaciones de adyacencia.
 */
fun construirGrafoMundoChiquito(cartas: List<CartaMostro>): ListaAdyacenciaGrafo<CartaMostro> {
    val grafo = ListaAdyacenciaGrafo<CartaMostro>()

    // Transformación funcional para reducir la lista a elementos únicos por identificador (nombre)
    val cartasUnicas = cartas.distinctBy { it.nombre }

    // Inserción de vértices en la estructura del grafo
    for (carta in cartasUnicas) {
        grafo.agregarVertice(carta)
    }

    // Establecimiento de aristas mediante comparación de pares
    for (i in cartasUnicas.indices) {
        for (j in i + 1 until cartasUnicas.size) {
            val cartaA = cartasUnicas[i]
            val cartaB = cartasUnicas[j]

            // Si se cumple la condición de adyacencia, se establece una conexión bidireccional
            if (sonAdyacentes(cartaA, cartaB)) {
                grafo.conectar(cartaA, cartaB)
                grafo.conectar(cartaB, cartaA)
            }
        }
    }

    return grafo
}

/**
 * Determina si existe una relación de adyacencia entre dos elementos.
 * La condición se cumple si los objetos comparten exactamente un atributo de sus propiedades.
 */
fun sonAdyacentes(a: CartaMostro, b: CartaMostro): Boolean {
    var coincidencias = 0
    
    // Evaluación comparativa de los atributos de los objetos
    if (a.nivel == b.nivel) coincidencias++
    if (a.atributo == b.atributo) coincidencias++
    if (a.poder == b.poder) coincidencias++
    
    // Retorna verdadero únicamente si el grado de coincidencia es igual a uno
    return coincidencias == 1
}


/**
 * Punto de entrada principal para la ejecución del programa y orquestación de módulos.
 */
fun main() {
    // Inicialización del flujo de carga de datos
    val mazo = leerMazo()
    
    // Generación de la estructura de datos relacional (Grafo)
    val grafo = construirGrafoMundoChiquito(mazo)
    
    // Salida informativa sobre el estado final de la construcción
    println("Proceso finalizado.")
    println("Total de elementos únicos (vértices): ${grafo.tamano()}")
}