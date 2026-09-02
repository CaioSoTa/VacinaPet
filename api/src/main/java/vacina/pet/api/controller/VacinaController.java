package vacina.pet.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vacina.pet.api.model.Vacina;
import vacina.pet.api.repository.PetRepository;
import vacina.pet.api.repository.VacinaRepository;

import java.util.List;

@RestController
@RequestMapping("/vacinas")
@CrossOrigin(origins = "*")
public class VacinaController {

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private PetRepository petRepository;

    @GetMapping
    public List<Vacina> listarTodas() {
        return vacinaRepository.findAll();
    }

    @GetMapping("/pet/{petId}")
    public List<Vacina> ListarVacinasPorId(@PathVariable Long petId) {
        return vacinaRepository.findByPetId(petId);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Vacina vacina) {
        // Verifica se o pet informado no JSON realmente existe no banco
        if (vacina.getPet() == null || vacina.getPet().getId() == null) {
            return ResponseEntity.badRequest().body("É necessário informar o ID do pet.");
        }

        boolean petExiste = petRepository.existsById(vacina.getPet().getId());
        if (!petExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet não encontrado com o ID informado.");
        }

        Vacina novaVacina = vacinaRepository.save(vacina);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVacina);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!vacinaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vacinaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
