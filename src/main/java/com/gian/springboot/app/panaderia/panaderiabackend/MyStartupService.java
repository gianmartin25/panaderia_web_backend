package com.gian.springboot.app.panaderia.panaderiabackend;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ImageService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.InventarioProductoService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyStartupService implements CommandLineRunner {
    private final ProductoService productoService;
    private final InventarioProductoService inventarioProductoService;
    private  final ImageService imageService;

    @Autowired
    public MyStartupService(ProductoService productoService,
                            InventarioProductoService inventarioProductoService, ImageService imageService) {
        this.productoService = productoService;
        this.inventarioProductoService = inventarioProductoService;
        this.imageService = imageService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executing startup logic...");
        imageService.eliminarCarpetaUploads();
        imageService.crearCarpetaUploads();
        productoService.crearProductos();

        for (int i = 1; i <= 47; i++) {
            RegistroInventarioProductoDTO registroInventarioProductoDTO = new RegistroInventarioProductoDTO();
            registroInventarioProductoDTO.setProductoId((long) i);
            registroInventarioProductoDTO.setCantidad(10);

            inventarioProductoService.agregarInventario(registroInventarioProductoDTO);

        }
    }
}
