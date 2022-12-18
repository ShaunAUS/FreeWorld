package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.querydlsRepository.ArticleCustomRepository;
import com.example.testSecurity.repository.ArticleJpaRepository;
import com.example.testSecurity.repository.MemberJpaRepository;
import com.example.testSecurity.service.ArticleService;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleJpaRepository articleJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ArticleCustomRepository articleCustomRepository;
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
    @Transactional
    public ArticleDto.Info updateArticle(ArticleDto.Create articleCreateDTO, Long no) {

        Optional<Article> articleById = articleJpaRepository.findById(no);

        if(articleById.isPresent()) {
            Article article = articleById.get();
            //TODO : Dirty checking
            Article.update(article, articleCreateDTO);
            return Article.toDto(article);
        }else{
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND);
        }
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
    public Boolean checkIsMemberArticle(Long articleNo, Integer loginMemberNo) {
        return articleCustomRepository.checkIsMemberArticle(articleNo, loginMemberNo).isPresent();
    }
}
