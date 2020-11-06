package com.kaue.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.model.Lote;
import com.kaue.model.Produto;
import com.kaue.service.CategoriaService;
import com.kaue.service.FornecedorService;
import com.kaue.service.ProdutoService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	private static final String CADASTRAR_VIEW = "page/produto/Cadastrar";
	private static final String LISTAR_VIEW = "page/produto/Listar";
	
	private static final String LISTAR_LOTE_VIEW = "page/lote/Cadastrar";
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@ModelAttribute
	public void addAttributes(Model model){
		model.addAttribute("imagem", "/custom/img/produto/sem-imagem_2.jpg");
	}
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Produto produto = new Produto();
		produto.setQuantidade(0);
		produto.setImagemBase64(produtoService.carregarImagem(null));
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		/*
		 * byte[] bytes = produto.getImagem(); if(bytes != null) { try { String
		 * encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
		 * produto.setImagemBase64(encodedfile); } catch (UnsupportedEncodingException
		 * e) { e.printStackTrace(); } } else {
		 * produto.setImagemBase64(buscarImagemPadrao()); }
		 */
		produto.setImagemBase64(produtoService.carregarImagem(produto.getImagemPath()));
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping("{id}/lote")
	public ModelAndView showListarLote(@PathVariable("id") Produto produto) 
	{
		ModelAndView mv = new ModelAndView(LISTAR_LOTE_VIEW);
		Lote lote = new Lote();
		lote.setProduto(produto);
		mv.addObject("lote", lote);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Produto produto, Errors errors, @RequestParam("file") MultipartFile multipartFile, 
			RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			String url = "";
			if(produto.getId()==null) {
				attributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
				Long idAtual = produtoService.obterIdAtual();
				produto.setCodigo(String.format("%010d" , idAtual+1));
				url = "redirect:/produtos/novo";
			} else {
				attributes.addFlashAttribute("mensagem", "Produto alterado com sucesso!");
				url = "redirect:/produtos/"+produto.getId();
			}
			
			
			boolean hasFileUploaded = !multipartFile.isEmpty();
			/*
			 * if(hasFileUploaded) { byte[] bytes = null; try { bytes =
			 * multipartFile.getBytes(); if(bytes != null) { BufferedImage croppedImage =
			 * cropImage(bytes); String extensao = "jpg";
			 * if(multipartFile.getContentType().equals("image/png")) { extensao = "png"; }
			 * else if(multipartFile.getContentType().equals("image/gif")) { extensao =
			 * "gif"; } ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 * ImageIO.write(croppedImage, extensao, baos); bytes = baos.toByteArray();
			 * produto.setImagem(bytes); }
			 * 
			 * } catch (IOException e) { attributes.addFlashAttribute("aviso",
			 * "Não foi possível salvar a imagem. " +
			 * "Verifique se o arquivo não está corrompido."); } }
			 */
			if(hasFileUploaded) {
				try {
					Long id;
					if(HasValue.execute(produto.getId())) {
						id = produto.getId();
					} else {
						id = produtoService.obterMaxId()+1;
					}
					
					String filename = produtoService.salvarImagem(multipartFile, "Produto_"+id);
					produto.setImagemPath(filename);
				} catch (Exception e) {
					attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem. Motivo: "+e.getMessage());
				}
			}
			
			try {
				produto = produtoService.salvar(produto);
			} catch (Exception e) {
				attributes.addFlashAttribute("mensagem_erro", e.getMessage());
			}
			
			return url;
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataDeVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	private BufferedImage cropImage(byte[] image) throws IOException {
	  // Get a BufferedImage object from a byte array
	  InputStream in = new ByteArrayInputStream(image);
	  BufferedImage originalImage = ImageIO.read(in);
	  
	  if(!HasValue.execute(originalImage)) {
		  throw new IOException();
	  }
	  
	  // Get image dimensions
	  int height = originalImage.getHeight();
	  int width = originalImage.getWidth();
	  
	  // The image is already a square
	  if (height == width) {
	    return originalImage;
	  }
	  
	  // Compute the size of the square
	  int squareSize = (height > width ? width : height);
	  
	  // Coordinates of the image's middle
	  int xc = width / 2;
	  int yc = height / 2;
	  
	  // Crop
	  BufferedImage croppedImage = originalImage.getSubimage(
	      xc - (squareSize / 2), // x coordinate of the upper-left corner
	      yc - (squareSize / 2), // y coordinate of the upper-left corner
	      squareSize,            // widht
	      squareSize             // height
	  );
	  
	  return croppedImage;
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ProdutoFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				filtro.getProduto().setDescricao(termo);
				filtro.getProduto().setMarca(termo);
				filtro.getProduto().setCategoria(new Categoria());
				filtro.getProduto().getCategoria().setDescricao(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getProduto().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		List<Produto> produtoList = produtoService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("produtos", produtoList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		try {
			produtoService.excluir(id);
			attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
		} catch (Exception e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
		}

		return "redirect:/produtos";
	}
	
	@ModelAttribute
	public List<Categoria> todasCategorias(){
		List<Categoria> categoriaList = categoriaService.pesquisar(new CategoriaFilter());
		return categoriaList;
	}
	
	@ModelAttribute
	public List<Fornecedor> todosFornecedores() {
		List<Fornecedor> fornecedorList = fornecedorService.pesquisar(new FornecedorFilter());
		return fornecedorList;
	}
	
	@ModelAttribute
	public List<Unitario> todosUnitarios(){
		return Arrays.asList(Unitario.values());
	}
	
	private String buscarImagemPadrao() {
		
		String base64 = null;
		try {
			File file = ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg");
			Path path = file.toPath();
			byte[] bytes = Files.readAllBytes(path);
			
			base64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return base64;
	}
}
