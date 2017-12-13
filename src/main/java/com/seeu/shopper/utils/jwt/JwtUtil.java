package com.seeu.shopper.utils.jwt;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    private JwtConstant jwtConstant;

    @Value("${com.seeu.jwt.secret}")
    private String JWT_SECRET;

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public SecretKey generalKey() {
        String stringKey = "seeu" + jwtConstant.getJWT_SECRET();
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * 生成subject信息
     *
     * @param user
     * @return
     */
    public String generalSubject(EmailRegistUserToken user) {
        JSONObject jo = new JSONObject();
        jo.put("uid", user.getUid());
        jo.put("type", user.getType());
        jo.put("extra",user.getExtra());
        jo.put("email",user.getEmail());
        return jo.toJSONString();
    }

    /**
     * 用 TOKEN 获取用户标识信息
     * @param token
     * @return
     */
    public EmailRegistUserToken parseToken(String token) {
        if (token == null || token.length() < 10) {
            return null;
        }
        try {
            Claims claims = parseJWT(token);
            String json = claims.getSubject();
            return JSONObject.parseObject(json, EmailRegistUserToken.class);
        } catch (Exception e) {
            // 不应该把你抛出去，要不然会报 400 Fail ，而不是 401 Unsigned
//            throw new ServiceException("Illegal Token : " + token);
            return null;
        }
    }
}
