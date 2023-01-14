insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member1@email.com', '{noop}asdf1234!@#$', 'shareholder_1', 2, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member2@email.com', '{noop}asdf1234!@#$', 'shareholder_2', 2, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member3@email.com', '{noop}asdf1234!@#$', 'shareholder_3', 2, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member4@email.com', '{noop}asdf1234!@#$', 'shareholder_4', 2, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member5@email.com', '{noop}asdf1234!@#$', 'shareholder_5', 3, 'ROLE_SHAREHOLDER', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('admin1@email.com', '{noop}asdf1234!@#$', 'admin1', 0, 'ROLE_ADMIN', now(), now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('admin2@email.com', '{noop}asdf1234!@#$', 'admin2', 0, 'ROLE_ADMIN', now(), now())
;

-- 안건 sample data
insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건1', '일주일에 1번의 회의를 진행하려고합니다.', 'RUNNING', 6, now(), now())
;

insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건2', '일주일에 2번의 회의를 진행하려고합니다.', 'RUNNING', 6, now(), now())
;

insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건3', '내년에 계획을 오늘 수립하는 것은 어떨까요.', 'PENDING', 6, now(), now())
;

insert into agenda (title, content, agenda_status, member_id, created_at, modified_at)
values ('안건4', '종료된 안건 입니다.', 'COMPLETED', 6, now(), now())
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

insert into issue (agenda_id, issue_type, issue_status, start_at, end_at, yes_count, no_count, give_up_count, created_at, modified_at)
values (4, 'NO_LIMITED', 'CLOSE', now(), date_add(now(), INTERVAL + 4 DAY), 2, 2, 3, now(), now())
;


-- 투표 sample data
insert into vote (vote_type, vote_count, issue_id, member_id, created_at)
values ('YES', 2, 4, 3, date_add(now(), INTERVAL + 1 DAY))
;

insert into vote (vote_type, vote_count, issue_id, member_id, created_at)
values ('NO', 2, 4, 4, date_add(now(), INTERVAL + 2 DAY))
;

insert into vote (vote_type, vote_count, issue_id, member_id, created_at)
values ('GIVE_UP', 3, 4, 5, date_add(now(), INTERVAL + 3 DAY))
;