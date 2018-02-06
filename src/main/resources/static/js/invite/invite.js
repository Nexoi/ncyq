var gaincode = $("#spn");
var ock = $("#button_code");
var InterValObj; //timer变量，控制时间
var count = 60; //间隔函数，1秒执行
var curCount;//当前剩余秒数

function validatemobile(mobile) {

    if (mobile == undefined) {
        layer.alert('请输入手机号码！');
        return false;
    }
    var myreg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
    if (!myreg.test(mobile)) {
        layer.alert('请输入有效的手机号码！');
        return false;
    }
    return true;
}

function validatePsw(psw) {

    if (psw.length < 8) {
        layer.alert("密码至少八位！");
        return false;
    }
    if (!/^[a-z0-9]{8,}$/i.test(psw) && /^.*[^\d].*$/.test(psw)) {
        layer.alert("密码必须由字母和数字组成！");
        return false;
    }
    return true;
}

//获取验证码
function sendMessage() {

    var register = "register";
    var phone = $("#user_no").val();

    if (!validatemobile(phone)) {
        return;
    }
    //
    curCount = count;
    console.log(curCount);
    //设置button效果，开始计时
    ock.attr("disabled", true);
    gaincode.html("");
    gaincode.html(curCount + "s");
    InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
    //
    $.ajax({
        type: "POST",
        url: "/api/v1/signup/sendcode/" + phone,
        dataType: "json",
        statusCode: {
            200: function () {
                alert("发送成功！");
            },

            400: function () {
                alert("发送失败,重新发送！");
            },

            404: function () {
                alert("页面加载失败！");
            }
        },
        success: function (data) {

        },
        error: function (err) {
            alert("发送失败！");
        }
    });
}

//timer处理函数
function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        ock.removeAttr("disabled");//启用按钮
        gaincode.html("获取验证码");
    } else {
        curCount--;
        gaincode.html(curCount + "s");
    }
}

//注册
function userRegister() {

    var postData = {};
    var userNo = $("#user_no").val();
    var loginPsw = $("#user_psw").val();
    var vcNum = $("#vc_num").val();

    if (!validatemobile(userNo)) {
        return;
    }
    if (!validatePsw(loginPsw)) {
        return;
    }
    var inviteUid = $("#invitedUid").val();
    if (inviteUid == undefined || inviteUid == 0 || inviteUid == "0") {
        postData = {
            username: "x",
            phone: userNo,
            password: loginPsw,
            code: vcNum
        }
    } else {
        postData = {
            username: "x",
            phone: userNo,
            password: loginPsw,
            code: vcNum,
            inviteUid: inviteUid
        }
    }

    $.ajax({
        type: "POST",
        url: "/api/v1/signup",
        dataType: "json",
        data: postData,
        statusCode: {
            201: function () {
                alert("注册成功！");
                // qrcode???
                $("div.invite").html(
                    '<h3>注册成功</h3><div id="qrcode"></div><h3>扫二维码下载</h3>'
                );
                var text = "http://api.uuuooo.net/download";
                // 设置参数方式
                var qrcode = new QRCode('qrcode', {
                    text: text,
                    width: 256,
                    height: 256,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: QRCode.CorrectLevel.H
                });
            },

            400: function (res) {
                alert(res.message);
            },

            500: function () {
                alert("网络／服务器异常！");
            }
        }
    });

}
