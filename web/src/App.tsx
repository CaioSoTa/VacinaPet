import { useState, useEffect } from 'react';
import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import type { Usuario, LoginResponse } from './types';

export function App() {
  const [user, setUser] = useState<Usuario | null>(null);

  useEffect(() => {
    const savedUser = localStorage.getItem('@VacinaPet:user');
    const savedToken = localStorage.getItem('@VacinaPet:token');

    if (savedUser && savedToken) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const handleLoginSuccess = (data: LoginResponse) => {
    setUser({
      id: data.id,
      nome: data.nome,
      email: data.email,
      perfil: data.perfil,
    });
  };

  const handleLogout = () => {
    localStorage.removeItem('@VacinaPet:token');
    localStorage.removeItem('@VacinaPet:user');
    setUser(null);
  };

  return (
    <div>
      {!user ? (
        <Login onLoginSuccess={handleLoginSuccess} />
      ) : (
        <Dashboard user={user} onLogout={handleLogout} />
      )}
    </div>
  );
}

export default App;