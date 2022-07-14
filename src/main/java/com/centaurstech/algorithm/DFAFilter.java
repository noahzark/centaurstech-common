package com.centaurstech.algorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 参考https://my.oschina.net/magicalSam/blog/1528428
 * 修改了其中不安全（IDE警告）的部分
 *
 * @author hundun
 * Created on 2018/12/04
 */
public class DFAFilter {
    
    public static class DfaResultNode {
        private final String text;
        private final int startIndex;
        
        public DfaResultNode(String text, int startIndex) {
            super();
            this.text = text;
            this.startIndex = startIndex;
        }
        public String getText() {
            return text;
        }
        public int getStartIndex() {
            return startIndex;
        }
    }
    
    public enum MatchType {
        /**
         * 最短，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：[中国]
         */
        MIN,
        /**
         * 最长，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：[中国人]
         */
        MAX,
        /**
         * 所有，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：[中国, 中国人]  
         */
        ALL
    }

    /**
     * @Deprecated use enum MatchType
     */
    @Deprecated
    public static final int MinMatchTYpe = 1; 
    
    /**
     * @Deprecated use enum MatchType
     */
    @Deprecated
    public static final int MaxMatchType = 2;      

    public class DFANode {
        /**
         * 下一级节点
         */
        HashMap<Character, DFANode> data;
        /**
         * 是否某个敏感词在该node结束
         */
        boolean isEnd;

        public DFANode() {
            this.data = new HashMap<>();
            this.isEnd = true;
        }

        public DFANode get(Character character) {
            return data.get(character);
        }

        public void put(Character character, DFANode node) {
            data.put(character, node);
        }

        public int size() {
            return data.size();
        }

    }

    /**
     * 使用敏感词Set构造一个Filter
     * @param sensitiveWordSet
     * @return
     */
    public static DFAFilter fromWordSet(Set<String> sensitiveWordSet) {
        DFAFilter dfaFilter = new DFAFilter();
        return dfaFilter.init(sensitiveWordSet);
    }

    /**
     * 敏感词集合
     */
    public DFANode sensitiveWordMapRoot;

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }
    
    
    private MatchType codeToMatchType(int code) {
        MatchType matchType;
        if (code == MinMatchTYpe) {
            matchType = MatchType.MIN;
        } else {
            matchType = MatchType.MAX;
        }
        return matchType;
    }

    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param sensitiveWordSet 敏感词库
     */
    public synchronized DFAFilter init(Set<String> sensitiveWordSet) {
        initSensitiveWordMap(sensitiveWordSet);
        return this;
    }

    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param sensitiveWordSet 敏感词库
     */
    private void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        
        sensitiveWordMapRoot = new DFANode();
        String key;
        DFANode nowMap;
        DFANode newWorMap;
 
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {

            key = iterator.next();
            nowMap = sensitiveWordMapRoot;
            for (int i = 0; i < key.length(); i++) {

                Character keyChar = key.charAt(i);

                DFANode wordMap = nowMap.get(keyChar);

                if (wordMap != null) {
                    nowMap = wordMap;
                } else {
                    newWorMap = new DFANode();
                    newWorMap.isEnd = false;
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.isEnd = true;
                }
            }
        }
    }

    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public boolean contains(String txt, int matchTypeCode, int floor, int ceiling) {
        List<String> words = getSensitiveWordList(txt, matchTypeCode, floor, ceiling);
        return words.size() > 0;
    }
    

    public boolean contains(String txt, MatchType matchType) {
        return contains(txt, matchType, null, null);
    }
    
    /**
     * 计算txt中是否存在敏感词
     * @param lengthFilterFloor include; 不限制则传null;
     * @param lengthFilterCeiling exclude; 不限制则传null;
     */
    public boolean contains(String txt, MatchType matchType, Integer lengthFilterFloor, Integer lengthFilterCeiling) {
        List<String> words = getSensitiveWordList(txt, matchType, lengthFilterFloor, lengthFilterCeiling);
        return words.size() > 0;
    }
    

    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public boolean contains(String txt) {
        return contains(txt, MaxMatchType, -1, -1);
    }

    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public Set<String> getSensitiveWord(String txt, int matchType, int lengthFloor, int lengthCeiling) {
        return new HashSet<>(getSensitiveWordList(txt, matchType, lengthFloor, lengthCeiling));
    }

    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public List<String> getSensitiveWordList(String txt) {
        return getSensitiveWordList(txt, MaxMatchType, -1, -1);
    }
    
    
    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public List<String> getSensitiveWordList(String txt, int matchTypeCode, int floor, int ceiling) {
        return getSensitiveWordList(txt, codeToMatchType(matchTypeCode), floor < 0 ? null : floor, ceiling < 0 ? null : ceiling);
    }
    
    
    public List<String> getSensitiveWordList(String txt, MatchType matchType) {
        return getSensitiveWordList(txt, matchType, null, null);
    }

    /**
     * @Deprecated use getDfaResults
     * 计算txt中的所有敏感词。结果排序与该敏感词在原文中的其实位置一致。
     * @param lengthFilterFloor include; 不限制则传null;
     * @param lengthFilterCeiling exclude; 不限制则传null;
     */
    @Deprecated
    public List<String> getSensitiveWordList(String txt, MatchType matchType, Integer lengthFilterFloor, Integer lengthFilterCeiling) {
        List<DfaResultNode> dfaResultNodes = getDfaResult(txt, matchType, lengthFilterFloor, lengthFilterCeiling);
        return dfaResultNodes.stream().map(node -> node.getText()).collect(Collectors.toList());
    }
    
    /**
     * 计算txt中的所有敏感词。结果包含其在原串的起始位置。同一个敏感词出现于不同位置时得到多个结果。
     * @param lengthFilterFloor include; 不限制则传null;
     * @param lengthFilterCeiling exclude; 不限制则传null;
     * @return not null
     */
    public List<DfaResultNode> getDfaResult(String txt, MatchType matchType, Integer lengthFilterFloor, Integer lengthFilterCeiling) {
        List<DfaResultNode> result = new ArrayList<>();

        for (int i = 0; i < txt.length(); i++) {
            
            List<DfaResultNode> wordsFromBeginIndex = getDfaResultNodesFromBeginIndex(txt, i);
            
            wordsFromBeginIndex.removeIf(node -> {
                String word = node.getText();
                boolean floorMatch = lengthFilterFloor == null || word.length() >= lengthFilterFloor;
                boolean ceilingMatch = lengthFilterCeiling == null || word.length() < lengthFilterCeiling;
                return !floorMatch || !ceilingMatch;
            });
            
            
            if (wordsFromBeginIndex.size() > 0) {
                wordsFromBeginIndex.sort(Comparator.comparingInt(node -> node.getText().length()));

                switch (matchType) {
                    case MIN:
                        result.add(wordsFromBeginIndex.get(0));
                        // skip some length
                        i = i + wordsFromBeginIndex.get(0).getText().length() - 1;
                        break;
                    case MAX:
                        result.add(wordsFromBeginIndex.get(wordsFromBeginIndex.size() - 1));
                        // skip some length
                        i = i + wordsFromBeginIndex.get(wordsFromBeginIndex.size() - 1).getText().length() - 1;
                        break;
                    case ALL:    
                        result.addAll(wordsFromBeginIndex);
                        // not skip
                        break; 
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        }

        return result;
    }

    /**
     * @Deprecated use enum matchType
     */
    @Deprecated
    public Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType, -1, -1);
    }


    /**
     * 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @Deprecated getWordsList()后自行替换，本工具不再提供替换方法
     */
    @Deprecated
    public String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType, -1, -1);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * @Deprecated getWordsList()后自行替换，本工具不再提供替换方法
     */
    @Deprecated
    public String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     * 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @Deprecated getWordsList()后自行替换，本工具不再提供替换方法
     */
    @Deprecated
    public String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType, -1, -1);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }

        return resultTxt;
    }

    /**
     * @Deprecated getWordsList()后自行替换，本工具不再提供替换方法
     */
    @Deprecated
    public String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
    }


    /**
     * 计算txt中，以beginIndex为起点，的所有敏感词
     */
    private List<DfaResultNode> getDfaResultNodesFromBeginIndex(String input, int beginIndex) {
        
        List<Integer> matchedLengthList = new ArrayList<>(1);
        
        int matchedLength = 0;
        
        Character checkingChar;
        DFANode currentNode = sensitiveWordMapRoot;
        
        for (int i = beginIndex; i < input.length(); i++) {
            checkingChar = input.charAt(i);
            currentNode = currentNode.get(checkingChar);
            if (currentNode != null) {
                matchedLength++;
                if (currentNode.isEnd) {
                    matchedLengthList.add(matchedLength);
                }
            } else {
                break;
            }
        }
        List<DfaResultNode> result = matchedLengthList.stream()
                .map(length -> {
                    String text = input.substring(beginIndex, beginIndex + length);
                    return new DfaResultNode(text, beginIndex);
                })
                .collect(Collectors.toList());
        
        return result;
    }

}
