export interface Usuario {
  id: number;
  nome: string;
  email: string;
  perfil: string;
}

export interface LoginResponse {
  id: number;
  nome: string;
  email: string;
  perfil: string;
  token: string;
}

export interface Pet {
  id?: number;
  nome: string;
  especie: string;
  raca: string;
  dataNascimento: string;
  tutor?: Usuario;
}