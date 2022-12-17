package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.repository.ArticleJpaRepository;
import com.example.testSecurity.service.ArticleService;
import com.example.testSecurity.exception.enums.ServiceMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleJpaRepository articleJpaRepository;
    @Override
    public ArticleDto.Info createArticle(ArticleDto.Create articleCreateDTO) {
        Article savedArticle = articleJpaRepository.save(ArticleDto.Create.toEntity(articleCreateDTO));
        return Article.toDto(savedArticle);
    }

    @Override
    public ArticleDto.Info getArticle(Long no) {

        Optional<Article> articleByNo = articleJpaRepository.findById(no);
        if(articleByNo.isPresent()){
            return Article.toDto(articleByNo.get());
        }else{
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND);
        }
    }

    @Override
    public ArticleDto.Info updateArticle(ArticleDto.Create articleCreateDTO, Long no) {
        Article savedArticle = articleJpaRepository.save(ArticleDto.Create.toEntity(articleCreateDTO));
        return Article.toDto(savedArticle);
    }

    @Override
    public void deleteArticle(Long no) {
        articleJpaRepository.deleteById(no);
    }

    @Override
    public void bookmarkArticle(Long articleNo, Integer loginMemberNo) {
        articleJpaRepository.addArticleBookMark(Long.valueOf(loginMemberNo),articleNo);
    }

    @Override
    public Boolean checkIsMemberArticle(Long no, Integer loginMemberNo) {
        return null;
    }
}
