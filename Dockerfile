# 使用官方 Maven 镜像构建项目
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app

# 将项目的所有文件复制到容器中
COPY . .

# 使用 Maven 构建项目
RUN mvn clean package -DskipTests

# 使用官方 OpenJDK 运行时镜像
FROM openjdk:17-jdk-slim
WORKDIR /app

# 从构建阶段复制生成的 JAR 文件
COPY --from=builder /app/target/movie_verse_backend-0.0.1-SNAPSHOT.jar ./app.jar

# 暴露服务端口（例如 8080）
EXPOSE 8080

# 启动命令
CMD ["java", "-jar", "app.jar"]
