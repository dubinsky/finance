package org.podval.finance

final class SocialSecurity(
  ageClaimed: Int,
  pia: Int = 1
):
  require(SocialSecurity.early <= ageClaimed && ageClaimed <= SocialSecurity.late)

  override def toString: String = s"$ageClaimed: $$$benefit (${Util.toInt(portion*100)}%)"

  def benefit: Int = Util.toInt(pia * portion)

  def portion: Double = 1.0 + SocialSecurity.adjustment(ageClaimed)

  def totalBenefit(age: Int, growth: Double) = Util.toInt(pia * totalPortion(age, growth))

  def totalPortion(age: Int, growth: Double): Double =
    val length: Int = age - ageClaimed
    require(length >= 0)
    1.to(age - ageClaimed).map(year => portion * Util.compound(growth, length - year)).sum

// https://www.congress.gov/crs-product/IF11747
// Adjustments for Claiming Age
object SocialSecurity:
  // The earliest eligibility age is the age at which a retired worker can first claim benefits (age 62).
  private val early: Int = 62
  // The full retirement age (FRA) is the age at which the worker can receive the full PIA increased by any COLAs.
  // For workers born in 1960 or later, the FRA is age 67.
  private val full: Int = 67
  // The permanent reduction in monthly benefits that applies to people who claim before the FRA
  // is an actuarial reduction.
  // It equals five-ninths of 1% for each month (6⅔% per year) for the first three years of early claim
  private val firstYears: Int = 3
  private val first: Double = -(0.06 + 0.02/3.0)
  // and five-twelfths of 1% for each month (5% per year) beyond 36 months.
  private val after: Double = -0.05

  // The permanent increase in monthly benefits that applies to those who claim after the FRA
  // is called the delayed retirement credit (DRC).
  // For people born after 1942, the DRC is 8% for each year of delayed claim after the FRA up to age 70.
  private val late: Int = 70
  private val dcr: Double = 0.08

  private def adjustment(ageClaimed: Int): Double = if ageClaimed >= full then (ageClaimed - full) * dcr else
    val yearsEarly: Int = full - ageClaimed
    val firstAdjustment: Double = Math.min(yearsEarly, firstYears) * first
    val afterAdjustment = Math.max(yearsEarly - firstYears, 0) * after
    firstAdjustment + afterAdjustment


  def tabulate(pia: Int): Unit = early.to(late).map(SocialSecurity(_, pia)).foreach(println)

  def breakEven(ageClaimed1: Int, ageClaimed2: Int, growth: Double): Int =
    require(ageClaimed1 <= ageClaimed2)
    ageClaimed2.to(120).find(age =>
      SocialSecurity(ageClaimed1).totalPortion(age, growth)/SocialSecurity(ageClaimed2).totalPortion(age, growth) <= 1.0
    ).get
