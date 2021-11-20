package com.centaurstech.algorithm;

import org.junit.Test;

import com.centaurstech.algorithm.DFAFilter.MatchType;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DFAFilterTest {

    @Test
    public void testDFAFilter() {
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet.add("white");
        sensitiveWordSet.add("album");
        sensitiveWordSet.add("季节");
        sensitiveWordSet.add("季");
        sensitiveWordSet.add("it");
        //初始化敏感词库

        DFAFilter dfaFilter = DFAFilter.fromWordSet(sensitiveWordSet);

        String txt = "又到了white album的季节";
        String notMatchedTxt = "又到了whi albu的节";
        
        List<String> assertList;
        
        // ------ MAX ------
        // without length filter
        assertList = Arrays.asList("white", "album", "季节");
        assertEquals(true, dfaFilter.contains(txt, MatchType.MAX, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.MAX, null, null));
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MAX, null, null));
        // with length filter
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MAX, 5, null));
        assertList = Arrays.asList("it", "季节");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MAX, null, 5));
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MAX, 5, 6));
        
        // ------ MIN ------
        // without length filter
        assertList = Arrays.asList("white", "album", "季");
        assertEquals(true, dfaFilter.contains(txt, MatchType.MIN, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.MIN, null, null));
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MIN, null, null));
        // with length filter
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MIN, 5, null));
        assertList = Arrays.asList("it", "季");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MIN, null, 5));
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.MIN, 5, 6));
        
        // ------ ALL ------
        // without length filter
        assertList = Arrays.asList("white", "it", "album", "季", "季节");
        assertEquals(true, dfaFilter.contains(txt, MatchType.ALL, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.ALL, null, null));
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.ALL, null, null));
        // with length filter
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.ALL, 5, null));
        assertList = Arrays.asList("it", "季", "季节");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.ALL, null, 5));
        assertList = Arrays.asList("white", "album");
        assertEquals(assertList, dfaFilter.getSensitiveWordList(txt, MatchType.ALL, 5, 6));
    }

}
