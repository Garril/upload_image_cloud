package software.guozw.web;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;
import software.guozw.service.ImgService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


@RestController
@RequestMapping("img")
public class ImgController {

   @Autowired
   private ImgService imgService;

    @GetMapping("")
    public String queryOrderByUserId() {
        // 根据id查询订单并返回
        return imgService.notice();
    }
    
    @GetMapping("/{type}/{name}")
    public ResponseData getImageUrl(@PathVariable("name") String imgName, @PathVariable("type") String type) throws IOException {
        // 根据路径拼接拿url，保存图片到本地，并返回url
        return imgService.receiveImg(imgName,type);
    }
}
