//eliminar carpeta de imagenes uploads
package com.gian.springboot.app.panaderia.panaderiabackend.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    public void eliminarCarpetaUploads() throws IOException {
        Path uploadsPath = Paths.get("uploads");

        if (Files.exists(uploadsPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(uploadsPath)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                }
            }
            Files.delete(uploadsPath);
        }
    }


    public void crearCarpetaUploads() throws IOException {
        Path uploadsPath = Paths.get("uploads");

        if (!Files.exists(uploadsPath)) {
            Files.createDirectory(uploadsPath);
        }
    }
}


