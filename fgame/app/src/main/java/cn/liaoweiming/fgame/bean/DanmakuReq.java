package cn.liaoweiming.fgame.bean;

public class DanmakuReq {
    Long anchorId;
    String pid;
    String lastTm;

    public DanmakuReq(Long anchorId, String pid, String lastTm) {
        this.anchorId = anchorId;
        this.pid = pid;
        this.lastTm = lastTm;
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLastTm() {
        return lastTm;
    }

    public void setLastTm(String lastTm) {
        this.lastTm = lastTm;
    }
}
