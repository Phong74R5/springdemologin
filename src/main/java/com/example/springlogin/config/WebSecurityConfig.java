package com.example.springlogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springlogin.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // set dịch vụ để tìm kiếm user trong database
        // và set PasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // các trang không yêu cầu login
        http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();

        // trong /userInfo yêu cầu phải login với vai trò ROLE_USER hoặc ROLE_ADMIN
        // Nếu chưa login, nó sẽ redirect với trong /login
        http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')");

        // trang chỉ dành cho admin
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");

        // khi người dùng đã login, với vai trò ROLE_USER
        // nhưng truy cập vào trang yêu cầu vai trò ROLE_ADMIN
        // ngoại lệ accessDeniedException sẽ ném ra
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // cấu hình cho login form
        http.authorizeRequests().and().formLogin()
                // submit url của trang login
                .loginPage("/login")//
                .defaultSuccessUrl("/userAccountInfo")// dg dẫn tới trang đăng nhập thành công
                .failureUrl("/login?error=true")// dg dẫn tới trang dn thất bại
                .usernameParameter("username")
                .passwordParameter("password")
                // cấu hình cho logout page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
    }
}
