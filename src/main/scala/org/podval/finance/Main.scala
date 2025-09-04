package org.podval.finance

object Main:
  def main(args: Array[String]): Unit =
    val incomeTax = IncomeTax(200000, 55000)
    println(incomeTax)
    println(incomeTax.federalByBracket)

    //println(SocialSecurity.benefitTable(2313))

    // Social Security break-even:
    //   62, 63, 64 -> 67, 68, 69: 79
    //println(SocialSecurity.breakEven(62, 67, growth = 0.07))
