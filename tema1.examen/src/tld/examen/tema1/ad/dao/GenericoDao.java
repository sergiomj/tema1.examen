package tld.examen.tema1.ad.dao;

import java.io.Serializable;

import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;

public interface GenericoDao<T, PK extends Serializable>
{

    /**
     * Creamos un nuevo objeto 
     * @param nuevoObjeto El nuevo objeto a almacenar
     * @return El identificador de esa clase
     * @throws DaoException  Si se produce algún error en el acceso al fichero 
     */
    PK crear(T nuevoObjeto) throws DaoException;

    /**
     * Buscamos un objeto dado un identificador del objeto
     * @param id El identificador del objeto a buscar 
     * @return El objeto encontrado
     * @throws DaoException  Si se produce algún error en el acceso al fichero 
     */
    T leer(PK id) throws DaoException;

    /**
     * Actualizamos el objeto 
     * @param objetoModificado El obejto a almacenar 
     * @throws DaoException  Si se produce algún error en el acceso al fichero 
     */
    void actualizar(T objetoModificado) throws DaoException;

    /**
     * Borramos el objeto pasado como parámetro
     * @param objetoAlmacenado. El objeto a borrar
     * @throws DaoException  Si se produce algún error en el acceso al fichero 
     */
    void borrar(T objetoAlmacenado) throws DaoException;

	
}