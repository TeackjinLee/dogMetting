package com.springboot.dogmeeting.api.blog;

import com.springboot.dogmeeting.api.blog.dto.*;
import com.springboot.dogmeeting.datasource.article.Article;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장
        return "articleList"; // articleList.html 뷰 조회
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable(name = "id") Long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<AritcleResponse>> getAllArticles() {
        List<AritcleResponse> articleList = blogService.findAll()
                .stream()
                .map(AritcleResponse::new)
                .toList();
        return ResponseEntity.ok().body(articleList);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<AritcleResponse> findArticle(@PathVariable(name = "id") Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new AritcleResponse(article));
    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable(name = "id") Long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

}
