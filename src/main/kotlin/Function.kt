import org.antlr.v4.runtime.tree.ParseTree

class Function(val name: String) {
    val variables = mutableListOf<ParseTree>()

    fun print() {
        println(name)
        variables.forEach {
            println("\t$it")
        }
    }
}