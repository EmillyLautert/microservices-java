import axios from 'axios';

const API_BASE_URL = 'https://dummyjson.com';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

export default api;