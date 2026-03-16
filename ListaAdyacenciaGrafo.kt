// Implementación de un grafo utilizando una lista de adyacencia
class ListaAdyacenciaGrafo<T> : Grafo<T> {
    private val adyacencias: MutableMap<T, MutableList<T>> = mutableMapOf()

    override fun contiene(v: T): Boolean { // Verifica si el vértice existe en el grafo
        return v in adyacencias 
    }

    override fun agregarVertice(v: T): Boolean { 
        if (v !in adyacencias) { // Si el vértice no existe, se agrega al grafo
            adyacencias[v] = mutableListOf() 
            return true
        }

        return false 
    }

    override fun tamano(): Int { 
        return adyacencias.size // Devuelve la cantidad de vértices en el grafo
    }

    override fun conectar(desde: T, hasta: T): Boolean { 
        // Verifica que ambos nodos a conectar existan en el grafo
        if (desde !in adyacencias || hasta !in adyacencias){
            return false 
        }

        // Verifica si la conexión ya existe 
        if (hasta in adyacencias[desde]!!) {
            return false
        }

        // Realiza la conexión   
        adyacencias[desde]!!.add(hasta)

        return true
    }

    override fun obtenerArcosSalida(v: T): List<T> { 
        // Verifica si el vértice existe 
        if (v !in adyacencias) {
            return listOf() // Si no existe, retorna una lista vacía
        }

        // Retorna la lista de vértices que salen desde v 
        return adyacencias[v]!!
    }

    override fun obtenerArcosEntrada(v: T): List<T> { 

        // Verifica si el vértice existe
        if (v !in adyacencias) {
            return listOf() // Si no existe, retorna una lista vacía
        }

        val entrada = mutableListOf<T>() // Almacena los vértices que tienen una conexión hacia v

        // Recorre todos los vertices 
        for (vertices in adyacencias) { 
            // Verifica la conexión 
            if (v in vertices.value) { // Si v esta en la lista actual, se agrega el vértice a la lista de entrada
                entrada.add(vertices.key) 
            }
        }

        return entrada
    }

    override fun eliminarVertice(v: T): Boolean {

        // Verifica si el vértice existe
        if (v !in adyacencias) {
            return false // Si no existe retorna false
        }

        adyacencias.remove(v) // Elimina el vértice v y los arcos asociados

        // Recorre los demás vértices
        for (entrada in adyacencias) {
            val listaSucesores = entrada.value

            // Elimina las referencias al vértice v en los vértices asociados
            if (v in listaSucesores) {
                listaSucesores.remove(v)
            }
        }

        return true
    }

    override fun subgrafo(vertices: Collection<T>): Grafo<T> {

        // Creamos una nueva instancia de grafo
        val grafoNuevo = ListaAdyacenciaGrafo<T>()

        // Se recorre la lista de vértices que interesan
        for (v in vertices) {
            if (this.contiene(v)) {
                grafoNuevo.agregarVertice(v) // Se agregan vértices solo si estaban en el original
            }
        }

        // Se recorren de nuevo los vértices viendo sus arcos
        for (v in vertices) {
            if (this.contiene(v)) {
                // Obtenemos los sucesores de cada vértice en el grafo original
                val sucesoresOriginales = obtenerArcosSalida(v)

                for (sucesor in sucesoresOriginales) {
                    // Solo se crea el arco si el destino también está en el subgrafo
                    if (grafoNuevo.contiene(sucesor)) {
                        grafoNuevo.conectar(v, sucesor)
                    }
                }
            }
        }
        return grafoNuevo // Retorna la instancia con los nuevos datos
    }

    override fun obtenerVertices(): Set<T> {
        return adyacencias.keys.toSet()
    }
}