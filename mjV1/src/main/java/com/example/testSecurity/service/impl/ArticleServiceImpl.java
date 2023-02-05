package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.dto.ArticleDto.Info;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.entity.MemberArticleBookmark;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.querydlsRepository.ArticleCustomRepository;
import com.example.testSecurity.repository.ArticleJpaRepository;
import com.example.testSecurity.repository.MemberArticleBookmarkJpaRepository;
import com.example.testSecurity.repository.MemberJpaRepository;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.ArticleService;
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
    public ArticleDto.Info createArticle(ArticleDto.Create articleCreateDTO,
        Long loginMemberNo) {

        //must have profile when create article
        Profile profileByMemberNo = profileJpaRepository.findProfileByMemberNo(loginMemberNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));

        articleCreateDTO.changeWriter(profileByMemberNo);

        Article savedArticle = articleJpaRepository.save(articleCreateDTO.toEntity());

        log.info("============savedArticle============");
        log.info("savedArticle : {}", savedArticle);
        log.info("============savedArticle============");

        return savedArticle.toInfoDto();
    }

    @Override
    @Transactional
    public ArticleDto.Info getArticle(Long articleNo) {
        Article articleByNo = getArticleByNo(articleNo);
        articleByNo.addView();
        return articleByNo.toInfoDto();
    }

    @Override
    @Transactional
    public ArticleDto.Info updateArticle(ArticleDto.Update articleUpdateDto, Long articleNo) {

        Article articleByNo = getArticleByNo(articleNo);

        log.info("============before update article============");
        log.info("article : {}", articleByNo);
        log.info("============before update article============");

        //조회로 인한 영속성 상태+ 트랜젹선 안에서 +  엔티티 변경 = 더티체킹
        articleByNo.update(articleUpdateDto);

        log.info("===========after update article============");
        log.info("updatedArticle : {}", articleByNo);
        log.info("===========after update article============");

        return articleByNo.toInfoDto();
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
    public ArticleDto.Info likeArticle(Long articleNo) {
        Article articleByNo = getArticleByNo(articleNo);

        log.info("============before like article============");
        log.info("likeArticle : {}", articleByNo);
        log.info("============Before like Article============");

        articleByNo.addLike();

        log.info("============after like article============");
        log.info("likeArticle : {}", articleByNo);
        log.info("============after like article============");

        return articleByNo.toInfoDto();
    }

    @Override
    public Page<ArticleDto.Info> search(ArticleDto.Search searchCondition, Pageable pageable) {
        return articleCustomRepository.search(searchCondition, pageable)
            .map(Article::toInfoDto);
    }

    @Override
    public Page<Info> getArticles(Pageable pageable) {
        return articleJpaRepository.findAll(pageable)
            .map(article -> article.toInfoDto());
    }

    private Article getArticleByNo(Long articleNo) {
        return articleJpaRepository.findById(articleNo).orElseThrow(
            () -> new ServiceProcessException(ServiceMessage.NOT_FOUND));
    }
}
