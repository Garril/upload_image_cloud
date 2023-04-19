package software.guozw.pojo;

import lombok.Data;

@Data
public class ResponseData {
    private String code;
    private UrlObj data;

    public ResponseData(String code, UrlObj data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public UrlObj getData() {
        return data;
    }
}
