package vacina.pet.api.repository;

import vacina.pet.api.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // Para buscar pets cadastrados para um tutor/usuário específico
    List<Pet> findByTutorId(Long tutorId);
    List<Pet> findByRaca(String raca);
}