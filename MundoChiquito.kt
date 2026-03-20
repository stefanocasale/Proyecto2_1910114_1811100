/*
 * Lee y procesa un archivo CSV para extraer la información de los nodos.
 * Implementa validaciones de dominio para asegurar la integridad de los datos cargados.
 */
fun leerMazo(): List<CartaMostro> {
    val listaDeCartas = mutableListOf<CartaMostro>()
    val conteoNombres = mutableMapOf<String, Int>()
    
    val archivo = java.io.File("deck.csv")
    if (!archivo.exists()) {
        throw IllegalArgumentException("Error: Archivo de datos no encontrado.")
    }

    val lineas = archivo.readLines()
    if (lineas.size < 2) {
        throw IllegalArgumentException("Error, el archivo no tiene datos")
    }
    
    for (i in 1 until lineas.size) {
        val columnas = lineas[i].split(",").map { it.trim() }
        val linea = lineas[i].trim()

        if (linea.isBlank()) continue
        
        if (columnas.size != 4) {
            throw IllegalArgumentException("Error en la línea ${i+1}: No tiene los 4 atributos")
        }

        val nombre = columnas[0]
        if (nombre.isBlank()) {
            throw IllegalArgumentException("Error en linea ${i+1}: El nombre no debe estas vacío")
        }

        val nivel = try {
            columnas[1].toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Error en línea ${i+1}: El nivel debe ser un número entero")
        }

        val poder = try {
            columnas[3].toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Errror en línea ${i+1}: El poder no cumple el formato válido")
        }

        val atributoStr = columnas[2].uppercase()
        val atributo = try {
            Atributo.valueOf(atributoStr)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Error en linea ${i+1}: El atributo no es válido")
        }

        // Límite de 3 copias por nombre
        val copiasActuales = conteoNombres.getOrDefault(nombre, 0)
        if (copiasActuales >= 3) continue 

        val carta = CartaMostro(nombre, nivel, atributo, poder)
        listaDeCartas.add(carta)
        conteoNombres[nombre] = copiasActuales + 1
    }

    if (listaDeCartas.size < 40 || listaDeCartas.size > 60) {
        throw IllegalArgumentException("El mazo debe tener entre 40 o 60 cartas")
    }

    return listaDeCartas
}

/**
 * Construye el grafo basado en cartas únicas para evitar aristas redundantes.
 */
fun construirGrafoMundoChiquito(cartas: List<CartaMostro>): ListaAdyacenciaGrafo<CartaMostro> {
    val grafo = ListaAdyacenciaGrafo<CartaMostro>()
    val cartasUnicas = cartas.distinctBy { it.nombre }

    for (carta in cartasUnicas) {
        grafo.agregarVertice(carta)
    }

    for (i in cartasUnicas.indices) {
        for (j in i + 1 until cartasUnicas.size) {
            val cartaA = cartasUnicas[i]
            val cartaB = cartasUnicas[j]

            if (sonAdyacentes(cartaA, cartaB)) {
                grafo.conectar(cartaA, cartaB)
                grafo.conectar(cartaB, cartaA)
            }
        }
    }
    return grafo
}

fun sonAdyacentes(a: CartaMostro, b: CartaMostro): Boolean {
    var coincidencias = 0
    if (a.nivel == b.nivel) coincidencias++
    if (a.atributo == b.atributo) coincidencias++
    if (a.poder == b.poder) coincidencias++
    return coincidencias == 1
}

/**
 * Encuentra ternas únicas basadas en el nombre de las cartas.
 * Utiliza un Set para filtrar duplicados lógicos.
 */
fun encontrarTernas(grafo: ListaAdyacenciaGrafo<CartaMostro>): List<Triple<CartaMostro, CartaMostro, CartaMostro>> {
    val ternasUnicas = mutableListOf<Triple<CartaMostro, CartaMostro, CartaMostro>>()
    val registroNombres = mutableSetOf<String>() // Para evitar "A B C" duplicado
    val vertices = grafo.obtenerVertices()

    for (cartaMano in vertices) {
        val vecinosMano = grafo.obtenerArcosSalida(cartaMano)

        for (cartaMazo1 in vecinosMano) {
            val vecinosMazo1 = grafo.obtenerArcosSalida(cartaMazo1)
            
            for (cartaMazo2 in vecinosMazo1) {
                // Regla: La carta destino no puede ser la inicial
                if (cartaMano.nombre != cartaMazo2.nombre) {
                    val identificador = "${cartaMano.nombre}|${cartaMazo1.nombre}|${cartaMazo2.nombre}"
                    
                    if (!registroNombres.contains(identificador)) {
                        ternasUnicas.add(Triple(cartaMano, cartaMazo1, cartaMazo2))
                        registroNombres.add(identificador)
                    }
                }
            }
        }
    }
    return ternasUnicas
}

fun imprimirTernas(ternas: List<Triple<CartaMostro, CartaMostro, CartaMostro>>) {
    if (ternas.isEmpty()) {
        println("No se encontraron ternas válidas.")
        return
    }
    for ((mano, mazo1, mazo2) in ternas) {
        println("${mano.nombre} ${mazo1.nombre} ${mazo2.nombre}")
    }
}

fun main() {
    try {
        val mazo = leerMazo()
        val grafo = construirGrafoMundoChiquito(mazo)
        val ternas = encontrarTernas(grafo)
        imprimirTernas(ternas)
    } catch (e: Exception) {
        println(e.message)
        System.exit(1)
    }
}