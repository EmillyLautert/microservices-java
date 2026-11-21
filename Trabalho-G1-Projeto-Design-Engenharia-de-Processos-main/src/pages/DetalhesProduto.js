import React, {
  useEffect,
  useState,
} from 'react';

import {
  ActivityIndicator,
  Image,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import {
  getProdutoPorId,
} from '../services/productService';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

export default function DetalhesProduto({
  route,
}) {
  const {
    produtoId,
  } = route.params;

  const [produto, setProduto] =
    useState(null);

  const [carregando, setCarregando] =
    useState(true);

  const [erro, setErro] =
    useState('');

  useEffect(() => {
    carregarProduto();
  }, [produtoId]);

  async function carregarProduto() {
    try {
      setCarregando(true);

      setErro('');

      const dados =
        await getProdutoPorId(
          produtoId
        );

      setProduto(dados);
    } catch (error) {
      setErro(
        'Não foi possível carregar os detalhes do produto.'
      );
    } finally {
      setCarregando(false);
    }
  }

  if (carregando) {
    return (
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
          Carregando detalhes...
        </Text>
      </View>
    );
  }

  if (erro) {
    return (
      <View
        style={
          styles.mensagem
        }
      >
        <Text
          style={
            styles.textoErro
          }
        >
          {erro}
        </Text>
      </View>
    );
  }

  const imagemProduto =
    produto.images?.[0] ||
    produto.thumbnail;

  const precoFormatado =
    Number(
      produto.price
    ).toLocaleString(
      'pt-BR',
      {
        style:
          'currency',
        currency:
          'BRL',
      }
    );

  return (
    <ScrollView
      style={
        styles.container
      }
      contentContainerStyle={
        styles.conteudo
      }
      showsVerticalScrollIndicator={
        false
      }
    >
      <View
        style={
          styles.imagemContainer
        }
      >
        <Image
          source={{
            uri: imagemProduto,
          }}
          style={
            styles.imagem
          }
          resizeMode="contain"
        />
      </View>

      <Text
        style={
          styles.categoria
        }
      >
        {produto.category}
      </Text>

      <Text
        style={
          styles.titulo
        }
      >
        {produto.title}
      </Text>

      <Text
        style={
          styles.preco
        }
      >
        {precoFormatado}
      </Text>

      <View
        style={
          styles.separador
        }
      />

      <Text
        style={
          styles.tituloDescricao
        }
      >
        Descrição
      </Text>

      <Text
        style={
          styles.descricao
        }
      >
        {produto.description}
      </Text>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,

    backgroundColor:
      COLORS.background,
  },

  conteudo: {
    padding:
      SPACING.four,

    paddingBottom:
      SPACING.six,
  },

  imagemContainer: {
    height: 320,

    backgroundColor:
      COLORS.lightPink,

    borderRadius:
      BORDER_RADIUS.large,

    alignItems:
      'center',

    justifyContent:
      'center',

    marginBottom:
      SPACING.four,
  },

  imagem: {
    width: '85%',
    height: '85%',
  },

  categoria: {
    color:
      COLORS.orange,

    fontSize: 14,

    fontWeight: '700',

    marginBottom:
      SPACING.one,
  },

  titulo: {
    color:
      COLORS.primary,

    fontSize: 26,

    fontWeight: '700',

    lineHeight: 32,
  },

  preco: {
    color:
      COLORS.primary,

    fontSize: 25,

    fontWeight: '700',

    marginTop:
      SPACING.two,
  },

  separador: {
    height: 3,

    width: 50,

    backgroundColor:
      COLORS.yellow,

    borderRadius: 2,

    marginVertical:
      SPACING.four,
  },

  tituloDescricao: {
    color:
      COLORS.primary,

    fontSize: 20,

    fontWeight: '700',

    marginBottom:
      SPACING.two,
  },

  descricao: {
    color:
      COLORS.textSecondary,

    fontSize: 16,

    lineHeight: 25,
  },

  carregando: {
    flex: 1,

    backgroundColor:
      COLORS.background,

    alignItems:
      'center',

    justifyContent:
      'center',

    gap:
      SPACING.two,
  },

  textoCarregando: {
    color:
      COLORS.textSecondary,

    fontSize: 15,
  },

  mensagem: {
    flex: 1,

    backgroundColor:
      COLORS.background,

    alignItems:
      'center',

    justifyContent:
      'center',

    padding:
      SPACING.four,
  },

  textoErro: {
    color:
      COLORS.error,

    fontSize: 16,

    textAlign:
      'center',
  },
});