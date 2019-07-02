package com.zanebono.chart.ServiceImpl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.zanebono.chart.Listener.PutObjectProgressListener;
import com.zanebono.chart.Service.FileService;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String fileUpload(MultipartFile file) {
        if(file.isEmpty()){
            return "false";
        }
        String name = file.getOriginalFilename();
        int size = (int)file.getSize();
        System.out.println(name + "-->" + size);

        String path = "D:/spring_boot_beifen/file_test";
        File dest = new File(path+"/"+name);
        //父目录判断
        if(!dest.getParentFile().exists())
            dest.getParentFile().mkdir();
        try {
            file.transferTo(dest);
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
    }

    @Override
    public String multifileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");
        if(files.isEmpty())
            return "false";
        String path = "D:/spring_boot_beifen/file_test" ;
        for(MultipartFile file:files){
            String name = file.getOriginalFilename();
            int size = (int)file.getSize();
            System.out.println(name + "-->" + size);
            if (file.isEmpty()){
                return "false";
            }else{
                File dest = new File(path+"/"+name);
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "true";
    }

    @Override
    public String download(HttpServletResponse response, String name) {
        return null;
    }

    @Override
    public String multifileOssUpload(HttpServletRequest request) {
        // 日志配置，OSS Java SDK使用log4j记录错误信息。示例程序会在工程目录下生成“oss-demo.log”日志文件，默认日志级别是INFO。
        // 日志的配置文件是“conf/log4j.properties”，如果您不需要日志，可以没有日志配置文件和下面的日志配置。
        PropertyConfigurator.configure("conf/log4j.properties");
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAIE3EL7nf9dL3n";
        String  accessKeySecret = "sRsI5JtFZIhecH5RqLAOygZvNRGPsD";
        String bucketName = "zanebono";
        String path = "D:/spring_boot_beifen/file_test" ;
        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");
        if(files.isEmpty())
            return "false";
        for(MultipartFile file:files){
            String name = file.getOriginalFilename();
            int size = (int)file.getSize();
            System.out.println(name + "-->" + size);
            if (file.isEmpty()){
                return "false";
            }else{
                File dest = new File(path+"/"+name);
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "false";
                }
//                ossClient.putObject(new PutObjectRequest(bucketName, name,dest)).
//                        <PutObjectRequest>withProgressListener(new PutObjectProgressListener());
                try {
                    ossClient.putObject(new PutObjectRequest(bucketName, name, new FileInputStream(path+"/"+name)).
                            <PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Object：" + name + "存入OSS成功。");
                dest.delete();
            }
        }
        ossClient.shutdown();
        return "true";
    }
}
