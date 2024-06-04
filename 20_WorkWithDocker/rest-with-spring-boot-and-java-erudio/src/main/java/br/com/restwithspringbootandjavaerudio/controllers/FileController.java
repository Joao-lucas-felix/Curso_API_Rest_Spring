package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.DataTransfers.UploadFileResponseDto;
import br.com.restwithspringbootandjavaerudio.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/file/v1")
@Tag(name = "Files", description = "EndPoint For manage Files")
public class FileController {

    private final Logger logger = Logger.getLogger(FileController.class.getName());
    @Autowired
    private FileStorageService service;


    @Operation(summary = "Upload a file",
            description = "Upload a file",
            tags = "File",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PostMapping(value = "/upload")
    public UploadFileResponseDto upload(
            @RequestParam("file") MultipartFile file
            ){
        logger.info("Storing a file in disk.");

        String fileName = service.storageFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/file/v1/download/"+fileName).toUriString();

        return new UploadFileResponseDto(
                fileName, fileDownloadUri, file.getContentType(), file.getSize()
        );
    }

    @Operation(summary = "Upload multiples files",
            description = "Upload multiples files",
            tags = "File",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PostMapping(value = "/uploadMultipleFiles")
    public List<UploadFileResponseDto> uploadMultiples(
            @RequestParam MultipartFile[] files
    ){
        return Arrays
                .stream(files)
                .toList()
                .stream()
                .map(this::upload)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Download a file",
            description = "Download a file",
            tags = "File",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })

    @GetMapping(value = "/download/{fileName:.+}")
    public ResponseEntity<Resource> download(
            @PathVariable String fileName, HttpServletRequest request
    ) {
        logger.info("Loading a file as Resource");
        Resource resource = service.loadFileAsResource(fileName);

        String contentType = "";

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception e){
            logger.info("Do not determine the file content type");
        }
        if (contentType.isBlank()) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }


}
