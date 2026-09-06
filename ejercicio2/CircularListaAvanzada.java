package ejercicio2;

import java.util.Scanner;

class NodoAvanzado {
    int dato;
    NodoAvanzado siguiente;

    NodoAvanzado(int dato) {
        this.dato = dato;
    }
}

public class CircularListaAvanzada {

    private NodoAvanzado cabeza;
    private NodoAvanzado cola;
    private int tamano;

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int contarElementos() {
        return tamano;
    }

    public void insertarEnPosicion(int valor, int posicion) {
        NodoAvanzado nuevo = new NodoAvanzado(valor);

        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
            nuevo.siguiente = cabeza;
            tamano++;
            return;
        }

        if (posicion <= 1) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            cola.siguiente = cabeza;
        } else if (posicion >= tamano + 1) {
            cola.siguiente = nuevo;
            cola = nuevo;
            cola.siguiente = cabeza;
        } else {
            NodoAvanzado anterior = cabeza;
            for (int i = 1; i < posicion - 1; i++) {
                anterior = anterior.siguiente;
            }
            nuevo.siguiente = anterior.siguiente;
            anterior.siguiente = nuevo;
        }
        tamano++;
    }

    public boolean eliminarPorPosicion(int posicion) {
        if (estaVacia()) {
            System.out.println(">> No se puede eliminar: la lista esta vacia.");
            return false;
        }
        if (posicion < 1 || posicion > tamano) {
            System.out.println(">> Posicion invalida: " + posicion + " (rango valido: 1 a " + tamano + ")");
            return false;
        }

        if (tamano == 1) {
            cabeza = null;
            cola = null;
            tamano = 0;
            return true;
        }

        if (posicion == 1) {
            cabeza = cabeza.siguiente;
            cola.siguiente = cabeza;
        } else {
            NodoAvanzado anterior = cabeza;
            for (int i = 1; i < posicion - 1; i++) {
                anterior = anterior.siguiente;
            }
            NodoAvanzado eliminado = anterior.siguiente;
            anterior.siguiente = eliminado.siguiente;
            if (eliminado == cola) {
                cola = anterior;
            }
        }
        tamano--;
        return true;
    }

    public boolean eliminarPorValor(int valor) {
        if (estaVacia()) {
            System.out.println(">> No se puede eliminar: la lista esta vacia.");
            return false;
        }

        if (tamano == 1) {
            if (cabeza.dato == valor) {
                cabeza = null;
                cola = null;
                tamano = 0;
                return true;
            }
            System.out.println(">> Valor " + valor + " no encontrado.");
            return false;
        }

        if (cabeza.dato == valor) {
            cabeza = cabeza.siguiente;
            cola.siguiente = cabeza;
            tamano--;
            return true;
        }

        NodoAvanzado anterior = cabeza;
        NodoAvanzado actual = cabeza.siguiente;
        while (actual != cabeza) {
            if (actual.dato == valor) {
                anterior.siguiente = actual.siguiente;
                if (actual == cola) {
                    cola = anterior;
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        System.out.println(">> Valor " + valor + " no encontrado.");
        return false;
    }

    /** Imprime la lista completa. */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista: [ vacia ]");
            return;
        }
        StringBuilder sb = new StringBuilder("Lista: ");
        NodoAvanzado actual = cabeza;
        do {
            sb.append(actual.dato);
            actual = actual.siguiente;
            if (actual != cabeza) {
                sb.append(" -> ");
            }
        } while (actual != cabeza);
        sb.append(" -> (").append(cabeza.dato).append(")");
        System.out.println(sb.toString());
    }

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

    /** Lee un entero que debe ser mayor o igual a 1 (usado para posiciones). */
    private static int leerEnteroPositivo(Scanner sc, String mensaje) {
        while (true) {
            int valor = leerEntero(sc, mensaje);
            if (valor >= 1) {
                return valor;
            }
            System.out.println(">> Debe ingresar un numero entero mayor o igual a 1.");
        }
    }

    // ----------------------------------------------------------------
    // Menu interactivo
    // ----------------------------------------------------------------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CircularListaAvanzada lista = new CircularListaAvanzada();
        int opcion;

        do {
            System.out.println("\n===== MENU: Insercion/Eliminacion controlada =====");
            System.out.println("1. Insertar en una posicion especifica");
            System.out.println("2. Eliminar por posicion");
            System.out.println("3. Eliminar por valor");
            System.out.println("4. Mostrar lista");
            System.out.println("5. Salir");

            opcion = leerOpcion(sc, "Seleccione una opcion (1-5): ", 1, 5);

            switch (opcion) {
                case 1: {
                    int valor = leerEntero(sc, "Valor a insertar: ");
                    int posicion = leerEnteroPositivo(sc, "Posicion donde insertar (1 = inicio): ");
                    System.out.print("Antes:   ");
                    lista.imprimir();
                    lista.insertarEnPosicion(valor, posicion);
                    System.out.print("Despues: ");
                    lista.imprimir();
                    break;
                }
                case 2: {
                    if (lista.estaVacia()) {
                        System.out.println(">> La lista esta vacia, no hay nada que eliminar.");
                        break;
                    }
                    int posicion = leerEnteroPositivo(sc, "Posicion a eliminar: ");
                    System.out.print("Antes:   ");
                    lista.imprimir();
                    lista.eliminarPorPosicion(posicion);
                    System.out.print("Despues: ");
                    lista.imprimir();
                    break;
                }
                case 3: {
                    if (lista.estaVacia()) {
                        System.out.println(">> La lista esta vacia, no hay nada que eliminar.");
                        break;
                    }
                    int valor = leerEntero(sc, "Valor a eliminar: ");
                    System.out.print("Antes:   ");
                    lista.imprimir();
                    lista.eliminarPorValor(valor);
                    System.out.print("Despues: ");
                    lista.imprimir();
                    break;
                }
                case 4:
                    lista.imprimir();
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
            }
        } while (opcion != 5);

        sc.close();
    }
}