package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.config.FileUploadConfig;
import br.com.restwithspringbootandjavaerudio.exception.FileStorageExeception;
import br.com.restwithspringbootandjavaerudio.exception.MyFileNotFoundExeception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    @Autowired
    public FileStorageService(FileUploadConfig config) {
        Path path = Paths.get(config.getUploadDir())
                .toAbsolutePath()
                .normalize();
        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception e) {
            throw new FileStorageExeception("Could not create the directory.", e);
        }
    }

    public String storageFile (MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            //Regras de validação:
            if(fileName.contains("..")){
                throw new FileStorageExeception("File name contains invalid char sequence: "+ fileName);
            }
            //Gravação do arquivo em disco:
            //Encontra o caminho absoluto para o arquivo
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            //Copia rescrevendo se ele ja existir
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


        }catch (Exception e) {
            throw new FileStorageExeception("Could not Store the file: "+fileName, e);
        }

        return fileName;
    }

    public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) return resource;
            else throw  new MyFileNotFoundExeception("File not found");

        }catch (Exception e){
            throw new MyFileNotFoundExeception("Impossible to load a file with this name "+fileName, e);
        }
    }
}
