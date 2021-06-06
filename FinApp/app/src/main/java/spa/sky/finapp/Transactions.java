package spa.sky.finapp;

import java.text.DateFormat;

public class Transactions {
    String tag;
    int uid, exin;
    DateFormat Date;
    long amount;
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getExin() {
        return exin;
    }

    public void setExin(int exin) {
        this.exin = exin;
    }


}
