package com.codex.codexplt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl.CriterionEntry;
import org.hibernate.metadata.ClassMetadata;

import com.codex.codexplt.bd.PltDominio;
import com.codex.codexplt.bd.PltEnv;
import com.codex.codexplt.bd.PltFormulario;
import com.codex.codexplt.bd.PltMenu;
import com.codex.codexplt.bd.PltPermiso;
import com.codex.codexplt.bd.PltRelaForm;
import com.codex.codexplt.bd.PltRol;
import com.codex.codexplt.bd.PltUsuaRol;
import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.vo.Dominio;
import com.codex.codexplt.vo.Formulario;

public class PlataformaDAO extends PltDAO {
	
	/**
	 * Log (log4j).
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Retorna un listado de funcionalidades a las que tiene permisos un usuario
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<Formulario> getFormulariosUsuario(PltUsuario usuario) throws Exception{
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			
			String hql = "select distinct perm.pltFormulario from PltPermiso perm where perm.pltRol in (select distinct usuarol.pltRol from PltUsuaRol usuarol where usuarol.pltUsuario = :usuario )";
			
			Query query = sesion.createQuery(hql);
			query.setEntity("usuario", usuario);
			
			return query.list();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	
	public List<PltMenu> getMenu() throws Exception{
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}		
			
			Criteria criterio = sesion.createCriteria(PltMenu.class);
			criterio.addOrder(Order.asc("pltMenu.menuIdn"));
			criterio.addOrder(Order.asc("menuOrden"));
			
			return criterio.list();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	public List<Object> getDatos(Class clase, String condicion, String orderBy) throws Exception{
		
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
		
			StringBuilder hql = new StringBuilder("from ");
			hql.append(clase.getName());
			if(StringUtils.isNotBlank(condicion)){
				hql.append(" where ");
				hql.append(condicion);
			}
			
			if(StringUtils.isNotBlank(orderBy)){
				hql.append(" order by ");
				hql.append(orderBy);
			}
			
			Query query = sesion.createQuery(hql.toString());
			List<Object> resultado = query.list();
			return resultado;			
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
		
	}


	public String getCondicionId(Object padre) {
		
		ClassMetadata meta = sessionFactory.getClassMetadata(padre.getClass());
		if(meta == null){
			meta = sessionFactory.getClassMetadata(padre.getClass().getGenericSuperclass().getClass());
		}
		
		StringBuilder condicion = new StringBuilder(meta.getIdentifierPropertyName());
		condicion.append(" = ");
		condicion.append(meta.getIdentifier(padre,(SessionImplementor)sessionFactory.getCurrentSession()));
		
		return condicion.toString();
	}


	public PltEnv getEnv(String propiedad) {
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
		
			Criteria criteria = sesion.createCriteria(PltEnv.class);
			criteria.add(Restrictions.eq("envPropiedad", propiedad));
			return (PltEnv) criteria.uniqueResult();			
		} catch(Exception ex){
			
			return null;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	/**
	 * Metodo para crear un rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void crearRol(PltRol pltRol) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Creando Rol = ").append(pltRol.getRolNombre()));
		super.save(pltRol);
	}
	
	/**
	 * Metodo para modificar un rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void modificarRol(PltRol pltRol) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Modificando Rol = ").append(pltRol.getRolNombre()));
		super.update(pltRol);
	}
	
	
	/**
	 * Metodo para anular un rol
	 * @param pltRol
	 * @throws Exception
	 */
	public void anularRol(PltRol pltRol)throws Exception{ 
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Anulando Rol = ").append(pltRol.getRolNombre()));
		super.update(pltRol);
	}


	/**
	 * Metodo que retorna los hijos de un formulario
	 * @param formulario
	 * @return
	 */
	public List<PltRelaForm> getHijos(PltFormulario formulario) {
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
		
			Criteria criteria = sesion.createCriteria(PltRelaForm.class);
			criteria.add(Restrictions.eq("pltFormularioByFormPadre", formulario));
			criteria.addOrder(Order.asc("relaFormOrden"));
			return criteria.list();			
		} catch(Exception ex){
			
			return null;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	/**
	 * Retorna rol por nombre
	 * @param nombreRol
	 * @return
	 * @throws Exception
	 */
	public PltRol getRolPorNombre(String nombreRol) throws Exception{	
		Session sesion = sessionFactory.getCurrentSession();		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}			
			Criteria criteria = sesion.createCriteria(PltRol.class);
			criteria.add(Restrictions.eq("rolNombre", nombreRol));
			return (PltRol) criteria.uniqueResult();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
		
	}
	
	/**
	 * Retorna listado de formularios creados en el sistema
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<PltFormulario> getFormularios() throws Exception{
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			String hql = "from PltFormulario form order by form.formModulo, form.formNombre";
			
			Query query = sesion.createQuery(hql);
			List<PltFormulario> resultado = query.list();
//			Criteria criteria = sesion.createCriteria(PltFormulario.class,"formulario");
//			criteria.addOrder(Order.asc("formulario.formModulo"));
//			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			
			
			return resultado;
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}


	/**
	 * Metodo para obtener roles ordenados por nombre
	 * @return
	 */
	public List<PltRol> getRolesOrdenadoNombre() {
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			Criteria criteria = sesion.createCriteria(PltRol.class);			
			criteria.addOrder(Order.asc("rolNombre"));
			return criteria.list();
		} catch(Exception ex){
			
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	/**
	 * Metodo para crear un permiso
	 * @param permiso
	 * @throws Exception 
	 */
	public void crearPermiso(PltPermiso permiso) throws Exception{
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Creando Permiso para formulario = ").append(permiso.getPltFormulario().getNombre()));
		super.save(permiso);		
	}
	
	/**
	 * Metodo que elimina los permisos de un rol
	 * @param rol
	 */
	public void eliminarPermisos(PltRol rol){
		
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			
			String hql = "delete from PltPermiso perm where perm.pltRol = :rol)";
			
			Query query = sesion.createQuery(hql);
			query.setEntity("rol", rol);
			
			query.executeUpdate();
			
			tx.commit();
			
		} catch(Exception ex){
			tx.rollback();
			sesion.close();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
			
		}
	}
	
	/**
	 * Retorna listado de formularios creados en el sistema
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<PltPermiso> getFormulariosConPermisos(PltRol rol) throws Exception{
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			Criteria criteria = sesion.createCriteria(PltPermiso.class,"permiso");
			criteria.add(Restrictions.eq("permiso.pltRol", rol));
			
			return criteria.list();
		} catch(Exception ex){
			throw ex;
		}finally {
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	/**
	 * Metodo para asociar un rol a un usuario
	 * @param permiso
	 * @throws Exception 
	 */
	public void asociarUsuarioRol(PltUsuaRol pltUsuaRol) throws Exception{
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Asociando Rol para usuario = ").append(pltUsuaRol.getPltUsuario().getLogin()));
		super.save(pltUsuaRol);		
	}
	
	/**
	 * Metodo que retorna todos los roles asociados a un usuario
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<PltUsuaRol> getRolesUsuario(PltUsuario usuario) throws Exception{
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			Criteria criteria = sesion.createCriteria(PltUsuaRol.class,"pltUsuaRol");
			criteria.add(Restrictions.eq("pltUsuaRol.pltUsuario", usuario));
			
			return criteria.list();
		} catch(Exception ex){			
			throw ex;
		} finally {
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	/**
	 * Metodo que elimina los roles asociados a un usuario
	 * @param usuario
	 */
	public void eliminarRolesUsuario(PltUsuario usuario){
		
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			
			String hql = "delete from PltUsuaRol usuaRol where usuaRol.pltUsuario = :usuario)";
			
			Query query = sesion.createQuery(hql);
			query.setEntity("usuario", usuario);
			
			query.executeUpdate();
			
			tx.commit();
			
		} catch(Exception ex){
			tx.rollback();
			sesion.close();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
			
		}
	}


	public List<Dominio> getDominio(String nombreDominio) {
		Session sesion = sessionFactory.getCurrentSession();
		Transaction tx = sesion.getTransaction();
		try{
			
			if(! tx.isActive()){
				tx.begin();
			}
			Criteria criteria = sesion.createCriteria(PltDominio.class);
			criteria.add(Restrictions.eq("dominioNombre", nombreDominio));
			criteria.addOrder(Order.asc("dominioLeyenda"));
			
			return criteria.list();
		} catch(Exception ex){			
			throw ex;
		} finally {
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}
	
	
}
