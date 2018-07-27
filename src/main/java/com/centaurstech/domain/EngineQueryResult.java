package com.centaurstech.domain;

/**
 * All engine query result must extend this class
 * Created by BigYuki on 2017/3/14.
 * Updated by Feliciano Long
 */
public class EngineQueryResult {
    private String solution;
    private int status;
    private String process;

    /**
     * Initialize query result attributes
     */
    public EngineQueryResult() {
        this.solution = "我不知道你在说什么。";
        this.status = 0;
        this.process = "";
    }

    /**
     * Return an answer to engine
     *
     * @param solution Query result solution (answer returned to engine)
     */
    public EngineQueryResult(String solution) {
        this();
        setSolution(solution);
    }

    /**
     * Return an answer to engine and tells engine next step
     *
     * @param solution Answer string
     * @param process  Next step
     */
    public EngineQueryResult(String solution, String process) {
        this(solution);
        setProcess(process);
    }

    /**
     * Return an answer with exception status to engine
     *
     * @param solution Answer string
     * @param status   Exception status code
     */
    public EngineQueryResult(String solution, int status) {
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
    public EngineQueryResult(String solution, String process, int status) {
        this(solution, process);
        setStatus(status);
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String nextStep) {
        this.process = "nextStep=" + nextStep;
    }

    @Override
    public String toString() {
        return String.format("Query result: -solution: %s -status: %d -process: %s", solution, status, process);
    }

}
