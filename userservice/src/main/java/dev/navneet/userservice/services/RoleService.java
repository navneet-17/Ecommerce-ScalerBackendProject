package dev.navneet.userservice.services;

import dev.navneet.userservice.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import dev.navneet.userservice.models.Role;
@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setRole(name);

        return roleRepository.save(role);
    }
}
