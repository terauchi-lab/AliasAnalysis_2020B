import clang.ClangParser

class Function(val name: String) {
    val variables = mutableSetOf<ClangParser.DirectDeclaratorContext>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()
    val pointers =
        mutableListOf<Pair<ClangParser.DirectDeclaratorContext, MutableSet<ClangParser.DirectDeclaratorContext>>>()

    fun initPointers() {
        variables.forEach {
            pointers.add(Pair(it, mutableSetOf()))
        }
    }

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