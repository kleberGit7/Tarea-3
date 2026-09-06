package ejercicio4;

import java.util.Scanner;

class NodoPersona {
    int id;
    NodoPersona siguiente;

    NodoPersona(int id) {
        this.id = id;
    }
}

public class JosephusProblem {

    /** Resuelve el problema de Josephus para n personas y salto k. */
    public static int resolverJosephus(int n, int k) {
        System.out.println("\n--- Josephus con n = " + n + ", k = " + k + " ---");

        // Construir la lista circular con personas numeradas de 1 a n
        NodoPersona cabeza = new NodoPersona(1);
        NodoPersona ultimo = cabeza;
        for (int i = 2; i <= n; i++) {
            NodoPersona nuevo = new NodoPersona(i);
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        ultimo.siguiente = cabeza; // cierre circular

        NodoPersona anterior = ultimo; // nodo previo a 'actual'
        NodoPersona actual = cabeza;
        int restantes = n;

        StringBuilder orden = new StringBuilder();

        while (restantes > 1) {
            // Avanzar k-1 pasos para llegar a la k-esima persona
            for (int i = 1; i < k; i++) {
                anterior = actual;
                actual = actual.siguiente;
            }

            orden.append(actual.id).append(" ");

            // Eliminar 'actual' de la lista circular
            anterior.siguiente = actual.siguiente;
            actual = actual.siguiente;

            restantes--;
        }

        String ordenTexto = orden.toString().trim();
        System.out.println("Orden de eliminacion: " + (ordenTexto.isEmpty() ? "(sin eliminaciones)" : ordenTexto));
        System.out.println("Superviviente final: " + actual.id);
        return actual.id;
    }

    // ----------------------------------------------------------------
    // Metodos auxiliares de lectura y validacion de entrada por consola
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

    /** Lee un entero que debe ser mayor o igual a 1 (usado para n y k). */
    private static int leerEnteroPositivo(Scanner sc, String mensaje) {
        while (true) {
            int valor = leerEntero(sc, mensaje);
            if (valor >= 1) {
                return valor;
            }
            System.out.println(">> Debe ingresar un numero entero mayor o igual a 1.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENU: Problema de Josephus =====");
            System.out.println("1. Resolver con n y k ingresados por teclado");
            System.out.println("2. Ejecutar casos de prueba predefinidos (n=5,k=2 y n=7,k=3)");
            System.out.println("3. Salir");

            opcion = leerOpcion(sc, "Seleccione una opcion (1-3): ", 1, 3);

            switch (opcion) {
                case 1: {
                    int n = leerEnteroPositivo(sc, "Numero de personas (n): ");
                    int k = leerEnteroPositivo(sc, "Cada cuantas personas se elimina (k): ");
                    resolverJosephus(n, k);
                    break;
                }
                case 2:
                    resolverJosephus(5, 2);
                    resolverJosephus(7, 3);
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
            }
        } while (opcion != 3);

        sc.close();
    }
}