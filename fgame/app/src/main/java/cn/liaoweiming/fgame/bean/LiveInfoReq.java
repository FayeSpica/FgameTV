package cn.liaoweiming.fgame.bean;

public class LiveInfoReq {
    String appid;
    Integer pageNum;
    Integer pageSize;
    Integer i;

    public LiveInfoReq(String appid, Integer pageNum, Integer pageSize, Integer i) {
        this.appid = appid;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.i = i;
    }

    public LiveInfoReq(LiveLayout liveLayout, Integer pageNum, Integer pageSize, Integer i) {
        this.appid = liveLayout.getId();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.i = i;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "LiveInfoReq{" +
                "appid='" + appid + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", i=" + i +
                '}';
    }
}
