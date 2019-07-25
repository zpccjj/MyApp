package app.bean;

public class HttpData {
    private boolean succ;
    private String fun;
    private String msg;
    private String err;

    public HttpData(){}

    public HttpData(boolean s, String f, String m, String e){
        this.succ = s;
        this.fun = f;
        this.msg = m;
        this.err = e;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
