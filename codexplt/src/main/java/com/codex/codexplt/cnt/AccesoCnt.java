package com.codex.codexplt.cnt;



import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.codex.codexplt.exc.PltException;
import com.codex.codexplt.ngc.UsuarioNgc;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.vo.Usuario;

public class AccesoCnt extends GenericForwardComposer<Window> {
	
	
	private UsuarioNgc usuarioNgc;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Window winAcceso;
	private Textbox txtUsuario;
	private Textbox txtClave;
	private Button btnIngresar;
	private Timer tmrUpdate;
	
	 
	
	@Override
	public void doAfterCompose(Window winAcceso) throws Exception{
		
		super.doAfterCompose(winAcceso);
		logger = Logger.getLogger(this.getClass());
		
		
		btnIngresar.setAutodisable("self");
		
		winAcceso.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Events.sendEvent(new Event(Events.ON_CLICK,btnIngresar,null));
				
			}
		});
		
		
		
		
		
		
		/*
		txtClave.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				onClick$btnIngresar();
				
			}
		});*/
		
	} 
	
	
	public void onUser$winAcceso(Event evt) throws Exception{
		ingresar();
	}
	
	public void onClick$btnIngresar() throws Exception{
		Clients.evalJavaScript("var wTxtUsuario = zk.Widget.$('$txtUsuario');"
				+ "if(wTxtUsuario != null){"
				+ "wTxtUsuario.updateChange_();"
				+ ""
				+ "}"
				+ "var wTxtClave = zk.Widget.$('$txtClave');"
				+ "if(wTxtClave != null){"
				+ "wTxtClave.updateChange_();"
				+ ""
				+ "}"
				+ "var wWinAcceso = zk.Widget.$('$winAcceso');"
				+ "zAu.send(new zk.Event(wWinAcceso, 'onUser', null, {toServer:true}));");
		
		btnIngresar.setDisabled(true);
	}
	
	
	
	public void ingresar() throws Exception{
		
		
		if(validar()){
			
			String login = txtUsuario.getValue();
			String clave = txtClave.getValue();
			try{
				Usuario usuario = usuarioNgc.validarAcceso(login, clave);
				tmrUpdate.stop();
				Events.sendEvent(Events.ON_CLOSE,winAcceso,usuario);
			} catch(PltException ex){
				Messagebox.show(Labels.getLabel(ex.getCodigo()), "Error", Messagebox.OK, Messagebox.ERROR);
				btnIngresar.setDisabled(false);
				logger.error("Error de logueo",ex);
			} catch(Exception ex){
				Messagebox.show(ex.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
				btnIngresar.setDisabled(false);
				logger.error("Error Desconocido de logueo",ex);
			}
						
		} else {
			btnIngresar.setDisabled(false);
		}
		
		
	}
	
	private boolean validar() throws Exception{
		
		boolean siError = false;
		
		String usuario = txtUsuario.getValue();
		
		if(usuario == null || (usuario != null && usuario.trim().length() <= 0)){
			txtUsuario.setErrorMessage(Labels.getLabel(ConstantesAdmin.ERR0002));
			siError = true;
		}
		
		String clave = txtClave.getValue();
		if(clave == null || (clave != null && clave.trim().length() <= 0)){
			txtClave.setErrorMessage(Labels.getLabel(ConstantesAdmin.ERR0002));
			siError = true;
		}
		
		return ! siError;
	}

	public UsuarioNgc getUsuarioNgc() {
		return usuarioNgc;
	}

	public void setUsuarioNgc(UsuarioNgc usuarioNgc) {
		this.usuarioNgc = usuarioNgc;
	}
	
	

}
