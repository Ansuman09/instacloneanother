package com.api.instaclone.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/get-images")
public class ImageController {

    
    private final Path imageDirectory = Paths.get("/home/ansuman/Documents/InstaImageBucket");
    
    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) {
        try {
            Path filePath = imageDirectory.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            // Determine the content type of the image
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback if content type cannot be determined
            }

            if (resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set Content-Type
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"") // Inline display with filename
                    .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Image not found or not readable
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle any other errors
        }
    }
}
