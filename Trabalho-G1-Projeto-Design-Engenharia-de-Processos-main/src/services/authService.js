import api from './api';

export async function getUsers() {
  const response = await api.get('/users?limit=0');

  return response.data.users;
}

export async function login(username, password) {
  const usuarios = await getUsers();

  const usuarioEncontrado = usuarios.find(
    (usuario) =>
      usuario.username === username &&
      usuario.password === password
  );

  if (!usuarioEncontrado) {
    throw new Error(
      'Usuário ou senha inválidos.'
    );
  }

  const response = await api.post(
    '/auth/login',
    {
      username,
      password,
      expiresInMins: 60,
    }
  );

  return response.data;
}