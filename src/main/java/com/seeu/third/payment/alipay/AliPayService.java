package com.seeu.third.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.pay.model.AliPayTradeModel;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.service.AliPayTradeService;
import com.seeu.ywq.pay.service.OrderService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AliPayService {

    @Value("${alipay.appId}")
    private String APP_ID;
    @Value("${alipay.privateKey}")
    private String APP_PRIVATE_KEY;
    @Value("${alipay.publicKey}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${alipay.timeoutExpress}")
    private String TIMEOUT_EXPRESS;
    @Value("${alipay.notifyUrl}")
    private String NOTIFY_URL;

    @Autowired
    private OrderService orderService;
    @Autowired
    private AliPayTradeService aliPayTradeService;

    /**
     * 返回支付宝订单ID
     *
     * @param oid
     * @param price
     * @param body
     * @param subject
     * @return
     */
    public String createOrder(String oid, BigDecimal price, String subject, String body) throws ActionParameterException {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                APP_ID,
                APP_PRIVATE_KEY,
                "json",
                "utf-8",
                ALIPAY_PUBLIC_KEY,
                "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(oid);
        model.setTimeoutExpress(TIMEOUT_EXPRESS);
//        model.setTotalAmount("" + price.doubleValue());
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(NOTIFY_URL);

        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String callBack(AliPayTradeModel aliPayTradeModel) throws AlipayApiException {
        //将异步通知中收到的所有参数都存放到map中
        JSONObject json = JSONObject.fromObject(aliPayTradeModel);
        boolean signVerified = AlipaySignature.rsaCheckV1(json, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");          //调用SDK验证签名
        if (signVerified) {
            //验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            String out_trade_no = aliPayTradeModel.getOut_trade_no();
            // 查询该订单是否已经完成交易，若否，则继续
            TradeModel.TRADE_STATUS trade_status = aliPayTradeModel.getTrade_status();
//            logger.info(out_trade_no1 + ":" + trade_status1);
            // 修改订单状态，判断是否完成交易
            switch (trade_status) {
                case WAIT_BUYER_PAY:// 交易创建，等待买家付款（该通知不可能拿到，支付宝默认不开启该通知）
                    aliPayTradeService.save(aliPayTradeModel);
                    break;
                case TRADE_CLOSED:// 未付款交易超时关闭，或支付完成后全额退款
                    aliPayTradeService.save(aliPayTradeModel);
                    orderService.failOrder(out_trade_no);
                    break;
                case TRADE_SUCCESS: // 交易支付成功
                case TRADE_FINISHED:// 交易结束，不可退款
                    // 成功！
                    aliPayTradeService.save(aliPayTradeModel);
                    orderService.finishOrder(out_trade_no);
                    break;
            }
            return "success";
        } else {
            //验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }
    }
}
