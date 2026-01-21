# --- Stage 1: Build ---
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# --- Stage 2: RunTime
FROM mcr.microsoft.com/playwright/java:v1.57.0-jammy

# Setting up Xvfb (if it is not installed)
RUN apt-get update && apt-get install -y xvfb

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN mkdir videos

EXPOSE 8080

# added --auto-servernum (auto select fake display)
ENTRYPOINT xvfb-run --auto-servernum --server-num=1 --server-args="-screen 0 1920x1080x24" java -jar app.jar