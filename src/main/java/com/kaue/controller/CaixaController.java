package com.kaue.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
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
import com.kaue.enumeration.StatusVenda;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Categoria;
import com.kaue.model.Cliente;
import com.kaue.model.Item;
import com.kaue.model.Produto;
import com.kaue.model.Registro;
import com.kaue.model.Venda;
import com.kaue.service.ClienteService;
import com.kaue.service.ProdutoService;
import com.kaue.service.RelatorioService;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

	static final String REGISTRADORA_VIEW = "page/caixa/Vender";
	static final String PAGAMENTO_VIEW = "page/caixa/Pagamento";
	static final String RECIBO_VIEW = "page/caixa/Recibo";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private RelatorioService relatorioService;
	
	private Venda venda;
	
	private Cliente cliente;
	
	@ModelAttribute
	public void addAttributes(Model model){
		Item item = new Item();
		item.setProduto(new Produto());
		model.addAttribute("item", item);
		model.addAttribute("teste", "Teste de passagem de valor do thymeleaf para o javascript.");
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
	
	@RequestMapping(value = "/balanca/{codigo}")
	public ModelAndView mostrarBalanca(@PathVariable("codigo") String codigo, Model model) {
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #modal-balanca");
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
			if(produto != null) {
				Item item = new Item();
				item.setProduto(produto);
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
	
		
	
	@RequestMapping(value = "/pagamento", method = RequestMethod.PUT)
	public ModelAndView iniciarPagamento() {		
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #caixaForm");
		
		venda.setStatus(StatusVenda.ABERTA);
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setDataVenda(new Date());
		venda.setSaldo(BigDecimal.ZERO);
		venda = vendaService.salvar(venda);
		
		mv.addObject("venda", venda);
		return mv;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	public String finalizarPagamento(@Validated Venda venda, Errors errors, RedirectAttributes attributes) {
		
		if(venda.getSubtotal().compareTo(BigDecimal.ZERO)==0) {
			errors.rejectValue("subtotal", null, "Erro: A lista não pode estar vazia!");
		}
		
		if(venda.getSaldo()!=null) {
			if(venda.getSaldo().compareTo(venda.getTotal())==-1 && venda.getCliente() == null) {
				errors.rejectValue("saldo", null, "Erro: Saldo insuficiente!");
			}
		}
		venda.setDataVenda(new Date());
		
		if(errors.hasErrors()) {
			return REGISTRADORA_VIEW;
		} else {
			for(Item item : venda.getItemList()) { 
				item.setVenda(venda);
			}
			venda.setStatus(StatusVenda.FINALIZADA);
			BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
			venda.setTroco(troco);
			
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
					byte[] bytes = produto.getImagem();
					if(bytes == null) {
						File file = ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg");
						Path path = file.toPath();
						bytes = Files.readAllBytes(path);
					}
					String encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
					produto.setImagemBase64(encodedfile);
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
	
	@RequestMapping(value = "/selecionarCliente/{id}")
	public ModelAndView selecionarCliente(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #caixaForm");
		/* this.cliente = cliente; */
		venda.setCliente(cliente);
		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/removerCliente")
	public ModelAndView removerCliente() {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #caixaForm");
		venda.setCliente(null);
		mv.addObject("venda", venda);
		return mv;
	}
	
	@ModelAttribute
	public List<OpcoesDesconto> todasOpcoesDesconto(){
		return Arrays.asList(OpcoesDesconto.values());
	}
	
	@ModelAttribute
	public List<Cliente> todosClientes(){
		List<Cliente> clienteList = new ArrayList<Cliente>();
		clienteList = clienteService.pesquisar(new ClienteFilter());
		return clienteList;
	}
	
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
			
			String path = ResourceUtils.getFile("classpath:jrxml").getAbsolutePath();
			String origem = path + "/cupom.jrxml";
			String destino = path + "/cupom.pdf";
			String sub = path + "/sub-cupom.jrxml";
			relatorioService.gerarRelatorioEmPdf(registroList, param, origem, sub, destino);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/administracao")
	public ModelAndView acessarAdministracao() {
		ModelAndView mv = new ModelAndView("page/caixa/Administracao");
		mv.addObject("caixa", new Venda());
		
		return mv;
	}
}
