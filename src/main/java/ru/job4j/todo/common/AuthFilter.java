package ru.job4j.todo.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * Проверка доступа
 */
@Component
public class AuthFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class.getName());
    private static final String USER = "user";
    private static final String LOGIN_PAGE = "users/auth";

    private final Set<String> addresses = Set.of(
            "users/formAdd",
            "users/registration",
            "users/success",
            "users/fail",
            "users/login",
            "users/auth"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Фильтр авторизации");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (addressPresent(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (isNull(req.getSession().getAttribute(USER))) {
            res.sendRedirect(req.getContextPath() + LOGIN_PAGE);
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean addressPresent(String uri) {
        return addresses.stream()
                .anyMatch(address -> uri.endsWith(address));
    }
}
