package com.gian.springboot.app.panaderia.panaderiabackend;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.services.InventarioProductoService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyStartupService implements CommandLineRunner {
    private final ProductoService productoService;
  private  final InventarioProductoService inventarioProductoService;
    @Autowired
    public MyStartupService(ProductoService productoService,
                            InventarioProductoService inventarioProductoService) {
        this.productoService = productoService;
        this.inventarioProductoService = inventarioProductoService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executing startup logic...");
        productoService.crearProductos();

        for (int i = 1; i <= 47; i++) {
            RegistroInventarioProductoDTO  registroInventarioProductoDTO = new RegistroInventarioProductoDTO();
            registroInventarioProductoDTO.setProductoId((long) i);
            registroInventarioProductoDTO.setCantidad(10);

            inventarioProductoService.agregarInventario(registroInventarioProductoDTO);

        }
    }
}
