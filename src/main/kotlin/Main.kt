import clang.*
import org.antlr.v4.runtime.*

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    val lexer = ClangLexer(CharStreams.fromFileName(args.first()))
    val tokenStream = CommonTokenStream(lexer)
    val parser = ClangParser(tokenStream)
    parser.compilationUnit().apply {
        println(start)
        println(stop)
        children.forEach {
            println(it.text)
        }
    }
}
