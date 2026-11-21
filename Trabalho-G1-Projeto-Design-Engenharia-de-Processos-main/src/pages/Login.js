import { useState } from 'react';

import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import CampoTexto from '../components/CampoTexto';
import Botao from '../components/Botao';

import {
  COLORS,
  SPACING,
  BORDER_RADIUS,
} from '../constants/theme';

import { login } from '../services/authService';

export default function Login({ navigation }) {
  const [username, setUsername] =
    useState('');

  const [password, setPassword] =
    useState('');

  const [carregando, setCarregando] =
    useState(false);

  const [erro, setErro] =
    useState('');

  async function handleLogin() {
    if (
      !username.trim() ||
      !password.trim()
    ) {
      setErro(
        'Informe o username e a senha.'
      );

      return;
    }

    try {
      setCarregando(true);
      setErro('');

      await login(
        username.trim(),
        password
      );

      navigation.replace('Home');
    } catch (error) {
      if (
        error?.message ===
        'Usuário ou senha inválidos.'
      ) {
        setErro(
          'Usuário ou senha inválidos. Verifique os dados e tente novamente.'
        );
      } else if (
        error?.request &&
        !error?.response
      ) {
        setErro(
          'Não foi possível conectar ao servidor. Verifique sua conexão com a internet e tente novamente.'
        );
      } else if (error?.response) {
        setErro(
          'Não foi possível realizar o login no momento. Tente novamente mais tarde.'
        );
      } else {
        setErro(
          'Não foi possível realizar o login. Tente novamente.'
        );
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={
        Platform.OS === 'ios'
          ? 'padding'
          : undefined
      }
    >
      <View style={styles.conteudo}>
        <View style={styles.marca}>
          <View style={styles.logo}>
            <Text style={styles.logoTexto}>
              O
            </Text>
          </View>

          <Text style={styles.nomeLoja}>
            Our Store
          </Text>

          <Text style={styles.slogan}>
            Seu estilo, suas escolhas.
          </Text>
        </View>

        <View style={styles.formulario}>
          <View>
            <Text style={styles.titulo}>
              Bem-vindo!
            </Text>

            <Text style={styles.subtitulo}>
              Entre para acessar nossa loja
            </Text>
          </View>

          <CampoTexto
            label="Username"
            valor={username}
            setValor={(valor) => {
              setUsername(valor);
              setErro('');
            }}
            placeholder="Digite seu username"
          />

          <CampoTexto
            label="Password"
            valor={password}
            setValor={(valor) => {
              setPassword(valor);
              setErro('');
            }}
            placeholder="Digite sua senha"
            secureTextEntry
          />

          {erro ? (
            <View style={styles.containerErro}>
              <Text style={styles.textoErro}>
                {erro}
              </Text>
            </View>
          ) : null}

          {carregando ? (
            <View style={styles.carregando}>
              <ActivityIndicator
                size="large"
                color={COLORS.primary}
              />

              <Text
                style={
                  styles.textoCarregando
                }
              >
                Entrando...
              </Text>
            </View>
          ) : (
            <Botao
              titulo="Entrar"
              onPress={handleLogin}
            />
          )}
        </View>

        <View style={styles.rodapeContainer}>
          <View
            style={styles.linhaDecorativa}
          />

          <Text style={styles.rodape}>
            Our Store
          </Text>
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },

  conteudo: {
    flex: 1,
    width: '100%',
    maxWidth: 520,
    alignSelf: 'center',
    justifyContent: 'center',
    paddingHorizontal: SPACING.four,
    paddingVertical: SPACING.five,
  },

  marca: {
    alignItems: 'center',
    marginBottom: SPACING.five,
  },

  logo: {
    width: 72,
    height: 72,
    borderRadius: 36,
    backgroundColor: COLORS.accent,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: SPACING.two,
    shadowColor: COLORS.accent,
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
  },

  logoTexto: {
    color: COLORS.primary,
    fontSize: 38,
    fontWeight: '800',
  },

  nomeLoja: {
    color: COLORS.primary,
    fontSize: 34,
    fontWeight: '800',
    letterSpacing: 0.5,
  },

  slogan: {
    color: COLORS.textSecondary,
    fontSize: 14,
    marginTop: SPACING.one,
  },

  formulario: {
    backgroundColor:
      COLORS.backgroundElement,
    borderRadius: BORDER_RADIUS.large,
    padding: SPACING.four,
    borderWidth: 1,
    borderColor: COLORS.border,
    gap: SPACING.three,
    shadowColor: COLORS.primary,
    shadowOffset: {
      width: 0,
      height: 6,
    },
    shadowOpacity: 0.08,
    shadowRadius: 16,
    elevation: 3,
  },

  titulo: {
    color: COLORS.text,
    fontSize: 28,
    fontWeight: '800',
  },

  subtitulo: {
    color: COLORS.textSecondary,
    fontSize: 15,
    marginTop: SPACING.one,
  },

  containerErro: {
    backgroundColor:
      COLORS.errorBackground,
    borderWidth: 1,
    borderColor: COLORS.error,
    borderRadius: BORDER_RADIUS.medium,
    paddingHorizontal: SPACING.two,
    paddingVertical: SPACING.two,
  },

  textoErro: {
    color: COLORS.error,
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  },

  carregando: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.two,
    gap: SPACING.one,
  },

  textoCarregando: {
    color: COLORS.textSecondary,
    fontSize: 14,
  },

  rodapeContainer: {
    alignItems: 'center',
    marginTop: SPACING.five,
    gap: SPACING.two,
  },

  linhaDecorativa: {
    width: 36,
    height: 4,
    borderRadius: 2,
    backgroundColor: COLORS.accent,
  },

  rodape: {
    color: COLORS.textSecondary,
    fontSize: 12,
    fontWeight: '600',
  },
});