# Proyecto2_1910114_1811100
- Nombres: Stefano Casale, Enyerber Silva
- Carnets: 1910114, 1811100
- Universidad Simón Bolívar
- Trimestre Ene - Mar 2026

# Pasos de Ejecucion
1. Instalar Kotlin:
    - Desde tu terminal de WSL ejecuta el siguiente comando:
        - sudo apt install kotlin
2. Ubica tu entorno:
    - Desde tu terminal de WSL ejecuta el siguiente comando:
        - cd ubicacion/de/tu/entorno
3. Compila todos los archivos .kt que estan en la misma carpeta:
    - Desde tu terminal de WSL ejecuta el siguiente comando:
        - kotlinc *.kt -d out -include-runtime
4. Ejecuta Main.kt:
    - Desde tu terminal de WSL ejecuta el siguiente comando:
        - kotlin -cp out MundoChiquitoKt

Al compilar se crea una carpeta "out" en tu entorno. Contiene los archivos ya compilados

# Funcionamiento
El programa simula el efecto de la carta de conjuro "Mundo Chiquito" del juego de cartas coleccionables Duelo de cartas mostro. Dado un archivo CSV (deck.csv) que contiene una lista de cartas mostro, el programa:
    1. Lee y valida el archivo CSV: Se asegura que cada carta cumpla con las reglas del juego:
        - Nivel entre 1 y 12
        - Atributo válido
        - Poder múltiplo de 50
        - Mazo entre 40 y 60 cartas
    2. Construye un grafo no dirigido con las siguientes características
        - Los vértices son las cartas 
        - Existe una arista entre dos vértices si comparten exactamente una de las 3 características (nivel, poder, atributo)
    3. Encuentra las ternas que cumplen con:
        - <cartaMazo1> es vecio de <cartaMano> en el grafo
        - <cartraMazo2> es vecino de <cartaMazo1>


# Decisiones de Implementacion
Si el archivo tiene un error y el mazo no es válido, no construimos el grafo inicial. En este caso, solicitamos al usuario correjir el archivo. El uso de excepciones mejora el flujo del programa y hace que el manejo de errores se centre en el main.

Reutilizamos la clase ListaAdyacenciaGrafo del proyecto 1 para implementar un grafo por listas de adyacencia.

Para la construccion del grafo:
    - Tomamos vértices únicos dado por el nombre de la carta. Usamos distinctBy { it.nombre } sobre la lista de cartas leídas para obtener una lista de cartas únicas. De esta manera, para las relaciones, todas las copias son equivalentes. Luego iteramos sobre las cartas únicas y las insertamos en el mapa con una lista vacía de vecinos
    - Comparamos cada par de cartas una sola vez. Si son adyacentes, entonces creamos un grafo no dirigido.
    - Se cuentan las propiedades compartidas

Para la búsqueda de ternas se tuvo el objetivo de enontrar todas las combinaciones de (cartaMano, cartaMazo1, cartaMazo2) que complan con:
    - <cartaMazo1> es vecio de <cartaMano> en el grafo
    - <cartraMazo2> es vecino de <cartaMazo1>
Lo que significa, encontrar los caminos de longitud 2 en el grafo. Pudimos usar DFS con profundidad maxima 2 pero sería introducir complejidad innecesaria al código. Optamos por un triple bucle anidado que, para cada vértice, explora todos sus vecinos y para cada vecino, explora todos sus vecinos. Cada combinación genera una terna.

Cada terna se almacena con una lista de Triple<CartaMostro, CartaMostro, CartaMostro>

Finalmente imprimimos cada una de las ternas en la lista.
    
# Tabla de complejidad computacional (Big O)

## Tabla de complejidad computacional (Big O)

| Método                      | Complejidad       | Descripción                                                                                       |
|-----------------------------|-------------------|---------------------------------------------------------------------------------------------------|
| leerMazo()                |  O(n)          | Recorre las n líneas del archivo CSV, procesando cada una en tiempo constante.                 |
| construirGrafoMundoChiquito() | O(v²)     | Compara cada par de v cartas únicas (v ≤ 60) para determinar adyacencia.                       |
| sonAdyacentes()           | O(1)          | Compara tres atributos de dos cartas.                                                             |
| encontrarTernas()         | O(v * g^2)     | Para cada vértice (v), explora sus vecinos (grado promedio g) y los vecinos de estos.            |
| imprimirTernas()        | O(t)        | Recorre las t ternas e imprime cada una.                                                        |
| obtenerVertices()         | O(1)         | Devuelve el conjunto de vértices del grafo.                                                       |

Leyenda: 
- v = número de vértices (cartas únicas).
- g = grado promedio del grafo.  
- t = número de ternas generadas.  




