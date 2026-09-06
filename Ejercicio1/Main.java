package Ejercicio1;

import java.util.Scanner;

class Nodo<T> {
    public T dato;
    public Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}

class ListaSimpleCircular<T> {
    private Nodo<T> cabeza;
    private int cantidad;

    public ListaSimpleCircular() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int contar() {
        return cantidad;
    }

    public void insertarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != cabeza) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            actual.siguiente = cabeza;
        }
        cantidad++;
    }

    public void insertarAlFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != cabeza) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
            nuevo.siguiente = cabeza;
        }
        cantidad++;
    }

    public void mostrar() {
        if (estaVacia()) {
            System.out.println("La lista esta vacia.");
            return;
        }
        Nodo<T> actual = cabeza;
        System.out.print("Elementos (circular): ");
        do {
            System.out.print("[" + actual.dato + "] -> ");
            actual = actual.siguiente;
        } while (actual != cabeza);
        System.out.println("(vuelve a la cabeza)");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ListaSimpleCircular<String> lista = new ListaSimpleCircular<>();
        int opcion = 0;

        do {
            System.out.println("\n==================================");
            System.out.println("   MENU LISTA SIMPLE CIRCULAR");
            System.out.println("==================================");
            System.out.println("1. Insertar al inicio");
            System.out.println("2. Insertar al final");
            System.out.println("3. Mostrar todos los elementos");
            System.out.println("4. Verificar si está vacia");
            System.out.println("5. Numero de elementos");
            System.out.println("6. Salir");
            System.out.print("Elija una opcion: ");

            String entrada = teclado.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el elemento a insertar al inicio: ");
                    String datoInicio = teclado.nextLine().trim();
                    if (!datoInicio.isEmpty()) {
                        lista.insertarAlInicio(datoInicio);
                        System.out.println("Elemento insertado correctamente al inicio.");
                    } else {
                        System.out.println("El elemento no puede estar vacio.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el elemento a insertar al final: ");
                    String datoFinal = teclado.nextLine().trim();
                    if (!datoFinal.isEmpty()) {
                        lista.insertarAlFinal(datoFinal);
                        System.out.println("Elemento insertado correctamente al final.");
                    } else {
                        System.out.println("El elemento no puede estar vacio.");
                    }
                    break;
                case 3:
                    lista.mostrar();
                    break;
                case 4:
                    if (lista.estaVacia()) {
                        System.out.println("Si, la lista está vacia.");
                    } else {
                        System.out.println("No, la lista contiene elementos.");
                    }
                    break;
                case 5:
                    System.out.println("Numero de elementos en la lista: " + lista.contar());
                    break;
                case 6:
                    System.out.println("Finalizando el programa...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo con un número del 1 al 6.");
            }
        } while (opcion != 6);

        teclado.close();
    }
}