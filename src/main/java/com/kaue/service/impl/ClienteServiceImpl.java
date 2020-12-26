package com.kaue.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.ClienteDAO;
import com.kaue.dao.ProdutoDAO;
import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Caderneta;
import com.kaue.model.Cliente;
import com.kaue.service.CadernetaService;
import com.kaue.service.ClienteService;
import com.kaue.util.FileManager;
import com.kaue.util.HasValue;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
	private CadernetaService cadernetaService;
	
	@Autowired
    FileManager fileManager;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		try {
			return clienteDAO.save(cliente);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Override
	public void excluir(Long id) throws Exception {		
		try {
			Caderneta caderneta = cadernetaService.buscarPorCliente(id);
			if(HasValue.execute(caderneta)) {
				if(!HasValue.execute(caderneta.getAberta())) {
					cadernetaService.excluir(caderneta.getId());
				}
			}
			clienteDAO.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new Exception("Verifique se o cliente possui caderneta aberta.");
		} catch(Exception e) {
			throw new Exception("Ocorreu um erro ao tentar excluir o cliente.");
		}
	}

	@Transactional
	public List<Cliente> pesquisar(ClienteFilter filtro) {
		List<Cliente> clienteList = clienteDAO.findClienteByFiltro(filtro);
		if(clienteList==null) {
			clienteList = new ArrayList<Cliente>();
		}
		return clienteList;
	}
	
	@Transactional
	public Long contar(ClienteFilter filtro) {
		Long count = clienteDAO.countClienteByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Transactional
	public Cliente buscarPorId(Long id) {
		return clienteDAO.findById(id).orElse(null);
	}

	@Override
	public String salvarImagem(MultipartFile file, String nome) throws Exception {
		return fileManager.salvarImagem(file, nome, "classpath:static/custom/img/cliente");
	}
	
	@Override
	public String carregarImagem(String nome) {
		return fileManager.carregarImagem(nome, "classpath:static/custom/img/cliente");
	}

	@Override
	public Long obterMaxId() {
		return clienteDAO.obterMaxId();
	}

	
	
}
