package com.seeu.third.im;

import com.seeu.third.exception.IMTokenGetException;

public interface IMService {
    // 此处采用了融云服务，所以获取 token 参数即可
    String getToken(Long uid, String username, String headIconUrl)throws IMTokenGetException;
}
