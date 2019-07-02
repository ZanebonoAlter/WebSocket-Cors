package com.zanebono.chart.Controller;

import com.zanebono.chart.Service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Api(value="/File",description = "文件上传下载接口")
@Controller
@RequestMapping("/File")
public class FileController {
    @Autowired
    private FileService fileService;
    /**
     * 实现文件上传
     * */
    @ApiOperation(value = "单个文件上传接口",notes = "最大为50MB的单个文件上传")
    @RequestMapping(value="/fileUpload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file){
        return this.fileService.fileUpload(file);
    }
    /**
     * 实现多文件上传
     * */
    @ApiOperation(value = "多个文件上传接口",notes = "最大为50MB的多个文件上传")
    @RequestMapping(value="/multifileUpload",method= RequestMethod.POST)
    /**public @ResponseBody String multifileUpload(@RequestParam("fileName")List<MultipartFile> files) */
    public @ResponseBody String multifileUpload(HttpServletRequest request){
       return this.fileService.multifileUpload(request);
    }
    @ApiOperation(value = "多个文件上传OSS接口",notes = "最大为50MB的多个文件上传OSS")
    @RequestMapping(value="/multifileOSSUpload",method= RequestMethod.POST)
    /**public @ResponseBody String multifileUpload(@RequestParam("fileName")List<MultipartFile> files) */
    public @ResponseBody String multifileOssUpload(HttpServletRequest request){
        return this.fileService.multifileOssUpload(request);
    }
    @RequestMapping("/download")
    public boolean downLoad(HttpServletResponse response) throws UnsupportedEncodingException {
        String filename="\\zip.zip";
        String filePath = "D:\\doc_test\\test2" ;
        File file = new File(filePath + filename);
        if(file.exists()){ //判断文件父目录是否存在
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Methods","POST");
            response.setHeader("Access-Control-Allow-Headers:x-requested-with","content-type");
            response.setContentType("application/octet-stream");
            response.setContentType("application/force-download");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setContentType("application/x-zip-compressed");
            response.setHeader("content-disposition",
                    "attachment;filename=" + new String(file.getName().getBytes(), "ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        downloadFile(file,response,false);

        return true;
    }
    public void downloadFile(File file, HttpServletResponse response, boolean isDelete) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            if(isDelete)
            {
                file.delete();        //是否将生成的服务器端文件删除
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeZip(String path,String fileName,InputStream in) {

        String str = "";

        if (StringUtils.isEmpty(path))

            throw new NullPointerException("file path is null.");

        if (StringUtils.isEmpty(fileName))

            throw new NullPointerException("name is null.");

        String filePath = path + File.separator + fileName;

        DataOutputStream out = null;

        try {

            out = new DataOutputStream(new FileOutputStream(filePath));

            byte[] b = new byte[1024];

            int readCount = in.read(b);

            while (readCount > -1) {

                if (readCount < 1024) {

                    byte[] temp = new byte[readCount];

                    System.arraycopy(b, 0, temp, 0, readCount);

                    out.write(temp);

                } else {

                    out.write(b);

                }

                readCount = in.read(b);

            }

            str = new String(b, "UTF-8");

//            logger.info("ca receive data : " + str);

            out.flush();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (out != null) {

                try {

                    out.close();

                } catch (IOException e) {

                }
            }
        }

    }
}