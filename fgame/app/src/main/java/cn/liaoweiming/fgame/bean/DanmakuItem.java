package cn.liaoweiming.fgame.bean;

import androidx.annotation.Nullable;

public class DanmakuItem {
    String nick;
    String content;
    Long tm;
    Integer type;
    Ext ext;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "DanmakuItem{" +
                "nick='" + nick + '\'' +
                ", content='" + content + '\'' +
                ", tm=" + tm +
                ", type=" + type +
                ", ext=" + ext +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean equal = true;
        if (obj instanceof DanmakuItem){
            DanmakuItem b = (DanmakuItem)  obj;
            equal = (this.tm == b.getTm()) && (this.getNick().equals(b.getNick())) && (this.getContent().equals(b.getContent()));

        }else{
            equal = false;
        }
        return equal;
    }
}
