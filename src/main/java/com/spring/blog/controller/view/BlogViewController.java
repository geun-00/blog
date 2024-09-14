package com.spring.blog.controller.view;

import com.spring.blog.common.annotation.UserKey;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.dto.response.PageResponse;
import com.spring.blog.service.BlogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/")
    public String home() {
        return "redirect:/guest";
    }

    @GetMapping("/guest")
    public String guest(HttpServletRequest request, HttpServletResponse response) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        return "redirect:/articles";
    }

    @GetMapping("/articles")
    public String getArticles(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                              Model model) {

        PageResponse<ArticleListViewResponse> articles = blogService.findAll(pageable);

        model.addAttribute("articles", articles.getDataList());
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("searchType", SearchType.values());

        model.addAttribute("page", articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id,
                             @UserKey String userKey,
                             Model model) {

        ArticleViewResponse response = blogService.getArticleAndIncreaseViews(id, userKey);
        model.addAttribute("article", response);

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(name = "id", required = false) Long id, Model model) {

        if (id == null) {
            model.addAttribute("article", AddArticleViewResponse.of());
        } else {
            AddArticleViewResponse article = blogService.getAddArticleViewResponse(id);
            model.addAttribute("article", article);
        }

        return "newArticle";
    }
}