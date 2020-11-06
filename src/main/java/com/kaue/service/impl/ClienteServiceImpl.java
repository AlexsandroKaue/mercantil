package com.kaue.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.ClienteDAO;
import com.kaue.dao.ProdutoDAO;
import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Cliente;
import com.kaue.service.ClienteService;
import com.kaue.util.HasValue;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Autowired
	private ClienteDAO clienteDAO;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		try {
			return clienteDAO.save(cliente);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Transactional
	public void excluir(Long id) {
		clienteDAO.deleteById(id);
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
		byte[] bytes = file.getBytes();
		if(bytes!=null) {
			String contentType = file.getContentType();
			if(!(contentType.equals("image/jpg")
					||contentType.equals("image/jpeg")
					||contentType.equals("image/png")
					||contentType.equals("image/gif"))) 
			{
				throw new IOException("Formato de arquivo incorreto. \n Apenas jpg/jpeg, png ou gif são válidos.");
			}
			
			String filename = nome +".png";
			Path resourcePath = Paths.get("./src/main/resources/static/custom/img/cliente/"+filename);
			Path absolutePath = resourcePath.toAbsolutePath();
			BufferedImage croppedImage = cropImage(bytes);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(croppedImage, "png", baos); 
			bytes = baos.toByteArray();
			
			Files.write(absolutePath, bytes);
		    return filename;
		}
		return null;
	}
	
	private BufferedImage cropImage(byte[] image) throws IOException {
	  // Get a BufferedImage object from a byte array
	  InputStream in = new ByteArrayInputStream(image);
	  BufferedImage originalImage = ImageIO.read(in);
	  
	  if(!HasValue.execute(originalImage)) {
		  throw new IOException("Arquivo corrompido.");
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

	@Override
	public Long obterMaxId() {
		return null;
	}

	@Override
	public String carregarImagem(String nome) {
		Path resourcePath = Paths.get("./src/main/resources/static/custom/img/produto/"+nome);
		Path absolutePath = resourcePath.toAbsolutePath();
	    return tranformarEmImagemBase64(absolutePath);
	}
	
}
