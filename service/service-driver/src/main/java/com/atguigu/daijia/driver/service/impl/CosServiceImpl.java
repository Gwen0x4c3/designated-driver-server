package com.atguigu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.daijia.driver.config.TencentCloudProperties;
import com.atguigu.daijia.driver.service.CosService;
import com.atguigu.daijia.model.vo.driver.CosUploadVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CosServiceImpl implements CosService {

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    private COSClient getCosClient() {
        COSCredentials cosCredentials = new BasicCOSCredentials(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());
        ClientConfig config = new ClientConfig(new Region(tencentCloudProperties.getRegion()));
        config.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cosCredentials, config);
    }

    @SneakyThrows
    @Override
    public CosUploadVo upload(MultipartFile file, String path) {
        COSClient cosClient = this.getCosClient();
        // 元信息
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentEncoding("UTF-8");
        metadata.setContentType(file.getContentType());
        // 向桶中保存数据
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uploadPath = "/driver/" + path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;
        PutObjectRequest request = new PutObjectRequest(tencentCloudProperties.getBucketPrivate(), uploadPath, file.getInputStream(), metadata);
        request.setStorageClass(StorageClass.Standard);
        PutObjectResult result = cosClient.putObject(request);
        log.info(JSON.toJSONString(result));
        cosClient.shutdown();

        //封装返回对象
        CosUploadVo cosUploadVo = new CosUploadVo();
        cosUploadVo.setUrl(uploadPath);
        //图片临时访问url，回显使用
        cosUploadVo.setShowUrl(this.getImageUrl(uploadPath));
        return cosUploadVo;
    }

    @Override
    public String getImageUrl(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        COSClient cosClient = this.getCosClient();
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(tencentCloudProperties.getBucketPrivate(), path);
        Date expiration = new DateTime().plusMinutes(15).toDate();
        request.setExpiration(expiration);
        URL url = cosClient.generatePresignedUrl(request);
        cosClient.shutdown();
        return url.toString();
    }
}
