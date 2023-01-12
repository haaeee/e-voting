insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member1@email.com', '{noop}asdf1234!@#$', 'shareholder_1', 5, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member2@email.com', '{noop}asdf1234!@#$', 'shareholder_2', 7, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('admin1@email.com', '{noop}asdf1234!@#$', 'admin1', 0, 'ROLE_ADMIN', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('admin2@email.com', '{noop}asdf1234!@#$', 'admin2', 0, 'ROLE_ADMIN', now(), now())
;

-- 안건 sample data
insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건1', '일주일에 1번의 회의를 진행하려고합니다.', 'RUNNING', 3, now(), now())
;

insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건2', '일주일에 2번의 회의를 진행하려고합니다.', 'RUNNING', 3, now(), now())
;

insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건3', '내년에 계획을 오늘 수립하는 것은 어떨까요.', 'PENDING', 3, now(), now())
;



-- 현안(이슈) sample data
-- 열려 있는 현안 + 기간이 남음 + 선착순
insert into issue (agenda_id, issue_type, issue_status, start_at, end_at, yes_count, no_count, give_up_count, created_at, modified_at)
values (1, 'LIMITED', 'OPEN', now(), date_add(now(), INTERVAL 1 DAY), 0, 0, 0, now(), now())
;

-- 열려 있는 현안 + 기간이 남음 + 제한 없음
insert into issue (agenda_id, issue_type, issue_status, start_at, end_at, yes_count, no_count, give_up_count, created_at, modified_at)
values (2, 'NO_LIMITED', 'OPEN', now(), date_add(now(), INTERVAL 2 DAY), 0, 0, 0, now(), now())
;

-- 열려 있는 현안 + 기간이 지남 투표하려고하면 CLOSE로 issue_status 변경 및 agenda completed로
insert into issue (agenda_id, issue_type, issue_status, start_at, end_at, yes_count, no_count, give_up_count, created_at, modified_at)
values (3, 'NO_LIMITED', 'OPEN', now(), date_add(now(), INTERVAL -1 DAY), 0, 0, 0, now(), now())
;

