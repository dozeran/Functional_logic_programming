import scala.util.Random

// клас адреси
class Address(private val _country: String, private val _city: String) {
    require(_country.trim.nonEmpty, "Країна не може бути порожньою")
    require(_city.trim.nonEmpty, "Місто не може бути порожнім")

    def country: String = _country
    def city: String = _city

    override def toString: String = s"$country, $city"
}

// клас людини
class Human(private val _name: String, private val _age: Int,
            private val _address: Address) {
    require(_name.trim.nonEmpty, "Імʼя не може бути порожнім")
    require(_age >= 0, "Вік не може бути від'ємним")

    def name: String = _name
    def age: Int = _age
    def address: Address = _address

    def show(): String =
        s"Імʼя: $name, вік: $age, адреса: $address"
}

// клас студента
class Student(name: String, age: Int, private val _group: String,
              address: Address) extends Human(name, age, address) {
    require(_group.trim.nonEmpty, "Назва групи не може бути порожньою")

    private val _estimation: Int = Random.nextInt(5) + 1

    def group: String = _group
    def estimation: Int = _estimation

    override def show(): String =
        super.show() + s", група: $group, оцінка: $estimation"
}

// клас викладача
class Teacher(name: String, age: Int, private val _salary: Double,
              private val _limit: Int, address: Address)
    extends Human(name, age, address) {
    require(_salary >= 0, "Зарплата не може бути відʼємною")
    require(_limit >= 0, "Обмеження не може бути відʼємним")

    private var _students: List[Student] = List.empty[Student]

    def salary: Double = _salary
    def limit: Int = _limit
    def students: List[Student] = _students
    def studentCount: Int = _students.size

    def addStudent(student: Student): Unit = {
        require(studentCount < _limit, "Перевищено ліміт студентів для викладача")
        require(!_students.exists(s => s eq student), "Цього студента вже додано")
        _students = _students :+ student
    }

    def findStudentsByGroup(group: String): List[Student] =
        _students.filter(_.group == group)

    def averageEstimation: Option[Double] = {
        if (_students.isEmpty) None
        else Some(_students.map(_.estimation).sum.toDouble / studentCount)
    }

    override def show(): String =
        super.show() + s", зарплата: $salary грн, студентів: $studentCount, обмеження кількості студентів: $limit"

    def printStudents(): Unit =
        _students.foreach(student => println("    " + student.show()))
}

// клас даних про університет
class DataList(private val _teachers: List[Teacher] = List.empty[Teacher]) {
    def teachers: List[Teacher] = _teachers

    def printAllInformation(): Unit =
        _teachers.foreach { teacher =>
            println(teacher.show())
            teacher.printStudents()
        }

    def findTeachers(count: Int): List[Teacher] =
        _teachers.filter(_.studentCount > count)

    def sortByStudentCount(): List[Teacher] =
        _teachers.sortBy(teacher => -teacher.studentCount)

    def findTeachersByCity(city: String): List[Teacher] =
        _teachers.filter(_.address.city == city)

    def totalStudentAssignments: Int =
        _teachers.map(_.studentCount).sum
}

// обʼєкт ХДУ
object KSU {
    def main(args: Array[String]): Unit = {

        // створюємо міста
        val kyiv = new Address("Україна", "Київ")
        val kherson = new Address("Україна", "Херсон")
        val mykolaiv = new Address("Україна", "Миколаїв")

        // створюємо студентів
        val student1 = new Student("Денис", 38, "51-281", kyiv)
        val student2 = new Student("Тетяна", 18, "51-281", kherson)
        val student3 = new Student("Ярослав", 18, "51-281", mykolaiv)
        val student4 = new Student("Владислав", 19, "51-281", kherson)

        // створюємо вчителів
        val teacher1 = new Teacher("Максим", 40, 50000, 15, kyiv)
        val teacher2 = new Teacher("Ольга", 36, 38000, 10, kherson)
        val teacher3 = new Teacher("Володимир", 47, 25000, 20, kherson)

        // додаємо студентів
        teacher1.addStudent(student1)
        teacher1.addStudent(student2)
        teacher2.addStudent(student3)
        teacher3.addStudent(student4)

        // створюємо список даних викладачів
        val data = new DataList(List(teacher1, teacher2, teacher3))

        // WORK + OUTPUT:
        println("==== Повна інформація ====")
        data.printAllInformation()

        println("\n==== Викладачі з більш, ніж 1 студентом ====")
        val founded = data.findTeachers(1)
        if (founded.isEmpty) println("Викладачів не знайдено")
        founded.foreach(t => println(s"${t.name}: ${t.studentCount} студенти"))

        println("\n==== Інші пороги пошуку ====")
        List(0, 2, 3).foreach { count =>
            val result = data.findTeachers(count)
            if (result.isEmpty) println(s"Більше за $count: викладачів не знайдено.")
            else result.foreach(t => println(s"Більше за $count: ${t.name}"))
        }

        println("\n==== Викладачі, відсортовані за кількістю студентів ====")
        data.sortByStudentCount().foreach(t => println(s"${t.name}: ${t.studentCount} студентів"))

        println("\n==== Поліморфізм ====")
        val people: List[Human] = List(student1, teacher1)
        people.foreach(person => println(person.show()))

        println("\n==== Студенти пана Максима групи 51-281 ====")
        val students = teacher1.findStudentsByGroup("51-281")
        if (students.isEmpty) println("Студентів не знайдено")
        students.foreach(student => println(student.name))

        println("\n==== Студенти невідомої групи ====")
        if (teacher1.findStudentsByGroup("66-666").isEmpty)
            println("Студентів групи 66-666 не знайдено")

        println("\n==== Середня оцінка студентів у викладачів ====")
        println("Максим: " + teacher1.averageEstimation.getOrElse("середня оцінка відсутня"))
        println("Володимир: " + teacher3.averageEstimation.getOrElse("середня оцінка відсутня"))

        println("\n==== Викладачі з Херсону ====")
        data.findTeachersByCity("Херсон").foreach(t => println(t.name))
        if (data.findTeachersByCity("Івано-Франківськ").isEmpty)
            println("Викладачів з Івано-Франківська не знайдено")

        println("\n==== Загальна кількість студентів ==== " + data.totalStudentAssignments)
        println("Список порожніх даних: " + new DataList().totalStudentAssignments)

        println("\n==== Відʼємні та порожні перевірки ====")
        def check(test: => Unit): Unit = {
            try { test }
            catch { case e: IllegalArgumentException => println(e.getMessage) }
        }

        check { new Human("Перевірка_1", -1, kyiv) }
        check { new Teacher("Перевірка_2", 30, -100, 2, kyiv) }
        check { new Teacher("Перевірка_3", 30, 1000, -1, kyiv) }
        check { new Student(" ", 18, "51-281", kyiv) }
        check { new Student("Перевірка_4", 18, "   ", kyiv) }
        check { new Address("  ", "Херсон") }
        check { new Address("Україна", "  ") }

        check { teacher3.addStudent(student1) }
        println("Кількість студентів пана Володимира: " + teacher3.studentCount)

        val limited = new Teacher("Перевірка_5", 30, 1000, 1, kyiv)
        limited.addStudent(student1)
        check { limited.addStudent(student2) }
        println("Обмеження кількості студентів: " + limited.studentCount)

        check { teacher1.addStudent(student1) }
        println("Кількість студентів пана Максима після додавання дублю: " + teacher1.studentCount)
    }
}
