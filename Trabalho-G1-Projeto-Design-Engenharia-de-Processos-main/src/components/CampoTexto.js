import {
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

export default function CampoTexto({
  label,
  valor,
  setValor,
  placeholder,
  secureTextEntry = false,
}) {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>
        {label}
      </Text>

      <TextInput
        value={valor}
        onChangeText={setValor}
        placeholder={placeholder}
        placeholderTextColor={
          COLORS.textSecondary
        }
        secureTextEntry={secureTextEntry}
        style={styles.input}
        autoCapitalize="none"
        autoCorrect={false}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    gap: SPACING.one,
  },

  label: {
    color: COLORS.text,
    fontSize: 14,
    fontWeight: '700',
  },

  input: {
    borderWidth: 1.5,
    borderColor: COLORS.border,
    backgroundColor: COLORS.background,
    borderRadius: BORDER_RADIUS.medium,
    paddingHorizontal: SPACING.three,
    paddingVertical: SPACING.three,
    color: COLORS.text,
    fontSize: 16,
  },
});