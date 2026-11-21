import api from './api';

export async function getProdutos() {
  const response = await api.get(
    '/products?limit=0'
  );

  return response.data.products;
}

export async function getProdutosPorCategoria(
  categoria
) {
  const response = await api.get(
    `/products/category/${encodeURIComponent(
      categoria
    )}`
  );

  return response.data.products;
}

export async function getProdutoPorId(id) {
  const response = await api.get(
    `/products/${id}`
  );

  return response.data;
}

export async function getCategorias() {
  const response = await api.get(
    '/products/category-list'
  );

  return response.data;
}