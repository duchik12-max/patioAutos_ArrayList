package patio_ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class archivos {
	private static final String SEPARADOR = ";";
	 
    private Path archivoVehiculos;
    private Path archivoTransacciones;
    
    public archivos(String rutaVehiculos, String rutaTransacciones) {
        this.archivoVehiculos = Path.of(rutaVehiculos);
        this.archivoTransacciones = Path.of(rutaTransacciones);
    }
    public ArrayList<vehiculo> cargarVehiculos() {
        ArrayList<vehiculo> lista = new ArrayList<>();
        if (!Files.exists(archivoVehiculos)) {
            return lista;
        }
        try {
            for (String linea : Files.readAllLines(archivoVehiculos, StandardCharsets.UTF_8)) {
                String[] partes = linea.split(SEPARADOR);
                if (partes.length == 2) {
                    lista.add(new vehiculo(partes[0], Long.parseLong(partes[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer vehiculos.txt: " + e.getMessage());
        }
        return lista;
    }
 
    //reescribe el archivo completo con los vehiculos que siguen dentro. */
    public void guardarVehiculos(ArrayList<vehiculo> vehiculos) {
        ArrayList<String> lineas = new ArrayList<>();
        for (vehiculo v : vehiculos) {
            lineas.add(v.getMatricula() + SEPARADOR + v.getHoraEntrada());
        }
        try {
            Files.write(archivoVehiculos, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo guardar vehiculos.txt: " + e.getMessage());
        }
    }
 
    // ---------------- Transacciones ----------------
 
    public void registrarTransaccion(vehiculo v, long horas, double valor) {
        String linea = String.join(SEPARADOR,
                v.getMatricula(),
                String.valueOf(v.getHoraEntrada()),
                String.valueOf(v.getHoraSalida()),
                String.valueOf(horas),
                String.format(Locale.US, "%.2f", valor));
        try {
            Files.write(archivoTransacciones, List.of(linea), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("No se pudo guardar la transaccion: " + e.getMessage());
        }
    }
 
    /**
     * Devuelve cada transaccion como arreglo:
     * [0]=matricula, [1]=entrada, [2]=salida, [3]=horas, [4]=valor
     */
    public ArrayList<String[]> leerTransacciones() {
        ArrayList<String[]> lista = new ArrayList<>();
        if (!Files.exists(archivoTransacciones)) {
            return lista;
        }
        try {
            for (String linea : Files.readAllLines(archivoTransacciones, StandardCharsets.UTF_8)) {
                String[] partes = linea.split(SEPARADOR);
                if (partes.length == 5) {
                    lista.add(partes);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer transacciones.txt: " + e.getMessage());
        }
        return lista;
    }
 
    // vaciar el archivo de transacciones  
    public void limpiarTransacciones() {
        try {
            Files.write(archivoTransacciones, new byte[0]);
        } catch (IOException e) {
            System.out.println("No se pudo limpiar transacciones.txt: " + e.getMessage());
        }
    }
}
