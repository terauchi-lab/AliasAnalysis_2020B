import clang.ClangParser

class Function(val name: String) {
    val variables = mutableSetOf<ClangParser.DirectDeclaratorContext>()
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