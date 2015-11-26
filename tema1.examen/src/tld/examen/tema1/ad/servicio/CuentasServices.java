package tld.examen.tema1.ad.servicio;

import java.io.File;
import java.util.List;

import tld.examen.tema1.ad.dao.impl.ClientesDao;
import tld.examen.tema1.ad.dao.impl.CuentasDatosDao;
import tld.examen.tema1.ad.dao.impl.ImportarClientesXMLDao;
import tld.examen.tema1.ad.dao.impl.ImportarMovimientosXMLDao;
import tld.examen.tema1.ad.infraestructura.dominio.Cliente;
import tld.examen.tema1.ad.infraestructura.dominio.Movimiento;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;
import tld.examen.tema1.ad.infraestructura.excepciones.ServiceException;

public class CuentasServices {

	private static CuentasServices cuentasServices;

	private ClientesDao clientesDao;
	private CuentasDatosDao cuentasDatosDao;

	public static CuentasServices getInstance(String clientesTexto, String ficheroCuentas) throws ServiceException {

		if (cuentasServices == null)
			
				cuentasServices = new CuentasServices(clientesTexto, ficheroCuentas);
			
		return cuentasServices;

	}

	public static void main(String args[]) {

		CuentasServices cuentasServices;
		try {
			
			
			cuentasServices = CuentasServices.getInstance("clientes1.txt", "cuentas.db");
			cuentasServices.importarUsuarios("examen/ficheros/clientes.xml");
			cuentasServices.importarMovimientos("examen/ficheros/movimientos.xml");
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public void importarUsuarios(String fichero) throws ServiceException {
		try {
			List<Cliente> listaClientes = ImportarClientesXMLDao.leerClientesXML(new File(fichero));
			clientesDao.añadirListaClientes(listaClientes);
		} catch (DaoException e) {
			throw new ServiceException("No podemos importar Usuarios", e);
		}

	}
	
	public void importarMovimientos(String fichero) throws ServiceException {
		try {
			List<Movimiento> listaMovimientos = ImportarMovimientosXMLDao.leerMovimientoXML(new File(fichero));
			
			añadirListaMovimientos(listaMovimientos);
			
		} catch (DaoException e) {
			throw new ServiceException("No podemos importar Usuarios", e);
		}

	}
	
	
	private void añadirListaMovimientos(List<Movimiento> listaMovimientos) throws DaoException {

		for (Movimiento movimiento : listaMovimientos) {

			cuentasDatosDao.añadirSaldoACuenta(movimiento.getAño(), movimiento.getCcc(), movimiento.getVariacion());
			clientesDao.actualizarSaldo(movimiento.getCcc(), movimiento.getVariacion());
		}
		cuentasDatosDao.salvarDatos();

	}
	
	
	

	private CuentasServices(String clientesTexto, String ficheroCuentas) throws ServiceException {
		try {
			clientesDao = ClientesDao.getInstance(clientesTexto);
			cuentasDatosDao = CuentasDatosDao.getInstance(ficheroCuentas);
		} catch (DaoException e) {
			throw new ServiceException("No podemos arrancar CuentasServices", e);
		}

	}

}
