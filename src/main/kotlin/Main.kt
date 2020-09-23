import clang.*
import org.antlr.v4.runtime.*

fun main(args: Array<String>) {
    val lexer = ClangLexer(CharStreams.fromFileName("examples/demo.c"))
    val tokenStream = CommonTokenStream(lexer)
    val parser = ClangParser(tokenStream)
    println(parser.compilationUnit().children)
}