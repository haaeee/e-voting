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


## 요구사항

### API

* [ ] 안건(Agenda) 생성 및 삭제, 상태 변경
  * ROLE_ADMIN 을 가진 관리자만 가능

* [ ] 안건(`Agenda`) 조회
  * 모든 인가된 사용자는 조회가 가능하다.
  * Paging 처리


* [ ] 무제한 투표
  * Issue 테이블 issue_type: NO_LIMITED
  * Issue 테이블 선착순 갯수인 vote_count = 0 으로 설정
  * 시작 시간 종료 시간 사이에 투표가 가능하다.

* [ ] 선착순 투표
  * Issue 테이블 issue_type: LIMITED
  * Issue 테이블 선착순 갯수인 vote_count = 10 으로 설정
  * Vote 테이블에 기록을 남기고 vote_count를 차감하는 공유 자원, 이에 따라 `비관적 락`이 필요하다.
    * JPA (READ + COMMITTED + 낙관적 락): 레퍼런스 낙관적 락 예시에서 알 수 있듯이 `데드락`이 발생한다. (다대다 관계 테이블 + 공유 자원 (컬럼) 을 수정하기에)
  * 시작 시간 종료 시간 사이에 투표가 가능하다.


<details>
<summary>비관적 락</summary>
<div markdown="1">

JPA는 데이터베이스 트랜잭션 격리 수준을 READ COMMITTED 정도로 가정합니다.

만약 일부 로직에 더 높은 격리 수준이 필요하면 낙관적 락과 비관적 락 중 하나를 사용하면 됩니다.

이를 이해하기 위해서는 트랜잭션 격리 수준을 먼저 알아야합니다.

격리수준은 크게 4가지입니다.

- READ UNCOMMITTED
  - Commit 되지 않은 데이터를 읽을 수 있다.
  - Dirty Read 방지 (x), NonRepeatable read 방지 (x), Phantom Read 방지 (x)

- READ COMMITTED
  - Commit 된 데이터만 읽을 수 있다.

- **REPEATABLE READ** (✅)
  - Phantom Read가 발생할 수 있다
  - REPEATABLE READ는 변경(Update)는 제어하지만, INSERT는 제어할 수 없기 때문이다

- SERIALIZABLE
  - 모든 트랜잭션을 순서대로 실행한다.

> 발생할 수 있는 문제 <br/>
> - Dirty Read: `다른 트랜잭션에 의해 수정됐지만 아직 커밋되지 않은 데이터를 읽는 것`
> - Dirty Write: `같은 데이터에 동시에 두 개 이상의 트랜잭션이 값을 바꾸고자 함(갱신 분실)`
> - NonRepeatable Read: `동일 트랜잭션에서 동일한 대상을 여러번 읽을 때 그 사이에 수정 또는 삭제가 반영되어 값이 변경됨`
> - Phantom Read: `동일 트랜잭션에서 동일한 대상을 여러번 읽을 때 그 사이에 새로운 값(Phantom Tuple)이 삽입되어 값이 변경됨`


## Reference
- [발생할 수 있는 문제 레퍼런스](https://shorturl.at/iOSTU)
- [낙관적 락 예시](https://shorturl.at/hjl34)
- [비관적 락 예시](https://shorturl.at/hr246)
- [요약된 레퍼런스](https://modimodi.tistory.com/55)
</div>
</details>

* [ ]

### Security

---

* [x] 인증 및 인가
  * ROLE_SHAREHOLDER, ROLE_ADMIN