package com.centaurstech.algorithm;

import org.junit.Test;

import com.centaurstech.algorithm.DFAFilter.MatchType;
import com.centaurstech.algorithm.DFAFilter.DfaResultNode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DFAFilterTest {

    @Test
    public void testDFAFilter() {
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet.add("工程师f");
        sensitiveWordSet.add("工程师foo");
        sensitiveWordSet.add("bar");
        //初始化敏感词库

        DFAFilter dfaFilter = DFAFilter.fromWordSet(sensitiveWordSet);

        String txt = "测试工程师foo来到bar";
        String notMatchedTxt = "测试工程师boo来到far";
        
        List<DfaResultNode> assertList;
        
        // ------ MAX ------
        // without length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师foo", 2), 
                new DfaResultNode("bar", 10)
                );
        assertEquals(true, dfaFilter.contains(txt, MatchType.MAX, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.MAX, null, null));
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MAX, null, null));
        // with length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师foo", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MAX, 4, null));
        assertList = Arrays.asList(
                new DfaResultNode("bar", 10)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MAX, null, 4));
        assertList = Arrays.asList(
                new DfaResultNode("工程师foo", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MAX, 4, 7));
        
        // ------ MIN ------
        // without length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2), 
                new DfaResultNode("bar", 10)
                );
        assertEquals(true, dfaFilter.contains(txt, MatchType.MIN, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.MIN, null, null));
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MIN, null, null));
        // with length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MIN, 4, null));
        assertList = Arrays.asList(
                new DfaResultNode("bar", 10)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MIN, null, 4));
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.MIN, 4, 7));
        
        // ------ ALL ------
        // without length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2), 
                new DfaResultNode("工程师foo", 2), 
                new DfaResultNode("bar", 10)
                );
        assertEquals(true, dfaFilter.contains(txt, MatchType.ALL, null, null));
        assertEquals(false, dfaFilter.contains(notMatchedTxt, MatchType.ALL, null, null));
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.ALL, null, null));
        // with length filter
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2), 
                new DfaResultNode("工程师foo", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.ALL, 4, null));
        assertList = Arrays.asList( 
                new DfaResultNode("bar", 10)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.ALL, null, 4));
        assertList = Arrays.asList(
                new DfaResultNode("工程师f", 2), 
                new DfaResultNode("工程师foo", 2)
                );
        assertListContentToStringEquals(assertList, dfaFilter.getDfaResult(txt, MatchType.ALL, 4, 7));
    }
    
    private static void assertListContentToStringEquals(List<DfaResultNode> list1, List<DfaResultNode> list2) {
        assertEquals(
                list1.stream().map(it -> it.getText() + it.getStartIndex()).collect(Collectors.joining(",")), 
                list2.stream().map(it -> it.getText() + it.getStartIndex()).collect(Collectors.joining(","))
                );
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testDFAFilterLegacy() {
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
