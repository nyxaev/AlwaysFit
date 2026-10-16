package com.alwaysfit.imc

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Vidro(
    modifier: Modifier = Modifier,
    raio: Dp = 28.dp,
    conteudo: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(raio)
    Column(
        modifier = modifier
            .clip(forma)
            .background(
                Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.1f))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.4f)
                    )
                ),
                shape = forma
            )
            .padding(20.dp),
        content = conteudo
    )
}

@Composable
fun FundoFresco(modifier: Modifier = Modifier) {
    val transicao = rememberInfiniteTransition(label = "fundo")
    val fase by transicao.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(26000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fase"
    )

    Canvas(modifier = modifier) {
        drawRect(
            Brush.linearGradient(
                colors = listOf(Color(0xFF0B8A93), Color(0xFF1F6FD0), Color(0xFF5E49C7)),
                start = Offset.Zero,
                end = Offset(size.width, size.height)
            )
        )
        val angulo = fase * 2f * PI.toFloat()
        val lado = size.minDimension
        brilho(
            Offset(size.width * (0.15f + 0.12f * sin(angulo)), size.height * (0.12f + 0.05f * cos(angulo))),
            lado * 0.8f, Color(0xFF9BF5D2), 0.55f
        )
        brilho(
            Offset(size.width * (0.92f + 0.08f * cos(angulo)), size.height * (0.38f + 0.08f * sin(angulo))),
            lado * 0.85f, Color(0xFF7DD3FC), 0.5f
        )
        brilho(
            Offset(size.width * (0.28f + 0.15f * cos(angulo)), size.height * (0.9f + 0.04f * sin(angulo))),
            lado * 0.9f, Color(0xFFC4B5FD), 0.55f
        )
        brilho(
            Offset(size.width * (0.75f + 0.1f * sin(angulo)), size.height * (0.72f + 0.06f * cos(angulo))),
            lado * 0.5f, Color(0xFFB6F36B), 0.28f
        )
    }
}

private fun DrawScope.brilho(centro: Offset, raio: Float, cor: Color, opacidade: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(cor.copy(alpha = opacidade), Color.Transparent),
            center = centro,
            radius = raio
        ),
        radius = raio,
        center = centro
    )
}
