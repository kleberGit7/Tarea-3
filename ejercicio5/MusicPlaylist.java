package ejercicio5;

import java.util.Scanner;

/**
 * EJERCICIO 5: Sistema de reproducción circular (playlist musical)
 *
 * Permite:
 * - Agregar canciones al inicio y al final
 * - Mostrar la playlist completa
 * - Reproducir la siguiente canción (vuelve automáticamente al inicio al llegar
 * al final)
 * - Eliminar una canción por nombre
 *
 * Incluye menú interactivo por consola con validación de entradas.
 */

class NodoCancion {
    String titulo;
    NodoCancion siguiente;

    NodoCancion(String titulo) {
        this.titulo = titulo;
    }
}

public class MusicPlaylist {

    private NodoCancion cabeza;
    private NodoCancion cola;
    private NodoCancion reproduccionActual; // puntero independiente de reproduccion
    private int tamano;

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregarInicio(String titulo) {
        NodoCancion nuevo = new NodoCancion(titulo);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
            nuevo.siguiente = cabeza;
            reproduccionActual = cabeza;
        } else {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            cola.siguiente = cabeza;
        }
        tamano++;
    }

    public void agregarFinal(String titulo) {
        NodoCancion nuevo = new NodoCancion(titulo);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
            nuevo.siguiente = cabeza;
            reproduccionActual = cabeza;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
            cola.siguiente = cabeza;
        }
        tamano++;
    }

    public void mostrarPlaylist() {
        if (estaVacia()) {
            System.out.println("La playlist esta vacia.");
            return;
        }
        StringBuilder sb = new StringBuilder("Playlist: ");
        NodoCancion actual = cabeza;
        do {
            sb.append("[").append(actual.titulo).append("]");
            actual = actual.siguiente;
            if (actual != cabeza) {
                sb.append(" -> ");
            }
        } while (actual != cabeza);
        sb.append(" -> (vuelve a [").append(cabeza.titulo).append("])");
        System.out.println(sb.toString());
    }

    /** Reproduce la siguiente canción según el puntero de reproducción. */
    public void reproducirSiguiente() {
        if (estaVacia()) {
            System.out.println(">> No hay canciones para reproducir.");
            return;
        }
        if (reproduccionActual == null) {
            reproduccionActual = cabeza;
        }
        System.out.println("Reproduciendo: " + reproduccionActual.titulo);
        NodoCancion siguienteNodo = reproduccionActual.siguiente;
        if (siguienteNodo == cabeza) {
            System.out.println("   (fin de la playlist alcanzado -> se vuelve automaticamente al inicio)");
        }
        reproduccionActual = siguienteNodo;
    }

    public boolean eliminarPorNombre(String titulo) {
        if (estaVacia()) {
            System.out.println(">> No se puede eliminar: la playlist esta vacia.");
            return false;
        }

        if (tamano == 1) {
            if (cabeza.titulo.equals(titulo)) {
                cabeza = null;
                cola = null;
                reproduccionActual = null;
                tamano = 0;
                return true;
            }
            System.out.println(">> Cancion \"" + titulo + "\" no encontrada.");
            return false;
        }

        if (cabeza.titulo.equals(titulo)) {
            NodoCancion eliminado = cabeza;
            cabeza = cabeza.siguiente;
            cola.siguiente = cabeza;
            if (reproduccionActual == eliminado) {
                reproduccionActual = cabeza;
            }
            tamano--;
            return true;
        }

        NodoCancion anterior = cabeza;
        NodoCancion actual = cabeza.siguiente;
        while (actual != cabeza) {
            if (actual.titulo.equals(titulo)) {
                anterior.siguiente = actual.siguiente;
                if (actual == cola) {
                    cola = anterior;
                }
                if (reproduccionActual == actual) {
                    reproduccionActual = actual.siguiente;
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        System.out.println(">> Cancion \"" + titulo + "\" no encontrada.");
        return false;
    }

    // ----------------------------------------------------------------
    // Métodos auxiliares de lectura y validación de entrada por consola
    // ----------------------------------------------------------------

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(">> Entrada invalida. Debe ingresar un numero entero.");
            }
        }
    }

    private static int leerOpcion(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            int opcion = leerEntero(sc, mensaje);
            if (opcion >= min && opcion <= max) {
                return opcion;
            }
            System.out.println(">> Opcion fuera de rango. Ingrese un valor entre " + min + " y " + max + ".");
        }
    }

    private static String leerTextoNoVacio(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println(">> El titulo no puede estar vacio.");
        }
    }

    // ----------------------------------------------------------------
    // Menú interactivo
    // ----------------------------------------------------------------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MusicPlaylist playlist = new MusicPlaylist();
        int opcion;

        do {
            System.out.println("\n===== MENU: Playlist Musical Circular =====");
            System.out.println("1. Agregar cancion al inicio");
            System.out.println("2. Agregar cancion al final");
            System.out.println("3. Mostrar playlist");
            System.out.println("4. Reproducir siguiente cancion");
            System.out.println("5. Eliminar cancion por nombre");
            System.out.println("6. Salir");

            opcion = leerOpcion(sc, "Seleccione una opcion (1-6): ", 1, 6);

            switch (opcion) {
                case 1: {
                    String titulo = leerTextoNoVacio(sc, "Titulo de la cancion: ");
                    playlist.agregarInicio(titulo);
                    System.out.println(">> \"" + titulo + "\" agregada al inicio.");
                    playlist.mostrarPlaylist();
                    break;
                }
                case 2: {
                    String titulo = leerTextoNoVacio(sc, "Titulo de la cancion: ");
                    playlist.agregarFinal(titulo);
                    System.out.println(">> \"" + titulo + "\" agregada al final.");
                    playlist.mostrarPlaylist();
                    break;
                }
                case 3:
                    playlist.mostrarPlaylist();
                    break;
                case 4:
                    playlist.reproducirSiguiente();
                    break;
                case 5: {
                    if (playlist.estaVacia()) {
                        System.out.println(">> La playlist esta vacia, no hay nada que eliminar.");
                        break;
                    }
                    String titulo = leerTextoNoVacio(sc, "Titulo de la cancion a eliminar: ");
                    playlist.eliminarPorNombre(titulo);
                    playlist.mostrarPlaylist();
                    break;
                }
                case 6:
                    System.out.println("Saliendo del programa...");
                    break;
            }
        } while (opcion != 6);

        sc.close();
    }
}