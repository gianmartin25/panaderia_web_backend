package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.OrdenResponseDto;
import com.gian.springboot.app.panaderia.panaderiabackend.services.OrdenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenController.class)
class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @Test
    @WithMockUser // Simula un usuario autenticado
    void testObtenerTodasLasOrdenes() throws Exception {
        // Arrange
        List<OrdenResponseDto> ordenes = new ArrayList<>();
        OrdenResponseDto orden = new OrdenResponseDto();
        orden.setId(1L);
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion("2023-10-01");
        orden.setTotal("100.00");
        orden.setPagado(false);
        orden.setCliente("Juan Perez");
        ordenes.add(orden);

        when(ordenService.obtenerTodasLasOrdenes()).thenReturn(ordenes);

        // Act & Assert
        mockMvc.perform(get("/ordenes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"))
                .andExpect(jsonPath("$[0].fechaCreacion").value("2023-10-01"))
                .andExpect(jsonPath("$[0].total").value("100.00"))
                .andExpect(jsonPath("$[0].pagado").value(false))
                .andExpect(jsonPath("$[0].cliente").value("Juan Perez"));
    }
}