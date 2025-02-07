package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.EmpleadoDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroEmpleadoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.errors.ErrorResponse;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Empleado;
import com.gian.springboot.app.panaderia.panaderiabackend.services.EmpleadoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEmpleado(@RequestBody RegistroEmpleadoDTO registroEmpleadoDTO) {
        try {
            EmpleadoDto empleado = empleadoService.registrarEmpleado(registroEmpleadoDTO);
            return ResponseEntity.ok(empleado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
       }
//        catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
//        }
    }

    @GetMapping("/{id}")
    public Empleado obtenerEmpleado(@PathVariable Long id) {
        return empleadoService.obtenerEmpleado(id);
    }

    @GetMapping
    public List<EmpleadoDto> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    @PutMapping("/{id}")
    public Empleado actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.actualizarEmpleado(id, empleado);
    }

    @DeleteMapping("/{id}")
    public void eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
    }
}