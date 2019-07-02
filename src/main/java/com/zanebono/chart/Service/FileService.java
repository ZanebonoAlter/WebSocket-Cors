package com.zanebono.chart.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
    public String fileUpload(MultipartFile File);
    public String multifileUpload(HttpServletRequest request);
    public String download(HttpServletResponse response,String name);
    public String multifileOssUpload(HttpServletRequest request);
}
