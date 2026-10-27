import scala.util.Random

// Лабораторна робота 1. Основи програмування мовою Scala.

object Lab1:

  // 1. Методи для чисел і рядків.
  def add(x: Int, y: Int): Int = x + y

  // Умовний вираз у Scala повертає значення, тому return не потрібен.
  def maxOfTwo(x: Int, y: Int): Int = if x >= y then x else y

  // s-інтерполяція додає рівно один пробіл між словами.
  def joinWords(first: String, second: String): String = s"$first $second"

  // 2. Відбір чисел двома рівнозначними способами.
  private def matchesCondition(number: Int): Boolean =
    number > 5 && number < 10 && number != 8

  // for ... yield будує та повертає новий список.
  def selectNumbersFor(values: List[Int]): List[Int] =
    for number <- values if matchesCondition(number) yield number

  // filter також повертає новий список з елементами, що відповідають умові.
  def selectNumbersFilter(values: List[Int]): List[Int] =
    values.filter(matchesCondition)

  // Основний метод із завдання: реалізація через filter.
  def selectNumbers(values: List[Int]): List[Int] = selectNumbersFilter(values)

  // 3. headOption безпечно повертає Some(перший елемент) або None.
  def firstWord(values: List[String]): Option[String] = values.headOption

  /** Результати обробки масиву. Option захищає від min/max порожнього масиву. */
  final case class ArrayStatistics(
      length: Int,
      sum: Int,
      minimum: Option[Int],
      maximum: Option[Int],
      evenCount: Int
  )

  // 4. Метод не змінює переданий масив, а лише обчислює його характеристики.
  def arrayStatistics(values: Array[Int]): ArrayStatistics =
    ArrayStatistics(
      length = values.length,
      sum = values.sum,
      minimum = values.minOption,
      maximum = values.maxOption,
      evenCount = values.count(_ % 2 == 0)
    )

  /** Форматує множину у відсортованому порядку для сталого виведення. */
  def formatSet(values: Set[Int]): String = values.toList.sorted.mkString("{", ", ", "}")

  /** Форматує статистику для читабельного виведення у main. */
  def formatStatistics(values: Array[Int]): String =
    val statistics = arrayStatistics(values)
    s"елементи: ${values.mkString(", ")}; довжина: ${statistics.length}; " +
      s"сума: ${statistics.sum}; min: ${statistics.minimum}; " +
      s"max: ${statistics.maximum}; парних: ${statistics.evenCount}"

  def main(args: Array[String]): Unit =
    println("=== 1. Числа та рядки ===")
    println(s"add(1, 2) = ${add(1, 2)}")
    println(s"add(-4, 7) = ${add(-4, 7)}")
    println(s"maxOfTwo(2, 5) = ${maxOfTwo(2, 5)}")
    println(s"maxOfTwo(-3, -8) = ${maxOfTwo(-3, -8)}")
    println(s"maxOfTwo(4, 4) = ${maxOfTwo(4, 4)}")
    println(s"joinWords(\"Hello\", \"Scala\") = ${joinWords("Hello", "Scala")}")

    println("\n=== 2. Відбір елементів List ===")
    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val selectedByFor = selectNumbersFor(numbers)
    val selectedByFilter = selectNumbersFilter(numbers)
    println(s"Початковий список: $numbers")
    println(s"for yield: $selectedByFor")
    println(s"filter: $selectedByFilter")
    println(s"Результати однакові: ${selectedByFor == selectedByFilter}")
    println(s"Порожній список: ${selectNumbers(Nil)}")
    println(s"Границі та повтори: ${selectNumbers(List(5, 6, 6, 8, 9, 10))}")

    println("\n=== 3. Безпечний перший рядок ===")
    val fruits = List("apples", "avocado", "oranges")
    val emptyWords = List.empty[String]
    val wordLists = List(fruits, emptyWords, List("Empty"))
    wordLists.foreach { words =>
      firstWord(words) match
        case Some(word) => println(s"$words -> перший елемент: $word")
        case None       => println(s"$words -> список не містить елементів")
    }

    println("\n=== 4. Обробка Array ===")
    // nextInt(100) генерує ціле число від 0 до 99.
    val randomValues = Array.fill(10)(Random.nextInt(100))
    val testValues = Array(4, 7, 2, 9, 4)
    val emptyArray = Array.empty[Int]
    println(s"Випадковий масив: ${formatStatistics(randomValues)}")
    println(s"Тестовий масив: ${formatStatistics(testValues)}")
    println(s"Порожній масив: ${formatStatistics(emptyArray)}")

    println("\n=== 5. Операції над Set ===")
    val a = Set(1, 2, 3, 4, 5)
    val b = Set(4, 5, 6, 7)
    val union = a ++ b
    val intersection = a intersect b
    val difference = a.diff(b)
    println(s"a = ${formatSet(a)}")
    println(s"b = ${formatSet(b)}")
    println(s"Об'єднання: ${formatSet(union)}")
    println(s"Перетин: ${formatSet(intersection)}")
    println(s"Різниця a.diff(b): ${formatSet(difference)}")
    println(s"a містить число, кратне 3: ${a.exists(_ % 3 == 0)}")
    println(s"Пошук числа 3: ${a.find(_ == 3)}")
    println(s"Порожня множина: ${formatSet(Set.empty[Int])}")

    println("\n=== Додаткове завдання ===")
    val repeatedList = List(1, 1, 2, 2, 3)
    val uniqueSet = repeatedList.toSet
    println(s"List: $repeatedList, кількість елементів: ${repeatedList.size}")
    println(s"Set: ${formatSet(uniqueSet)}, кількість елементів: ${uniqueSet.size}")
    println("Під час перетворення List у Set втрачаються повтори, але зберігаються унікальні значення.")
