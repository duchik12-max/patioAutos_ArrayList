package patio_ArrayList;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class main {
	private static Scanner teclado = new Scanner(System.in);
    private static DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
 
    public static void main(String[] args) {
        parqueadero parqueadero = new parqueadero(new archivos("vehiculos.txt", "transacciones.txt"));
 
        int opcion;
        do {
            mostrarMenu(parqueadero);
            opcion = leerOpcion();
            switch (opcion) {
                case 1 -> ingresoVehiculo(parqueadero);
                case 2 -> salidaVehiculo(parqueadero);
                case 3 ->System.out.println("Vehiculos dentro: "+parqueadero.cantidadDentro());
                case 4 -> mostrarCaja(parqueadero);
                case 5 -> finalizarDia(parqueadero);
                case 6 -> System.out.println("Fin del programa");
            }
        } while (opcion != 6);
    }
 
    // Menu
    private static void mostrarMenu(parqueadero p) {
        System.out.println("1. Ingreso de vehiculo");
        System.out.println("2. Salida de vehiculo");
        System.out.println("3. Ver cantidad de vehiculos");
        System.out.println("4. Mostrar caja");
        System.out.println("5. Finalizar dia");
        System.out.println("6. Salir");
    }
 
    private static void ingresoVehiculo(parqueadero p) {
        String matricula = leerMatricula();
        vehiculo v = p.ingresar(matricula);
        if (v == null) {
            System.out.println("Error: el vehiculo " + matricula + " ya se encuentra en el parqueadero.");
            return;
        }
        System.out.println("Ingreso registrado: " + v.getMatricula() + " a las " + formatear(v.getHoraEntrada()));
    }
 
    private static void salidaVehiculo(parqueadero p) {
        String matricula = leerMatricula();
        vehiculo v = p.salir(matricula);
        if (v == null) {
            System.out.println("No hay ningun vehiculo con la matricula " + matricula );
            return;
        }
        long horas = parqueadero.calcularHoras(v);
        double valor = parqueadero.calcularValor(horas);
        System.out.println("----- Recibo -----");
        System.out.println("Matricula: " + v.getMatricula());
        System.out.println("Entrada:   " + formatear(v.getHoraEntrada()));
        System.out.println("Salida:    " + formatear(v.getHoraSalida()));
        System.out.println("Horas a cobrar: " + horas);
        System.out.println("Total a pagar:  " + dinero(valor));
    }
 
    private static void mostrarCaja(parqueadero p) {
        ArrayList<String[]> transacciones = p.transacciones();
        if (transacciones.isEmpty()) {
            System.out.println("Todavia no hay transacciones hoy.");
            return;
        }
        System.out.println("----- Caja del dia -----");
        for (String[] t : transacciones) {
            System.out.println(t[0] + " | entrada " + formatear(Long.parseLong(t[1]))
                    + " | salida " + formatear(Long.parseLong(t[2]))
                    + " | " + t[3] + " h | $" + t[4]);
        }
        System.out.println("Transacciones: " + transacciones.size());
        System.out.println("TOTAL: " + dinero(p.totalCaja()));
    }
 
    private static void finalizarDia(parqueadero p) {
        System.out.println("Total del dia: " + dinero(p.totalCaja()));
        System.out.print("Se borraran las transacciones del dia. Confirmar? (s/n): ");
        String respuesta = leerLinea().trim().toLowerCase();
        if (respuesta.equals("s")) {
            p.finalizarDia();
            System.out.println("Dia finalizado. Caja en cero.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
 //aqui
    private static int leerOpcion() {
        while (true) {
            System.out.print("Elija una opcion (1-6): ");
            String texto = leerLinea().trim();
            if (texto.matches("[1-6]")) {
                return Integer.parseInt(texto); 
            }
            System.out.println("Opcion invalida. Escriba un numero del 1 al 6.");
        }
    }
 
    //repite hasta recibir una matricula no vacia de 3 a 10 letras, numeros o guiones
    private static String leerMatricula() {
        while (true) {
            System.out.print("Matricula: ");
            String matricula = leerLinea().trim().toUpperCase();
            if (matricula.matches("[A-Z0-9-]{3,10}")) {
                return matricula;
            }
            System.out.println("Matricula invalida. Use de 3 a 10 caracteres (letras, numeros o guion).");
        }
    }
 
    private static String leerLinea() {
        if (!teclado.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada finalizada. Cerrando.");
            System.exit(0);
        }
        return teclado.nextLine();
    }
 
    // formato de la hora
 
    private static String formatear(long segundos) {
        return FORMATO.format(Instant.ofEpochSecond(segundos));
    }
 
    private static String dinero(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }
}
