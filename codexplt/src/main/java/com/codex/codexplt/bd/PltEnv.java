package com.codex.codexplt.bd;
// Generated 11/03/2017 08:16:33 AM by Hibernate Tools 3.5.0.Final

import java.util.Date;

/**
 * PltEnv generated by hbm2java
 */
public class PltEnv implements java.io.Serializable {

	private long envIdn;
	private String envPropiedad;
	private String envValor;
	private String envDescripcion;
	private String audiUsuario;
	private Date audiFechModi;
	private boolean audiSiAnul;
	private String audiMotiAnul;
	private String audiChecksum;

	public PltEnv() {
	}

	public PltEnv(long envIdn, String envPropiedad, String envValor, String audiUsuario, Date audiFechModi,
			boolean audiSiAnul) {
		this.envIdn = envIdn;
		this.envPropiedad = envPropiedad;
		this.envValor = envValor;
		this.audiUsuario = audiUsuario;
		this.audiFechModi = audiFechModi;
		this.audiSiAnul = audiSiAnul;
	}

	public PltEnv(long envIdn, String envPropiedad, String envValor, String envDescripcion, String audiUsuario,
			Date audiFechModi, boolean audiSiAnul, String audiMotiAnul, String audiChecksum) {
		this.envIdn = envIdn;
		this.envPropiedad = envPropiedad;
		this.envValor = envValor;
		this.envDescripcion = envDescripcion;
		this.audiUsuario = audiUsuario;
		this.audiFechModi = audiFechModi;
		this.audiSiAnul = audiSiAnul;
		this.audiMotiAnul = audiMotiAnul;
		this.audiChecksum = audiChecksum;
	}

	public long getEnvIdn() {
		return this.envIdn;
	}

	public void setEnvIdn(long envIdn) {
		this.envIdn = envIdn;
	}

	public String getEnvPropiedad() {
		return this.envPropiedad;
	}

	public void setEnvPropiedad(String envPropiedad) {
		this.envPropiedad = envPropiedad;
	}

	public String getEnvValor() {
		return this.envValor;
	}

	public void setEnvValor(String envValor) {
		this.envValor = envValor;
	}

	public String getEnvDescripcion() {
		return this.envDescripcion;
	}

	public void setEnvDescripcion(String envDescripcion) {
		this.envDescripcion = envDescripcion;
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
