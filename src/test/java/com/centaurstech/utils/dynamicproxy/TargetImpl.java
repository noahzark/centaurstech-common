package com.centaurstech.utils.dynamicproxy;

/**
 * @author 徐刘根
 * https://blog.csdn.net/xlgen157387/article/details/82497594 
 */
public class TargetImpl implements Target {

    @Override
    public int test(int i) {
        return i + 1;
    }
}
