package com.github.kmizu.nub.tool;

import com.github.kmizu.nub.NubLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

public class Streams {
    public static CommonTokenStream streamFrom(String input) throws IOException {
        ANTLRInputStream antlrStream = new ANTLRInputStream(new StringReader(input));
        NubLexer         lexer       = new NubLexer(antlrStream);
        return new CommonTokenStream(lexer);
    }

    public static CommonTokenStream streamFrom(File file) throws IOException {
        ANTLRInputStream antlrStream = new ANTLRInputStream(new FileInputStream(file));
        NubLexer lexer = new NubLexer(antlrStream);
        return new CommonTokenStream(lexer);
    }
}
