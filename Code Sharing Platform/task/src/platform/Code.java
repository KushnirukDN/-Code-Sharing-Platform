package platform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Code {

    String code = "";
    String date = null;

    public Code() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDateOfUpdate() {
        this.date = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return "Code{" +
                "code='" + code + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
