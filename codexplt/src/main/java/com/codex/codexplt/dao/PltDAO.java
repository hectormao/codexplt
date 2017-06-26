package com.codex.codexplt.dao;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class PltDAO {
	
	protected SessionFactory sessionFactory;

	/**
	 * Inserta un objeto perteneciente a un registro en la base de datos, si este elemento existe (se evalua clave primaria) ser&aacuten actualizados sus atributos.
	 * 
	 * @param ob Object mapeado por Hibernate que será almcenado o actualizado en base de datos
	 * @throws Exception 
	 */
	public void saveOrUpdate(Object ob) throws Exception {
		Session sesion = sessionFactory.getCurrentSession();
		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}
			
			sesion.saveOrUpdate(ob);
			
			tx.commit();
		} catch(Exception ex){
			tx.rollback();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}

	/**
	 * Almacena un objeto en la base de datos.
	 * 
	 * @param ob Object mapeado por hibernate correspondiente a una tabla
	 * @throws Exception 
	 */
	public void save(Object ob) throws Exception {
		Session sesion = sessionFactory.getCurrentSession();
		
		Transaction tx = sesion.getTransaction();
		try{
			if(! tx.isActive()){
				tx.begin();
			}
			
			sesion.save(ob);
			
			tx.commit();
		} catch(Exception ex){
			tx.rollback();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}

	/**
	 * Actualiza un objeto en base de datos.
	 * 
	 * @param ob Object mapeado por hibernate correspondiente a una tabla
	 * @throws Exception 
	 */
	public void update(Object ob) throws Exception {
		Session sesion = sessionFactory.getCurrentSession();
		
		Transaction tx = sesion.getTransaction();
		
		try{
			if(! tx.isActive()){
				tx.begin();
			}
			sesion.merge(ob);
			tx.commit();
		} catch(Exception ex){
			tx.rollback();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}

	/**
	 * Elimina un objeto o registro de base de datos.
	 * 
	 * @param ob Object mapeado por hibernate correspondiente a una tabla
	 * @throws Exception 
	 */
	public void delete(Object ob) throws Exception {
		Session sesion = sessionFactory.getCurrentSession();

		
		Transaction tx = sesion.getTransaction();
		
		try{
			if(! tx.isActive()){
				tx.begin();
			}
		//Se limpia la sesion para evitar errores al eliminar registros en las grillas
			sesion.clear();
			
			sesion.delete(ob);
			
			tx.commit();
		} catch(Exception ex){
			tx.rollback();
			throw ex;
		}finally{
			if(sesion.isOpen()){
				sesion.close();
			}
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
