package software.guozw.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;
import software.guozw.service.GrayService;

import java.io.IOException;


@RestController
@RequestMapping("gray")
public class GrayController {

   @Autowired
   private GrayService grayService;

    @PostMapping("")
    public ResponseData getImageUrl(@RequestBody ResponseData responseData) throws IOException {
        if(responseData.getCode().equals("200")) {
            UrlObj data = responseData.getData();
            if(data != null) {
                return grayService.dealImg(data);
            }
        }
        return null;
    }
}
