package com.liziyuan.hope.file.controller;

import com.liziyuan.hope.file.core.model.ResourceDownloadParameters;
import com.liziyuan.hope.file.service.FileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 文件处理controller
 *
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 23:37
 */
@Slf4j
@RestController
public class FileProcessController {
    @Resource
    FileProcessService fileProcessService;

    @PostMapping("/{domainCode}/download-resource")
    public ResponseEntity<Void> downloadResourceMetaData(@PathVariable("domainCode") String domainCode, @Valid @RequestBody(required = false) ResourceDownloadParameters resourceDownloadParameters) {
        fileProcessService.downloadResourceMetaData(domainCode, resourceDownloadParameters);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{domainCode}/upload-task")
    public ResponseEntity<Void> uploadVerifyRuleTaskFile(@PathVariable("domainCode") String domainCode, @Valid @RequestPart("file") MultipartFile file) {
        fileProcessService.uploadVerifyRuleTaskFile(domainCode, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
