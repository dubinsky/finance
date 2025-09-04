package org.podval.finance

// https://www.congress.gov/crs-product/IF11747
//
// Adjustments for Claiming Age
//
// The earliest eligibility age is the age at which a retired worker can first claim benefits (age 62).
// The full retirement age (FRA) is the age at which the worker can receive the full PIA increased by any COLAs.
// For workers born in 1960 or later, the FRA is age 67.
//
// The permanent reduction in monthly benefits that applies to people who claim before the FRA
// is an actuarial reduction.
// It equals five-ninths of 1% for each month (6⅔% per year) for the first three years of early claim
// and five-twelfths of 1% for each month (5% per year) beyond 36 months.
//
// The permanent increase in monthly benefits that applies to those who claim after the FRA
// is called the delayed retirement credit (DRC).
// For people born after 1942, the DRC is 8% for each year of delayed claim after the FRA up to age 70.
object SocialSecurity:
  val early: Int = 62
  val full: Int = 67
  val late: Int = 70
  private val firstYears: Int = 3
  private val first: Double = -0.0666666
  private val after: Double = -0.05
  private val dcr: Double = 0.08

  def breakEven(ageClaimedAt1: Int, ageClaimedAt2: Int, growth: Double): Int =
    require(ageClaimedAt1 <= ageClaimedAt2)
    ageClaimedAt2.to(120).find(age => ratio(ageClaimedAt1, ageClaimedAt2, age, growth) <= 1.0).get

  def ratio(ageClaimedAt1: Int, ageClaimedAt2: Int, age: Int, growth: Double): Double =
    total(ageClaimedAt1, age, growth)/total(ageClaimedAt2, age, growth)

  def total(ageClaimedAt: Int, age: Int, growth: Double): Double =
    val length: Int = age - ageClaimedAt
    require(length >= 0)
    val benefit: Double = portionWhenClaimedAt(ageClaimedAt)
    1.to(age-ageClaimedAt).map(year => benefit*(1.0 + Math.pow(1.0+growth, length-year))).sum

  def benefitTable(pia: Int): Unit =
    early.to(late).foreach(age => println(s"$age: ${benefitWhenClaimedAt(age, pia)}"))

  def benefitWhenClaimedAt(age: Int, pia: Int): Int = (pia * portionWhenClaimedAt(age)).toInt

  def portionWhenClaimedAt(age: Int): Double =
    require(early <= age && age <= late)
    val adjustment: Double =
      if age >= full then (age - full) * dcr else
        val years: Int = full - age
        if years <= firstYears then years * first else firstYears * first + (years - 3) * after

    1.0 + adjustment
