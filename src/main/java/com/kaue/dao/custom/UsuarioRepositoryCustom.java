package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;

public interface UsuarioRepositoryCustom {

	public List<Usuario> findUsuarioByFiltro(UsuarioFilter usuarioFiltro);
	public Long countUsuarioByFiltro(UsuarioFilter usuarioFiltro);
}
