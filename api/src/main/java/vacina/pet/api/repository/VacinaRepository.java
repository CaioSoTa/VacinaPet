package vacina.pet.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vacina.pet.api.model.Vacina;

import java.util.List;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {
    // Busca todas as vacinas de um Pet específico pelo ID dele
    List<Vacina> findByPetId(Long petId);
}