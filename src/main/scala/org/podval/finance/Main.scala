package org.podval.finance

object Main:
  def main(args: Array[String]): Unit =
    //val incomeTax = IncomeTax(200000, 55000)
    //println(incomeTax)
    //println(incomeTax.federalByBracket)

    //SocialSecurity.tabulate(2313)

    //val ss = SocialSecurity(62)
    //println(ss.totalPortion(65, 0.05))

    // When claiming at age catches up to claiming at 62:
    // growth  age
    //         63  65  67  70
    // ------  --  --  --  --
    // 0       77  78  79  81
    // 1%      79  79  80  82
    // 2%      80  81  82  83
    // 3%      82  82  83  85
    // 4%      84  84  85  87
    // 5%      88  88  89  90
    // 6%      95? 93  94  96   // TODO figure out the bug
    // 7%       X 108 109 110
    // 8%       X   X   X   X
    println(SocialSecurity.breakEven(62, 64, growth = 0.06))
    // TODO print Markdown table
