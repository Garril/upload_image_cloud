package software.guozw.service;

import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


@Service
public class GrayService {
    public static ResponseData dealImg(UrlObj urlObj) throws IOException {
        // 加载 OpenCV 库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 定义输入和输出文件路径
        String inputPath = urlObj.getLocalPath();
        String filePath = new File("").getAbsolutePath();
        String grayImgName = "test_gray.png";
        String outputPath = (filePath + "\\assets\\" + grayImgName).replace("\\", "/");;

        // 读取输入图像
        Mat image = Imgcodecs.imread(inputPath);
        if(image == null) {
            saveImgToLocal(urlObj.getImgUrl(),inputPath);
            image = Imgcodecs.imread(inputPath);
        }

        // 转化为灰度图像
        Mat grayImage = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // 保存输出图像
        System.out.println("output gray img ing...");
        Imgcodecs.imwrite(outputPath, grayImage);
        System.out.println("output gray img success!");

        // 保存给前端用
        outputPath = "D:\\FrontEnd\\project\\upload_img_visual-main\\public\\test_gray.png";
        Imgcodecs.imwrite(outputPath, grayImage);

        // 设置新图片本地路径
        urlObj.setNewLocalPath(outputPath);
        return new ResponseData("200",urlObj);
    }

    // 保存图片到本地 -- 测试用
    public static void saveImgToLocal(String imageUrl, String imgLocalPath) throws IOException {

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
