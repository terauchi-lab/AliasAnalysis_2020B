import clang.ClangParser

class Call(val name: String, val args: List<ClangParser.AssignmentExpressionContext>) {
    private val func: Function?

    init {
        func = null
    }

    override fun toString(): String {
        return "$name(${args.map { it.text }.toString().drop(1).dropLast(1)})"
    }
}