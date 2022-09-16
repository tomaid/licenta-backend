package com.licenta.licenta.service;
import com.licenta.licenta.dto.RoleDto;
import com.licenta.licenta.model.Role;
import com.licenta.licenta.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDto> getAllRoles() {

        List<Role> roles = roleRepository.findAll();
        List<RoleDto> roleDtoList = new ArrayList<>();
        for (Role role: roles) {
            RoleDto roleDto = new RoleDto(role.getId(),role.getNume());
            roleDtoList.add(roleDto);
        }

        return roleDtoList;
    }
}
