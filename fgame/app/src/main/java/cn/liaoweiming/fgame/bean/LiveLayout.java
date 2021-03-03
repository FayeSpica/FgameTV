package cn.liaoweiming.fgame.bean;

public class LiveLayout {
    String id;
    String title;

    public LiveLayout() {
    }

    public LiveLayout(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LiveLayout{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
