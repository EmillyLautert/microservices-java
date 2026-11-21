import React, {
  useEffect,
  useState,
} from 'react';

import {
  ActivityIndicator,
  FlatList,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import CardProduto from '../components/CardProduto';
import FiltroCategoria from '../components/FiltroCategoria';

import {
  getProdutos,
  getProdutosPorCategoria,
} from '../services/productService';

import {
  COLORS,
  SPACING,
} from '../constants/theme';

export default function Home({
  navigation,
}) {
  const [produtos, setProdutos] =
    useState([]);

  const [
    categoriaSelecionada,
    setCategoriaSelecionada,
  ] = useState('');

  const [
    carregando,
    setCarregando,
  ] = useState(true);

  const [erro, setErro] =
    useState('');

  useEffect(() => {
    carregarProdutos();
  }, [categoriaSelecionada]);

  async function carregarProdutos() {
    try {
      setCarregando(true);
      setErro('');

      const dados =
        categoriaSelecionada
          ? await getProdutosPorCategoria(
              categoriaSelecionada
            )
          : await getProdutos();

      setProdutos(dados);
    } catch (error) {
      setErro(
        'Não foi possível carregar os produtos.'
      );
    } finally {
      setCarregando(false);
    }
  }

  function abrirDetalhes(produto) {
    navigation.navigate(
      'DetalhesProduto',
      {
        produtoId: produto.id,
      }
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.cabecalho}>
        <Text style={styles.titulo}>
          Encontre seus favoritos
        </Text>

        <Text style={styles.subtitulo}>
          Explore nossa seleção de produtos
        </Text>

        <FiltroCategoria
          categoriaSelecionada={
            categoriaSelecionada
          }
          onSelecionarCategoria={
            setCategoriaSelecionada
          }
        />
      </View>

      {carregando ? (
        <View
          style={
            styles.carregando
          }
        >
          <ActivityIndicator
            size="large"
            color={
              COLORS.primary
            }
          />

          <Text
            style={
              styles.textoCarregando
            }
          >
            Carregando produtos...
          </Text>
        </View>
      ) : erro ? (
        <View
          style={styles.mensagem}
        >
          <Text
            style={
              styles.textoErro
            }
          >
            {erro}
          </Text>
        </View>
      ) : (
        <FlatList
          data={produtos}
          keyExtractor={(item) =>
            String(item.id)
          }
          renderItem={({ item }) => (
            <CardProduto
              produto={item}
              onPress={() =>
                abrirDetalhes(item)
              }
            />
          )}
          contentContainerStyle={
            styles.lista
          }
          showsVerticalScrollIndicator={
            false
          }
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,

    backgroundColor:
      COLORS.background,
  },

  cabecalho: {
    paddingHorizontal:
      SPACING.four,

    paddingTop:
      SPACING.four,
  },

  titulo: {
    color:
      COLORS.primary,

    fontSize: 28,

    fontWeight: '700',

    marginBottom:
      SPACING.one,
  },

  subtitulo: {
    color:
      COLORS.textSecondary,

    fontSize: 16,

    fontWeight: '400',

    marginBottom:
      SPACING.two,
  },

  carregando: {
    flex: 1,

    alignItems: 'center',
    justifyContent: 'center',

    gap: SPACING.two,
  },

  textoCarregando: {
    color:
      COLORS.textSecondary,

    fontSize: 15,
  },

  mensagem: {
    flex: 1,

    alignItems: 'center',
    justifyContent: 'center',

    padding:
      SPACING.four,
  },

  textoErro: {
    color: COLORS.error,

    fontSize: 16,

    textAlign: 'center',
  },

  lista: {
    paddingHorizontal:
      SPACING.four,

    paddingTop:
      SPACING.two,

    paddingBottom:
      SPACING.four,
  },
});