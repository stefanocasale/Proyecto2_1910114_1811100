// Interfaz
interface Grafo<T> {
    fun agregarVertice(v: T): Boolean
    fun eliminarVertice(v: T): Boolean
    fun conectar(desde: T, hasta: T): Boolean
    fun contiene(v: T): Boolean
    fun obtenerArcosSalida(v: T): List<T>
    fun obtenerArcosEntrada(v: T): List<T>
    fun tamano(): Int
    fun subgrafo(vertices: Collection<T>): Grafo<T>
    fun obtenerVertices(): Set<T>
}