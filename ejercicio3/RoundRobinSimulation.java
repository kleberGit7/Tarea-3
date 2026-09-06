package ejercicio3;

import java.util.Scanner;

class Proceso {
    String nombre;
    int tiempoRestante;

    Proceso(String nombre, int tiempoRestante) {
        this.nombre = nombre;
        this.tiempoRestante = tiempoRestante;
    }
}

class NodoProceso {
    Proceso proceso;
    NodoProceso siguiente;

    NodoProceso(Proceso proceso) {
        this.proceso = proceso;
    }
}

public class RoundRobinSimulation {

    private NodoProceso cabeza;
    private NodoProceso cola;
    private int tamano;

    // Estado de la ronda Round-Robin (persiste entre turnos ejecutados desde el
    // menu)
    private NodoProceso actual;
    private NodoProceso anterior;
    private int quantum = 2;
    private int turno = 1;

    public void agregarProceso(String nombre, int tiempo) {
        NodoProceso nuevo = new NodoProceso(new Proceso(nombre, tiempo));
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
            nuevo.siguiente = cabeza;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
            cola.siguiente = cabeza;
        }
        tamano++;
    }

    public void configurarQuantum(int q) {
        this.quantum = q;
    }

    public int getQuantum() {
        return quantum;
    }

    public boolean hayProcesos() {
        return cabeza != null;
    }

    public void imprimirEstado() {
        if (cabeza == null) {
            System.out.println("   Cola de procesos: [ vacia ]");
            return;
        }
        StringBuilder sb = new StringBuilder("   Cola de procesos: ");
        NodoProceso nodo = cabeza;
        do {
            sb.append(nodo.proceso.nombre).append("(").append(nodo.proceso.tiempoRestante).append(")");
            if (nodo == actual) {
                sb.append("*"); // marca el proceso que le toca ejecutar en el proximo turno
            }
            nodo = nodo.siguiente;
            if (nodo != cabeza) {
                sb.append(" -> ");
            }
        } while (nodo != cabeza);
        System.out.println(sb.toString());
    }

    /** Ejecuta un unico turno de Round-Robin. Retorna false si no hay procesos. */
    public boolean ejecutarTurno() {
        if (cabeza == null) {
            System.out.println(">> No hay procesos en la cola.");
            return false;
        }
        if (actual == null) {
            actual = cabeza;
            anterior = cola;
        }

        Proceso p = actual.proceso;
        int ejecutado = Math.min(quantum, p.tiempoRestante);
        p.tiempoRestante -= ejecutado;

        System.out.println("Turno " + turno + ": ejecutando " + p.nombre
                + " durante " + ejecutado + " unidades (tiempo restante: " + p.tiempoRestante + ")");

        if (p.tiempoRestante <= 0) {
            System.out.println("   -> " + p.nombre + " ha terminado y se elimina de la lista.");
            NodoProceso siguienteNodo = actual.siguiente;

            if (tamano == 1) {
                cabeza = null;
                cola = null;
                actual = null;
                anterior = null;
            } else {
                anterior.siguiente = siguienteNodo;
                if (actual == cabeza) {
                    cabeza = siguienteNodo;
                }
                if (actual == cola) {
                    cola = anterior;
                }
                actual = siguienteNodo; // 'anterior' sigue siendo valido para el proximo turno
            }
            tamano--;
        } else {
            System.out.println("   -> " + p.nombre + " vuelve al final del ciclo.");
            anterior = actual;
            actual = actual.siguiente;
        }

        imprimirEstado();
        turno++;
        return true;
    }

    /** Ejecuta turnos hasta que todos los procesos terminen. */
    public void ejecutarSimulacionCompleta() {
        if (cabeza == null) {
            System.out.println(">> No hay procesos en la cola.");
            return;
        }
        while (cabeza != null) {
            ejecutarTurno();
        }
        System.out.println("Todos los procesos han finalizado.");
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

    private static int leerEnteroPositivo(Scanner sc, String mensaje) {
        while (true) {
            int valor = leerEntero(sc, mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println(">> Debe ingresar un numero entero mayor que 0.");
        }
    }

    private static String leerTextoNoVacio(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println(">> El nombre no puede estar vacio.");
        }
    }

    // ----------------------------------------------------------------
    // Menu interactivo
    // ----------------------------------------------------------------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RoundRobinSimulation simulador = new RoundRobinSimulation();
        int opcion;

        do {
            System.out.println(
                    "\n===== MENU: Simulacion Round-Robin (quantum actual: " + simulador.getQuantum() + ") =====");
            System.out.println("1. Agregar proceso");
            System.out.println("2. Mostrar cola de procesos");
            System.out.println("3. Configurar quantum");
            System.out.println("4. Ejecutar siguiente turno");
            System.out.println("5. Ejecutar simulacion completa");
            System.out.println("6. Salir");

            opcion = leerOpcion(sc, "Seleccione una opcion (1-6): ", 1, 6);

            switch (opcion) {
                case 1: {
                    String nombre = leerTextoNoVacio(sc, "Nombre del proceso: ");
                    int tiempo = leerEnteroPositivo(sc, "Tiempo de ejecucion requerido: ");
                    simulador.agregarProceso(nombre, tiempo);
                    System.out.println(">> Proceso " + nombre + " agregado con tiempo " + tiempo + ".");
                    simulador.imprimirEstado();
                    break;
                }
                case 2:
                    simulador.imprimirEstado();
                    break;
                case 3: {
                    int q = leerEnteroPositivo(sc, "Nuevo valor de quantum: ");
                    simulador.configurarQuantum(q);
                    System.out.println(">> Quantum actualizado a " + q + ".");
                    break;
                }
                case 4:
                    simulador.ejecutarTurno();
                    break;
                case 5:
                    simulador.ejecutarSimulacionCompleta();
                    break;
                case 6:
                    System.out.println("Saliendo del programa...");
                    break;
            }
        } while (opcion != 6);

        sc.close();
    }
}