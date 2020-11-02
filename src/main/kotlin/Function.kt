import clang.ClangParser
import org.antlr.v4.runtime.tree.ParseTree

class Function(val name: String) {
    val variables = mutableSetOf<ParseTree>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()

    fun print() {
        println(name)
        variables.forEach {
            println("\t${it.text}")
        }
        expressions.forEach {
            println("\t${it.text}")
        }
    }
}