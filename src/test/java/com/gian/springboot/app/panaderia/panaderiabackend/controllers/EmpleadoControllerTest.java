package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.EmpleadoDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroEmpleadoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Empleado;
import com.gian.springboot.app.panaderia.panaderiabackend.services.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    @WithMockUser
    void testRegistrarEmpleado() throws Exception {
        // Arrange
        RegistroEmpleadoDTO registroEmpleadoDTO = new RegistroEmpleadoDTO();
        registroEmpleadoDTO.setNombres("Juan");
        registroEmpleadoDTO.setApellidos("Perez");

        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setId(1L);
        empleadoDto.setNombres("Juan");
        empleadoDto.setApellidos("Perez");

        when(empleadoService.registrarEmpleado(any(RegistroEmpleadoDTO.class))).thenReturn(empleadoDto);

        // Act & Assert
        mockMvc.perform(post("/empleados/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombres\":\"Juan\",\"apellidos\":\"Perez\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombres").value("Juan"))
                .andExpect(jsonPath("$.apellidos").value("Perez"));
    }

    @Test
    @WithMockUser
    void testListarEmpleados() throws Exception {
        // Arrange
        List<EmpleadoDto> empleados = new ArrayList<>();
        EmpleadoDto empleado = new EmpleadoDto();
        empleado.setId(1L);
        empleado.setNombres("Juan");
        empleado.setApellidos("Perez");
        empleados.add(empleado);

        when(empleadoService.listarEmpleados()).thenReturn(empleados);

        // Act & Assert
        mockMvc.perform(get("/empleados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombres").value("Juan"))
                .andExpect(jsonPath("$[0].apellidos").value("Perez"));
    }

    @Test
    @WithMockUser
    void testObtenerEmpleado() throws Exception {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombres("Juan");
        empleado.setEmail("juan@google.com");

        when(empleadoService.obtenerEmpleado(1L)).thenReturn(empleado);

        // Act & Assert
        mockMvc.perform(get("/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombres").value("Juan"));
    }

    @Test
    @WithMockUser
    void testEliminarEmpleado() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/empleados/1"))
                .andExpect(status().isOk());

        verify(empleadoService, times(1)).eliminarEmpleado(1L);
    }
}