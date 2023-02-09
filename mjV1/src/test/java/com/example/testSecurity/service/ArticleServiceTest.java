package com.example.testSecurity.service;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.enums.RoleType;
import com.example.testSecurity.domain.dto.article.ArticleCreateDto;
import com.example.testSecurity.domain.dto.article.ArticleInfoDto;
import com.example.testSecurity.domain.dto.article.ArticleSearchConditionDto;
import com.example.testSecurity.domain.dto.article.ArticleUpdateDto;
import com.example.testSecurity.domain.dto.career.CareerCreateDto;
import com.example.testSecurity.domain.dto.member.MemberCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.entity.Article;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.MemberArticleBookmark;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.repository.ArticleJpaRepository;
import com.example.testSecurity.domain.repository.MemberArticleBookmarkJpaRepository;
import com.example.testSecurity.domain.repository.MemberJpaRepository;
import com.example.testSecurity.domain.repository.ProfileJpaRepository;
import com.example.testSecurity.service.service.ArticleService;
import com.example.testSecurity.service.service.ProfileService;
import com.example.testSecurity.service.service.auth.AuthService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleServiceTest {


    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileJpaRepository profileJpaRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private ArticleJpaRepository articleJpaRepository;
    @Autowired
    private MemberArticleBookmarkJpaRepository bookmarkJpaRepository;


    //Article 작성 조건 : 멤버 -> 프로파일 하나있어야함
    @BeforeEach
    private void addMember() {

        MemberCreateDto member = MemberCreateDto.builder()
            .userName("testUserName")
            .password("12345")
            .roleType(RoleType.ADMIN)
            .build();
        authService.createMember(member);

    }


    @BeforeEach
    private void addProfileWithArticle() {

        List<CareerCreateDto> careerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CareerCreateDto careerCreateDto = CareerCreateDto.builder()
                .companyName("company" + i)
                .assignedTask("assignedTask" + i)
                .description("description" + i)
                .startPeriod(20200610)
                .finishPeriod(20210610)
                .category(CategoryType.PROGRAMMING)
                //.categoryDetail()
                .build();
            careerList.add(careerCreateDto);
        }

        ProfileCreateDto create = ProfileCreateDto.builder()
            .name("test")
            .introduce("test introduce")
            .email("test email")
            .contactNumber("010-1234-5678")
            .careers(careerList)
            .build();

        Member member = memberJpaRepository.findAll().get(0);
        profileService.createProfile(create, member);

        ArticleCreateDto articleCreate = ArticleCreateDto.builder()
            .title("test title")
            .contents("test contents")
            .profile(Profile.of(create))  //게시글의 작성자는 Profile 자동 등록
            .likeCnt(0)
            .views(0)
            .category(CategoryType.PROGRAMMING)
            .build();

        articleService.createArticle(articleCreate, member.getNo());
    }


    @AfterEach
    public void deleteAll() {
        bookmarkJpaRepository.deleteAll();
        articleJpaRepository.deleteAll();
        profileJpaRepository.deleteAll();
        memberJpaRepository.deleteAll();
    }

    @Test
    @Transactional
    void createArticle() {

        //given
        //when
        List<Article> all = articleJpaRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getTitle()).isEqualTo("test title");
        assertThat(all.get(0).getCategory()).isEqualTo(0);
        assertThat(all.get(0).getProfile().getName()).isEqualTo("test");
    }

    //조회수 증가
    @Test
    void getArticle() {

        //given
        //when
        Article article = articleJpaRepository.findAll().get(0);
        ArticleInfoDto articleInfo = articleService.getArticle(article.getNo());

        //then
        assertThat(article.getTitle()).isEqualTo("test title");
        assertThat(article.getContents()).isEqualTo("test contents");
        assertThat(articleInfo.getViews()).isEqualTo(1); // 조회수 1증가

    }

    @Test
    @Transactional
    void updateArticle() {
        //given
        ArticleUpdateDto update = ArticleUpdateDto.builder()
            .title("update test title")
            .contents("update test contents")
            .profile(profileJpaRepository.findAll().get(0))  //게시글의 작성자는 Profile로 자동 등록
            .likeCnt(0)
            .views(0)
            .category(CategoryType.ANNOUNCE)
            .build();

        //when
        Article article = articleJpaRepository.findAll().get(0);
        ArticleInfoDto articleInfo = articleService.updateArticle(update, article.getNo());
        //then
        assertThat(articleInfo.getTitle()).isEqualTo("update test title");
        assertThat(articleInfo.getContents()).isEqualTo("update test contents");
        assertThat(articleInfo.getCategory()).isEqualTo(CategoryType.ANNOUNCE);
    }

    @Test
    void deleteArticle() {
        //given
        //when
        Article article = articleJpaRepository.findAll().get(0);
        articleService.deleteArticle(article.getNo());

        //then
        assertThat(articleJpaRepository.findAll().size()).isEqualTo(0);
        assertThat(articleJpaRepository.findById(article.getNo())).isEqualTo(Optional.empty());

    }

    @Test
    @Transactional
    void bookmarkArticle() {

        //given
        //when
        Member member = memberJpaRepository.findAll().get(0);
        Article article = articleJpaRepository.findAll().get(0);
        articleService.bookmarkArticle(article.getNo(), member);

        Profile profile = profileJpaRepository.findAll().get(0);

        MemberArticleBookmark memberArticleBookmark = bookmarkJpaRepository.findAll().get(0);
        //then

        assertThat(memberArticleBookmark.getArticle()).isEqualTo(article);
        assertThat(memberArticleBookmark.getProfile()).isEqualTo(profile);

    }

    @Test
    void checkIsMemberArticle() {

        //given
        //when
        Member member = memberJpaRepository.findAll().get(0);
        Article article = articleJpaRepository.findAll().get(0);

        Boolean result = articleService.checkIsMemberArticle(article.getNo(), member.getNo());
        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @Transactional
    void likeArticle() {

        //given
        //when
        Article article = articleJpaRepository.findAll().get(0);
        articleService.likeArticle(article.getNo());

        //then
        assertThat(article.getLikeCnt()).isEqualTo(1);
    }

    //제목+내용 , 카테고리로 검색
    @Test
    void search() {

        //given
        Profile profile = profileJpaRepository.findAll().get(0);

        ArticleCreateDto create = ArticleCreateDto.builder()
            .title("second test title")
            .contents("test contents")
            .profile(profile)  //게시글의 작성자는 Profile로 자동 등록
            .likeCnt(0)
            .views(55)
            .category(CategoryType.ANNOUNCE)
            .build();

        ArticleCreateDto secondArticle = ArticleCreateDto.builder()
            .title("article title")
            .contents("article contents")
            .profile(profile)  //게시글의 작성자는 Profile로 자동 등록
            .likeCnt(0)
            .views(55)
            .category(CategoryType.PROGRAMMING)
            .build();

        ArticleCreateDto thirdArticle = ArticleCreateDto.builder()
            .title("third article title")
            .contents("third article contents")
            .profile(profile)  //게시글의 작성자는 Profile로 자동 등록
            .likeCnt(0)
            .views(55)
            .category(CategoryType.PROGRAMMING)
            .build();

        Long loginMemberNo = memberJpaRepository.findAll().get(0).getNo();
        articleService.createArticle(create, loginMemberNo);
        articleService.createArticle(secondArticle, loginMemberNo);
        articleService.createArticle(thirdArticle, loginMemberNo);

        ArticleSearchConditionDto searchCondition = new ArticleSearchConditionDto("third",
            CategoryType.PROGRAMMING);
        ArticleSearchConditionDto secondSearchCondition = new ArticleSearchConditionDto(null,
            CategoryType.PROGRAMMING);

        Sort sort = Sort.by("article_no").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        //when
        Page<ArticleInfoDto> firstSearchResult = articleService.search(searchCondition,
            pageable); // 0
        Page<ArticleInfoDto> secondSearchResult = articleService.search(secondSearchCondition,
            pageable);

        //then
        assertThat(firstSearchResult.getTotalElements()).isEqualTo(1);
        assertThat(firstSearchResult.getContent().get(0).getContents()).isEqualTo(
            "third article contents");

        assertThat(secondSearchResult.getTotalElements()).isEqualTo(3);
        assertThat(secondSearchResult.getContent().get(1).getTitle()).isEqualTo("article title");
    }
}