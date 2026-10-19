package patio_ArrayList;

public class vehiculo {
	private String matricula;
	private long horaEntrada;
	private long horaSalida;
	public vehiculo(String matricula, long horaEntrada) {
		super();
		this.matricula = matricula;
		this.horaEntrada = horaEntrada;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public long getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(long horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public long getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(long horaSalida) {
		this.horaSalida = horaSalida;
	}
	public boolean haSalido() {
		return horaSalida!=0;
	}
	
	

}
