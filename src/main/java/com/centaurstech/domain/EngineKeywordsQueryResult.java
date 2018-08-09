package com.centaurstech.domain;

/**
 * All engine query result must extend this class
 * Created by tangtongcheng on 2018/8/8.
 */
public class EngineKeywordsQueryResult extends EngineQueryResult{
    private String keywords;//be used to add keywords to engine in a single session

    /**
     * Initialize query result attributes
     */
    public EngineKeywordsQueryResult() {
        super();
        this.keywords="";
    }

    /**
     * Return an answer to engine
     *
     * @param solution Query result solution (answer returned to engine)
     */
    public EngineKeywordsQueryResult(String solution) {
        this();
        setSolution(solution);
    }

    /**
     * Return an answer to engine and tells engine next step
     *
     * @param solution Answer string
     * @param process  Next step
     */
    public EngineKeywordsQueryResult(String solution, String process) {
        this(solution);
        setProcess(process);
    }

    /**
     * Return an answer with exception status to engine
     *
     * @param solution Answer string
     * @param status   Exception status code
     */
    public EngineKeywordsQueryResult(String solution, int status) {
        this(solution);
        setStatus(status);
    }

    /**
     * Return an answer to engine, tells next step with an exception code
     *
     * @param solution Answer string
     * @param process  Next step
     * @param status   Exception status code
     */
    public EngineKeywordsQueryResult(String solution, String process, int status) {
        this(solution, process);
        setStatus(status);
    }


    @Override
    public String toString() {
        return String.format("Query result with keywords: -solution: %s -status: %d -process: %s -keywords: %s", this.getSolution(), this.getStatus(), this.getProcess(),keywords);
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

}
