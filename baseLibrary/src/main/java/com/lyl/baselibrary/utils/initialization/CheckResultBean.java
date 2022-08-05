package com.lyl.baselibrary.utils.initialization;

/**
 * @Author lyl
 * @Date 2022/07/21
 */
public class CheckResultBean {
    private int state = -1;
    private String checkResult;

    private String checkTitle;

    public int getState() {
        return state;
    }
    public boolean isState() {
        return state==1;
    }
    public void setState(int state) {
        this.state = state;
    }


    public String getCheckResult() {
        return checkResult;
    }

    public String getCheckTitle() {
        return checkTitle;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public void setCheckTitle(String checkTitle) {
        this.checkTitle = checkTitle;
    }

    public CheckResultBean() {
    }


    @Override
    public String toString() {
        return "CheckResultBean{" +
                "state=" + state +
                ", checkResult='" + checkResult + '\'' +
                ", checkTitle='" + checkTitle + '\'' +
                '}';
    }
}
