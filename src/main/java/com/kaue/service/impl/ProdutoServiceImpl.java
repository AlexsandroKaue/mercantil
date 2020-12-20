package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.ProdutoDAO;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;
import com.kaue.service.ProdutoService;
import com.kaue.util.FileManager;

@Service
public class ProdutoServiceImpl implements ProdutoService{

	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
    FileManager fileManager;

	@Override
	public Produto salvar(Produto produto) throws Exception {
		try {
			return produtoDAO.save(produto);
		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao tentar salvar o produto.");
		}
	}

	@Override
	public void excluir(Long codigo) throws Exception {
		try {
			produtoDAO.deleteById(codigo);
		} catch(DataIntegrityViolationException e) {
			throw new Exception("Não foi possível excluir o produto porque está associado a outras partes do sistema.");
		} catch(Exception e) {
			throw new Exception("Ocorreu um erro ao tentar excluir o produto.");
		}
	}

	@Override
	public List<Produto> pesquisar(ProdutoFilter filtro) {
		List<Produto> produtoList = produtoDAO.findProdutoByFiltro(filtro);
		if(produtoList==null) {
			produtoList = new ArrayList<Produto>();
		}
		return produtoList;
	}
	
	@Override
	public Long contar(ProdutoFilter filtro) {
		Long count = produtoDAO.countProdutoByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Override
	public Produto buscarPorId(Long id) {
		return produtoDAO.findById(id).orElse(null);
	}

	@Override
	public Produto buscarPorCodigo(String codigo) {
		String cod = (codigo == null ? "" : codigo);
		return produtoDAO.findByCodigo(cod);
	}

	@Transactional
	public Long obterIdAtual() {
		return produtoDAO.obterMaxId();
	}

	@Override
	public String salvarImagem(MultipartFile file, String nome) throws Exception {
		return fileManager.salvarImagem(file, nome, "classpath:static/custom/img/produto");
	}
	
	@Override
	public String carregarImagem(String nome) {
		return fileManager.carregarImagem(nome, "classpath:static/custom/img/produto");
	}

	@Override
	public Long obterMaxId() {
		return produtoDAO.obterMaxId();
	}

	@Override
	public Page<Produto> pesquisarPaginado(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page <Produto> page = produtoDAO.findAll(pageable);
		return page;
	}

	@Override
	public Page<Produto> pesquisarPaginado(ProdutoFilter filtro) {
		List<Produto> produtoList = produtoDAO.findProdutoByFiltro(filtro);
		Long total = produtoDAO.countProdutoByFiltro(filtro);
		int pageNo = filtro.getPage().intValue();
		int pageSize = filtro.getPageSize().intValue();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Produto> page = new PageImpl<Produto>(produtoList, pageable, total);
		return page;
	}

	@Override
	public byte[] prepararImagem(MultipartFile multipartFile) throws Exception {
		return fileManager.prepararImagem(multipartFile);
	}

	@Override
	public String tranformarEmImagemBase64(byte[] bytes) {
		return fileManager.tranformarEmImagemBase64(bytes);
	}
	
}
