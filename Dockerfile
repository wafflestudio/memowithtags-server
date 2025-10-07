# Amazon Corretto 17을 기반 이미지로 사용
FROM amazoncorretto:17-alpine

# 애플리케이션을 담을 디렉토리 설정
WORKDIR /app

# 로컬의 빌드된 JAR 파일을 컨테이너 안으로 복사
# (빌드 과정에서 JAR 파일을 생성한다고 가정)
COPY build/libs/*jar /app/app.jar

RUN mkdir -p /app/logs

# 컨테이너가 시작될 때 실행될 명령어 설정
CMD ["java", "-jar", "/app/app.jar"]

EXPOSE 8080