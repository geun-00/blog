package com.spring.blog.controller;

import com.spring.blog.domain.Article;
import com.spring.blog.dto.ArticleListViewResponse;
import com.spring.blog.dto.ArticleViewResponse;
import com.spring.blog.service.BlogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/")
    public String home() {
        return "redirect:/articles";
    }

    @GetMapping("/guest")
    public String guest(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();

        for (Cookie cookie : request.getCookies()) {
            if ("JSESSIONID".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return "redirect:/articles";
    }

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAllByCreatedAtDesc().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("currentDate", LocalDate.now());

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {

        Article article = blogService.findWithUserById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(name = "id", required = false) Long id, Model model) {

        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findWithUserById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
