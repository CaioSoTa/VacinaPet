package vacina.pet.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate dataAplicacao;
    private LocalDate dataProximaDose;
    private String fabricante;

    // Tag de relacionamento, faz com que muitas vacinas possam ser  add para Um Pet
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public Vacina() {

    }

    public Vacina(Long id, String nome, LocalDate dataAplicacao, LocalDate dataProximaDose, String fabricante, Pet pet) {
        this.id = id;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.dataProximaDose = dataProximaDose;
        this.fabricante = fabricante;
        this.pet = pet;
    }

    //Getters e Setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }
    public LocalDate getDataProximaDose() {
        return dataProximaDose;
    }

    public void setDataProximaDose(LocalDate dataProximaDose) {
        this.dataProximaDose = dataProximaDose;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

}
