package com.czeta.onlinejudgecore.spider.poj;

import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudgecore.exception.SpiderRuntimeException;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudgecore.utils.spider.response.SpiderResponse;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * @ClassName POJSpider
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 16:01
 * @Version 1.0
 */
@Data
public class POJResultSpider implements SpiderPageProcessor {
    private String spiderSubmitId = null;

    @Override
    public Object process(SpiderResponse response) {
        Document doc = response.getDocument();
        Elements elements = null;
        String submitId = null;
        String result = null;
        String memory = null;
        String time = null;
        try {
            if (spiderSubmitId == null) { // 正常提交后的界面，只需要获取第一条记录即可
                elements = doc.select("table.a>tbody tr[align=center]>td");
            } else { // 爬取指定提交ID的结果
                elements = doc.select("table.a>tbody tr[align=center]");
                for (Element element : elements) {
                    if (element.text().contains(spiderSubmitId)) {
                        elements = element.children();
                        break;
                    }
                }
            }
            submitId = elements.get(0).text();
            result = elements.get(3).text();
            memory = elements.get(4).text();
            time = elements.get(5).text();
            AssertUtils.notNull(submitId, BaseStatusMsg.APIEnum.FAILED);
            AssertUtils.notNull(result, BaseStatusMsg.APIEnum.FAILED);
            AssertUtils.notNull(memory, BaseStatusMsg.APIEnum.FAILED);
            AssertUtils.notNull(time, BaseStatusMsg.APIEnum.FAILED);
        } catch (Exception ex) {
            // 统一转换成自定义爬虫运行时异常并向上抛出
            throw new SpiderRuntimeException(ex);
        }
        SubmitResultModel submitResultModel = new SubmitResultModel();
        // 如果最终评测状态还未确定，则这里需要做个标记，供上层继续访问最终评测状态直到结果出来。
        if ("Running & Judging".equals(result) || "Compiling".equals(result) || "Waiting".equals(result)) {
            submitResultModel.setPending(true);
            submitResultModel.setSpiderSubmitId(submitId);
            return submitResultModel;
        }
        // 最终评测已经确定，属性转换成本平台通用的
        submitResultModel.setSubmitStatus(result.replaceAll(" ", ""));
        submitResultModel.setMemory(memory);
        submitResultModel.setTime(time);
        submitResultModel.setPending(false);
        return submitResultModel;
    }
}
