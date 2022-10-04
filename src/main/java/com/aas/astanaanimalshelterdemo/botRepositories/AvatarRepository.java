package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    /**
     * Поиск фото по питомцу
     * @param pet - питомец
     * @return - List<Avatar>
     */
    List<Avatar> findAvatarByPet(Pet pet);
}
