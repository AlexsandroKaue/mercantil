package com.kaue.service.impl;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.UsuarioDAO;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;
import com.kaue.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}

	@Transactional
	public void excluir(Long id) {
		usuarioDAO.deleteById(id);
	}

	@Transactional
	public List<Usuario> pesquisar(UsuarioFilter filtro) {
		List<Usuario> usuarioList = usuarioDAO.findUsuarioByFiltro(filtro);
		if(usuarioList==null) {
			usuarioList = new ArrayList<Usuario>();
		}
		return usuarioList;
	}
	
	@Transactional
	public Long contar(UsuarioFilter filtro) {
		Long count = usuarioDAO.countUsuarioByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Transactional
	public Usuario buscarPorId(Long id) {
		return usuarioDAO.findById(id).orElse(null);
	}

	@Override
	public Usuario buscarPorLogin(String login) {
		return usuarioDAO.findByLogin(login);
	}

	@Override
	public void salvarImagem(MultipartFile imagem, String nome) throws Exception {
		InputStream is = imagem.getResource().getInputStream();
		if(is!=null) {
			Path resourcePath = Paths.get("./src/main/resources/static/custom/img/users/"+nome+".jpg");
			Path absolutePath = resourcePath.toAbsolutePath();
			BufferedImage image = ImageIO.read(imagem.getResource().getInputStream());
		    ImageIO.write(image, "jpg", absolutePath.toFile());
		}
		//Files.write(absolutePath, bytes);
	}

	@Override
	public String carregarImagem(String nome) {
		Path resourcePath = Paths.get("./src/main/resources/static/custom/img/users/"+nome+".jpg");
		Path absolutePath = resourcePath.toAbsolutePath();
	    return tranformarEmImagemBase64(absolutePath);
	}
	
	
	
	private String tranformarEmImagemBase64(Path path) {
		String encodedfile = null;
		
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(path);
			if(bytes != null) {
				encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
			} else {
				encodedfile = buscarImagemPadrao();
			}
		} catch (Exception e) {
			encodedfile = buscarImagemPadrao();
		}
		
		return encodedfile;
	}
	
	
	private String buscarImagemPadrao() {
		
		String encodedfile = null;
		try {
			Path resourcePath = Paths.get("./src/main/resources/static/custom/img/users/sem-imagem.jpg");
			Path absolutePath = resourcePath.toAbsolutePath();
			//File file = ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg");
			byte[] bytes = Files.readAllBytes(absolutePath);
			
			encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encodedfile;
	}
	
}
