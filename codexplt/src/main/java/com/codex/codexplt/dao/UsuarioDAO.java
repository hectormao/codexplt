package com.codex.codexplt.dao;


import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.vo.Usuario;

public class UsuarioDAO extends PltDAO {
	
	/**
	 * Log (log4j).
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	public PltUsuario getUsuarioPorLogin(String login) throws Exception{		
		Session sesion = sessionFactory.getCurrentSession();		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}			
			Criteria criterio = sesion.createCriteria(PltUsuario.class);
			criterio.add(Restrictions.eq("usuaLogin", login));
			return (PltUsuario) criterio.uniqueResult();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	
	/**
	 * Método para crear un nuevo usuario
	 * @param pltUsuario
	 * @throws Exception
	 */
	public void crearUsuario(PltUsuario pltUsuario) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Creando Usuario = ").append(pltUsuario.getUsuaLogin()));{
			super.save(pltUsuario);
		}
	}
	
	/**
	 * Método para modificar un usuario
	 * @param pltUsuario
	 * @throws Exception
	 */
	public void modificarUsuario(PltUsuario pltUsuario)throws Exception{
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Modificando Usuario = ").append(pltUsuario.getUsuaLogin()));{
			super.update(pltUsuario);
		}
		
	}
	
	/**
	 * Metodo para anular un usuario
	 * @param pltUsuario
	 * @throws Exception
	 */
	public void anularUsuario(PltUsuario pltUsuario)throws Exception{
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Anulando Usuario = ").append(pltUsuario.getUsuaLogin()));{
			super.update(pltUsuario);
		}
		
	}


	public List<PltUsuario> getusuariosOrdenadosNombre() {
		
		Session sesion = sessionFactory.getCurrentSession();		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}			
			Criteria criterio = sesion.createCriteria(PltUsuario.class);
			criterio.addOrder(Order.asc("usuaNombre"));
			return  criterio.list();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
		
	}


	public List<PltUsuario> getusuariosDiferentesOrdenadosNombre(PltUsuario usuario) {
		
		Session sesion = sessionFactory.getCurrentSession();		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}			
			Criteria criterio = sesion.createCriteria(PltUsuario.class);
			criterio.add(Restrictions.ne("usuaIdn", usuario.getUsuaIdn()));
			criterio.addOrder(Order.asc("usuaNombre"));
			return  criterio.list();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
}
