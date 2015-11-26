package tld.examen.tema1.ad.infraestructura.dominio;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.Locale;

public class Cliente implements Comparable<Cliente> {

	

	private String nif;
	
	private String nombre;
	private String domicilio;
	private String ccc;
	private BigDecimal balance=new BigDecimal("0.00");
	

	public Cliente(String nif, String nombre, String domicilio, String ccc, BigDecimal balance) {
		super();
		this.nif = nif;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.ccc = ccc;
		this.balance = balance;
	}
	public Cliente(Cliente cliente) {
		super();
		this.nif = cliente.getNif();
		this.nombre = cliente.getNombre();
		this.domicilio =  cliente.getDomicilio();
		this.ccc =  cliente.getCcc();
		this.balance =  cliente.getBalance();
	}
	
	public void actualizar(Cliente cliente) {
		this.nif = cliente.getNif();
		this.nombre = cliente.getNombre();
		this.domicilio =  cliente.getDomicilio();
		this.ccc =  cliente.getCcc();
		this.balance =  cliente.getBalance();
	}
	
	
	
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCcc() {
		return ccc;
	}

	public void setCcc(String ccc) {
		this.ccc = ccc;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		StringBuilder cadena=new StringBuilder();
		Formatter formateador=new Formatter(new Locale("es", "es"));
		
		cadena.append(	
		 "Cliente [nif=" + nif + ", nombre=" + nombre + ", domicilio=" + domicilio + ", ccc=" + ccc + ", balance="+ formateador.format("%.2f",balance) + "]");
		formateador.close();
		return cadena.toString();
		
	}






	@Override
	public int compareTo(Cliente o) {
		
		return nif.compareTo(o.nif);
		
	
	
	}
	public void inputarMovimiento(BigDecimal movimiento) {
		setBalance(getBalance().add(movimiento));
	}
	
	

	
	
}
