package com.alwaysfit.imc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val PESO_INICIAL = 70f
private const val ALTURA_INICIAL = 170f

private val Escala = arrayOf(
    0f to Categoria.AbaixoDoPeso.cor,
    0.12f to Categoria.AbaixoDoPeso.cor,
    0.18f to Categoria.Normal.cor,
    0.37f to Categoria.Normal.cor,
    0.43f to Categoria.Sobrepeso.cor,
    0.57f to Categoria.Sobrepeso.cor,
    0.63f to Categoria.ObesidadeI.cor,
    0.77f to Categoria.ObesidadeI.cor,
    0.83f to Categoria.ObesidadeII.cor,
    1f to Categoria.ObesidadeIII.cor
)

@Composable
fun TelaImc(modifier: Modifier = Modifier) {
    var peso by rememberSaveable { mutableStateOf(PESO_INICIAL) }
    var altura by rememberSaveable { mutableStateOf(ALTURA_INICIAL) }
    var visivel by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visivel = true }

    val imc = arredondar(calcularImc(peso, altura))
    val categoria = categoriaDe(imc)

    Box(modifier = modifier) {
        FundoFresco(Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Entrada(visivel, 0) { Cabecalho() }

            Entrada(visivel, 1) {
                Vidro(modifier = Modifier.fillMaxWidth()) {
                    Ajuste(
                        rotulo = stringResource(R.string.peso),
                        valor = peso,
                        unidade = stringResource(R.string.unidade_kg),
                        minimo = 30f,
                        maximo = 200f,
                        passo = 0.5f,
                        casas = 1,
                        aoMudar = { peso = it }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Ajuste(
                        rotulo = stringResource(R.string.altura),
                        valor = altura,
                        unidade = stringResource(R.string.unidade_cm),
                        minimo = 120f,
                        maximo = 220f,
                        passo = 1f,
                        casas = 0,
                        aoMudar = { altura = it }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            peso = PESO_INICIAL
                            altura = ALTURA_INICIAL
                        }) {
                            Text(
                                text = stringResource(R.string.redefinir),
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Entrada(visivel, 2) { ResultadoImc(imc, categoria, altura) }

            Text(
                text = stringResource(R.string.aviso),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun Entrada(visivel: Boolean, ordem: Int, conteudo: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visivel,
        modifier = Modifier.fillMaxWidth(),
        enter = fadeIn(tween(600, delayMillis = ordem * 130)) +
            slideInVertically(
                animationSpec = tween(600, delayMillis = ordem * 130, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 5 }
            )
    ) {
        conteudo()
    }
}

@Composable
private fun Cabecalho() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.titulo),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = stringResource(R.string.selo),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.22f))
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            )
        }
        Text(
            text = stringResource(R.string.subtitulo),
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun Ajuste(
    rotulo: String,
    valor: Float,
    unidade: String,
    minimo: Float,
    maximo: Float,
    passo: Float,
    casas: Int,
    aoMudar: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = rotulo,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotaoVidro("−", stringResource(R.string.desc_diminuir, rotulo)) {
                aoMudar((valor - passo).coerceIn(minimo, maximo))
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = formatar(valor, casas),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " $unidade",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
            BotaoVidro("+", stringResource(R.string.desc_aumentar, rotulo)) {
                aoMudar((valor + passo).coerceIn(minimo, maximo))
            }
        }
        Slider(
            value = valor,
            onValueChange = { aoMudar(ajustar(it, passo, minimo, maximo)) },
            valueRange = minimo..maximo,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.28f)
            )
        )
    }
}

@Composable
private fun BotaoVidro(simbolo: String, descricao: String, aoClicar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            .semantics { contentDescription = descricao }
            .clickable(role = Role.Button, onClick = aoClicar),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = simbolo,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ResultadoImc(imc: Float, categoria: Categoria, alturaCm: Float) {
    val imcAnimado by animateFloatAsState(
        targetValue = imc,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "imc"
    )
    val posicao by animateFloatAsState(
        targetValue = ((imc - 15f) / 25f).coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "posicao"
    )
    val corCategoria by animateColorAsState(
        targetValue = categoria.cor,
        animationSpec = tween(500),
        label = "corCategoria"
    )
    val (minimo, maximo) = pesoSaudavel(alturaCm)
    val descricao = stringResource(
        R.string.desc_imc,
        formatar(imc, 1),
        stringResource(categoria.rotulo)
    )

    Vidro(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { contentDescription = descricao }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.seu_imc),
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.6.sp
            )
            Crossfade(
                targetState = categoria,
                animationSpec = tween(400),
                label = "rotulo",
                modifier = Modifier
                    .animateContentSize()
                    .clip(RoundedCornerShape(50))
                    .background(corCategoria.copy(alpha = 0.3f))
                    .border(1.dp, corCategoria.copy(alpha = 0.9f), RoundedCornerShape(50))
            ) { atual ->
                Text(
                    text = stringResource(atual.rotulo),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }

        Text(
            text = formatar(imcAnimado, 1),
            color = Color.White,
            fontSize = 68.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Medidor(posicao = posicao, corMarcador = corCategoria)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp)
        ) {
            Marca(3.5f, "18,5")
            Marca(6.5f, "25")
            Marca(5f, "30")
            Marca(5f, "35")
            Spacer(modifier = Modifier.weight(5f))
        }

        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.22f))
        )
        Spacer(modifier = Modifier.height(14.dp))

        Crossfade(
            targetState = categoria,
            animationSpec = tween(400),
            label = "mensagem",
            modifier = Modifier.animateContentSize()
        ) { atual ->
            Text(
                text = stringResource(atual.mensagem),
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 15.sp,
                lineHeight = 21.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(
                R.string.peso_saudavel,
                formatar(minimo, 1),
                formatar(maximo, 1)
            ),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun Medidor(posicao: Float, corMarcador: Color) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        val raio = 11.dp.toPx()
        val espessura = 12.dp.toPx()
        val larguraFaixa = size.width - 2f * raio
        val topo = (size.height - espessura) / 2f

        drawRoundRect(
            brush = Brush.horizontalGradient(
                *Escala,
                startX = raio,
                endX = raio + larguraFaixa
            ),
            topLeft = Offset(raio, topo),
            size = Size(larguraFaixa, espessura),
            cornerRadius = CornerRadius(espessura / 2f, espessura / 2f)
        )

        val centro = Offset(raio + larguraFaixa * posicao, size.height / 2f)
        drawCircle(
            color = Color.Black.copy(alpha = 0.2f),
            radius = raio + 2.dp.toPx(),
            center = centro + Offset(0f, 1.dp.toPx())
        )
        drawCircle(color = Color.White, radius = raio, center = centro)
        drawCircle(color = corMarcador, radius = raio * 0.5f, center = centro)
    }
}

@Composable
private fun RowScope.Marca(peso: Float, texto: String) {
    Box(
        modifier = Modifier.weight(peso),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = texto,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 11.sp,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier
                .wrapContentWidth(Alignment.End, unbounded = true)
                .offset(x = 10.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 568)
@Composable
private fun PreviewTelaImc() {
    AlwaysFitTheme {
        TelaImc(modifier = Modifier.fillMaxSize())
    }
}
