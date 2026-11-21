import React from 'react';

import {
  Image,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

export default function CardProduto({
  produto,
  onPress,
}) {
  const imagemProduto =
    produto.images?.[0] ||
    produto.thumbnail;

  const precoFormatado =
    Number(
      produto.price
    ).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });

  return (
    <Pressable
      onPress={onPress}
      style={({ pressed }) => [
        styles.card,
        pressed &&
          styles.cardPressionado,
      ]}
    >
      <View style={styles.imagemContainer}>
        <Image
          source={{
            uri: imagemProduto,
          }}
          style={styles.imagem}
          resizeMode="contain"
        />
      </View>

      <View style={styles.informacoes}>
        <Text
          style={styles.nome}
          numberOfLines={2}
        >
          {produto.title}
        </Text>

        <Text style={styles.preco}>
          {precoFormatado}
        </Text>
      </View>

      <View style={styles.botaoDetalhes}>
        <Text style={styles.seta}>
          →
        </Text>
      </View>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    alignItems: 'center',

    backgroundColor:
      COLORS.productCard,

    borderWidth: 1,
    borderColor: COLORS.border,

    borderRadius:
      BORDER_RADIUS.large,

    padding: SPACING.three,

    marginBottom:
      SPACING.three,

    minHeight: 150,
  },

  cardPressionado: {
    opacity: 0.82,
  },

  imagemContainer: {
    width: 108,
    height: 108,

    backgroundColor:
      COLORS.backgroundElement,

    borderRadius:
      BORDER_RADIUS.medium,

    alignItems: 'center',
    justifyContent: 'center',

    overflow: 'hidden',
  },

  imagem: {
    width: '84%',
    height: '84%',
  },

  informacoes: {
    flex: 1,

    marginLeft:
      SPACING.four,

    paddingRight:
      SPACING.two,
  },

  nome: {
    color: COLORS.primary,

    fontSize: 17,
    fontWeight: '600',

    lineHeight: 22,
  },

  preco: {
    color: COLORS.primary,

    fontSize: 20,
    fontWeight: '700',

    marginTop:
      SPACING.two,
  },

  botaoDetalhes: {
    width: 42,
    height: 42,

    borderRadius:
      BORDER_RADIUS.round,

    backgroundColor:
      COLORS.softPurple,

    alignItems: 'center',
    justifyContent: 'center',
  },

  seta: {
    color: COLORS.primary,

    fontSize: 24,
    fontWeight: '400',

    lineHeight: 28,
  },
});