import { useState, type FormEvent } from 'react';
import { api } from '../services/api';
import type { LoginResponse } from '../types';

interface LoginProps {
  onLoginSuccess: (data: LoginResponse) => void;
}

export function Login({ onLoginSuccess }: LoginProps) {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setErro('');

    try {
      const response = await api.post<LoginResponse>('/auth/login', { email, senha });
      
      // Salva o Token e o Usuário no navegador
      localStorage.setItem('@VacinaPet:token', response.data.token);
      localStorage.setItem('@VacinaPet:user', JSON.stringify(response.data));

      onLoginSuccess(response.data);
    } catch (err) {
      setErro('E-mail ou senha inválidos.');
    }
  };

  return (
    <div style={{ maxWidth: '350px', margin: '50px auto', fontFamily: 'sans-serif' }}>
      <h2>Login - VacinaPet</h2>
      {erro && <p style={{ color: 'red' }}>{erro}</p>}
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input 
          type="email" 
          placeholder="E-mail" 
          value={email} 
          onChange={(e) => setEmail(e.target.value)} 
          required 
        />
        <input 
          type="password" 
          placeholder="Senha" 
          value={senha} 
          onChange={(e) => setSenha(e.target.value)} 
          required 
        />
        <button type="submit" style={{ padding: '8px', cursor: 'pointer' }}>Entrar</button>
      </form>
    </div>
  );
}