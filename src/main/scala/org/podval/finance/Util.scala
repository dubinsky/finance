package org.podval.finance

object Util:
  def fromRate(rate: Int): Double = rate.toDouble / 100.0

  def toRate(part: Double, whole: Double): Int = toInt(part / whole * 100.0)

  def toInt(amount: Double): Int = amount.round.toInt
  
  def compound(growth: Double, years: Int): Double = Math.pow(1.0 + growth, years)
