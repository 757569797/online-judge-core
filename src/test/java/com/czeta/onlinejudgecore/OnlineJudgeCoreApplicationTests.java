package com.czeta.onlinejudgecore;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.machine.impl.QDOJMachineServiceImpl;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineJudgeCoreApplicationTests {

    @Test
    void test() throws Exception{
        QDOJMachineServiceImpl service = new QDOJMachineServiceImpl();
        SubmitMessage message = new SubmitMessage();
        message.setCode("aW1wb3J0IGphdmEudXRpbC4qOwpwdWJsaWMgY2xhc3MgTWFpbnsKCQoJcHVibGljIHN0YXRpYyB2b2lkIG1haW4oU3RyaW5nW10gYXJncykgewoJCQoJCVNjYW5uZXIgcmVhZD0gbmV3IFNjYW5uZXIoU3lzdGVtLmluKTsKCQkKCQl3aGlsZShyZWFkLmhhc05leHQoKSkKCQl7CgkJCWludCBhID0gcmVhZC5uZXh0SW50KCk7CgkJCWludCBiID0gcmVhZC5uZXh0SW50KCk7CgkJCVN5c3RlbS5vdXQucHJpbnRsbihhICsgYik7CgkJfQoJfQoJCn0===");
        message.setLanguage("Java");
        message.setProblemId(6666l);
        message.setTimeLimit(1000);
        message.setMemoryLimit(256);
        message.setJudgeUrl("http://39.97.183.91:8080");
        message.setVisitToken("93cf7ef4deb5e55d16f8a0f7ed9e18065800c0153dbc33402bc4820dbe1238c3");
        message.setSpj(1);
        message.setSpjCode("I2luY2x1ZGUgPHN0ZGlvLmg+CgojZGVmaW5lIEFDIDAKI2RlZmluZSBXQSAxCiNkZWZpbmUgRVJST1IgLTEKCmludCBzcGooRklMRSAqaW5wdXQsIEZJTEUgKnVzZXJfb3V0cHV0KTsKCnZvaWQgY2xvc2VfZmlsZShGSUxFICpmKXsKICAgIGlmKGYgIT0gTlVMTCl7CiAgICAgICAgZmNsb3NlKGYpOwogICAgfQp9CgppbnQgbWFpbihpbnQgYXJnYywgY2hhciAqYXJnc1tdKXsKICAgIEZJTEUgKmlucHV0ID0gTlVMTCwgKnVzZXJfb3V0cHV0ID0gTlVMTDsKICAgIGludCByZXN1bHQ7CiAgICBpZihhcmdjICE9IDMpewogICAgICAgIHByaW50ZigiVXNhZ2U6IHNwaiB4LmluIHgub3V0XG4iKTsKICAgICAgICByZXR1cm4gRVJST1I7CiAgICB9CiAgICBpbnB1dCA9IGZvcGVuKGFyZ3NbMV0sICJyIik7CiAgICB1c2VyX291dHB1dCA9IGZvcGVuKGFyZ3NbMl0sICJyIik7CiAgICBpZihpbnB1dCA9PSBOVUxMIHx8IHVzZXJfb3V0cHV0ID09IE5VTEwpewogICAgICAgIHByaW50ZigiRmFpbGVkIHRvIG9wZW4gb3V0cHV0IGZpbGVcbiIpOwogICAgICAgIGNsb3NlX2ZpbGUoaW5wdXQpOwogICAgICAgIGNsb3NlX2ZpbGUodXNlcl9vdXRwdXQpOwogICAgICAgIHJldHVybiBFUlJPUjsKICAgIH0KCiAgICByZXN1bHQgPSBzcGooaW5wdXQsIHVzZXJfb3V0cHV0KTsKICAgIHByaW50ZigicmVzdWx0OiAlZFxuIiwgcmVzdWx0KTsKCiAgICBjbG9zZV9maWxlKGlucHV0KTsKICAgIGNsb3NlX2ZpbGUodXNlcl9vdXRwdXQpOwogICAgcmV0dXJuIHJlc3VsdDsKfQoKaW50IHNwaihGSUxFICppbnB1dCwgRklMRSAqdXNlcl9vdXRwdXQpewogICAgICBpbnQgYSwgYjsKICAgICAgd2hpbGUoZnNjYW5mKGlucHV0LCAiJWQgJWQiLCAmYSwgJmIpICE9IEVPRil7CiAgICAgICAgICBpZihhIC1iICE9IDMpewogICAgICAgICAgICAgIHJldHVybiBXQTsKICAgICAgICAgIH0KICAgICAgfQogICAgICByZXR1cm4gQUM7Cn0=");
        message.setSpjLanguage("C");
        message.setSpjVersion(DigestUtils.md5Hex(message.getSpjLanguage() + message.getSpjCode()));
        System.out.println(JSONObject.toJSONString(service.machineImplMethod(message)));
    }
}
