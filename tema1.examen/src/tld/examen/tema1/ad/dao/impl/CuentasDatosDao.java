package tld.examen.tema1.ad.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import tld.examen.tema1.ad.infraestructura.dominio.MovimientoAgrupadosPorCuenta;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;



/**
 * Clase que va tratar el almacenamientos en fichero de los movimientos 
 * Los movimientos se agrupan por años. Por cada año se almacenan el total del saldo por cada cuentas. 
 * 
 * @author sergio5
 *
 */
public class CuentasDatosDao {

	private static CuentasDatosDao cuentasDatosDao = null;

	
	// La lista de movimientos agrupados por año. 
	private HashMap<String, MovimientoAgrupadosPorCuenta> movimientosPorAño;
	private File file;

	private CuentasDatosDao(String fichero) throws DaoException {
		file = new File(fichero);
		if (file.exists()) {
			this.movimientosPorAño = cargarDatos();
		} else
			this.movimientosPorAño = new HashMap<String, MovimientoAgrupadosPorCuenta>();

	}

	/**
	 * Patrón Singleton. 
	 * 
	 * @param fichero El fichero donde se va a guardar el objeto movimientosPorAño
	 * @return Un Objeto CuentasDatosDao
	 * @throws DaoException Si se produce algún error en el acceso al fichero 
	 */
	public static CuentasDatosDao getInstance(String fichero) throws DaoException {
		if (cuentasDatosDao == null)
			cuentasDatosDao = new CuentasDatosDao(fichero);
		return cuentasDatosDao;

	}

	/**
	 * Cargamos en movimientosPorAño (en memoria) los datos de los movimientos por año almacenados en el fichero 
	 * 
	 * @return El objeto almacenado en en el fichero  
	 * @throws DaoException  Si se produce algún error en el acceso al fichero 
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, MovimientoAgrupadosPorCuenta> cargarDatos() throws DaoException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {

			ObjectInputStream input = new ObjectInputStream(fileInputStream);
			return (HashMap<String, MovimientoAgrupadosPorCuenta>) input.readObject();

		} catch (ClassNotFoundException e) {
			throw new DaoException("La clase al hacer cast no existe", e);
		} catch (FileNotFoundException e) {
			throw new DaoException("El fichero de datos no existe ", e);
		} catch (IOException e) {
			throw new DaoException("Error al almacenar los datos de movimientos", e);
		}

	}

	/**
	 * Almacenamos en el fichero los datos que se encuentran en la memoria
	 * @throws DaoException  Si se produce algún error en el acceso al fichero 
	 * 
	 */
	public void salvarDatos() throws DaoException {
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

			ObjectOutputStream output = new ObjectOutputStream(fileOutputStream);
			output.writeObject(movimientosPorAño);

		} catch (FileNotFoundException e) {
			throw new DaoException("El fichero de datos no existe ", e);
		} catch (IOException e) {
			throw new DaoException("Error al almacenar los datos de movimientos", e);
		}

	}

	/**
	 * Realizamos una imputación en una cuenta en un año
	 * 
	 * @param año El año en que se produce la imputación o movimiento
	 * @param ccc La cuenta en donde realizar la imputación  
	 * @param imputacion La cantidad imputada
	 */
	public void añadirSaldoACuenta(String año, String ccc, BigDecimal imputacion) {

		if (movimientosPorAño.containsKey(año))
			movimientosPorAño.get(año).añadirSaldoACuenta(ccc, imputacion);
		else {
			movimientosPorAño.put(año, new MovimientoAgrupadosPorCuenta());
			movimientosPorAño.get(año).añadirSaldoACuenta(ccc, imputacion);
		}

	}

	
	

}
