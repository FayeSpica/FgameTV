package cn.liaoweiming.fgame.bean.fgame;

public class RetBody {
    DataInner data;
    String message;
    Integer result;

    public DataInner getData() {
        return data;
    }

    public void setData(DataInner data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RetBody{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
