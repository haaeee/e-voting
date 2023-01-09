# 백그라운드 실행, 컨테이너 강제 재실행
# make db-up
# docker-compose-local.yaml : docker-compose up -d --force-recreate
db-up:
	docker-compose -f docker-compose-local.yaml up -d --force-recreate

# volume 삭제
db-down:
	# docker-compose down -v
	docker-compose -f .\docker-compose-local.yaml down -v