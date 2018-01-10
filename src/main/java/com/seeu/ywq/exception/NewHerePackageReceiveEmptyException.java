package com.seeu.ywq.exception;

public class NewHerePackageReceiveEmptyException extends Exception {
    public NewHerePackageReceiveEmptyException(Long uid) {
        super("新人礼包已经领取完了！用户【UID:" + uid + "】");
    }
}
