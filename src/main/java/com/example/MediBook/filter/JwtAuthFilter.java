package com.example.MediBook.filter;

import com.example.MediBook.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component 
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;
    //UserDetailsService là interface có sẳn trong Spring Security dùng để :Lấy thông tin user từ database khi đăng nhập
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(//khi có request đến server thì hàm này sẽ được gọi để xử lý JWT token trong header của request
        @NonNull HttpServletRequest request, //HttpServletRequest là đối tượng đại diện cho yêu cầu HTTP đến server
        @NonNull HttpServletResponse response,//HttpServletResponse là đối tượng đại diện cho phản hồi HTTP từ server đến client
        @NonNull FilterChain filterChain)//nếu request hợp lệ thì tiếp tục chuỗi filter, nếu không thì dừng lại và trả về lỗi
        throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            //ko có header Authorization hoặc header ko bắt đầu bằng "Bearer " thì bỏ
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response); //tiếp tục chuỗi filter nếu header ko hợp lệ
                return;
            }

            final String jwt = authHeader.substring(7); //loại bỏ 7 ký tự đầu 
            final String userEmail = jwtUtil.extractUsername(jwt);//giải mã token để lấy tên đăng nhập

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //vào csdl để lấy thông tin user có cái email này
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                //gọi hàm kiểm tra xem cái token có phải của user này ko, nếu hợp lệ thì tạo đối tượng Authentication để lưu vào SecurityContext
                if (jwtUtil.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, // thông tin
                        null ,//MK
                        userDetails.getAuthorities//quyền
                        ());
                        //hàm setDetails để thêm thông tin chi tiết về request vào đối tượng Authentication, như địa chỉ IP, thông tin trình duyệt, v.v.
                        authToken.setDetails(new  //setDetails(...) Gắn các thông tin đó vào authToken
                            WebAuthenticationDetailsSource().buildDetails(request)//buildDetails(request)  lấy thông tin từ requesst
                        );
                        //SecurityContextHolder là nới Spring security lưu trữ thông tin bảo mật của người dùng hiện tại, bao gồm đối tượng Authentication. Khi bạn gọi SecurityContextHolder.getContext().setAuthentication(authToken), bạn đang đặt đối tượng Authentication mới vào SecurityContext, cho phép Spring Security nhận biết rằng người dùng đã được xác thực và có thể truy cập các tài nguyên được bảo vệ dựa trên quyền của họ.
                        //setAuthentication(authToken) đưa thông tin user vào hệ thống
                        SecurityContextHolder.getContext().setAuthentication(authToken); //lưu đối tượng Authentication vào SecurityContext để Spring Security có thể sử dụng thông tin này cho các bước bảo mật tiếp theo
                }
            }
            filterChain.doFilter(request, response); //tiếp tục chuỗi filter sau khi đã xử lý JWT
        }

        

}
 