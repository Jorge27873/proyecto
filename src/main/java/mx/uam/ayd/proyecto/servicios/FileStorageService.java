package mx.uam.ayd.proyecto.servicios;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	String storeFile(MultipartFile file) throws IOException;
	Resource loadFileAsResource(String fileName);
}