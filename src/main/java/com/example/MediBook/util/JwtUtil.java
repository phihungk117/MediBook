package com.example.MediBook.util;

import com.example.MediBook.entity.User; // Import chính xác Entity User của bạn
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    // Lấy secret key và thời gian sống từ file application.yml
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // hàm tạo chữ ký bảo mật để xác thực JWT token
    private SecretKey getSingKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        //Tạo SecretKey dùng cho thuật toán HMAC-SHA
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //hàm tạo token 
    //UserDetails userDetails là object của Spring Security chưa username,password,authorities
    //u
    public String generateToken(UserDetails userDetails,User userEntity){
        //calim = dữ liệu ên trong JWt
        Map<String,Object> extraClaims = new HashMap<>();
        //thêm role vào claim
        extraClaims.put("role",userEntity.getRole().name());
        extraClaims.put("userId",userEntity.getId().toString());
        //bắt dầu tạo JWT
        return Jwts.builder() //builder để tạo jwt
                .claims(extraClaims) //thêm claim vào jwt
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis())) //thời gian tạo token
                .expiration(new Date(System.currentTimeMillis() + expiration)) //thời gian hết hạn
                .signWith(getSingKey()) //ký token với secret key
                .compact(); //đóng gói token thành chuỗi
    }
        // Overload: Nếu chỉ truyền UserDetails mà không cần biến phụ thì xài hàm này
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    //giải mã token để lấy thông tin
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey()) // Đưa khóa bí mật ra đối chiếu chữ ký
                .build()
                .parseSignedClaims(token) // Bóc tách dữ liệu
                .getPayload(); // Lấy khúc Payload chứa data
        return claimsResolver.apply(claims);
    }

    //LẤY EMAIL (USERNAME) TỪ TOKEN
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    // 3. LẤY ROLE TỪ TOKEN
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    // 5. KIỂM TRA HẾT HẠN CHƯA

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Trùng khớp email VÀ chưa quá hạn thì trả về TRUE
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}  
