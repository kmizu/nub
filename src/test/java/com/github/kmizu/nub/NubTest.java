package com.github.kmizu.nub;

import com.github.kmizu.nub.tool.Streams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class NubTest {
    static Object eval(String input) throws Exception {
        return new Evaluator().evaluate(new NubParser(Streams.streamFrom(input)).program().e);
    }

    @Test
    public void testPlus() throws Exception {
        assertEquals((Integer)1, eval("1;"));
        assertEquals((Integer)2, eval("1+1;"));
        assertEquals((Integer)3, eval("1+1+1;"));
    }

    @Test
    public void testMinus() throws Exception {
        assertEquals((Integer)0, eval("1-1;"));
        assertEquals((Integer)(-1), eval("1-1-1;"));
        assertEquals((Integer)(-2), eval("1-1-1-1;"));
    }

    @Test
    public void testRemainder() throws Exception {
        assertEquals((Integer)0, eval("1%1;"));
        assertEquals((Integer)(1), eval("5%2;"));
        assertEquals((Integer)(2), eval("5%3;"));
    }

    @Test
    public void testString() throws Exception {
        assertEquals((String)("1"), eval("\"1\";"));
        assertEquals((String)("2"), eval("\"2\";"));
        assertEquals((String)("3"), eval("\"3\";"));
    }

    @Test
    public void testPow() throws Exception {
        assertEquals((Integer)(4), eval("2**2;"));
        assertEquals((Integer)(8), eval("2**3;"));
        assertEquals((Integer)(16), eval("2**4;"));
    }

    @Test
    public void testBool() throws Exception {
        assertEquals((Boolean)(true), eval("true;"));
        assertEquals((Boolean)(false), eval("false;"));
    }
}
