package com.codex.codexplt.ngc;

import java.util.List;

import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.vo.Usuario;

/**
 * determina el comportamiento del usuario en la plataforma 
 * @author hectormao
 *
 */
public interface UsuarioNgc {
	/**
	 * Valida la autorización de acceso de un usuario a la plataforma por medio de login y contraseña
	 * @param usuario
	 * @param clave
	 * @return
	 */
	public Usuario validarAcceso(String login, String clave) throws Exception;
	
	/**
	 * Crea un usuario, de no existir el mismo login
	 * @param pltUsuario
	 * @param loginUsuario
	 * @throws Exception
	 */
	public void crearUsuario(PltUsuario pltUsuario, String loginUsuario) throws Exception;
	
	/**
	 * Modifica un usuario
	 * @param pltUsuario
	 * @throws Exception
	 */
	public void modificarUsuario(PltUsuario pltUsuario) throws Exception;
	
	/**
	 * Elimina un Usuario
	 * @param pltUsuario
	 * @throws Exception
	 */
	public void anularUsuario(PltUsuario pltUsuario) throws Exception;
	
	/**
	 * Obtiene todos los usuarios ordenados por su nombre
	 * @return
	 * @throws Exception
	 */
	public List<PltUsuario> getUsuarios() throws Exception;

	/**
	 * Obtiene los usuarios diferentes al que le llega por parametro
	 * @param usuario
	 * @return
	 */
	public List<PltUsuario> getUsuariosDiferentesA(PltUsuario usuario);
	
	
	
}
