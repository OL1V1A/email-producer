package com.lwj.emailserverproducer.controller;

import com.alibaba.fastjson.JSONObject;
import com.lwj.common.model.MailMessageModel;
import com.lwj.common.utils.HessianSerialization;
import com.lwj.common.utils.KryoSerializer;
import com.lwj.emailserverproducer.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @Auth: lwj
 * @Date: 2019/6/27 9:33
 */
@RestController
@RequestMapping(value = "/v1/emails")
public class EmailController {

    @Resource
    private EmailService emailService;

    @Resource
    private KryoSerializer kryo;

    @Resource
    private HessianSerialization hessian;

    @Value("${mail.username}")
    private String mailUsername;

    @PostMapping
    public JSONObject add(@RequestBody JSONObject jsonObject) throws Exception {
        emailService.sendEmail(jsonObject.toJSONString());
        return jsonObject;
    }

    @PostMapping("/send")
    public String send(@RequestBody Map map, MultipartFile[] files) throws Exception {
        MailMessageModel model = new MailMessageModel();
        model.setFrom(mailUsername);
        model.setTo(String.valueOf(map.get("to")));
        model.setSubject(String.valueOf(map.get("subject")));
        model.setText(String.valueOf(map.get("text")));
        model.setFiles(new File[]{new File("C:\\Users\\Lenovo\\Pictures\\Saved Pictures\\1234.jpg")
        ,new File("C:\\Users\\Lenovo\\Pictures\\Saved Pictures\\1234.jpg")});
//        byte[] bytes = kryo.serialize(model);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream o = new ObjectOutputStream(out);
//        o.writeObject(model);

        emailService.sendEmail(hessian.serialize(model));
        return "success";
    }
}
