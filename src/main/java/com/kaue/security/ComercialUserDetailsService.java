package com.kaue.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kaue.model.Grupo;
import com.kaue.model.GrupoPermissao;
import com.kaue.model.Permissao;
import com.kaue.model.Usuario;
import com.kaue.model.UsuarioWeb;
import com.kaue.service.GrupoService;
import com.kaue.service.PermissaoService;
import com.kaue.service.UsuarioService;

@Component
public class ComercialUserDetailsService implements UserDetailsService {
   
  @Autowired
  private UsuarioService usuarioService;
 
  @Autowired
  private GrupoService grupoService;
   
  @Autowired
  private PermissaoService permissaoService;
 
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioService.buscarPorLogin(username);
     
    if (usuario == null) {
      throw new UsernameNotFoundException("Usuário não encontrado!");
    }
     
    return new UsuarioWeb(usuario, authorities(usuario));
  }
   
  public Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
	  List<Grupo> grupoList = new ArrayList<Grupo>();
	  grupoList.add(usuario.getGrupo());
    //return authorities(grupoService.buscarPorUsuario(usuario));
	  return authorities(grupoList);
  }
   
  public Collection<? extends GrantedAuthority> authorities(List<Grupo> grupos) {
    Collection<GrantedAuthority> auths = new ArrayList<>();
     
    Grupo grupo = grupos.get(0);
    List<GrupoPermissao> grupoPermissaoList = grupo.getGrupoPermissaoList();
    
    //for (Grupo grupo: grupos) {
      for(GrupoPermissao gruPer : grupoPermissaoList) {
    	  Permissao permissao = gruPer.getPermissao();
    	  auths.add(new SimpleGrantedAuthority(permissao.getSigla()));
    	  //List<Permissao> lista = permissaoService.buscarPorGrupo(grupo);
    	     
          //for (Permissao permissao: lista) {
          //  auths.add(new SimpleGrantedAuthority("ROLE_" + permissao.getDescricao()));
          //}
      }
      
    //}
     
    return auths;
  }
}