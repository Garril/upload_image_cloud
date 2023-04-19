package software.guozw.pojo;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class UrlObj {
    private String imgUrl; // 图片url
    private String localPath; // 保存在本地的地址
    private String newLocalPath; // 新图片本地地址
    private String expires;
    private String accessKeyId;
    private String signature;
    private BufferedImage bufferedImage;
    private String type;

    public UrlObj(String imgUrl,String localPath,String expires, String accessKeyId, String signature,BufferedImage bufferedImage,String type) {
        this.imgUrl = imgUrl;
        this.localPath = localPath;
        this.expires = expires;
        this.accessKeyId = accessKeyId;
        this.signature = signature;
        this.bufferedImage = bufferedImage;
        this.type = type;
        this.newLocalPath = "";
    }
}