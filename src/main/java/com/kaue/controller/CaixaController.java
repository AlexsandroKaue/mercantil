package com.kaue.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.OpcoesDesconto;
import com.kaue.enumeration.StatusCaixa;
import com.kaue.enumeration.StatusVenda;
import com.kaue.enumeration.TipoConta;
import com.kaue.enumeration.TipoMovimentacaoCaixa;
import com.kaue.enumeration.TipoVenda;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Caixa;
import com.kaue.model.Categoria;
import com.kaue.model.Cliente;
import com.kaue.model.Item;
import com.kaue.model.MovimentacaoCaixa;
import com.kaue.model.Produto;
import com.kaue.model.Registro;
import com.kaue.model.Usuario;
import com.kaue.model.UsuarioWeb;
import com.kaue.model.Venda;
import com.kaue.service.CaixaService;
import com.kaue.service.ClienteService;
import com.kaue.service.MovimentacaoCaixaService;
import com.kaue.service.ProdutoService;
import com.kaue.service.RelatorioService;
import com.kaue.service.VendaService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

	static final String REGISTRADORA_VIEW = "page/caixa/Vender";
	static final String PAGAMENTO_VIEW = "page/caixa/Pagamento";
	static final String RECIBO_VIEW = "page/caixa/Recibo";
	static final String ADMINISTRADOR_VIEW = "page/caixa/Administracao";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private CaixaService caixaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@Autowired
	private MovimentacaoCaixaService movimentacaoCaixaService;
		
	private Venda venda;
	
	private Cliente cliente;
	
	@ModelAttribute
	public void addAttributes(Model model){
		Item item = new Item();
		item.setProduto(new Produto());
		model.addAttribute("item", item);
	}
	
	@ModelAttribute
	public List<TipoMovimentacaoCaixa> todosTipoMovimentacao(){
		return Arrays.asList(TipoMovimentacaoCaixa.values());
	}
	
	@ModelAttribute
	public List<TipoConta> todosTipoConta(){
		return Arrays.asList(TipoConta.values());
	}	
	
	@ModelAttribute
	public List<OpcoesDesconto> todasOpcoesDesconto(){
		return Arrays.asList(OpcoesDesconto.values());
	}
	
	@ModelAttribute
	public List<TipoVenda> todosTipo(){
		return Arrays.asList(TipoVenda.values());
	}
	
	@RequestMapping
	public ModelAndView showRegistradora() {
		venda = new Venda();
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW);
		
		inicializarVenda();
		
		mv.addObject("venda", venda);
		
		return mv;
	}
	
	private void inicializarVenda() {
		venda.setStatus(StatusVenda.ABERTA);
		venda.setItemList(new ArrayList<Item>());
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setSubtotal(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setSaldo(BigDecimal.ZERO);
		venda.setDataVenda(new Date());
		venda.setItemList(new ArrayList<Item>());
		venda.setDesconto(OpcoesDesconto.ZERO); 
		venda.setSubtotal(new BigDecimal(0.0));
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setTipoVenda(TipoVenda.ESPECIE);
		//venda.setCliente(new Cliente());
	}
	
	@RequestMapping(value = "/incluir/{codigo}")
	public ModelAndView incluirItem(@PathVariable("codigo") String codigo, Model model) {
		
		ModelAndView mv = null;
		mv = new ModelAndView(REGISTRADORA_VIEW+" :: #caixaForm");
		
		if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
		
		Item item = null;
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
				
			if(produto != null) {
				boolean isNew = true;
				for(Item it : venda.getItemList()) {
					if(it.getProduto().getCodigo().equals(codigo)) {
						it.setQuantidade(it.getQuantidade().add(new BigDecimal(1)));
						isNew = false;
						item = it; break;
					}
				}
				if(isNew) {
					item = new Item();
					item.setProduto(produto);
					item.setValor(produto.getValorDeVenda());
					item.setQuantidade(new BigDecimal(1));
					
					venda.getItemList().add(item);
					item.setVenda(venda);
				}
			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValor().multiply(it.getQuantidade()));
			}
			venda.setSubtotal(subtotal);
			venda.setTotal(venda.getSubtotal());
			venda.setDesconto(OpcoesDesconto.ZERO);
			venda.setValorDesconto(new BigDecimal(0.0));
		}

		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public ModelAndView incluir(@RequestBody Item i) {// HttpServletRequest request
		
		String codigo = i.getProduto().getCodigo();
		BigDecimal quantidade = i.getQuantidade();
		
		ModelAndView mv = null;
		mv = new ModelAndView(REGISTRADORA_VIEW+" :: #caixaForm");
		
		if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
		
		Item item = null;
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
				
			if(produto != null) {
				boolean isNew = true;
				for(Item it : venda.getItemList()) {
					if(it.getProduto().getCodigo().equals(codigo)) {
						it.setQuantidade(it.getQuantidade().add(quantidade));
						it.setValorTotal(it.getQuantidade().multiply(it.getValor()));
						isNew = false;
						item = it; break;
					}
				}

				if(isNew) {
					item = new Item();
					item.setProduto(produto);
					item.setValor(item.getProduto().getValorDeVenda());
					item.setValorTotal(quantidade.multiply(item.getValor()));
					item.setQuantidade(quantidade);
					
					venda.getItemList().add(item);
					item.setVenda(venda);
				} 

			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValorTotal());
			}
			venda.setSubtotal(subtotal);
			venda.setTotal(venda.getSubtotal());
			venda.setDesconto(OpcoesDesconto.ZERO);
			venda.setValorDesconto(new BigDecimal(0.0));
		}

		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
	}
	
	@RequestMapping(value = "/excluir/{codigo}")
	public ModelAndView excluirItem(@PathVariable("codigo") String codigo) {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #caixaForm");
		
		Iterator<Item> itemIterator = venda.getItemList().iterator();
		Item item = null;
		while(itemIterator.hasNext()) {
			item = itemIterator.next();
			if(item.getProduto().getCodigo().equals(codigo)) {
				itemIterator.remove();
				item.setVenda(null);
			}
		}
		
		BigDecimal subtotal = new BigDecimal(0.0);
		for(Item it : venda.getItemList()) {
			subtotal = subtotal.add(it.getValorTotal());
		}
		venda.setSubtotal(subtotal);
		venda.setTotal(venda.getSubtotal());
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		
		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
		
	}
	
	@RequestMapping(value = "/detalhes/{codigo}")
	public ModelAndView mostrarBalanca(@PathVariable("codigo") String codigo, Model model) {
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #modalQuantidade");
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
			if(produto != null) {
				Item item = new Item();
				item.setProduto(produto);
				if(item.getProduto().getUnitario()==Unitario.Kg) {
					item.setQuantidade(new BigDecimal(0));
				} else {
					item.setQuantidade(new BigDecimal(1));
				}
				
				model.addAttribute("item", item);
			}
		}
		
	    return mv;
		
	}
		
	@RequestMapping(value = "/venda/{id}")
	public ModelAndView detalhesVenda(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(RECIBO_VIEW);
		mv.addObject("venda", venda);
		return mv;
	}
	
	
	@RequestMapping(value="/venda/{id}/comprovante", method=RequestMethod.GET)
	@ResponseBody
	public void downloadFile(@PathVariable("id") Venda venda, HttpServletResponse response) {
		gerarRelatorio(venda);
		String path = "";
		try {
			path = ResourceUtils.getFile("classpath:jrxml").getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String fileName = path + "/cupom.pdf";
        response.setContentType("application/pdf");//vnd.openxmlformats-officedocument.spreadsheetml.sheet
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
            try {
                int c;
                while ((c = inputStream.read()) != -1) {
                	response.getWriter().write(c);
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.getWriter().close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@PathVariable("id") Venda venda) throws IOException {

		gerarRelatorio(venda);
		File file = ResourceUtils.getFile("classpath:jrxml/cupom.pdf");
	    Path path = Paths.get(file.getAbsolutePath());
	    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
	    
	    String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "comprovante_venda.pdf");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(headerKey, headerValue);

	    return ResponseEntity.ok()
	        .headers(httpHeaders)
	        .contentLength(file.length())
	        .contentType(MediaType.APPLICATION_OCTET_STREAM)
	        .body(resource);
	}
		
	
	@RequestMapping(value = "/pagamento", method = RequestMethod.PUT)
	public ModelAndView iniciarPagamento() {		
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		
		venda.setStatus(StatusVenda.ABERTA);
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setDataVenda(new Date());
		venda.setSaldo(BigDecimal.ZERO);
		//venda = vendaService.salvar(venda);
				
		mv.addObject("venda", venda);
		return mv;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	public String finalizarPagamento(@Validated Venda venda, Errors errors, RedirectAttributes attributes) {
		
		if(venda.getSubtotal().compareTo(BigDecimal.ZERO)==0) {
			errors.rejectValue("subtotal", null, "A lista de compras está vazia!");
		}
		
		if(venda.getTipoVenda()==TipoVenda.ESPECIE) {
			if(venda.getSaldo()==null || venda.getSaldo().compareTo(venda.getTotal())==-1) {
				errors.rejectValue("saldo", null, "Saldo insuficiente!");
			}
		} else if(venda.getTipoVenda()==TipoVenda.CONTA){
			if(HasValue.execute(venda.getCliente())) {
				if(HasValue.execute(venda.getCliente().getCadernetaAberta())) {
					venda.setSaldo(new BigDecimal(0.0));
				} else {
					errors.rejectValue("cliente", null, "O cliente selecionado não possui caderneta aberta!");
				}
			} else {
				errors.rejectValue("cliente", null, "Selecione um cliente!");
			}
			
			/*if(!HasValue.execute(venda.getCliente())) {
				errors.rejectValue("cliente", null, "Selecione um cliente!");
			} else {
				if(HasValue.execute(venda.getCliente().getCadernetaAberta())) {
					errors.rejectValue("cliente", null, "O cliente selecionado não possui caderneta aberta!");
				}
				venda.setSaldo(new BigDecimal(0.0));
			}*/
		}
		UsuarioWeb usuarioWeb = (UsuarioWeb)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		venda.setUsuario(usuarioWeb.getUsuario());
		venda.setDataVenda(new Date());
		
		if(errors.hasErrors()) {
			return REGISTRADORA_VIEW;
		} else {
			for(Item item : venda.getItemList()) { 
				item.setVenda(venda);
			}
			
			if(venda.getTipoVenda()==TipoVenda.CONTA) {
				venda.setStatus(StatusVenda.ABERTA);
				venda.setTroco(new BigDecimal(0.0));
			} else if(venda.getTipoVenda()==TipoVenda.ESPECIE) {
				venda.setStatus(StatusVenda.FINALIZADA);
				BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
				venda.setTroco(troco);
			}
			
			venda = vendaService.salvar(venda);
			List<Item> itemList = venda.getItemList();
			
			/* Atualiza a quantidade do produto */
			for(Item item : itemList) {
				if(item.getProduto().getUnitario() == Unitario.Un) {
					Produto produto = item.getProduto();
					Integer quantidadeAtual = produto.getQuantidade();
					Integer quantidadeNova = quantidadeAtual - item.getQuantidade().intValueExact();
					if(quantidadeNova < 0) {
						quantidadeNova = 0;
					}
					produto.setQuantidade(quantidadeNova);
					try {
						produto = produtoService.salvar(produto);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			attributes.addFlashAttribute("mensagem", "Venda concluída com sucesso!");
			return "redirect:/caixa/venda/"+venda.getId();
		}
	}
	
	@RequestMapping(value = "/desconto/{desconto}")
	public ModelAndView aplicarDesconto(@PathVariable("desconto") OpcoesDesconto opcao) {		
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		venda.setDesconto(opcao);
		BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
		venda.setValorDesconto(valorDesconto);
		venda.setTotal(total);

		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/buscarItem", produces = "application/json")
	public @ResponseBody Map<String, Object> obterProdutosAjax(@RequestParam("q") String q, @RequestParam("page") Integer page) throws IOException {
		
		String termo = q;
		List<Produto> produtoList = new ArrayList<Produto>();
		Long quantidadeDeProdutos = 0L;
		Long pageSize = 3L;
		if(termo != null && !termo.trim().isEmpty()) {
			ProdutoFilter filtro = new ProdutoFilter();

			try {
				Integer codigo = Integer.parseInt(termo);
				filtro.getProduto().setCodigo(String.format("%010d" , codigo));
			} catch(NumberFormatException nfe) {}

			filtro.getProduto().setDescricao(termo);
			filtro.getProduto().setCategoria(new Categoria());
			filtro.getProduto().getCategoria().setDescricao(termo);
			filtro.setPage(new Long(page.intValue()-1));
			filtro.setPageSize(pageSize);
			
			produtoList = produtoService.pesquisar(filtro);
			if(produtoList != null && produtoList.size()>0) {
				for(Produto produto : produtoList) {
					//String imagemBase64 = produtoService.carregarImagem(produto.getImagemPath());
					String imagemBase64 = produtoService.tranformarEmImagemBase64(produto.getImagem());
					produto.setImagemBase64(imagemBase64);
					/*
					 * byte[] bytes = produtoService.carregarImagem(produto.getImagemPath());
					 * if(bytes == null) { File file =
					 * ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg")
					 * ; Path path = file.toPath(); bytes = Files.readAllBytes(path); } String
					 * encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
					 * produto.setImagemBase64(encodedfile);
					 */
				}
			}
			
			quantidadeDeProdutos = produtoService.contar(filtro);
		} 
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", produtoList);
		data.put("total_count", quantidadeDeProdutos);
		data.put("page_size", pageSize);
		
		return data;
	}
	
	@RequestMapping(value = "/produto/{id}")
	public @ResponseBody Produto detalhesProduto(@PathVariable("id") Produto produto) {
		produto.setImagemBase64(produtoService.carregarImagem(produto.getImagemPath()));
		return produto;
	}
	
	@RequestMapping(value = "/buscarCliente", produces = "application/json")
	public @ResponseBody Map<String, Object> buscarCliente(@RequestParam("q") String q, @RequestParam("page") Integer page) throws IOException {
		
		String termo = q;
		List<Cliente> clienteList = new ArrayList<Cliente>();
		Long quantidadeDeClientes = 0L;
		Long pageSize = 3L;
		if(termo != null && !termo.trim().isEmpty()) {
			ClienteFilter filtro = new ClienteFilter();

			filtro.getCliente().setNome(termo);
			filtro.getCliente().setCpf(termo);
			filtro.setPage(new Long(page.intValue()-1));
			filtro.setPageSize(pageSize);
			
			clienteList = clienteService.pesquisar(filtro);
			if(clienteList != null && clienteList.size()>0) {
				for(Cliente cliente : clienteList) {
					byte[] bytes = null;//cliente.getImagem();
					if(bytes == null) {
						File file = ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg");
						Path path = file.toPath();
						bytes = Files.readAllBytes(path);
					}
					String encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
					cliente.setImagemBase64(encodedfile);
				}
			}
			
			quantidadeDeClientes = clienteService.contar(filtro);
		} 
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("clientes", clienteList);
		data.put("total_count", quantidadeDeClientes);
		data.put("page_size", pageSize);
		
		return data;
	}
	
	@RequestMapping(value = "/buscarCliente2")
	public ModelAndView pesquisarCliente(@RequestParam("termo") String termo) {
		ClienteFilter filtro = new ClienteFilter();
		if(termo!=null) {
			filtro.getCliente().setNome(termo);
			filtro.getCliente().setEmail(termo);
			try {
				Long id = Long.parseLong(termo);
				filtro.getCliente().setId(id);
			} catch(NumberFormatException nfe) {}
		}
		List<Cliente> clienteList = clienteService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+"::#modalCliente");
		mv.addObject("clientes", clienteList);
		mv.addObject("tipoVenda", venda.getTipoVenda());
		return mv;
	}
	
	/*@RequestMapping(value = "/selecionarCliente/{id}")
	public @ResponseBody Cliente detalhesCliente(@PathVariable("id") Cliente cliente) {
		venda.setCliente(cliente);
		return cliente;
	}*/
	
	@RequestMapping(value = "/selecionarCliente/{id}")
	public ModelAndView detalhesCliente(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		venda.setCliente(cliente);
		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/removerCliente")
	public ModelAndView removerCliente() {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		venda.setCliente(null);
		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/formaPagamento/{opcao}")
	public ModelAndView aplicarFormaPAgamento(@PathVariable("opcao") TipoVenda tipoVenda) {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		venda.setTipoVenda(tipoVenda);
		if(tipoVenda==TipoVenda.CONTA) {
			venda.setDesconto(OpcoesDesconto.ZERO);
			BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
			venda.setValorDesconto(valorDesconto);
			venda.setTotal(total);
		}
		mv.addObject("venda", venda);
		return mv;
	}
	
	/*@RequestMapping(value = "/removerCliente")
	public @ResponseBody Cliente removerCliente() {
		venda.setCliente(null);
		return null;
	}*/
	
	
	
	
	
	public void gerarRelatorio(Venda venda) {
		try {
			List<Registro> registroList = new ArrayList<Registro>();
			Registro registro = null;
			List<Item> itemList = venda.getItemList();
			int count = 0;
			for(Item item : itemList) {
				registro = new Registro();
				registro.setCodigo(item.getProduto().getCodigo());
				registro.setDescricao(item.getProduto().getDescricao().toUpperCase());
				registro.setQuantidade(item.getQuantidade().toString().replace(".", ","));
				registro.setValorUnitario(item.getValor().toString().replace(".", ","));
				registro.setValorItem(item.getValorTotal().toString().replace(".", ","));
				registro.setUnitario(item.getProduto().getUnitario().getSigla());
				registro.setPosicao(String.format("%03d", ++count));
				registroList.add(registro);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("valorTotalItem", venda.getSubtotal().toString());
			param.put("valorDesconto", venda.getValorDesconto().toString());
			param.put("valorTotal", venda.getTotal().toString());
			param.put("data", venda.getDataVenda());
			
			String path = ResourceUtils.getFile("classpath:jrxml").getAbsolutePath();
			String origem = path + "/cupom.jrxml";
			String destino = path + "/cupom.pdf";
			String sub = path + "/sub-cupom.jrxml";
			relatorioService.gerarRelatorioEmPdf(registroList, param, origem, sub, destino);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/abrirCaixa")
	public String abrirCaixa(@Validated @ModelAttribute("caixa") Caixa caixa, 
			Errors errors, RedirectAttributes attributes, Model model) {
		
		try {
			if(errors.hasErrors()) { 
				throw new Exception();
			} 
			caixa.setValorGaveta(caixa.getValorInicial()); //inicia a gaveta do caixa com o valor inicial
			caixa = caixaService.salvar(caixa);
			attributes.addFlashAttribute("mensagem", "Caixa aberto com sucesso!");
			return "redirect:/caixa/administracao";
		} catch (Exception e) {
			model.addAttribute("movimentacao", inicializaNovaMovimentacaoCaixa());
			model.addAttribute("caixaAtual", caixaService.obterCaixaMaisRecente());
			return ADMINISTRADOR_VIEW;
		}
	}
	
	@RequestMapping(value = "/fecharCaixa/{id}")
	public String fecharCaixa(@PathVariable("id") Caixa caixa, RedirectAttributes attributes) {
				
		caixa.setStatus(StatusCaixa.FECHADO);
		caixa.setDataFechamento(new Date());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioLogado = ((UsuarioWeb)auth.getPrincipal()).getUsuario();
		caixa.setUsuarioFechamento(usuarioLogado);
		
		/*
		 * BigDecimal valorInicial = caixa.getValorInicial(); BigDecimal entradas =
		 * caixa.getValorEntradas()!=null?caixa.getValorEntradas():new BigDecimal(0.00);
		 * BigDecimal saidas = caixa.getValorSaidas()!=null?caixa.getValorSaidas():new
		 * BigDecimal(0.00);
		 * 
		 * BigDecimal valorEmGaveta = valorInicial.add(entradas).subtract(saidas);
		 */
		
		try {
			caixa = caixaService.salvar(caixa);
			attributes.addFlashAttribute("mensagem", "Caixa fechado com sucesso!");
			return "redirect:/caixa/administracao";
		} catch (Exception e) {
			return ADMINISTRADOR_VIEW;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/realizarSangria")
	public String sangriaCaixa(@Validated @ModelAttribute("movimentacao") MovimentacaoCaixa movimentacao, 
			Errors errors, RedirectAttributes attributes, 
			@ModelAttribute("caixa") Caixa caixa,
			@ModelAttribute("caixaAtual") Caixa caixaAtual) {
		
		if(errors.hasErrors()) { 
			return ADMINISTRADOR_VIEW; 
		} 
		
		try {
			movimentacao = movimentacaoCaixaService.salvar(movimentacao);
			attributes.addFlashAttribute("mensagem", "Movimentação realizada com sucesso!");
			return "redirect:/caixa/administracao";
		} catch (Exception e) {
			errors.reject(e.getMessage());
			return ADMINISTRADOR_VIEW;
		}
	}
	
	@RequestMapping(value = "/administracao")
	public String initCaixa(Model model) {
		
		//Lembre: cadastrar um registro inicial na tabela Caixa.
		Caixa caixaAtual = caixaService.obterCaixaMaisRecente();
		Caixa caixa = inicializaNovoCaixa(caixaAtual);
		MovimentacaoCaixa movimentacaoCaixa = inicializaNovaMovimentacaoCaixa();
		
		model.addAttribute("caixa", caixa);
		model.addAttribute("caixaAtual", caixaAtual);
		model.addAttribute("movimentacao", movimentacaoCaixa);
		return ADMINISTRADOR_VIEW;
	}
	
	@RequestMapping(value = "/abrir")
	public @ResponseBody Map<String, Object> obterData() {
		Map<String, Object> map = new HashMap<>();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		map.put("date", formattedDate);
		return map;
	}
	
	@RequestMapping(value = "/fechar")
	public @ResponseBody Map<String, Object> calcular() {
		Map<String, Object> map = new HashMap<>();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		map.put("date", formattedDate);
		return map;
	}
	
	private Caixa inicializaNovoCaixa(Caixa caixaAtual) {
		Caixa caixa = new Caixa();
		caixa.setNumero(1);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioLogado = ((UsuarioWeb)auth.getPrincipal()).getUsuario();
		caixa.setUsuarioAbertura(usuarioLogado);
		//caixa.setDataAbertura(new Date());
		caixa.setStatus(StatusCaixa.ABERTO);
		caixa.setValorInicial(caixaAtual.getValorGaveta());
		
		return caixa;
	}
	
	private MovimentacaoCaixa inicializaNovaMovimentacaoCaixa() {
		MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
		Caixa caixaAtual = caixaService.obterCaixaMaisRecente();
		movimentacaoCaixa.setCaixa(caixaAtual);
		return movimentacaoCaixa;
	}
	
}
