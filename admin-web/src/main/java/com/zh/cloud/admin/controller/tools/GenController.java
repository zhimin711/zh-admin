package com.zh.cloud.admin.controller.tools;

import com.alibaba.fastjson.JSONObject;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.tools.utils.ZipUtils;
import com.ch.utils.*;
import com.zh.cloud.admin.pojo.tools.MyBatis3Config;
import com.zh.cloud.admin.utils.MyBatisGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * decs:
 *
 * @author 01370603
 * @date 2021/2/1
 */
@RestController
@RequestMapping("/api/{env}/tools/gen")
@Slf4j
public class GenController {

    @Value("${path.temp}")
    private String tempDir;


    @PostMapping("myBatis3")
    public Result<String> myBatis3(@RequestBody MyBatis3Config config) {
        return ResultUtils.wrapFail(() -> {
            String dir = FileExtUtils.linkPath(DateUtils.formatDate(DateUtils.current()), "myBatis3", config.getUid());
            config.setOutputDir(tempDir + dir);
            Document doc = MyBatisGeneratorUtil.assemble(config);
            Properties properties = MyBatisGeneratorUtil.properties(config);
            File dirFile = new File(tempDir + dir);
            FileExtUtils.create(dirFile);
            MyBatisGeneratorUtil.generate(doc.asXML(), properties);
            ZipUtils.zipFile(tempDir + dir, tempDir + dir + ".zip");
            FileSystemUtils.deleteRecursively(dirFile);
            return dir;
        });
    }


    @GetMapping("download")
    public void download(HttpServletResponse response,
                         @RequestParam String filePath, @RequestParam(required = false) String fileName) {
//    @PostMapping("download")
//    public void download(HttpServletResponse response,
//                         @RequestBody JSONObject data) {
//        String filePath = data.getString("filePath");
//        String fileName = data.getString("fileName");
        if (CommonUtils.isEmpty(filePath)) {
            log.error("file url is empty not allow download! {}", filePath);
            return;
        }
        String realPath = tempDir + filePath + ".zip";

        String tmpName = CommonUtils.isEmpty(fileName) ? realPath.substring(realPath.lastIndexOf(File.separator) + 1) : fileName;
        tmpName = new String(tmpName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + tmpName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            OutputStream os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(realPath)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            log.error("read file failed!", e);
        } finally {
            IOUtils.close(bis);
        }
    }
}