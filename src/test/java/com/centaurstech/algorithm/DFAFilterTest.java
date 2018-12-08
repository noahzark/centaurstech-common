package com.centaurstech.algorithm;

import org.junit.Test;

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
        //初始化敏感词库

        DFAFilter dfaFilter = DFAFilter.fromWordSet(sensitiveWordSet);

        System.out.println("敏感词的数量：" + dfaFilter.sensitiveWordMapRoot.size());
        String string = "又到了white album的季节";
        System.out.println("待检测语句字数：" + string.length());

        //是否含有关键字
        boolean result;
        result = dfaFilter.contains(string);
        System.out.println(result);
        result = dfaFilter.contains(string, DFAFilter.MinMatchTYpe, -1, -1);
        System.out.println(result);

        //获取语句中的敏感词
        Set<String> set;
        set = dfaFilter.getSensitiveWord(string);
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        set = dfaFilter.getSensitiveWord(string, DFAFilter.MaxMatchType, 4, -1);
        System.out.println("语句中包含敏感词(4, +∞)的个数为：" + set.size() + "。包含：" + set);
        set = dfaFilter.getSensitiveWord(string, DFAFilter.MinMatchTYpe, 0, 5);
        System.out.println("语句中包含敏感词(0, 4]的个数为：" + set.size() + "。包含：" + set);

        //替换语句中的敏感词
        String filterStr = dfaFilter.replaceSensitiveWord(string, '*');
        System.out.println(filterStr);
        filterStr = dfaFilter.replaceSensitiveWord(string, '*', DFAFilter.MinMatchTYpe);
        System.out.println(filterStr);

        String filterStr2 = dfaFilter.replaceSensitiveWord(string, "[*敏感词*]");
        System.out.println(filterStr2);
        filterStr2 = dfaFilter.replaceSensitiveWord(string, "[*敏感词*]", DFAFilter.MinMatchTYpe);
        System.out.println(filterStr2);

        // 按出现顺序获取关键词列表
        List<String> resultList = dfaFilter.getSensitiveWordList(string);
        System.out.println(String.valueOf(resultList));
    }

}
