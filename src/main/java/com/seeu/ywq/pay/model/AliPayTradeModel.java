package com.seeu.ywq.pay.model;

import com.seeu.ywq.pay.model.TradeModel;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝回调参数
 */
@Entity
@Table(name = "ywq_pay_trade_ali")
public class AliPayTradeModel {


    @Id
    @Length(max = 64)
    @Column(length = 20)
    private String out_trade_no;    // 商户订单号

    private Date notify_time;   // 通知的发送时间。格式为yyyy-MM-dd HH:mm:ss
    @Length(max = 64)
    private String notify_type; // 通知的类型
    @Length(max = 128)
    private String notify_id;   // 通知校验ID
    @Length(max = 32)
    private String app_id;       // 支付宝分配给开发者的应用Id
    @Length(max = 10)
    private String charset;      // 编码格式，如utf-8、gbk、gb2312等
    @Length(max = 3)
    private String version;      // 调用的接口版本，固定为：1.0
    @Length(max = 10)
    private String sign_type;    // 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
    @Length(max = 256)
    private String sign;          // 签名，请参考异步返回结果的验签
    @Length(max = 64)
    private String trade_no;      // 支付宝交易号（支付宝交易凭证号，2013112011001004330000121536）

    @Length(max = 64)
    private String out_biz_no;      // 商户业务号，商户业务ID，主要是退款通知中返回退款申请的流水号，HZRF001
    @Length(max = 16)
    private String buyer_id;        // 买家支付宝用户号，买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字，2088102122524333
    @Length(max = 100)
    private String buyer_logon_id;  // 买家支付宝账号，15901825620
    @Length(max = 30)
    private String seller_email;    // 卖家支付宝账号，zhuzhanghu@alitest.com
    //    @Length(max = 32)
    private TradeModel.TRADE_STATUS trade_status;    // 交易状态，TRADE_CLOSED
    // (9,2)
    private BigDecimal total_amount;    // 订单金额，本次交易支付的订单金额，单位为人民币（元）
    private BigDecimal receipt_amount;  // 实收金额，商家在交易中实际收到的款项，单位为元
    private BigDecimal invoice_amount;  // 开票金额，用户在交易中支付的可开发票的金额
    private BigDecimal buyer_pay_amount;// 付款金额，用户在交易中支付的金额
    private BigDecimal point_amount;    // 集分宝金额，使用集分宝支付的金额
    private BigDecimal refund_fee;      // 总退款金额，退款通知中，返回总退款金额，单位为元，支持两位小数
    @Length(max = 256)
    private String subject;             // 订单标题，商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来
    @Length(max = 400)
    private String body;                // 商品描述，该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来

    private Date gmt_create;           // 交易创建时间，该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss | 2015-04-27 15:45:57
    private Date gmt_payment;          // 交易付款时间，该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss | 2015-04-27 15:45:57
    private Date gmt_refund;           // 交易退款时间，该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S | 2015-04-28 15:45:57.320
    private Date gmt_close;            // 交易结束时间，该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss | 2015-04-29 15:45:57
    @Length(max = 512)
    private String fund_bill_list;     // 支付金额信息，支付成功的各个渠道金额信息。[{“amount”:“15.00”,“fundChannel”:“ALIPAYACCOUNT”}]
    @Length(max = 512)
    private String passback_params;    // 回传参数，公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝。merchantBizType%3d3C%26merchantBizNo%3d2016010101111
    //    @Length(max = )
    private String voucher_detail_list;// 优惠券信息，本交易支付时所使用的所有优惠券信息。[{“amount”:“0.20”,“merchantContribute”:“0.00”,“name”:“一键创建券模板的券名称”,“otherContribute”:“0.20”,“type”:“ALIPAY_DISCOUNT_VOUCHER”,“memo”:“学生卡8折优惠”]

    //////////////// GETer / SETer //////////////////


    public Date getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(Date notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public TradeModel.TRADE_STATUS getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(TradeModel.TRADE_STATUS trade_status) {
        this.trade_status = trade_status;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(BigDecimal receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public BigDecimal getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(BigDecimal invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public BigDecimal getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(BigDecimal buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public BigDecimal getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(BigDecimal point_amount) {
        this.point_amount = point_amount;
    }

    public BigDecimal getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(BigDecimal refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    public Date getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(Date gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public Date getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(Date gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    public Date getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(Date gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getFund_bill_list() {
        return fund_bill_list;
    }

    public void setFund_bill_list(String fund_bill_list) {
        this.fund_bill_list = fund_bill_list;
    }

    public String getPassback_params() {
        return passback_params;
    }

    public void setPassback_params(String passback_params) {
        this.passback_params = passback_params;
    }

    public String getVoucher_detail_list() {
        return voucher_detail_list;
    }

    public void setVoucher_detail_list(String voucher_detail_list) {
        this.voucher_detail_list = voucher_detail_list;
    }
}
