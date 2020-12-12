package com.kaue.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager {
	
	@Autowired
    ResourceLoader resourceLoader;
	
	public String salvarImagem(MultipartFile file, String nome, String repository) throws Exception {
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
			Resource resource = resourceLoader.getResource(repository);
			FileSystemResource fsResource = new FileSystemResource(resource.getURI().getPath()+"/"+filename);
			String absolutePath = fsResource.getFile().getAbsolutePath();
			Path path = Paths.get(absolutePath);
			
			BufferedImage croppedImage = cropImage(bytes);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(croppedImage, "png", baos); 
			bytes = baos.toByteArray();
			
			Files.write(path, bytes);
		    return filename;
		}
		return null;
	}
	
	public String carregarImagem(String nome, String repository) {
		try {
			Resource resource = resourceLoader.getResource(repository);
			if(HasValue.execute(nome)) {
				FileSystemResource fsResource = new FileSystemResource(resource.getURI().getPath()+"/"+nome);
				String absolutePath = fsResource.getFile().getAbsolutePath();
				Path path = Paths.get(absolutePath);
			    return tranformarEmImagemBase64(path);
			} else {
				return buscarImagemPadrao();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
			Resource resource = resourceLoader.getResource("classpath:static/custom/img/sem-imagem_2.jpg");
			FileSystemResource fsResource = new FileSystemResource(resource.getURI().getPath());
			String absolutePath = fsResource.getFile().getAbsolutePath();
			Path path = Paths.get(absolutePath);
			byte[] bytes = Files.readAllBytes(path);
			
			encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encodedfile;
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

}
