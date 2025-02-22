package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.CargoEmpleadoResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.CargoEmpleado;
import com.gian.springboot.app.panaderia.panaderiabackend.services.CargoEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cargo-empleado")
public class CargoEmpleadoController {

    @Autowired
    private CargoEmpleadoService cargoEmpleadoService;

    @PostMapping("/registrar")
    public CargoEmpleado registrarCargoEmpleado(@RequestBody CargoEmpleado cargoEmpleado) {
        return cargoEmpleadoService.registrarCargoEmpleado(cargoEmpleado);
    }

    @GetMapping("/{id}")
    public CargoEmpleado obtenerCargoEmpleado(@PathVariable Long id) {
        return cargoEmpleadoService.obtenerCargoEmpleado(id);
    }

    @GetMapping
    public List<CargoEmpleadoResponseDTO> listarCargosEmpleado() {
        return cargoEmpleadoService.listarCargosEmpleado();
    }

    @PutMapping("/{id}")
    public CargoEmpleado actualizarCargoEmpleado(@PathVariable Long id, @RequestBody CargoEmpleado cargoEmpleado) {
        return cargoEmpleadoService.actualizarCargoEmpleado(id, cargoEmpleado);
    }

    @DeleteMapping("/{id}")
    public void eliminarCargoEmpleado(@PathVariable Long id) {
        cargoEmpleadoService.eliminarCargoEmpleado(id);
    }
}