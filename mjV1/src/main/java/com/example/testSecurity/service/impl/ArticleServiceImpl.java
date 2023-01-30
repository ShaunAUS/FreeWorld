package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ArticleDto;
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

        //게시글 작성시 프로필이 최소 하나
        Profile profile = profileJpaRepository.findByMemberNo(loginMemberNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));

        //게시글의 작성자는 Profile name으로 자동 등록
        articleCreateDTO.changeWriter(profile.getName());

        Article savedArticle = articleJpaRepository.save(
            ArticleDto.Create.toEntity(articleCreateDTO, profile));

        log.info("============savedArticle============");
        log.info("savedArticle : {}", savedArticle);
        log.info("============savedArticle============");

        return savedArticle.toInfoDto();
    }

    @Override
    @Transactional
    public ArticleDto.Info getArticle(Long articleNo) {

        Optional<Article> articleByNo = articleJpaRepository.findById(articleNo);

        if (articleByNo.isPresent()) {
            Article article = articleByNo.get();
            //조회수 증가
            article.addView();
            return article.toInfoDto();

        } else {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ArticleDto.Info updateArticle(ArticleDto.Create articleCreateDTO, Long no) {

        Optional<Article> articleById = articleJpaRepository.findById(no);

        if (articleById.isPresent()) {
            Article article = articleById.get();

            log.info("============before update article============");
            log.info("article : {}", article);
            log.info("============before update article============");

            //조회로 인한 영속성 상태+ 트랜젹선 안에서 +  엔티티 변경 = 더티체킹
            //Article.update(article, articleCreateDTO);
            article.update(articleCreateDTO);

            log.info("===========after update article============");
            log.info("updatedArticle : {}", article);
            log.info("===========after update article============");

            return article.toInfoDto();

        } else {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND);
        }
    }

    @Override
    public void deleteArticle(Long no) {
        articleJpaRepository.deleteById(no);
    }

    @Override
    public void bookmarkArticle(Long articleNo, Member member) {
        //TODO 일단은 findById 객체 통쨰로 넣기 -> 나중에 id만 조절
        Article article = articleJpaRepository.findById(articleNo).get();

        MemberArticleBookmark bookmark = MemberArticleBookmark.builder()
            .article(article)
            .member(member)
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
        Article article = articleJpaRepository.findById(articleNo).get();

        log.info("============before like article============");
        log.info("likeArticle : {}", article);
        log.info("============Before like Article============");

        article.addLike();

        log.info("============after like article============");
        log.info("likeArticle : {}", article);
        log.info("============after like article============");

        return article.toInfoDto();
    }

    @Override
    public Page<ArticleDto.Info> search(ArticleDto.Search searchCondition, Pageable pageable) {
        return articleCustomRepository.search(searchCondition, pageable)
            .map(Article::toInfoDto);
    }
}
