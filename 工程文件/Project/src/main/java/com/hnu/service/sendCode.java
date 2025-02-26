package com.hnu.service;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hnu.util.VCodeUtil;
import com.hnu.util.Email;
import com.zhenzi.sms.ZhenziSmsClient;

public class sendCode {
    private String rans=VCodeUtil.verifyCode(6);

    public String getRans() {
        return rans;
    }

    /**
     * 邮箱格式是否合法
     * @param email
     * @return
     */
    public Boolean checkEmail(String email){
        Pattern p;
        Matcher m;
        p = Pattern.compile("^[A-Za-z0-9_-\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        m = p.matcher(email);
        return m.find();
    }
    /**
     * 邮箱格式是否合法
     * @param phone
     * @return
     */
    public Boolean checkPhone(String phone){
        Pattern p;
        Matcher m;
        p=Pattern.compile("^1[3,5,7,8,9][0-9]{9}$");
        m=p.matcher(String.valueOf(phone));
        return m.find();
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    public String SendByEmail(String email){
        Email.postMessage("test", rans, email);
        return rans;
    }
    public String SendByPHone(String phone) throws Exception {
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","114081","df5fe3f2-f82c-44a1-a23a-10d1f47c7b03");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number", phone);
        params.put("templateId", "13169");
        String[] templateParams = new String[2];
        templateParams[0] = rans;
        templateParams[1] = "5分钟";
        params.put("templateParams", templateParams);
        String result = client.send(params);
        return result;
    }
}
