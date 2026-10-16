# AlwaysFit IMC

App Android para calcular o IMC (Índice de Massa Corporal) com uma interface suave, em tons frescos e caixas de vidro translúcido.

Feito em Kotlin com Jetpack Compose.

## Funcionalidades

- Ajuste de peso e altura com controle deslizante e botões de ajuste fino.
- Resultado calculado na hora, sem botão de calcular.
- Número do IMC, cores e medidor animados.
- Classificação segundo a faixa da OMS, com mensagem curta para cada caso.
- Faixa de peso saudável para a altura informada.
- Fundo em degradê com brilhos que se movem devagar.
- Interface que se adapta a telas pequenas, com rolagem quando necessário.
- Textos em português (pt-BR) e descrições para leitores de tela.

## Classificação

| IMC | Categoria |
| --- | --- |
| menor que 18,5 | Abaixo do peso |
| 18,5 a 24,9 | Peso normal |
| 25,0 a 29,9 | Sobrepeso |
| 30,0 a 34,9 | Obesidade grau I |
| 35,0 a 39,9 | Obesidade grau II |
| 40,0 ou mais | Obesidade grau III |

O IMC é uma referência geral e não substitui a avaliação de um profissional de saúde.

## Como rodar

1. Abra a pasta do projeto no Android Studio (File > Open).
2. Aguarde o Gradle sincronizar.
3. Escolha um emulador ou celular e clique em Run.

## Como gerar o APK

1. Vá em Build > Build Bundle(s) / APK(s) > Build APK(s).
2. Quando terminar, clique em locate na notificação.
3. O arquivo fica em `app/build/outputs/apk/debug/app-debug.apk`.

## Estrutura

```text
app/src/main/
├── java/com/alwaysfit/imc/
│   ├── MainActivity.kt   entrada do app
│   ├── TelaImc.kt        tela, controles e resultado
│   ├── Vidro.kt          caixa de vidro e fundo animado
│   ├── Imc.kt            cálculo e categorias
│   └── Tema.kt           tema do app
└── res/                  textos, tema e ícone
```

## Tecnologias

| Item | Versão |
| --- | --- |
| Kotlin | 2.1.0 |
| Android Gradle Plugin | 8.8.0 |
| Compose BOM | 2024.12.01 |
| minSdk / targetSdk | 26 / 35 |
