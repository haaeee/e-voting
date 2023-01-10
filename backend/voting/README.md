## 개발 환경

- Java 11
- Spring Boot 2.7.7
- Mysql:8.0.31

## 기술 세부 스택

- Spring Web
- Spring Data JPA
- MySQL Driver
- Spring Security (AccessToken 발급)
    - accessToken을 Frontend 발급 후 매번 요청 들어올 때마다 검증한다.
    - 검증된 유저는 Spring Security Context에 매번 주입한다.'
    - 이후 인가된 유저는 API를 이용한다.
    - `추후 Refactoring RefreshToken 및 Interceptor 와 Annotation 으로 로그인 유저 판별`
- Lombok

## ERD

![ERD](https://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/sandbox/jaime-e-voting/uploads/52b5388d9913fe198cf8ae75f3e159cc/image.png)