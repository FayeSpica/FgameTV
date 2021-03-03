package cn.liaoweiming.fgame.bean.fgame;

public class Key {
    String module;
    String method;
    String retMsg;
    Integer retCode;
    RetBody retBody;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public RetBody getRetBody() {
        return retBody;
    }

    public void setRetBody(RetBody retBody) {
        this.retBody = retBody;
    }

    @Override
    public String toString() {
        return "Key{" +
                "module='" + module + '\'' +
                ", method='" + method + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", retCode=" + retCode +
                ", retBody=" + retBody +
                '}';
    }
}
