object Main extends App {
  val input = io.Source.stdin.getLines.toSeq.head.trim.toInt

  def straightRight(level: Int): Int =
    if (level == 0)
      1
    else
      straightRight(level - 1) + (level - 1) * 8 + 1

  def btmRight(level: Int): Int = straightRight(level) + level * 7

  def dist(i: Int) = {
    val level = ((Stream from 0)
      .find { (l: Int) =>
        i <= btmRight(l)
      }
      .get
    )

    val indexInCircle = i - btmRight(Math.max(0, level - 1)) - level
    val modulus = Math.max(1, 2 * level)

    level + Math.abs(indexInCircle % modulus)
  }

  def shell(level: Int): List[Int] = {
    if (level <= 2)
      List(1, 2, 4, 5, 10, 11, 23, 25)
    else {
      val prev = shell(level - 1)
      val length = prev.length + 8
      val m = 2 * (level - 1) - 1

      def value(i: Int): Int = i match {
        case 0                       =>                prev.last   + prev(i)
        case 1                       => prev.last    + prev(i - 1) + prev(i)     + value(i - 1)
        case i if i < m-1            => prev(i - 2)  + prev(i - 1) + prev(i)     + value(i - 1)
        case i if i == m-1           => prev(i - 2)  + prev(i - 1)               + value(i - 1)
        case i if i == m             => prev(i - 2)                              + value(i - 1)
        case i if i == m+1           => value(i - 2) + prev(i - 2) + prev(i - 3) + value(i - 1)

        case i if i < m+m            => prev(i - 4)  + prev(i - 3) + prev(i - 2) + value(i - 1)
        case i if i == m+m           => prev(i - 4)  + prev(i - 3)               + value(i - 1)
        case i if i == m+m+1         => prev(i - 4)                              + value(i - 1)
        case i if i == m+m+1+1       => value(i - 2) + prev(i - 4) + prev(i - 5) + value(i - 1)

        case i if i < m+m+1+m        => prev(i - 6)  + prev(i - 5) + prev(i - 4) + value(i - 1)
        case i if i == m+m+1+m       => prev(i - 6)  + prev(i - 5)               + value(i - 1)
        case i if i == m+m+1+m+1     => prev(i - 6)                              + value(i - 1)
        case i if i == m+m+1+m+1+1   => value(i - 2) + prev(i - 6) + prev(i - 7) + value(i - 1)

        case i if i < m+m+1+m+1+m    => prev(i - 8) + prev(i - 7) + prev(i - 6) + value(i - 1)
        case i if i == m+m+1+m+1+m   => prev(i - 8) + prev(i - 7) + value(0)    + value(i - 1)
        case i if i == m+m+1+m+1+m+1 => prev(i - 8) + value(0)                  + value(i - 1)
      }

      (0 until length).toList map value
    }
  }

  def solveB(input: Int): Int =
    (Stream from 0)
      .flatMap(shell)
      .find(_ > input)
      .get

  println(s"A: ${dist(input)}")
  println(s"B: ${solveB(input)}")
}