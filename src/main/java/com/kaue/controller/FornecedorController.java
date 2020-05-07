package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.enumeration.Estado;
import com.kaue.model.Endereco;
import com.kaue.model.Fornecedor;
import com.kaue.service.FornecedorService;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {
	
	@Autowired
	private FornecedorService fornecedorService;
	
	private static final String CADASTRAR_VIEW = "page/fornecedor/Cadastrar";
	private static final String LISTAR_VIEW = "page/fornecedor/Listar";
	
	@RequestMapping(value = "/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("fornecedor", new Fornecedor());
		return mv;
	}
	
	@RequestMapping(value = "{codigo}")
	public ModelAndView showFormEditar(@PathVariable("codigo") Fornecedor fornecedor) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("fornecedor", fornecedor);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Fornecedor fornecedor, Errors errors, RedirectAttributes attr) {
		
		if(!isValidated(fornecedor, errors)) {
			return CADASTRAR_VIEW;
		}
		if(isEnderecoEmpty(fornecedor.getEndereco())) {
			fornecedor.setEndereco(null);
		}
		
		fornecedorService.salvar(fornecedor);
		attr.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso!");
		return "redirect:/fornecedores/novo";
	}
	
	private boolean isValidated(Fornecedor fornecedor, Errors errors) {
		
		//Valida os campos de cnpj e telefone
		String cnpj = fornecedor.getCnpj();
		cnpj = cnpj.replaceAll("[_./-]", "");
		if(cnpj.length() > 0 && cnpj.length() < 14) {
			errors.rejectValue("cnpj", null, "Cnpj inválido");
		}
		String telefone = fornecedor.getTelefone();
		telefone = telefone.replaceAll("[_()-]", "");
		if(telefone.length() > 0 && telefone.length() < 10) {
			errors.rejectValue("telefone", null, "Telefone inválido");
		}
		String cep = fornecedor.getEndereco() != null ? fornecedor.getEndereco().getCep() : "";
		cep = cep.replaceAll("[_-]", "");
		if(cep.length() > 0 && cep.length() < 8) {
			errors.rejectValue("endereco.cep", null, "Cep inválido");
		}
		
		if(errors.hasErrors()) {
			return false;
		}
		
		return true;
	}
	
	private boolean isEnderecoEmpty(Endereco endereco) {
		
		if(endereco.getBairro().isEmpty()
				&& endereco.getCep().isEmpty()
				&& endereco.getCidade().isEmpty()
				&& endereco.getEstado()==null
				&& endereco.getLogradouro().isEmpty()
				&& endereco.getNumero().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") FornecedorFilter filtro) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		List<Fornecedor> fornecedorList = fornecedorService.pesquisar(filtro);
		mv.addObject("fornecedores", fornecedorList);
		return mv;
	}
	
	@RequestMapping(value = "{codigo}",method = RequestMethod.DELETE)
	public String excluir(@PathVariable("codigo") Long codigo, RedirectAttributes attr) {
		fornecedorService.excluir(codigo);
		attr.addFlashAttribute("mensagem","Registro excluído com sucesso!");
		return "redirect:/fornecedores";
	}
	
	@ModelAttribute
	public List<Estado> todosEstados() {
		return Arrays.asList(Estado.values());
	}
	
}
