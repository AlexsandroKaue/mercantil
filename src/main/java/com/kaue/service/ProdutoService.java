package com.kaue.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;

public interface ProdutoService {

	public Produto salvar(Produto produto) throws Exception;
	public void excluir(Long codigo) throws Exception;
	public List<Produto> pesquisar(ProdutoFilter filtro);
	public Page<Produto> pesquisarPaginado(int pageNo, int pageSize);
	public Long contar(ProdutoFilter filtro);
	public Produto buscarPorCodigo(String codigo);
	public Produto buscarPorId(Long id);
	public Long obterIdAtual();
	public String salvarImagem(MultipartFile file, String nome) throws Exception;
	public Long obterMaxId();
	public String carregarImagem(String nome);
}
