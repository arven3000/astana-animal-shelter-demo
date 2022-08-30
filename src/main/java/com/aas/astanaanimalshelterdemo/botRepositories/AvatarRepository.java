package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByPetId(Long petId);
}
