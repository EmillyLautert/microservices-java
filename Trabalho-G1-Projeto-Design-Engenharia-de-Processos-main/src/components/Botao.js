import {
  Pressable,
  StyleSheet,
  Text,
} from 'react-native';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

export default function Botao({
  titulo,
  onPress,
  disabled = false,
  variante = 'primary',
}) {
  return (
    <Pressable
      onPress={onPress}
      disabled={disabled}
      style={({ pressed }) => [
        styles.botao,
        variante === 'secondary' &&
          styles.botaoSecondary,
        disabled && styles.botaoDisabled,
        pressed && styles.botaoPressed,
      ]}
    >
      <Text
        style={[
          styles.texto,
          variante === 'secondary' &&
            styles.textoSecondary,
        ]}
      >
        {titulo}
      </Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  botao: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.three,
    borderRadius: BORDER_RADIUS.medium,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 52,
    shadowColor: COLORS.primary,
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 3,
  },

  botaoSecondary: {
    backgroundColor: COLORS.backgroundSelected,
    shadowOpacity: 0,
    elevation: 0,
  },

  botaoDisabled: {
    opacity: 0.5,
  },

  botaoPressed: {
    opacity: 0.85,
    transform: [
      {
        scale: 0.98,
      },
    ],
  },

  texto: {
    color: COLORS.white,
    fontSize: 16,
    fontWeight: '700',
  },

  textoSecondary: {
    color: COLORS.primary,
  },
});