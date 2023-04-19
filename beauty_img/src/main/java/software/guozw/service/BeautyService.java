package software.guozw.service;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.opencv.imgcodecs.Imgcodecs;

@Service
public class BeautyService {

    public static ResponseData dealImg(UrlObj urlObj) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 定义输入和输出文件路径
        String inputPath = urlObj.getLocalPath();
        String outputPath = "";

        // 读取输入原始图像
        Mat origin_image = Imgcodecs.imread(inputPath);
        if(origin_image == null) {
            saveImgToLocal(urlObj.getImgUrl(),inputPath);
            origin_image = Imgcodecs.imread(inputPath);
        }
        // 1.图片平滑处理 --- 结果：test_beauty1.png
        Mat dst1_image = new Mat();
        // 应用高斯模糊。
        Imgproc.GaussianBlur(origin_image,dst1_image,new Size(1,1),2);
        outputPath = getOutPutImagePath("test_beauty1.png");
        Imgcodecs.imwrite(outputPath, dst1_image);

        // 2.颜色增益处理 --- 结果：test_beauty2.png
        // 设置红、绿、蓝三个通道的增益值，这里分别设置为1.2，0.8，1.1。
        double rGain = 1.2;
        double gGain = 1.2;
        double bGain = 1.2;
        Scalar gain = new Scalar(bGain, gGain, rGain);
        Mat dst2_image = new Mat();
        // 应用颜色增益算法。
        Core.multiply(dst1_image, gain, dst2_image);
        outputPath = getOutPutImagePath("test_beauty2.png");
        Imgcodecs.imwrite(outputPath, dst2_image);
        // 保存给前端用
        outputPath = "D:\\FrontEnd\\project\\upload_img_visual-main\\public\\test_beauty.png";
        Imgcodecs.imwrite(outputPath, dst2_image);

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
    // 获取将要保存到本地的图片的路径
    public static String getOutPutImagePath(String outputImgName) {
        String filePath = new File("").getAbsolutePath();
        // outputImgName = "test_fix.png";
        String outputPath = (filePath + "\\assets\\" + outputImgName).replace("\\", "/");;
        return outputPath;
    }

}
