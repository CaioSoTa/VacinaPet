package vacina.pet.api.dto;

public record LoginResponse(Long id, String nome, String email, String perfil, String token) {}