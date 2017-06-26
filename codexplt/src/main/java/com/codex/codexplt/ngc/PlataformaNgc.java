package com.codex.codexplt.ngc;

import java.util.List;
import java.util.Map;

import com.codex.codexplt.bd.PltFormulario;
import com.codex.codexplt.bd.PltPermiso;
import com.codex.codexplt.bd.PltRol;
import com.codex.codexplt.bd.PltUsuaRol;
import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.vo.Dominio;
import com.codex.codexplt.vo.EstructuraMenu;
import com.codex.codexplt.vo.Formulario;
import com.codex.codexplt.vo.Menu;
import com.codex.codexplt.vo.Usuario;

public interface PlataformaNgc {
	/**
	 * Obtiene un mapa con las funcionalidades a las que tiene acceso un usuario
	 * @param usuario
	 * @return
	 */
	public Map<String,Formulario> getFuncionalidadesUsuario(Usuario usuario) throws Exception;
	
	
	/**
	 * Obtiene el menu a pintar para un usuario
	 * @param usuario
	 * @param mapaFormularios 
	 * @return
	 */
	public List<EstructuraMenu> getMenu(Usuario usuario, Map<String, Formulario> mapaFormularios) throws Exception;
	
	/**
	 * Obtiene los datos para una tabla de datos
	 * @param tablaDatos
	 * @return
	 */
	public List<Object> getDatos(Class clase) throws Exception;
	
	/**
	 * Obtiene los datos ejecutando la consulta hql
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public List<Object> getDatos(Class clase, String condicion, String orderBy) throws Exception;


	public String getCondicionPadre(Object padre);
	
	/**Crea un Rol, de no existir el mismo rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void crearRol(PltRol pltRol) throws Exception;
	
	/**Modifica un Rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void modificarRol(PltRol pltRol) throws Exception;
	
	/**Elimina un Rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void anularRol(PltRol pltRol) throws Exception;
	
	/**
	 * Obtiene todos los roles ordenados por su nombre
	 * @return
	 * @throws Exception
	 */
	public List<PltRol> getRoles() throws Exception;
	
	
	
	/**
	 * Obtiene el valor de una variable del entorno
	 * @param propiedad
	 * @return
	 */
	public String getEnv(String propiedad);
	
	/**
	 * Obtiene lista de todos formularios/funcionalidades del sistema
	 * @return
	 * @throws Exception
	 */
	public List<PltFormulario> getFormularios() throws Exception;
	
	/**
	 * Metodo para crear permisos
	 * @param permiso
	 * @throws Exception
	 */
	public void crearPermiso(PltPermiso permiso) throws Exception;
	
	/**
	 * Metodo para eliminar todos los permisos asociados a un rol
	 * @param rol
	 */
	public void eliminarPermisos(PltRol rol);
	
	/**
	 * Obtiene la lista de permisos de un rol
	 * @param rol
	 * @return
	 * @throws Exception
	 */
	public List<PltPermiso> getFormulariosConPermisos(PltRol rol) throws Exception;
	
	/**
	 * Metodo para asociar roles a usuario
	 * @param pltUsuaRol
	 * @throws Exception
	 */
	public void asociarUsuarioRol(PltUsuaRol pltUsuaRol) throws Exception;
	
	
	/**
	 * Metodo que retorna los roles asociados a un usuario
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<PltUsuaRol> getRolesUsuario(PltUsuario usuario) throws Exception;
	
	/**
	 * Metodo que elimina los roles asociados a un usuario
	 * @param usuario
	 */
	public void eliminarRolesUsuario(PltUsuario usuario);

	
	/**
	 * Obtiene los valores posibles de un dominio
	 * @param nombreDominio
	 * @return
	 */
	public List<Dominio> getDominio(String nombreDominio);
}
