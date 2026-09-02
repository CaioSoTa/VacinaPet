package vacina.pet.api.controller;

import vacina.pet.api.model.Pet;
import vacina.pet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @GetMapping
    public List<Pet> listarTodos() {
        return petRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet salvar(@RequestBody Pet pet) {
        return petRepository.save(pet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPorId(@PathVariable long id) {
        return petRepository.findById(id)
                .map(pet -> ResponseEntity.ok(pet))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizar(@PathVariable long id, @RequestBody Pet petAtualizado) {
        return petRepository.findById(id)
                .map(petExistente -> {
                    petExistente.setNome(petAtualizado.getNome());
                    petExistente.setEspecie(petAtualizado.getEspecie());
                    petExistente.setRaca(petAtualizado.getRaca());
                    petExistente.setTutor(petAtualizado.getTutor());
                    Pet petSalvo = petRepository.save(petExistente);
                    return ResponseEntity.ok(petSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarPorId(@PathVariable long id) {
        if(!petRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        petRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
