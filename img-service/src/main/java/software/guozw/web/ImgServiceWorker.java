package software.guozw.web;

import com.google.gson.Gson;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.guozw.pojo.ResponseData;
import software.guozw.service.ImgService;

import java.io.IOException;



@Component
public class ImgServiceWorker implements Worker {
    private final String taskDefName = "imgService";

    @Autowired
    private ImgService imgService;

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }
    @Override
    public TaskResult execute(Task task) {
        System.out.printf("Executing %s%n", taskDefName);
        // 拿到事件流的输入
        String name = (String) task.getInputData().get("name");
        String type = (String) task.getInputData().get("type");
        System.out.println("name: "+name);
        System.out.println("type: "+type);
        try {
            // 拿到图片对应的信息
            ResponseData responseData = imgService.receiveImg(name, type);
            // 把信息对象转化为json字符串
            Gson gson = new Gson();
            String jsonStr = gson.toJson(responseData);

            TaskResult result = new TaskResult(task);
            result.setStatus(TaskResult.Status.COMPLETED);
            // 当前事件的输出
            result.getOutputData().put("responseData", jsonStr);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

