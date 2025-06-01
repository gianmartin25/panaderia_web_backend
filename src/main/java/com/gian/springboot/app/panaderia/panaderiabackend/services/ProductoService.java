package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Categoria;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Proveedor;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.CategoriaRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;

    // Guardar producto y su imagen
    public ProductoDTO guardarProducto(RegistroProductoDTO registroProductoDTO, MultipartFile imagen) throws IOException {
        // Check if a product with the same name already exists
        if (productoRepository.existsByNombreIgnoreCase(registroProductoDTO.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + registroProductoDTO.getNombre());
        }

        Producto producto = new Producto();

        if (!imagen.isEmpty()) {
            // Guardar la imagen en el sistema de archivos o en la base de datos
            String nombreImagen = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Path imagenPath = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
            Files.copy(imagen.getInputStream(), imagenPath);
            Proveedor proveedor = proveedorRepository.findById(registroProductoDTO.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            Categoria categoria = categoriaRepository.findById(registroProductoDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

            producto.setNombre(registroProductoDTO.getNombre());
            producto.setDescripcion(registroProductoDTO.getDescripcion());
            producto.setPrecio(registroProductoDTO.getPrecio());
            producto.setProveedor(proveedor);
            producto.setImageUrl(nombreImagen);
            producto.setCategoria(categoria);
        }
        Producto productoGuardado = productoRepository.save(producto);
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(productoGuardado.getId());
        productoDTO.setNombre(productoGuardado.getNombre());
        productoDTO.setDescripcion(productoGuardado.getDescripcion());
        productoDTO.setPrecio(productoGuardado.getPrecio());
        productoDTO.setImageUrl(productoGuardado.getImageUrl());
        productoDTO.setCategoriaId(productoGuardado.getCategoria().getId());
        productoDTO.setCategoriaNombre(productoGuardado.getCategoria().getNombre());
        productoDTO.setProveedorId(productoGuardado.getProveedor().getId());
        productoDTO.setProveedorNombre(productoGuardado.getProveedor().getNombre());
        productoDTO.setTotalCantidad(0);
        return productoDTO;
    }

    public Map<String, Object> buscarProductosPorNombreYCategoria(int page, int size, String categoriaId, String nombre, String sortBy, String sortDirection) {
        Pageable pageable = PageRequest.of(page - 1, size,
                sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<Producto> productosPage;

        if (categoriaId.equals("1")) {
            productosPage = productoRepository.findByNombreContainingIgnoreCaseAndTotalCantidadGreaterThan(nombre, 0, pageable);
        } else {
            productosPage = productoRepository.findByNombreContainingIgnoreCaseAndCategoriaIdAndTotalCantidadGreaterThan(nombre, Long.parseLong(categoriaId), 0, pageable);
        }

        List<ProductoDTO> productos = productosPage.getContent().stream().map(producto -> {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setImageUrl("http://localhost:8080/api/uploads/" + producto.getImageUrl());
            productoDTO.setCategoriaId(producto.getCategoria().getId());
            productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
            productoDTO.setProveedorId(producto.getProveedor().getId());
            productoDTO.setProveedorNombre(producto.getProveedor().getNombre());
            productoDTO.setTotalCantidad(producto.getTotalCantidad());
            return productoDTO;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("products", productos);
        response.put("currentPage", productosPage.getNumber() + 1);
        response.put("totalItems", productosPage.getTotalElements());
        response.put("totalPages", productosPage.getTotalPages());
        response.put("categoriaId", categoriaId);
        response.put("sortBy", sortBy);
        response.put("sortDirection", sortDirection);

        return response;
    }

    public boolean verificarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return producto.getTotalCantidad() >= cantidad;
    }

    public List<ProductoDTO> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(producto -> {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setCategoriaId(producto.getCategoria().getId());
            productoDTO.setImageUrl(producto.getImageUrl());
            productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
            productoDTO.setProveedorId(producto.getProveedor().getId());
            productoDTO.setProveedorNombre(producto.getProveedor().getNombre());
            productoDTO.setTotalCantidad(producto.getTotalCantidad());
            return productoDTO;
        }).collect(Collectors.toList());
    }

    public List<ProductoDTO> filtrarProductosPorNombre(String nombre) {
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
        return productos.stream().map(producto -> {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setImageUrl(producto.getImageUrl());
            productoDTO.setCategoriaId(producto.getCategoria().getId());
            productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
            productoDTO.setProveedorId(producto.getProveedor().getId());
            productoDTO.setProveedorNombre(producto.getProveedor().getNombre());
            return productoDTO;
        }).collect(Collectors.toList());
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto =  productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setImageUrl("http://localhost:8080/api/uploads/" + producto.getImageUrl());
        productoDTO.setCategoriaId(producto.getCategoria().getId());
        productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
        productoDTO.setProveedorId(producto.getProveedor().getId());
        productoDTO.setProveedorNombre(producto.getProveedor().getNombre());
        productoDTO.setTotalCantidad(producto.getTotalCantidad());
        return productoDTO;
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }


    public void crearProductos() {
        // Ruta de la carpeta donde están las imágenes

        List<RegistroProductoDTO> productos = List.of(
                new RegistroProductoDTO("Torta Tres Leches", "La torta tres leches es un postre clásico y delicioso originario de América Latina, conocido por su textura suave y su sabor dulce y cremoso. Se elabora con un bizcocho esponjoso que, tras ser horneado, se empapa con una mezcla de tres tipos de leche: leche evaporada, leche condensada y crema de leche. Esta combinación le da su característica humedad y una riqueza única.\n" +
                        "\n" +
                        "El bizcocho generalmente no lleva mantequilla, lo que permite que absorba mejor el líquido. Una vez impregnada con las leches, la torta se cubre con crema chantilly o merengue y, a menudo, se decora con canela en polvo, frutas frescas, o caramelo. Es un postre ideal para celebraciones y reuniones, conocido por su sabor dulce y su irresistible textura.", new BigDecimal(40), 2L, 1L),
                new RegistroProductoDTO("Torta de Chocolate", "La torta de chocolate es un postre indulgente y popular en todo el mundo, apreciado por su intenso sabor a cacao y su textura suave y húmeda. Se prepara con una base de bizcocho hecho a partir de ingredientes como harina, azúcar, cacao en polvo, huevos, leche y mantequilla o aceite, que le dan su característica consistencia y riqueza.\n" +
                        "\n" +
                        "El bizcocho puede ser acompañado o relleno con diferentes capas de crema de chocolate, ganache, frosting de chocolate o incluso mermelada, según el estilo y el gusto personal. Su decoración puede incluir virutas de chocolate, frutas frescas, nueces o glaseados. La torta de chocolate es un clásico infaltable para cualquier tipo de celebración, ya que su sabor universalmente amado la convierte en una opción favorita para todas las edades.", new BigDecimal(50), 2L, 1L),
                new RegistroProductoDTO("Soúfle de Fresas", "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 3", new BigDecimal(30), 2L, 1L),
                new RegistroProductoDTO("Torta Doble de Chocolate", "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 4", new BigDecimal(60), 2L, 1L),
                new RegistroProductoDTO("Torta Florinda", "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 5", new BigDecimal(70), 2L, 1L),
                new RegistroProductoDTO("Torta Tres Leches Café","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 6",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Torta Selva Negra","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 7",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Torta Carlota (Torta Helada)","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. 8",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Torta Sonic","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Torta Soni","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. ",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Torta de Guanábana","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(50),2L,1L),
                new RegistroProductoDTO("Coca Cola","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),3L,1L),
                new RegistroProductoDTO("Agua Mineral San Mateo","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),3L,1L),
                new RegistroProductoDTO("Agua Mineral San Mate","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),3L,1L),
                new RegistroProductoDTO("Gaseosa Sprite","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),3L,1L),
                new RegistroProductoDTO("Bebida Rehidratante Gatorade Marandina","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),3L,1L),
                new RegistroProductoDTO("Gaseosa Pepsi Black","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),3L,1L),
                new RegistroProductoDTO("Gaseosa Guaranita","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),3L,1L),
                new RegistroProductoDTO("Agua Cielo","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),3L,1L),
                new RegistroProductoDTO("Bebida Energizante Volt Ginseng","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),3L,1L),
                new RegistroProductoDTO("Bebida de Durazno Frugos del Valle","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(7),3L,1L),
                new RegistroProductoDTO("Bebida Rehidratante Powerade Multifrutas","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),3L,1L),
                new RegistroProductoDTO("Bebida Cifrut","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),3L,1L),
                new RegistroProductoDTO("Alfajores de Maicena","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),4L,1L),
                new RegistroProductoDTO("Crema Volteada","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(6),4L,1L),
                new RegistroProductoDTO("Pie de Manzana","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(6),4L,1L),
                new RegistroProductoDTO("Suspiro a la limeña","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),4L,1L),
                new RegistroProductoDTO("Pionono de manjar","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),4L,1L),
                new RegistroProductoDTO("Tocino del Cielo","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(3),4L,1L),
                new RegistroProductoDTO("Mini cupcake de chocoteja","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),4L,1L),
                new RegistroProductoDTO("Empanada de Carne","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(6),4L,1L),
                new RegistroProductoDTO("Budín pan chico","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(7),4L,1L),
                new RegistroProductoDTO("Keke de zanahoria","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(7),4L,1L),
                new RegistroProductoDTO("Keke de plátano","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(7),4L,1L),
                new RegistroProductoDTO("Pie de limón","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),4L,1L),
                new RegistroProductoDTO("Pan mini Croissant","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Croissant","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(4),5L,1L),
                new RegistroProductoDTO("Pan Ciabatta","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Francés","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Baguette","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Ciabatta Integral","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Chapla","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan de Hamburguesa","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Campesino Blanco","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Multigrano de masa madre","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Loncherita","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L),
                new RegistroProductoDTO("Pan Caracol","Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",new BigDecimal(5),5L,1L)
                );





        File carpetaImagenes = new File("seed/images-products");

        // Obtener la lista de archivos de imagen en la carpeta
        File[] archivosImagenes = carpetaImagenes.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (archivosImagenes == null || archivosImagenes.length == 0) {
            System.out.println("No se encontraron imágenes en la carpeta: " + carpetaImagenes.getPath());
            return;
        }

        int j = 0;
        for (RegistroProductoDTO registroProductoDTO : productos) {
            try {
                File archivoImagen = archivosImagenes[j];
                MultipartFile imagen = convertirArchivoAMultipartFile(archivoImagen);
                this.guardarProducto(registroProductoDTO, imagen);
                j++;
            } catch (IOException e) {
                System.out.println("Error al guardar el producto " + registroProductoDTO.getNombre() + ": " + e.getMessage());
            }
        }



        // Recorrer los archivos de imagen y crear productos
//        for (int i = 0; i < archivosImagenes.length; i++) {
//            File archivoImagen = archivosImagenes[i];
//
//            RegistroProductoDTO registroProductoDTO = new RegistroProductoDTO();
//            registroProductoDTO.setNombre("Producto " + i);
//            registroProductoDTO.setDescripcion("Descripción del producto " + i);
//            registroProductoDTO.setPrecio(new BigDecimal(1000 + i));
//            registroProductoDTO.setCategoriaId(1L);
//            registroProductoDTO.setProveedorId(1L);
//
//            try {
//                // Convertir el archivo en MultipartFile para enviarlo a guardarProducto
//                MultipartFile imagen = convertirArchivoAMultipartFile(archivoImagen);
//                this.guardarProducto(registroProductoDTO, imagen);
//            } catch (IOException e) {
//                System.out.println("Error al guardar el producto " + i + " con la imagen " + archivoImagen.getName() + ": " + e.getMessage());
//            }
//        }
    }

    // Método de utilidad para convertir un archivo en MultipartFile
    private MultipartFile convertirArchivoAMultipartFile(File archivo) throws IOException {
        Path path = archivo.toPath();
        String nombreArchivo = archivo.getName();
        String contentType = Files.probeContentType(path);
        byte[] contenido = Files.readAllBytes(path);
        return new MultipartFile() {
            @Override
            public String getName() {
                return nombreArchivo;
            }

            @Override
            public String getOriginalFilename() {
                return nombreArchivo;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return contenido.length == 0;
            }

            @Override
            public long getSize() {
                return contenido.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return contenido;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(archivo);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.copy(path, dest.toPath());
            }
        };
    }


}
