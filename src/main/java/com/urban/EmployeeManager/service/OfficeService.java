package com.urban.EmployeeManager.service;

import com.urban.EmployeeManager.dto.OfficeCreateDTO;
import com.urban.EmployeeManager.dto.OfficeDTO;
import com.urban.EmployeeManager.dto.OfficeUpdateDTO;
import com.urban.EmployeeManager.exception.DuplicateResourceException;
import com.urban.EmployeeManager.exception.ResourceNotFoundException;
import com.urban.EmployeeManager.model.Office;
import com.urban.EmployeeManager.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfficeService {

    private final OfficeRepository officeRepository;

    public List<OfficeDTO> getAllOffices() {
        List<Office> offices = officeRepository.findAll();
        return offices.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public OfficeDTO getOfficeById(Long id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy văn phòng với ID: " + id));
        return convertToDTO(office);
    }

    public OfficeDTO createOffice(OfficeCreateDTO createDTO) {
        if (officeRepository.existsByName(createDTO.getName())) {
            throw new DuplicateResourceException("Tên văn phòng đã tồn tại: " + createDTO.getName());
        }

        Office office = new Office();
        office.setName(createDTO.getName());
        office.setAddress(createDTO.getAddress());


        Office savedOffice = officeRepository.save(office);
        return convertToDTO(savedOffice);
    }

    public OfficeDTO updateOffice(Long id, OfficeUpdateDTO updateDTO) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy văn phòng với ID: " + id));

        // Kiểm tra tên trùng (nếu thay đổi)
        if (!office.getName().equals(updateDTO.getName()) &&
                officeRepository.existsByName(updateDTO.getName())) {
            throw new DuplicateResourceException("Tên văn phòng đã tồn tại: " + updateDTO.getName());
        }

        office.setName(updateDTO.getName());
        office.setAddress(updateDTO.getAddress());


        Office savedOffice = officeRepository.save(office);
        return convertToDTO(savedOffice);
    }

    public void deleteOffice(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy văn phòng với ID: " + id);
        }
        officeRepository.deleteById(id);
    }

    private OfficeDTO convertToDTO(Office office) {
        OfficeDTO dto = new OfficeDTO();
        dto.setId(office.getId());
        dto.setName(office.getName());
        dto.setAddress(office.getAddress());
        return dto;
    }
}

