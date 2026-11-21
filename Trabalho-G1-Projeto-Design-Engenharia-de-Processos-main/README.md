# Trabalho G1 — Projeto, Design e Engenharia de Processos

## Descrição

Aplicativo mobile desenvolvido em **React Native com Expo e JavaScript**, utilizando a **DummyJSON** para autenticação e consulta de produtos.

Observação: O funcionamento das funcionalidades de autenticação e consulta de produtos depende da disponibilidade da DummyJSON. Caso a API esteja temporariamente indisponível, as funcionalidades que dependem dela poderão apresentar mensagens de erro ou não carregar os dados.

## Integrantes

- Camila Silveira Campos — RA: 1118454
- Emilly Lautert dos Santos — RA: 1138184
- Maria Elisa Favretto Moro — RA: 1138252
- Mathias Karling Stadtlober — RA: 1116251

## Como rodar o projeto com Expo

### 1. Pré-requisitos

É necessário ter instalado:

- [Node.js](https://nodejs.org/)
- npm
- Expo
- Expo Go, caso o aplicativo seja executado em um celular.

### 2. Instalar as dependências

Na pasta do projeto, execute: `npm install`

### 3. Iniciar o projeto

Execute: `npx expo start`

### 3.1 Executar no computador

Para executar o aplicativo no navegador, pressione `w` no terminal onde o Expo está sendo executado. Também é possível abrir o endereço indicado pelo Expo no navegador.
(Algumas funcionalidades podem apresentar diferenças quando executadas na versão Web. Para testar o aplicativo mobile de forma mais próxima do ambiente real, recomenda-se utilizar o Expo Go em um celular.)

### 3.2 Executar no celular

- Instale o aplicativo Expo Go no celular.
- Conecte o celular e o computador à mesma rede Wi-Fi.
- Escaneie o QR Code exibido pelo Expo utilizando o Expo Go.
- Aguarde o carregamento do aplicativo.

### 4. Encerrar o Expo

Para parar o servidor de desenvolvimento, pressione `Ctrl + C` no terminal.

## Como verificar os usuários disponíveis para login

- Para consultar os usuários disponíveis, acesse: https://dummyjson.com/users?limit=0. A resposta apresentará uma lista de usuários contendo as informações necessárias.
- Para realizar o login no aplicativo, utilize o `username` e o `password` de um mesmo usuário existente na lista.
- O aplicativo primeiro consulta o endpoint: `GET https://dummyjson.com/users?limit=0`
- Após verificar se o usuário e a senha informados correspondem a um usuário existente, o aplicativo realiza a autenticação através do endpoint: `POST https://dummyjson.com/auth/login`
- Caso os dados sejam válidos, o usuário é direcionado para a tela de produtos.
- Caso os dados sejam inválidos, uma mensagem de erro é exibida.

## Tecnologias utilizadas

- React Native
- Expo
- JavaScript
- Axios
- React Navigation
- DummyJSON
- useState
- useEffect
- FlatList
- ActivityIndicator