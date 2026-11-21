import {
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

const CATEGORIAS = [
  {
    valor: 'beauty',
    nome: 'Beleza',
  },
  {
    valor: 'fragrances',
    nome: 'Fragrâncias',
  },
  {
    valor: 'smartphones',
    nome: 'Celulares',
  },
  {
    valor: 'laptops',
    nome: 'Notebooks',
  },
  {
    valor: 'womens-dresses',
    nome: 'Vestidos Femininos',
  },
  {
    valor: 'mens-shirts',
    nome: 'Camisas Masculinas',
  },
];

export default function FiltroCategoria({
  categoriaSelecionada,
  onSelecionarCategoria,
}) {
  function handleCategoriaPress(categoria) {
    if (categoriaSelecionada === categoria) {
      onSelecionarCategoria('');
      return;
    }

    onSelecionarCategoria(categoria);
  }

  function limparFiltro() {
    onSelecionarCategoria('');
  }

  return (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      contentContainerStyle={styles.container}
    >
      {/* Botão Todos */}
      <Pressable
        onPress={limparFiltro}
        style={[
          styles.item,
          !categoriaSelecionada &&
          styles.itemSelecionado,
        ]}
      >
        <Text
          style={[
            styles.texto,
            !categoriaSelecionada &&
            styles.textoSelecionado,
          ]}
          numberOfLines={1}
        >
          Todos
        </Text>
      </Pressable>

      {/* Categorias */}
      {CATEGORIAS.map((categoria) => {
        const selecionada =
          categoriaSelecionada ===
          categoria.valor;

        return (
          <Pressable
            key={categoria.valor}
            onPress={() =>
              handleCategoriaPress(
                categoria.valor
              )
            }
            style={[
              styles.item,
              selecionada &&
              styles.itemSelecionado,
            ]}
          >
            <Text
              style={[
                styles.texto,
                selecionada &&
                styles.textoSelecionado,
              ]}
              numberOfLines={1}
            >
              {categoria.nome}
            </Text>

            {/* X aparece somente na categoria ativa */}
            {selecionada && (
              <Pressable
                onPress={(event) => {
                  event.stopPropagation();
                  limparFiltro();
                }}
                style={styles.botaoLimpar}
                hitSlop={8}
              >
                <Text style={styles.textoX}>
                  ×
                </Text>
              </Pressable>
            )}
          </Pressable>
        );
      })}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: SPACING.two,
    paddingVertical: SPACING.three,
  },

  item: {
    height: 40,
    minWidth: 72,

    paddingHorizontal: SPACING.three,

    borderRadius: BORDER_RADIUS.round,

    backgroundColor:
      COLORS.backgroundElement,

    borderWidth: 1,
    borderColor: COLORS.border,

    alignItems: 'center',
    justifyContent: 'center',

    flexDirection: 'row',

    gap: SPACING.one,

    flexShrink: 0,
  },

  itemSelecionado: {
    height: 40,
    minWidth: 72,

    backgroundColor:
      COLORS.lemon,

    borderColor:
      COLORS.lemon,
  },

  texto: {
    color: COLORS.primary,

    fontSize: 13,
    fontWeight: '500',
    lineHeight: 18,

    textAlign: 'center',
  },

  textoSelecionado: {
    color: COLORS.primary,
    fontWeight: '700',
  },

  botaoLimpar: {
    width: 18,
    height: 18,

    alignItems: 'center',
    justifyContent: 'center',

    marginLeft: 2,
  },

  textoX: {
    color: COLORS.primary,

    fontSize: 18,
    fontWeight: '700',

    lineHeight: 18,

    textAlign: 'center',
  },
});