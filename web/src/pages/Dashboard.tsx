import { useEffect, useState, type FormEvent } from 'react';
import { api } from '../services/api';
import type { Pet, Usuario } from '../types';
import styles from './css/Dashboard.module.css';

interface DashboardProps {
  user: Usuario;
  onLogout: () => void;
}

export function Dashboard({ user, onLogout }: DashboardProps) {
  const [pets, setPets] = useState<Pet[]>([]);

  // Estados do Pet (Datas como string YYYY-MM-DD)
  const [nomePet, setNomePet] = useState('');
  const [especie, setEspecie] = useState('');
  const [raca, setRaca] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');

  // Estados da Vacina
  const [petSelecionadoId, setPetSelecionadoId] = useState<number | ''>('');
  const [nomeVacina, setNomeVacina] = useState('');
  const [fabricante, setFabricante] = useState('');
  const [dataAplicacao, setDataAplicacao] = useState('');
  const [dataProximaDose, setDataProximaDose] = useState('');

  const [mensagem, setMensagem] = useState<string | null>(null);

  const carregarPets = () => {
    api.get<Pet[]>('/pets')
      .then((res) => {
        setPets(res.data);
        if (res.data.length > 0 && !petSelecionadoId) {
          setPetSelecionadoId(res.data[0].id ?? '');
        }
      })
      .catch((err) => console.error("Erro ao buscar pets:", err));
  };

  useEffect(() => {
    carregarPets();
  }, []);

  // Cadastrar Pet
  const handleCadastrarPet = async (e: FormEvent) => {
    e.preventDefault();
    setMensagem("");

    try {
      await api.post('/pets', {
        nome: nomePet,
        especie,
        raca,
        dataNascimento, // Envia "YYYY-MM-DD" limpo para o LocalDate
        tutor: { id: user.id } // Ajustado de tutorId -> tutor
      });

      setNomePet('');
      setEspecie('');
      setRaca('');
      setDataNascimento('');
      carregarPets();
      setMensagem("Pet cadastrado com sucesso!");
    } catch (error) {
      console.error("Erro ao cadastrar pet:", error);
      setMensagem("Erro ao cadastrar pet. Por favor, tente novamente.");
    }
  };

  // Cadastrar Vacina
  const handleCadastrarVacina = async (e: FormEvent) => {
    e.preventDefault();
    if (!petSelecionadoId) {
      setMensagem("Selecione um pet antes de cadastrar uma vacina.");
      return;
    }
    setMensagem("");

    try {
      await api.post('/vacinas', {
        nome: nomeVacina,
        fabricante,
        dataAplicacao, // Envia "YYYY-MM-DD" limpo
        dataProximaDose: dataProximaDose || null,
        pet: { id: Number(petSelecionadoId) } // Ajustado de petId -> pet
      });

      setNomeVacina('');
      setFabricante('');
      setDataAplicacao('');
      setDataProximaDose('');
      setMensagem("Vacina cadastrada com sucesso!");
    } catch (error) {
      console.error("Erro ao cadastrar vacina:", error);
      setMensagem("Erro ao cadastrar vacina. Por favor, tente novamente.");
    }
  };

  return (  
    <div className={styles.dashboardContainer}>
      <header className={styles.header}>
        <h2>Painel VacinaPet</h2>
        <div className={styles.userInfo}>
          <span>Olá, bem-vindo, {user.nome}!</span>
          <button onClick={onLogout} className={styles.btnLogout}>
            Sair
          </button>
        </div>
      </header>

      {mensagem && <p className={styles.alertMessage}>{mensagem}</p>}

      <section className={styles.gridForms}>
        {/* Form Pet */}
        <div className={styles.card}>
          <h3>Cadastrar Novo Pet</h3>
          <form onSubmit={handleCadastrarPet} className={styles.form}>
            <input type="text" placeholder="Nome do Pet" value={nomePet} onChange={(e) => setNomePet(e.target.value)} required />
            <select value={especie} onChange={(e) => setEspecie(e.target.value)} required>
              <option value="">Selecione a espécie</option>
              <option value="Cachorro">Cachorro</option>
              <option value="Gato">Gato</option>
              <option value="Outro">Outro</option>
            </select>
            <input type="text" placeholder="Raça" value={raca} onChange={(e) => setRaca(e.target.value)} required />
            <label>Data de Nascimento:</label>
            <input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)} required />
            <button type="submit" className={styles.btnSubmit}>Cadastrar Pet</button>
          </form>
        </div>

        {/* Form Vacina */}
        <div className={styles.card}>
          <h3>Registrar Vacina</h3>
          <form onSubmit={handleCadastrarVacina} className={styles.form}>
            <label>Selecione o Pet:</label>
            <select
              value={petSelecionadoId}
              onChange={(e) => setPetSelecionadoId(Number(e.target.value))}
              required
            >
              <option value="">Selecione um pet</option>
              {pets.map((pet) => (
                <option key={pet.id} value={pet.id}>
                  {pet.nome} ({pet.raca})
                </option>
              ))}
            </select>
            <input type="text" placeholder="Nome da Vacina" value={nomeVacina} onChange={(e) => setNomeVacina(e.target.value)} required />
            <input type="text" placeholder="Fabricante" value={fabricante} onChange={(e) => setFabricante(e.target.value)} required />
            <label>Data da Aplicação:</label>
            <input type="date" value={dataAplicacao} onChange={(e) => setDataAplicacao(e.target.value)} required />
            <label>Data da Próxima Dose:</label>
            <input type="date" value={dataProximaDose} onChange={(e) => setDataProximaDose(e.target.value)} />
            <button type="submit" disabled={pets.length === 0} className={styles.btnSubmit}>Registrar Vacina</button>
          </form>
        </div>
      </section>

      {/* Lista de Pets */}
      <section className={styles.sectionPets}>
        <h3>Meus Pets</h3>
        {pets.length === 0 ? (
          <p>Você ainda não cadastrou nenhum pet.</p>
        ) : (
          <ul className={styles.petList}>
            {pets.map((pet) => (
              <li key={pet.id} className={styles.petItem}>
                <strong>{pet.nome}</strong> ({pet.especie}, {pet.raca}) - Nascimento: {pet.dataNascimento}
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}