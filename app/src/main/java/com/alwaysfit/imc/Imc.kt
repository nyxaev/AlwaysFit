package com.alwaysfit.imc

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import java.util.Locale
import kotlin.math.roundToInt

val LocalePtBr: Locale = Locale("pt", "BR")

enum class Categoria(
    @StringRes val rotulo: Int,
    @StringRes val mensagem: Int,
    val cor: Color
) {
    AbaixoDoPeso(R.string.cat_abaixo, R.string.msg_abaixo, Color(0xFF7DD3FC)),
    Normal(R.string.cat_normal, R.string.msg_normal, Color(0xFF6EE7B7)),
    Sobrepeso(R.string.cat_sobrepeso, R.string.msg_sobrepeso, Color(0xFFFDE68A)),
    ObesidadeI(R.string.cat_ob1, R.string.msg_ob1, Color(0xFFFDBA74)),
    ObesidadeII(R.string.cat_ob2, R.string.msg_ob2, Color(0xFFFCA5A5)),
    ObesidadeIII(R.string.cat_ob3, R.string.msg_ob3, Color(0xFFFB7185))
}

fun calcularImc(pesoKg: Float, alturaCm: Float): Float {
    val metros = alturaCm / 100f
    return pesoKg / (metros * metros)
}

fun arredondar(valor: Float): Float = (valor * 10f).roundToInt() / 10f

fun categoriaDe(imc: Float): Categoria = when {
    imc < 18.5f -> Categoria.AbaixoDoPeso
    imc < 25f -> Categoria.Normal
    imc < 30f -> Categoria.Sobrepeso
    imc < 35f -> Categoria.ObesidadeI
    imc < 40f -> Categoria.ObesidadeII
    else -> Categoria.ObesidadeIII
}

fun pesoSaudavel(alturaCm: Float): Pair<Float, Float> {
    val metros = alturaCm / 100f
    return 18.5f * metros * metros to 24.9f * metros * metros
}

fun formatar(valor: Float, casas: Int): String =
    String.format(LocalePtBr, "%.${casas}f", valor)

fun ajustar(valor: Float, passo: Float, minimo: Float, maximo: Float): Float =
    ((valor / passo).roundToInt() * passo).coerceIn(minimo, maximo)
