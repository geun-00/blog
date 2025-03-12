# 게시판 프로젝트
- **Spring Boot**로 개발한 기본적인 웹 CRUD 게시판 프로젝트입니다.
- 배포 주소 : https://www.geuns-blog.com (AWS 비용 이슈로 서비스 중단)
- 최근 업데이트 : 25.03.12

<img width="940" alt="image" src="https://github.com/user-attachments/assets/59a2e41b-f39a-4323-8697-a31a22d6bc23">

# 1. 프로젝트 소개
- **프로젝트 명** : 웹 게시판 프로젝트
- **프로젝트 기간** : 2024.08.14 ~
- **프로젝트 목적**
  - 프로젝트 설정부터 배포까지 웹 개발의 A 부터 Z를 스스로 할 수 있는 능력에 대한 발전을 최우선 목표로 두었습니다.
  - 그동안 이론적으로 학습했던 기술들을 CRUD 프로젝트에 접목시켜 웹 애플리케이션 개발에 대한 기본기 발전과 신기술 체득을 목표로 두었습니다.

 # 2. 개발 환경

### Framework 
<img src="https://img.shields.io/badge/springboot-6DB33F?style=flat&logo=springboot&logoColor=white">![3.3.2](https://img.shields.io/badge/3.3.2-%23000000?style=flat&logo=3.3.2&logoColor=white)

### Language
<img src="https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white">![17](https://img.shields.io/badge/17-%23000000?style=flat&logo=17&logoColor=white)
 
### Build
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=flat&logo=Gradle&logoColor=white)![8.8](https://img.shields.io/badge/8.8-%23000000?style=flat&logo=8.8&logoColor=white)

### IDE
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=flat&logo=intellij-idea&logoColor=white)

### Database
![H2](https://img.shields.io/badge/H2-%230854C1?style=flat&logo=H2&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=flat&logo=mysql&logoColor=white)

### Version Control
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=flat&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=flat&logo=github&logoColor=white)

### Deployment
![AWS Elastic Beanstalk](https://img.shields.io/badge/amazon%20elastic%20beanstalk-%2383B81A?style=flat&logo=amazonelb&logoColor=white)

# 3. 기술 스택

### Backend

<img src="https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=flat&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=Spring-Security&logoColor=white"> <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=Hibernate&logoColor=white"> 

### Frontend

<img src="https://img.shields.io/badge/HTML-239120?style=flat&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/CSS-239120?&style=flat&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/Bootstrap-563D7C?style=flat&logo=bootstrap&logoColor=white">

![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=flat&logo=Thymeleaf&logoColor=white) ![Quill](https://img.shields.io/badge/Quill-52B0E7?style=flat&logo=apache&logoColor=white)

### Cloud

![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=flat&logo=githubactions&logoColor=white) 

![AWS S3](https://img.shields.io/badge/Amazon%20S3-569A31?style=flat&logo=Amazon%20S3&logoColor=white)
![AWS RDS](https://img.shields.io/badge/amazon%20rds-%23527FFF?style=flat&logo=amazon%20rds&logoColor=white)
![AWS Route53](https://img.shields.io/badge/amazon%20route53-%238C4FFF?style=flat&logo=amazonroute53&logoColor=white)
![AWS Elastic Cache](https://img.shields.io/badge/amazon%20elasticache-%23C925D1?style=flat&logo=amazonelasticache&logoColor=white)
![AWS EC2](https://img.shields.io/badge/amazon%20ec2-%23FF9900?style=flat&logo=amazonec2&logoColor=white)
![AWS Elastic Beanstalk](https://img.shields.io/badge/amazon%20elastic%20beanstalk-%2383B81A?style=flat&logo=amazonelb&logoColor=white)


### etc.

![MapStruct](https://img.shields.io/badge/mapstruct-%23D0271D?style=flat&logo=map%20struct&logoColor=white)
![QueryDSL](https://img.shields.io/badge/querydsl-%2331A8FF?style=flat&logoColor=white)
![OAUTH2](https://img.shields.io/badge/oauth2-%23000B1D?style=flat&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=flat&logo=redis&logoColor=white)
![Lombok](https://img.shields.io/badge/lombok-%23FF0000?style=flat&logoColor=white)
![Junit](https://img.shields.io/badge/junit5-%2325A162?style=flat&logo=junit5&logoColor=white)
![Jasypt](https://img.shields.io/badge/jasypt-%2341454A?style=flat&logoColor=white)

# 4. 프로젝트 구조

## 디렉토리

<details>
  <summary>Backend</summary>
  <pre>
    <code>
📦java
 ┗ 📂com
 ┃ ┗ 📂spring
 ┃ ┃ ┗ 📂blog
 ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentResponseMapping.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ConditionalValidation.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CurrentUser.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DuplicateCheck.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EncodeMapping.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜NotBlankContent.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜UserKey.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ValidArticleSearchRequest.java
 ┃ ┃ ┃ ┃ ┣ 📂aop
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DuplicateCheckAspect.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Pointcuts.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TransactionalLoggingAspect.java
 ┃ ┃ ┃ ┃ ┣ 📂argumentResolver
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CurrentUserArgumentResolver.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserKeyArgumentResolver.java
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂authority
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomAuthorityMapper.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂configs
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AppConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AsyncConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CacheConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JasyptConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜S3Config.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WebMvcConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebOAuthSecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂oauth
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂logoutHandler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractOAuth2LogoutHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomLogoutSuccessHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoogleLogoutHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoLogoutHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NaverLogoutHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomOAuth2SuccessHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginFailureHandler.java
 ┃ ┃ ┃ ┃ ┣ 📂converters
 ┃ ┃ ┃ ┃ ┃ ┣ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CookieUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OAuthAttributesUtils.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DelegatingOAuth2LogoutHandler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DelegatingProviderUserConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OAuth2GoogleProviderUserConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OAuth2KakaoOidcProviderUserConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OAuth2KakaoProviderUserConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OAuth2NaverProviderUserConverter.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ProviderUserRequest.java
 ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CacheType.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜SearchType.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SocialType.java
 ┃ ┃ ┃ ┃ ┣ 📂events
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ApplicationEventListener.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleDeletedEvent.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDeletedEvent.java
 ┃ ┃ ┃ ┃ ┣ 📂Interceptors
 ┃ ┃ ┃ ┃ ┃ ┣ 📂queryCounter
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ConProxyHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QueryCounter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QueryCounterAop.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QueryCounterInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExecutionTimeInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜FileCleanUpInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜WeatherInterceptor.java      
 ┃ ┃ ┃ ┃ ┗ 📜SetupData.java
 ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📂advice
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiControllerAdvice.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ViewControllerAdvice.java
 ┃ ┃ ┃ ┃ ┣ 📂api
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BlogApiController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentApiController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LocalFileController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜S3Controller.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜UserApiController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜UserVerifyApiController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜WeatherApiController.java      
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┗ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleSearchRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EditUserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailVerifyCodeRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FormAddUserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NewPasswordRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OAuthAddUserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PhoneNumberRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SmsVerifyCodeRequest.java
 ┃ ┃ ┃ ┃ ┣ 📂validator
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleSearchRequestValidator.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ConditionalValidator.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜NotBlankContentValidator.java
 ┃ ┃ ┃ ┃ ┗ 📂view
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BlogViewController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserViewController.java
 ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┣ 📜Article.java
 ┃ ┃ ┃ ┃ ┣ 📜ArticleImages.java
 ┃ ┃ ┃ ┃ ┣ 📜ArticleLikes.java
 ┃ ┃ ┃ ┃ ┣ 📜BaseEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜Comment.java
 ┃ ┃ ┃ ┃ ┗ 📜User.java
 ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┣ 📂duplicate
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DuplicateException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailDuplicateException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜NicknameDuplicateException.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PhoneNumberDuplicateException.java
 ┃ ┃ ┃ ┃ ┣ 📜CoordinateConvertException.java      
 ┃ ┃ ┃ ┃ ┣ 📜EmailSendException.java
 ┃ ┃ ┃ ┃ ┣ 📜ResponseStatusException.java
 ┃ ┃ ┃ ┃ ┣ 📜SmsException.java
 ┃ ┃ ┃ ┃ ┗ 📜VerificationException.java
 ┃ ┃ ┃ ┣ 📂mapper
 ┃ ┃ ┃ ┃ ┣ 📜ArticleMapper.java
 ┃ ┃ ┃ ┃ ┣ 📜CommentMapper.java
 ┃ ┃ ┃ ┃ ┣ 📜CommentResponseMapper.java
 ┃ ┃ ┃ ┃ ┣ 📜PasswordEncoderMapper.java
 ┃ ┃ ┃ ┃ ┗ 📜UserMapper.java
 ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┣ 📂social
 ┃ ┃ ┃ ┃ ┃ ┣ 📜GoogleUser.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoOidcUser.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoUser.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜NaverUser.java
 ┃ ┃ ┃ ┃ ┣ 📜Attributes.java
 ┃ ┃ ┃ ┃ ┣ 📜FormUser.java
 ┃ ┃ ┃ ┃ ┣ 📜OAuth2ProviderUser.java
 ┃ ┃ ┃ ┃ ┣ 📜PrincipalUser.java
 ┃ ┃ ┃ ┃ ┗ 📜ProviderUser.java
 ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┣ 📜ArticleImagesRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜ArticleLikesRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜BlogQueryRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜BlogRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜BulkInsertRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜CommentRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserQueryRepository.java
 ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleSearchServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EditUserServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FormAddUserServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NewPasswordServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OAuthAddUserServiceRequest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddArticleViewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleListViewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleViewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Item.java      
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LikeResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PageResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserInfoResponse.java
 ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┣ 📜FileService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LocalFileService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜S3FileService.java
 ┃ ┃ ┃ ┃ ┣ 📂oauth
 ┃ ┃ ┃ ┃ ┃ ┣ 📂unlink
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractOAuthUnlinkService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DelegatingOAuth2UnlinkHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoogleUnlinkService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoUnlinkService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NaverUnlinkService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OAuth2UnlinkService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractOAuth2UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomOAuth2UserService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomOidcUserService.java
 ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ArticleSecurity.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentSecurity.java
 ┃ ┃ ┃ ┃ ┣ 📂sms
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DevMessageService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MessageService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ProdMessageService.java
 ┃ ┃ ┃ ┃ ┣ 📜BlogService.java
 ┃ ┃ ┃ ┃ ┣ 📜CacheService.java
 ┃ ┃ ┃ ┃ ┣ 📜CommentService.java
 ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailsService.java
 ┃ ┃ ┃ ┃ ┣ 📜DuplicateCheckService.java
 ┃ ┃ ┃ ┃ ┣ 📜UserService.java
 ┃ ┃ ┃ ┃ ┣ 📜ValidationService.java
 ┃ ┃ ┃ ┃ ┣ 📜VerificationService.java
 ┃ ┃ ┃ ┃ ┗ 📜WeatherService.java      
 ┃ ┃ ┃ ┗ 📜BlogApplication.java
    </code>
  </pre>
</details>

<details>
  <summary>Frontend</summary>
  <pre>
    <code>
📦resources
 ┣ 📂static
 ┃ ┣ 📂images
 ┃ ┃ ┣ 📜comment.png
 ┃ ┃ ┣ 📜google.png
 ┃ ┃ ┣ 📜icon_like_off.png
 ┃ ┃ ┣ 📜icon_like_on.png
 ┃ ┃ ┣ 📜kakao.png
 ┃ ┃ ┣ 📜menu.png
 ┃ ┃ ┣ 📜naver.png
 ┃ ┃ ┣ 📜next.png
 ┃ ┃ ┣ 📜prev.png
 ┃ ┃ ┣ 📜rain.png
 ┃ ┃ ┣ 📜tmp.png      
 ┃ ┃ ┗ 📜views.png
 ┃ ┣ 📂js
 ┃ ┃ ┣ 📂article
 ┃ ┃ ┃ ┣ 📜article.js
 ┃ ┃ ┃ ┣ 📜articleMain.js
 ┃ ┃ ┃ ┣ 📜comment.js
 ┃ ┃ ┃ ┗ 📜like.js
 ┃ ┃ ┣ 📂login
 ┃ ┃ ┃ ┣ 📜screenToggle.js
 ┃ ┃ ┃ ┗ 📜validation.js
 ┃ ┃ ┣ 📂weather
 ┃ ┃ ┃ ┣ 📜app.js
 ┃ ┃ ┃ ┣ 📜pagination.js
 ┃ ┃ ┃ ┗ 📜weather.js      
 ┃ ┃ ┣ 📜editComment.js
 ┃ ┃ ┣ 📜loadPage.js
 ┃ ┃ ┣ 📜search.js
 ┃ ┃ ┣ 📜searchPageEvents.js
 ┃ ┃ ┗ 📜utils.js
 ┃ ┣ 📂style
 ┃ ┃ ┣ 📜500.css
 ┃ ┃ ┣ 📜article.css
 ┃ ┃ ┣ 📜articleList.css
 ┃ ┃ ┣ 📜footer.css      
 ┃ ┃ ┣ 📜loginPage.css
 ┃ ┃ ┣ 📜phone-number.css
 ┃ ┃ ┗ 📜signup.css
 ┃ ┗ 📜favicon.ico
 ┣ 📂templates
 ┃ ┣ 📂error
 ┃ ┃ ┣ 📜403.html
 ┃ ┃ ┣ 📜404.html
 ┃ ┃ ┗ 📜500.html
 ┃ ┣ 📂fragments
 ┃ ┃ ┣ 📜footer.html      
 ┃ ┃ ┗ 📜header.html
 ┃ ┣ 📜article.html
 ┃ ┣ 📜articleList.html
 ┃ ┣ 📜email-request.html
 ┃ ┣ 📜login.html
 ┃ ┣ 📜newArticle.html
 ┃ ┣ 📜oauthSignup.html
 ┃ ┣ 📜phone-number-request.html
 ┃ ┗ 📜userInfo.html
 ┗ 📜messages.properties
    </code>
  </pre>
</details>  

<details>
  <summary>설정 파일</summary>
  <pre>
    <code>
📦resources
 ┣ 📜application-dev.yml
 ┣ 📜application-local.yml
 ┣ 📜application-prod.yml
 ┣ 📜application-test.yml
 ┗ 📜application.yml
    </code>
  </pre>
</details>

## 아키텍쳐

![blog](https://github.com/user-attachments/assets/283fd49e-e568-4789-bd8e-a3e96174212b)

## DB

<img width="800" alt="image" src="https://github.com/user-attachments/assets/9cd21615-2430-410a-9d40-26fed2ce00ac">

# 5. API 명세서

[API 명세서](https://documenter.getpostman.com/view/29486061/2sAXqwYKR3)

# 6. 기능 소개

<details>
  <summary>사용자 관련 기능</summary>
  <h3>폼 회원가입</h3>
  <img src="https://github.com/user-attachments/assets/a3f66f32-68bb-4ac7-817b-2c1c82e1feac">
  <li>사용자 정보를 입력받아 기본적인 폼 회원가입을 시도합니다.</li>
  <li>중복된 정보나 유효성 검사에 실패할 경우 회원가입에 실패합니다.</li>

  <h3>소셜 회원가입</h3>
  <img src="https://github.com/user-attachments/assets/9d533cd5-d6c5-4e6d-b8a0-5e324cfad18a">
  <li>OAuth로 사용자의 기본 정보와 추가 정보를 받아 회원가입을 시도합니다.</li>
  <li>중복된 정보나 유효성 검사에 실패할 경우 회원가입에 실패합니다.</li>

  <h3>정보 수정</h3>
  <img src="https://github.com/user-attachments/assets/5059f7d8-5b16-49a0-8a21-a56d038d9401">
  <li>사용자의 닉네임과 프로필 이미지 정보를 수정합니다.</li>

  <h3>이메일 찾기</h3>
  <img src="https://github.com/user-attachments/assets/7d7e1b51-ba37-471d-ac1a-9012e158ad81">
  <li>전화번호를 입력받아 SMS 인증 서비스로 가입했던 이메일을 찾습니다.</li>

  <h3>비밀번호 재설정</h3>
  <img src="https://github.com/user-attachments/assets/5d63996d-9f9e-4d55-bfb8-13a2d247aad8">
  <li>이메일을 입력받아 이메일 인증 서비스로 비밀번호 재설정을 시도합니다.</li>
  <li>소셜 회원가입이 아닌 폼 회원가입 사용자만 비밀번호 재설정이 가능합니다.</li>
</details>

<details>
  <summary>게시글 관련 기능</summary>

  <h3>게시글 등록 및 수정</h3>
  <img src="https://github.com/user-attachments/assets/9961ff3f-2d70-427e-8725-8a92d955894d">
  <li>로그인된 사용자는 글을 등록할 수 있습니다.</li>
  <li>사진을 포함한 여러 가지 형태의 글을 등록할 수 있습니다.</li>

  <h3>게시글 특정 개수 조회</h3>
  <img src="https://github.com/user-attachments/assets/6bdb0c80-d038-40da-a238-85ec55a758ef">
  <li>한 페이지에 원하는 분량만큼 조회할 수 있습니다.</li>

  <h3>게시글 검색 조회</h3>
  <img src="https://github.com/user-attachments/assets/532cd630-3e67-492f-8aad-af1fee6a5945">
  <li>제목, 내용, 작성자, 제목 + 내용, 작성일로 특정 게시글을 검색할 수 있습니다.</li>

  <h3>좋아요 등록 및 취소</h3>
  <img src="https://github.com/user-attachments/assets/49d736b3-1646-467b-a312-8b2c9939b2a6">
  <li>특정 게시글에 좋아요를 누르고 취소할 수 있습니다.</li>

</details>

<details>
  <summary>댓글 관련 기능</summary>

  <h3>댓글 등록</h3>
  <img src="https://github.com/user-attachments/assets/901043ed-dcf3-4212-8973-6958c4f9068d">
  <li>로그인된 사용자는 게시글에 댓글을 등록할 수 있습니다.</li>
  <li>자신의 게시글이면 "작성자" 라는 표시가 보입니다.</li>

  <h3>댓글 수정 및 삭제</h3>
  <img src="https://github.com/user-attachments/assets/8ce03fc2-09b6-459c-9603-4b054de7026d">
  <li>등록했던 댓글을 수정하거나 삭제합니다.</li>
</details>

# 7. 고민하고 해결했던 내용들

### 1. 소셜 플랫폼 별 다른 API 추상화
- **고민 내용** : 구글, 카카오 등 OAuth2 소셜 플랫폼 별로 연결 끊기 요청에 대한 API가 모두 달라 **DIP**, **OCP**를 지키기 위한 추상화 방법을 고민
- **해결 과정** : 
  - **공통적인 부분과 다른 부분을 구분**해 **템플릿 메서드 패턴**으로 전체적인 구조를 추상화 할 수 있을 것 같다는 아이디어
  - 스프링 시큐리티의 내부 코드 방식을 참고하여 코드를 작성 
- **결과** : 단순 `if`문 대신 **고수준 모듈을 의존**하여 새로운 소셜 플랫폼이 추가되어도 코드 수정 없이 **유연하게** 대처가
          가능하도록 설계됨

### 2. 시스템 강결합 문제
- **고민 내용** : 서비스 레이어에서 DB 요쳥과 AWS S3 요청을 보내는 구조는 **강결합 문제**와 **데이터 불일치 현상**이 일어날 수 있는 문제에 대한 고민
- **해결 과정** : 
  - 데이터베이스 **트랜잭션이 정상적으로 커밋 되었을 때** AWS S3 요청을 보낼 수 있는 구조에 대해 고민
  - 스프링이 제공하는 **트랜잭션 이벤트 기능**을 사용하기로 결정
- **결과** : **스프링 트랜잭션 커밋 이벤트를 발행**하여 강결합 문제와 데이터 불일치 현상이 일어날 수 있는 문제를 개선

### 3. 파일 저장 최적화
- **고민 내용** : 파일을 리스트로 받아 서버에서 한 번에 저장하는 구조는 서버에 큰 부하를 발생시킬 수 있는 문제를 고민
- **해결 과정** : **Presigned URL**을 알게 되어 프로젝트에 도입
- **결과** : 서버 요청 처리 수를 **최소 50% 절감**하였고, 쿼리를 **N번에서 한번**을 사용하도록 개선

### 4. 날씨 데이터 최적화
- **고민 내용** : **페이지 새로고침마다** 공공데이터 API 요청을 보내는 구조를 최적화하기 위해 고민
- **해결 과정** : **스프링 캐싱 기능**을 사용해서 성능을 최적화하고, **스프링 스케줄링 기능**을 사용해서 사용자에게 최대한
  최신의 데이터만 보여지도록 결정
- **결과** : 캐시 TTL 시간 동안 서버 요청 처리 수 **N번에서 1번** 감소와 서버 **응답 시간 약 100% 감소**

### 5. 외부 API 요청 비동기 처리
- **고민 내용** : 소셜, SMS, 이메일 전송 API 요청 등의 **I/O 바운드 작업을 동기적으로** 처리하는 방식에 대해 고민
- **해결 과정** : 강의를 통해 학습한 **`CompletableFuture` 자바 동시성 프로그래밍** 개념을 도입하기로 결정
- **결과** : I/O 바운드 작업을 비동기로 처리하여 **응답 시간 약 34%** 개선