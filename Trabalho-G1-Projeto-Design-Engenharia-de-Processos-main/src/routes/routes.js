import React from 'react';

import {
  Pressable,
  StyleSheet,
  Text,
} from 'react-native';

import {
  createNativeStackNavigator,
} from '@react-navigation/native-stack';

import Login from '../pages/Login';
import Home from '../pages/Home';
import DetalhesProduto from '../pages/DetalhesProduto';
import InformacoesGrupo from '../pages/InformacoesGrupo';

import {
  COLORS,
} from '../constants/theme';

const Stack =
  createNativeStackNavigator();

export default function Routes() {
  return (
    <Stack.Navigator
      initialRouteName="Login"
      screenOptions={{
        headerStyle: {
          backgroundColor:
            COLORS.backgroundElement,
        },

        headerTintColor:
          COLORS.primary,

        headerTitleAlign:
          'center',

        headerTitleStyle: {
          fontSize: 21,

          fontWeight: '700',

          color:
            COLORS.primary,
        },

        headerShadowVisible:
          false,
      }}
    >
      <Stack.Screen
        name="Login"
        component={Login}
        options={{
          headerShown: false,
        }}
      />

      <Stack.Screen
        name="Home"
        component={Home}
        options={({ navigation }) => ({
          title: 'Produtos',

          headerLeft: () => (
            <Pressable
              onPress={() =>
                navigation.replace(
                  'Login'
                )
              }
              hitSlop={8}
              style={
                styles.botaoHeader
              }
            >
              <Text
                style={
                  styles.textoHeader
                }
              >
                Sair
              </Text>
            </Pressable>
          ),

          headerRight: () => (
            <Pressable
              onPress={() =>
                navigation.navigate(
                  'InformacoesGrupo'
                )
              }
              hitSlop={8}
              style={
                styles.botaoHeader
              }
            >
              <Text
                style={
                  styles.textoHeader
                }
              >
                Info
              </Text>
            </Pressable>
          ),
        })}
      />

      <Stack.Screen
        name="DetalhesProduto"
        component={
          DetalhesProduto
        }
        options={{
          title:
            'Detalhes do produto',
        }}
      />

      <Stack.Screen
        name="InformacoesGrupo"
        component={
          InformacoesGrupo
        }
        options={{
          title:
            'Informações do grupo',
        }}
      />
    </Stack.Navigator>
  );
}

const styles = StyleSheet.create({
  botaoHeader: {
    paddingHorizontal: 8,
    paddingVertical: 6,
  },

  textoHeader: {
    color:
      COLORS.primary,

    fontSize: 14,

    fontWeight: '600',
  },
});