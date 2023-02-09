package com.example.testSecurity.service.service.impl;

import com.example.testSecurity.domain.dto.article.ArticleCreateDto;
import com.example.testSecurity.domain.dto.article.ArticleInfoDto;
import com.example.testSecurity.domain.dto.article.ArticleSearchConditionDto;
import com.example.testSecurity.domain.dto.article.ArticleUpdateDto;
import com.example.testSecurity.domain.entity.Article;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.MemberArticleBookmark;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.service.exception.ServiceProcessException;
import com.example.testSecurity.domain.repository.querydlsRepository.ArticleCustomRepository;
import com.example.testSecurity.domain.repository.ArticleJpaRepository;
import com.example.testSecurity.domain.repository.MemberArticleBookmarkJpaRepository;
import com.example.testSecurity.domain.repository.MemberJpaRepository;
import com.example.testSecurity.service.exception.enums.ServiceMessage;
import com.example.testSecurity.domain.repository.ProfileJpaRepository;
import com.example.testSecurity.service.service.ArticleService;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleJpaRepository articleJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ArticleCustomRepository articleCustomRepository;
    private final MemberArticleBookmarkJpaRepository bookmarkJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;


    @Override
    @Transactional
    public ArticleInfoDto createArticle(ArticleCreateDto articleCreateDTO,
        Long loginMemberNo) {

        //must have profile when create article
        Profile profileByMemberNo = profileJpaRepository.findProfileByMemberNo(loginMemberNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));

        articleCreateDTO.changeWriter(profileByMemberNo);

        Article savedArticle = articleJpaRepository.save(Article.of(articleCreateDTO));

        log.info("============savedArticle============");
        log.info("savedArticle : {}", savedArticle);
        log.info("============savedArticle============");

        return ArticleInfoDto.of(savedArticle);
    }

    @Override
    @Transactional
    public ArticleInfoDto getArticle(Long articleNo) {
        Article articleByNo = getArticleByNo(articleNo);
        articleByNo.addView();
        return ArticleInfoDto.of(articleByNo);
    }

    @Override
    @Transactional
    public ArticleInfoDto updateArticle(ArticleUpdateDto articleUpdateDto, Long articleNo) {

        Article articleByNo = getArticleByNo(articleNo);

        log.info("============before update article============");
        log.info("article : {}", articleByNo);
        log.info("============before update article============");

        //조회로 인한 영속성 상태+ 트랜젹선 안에서 +  엔티티 변경 = 더티체킹
        articleByNo.update(articleUpdateDto);

        log.info("===========after update article============");
        log.info("updatedArticle : {}", articleByNo);
        log.info("===========after update article============");

        return ArticleInfoDto.of(articleByNo);
    }


    @Override
    public void deleteArticle(Long articleNo) {
        articleJpaRepository.deleteById(articleNo);
    }

    @Override
    public void bookmarkArticle(Long articleNo, Member member) {

        Optional<Profile> profileByMemberNo = profileJpaRepository.findProfileByMemberNo(
            member.getNo());

        if (profileByMemberNo.isEmpty()) {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE);
        }

        MemberArticleBookmark bookmark = MemberArticleBookmark.builder()
            .profile(profileByMemberNo.get())
            .article(getArticleByNo(articleNo))
            .build();
        bookmarkJpaRepository.save(bookmark);

        log.info("============savedBookmark============");
        log.info("savedBookmark : {}", bookmark);
        log.info("============savedBookmark============");

    }

    @Override
    public Boolean checkIsMemberArticle(Long articleNo, Long loginMemberNo) {
        if (articleCustomRepository.checkIsMemberArticle(articleNo, loginMemberNo) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public ArticleInfoDto likeArticle(Long articleNo) {
        Article articleByNo = getArticleByNo(articleNo);

        log.info("============before like article============");
        log.info("likeArticle : {}", articleByNo);
        log.info("============Before like Article============");

        articleByNo.addLike();

        log.info("============after like article============");
        log.info("likeArticle : {}", articleByNo);
        log.info("============after like article============");

        return ArticleInfoDto.of(articleByNo);
    }

    @Override
    public Page<ArticleInfoDto> search(ArticleSearchConditionDto searchCondition,
        Pageable pageable) {
        return articleCustomRepository.search(searchCondition, pageable)
            .map(ArticleInfoDto::of);
    }

    @Override
    public Page<ArticleInfoDto> getArticles(Pageable pageable) {
        return articleJpaRepository.findAll(pageable)
            .map(ArticleInfoDto::of);
    }

    private Article getArticleByNo(Long articleNo) {
        return articleJpaRepository.findById(articleNo).orElseThrow(
            () -> new ServiceProcessException(ServiceMessage.NOT_FOUND));
    }
}
