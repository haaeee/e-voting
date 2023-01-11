insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member1@email.com', '{noop}asdf1234!@#$', 'shareholder_1', 5, 'ROLE_SHAREHOLDER', now(),  now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('member2@email.com', '{noop}asdf1234!@#$', 'shareholder_2', 7, 'ROLE_SHAREHOLDER', now(),  now())
;

insert into member (email, password, nickname, vote_right_count, role, created_at, modified_at)
values ('admin1@email.com', '{noop}asdf1234!@#$', 'admin1', 0, 'ROLE_ADMIN', now(),  now())
;
