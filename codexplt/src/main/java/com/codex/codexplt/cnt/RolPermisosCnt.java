package com.codex.codexplt.cnt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.codex.codexplt.bd.PltFormulario;
import com.codex.codexplt.bd.PltPermiso;
import com.codex.codexplt.bd.PltRol;
import com.codex.codexplt.exc.PltException;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.utl.WindowComposer;
import com.codex.codexplt.vo.Formulario;

public class RolPermisosCnt extends WindowComposer {
	

	/**
	 * Atributos
	 */
	private Window winRolPermisos;
	private Textbox txtNombreRol;
	private Textbox txtDescripcionRol;
	private Button btnAceptar;
	private Button btnCancelar;
	private Tree treePermisoRol;
	private Treechildren tchPermisoRol;	
	
	/**
	 * Atributos del arbol
	 */
	
	
	/**
	 *Objetos a insertar 
	 */
	private PltRol pltRol;
	private PltPermiso pltPermiso;
	
	
	
	/**
	 * Log (log4j)
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	private List<PltFormulario> listaFormularios;
	
	private List<PltPermiso> listaPermisosRol;
	
	
	public void doAfteCompose(Window winRolPermisos) throws Exception {

		super.doAfterCompose(winRolPermisos);
		logger = Logger.getLogger(this.getClass());
		btnAceptar.setAutodisable("self");	
				
	}
	
	public void onCreate$winRolPermisos(Event event) throws Exception {
		try {
			if (logger.isDebugEnabled())logger.debug(new StringBuilder("Formulario = ").append(formulario.getNombre()));
			//Se crea el arbol con los formularios
			cargarItemsArbol();
			
			if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_INSERTAR)) {
				// Se instancia nuevo objeto de rol
				pltRol = new PltRol();
				
				
				
			} else if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_EDITAR)) {

				pltRol = (PltRol) argumentos.get(ConstantesAdmin.ARG_SELECCION);

				if (pltRol == null) {
					throw new PltException(ConstantesAdmin.ERR0007);
				}

				cargarDatosRolPermisos(pltRol);
				

			}else if(formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_BORRAR)){
				pltRol = (PltRol) argumentos.get(ConstantesAdmin.ARG_SELECCION);
				cargarDatosRolPermisos(pltRol);
				soloConsulta();
				
			}
		} catch (PltException e) {
			logger.error(new StringBuilder("Error metodo onCreate rolPermisos").append(e.getClass().getName()).append(": ").append(e.getMessage()), e);
			Messagebox.show(e.getMessage(), Labels.getLabel("data3000.error"),Messagebox.OK, Messagebox.ERROR);
			self.detach();

		}

	}
	
	public void cargarItemsArbol() throws Exception{
		//Arbol de Formularios
		listaFormularios = plataformaNgc.getFormularios();
		Map<String,Object> mapaModuloFormulario = new HashMap<String, Object>();	
		
		Treechildren treechildrenAux = new Treechildren();
		
		for(PltFormulario formulario : listaFormularios){
			
//			if(mapaModuloFormulario.get(formulario.getFormModulo()) == null){
//				//Se instancian elementos del arbol
//				Treeitem treeitemPadre = new Treeitem();
//				Treechildren treechildren = new Treechildren();
//				Treeitem treeitem2 = new Treeitem();
//				Treerow treerow = new Treerow();
//				Treecell treecell = new Treecell();
//				
//				treeitemPadre.setLabel(formulario.getFormModulo());
//				treecell.setAttribute(ConstantesAdmin.ATRIBUTO_FORMULARIO, formulario);
//				treecell.setLabel(formulario.getFormNombre());
//				treerow.appendChild(treecell);
//				treeitem2.appendChild(treerow);
//				treechildren.appendChild(treeitem2);
//				treeitemPadre.appendChild(treechildren);					
//				tchPermisoRol.appendChild(treeitemPadre);
//				mapaModuloFormulario.put(formulario.getFormModulo(), formulario);
//				treechildrenAux = treechildren;
//			}else{
//				mapaModuloFormulario.put(formulario.getFormModulo(),formulario);
//				cargarHijosArbol(treechildrenAux, formulario);
//			}
			
			if(mapaModuloFormulario.get(formulario.getFormModulo()) == null){
				Treeitem treeitemPadre = new Treeitem(Labels.getLabel(formulario.getFormModulo()));
				Treechildren treechildren = new Treechildren();
				Treeitem treeitem2 = new Treeitem(Labels.getLabel(formulario.getFormNombre()),formulario);
				
				treechildren.appendChild(treeitem2);
				treeitemPadre.appendChild(treechildren);					
				tchPermisoRol.appendChild(treeitemPadre);
				mapaModuloFormulario.put(formulario.getFormModulo(), formulario);
				treechildrenAux = treechildren;
			}else{
//				mapaModuloFormulario.put(Labels.getLabel(formulario.getFormModulo()),formulario);
				cargarHijosArbol(treechildrenAux, formulario);
			}
		}
	}
	
	
	private void cargarHijosArbol(Treechildren treechildren, PltFormulario formulario){
		Treeitem treeitem = new Treeitem(Labels.getLabel(formulario.getNombre()),formulario);
		treechildren.appendChild(treeitem);
			
	}
	
	
	private void cargarDatosRolPermisos(PltRol rol) throws Exception{
//		Cargar datos en el formulario
		txtNombreRol.setValue(rol.getRolNombre());
		txtDescripcionRol.setValue(rol.getRolDescripcion());
		
		//Lista de los formularios que tiene permiso el rol
		listaPermisosRol = plataformaNgc.getFormulariosConPermisos(pltRol);
		seleccionarFormulariosConPermisos(listaPermisosRol);
		
	}
	
	public void seleccionarFormulariosConPermisos(List<PltPermiso> listaPermisos){
		Map<String, Object> mapaPermisos = new HashMap<String, Object>();
		for(PltPermiso permisos : listaPermisos){
			mapaPermisos.put(permisos.getPltFormulario().getNombre(), permisos.getPltFormulario());
		}
		for(Treeitem item : treePermisoRol.getItems()){
			if(item.getValue() != null){
				PltFormulario formulario = item.getValue();
				if(mapaPermisos.get(formulario.getNombre()) != null){
					item.setSelected(Boolean.TRUE);
				}
			}			
		}
		
	}
	
	public void onClick$btnAceptar(Event evt) throws Exception{
		
		establecerDatosRolPermisos();
		
		if(formulario.getTipo().equals(ConstantesAdmin.FORMULARIO_TIPO_INSERTAR)){			
			plataformaNgc.crearRol(pltRol);
			asociarFormularioRol(pltRol);
			
		} else if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_EDITAR)) {
			plataformaNgc.modificarRol(pltRol);
			//Se eliminan todos los permisos asociados a un rol
			plataformaNgc.eliminarPermisos(pltRol);
			//Se asocian formularios seleccionados al rol
			asociarFormularioRol(pltRol);
		}else if(formulario.getTipo().equals(ConstantesAdmin.FORMULARIO_TIPO_BORRAR)){
			String nota = solicitarNota();
			
			pltRol.setAudiFechModi(new Date());
			pltRol.setAudiMotiAnul(nota);
			pltRol.setAudiSiAnul(Boolean.TRUE);
			pltRol.setAudiUsuario(usuario.getLogin());
			plataformaNgc.anularRol(pltRol);
			
		}
		
		//si todo salio bien refrescar arbol de permisos en la sesion		
		Map<String,Formulario> mapaFormularios = plataformaNgc.getFuncionalidadesUsuario(usuario);
		Executions.getCurrent().getSession().setAttribute(ConstantesAdmin.SESION_MAPA_FORM,mapaFormularios);
		
		Events.sendEvent(new Event(Events.ON_CLOSE,this.self,pltRol));
	}
	
	public void asociarFormularioRol(PltRol rol) throws Exception{
		if(treePermisoRol.getSelectedItems() != null){
			for(Treeitem item : treePermisoRol.getSelectedItems() ){
				PltPermiso permiso = new PltPermiso();
				permiso.setPltRol(rol);
				permiso.setPltFormulario((PltFormulario) item.getValue());
				permiso.setAudiUsuario(usuario.getLogin());
				permiso.setAudiFechModi(new Date());
				plataformaNgc.crearPermiso(permiso);
			}
		}
	}

	public void onClick$btnCancelar(Event evt) throws Exception{
		Events.sendEvent(new Event(Events.ON_CLOSE,this.self,null));
		
	}
	
	private void establecerDatosRolPermisos() throws WrongValueException, Exception{
		pltRol.setRolNombre(txtNombreRol.getValue());
		pltRol.setRolDescripcion(txtDescripcionRol.getValue());
//		Auditoria
		pltRol.setAudiFechModi(new Date());
		pltRol.setAudiChecksum(null);
		pltRol.setAudiMotiAnul(null);
		pltRol.setAudiSiAnul(Boolean.FALSE);
		pltRol.setAudiUsuario(usuario.getLogin());
		
//		Asociar formularios a rol
		
		
		
		
	}

}
