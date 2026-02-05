# Heemo API Server 🌸

> **Relationship Management & Smart Date Curation Platform**  
> "Heemo"는 연인 간의 건강한 관계 유지와 데이터 기반의 스마트한 데이트 경험을 제공하는 서비스의 백엔드 시스템입니다.

---

## 📖 Project Overview

### Vision
현대 연인들이 겪는 관계 관리의 어려움을 IT 기술로 해결합니다. 감정 데이터 기반의 솔루션과 개인화된 데이트 큐레이션을 통해 더 깊은 유대감을 형성하는 것을 목표로 합니다.

### Project Focus
본 프로젝트는 **Spring Modulith** 아키텍처를 기반으로 설계되어, 서비스 초기 단계에서의 빠른 개발 속도와 추후 비즈니스 확장에 따른 유연한 대응력을 동시에 확보하는 데 중점을 두었습니다.

---

## 🛠 Tech Stack

### 🟦 Backend Core
*   **Kotlin 2.1.10**: 최신 문법과 강력한 코루틴 지원을 통한 생산성 향상.
*   **Spring Boot 3.4.2**: 최신 안정화 버전 및 생태계 라이브러리(SpringDoc 등) 최적화.
*   **Spring Data JPA** & **QueryDSL 5.1.0**: 타입 안전한 동적 쿼리 작성 및 데이터 접근 계층 추상화.
*   **Spring Modulith 1.3.1**: 도메인 기반 모듈화로 결합도(Coupling)를 낮춘 아키텍처 구현.

### 🟨 Security & Infrastructure
*   **OAuth2 + JWT**: Google, Kakao 소셜 로그인을 통한 간편 가입 및 Stateless 인증 시스템 구축.
*   **PostgreSQL 16**: 안정적인 관계형 데이터 관리.
*   **Swagger (SpringDoc 2.8.5)**: 프론트엔드 협업을 위한 API 문서 자동화 및 테스트 환경 제공.

---

## 🏗 System Architecture

### Modular Monolith
서비스의 각 기능을 독립적인 모듈로 정의하여 유지보수성을 극대화했습니다.

```text
com.yeonghoon.heemo
├── common       // 공통 컴포넌트 (Global Exception, Response DTO, Security)
├── auth         // 소셜 인증 및 토큰 관리 로직
├── user         // 사용자 프로필(MBTI, 생일 등) 및 권한 관리
└── [Domain]     // (확장 예정) Couple, Healing, DateCourse 등
```

---

## 🚀 Key Features (Current Implementation)

### 🔑 Advanced Authentication
*   **Social Login Integration**: 카카오 및 구글 OAuth2 인증을 통한 사용자 식별 및 자동 회원가입 프로세스.
*   **JWT Security**: 보안 정책에 따른 Stateless 토큰 인증 및 권한(Role) 관리.
*   **Fail-safe Nickname Generation**: 소셜 닉네임 부재 시 고유 ID 기반의 자동 임시 닉네임 부여 로직 구현.

### 🛡 Robust Infrastructure
*   **Global Exception Handling**: 전역 예외 처리기를 통해 표준화된 에러 응답(`ApiError`) 및 구체적인 에러 코드 제공.
*   **Documented Common Response**: 모든 API 응답을 `ApiResponse<T>`로 캡슐화하고 Swagger를 통해 명확히 명세화.
*   **Profile-based Configuration**: 운영(PROD) 환경의 보안을 위한 Swagger 자동 비활성화 등 환경별 최적화 설정.

### 📝 Developer Experience (DX)
*   **Integrated Swagger Auth**: Swagger UI 내에서 바로 JWT 인증 테스트가 가능한 **Authorize** 기능 통합.

---

## 📝 API Reference (Collaboration)

*   **Swagger UI (Local)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
*   **API Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ⚙️ How to Run

### Setup
1.  **Environment**: JDK 21 및 Docker(PostgreSQL)가 필요합니다.
2.  **Run**: `./gradlew bootRun` 명령어로 서버를 실행합니다.

---
**Developer**: Yeonghoon Mo ([GitHub](https://github.com/Yeonghoon-mo))  
**Organization**: Heemo Project
