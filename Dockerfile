# Dùng Java 17
FROM openjdk:17-jdk-slim

# Tạo thư mục trong container
WORKDIR /app

# Copy file jar vào container
COPY target/*.jar app.jar

# Chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]