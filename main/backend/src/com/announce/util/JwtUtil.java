package com.announce.util;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    // 默认的 token 有效期为 24 小时（单位：毫秒）
    private static final long tokenExpiration = 24 * 60 * 60 * 1000;

    // 默认的 token 签名密钥
    private static final String tokenSignKey = "123456";

    /**
     * 生成 JWT token
     * @param userId 用户 ID
     * @return 生成的 JWT token 字符串
     */
    public static String createToken(Long userId) {
        // 使用 Jwts.builder() 创建一个 JWT
        return Jwts.builder()
                .setSubject("YYGH-USER")  // 设置 token 的主题，标识身份
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))  // 设置 token 的过期时间
                .claim("userId", userId)  // 将用户 ID 作为 claim 存储
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)  // 使用 HS512 算法和密钥进行签名
                .compressWith(CompressionCodecs.GZIP)  // 对 token 进行 GZIP 压缩
                .compact();
    }

    /**
     * 从 JWT token 中获取用户 ID
     * @param token JWT token 字符串
     * @return 提取的用户 ID，如果 token 为空或无效，返回 null
     */
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;  // 如果 token 为空，直接返回 null

        // 解析 token 并获取 Claims 对象
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        // 从 claims 中获取用户 ID，注意将其从 Integer 转换为 Long
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }

    /**
     * 判断 JWT token 是否已经过期
     * @param token JWT token 字符串
     * @return 如果 token 过期，返回 true；否则返回 false
     */
    public static boolean isExpiration(String token) {
        try {
            // 解析 token 并获取过期时间。如果过期时间在当前时间之前，表示 token 已经过期
            return Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            // 如果解析过程中出现异常（例如 token 无效），表示 token 已经过期
            return true;
        }
    }
}
