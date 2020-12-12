import clang.ClangParser

class Call(name: String, args: List<ClangParser.AssignmentExpressionContext>) {
    private val func: Function?

    init {
        func = null
    }
}