/*
 * Lee y procesa un archivo CSV para extraer la información de los nodos.
 * Implementa validaciones de dominio para asegurar la integridad de los datos cargados.
 * Si encuentra algún error, lanza excepción y detiene la ejecución
 */
fun leerMazo(): List<CartaMostro> {
    val listaDeCartas = mutableListOf<CartaMostro>()
    val conteoNombres = mutableMapOf<String, Int>()
    
    // Accedemos al archivo
    val archivo = java.io.File("deck.csv")
    if (!archivo.exists()) {
        throw IllegalArgumentException("Error: Archivo de datos no encontrado.")
    }

    // Verificamos que hay datos
    val lineas = archivo.readLines()
    if (lineas.size < 2) {
        throw IllegalArgumentException("Error, el archivo no tiene datos")
    }
    
    // Procesamos cada una de las filas
    for (i in 1 until lineas.size) {
        val columnas = lineas[i].split(",").map { it.trim() }
        val linea = lineas[i].trim()

        // Saltamos las líneas vacías
        if (linea.isBlank()) {
            continue
        }
        
        // Verificamos la cantidad de atributos
        if (columnas.size != 4) {
            throw IllegalArgumentException("Error en la línea ${i+1}: No tiene los 4 atributos")
        }

        val nombre = columnas[0]
                
        // Verificamos que el nombre no este vacío
        if (nombre.isBlank()) {
            throw IllegalArgumentException("Error en linea ${i+1}: El nombre no debe estas vacío")
        }

        // Verificamos que el nivel sea un entero
        val nivel = try {
            columnas[1].toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Error en línea ${i+1}: El nivel debe ser un número entero")
        }

        // Verificamos que el poder sea entero
        val poder = try {
            columnas[3].toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Errror en línea ${i+1}: El poder no cumple el formato válido")
        }

        // Verificamos el atributo
        val atributoStr = columnas[2].uppercase()
        val atributo = try {
            Atributo.valueOf(atributoStr)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Error en linea ${i+1}: El atributo no es válido")
        }

        // Verificamos el límite de copias
        val copiasActuales = conteoNombres.getOrDefault(nombre, 0)
        if (copiasActuales >= 3) {
            continue 
        }        

        // Creamos la carta (Las validaciones de rango nivel 1-12 y poder % 50 
        // se ejecutan automáticamente en el init de CartaMostro)
        val carta = CartaMostro(
            nombre, 
            nivel,
            atributo,
            poder
        )
                
        listaDeCartas.add(carta)
        conteoNombres[nombre] = copiasActuales + 1
    }

    // Verificamos el rango del mazo
    if (listaDeCartas.size < 40 || listaDeCartas.size > 60) {
        throw IllegalArgumentException("El mazo debe tener entre 40 o 60 cartas")
    }

    return listaDeCartas
}

/**
 * Construye una representación de grafo basada en listas de adyacencia.
 * Los nodos representan tipos únicos y las aristas las relaciones de adyacencia.
 */
fun construirGrafoMundoChiquito(cartas: List<CartaMostro>): ListaAdyacenciaGrafo<CartaMostro> {
    val grafo = ListaAdyacenciaGrafo<CartaMostro>()

    // Transformamos la lista vértices únicos por nombre
    val cartasUnicas = cartas.distinctBy { it.nombre }

    // Insertamos los vértices al grafo
    for (carta in cartasUnicas) {
        grafo.agregarVertice(carta)
    }

    // Establecemos las aristas 
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
    
    // Buscamos las coincidencias de los atributos
    if (a.nivel == b.nivel) coincidencias++
    if (a.atributo == b.atributo) coincidencias++
    if (a.poder == b.poder) coincidencias++
    
    // Mundo chiquito requiere exactamente una coincidencia
    return coincidencias == 1
}

/**
 * Encuentra todas las ternas (cartaMano, cartaMazo1, cartaMazo2) que cumplen:
 * - cartaMazo1 es vecino de cartaMano
 * - cartaMazo2 es vecino de cartaMazo1
 * - cartaMazo2 debe ser diferente a cartaMano para ser una terna útil.
 */
fun encontrarTernas(grafo: ListaAdyacenciaGrafo<CartaMostro>): List<Triple<CartaMostro, CartaMostro, CartaMostro>> {
    val ternas = mutableListOf<Triple<CartaMostro, CartaMostro, CartaMostro>>()
    val vertices = grafo.obtenerVertices()

    // Iteramos sobre los vértices (Cartas en mano)
    for (cartaMano in vertices) {
        val vecinosMano = grafo.obtenerArcosSalida(cartaMano)

        // Iteramos sobre los vecinos (Carta puente del mazo)
        for (cartaMazo1 in vecinosMano) {
            val vecinosMazo1 = grafo.obtenerArcosSalida(cartaMazo1)
            
            // Iteramos sobre los vecinos de la carta puente (Carta destino del mazo)
            for (cartaMazo2 in vecinosMazo1) {

                // Verificamos que la carta final no sea la inicial 
                // para que la terna sea útil según el efecto de la carta.
                if (cartaMano.nombre != cartaMazo2.nombre) {
                    // Almacenamos la terna
                    ternas.add(Triple(cartaMano, cartaMazo1, cartaMazo2))
                }
            }
        }
    }
    return ternas
}

/**
 * Imprime cada terna en una línea, con los nombres separados por espacios.
 */
fun imprimirTernas(ternas: List<Triple<CartaMostro, CartaMostro, CartaMostro>>) {
    // Recorremos cada terna almacenada
    for ((mano, mazo1, mazo2) in ternas) {
        println("${mano.nombre} ${mazo1.nombre} ${mazo2.nombre}")
    }
}

/**
 * Punto de entrada principal para la ejecución del programa y orquestación de módulos.
 */
fun main() {
    try {
        // Iniciamos la carga de datos
        val mazo = leerMazo()
    
        // Construimos el Grafo
        val grafo = construirGrafoMundoChiquito(mazo)

        // Creamos las ternas
        val ternas = encontrarTernas(grafo)

        // Imprimimos las ternas
        imprimirTernas(ternas)
    } catch (e: Exception) {
        // Capturamos cualquier error de formato o validación de CartaMostro
        println(e.message)
        // Salimos del código porque el mazo no cumple las reglas
        System.exit(1)
    }
}