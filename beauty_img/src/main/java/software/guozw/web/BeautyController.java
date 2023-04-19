package software.guozw.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;
import software.guozw.service.BeautyService;

import java.io.IOException;


@RestController
@RequestMapping("beauty")
public class BeautyController {

   @Autowired
   private BeautyService beautyService;

    @PostMapping("")
    public ResponseData getImageUrl(@RequestBody ResponseData responseData) throws IOException {
        if(responseData.getCode().equals("200")) {
            UrlObj data = responseData.getData();
            if(data != null) {
                return beautyService.dealImg(data);
            }
        }
        return null;
    }
}
