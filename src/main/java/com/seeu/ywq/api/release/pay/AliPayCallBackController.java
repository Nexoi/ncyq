//package com.seeu.ywq.api.release.pay;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.internal.util.AlipaySignature;
//import io.swagger.annotations.Api;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * Created by suneo.
// * User: neo
// * Date: 26/01/2018
// * Time: 2:42 PM
// * Describe:
// */
//
//@Api(tags = "第三方支付回调接口", description = "不需要操作")
//@RestController
//public class AliPayCallBackController {
//
//    /**
//     * 支付宝支付成功后.通知页面
//     *@author Zhao
//     *@date 2017年11月2日
//     *@param request
//     *@return
//     *@throws UnsupportedEncodingException
//     */
//    @RequestMapping(value="api/alipay/return_url",method={RequestMethod.POST,RequestMethod.GET})
//    @ResponseBody
//    public Model returnUrl(@RequestParam("id") String id, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
//        System.err.println("。。。。。。 同步通知 。。。。。。");
//        System.err.println("。。。。。。 同步通知 。。。。。。");
//        System.err.println("。。。。。。 同步通知 。。。。。。");
//        Map returnMap = new HashMap();
//        try {
//
//            Trade trade = tradeService.selectByOrderNumber(id);
//            // 返回值Map
//            if(trade !=null && trade.getTradeStatus() == 2){
//                User user = userService.selectByPrimaryKey(trade.gettUserId());
//                returnMap.put("tradeType", trade.getTradeType());             //支付方式
//                returnMap.put("phoneNum", user.getPhoneNumber());             //支付帐号
//                returnMap.put("tradeMoney", trade.getTradeMoney()+"");        //订单金额
//
//            }else{
//                model.addAttribute("msg", "查询失败");
//                model.addAttribute("status", 0);
//            }
//            model.addAttribute("returnMap", returnMap);
//            System.err.println(returnMap);
//            model.addAttribute("msg", "查询成功");
//            model.addAttribute("status", 0);
//        } catch (Exception e) {
//            model.addAttribute("msg", "查询失败");
//            model.addAttribute("status", 1);
//        }
//
//        return model;
//    }
//
//    /**
//     * 支付宝支付成功后.回调该接口
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value="api/alipay/notify_url",method={RequestMethod.POST,RequestMethod.GET})
//    @ResponseBody
//    public String notify(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = request.getParameterMap();
//        Trade trade =null;
//        //1.从支付宝回调的request域中取值
//        Map<String, String[]> requestParams = request.getParameterMap();
//
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//            String name = iter.next();
//            String[] values = requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            params.put(name, valueStr);
//        }
//        //2.封装必须参数
//        String out_trade_no = request.getParameter("out_trade_no");            // 商户订单号
//        String orderType = request.getParameter("body");                    // 订单内容
//        String tradeStatus = request.getParameter("trade_status");            //交易状态
//
//        //3.签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
//        boolean signVerified = false;
//        try {
//            //3.1调用SDK验证签名
//            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);35     } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        //4.对验签进行处理
//        if (signVerified) {    //验签通过
//            if(tradeStatus.equals("TRADE_SUCCESS")) {    //只处理支付成功的订单: 修改交易表状态,支付成功
//                Trade trade = tradeService.selectByOrderNumber(out_trade_no);
//                trade.setTradeStatus((byte)3);            //支付完成
//                int returnResult = tradeService.updateByPrimaryKeySelective(trade);    //更新交易表中状态
//                if(returnResult>0){
//                    return "success";
//                }else{
//                    return "fail";
//                }
//            }else{
//                return "fail";
//            }
//        } else {  //验签不通过
//            System.err.println("验签失败");
//            return "fail";
//        }
//    }
//}
