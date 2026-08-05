# 1. 빌드 스테이지
FROM bellsoft/liberica-openjdk-alpine:17.0.19-cds AS builder
WORKDIR /app

# 빌드에 필요한 Gradle 기본 환경 파일 설정 복사
COPY gradle ./gradle
COPY build.gradle settings.gradle gradlew ./

RUN chmod +x gradlew

# 소스 코드 복사
COPY src/ src/

# 외부 라이브러리 저장소(modules-2)만 캐시 마운트하여 빌드 수행
RUN --mount=type=cache,target=/root/.gradle/caches/modules-2 \
    ./gradlew bootJar -x test --no-daemon

# 빌드된 JAR 파일로부터 스프링 부트 레이어 구조 추출
RUN mkdir extracted && \
    java -Djarmode=layertools -jar build/libs/*.jar extract --destination extracted

# 2. 실행 스테이지
FROM bellsoft/liberica-openjre-alpine:17.0.19-cds
WORKDIR /app

# 컨테이너 내부에서 사용할 사용자 계정, 그룹 생성
RUN addgroup -S smolder && adduser -S smolder -G smolder

# 빌드 스테이지에서 추출된 레이어드 폴더 구조를 실행 스테이지로 순차 복사
COPY --from=builder --chown=smolder:smolder /app/extracted/dependencies/ ./
COPY --from=builder --chown=smolder:smolder /app/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=smolder:smolder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=smolder:smolder /app/extracted/application/ ./

RUN chown -R smolder:smolder /app
# 이후 실행할 명령어 계정 설정
USER smolder

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]