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
}
