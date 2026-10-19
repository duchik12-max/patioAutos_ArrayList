package patio_ArrayList;

import java.time.Instant;
import java.util.ArrayList;

public class parqueadero {
	public static final double TARIFA_POR_HORA = 0.50;
	 
    private archivos gestor;
    private ArrayList<vehiculo> dentro;   
 
    public parqueadero(archivos gestor) {
        this.gestor = gestor;
        this.dentro = gestor.cargarVehiculos();
    }
 
    //Registra la entrada
    public vehiculo ingresar(String matricula) {
        if (buscar(matricula) != null) {
            return null;
        }
        vehiculo v = new vehiculo(matricula, Instant.now().getEpochSecond());
        dentro.add(v);
        gestor.guardarVehiculos(dentro);
        return v;
    }
 
    /**
     * Registra la salida, guarda la transaccion y retira el vehiculo.
     * Devuelve el vehiculo con su hora de salida, o null si esa matricula no esta dentro.
     */
    public vehiculo salir(String matricula) {
        vehiculo v = buscar(matricula);
        if (v == null) {
            return null;
        }
        v.setHoraSalida(Instant.now().getEpochSecond());
        long horas = calcularHoras(v);
        gestor.registrarTransaccion(v, horas, calcularValor(horas));
        dentro.remove(v);
        gestor.guardarVehiculos(dentro);
        return v;
    }
 
    // Horas a cobrar
    public static long calcularHoras(vehiculo v) {
        long segundos = v.getHoraSalida() - v.getHoraEntrada();
        long horas = (segundos + 3599) / 3600; 
        return Math.max(1, horas);
    }
 
    public static double calcularValor(long horas) {
        return horas * TARIFA_POR_HORA;
    }
 
    //suma los valores leyendo el archivo, asi el total nunca queda desincronizado
    public double totalCaja() {
        double total = 0;
        for (String[] t : gestor.leerTransacciones()) {
            total += Double.parseDouble(t[4]);
        }
        return total;
    }
 
    public ArrayList<String[]> transacciones() {
        return gestor.leerTransacciones();
    }
 
    public int cantidadDentro() {
        return dentro.size();
    }
 
    //vacia las transacciones del dia no afecta a los que aun no salieron
    public void finalizarDia() {
        gestor.limpiarTransacciones();
    }
 
    private vehiculo buscar(String matricula) {
        for (vehiculo v : dentro) {
            if (v.getMatricula().equals(matricula)) {
                return v;
            }
        }
        return null;
    }

}
