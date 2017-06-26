package com.codex.codexplt.bd;
// Generated 11/03/2017 08:16:33 AM by Hibernate Tools 3.5.0.Final

import java.util.Date;

/**
 * PltUsuaRol generated by hbm2java
 */
public class PltUsuaRol implements java.io.Serializable {

	private long usuaRolIdn;
	private PltRol pltRol;
	private PltUsuario pltUsuario;
	private String audiUsuario;
	private Date audiFechModi;
	private boolean audiSiAnul;
	private String audiMotiAnul;
	private String audiChecksum;

	public PltUsuaRol() {
	}

	public PltUsuaRol(long usuaRolIdn, PltRol pltRol, PltUsuario pltUsuario, String audiUsuario, Date audiFechModi,
			boolean audiSiAnul) {
		this.usuaRolIdn = usuaRolIdn;
		this.pltRol = pltRol;
		this.pltUsuario = pltUsuario;
		this.audiUsuario = audiUsuario;
		this.audiFechModi = audiFechModi;
		this.audiSiAnul = audiSiAnul;
	}

	public PltUsuaRol(long usuaRolIdn, PltRol pltRol, PltUsuario pltUsuario, String audiUsuario, Date audiFechModi,
			boolean audiSiAnul, String audiMotiAnul, String audiChecksum) {
		this.usuaRolIdn = usuaRolIdn;
		this.pltRol = pltRol;
		this.pltUsuario = pltUsuario;
		this.audiUsuario = audiUsuario;
		this.audiFechModi = audiFechModi;
		this.audiSiAnul = audiSiAnul;
		this.audiMotiAnul = audiMotiAnul;
		this.audiChecksum = audiChecksum;
	}

	public long getUsuaRolIdn() {
		return this.usuaRolIdn;
	}

	public void setUsuaRolIdn(long usuaRolIdn) {
		this.usuaRolIdn = usuaRolIdn;
	}

	public PltRol getPltRol() {
		return this.pltRol;
	}

	public void setPltRol(PltRol pltRol) {
		this.pltRol = pltRol;
	}

	public PltUsuario getPltUsuario() {
		return this.pltUsuario;
	}

	public void setPltUsuario(PltUsuario pltUsuario) {
		this.pltUsuario = pltUsuario;
	}

	public String getAudiUsuario() {
		return this.audiUsuario;
	}

	public void setAudiUsuario(String audiUsuario) {
		this.audiUsuario = audiUsuario;
	}

	public Date getAudiFechModi() {
		return this.audiFechModi;
	}

	public void setAudiFechModi(Date audiFechModi) {
		this.audiFechModi = audiFechModi;
	}

	public boolean isAudiSiAnul() {
		return this.audiSiAnul;
	}

	public void setAudiSiAnul(boolean audiSiAnul) {
		this.audiSiAnul = audiSiAnul;
	}

	public String getAudiMotiAnul() {
		return this.audiMotiAnul;
	}

	public void setAudiMotiAnul(String audiMotiAnul) {
		this.audiMotiAnul = audiMotiAnul;
	}

	public String getAudiChecksum() {
		return this.audiChecksum;
	}

	public void setAudiChecksum(String audiChecksum) {
		this.audiChecksum = audiChecksum;
	}

}