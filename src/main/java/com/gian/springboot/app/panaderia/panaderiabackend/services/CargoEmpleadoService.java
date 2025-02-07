package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.CargoEmpleado;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.CargoEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoEmpleadoService {

    @Autowired
    private CargoEmpleadoRepository cargoEmpleadoRepository;

    public CargoEmpleado registrarCargoEmpleado(CargoEmpleado cargoEmpleado) {
        return cargoEmpleadoRepository.save(cargoEmpleado);
    }

    public CargoEmpleado obtenerCargoEmpleado(Long id) {
        Optional<CargoEmpleado> cargoEmpleado = cargoEmpleadoRepository.findById(id);
        return cargoEmpleado.orElse(null);
    }

    public List<CargoEmpleado> listarCargosEmpleado() {
        return cargoEmpleadoRepository.findAll();
    }

    public CargoEmpleado actualizarCargoEmpleado(Long id, CargoEmpleado cargoEmpleado) {
        if (cargoEmpleadoRepository.existsById(id)) {
            cargoEmpleado.setId(id);
            return cargoEmpleadoRepository.save(cargoEmpleado);
        }
        return null;
    }

    public void eliminarCargoEmpleado(Long id) {
        cargoEmpleadoRepository.deleteById(id);
    }
}