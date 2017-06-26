package com.codex.codexplt.ngc;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zhtml.Tbody;
import org.zkoss.zk.ui.Executions;

import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.dao.UsuarioDAO;
import com.codex.codexplt.exc.PltException;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.utl.EncriptarClave;
import com.codex.codexplt.vo.Formulario;
import com.codex.codexplt.vo.Usuario;

public class UsuarioNgcImpl implements UsuarioNgc {
	
	
	private UsuarioDAO usuarioDAO;
	
	private PlataformaNgc plataformaNgc;
	
	/**
	 * Log (log4j).
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	

	@Override
	public Usuario validarAcceso(String login, String clave) throws Exception {
		
		PltUsuario usuario = usuarioDAO.getUsuarioPorLogin(login);
		
		if(usuario == null){
			throw new PltException(ConstantesAdmin.ERR0003);
		}
		
		if(usuario.getUsuaEstado().equals(ConstantesAdmin.ESTADO_INACTIVO)){
			throw new PltException(ConstantesAdmin.ERR0005);
		}
		
		String claveEncriptada = EncriptarClave.encryptPassword(clave);
		
		if(! claveEncriptada.equals(usuario.getUsuaClave())){
			throw new PltException(ConstantesAdmin.ERR0004);
		}
		
		Map<String,Formulario> mapaFormularios = plataformaNgc.getFuncionalidadesUsuario(usuario);
		
		Executions.getCurrent().getSession().setAttribute(ConstantesAdmin.SESION_MAPA_FORM,mapaFormularios);
		Executions.getCurrent().getSession().setAttribute(ConstantesAdmin.SESION_USUARIO,usuario);
		return usuario;
	}
	
	@Override
	public void crearUsuario(PltUsuario pltUsuario, String loginUsuario) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Creando Usuario = ").append(pltUsuario.getUsuaLogin()));{
		
	//		Se veririfica si ya existe un login igual creado
			if(usuarioDAO.getUsuarioPorLogin(pltUsuario.getUsuaLogin()) != null){
				throw new PltException(ConstantesAdmin.ERR0006);
			}
	//		Crear usuario
			usuarioDAO.crearUsuario(pltUsuario);
		}
		
	}

	@Override
	public void modificarUsuario(PltUsuario pltUsuario) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Modificando Usuario = ").append(pltUsuario.getUsuaLogin()));{
//			Modificar Usuario
			usuarioDAO.modificarUsuario(pltUsuario);			
		}
		
	}
	
	@Override
	public void anularUsuario(PltUsuario pltUsuario) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Anulando Usuario = ").append(pltUsuario.getUsuaLogin()));{
			usuarioDAO.anularUsuario(pltUsuario);
		}
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public PlataformaNgc getPlataformaNgc() {
		return plataformaNgc;
	}

	public void setPlataformaNgc(PlataformaNgc plataformaNgc) {
		this.plataformaNgc = plataformaNgc;
	}

	@Override
	public List<PltUsuario> getUsuarios() throws Exception {		
		return usuarioDAO.getusuariosOrdenadosNombre();
	}

	@Override
	public List<PltUsuario> getUsuariosDiferentesA(PltUsuario usuario) {
		
		return usuarioDAO.getusuariosDiferentesOrdenadosNombre(usuario);
	}

	

	
	
	
	
}
