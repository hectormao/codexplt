package com.codex.codexplt.cnt;

import java.awt.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Div;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.codex.codexplt.bd.PltFormulario;
import com.codex.codexplt.bd.PltRol;
import com.codex.codexplt.bd.PltUsuaRol;
import com.codex.codexplt.bd.PltUsuario;
import com.codex.codexplt.exc.PltException;
import com.codex.codexplt.ngc.PlataformaNgc;
import com.codex.codexplt.ngc.UsuarioNgc;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.utl.EncriptarClave;
import com.codex.codexplt.utl.WindowComposer;
import com.codex.codexplt.vo.Formulario;

public class UsuarioCnt extends WindowComposer {
	
	
	
//	ATRIBUTOS
	protected Window winUsuario;
	protected Textbox txtNombreCompleto;
	protected Textbox txtCorreo;
	protected Textbox txtLogin;
	protected Textbox txtClave;
//	private Textbox txtConfirmarClave;
	protected Button btnAceptar;
	protected Button btnCancelar;
//	private Label lblConfirmarClave;
	protected Listbox lstRolesUsuario;
	protected java.util.List<PltRol> listaRoles;
	protected Vlayout vlylogin;
	protected Textbox txtConfirmarClave;
	
	/**
	 * Objeto a insertar
	 */
	protected PltUsuario usu;
	
	/**
	 * Negocio Usuario
	 */
	protected UsuarioNgc usuarioNgc;
	
	
	
	/**
	 * Log (log4j)
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	
	public void doAfteCompose(Window winUsuario) throws Exception {

		super.doAfterCompose(winUsuario);
		logger = Logger.getLogger(this.getClass());
		btnAceptar.setAutodisable("self");
		
		

	}

	public void onCreate$winUsuario(Event event) throws Exception{
		try {
			if (logger.isDebugEnabled())logger.debug(new StringBuilder("Formulario = ").append(formulario.getNombre()));
//			Se crea listbox de roles
			cargarListboxRoles();
			if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_INSERTAR)) {
				// Se instancia nuevo objeto de usuario
				usu = new PltUsuario();

			} else if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_EDITAR)) {

				usu = (PltUsuario) argumentos.get(ConstantesAdmin.ARG_SELECCION);

				if (usu == null) {
					throw new PltException(ConstantesAdmin.ERR0007);
				}
				construirConfirmarContraseña();
				//Se diligencia formulario con datos del usuario
				cargarDatosUsuario(usu);
				//Se selecciona los roles del listbox que estan asociados al usuario
				seleccionarRolesUsuario();
				
				

			}else if(formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_BORRAR)){
				usu = (PltUsuario) argumentos.get(ConstantesAdmin.ARG_SELECCION);
				cargarDatosUsuario(usu);
				soloConsulta();
			}
		} catch (PltException e) {
			logger.error(new StringBuilder("Error metodo onCreate usuario").append(e.getClass().getName()).append(": ").append(e.getMessage()), e);
			Messagebox.show(e.getMessage(), Labels.getLabel("data3000.error"),Messagebox.OK, Messagebox.ERROR);
			self.detach();

		}

	}
	
	public void construirConfirmarContraseña(){
		Hlayout hlayout = new Hlayout();
		txtConfirmarClave = new Textbox();
		Div div = new Div();
		Label label = new Label();
		label.setId("lblConfirmarClave");
		String labelValue = Labels.getLabel("app.confirmarClave");
		label.setValue(labelValue);
		label.setStyle("font-weight:bold;");
		div.appendChild(label);
		div.setStyle("width:150px");
		hlayout.appendChild(div);
		txtConfirmarClave.setType("password");
		txtConfirmarClave.setWidth("150px");
		txtConfirmarClave.setReadonly(true);
		txtConfirmarClave.setStyle("background-color:#D8D8D8");
		txtConfirmarClave.setValue(usu.getUsuaClave());
		txtClave.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				if(txtConfirmarClave.isReadonly()){
					txtConfirmarClave.setReadonly(false);
					txtConfirmarClave.setValue("");
					txtConfirmarClave.setStyle("background-color:#FFFFFF");
				}
			}
		});
		hlayout.appendChild(txtConfirmarClave);
		vlylogin.appendChild(hlayout);
	}
	
	public void seleccionarRolesUsuario() throws Exception{
		java.util.List<PltUsuaRol> listaRolesUsuarios = new ArrayList<PltUsuaRol>();
		
		listaRolesUsuarios = plataformaNgc.getRolesUsuario(usu);
		
		Map<String, Object> mapaRolesUsuario = new HashMap<String, Object>();
		if(listaRolesUsuarios != null && !listaRolesUsuarios.isEmpty()){
			for(PltUsuaRol pltUsuaRol : listaRolesUsuarios){
				mapaRolesUsuario.put(pltUsuaRol.getPltRol().getRolNombre(), pltUsuaRol.getPltRol());			
			}
			
			for(Listitem listitem : lstRolesUsuario.getItems()){
				PltRol pltRol = listitem.getValue();
				if(mapaRolesUsuario.get(pltRol.getRolNombre()) != null){
					listitem.setSelected(Boolean.TRUE);
				}			
			}
		}
		
		
	}
	
	/**
	 * Metodo para crear listbox de roles del sistema
	 * @throws Exception
	 */
	public void cargarListboxRoles() throws Exception{
		listaRoles = plataformaNgc.getRoles();
		for(PltRol rol : listaRoles){
			Listitem listitem = new Listitem();
			listitem.setValue(rol);
			
			Listcell celdaChechBox = new Listcell("");
			Listcell celdaNombre = new Listcell(rol.getRolNombre());
			Listcell celdaDescripcion = new Listcell(rol.getRolDescripcion());
			
			listitem.appendChild(celdaChechBox);
			listitem.appendChild(celdaNombre);
			listitem.appendChild(celdaDescripcion);
			
			lstRolesUsuario.appendChild(listitem);
		}	
	}

	private void cargarDatosUsuario(PltUsuario usua) {
//		Cargar datos en el formulario
		txtNombreCompleto.setValue(usua.getUsuaNombre());
		txtCorreo.setValue(usua.getUsuaCorreo());
		txtLogin.setValue(usua.getUsuaLogin());
		txtLogin.setReadonly(true);
		txtLogin.setStyle("background-color:#D8D8D8");
		txtClave.setValue(usua.getUsuaClave());
		txtClave.setReadonly(false);
//		txtClave.setStyle("background-color:#D8D8D8");
//		txtConfirmarClave.setVisible(Boolean.FALSE);
//		lblConfirmarClave.setVisible(Boolean.FALSE);
	}
	
	public void onClick$btnAceptar(Event evt) throws Exception{
		try {
			
		
		establecerDatos();
		
		if(formulario.getTipo().equals(ConstantesAdmin.FORMULARIO_TIPO_INSERTAR)){
			usuarioNgc.crearUsuario(usu, usu.getLogin());
			asociarRolesUsuario(usu);
		} else if (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_EDITAR)) {
			if(! txtConfirmarClave.isReadonly()){
				String Clave1 = txtConfirmarClave.getValue();
				String Clave2 = txtClave.getValue();
				if(!Clave1.equalsIgnoreCase(Clave2)){
					throw new PltException(ConstantesAdmin.ERR0012);
				}
			}
			usuarioNgc.modificarUsuario(usu);
			
			//Se eliminan todos los roles asociados anteriormente
			plataformaNgc.eliminarRolesUsuario(usu);
			
			//Se asocian los roles nuevos al usuario.
			asociarRolesUsuario(usu);
			
		}else if(formulario.getTipo().equals(ConstantesAdmin.FORMULARIO_TIPO_BORRAR)){
			String nota = solicitarNota();
			
			usu.setAudiFechModi(new Date());
			usu.setAudiMotiAnul(nota);
			usu.setAudiSiAnul(true);
			usu.setAudiUsuario(usuario.getLogin());
			usuarioNgc.anularUsuario(usu);
			
		}
		
		
		Events.sendEvent(new Event(Events.ON_CLOSE,this.self,usu));
		} catch (PltException e) {
			logger.error(new StringBuilder("Error metodo onClick$btnAceptar usuario").append(e.getClass().getName()).append(": ").append(e.getMessage()), e);
			Messagebox.show(Labels.getLabel(e.getCodigo()), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onClick$btnCancelar(Event evt) throws Exception{
		Events.sendEvent(new Event(Events.ON_CLOSE,this.self,null));
		
	}
	
	public void asociarRolesUsuario(PltUsuario pltUsuario) throws Exception{
		if(lstRolesUsuario.getSelectedItems() != null){
			for(Listitem item : lstRolesUsuario.getSelectedItems()){
				PltUsuaRol pltUsuaRol = new PltUsuaRol();
				pltUsuaRol.setPltUsuario(pltUsuario);
				pltUsuaRol.setPltRol((PltRol) item.getValue());
				pltUsuaRol.setAudiUsuario(usuario.getLogin());
				pltUsuaRol.setAudiFechModi(new Date());
				plataformaNgc.asociarUsuarioRol(pltUsuaRol);
			}
		}
	}

	protected void establecerDatos() throws WrongValueException, Exception {
		
		usu.setUsuaNombre(txtNombreCompleto.getValue());
		usu.setUsuaCorreo(txtCorreo.getValue());
		usu.setUsuaLogin(txtLogin.getValue());	
		if (! (formulario.getTipo().equalsIgnoreCase(ConstantesAdmin.FORMULARIO_TIPO_EDITAR) && txtConfirmarClave.isReadonly())){
			EncriptarClave encriptarClave = new EncriptarClave();
			usu.setUsuaClave(encriptarClave.encryptPassword(txtClave.getValue()));		
		}
		usu.setUsuaEstado(ConstantesAdmin.ESTADO_ACTIVO);
		usu.setUsuaInteAute(new Short("0"));
		usu.setUsuaFechClav(new Date());
//		Auditoria
		usu.setAudiFechModi(new Date());
		usu.setAudiChecksum(null);
		usu.setAudiMotiAnul(null);
		usu.setAudiSiAnul(Boolean.FALSE);
		usu.setAudiUsuario(usuario.getLogin());		
		
	}

	public UsuarioNgc getUsuarioNgc() {
		return usuarioNgc;
	}

	public void setUsuarioNgc(UsuarioNgc usuarioNgc) {
		this.usuarioNgc = usuarioNgc;
	}

	

}
