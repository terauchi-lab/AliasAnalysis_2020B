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

    fun checkExpr(){
        expressions.forEach {
            
        }
    }

    fun print() {
        println(name)
        expressions.forEach {
            println("\t${it.text}")
        }
        pointers.forEach {
            println("\t${it.first.text}:${it.second.map { context -> context.text }}")
        }
    }
}