package tld.examen.tema1.ad.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import tld.examen.tema1.ad.dao.GenericoDao;
import tld.examen.tema1.ad.infraestructura.dominio.Cliente;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;

public class ClientesDao implements GenericoDao<Cliente, String> {

	private static ClientesDao clientesFicheroTextoDao = null;

	private File file = null;
	private List<Cliente> listaClientes = null;
	private String delimitador = "%";

	/**
	 * Patrón singleton.
	 * 
	 * @param fichero.
	 *            El fichero donde se va a almacenar los datos de los clientes
	 * @return
	 * @throws DaoException
	 */
	public static ClientesDao getInstance(String fichero) throws DaoException {
		if (clientesFicheroTextoDao == null)
			return clientesFicheroTextoDao = new ClientesDao(fichero);
		else
			return clientesFicheroTextoDao;
	}

	public static void main(String args[]) {
		// ClientesFicheroTextoDao.leerXmlyGuardarFicheroTexto();

		try {
			ClientesDao clientesFicheroTextoDao = ClientesDao.getInstance("clientes.txt");
			clientesFicheroTextoDao.mostratClientesdeFicheroTexto();
		} catch (DaoException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 
	 */
	private void mostratClientesdeFicheroTexto() {

		for (Cliente cliente : clientesFicheroTextoDao.getListaClientes()) {
			System.out.println("Cliente almacenado= " + cliente);
		}

	}

	/**
	 * Constructor. Cargamos la lista de clientes desde el fichero pasado como
	 * argumento
	 * 
	 * @param fichero
	 *            El fichero donde se guarda los datos de los clientes
	 * 
	 * @throws DaoException
	 *             Si se produce algún error en la carga del fichero de clientes
	 */
	private ClientesDao(String fichero) throws DaoException {
		super();
		File file = new File(fichero);
		this.file = file;
		if (file.isFile() && file.exists() && file.canWrite()) {

			this.listaClientes = cargarClientes();

		} else
			this.listaClientes = new ArrayList<Cliente>();

		// else

		// throw new DaoException("No se puede modificar el fichero de
		// clientes");

	}

	/**
	 * Volvemos a cargar la lista de clientes desde el fichero
	 * 
	 * @throws DaoException
	 */
	public void recargarClientesDesdeFichero() throws DaoException {
		this.listaClientes = cargarClientes();
	}

	/**
	 * Leemos los clientes almacenados
	 * 
	 * @return La lista de {@link Cliente}
	 * @throws DaoException
	 */
	private List<Cliente> cargarClientes() throws DaoException {
		LeerFicheroConScanner lectorClientes = new LeerFicheroConScanner(this.file, delimitador);
		return lectorClientes.leerFichero();
	}

	/*
	 * Añadimos el cliente nuevo a la lista que tenemos en memoria y almacenamos
	 * en disco (non-Javadoc)
	 * 
	 * @see tld.examen.tema1.ad.dao.GenericoDao#crear(java.lang.Object)
	 */
	@Override
	public String crear(Cliente clienteNuevo) throws DaoException {
		String nifCliente = añadirCliente(clienteNuevo);
		almacenar(listaClientes);
		return nifCliente;
	}

	private String añadirCliente(Cliente nuevoObjeto) throws DaoException {
		listaClientes.add(nuevoObjeto);
		return nuevoObjeto.getNif();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tld.examen.tema1.ad.dao.GenericoDao#leer(java.io.Serializable)
	 */
	@Override
	public Cliente leer(String dni) {
		return buscar(dni);

	}

	@Override
	public void borrar(Cliente objetoAlmacenado) throws DaoException {
		borrarCliente(objetoAlmacenado);
		almacenar(listaClientes);
	}

	private void borrarCliente(Cliente nuevoObjeto) throws DaoException {
		listaClientes.remove(nuevoObjeto);

	}

	/**
	 * Buscamos un cliente si existe en la lista de memoria de clientes
	 * 
	 * @param clienteABuscar
	 * @return Si se ha encontrado el cliente o no
	 */
	public boolean existe(Cliente clienteABuscar) {

		return listaClientes.contains(clienteABuscar);

	}

	/**
	 * Buscamos un cliente por nif. Nif es clave.
	 * 
	 * @param nif
	 *            El nif del cliente a buscar
	 * @return El cliente encontrado por nif
	 */
	public Cliente buscar(String nif) {

		for (Cliente cliente : listaClientes) {
			if (nif.compareToIgnoreCase(cliente.getNif()) == 0)
				return cliente;
		}
		return null;
	}

	/**
	 * Buscamos un cliente por CCC. En este ejercicio el ccc es único para por
	 * cliente.
	 * 
	 * @param ccc
	 *            La cuenta del cliente
	 * @return El cliente encontrado por ccc
	 */
	public Cliente buscarPorCCC(String ccc) {

		for (Cliente cliente : listaClientes) {
			if (ccc.compareToIgnoreCase(cliente.getCcc()) == 0)
				return cliente;
		}
		return null;
	}

	/**
	 * Actualizamos el saldo de un cliente
	 * 
	 * @param ccc
	 *            La cuenta del cliente
	 * @param imputacion
	 *            El movimiento en la cuenta del cliente
	 * @throws DaoException
	 *             Si se produce algún error
	 */
	public void actualizarSaldo(String ccc, BigDecimal imputacion) throws DaoException {
		Cliente cliente = buscarPorCCC(ccc);
		// Imputamos el nuevo movimiento en la cuenta del cliente
		cliente.inputarMovimiento(imputacion);
		// Actualizamos el cliente en memoria y en el fichero
		actualizar(cliente);

	}

	/**
	 * Almacena la lista del Clientes en un fichero de texto El formato del
	 * fichero de texto es con el limitador de campos en %:
	 * nif%nombreCliente%domicilio%ccc%saldo
	 * 
	 * @param listaObjetos
	 *            La lista de clientes a alamcenar
	 * @throws DaoException
	 *             Si se produce algún error almacenando en el fichero
	 */
	private void almacenar(List<Cliente> listaObjetos) throws DaoException {

		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8")))) {
			for (Cliente cliente : listaObjetos) {
				StringBuilder sb = new StringBuilder();
				Formatter formateador = new Formatter(new Locale("es", "es"));
				sb.append(cliente.getNif()).append(delimitador).append(cliente.getNombre()).append(delimitador)
						.append(cliente.getDomicilio()).append(delimitador).append(cliente.getCcc()).append(delimitador)
						.append(formateador.format("%.2f", cliente.getBalance()));
				formateador.close();
				out.println(sb.toString());

			}

		} catch (Exception e) {
			throw new DaoException("No se ha podido almacenar los clientes", e);
		}

	}

	/**
	 * Clase para leer el fichero con los clientes en el formato
	 * nif%nombreCliente%domicilio%ccc%saldo El saldo puede terner un + para
	 * números positivos
	 * 
	 * @author sergio5
	 *
	 */
	private class LeerFicheroConScanner {

		private final File fFilePath;
		private final Charset ENCODING = StandardCharsets.UTF_8;
		private String delimitador = null;

		

		public LeerFicheroConScanner(File aFileName, String delimitador) {
			fFilePath = aFileName;
			this.delimitador = delimitador;
		}

		public final List<Cliente> leerFichero() throws DaoException {

			List<Cliente> listaClientes = new ArrayList<>();
			try (Scanner scanner = new Scanner(fFilePath, ENCODING.name());) {

				while (scanner.hasNextLine()) {
					Cliente cliente = tratarLinea(scanner.nextLine());
					listaClientes.add(cliente);

				}

			} catch (IOException e) {
				throw new DaoException("El fichero no tiene el formato correcto", e);
			}
			return listaClientes;
		}

		protected Cliente tratarLinea(String aLine) throws DaoException {
			// Parseamos cada línea 
			try (Scanner scanner = new Scanner(aLine)) {
				scanner.useLocale(new Locale("es", "ES"));
				scanner.useDelimiter(delimitador);

				if (scanner.hasNext()) {
					//Cada línea tiene un cierto formato
					//	  nif%nombreCliente%domicilio%ccc%saldo El saldo puede terner un + para
					// números positivos
					String nif = scanner.next();
					String nombre = scanner.next();
					String domicilio = scanner.next();
					String ccc = scanner.next();
					BigDecimal balance = scanner.nextBigDecimal();

					return new Cliente(nif, nombre, domicilio, ccc, balance);
				} else
					throw new DaoException("El fichero no tiene el formato correcto");
			} catch (Exception e) {
				throw new DaoException("El fichero no tiene el formato correcto", e);
			}
		}

	}

	@Override
	public void actualizar(Cliente clienteModificado) throws DaoException {

		actualizarCliente(clienteModificado);
		almacenar(listaClientes);

	}

	/**
	 * Actualizamos la información del cliente en la lista de clientes en
	 * memoria
	 * 
	 * @param clienteModificado
	 *            El cliente modificado
	 * @throws DaoException
	 *             Si se produce algún error almacenando en el fichero
	 */
	private void actualizarCliente(Cliente clienteModificado) throws DaoException {

		for (Cliente cliente : listaClientes) {
			if (cliente.getNif().compareToIgnoreCase(clienteModificado.getNif()) == 0)
				cliente.actualizar(clienteModificado);
		}

	}

	/**
	 * Dada una lista de clientes actualizamos o creamos clientes en nuestro
	 * sistema
	 * 
	 * @param listaClientesModificado.
	 *            Los clientes nuevos o a actualizar
	 * @throws DaoException
	 *             Si se produce algún error almacenando en el fichero
	 */
	public void añadirListaClientes(List<Cliente> listaClientesModificado) throws DaoException {

		for (Cliente cliente : listaClientesModificado) {
			Cliente clienteExistente = buscar(cliente.getNif());
			if (clienteExistente == null)
				crear(cliente);
			else
				actualizar(cliente);

		}
		

	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

}
