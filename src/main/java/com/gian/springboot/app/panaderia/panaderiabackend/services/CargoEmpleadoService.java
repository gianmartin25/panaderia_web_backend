package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.CargoEmpleadoResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.CargoEmpleado;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.CargoEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<CargoEmpleadoResponseDTO> listarCargosEmpleado() {
        List<CargoEmpleado> cargosEmpleado = cargoEmpleadoRepository.findAll();

        List<CargoEmpleadoResponseDTO> cargoEmpleadoResponseDTOList = new ArrayList<>();
        for (CargoEmpleado cargoEmpleado : cargosEmpleado) {
            CargoEmpleadoResponseDTO cargoEmpleadoResponseDTO = new CargoEmpleadoResponseDTO();
            cargoEmpleadoResponseDTO.setId(cargoEmpleado.getId());
            cargoEmpleadoResponseDTO.setNombre(cargoEmpleado.getNombre());
            cargoEmpleadoResponseDTOList.add(cargoEmpleadoResponseDTO);
        }
        return cargoEmpleadoResponseDTOList;
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