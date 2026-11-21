import {
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

export const INTEGRANTES = [
  {
    nome: 'Camila Silveira Campos',
    ra: '1118454',
  },
  {
    nome: 'Emilly Lautert dos Santos',
    ra: '1138184',
  },
  {
    nome: 'Maria Elisa Favretto Moro',
    ra: '1138252',
  },
  {
    nome: 'Mathias Karling Stadtlober',
    ra: '1116251',
  },
];

export default function InformacoesGrupo() {
  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={
        styles.conteudo
      }
      showsVerticalScrollIndicator={false}
    >
      <View style={styles.cabecalho}>
        <View style={styles.logo}>
          <Text style={styles.logoTexto}>
            O
          </Text>
        </View>

        <View>
          <Text style={styles.titulo}>
            Our Store
          </Text>

          <Text style={styles.subtitulo}>
            Informações do Grupo
          </Text>
        </View>
      </View>

      <View style={styles.descricaoCard}>
        <View
          style={styles.indicador}
        />

        <Text style={styles.descricao}>
          Aplicativo desenvolvido para a
          disciplina de Projeto, Design e
          Engenharia de Processos utilizando
          React Native, Expo, JavaScript e 
          DummyJSON.
        </Text>
      </View>

      <Text style={styles.tituloIntegrantes}>
        Integrantes
      </Text>

      <View style={styles.lista}>
        {INTEGRANTES.map((integrante) => (
          <View
            key={integrante.ra}
            style={styles.card}
          >
            <View style={styles.avatar}>
              <Text
                style={styles.avatarTexto}
              >
                {integrante.nome.charAt(0)}
              </Text>
            </View>

            <View
              style={styles.informacoes}
            >
              <Text style={styles.nome}>
                {integrante.nome}
              </Text>

              <Text style={styles.ra}>
                RA: {integrante.ra}
              </Text>
            </View>
          </View>
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },

  conteudo: {
    padding: SPACING.four,
    gap: SPACING.three,
  },

  cabecalho: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: SPACING.three,
  },

  logo: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: COLORS.accent,
    alignItems: 'center',
    justifyContent: 'center',
  },

  logoTexto: {
    color: COLORS.primary,
    fontSize: 30,
    fontWeight: '800',
  },

  titulo: {
    fontSize: 26,
    fontWeight: '800',
    color: COLORS.primary,
  },

  subtitulo: {
    fontSize: 14,
    color: COLORS.textSecondary,
    marginTop: SPACING.one,
  },

  descricaoCard: {
    flexDirection: 'row',
    backgroundColor:
      COLORS.backgroundElement,
    borderRadius: BORDER_RADIUS.medium,
    padding: SPACING.three,
    borderWidth: 1,
    borderColor: COLORS.border,
    gap: SPACING.three,
    alignItems: 'center',
  },

  indicador: {
    width: 5,
    alignSelf: 'stretch',
    borderRadius: 3,
    backgroundColor: COLORS.secondary,
  },

  descricao: {
    flex: 1,
    fontSize: 15,
    lineHeight: 23,
    color: COLORS.textSecondary,
  },

  tituloIntegrantes: {
    fontSize: 20,
    fontWeight: '800',
    color: COLORS.text,
  },

  lista: {
    gap: SPACING.two,
  },

  card: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor:
      COLORS.backgroundElement,
    padding: SPACING.three,
    borderRadius: BORDER_RADIUS.medium,
    borderWidth: 1,
    borderColor: COLORS.border,
    gap: SPACING.three,
  },

  avatar: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor:
      COLORS.backgroundSelected,
    alignItems: 'center',
    justifyContent: 'center',
  },

  avatarTexto: {
    color: COLORS.primary,
    fontSize: 20,
    fontWeight: '800',
  },

  informacoes: {
    flex: 1,
    gap: SPACING.one,
  },

  nome: {
    fontSize: 16,
    fontWeight: '700',
    color: COLORS.text,
  },

  ra: {
    fontSize: 14,
    color: COLORS.textSecondary,
  },
});