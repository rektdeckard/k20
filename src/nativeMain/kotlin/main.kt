import kotlin.random.Random

val pattern = Regex("""(\d+[(x])?(\d+)?d(\d+)\s?([to]\d+)?\s?(\+\s?\d+)?(\))?""", RegexOption.IGNORE_CASE)

fun main(args: Array<String>) {
//    val args = readLine()?.split(" ") ?: listOf("1d8", "2d20", "1j20", "3d4 + 12", "4(d6+1)", "6d6t5")
    args.forEach { parseRoll(it, pattern)?.execute() ?: println("Could not parse $it") }
}

class DiceRoll(
    private val count: Int,
    private val die: Int,
    private val mod: Int?,
    private val take: Int?,
    private val times: Int?,
) {
    fun execute() {
        repeat(times ?: 1) {
            if (die <= 1) return println("Cannot roll that kind of die :)")

            val dice = generateSequence { Random.nextInt(from = 1, until = die + 1) }.take(count).toList()
            val total = (take?.let {
                if (it > 0) dice.sorted().takeLast(it).sum() else dice.sorted().take(count + it).sum()
            } ?: dice.sum()) + (mod ?: 0)

            print("${count}d${die}${take?.let { "t$take" } ?: ""}${mod?.let { "+$mod" } ?: ""}: ".padEnd(12))
            print("$total  ".padStart(4))
//            showDice(dice)
            println(dice)
        }
    }

    private fun showDice(dice: List<Int>) {
        val taken = take?.let { if (it > 0) dice.sorted().takeLast(it) else dice.sorted().take(count + it) }
        print("[")
        dice.forEach {
            if (taken?.contains(it) == true) {
                print("\u001B[4m")
                print(it)
                print("\u001B[0m")
                print(", ")
            } else {
                print("$it, ")
            }
        }
        println("]")
    }
}

fun parseRoll(it: String, pattern: Regex): DiceRoll? {
    return if (pattern.matches(it)) {
        val match = pattern.find(it)
        match?.destructured?.let { res ->
            val times = res.component1()
                .replace("(", "")
                .replace("x", "", true)
                .trim()
                .toIntOrNull()
            val count = res.component2().toIntOrNull()
            val die = res.component3().toInt()
            val take = res.component4()
                .replace("t", "", true)
                .replace("o", "-", true)
                .toIntOrNull()
            val mod = res.component5()
                .replace("+", "")
                .trim()
                .toIntOrNull()

            DiceRoll(count ?: 1, die, mod, take, times)
        }
    } else {
        null
    }
}