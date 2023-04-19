package software.guozw.web;

import com.google.gson.Gson;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.guozw.pojo.ResponseData;
import software.guozw.pojo.UrlObj;
import software.guozw.service.BeautyService;

import java.io.IOException;


@Component
public class BeautyImgWorker implements Worker {
    private final String taskDefName = "beautyImg";

    @Autowired
    private BeautyService beautyService;

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }
    @Override
    public TaskResult execute(Task task) {
        System.out.printf("Executing %s%n", taskDefName);
        // 拿到事件流的输入
        String inputJsonStr = (String) task.getInputData().get("inputData");
        Gson gson = new Gson();
        ResponseData inputData = gson.fromJson(inputJsonStr, ResponseData.class);
        System.out.println("inputData-code: "+inputData.getCode());
        try {
            if(inputData.getCode().equals("200")) {
                UrlObj data = inputData.getData();
                if(data != null) {
                    ResponseData responseData = beautyService.dealImg(data);
                    TaskResult result = new TaskResult(task);
                    result.setStatus(TaskResult.Status.COMPLETED);

                    Gson resGson = new Gson();
                    String resJsonStr = resGson.toJson(responseData);
                    // Register the output of the task
                    result.getOutputData().put("responseData", resJsonStr);
                    return result;
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

