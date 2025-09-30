package com.example.newboard.web.view;

import com.example.newboard.domain.Article;
import com.example.newboard.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor  // final로 필드에 선언되어 있으면 객체로 생성하지 않아도 자동으로 쓸 수 있게 해줌
public class ArticleViewController {
    private final ArticleService articleService;

    // 글 목록 (페이징 + 최신순)
    @GetMapping("/articles")
    public String list(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                           Pageable pageable, Model model) {

        Page<Article> articles = articleService.findAll(pageable);
        model.addAttribute("articles", articles);

//        model.addAttribute("articles", articleService.findAll());
        return "article-list";  // html 이동
    }

    @GetMapping("/articles/new")  // 화면에서 가져와
    public String createForm() { return "article-form"; }

    @GetMapping("/articles/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication auth, HttpSession session){
        // 조회수 증가 여부 확인 (세션 기반)
        articleService.checkAndIncreaseViewCount(id, session);

        // 게시글 조회 (증가 여부는 위에서 처리했으므로 단순 조회만)
        var article = articleService.findById(id);

        model.addAttribute("article", article);
        boolean isOwner = auth != null && article.getAuthor().getEmail().equals(auth.getName());
        model.addAttribute("isOwner", isOwner);
        return "article-detail";
    }

    @GetMapping("/articles/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){  // 스프링이 모델 제공
        var article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article-edit";
    }
}
