package software.guozw.service;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


@Service
public class ImgService {
    @Value("${spring.oss.endpoint}")
    private String endpoint; // OSS访问域名

    @Value("${spring.oss.access-key-id}")
    private String accessKeyId; // 阿里云账号AccessKey ID

    @Value("${spring.oss.access-key-secret}")
    private String accessKeySecret; // 阿里云账号AccessKey Secret

    @Value("${spring.oss.bucket-name}")
    private String bucketName; // Bucket 名称

    private static String localPath = "D:/Java/soa/experiment/SpringCloudofGuoZW/assets/";

    // 用户没传图片名字给提示
    public String notice() {
        return "please input the img name, like: http://localhost:8000/img/test.png";
    }

    // 接收图片并保存图片
    public ResponseData receiveImg(String imgName,String type) throws IOException {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置超时时间：10s
        Date expiration = new Date(System.currentTimeMillis() + 10000);
        HttpMethod httpMethod = HttpMethod.GET;

        // 生成URL地址
        // https://garril-upload.oss-cn-shenzhen.aliyuncs.com/imgs/test.png?Expires=xxxxx&OSSAccessKeyId=LTAI5t89xxxxx&Signature=m55yM9qxxxxM%3D
        URL ossUrl = ossClient.generatePresignedUrl(bucketName, imgName, expiration, httpMethod);
        System.out.println("ossUrl: "+ossUrl);
        // 拿到 https://garril-upload.oss-cn-shenzhen.aliyuncs.com/imgs/test.png
        String imgUrl = ossUrl.getProtocol() + "://" + ossUrl.getHost() + ossUrl.getPath();
        System.out.println("imgUrl: "+imgUrl);
        // 拿到参数
        String expires = "";
        String accessKeyId = "";
        String signature = "";

        // 获取参数部分并按照 & 字符进行分割
        String query = ossUrl.getQuery();
        String[] queryParams = query.split("&");
        // 遍历每个参数并判断是否匹配目标参数
        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if ("Expires".equals(keyValue[0])) {
                expires = keyValue[1];
            } else if ("OSSAccessKeyId".equals(keyValue[0])) {
                accessKeyId = keyValue[1];
            } else if ("Signature".equals(keyValue[0])) {
                signature = keyValue[1];
            }
        }
        String assetsPath = new File("").getAbsolutePath() + "/assets/";
        // D:\Java\soa\experiment\SpringCloudofGuoZW\assets\ + test.png
        String imgLocalPath = (assetsPath + imgName).replace("\\", "/");
        System.out.println(imgLocalPath);
        // 获取 图片对应的 BufferedImage 对象
//        URL url = new URL(imgUrl);
//        BufferedImage bufferedImage = ImageIO.read(url);
        // 导致json过长，不值得，直接给imgUrl，让他直接获取

        UrlObj urlObj = new UrlObj(imgUrl,imgLocalPath,expires,accessKeyId,signature,null,type);
        ResponseData res;
        if(urlObj != null) {
            res = new ResponseData("200",urlObj);
        } else {
            res = new ResponseData("404",null);
        }
        // 关闭OSSClient实例
        ossClient.shutdown();

        // 保存图片到本地
        saveImgToLocal(imgUrl,imgLocalPath);
        // 保存到前端路径
        String outputPath = "D:\\FrontEnd\\project\\upload_img_visual-main\\public\\test.png";
        saveImgToLocal(imgUrl,outputPath);
        return res;
    }

    // 保存图片到本地 -- 测试用
    public void saveImgToLocal(String imageUrl,String imgLocalPath) throws IOException {

        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        FileOutputStream fos = new FileOutputStream(imgLocalPath);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        inputStream.close();
        fos.close();


    }
}
