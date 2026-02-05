# Heemo API Server 🌸

> **Relationship Management & Smart Date Curation Platform**  
> "희모(Heemo)"는 연인 간의 건강한 관계 유지와 데이터 기반의 스마트한 데이트 경험을 제공하는 모바일 퍼스트 서비스의 백엔드 시스템입니다.

---

## 📖 Project Overview

### Problem Statement
현대 연인들은 잦은 갈등 해결의 어려움과 매번 반복되는 데이트 코스 선택의 피로감을 겪고 있습니다. 

### Solution: Heemo
*   **Healing Solution**: AI 기반의 감정 분석과 '백기(White Flag)' 시스템을 통해 갈등 상황에서 부드러운 화해의 계기를 제공합니다.
*   **Smart Curation**: 사용자의 취향, 위치, 과거 데이터를 기반으로 최적의 데이트 동선과 장소를 추천합니다.
*   **Modular Architecture**: 급변하는 요구사항에 유연하게 대응하기 위해 **Spring Modulith** 기반의 모듈형 구조를 채택하였습니다.

---

## 🛠 Tech Stack

### 🟦 Framework & Language
*   **Kotlin 2.1.10** (JDK 21)
*   **Spring Boot 3.4.2**
*   **Spring Data JPA** & **QueryDSL 5.1.0** (Type-safe Dynamic Query)
*   **Spring Modulith 1.3.1** (Modular Monolith)

### 🟨 Security & Infrastructure
*   **Spring Security** & **OAuth2 Client** (Google, Kakao)
*   **JWT (Json Web Token)**: Stateless Authentication
*   **PostgreSQL 16**: Relational Database
*   **Redis**: Refresh Token & Cache Layer (Planned)

---

## 🏗 System Architecture

### Modular Monolith (Spring Modulith)
서비스의 복잡도가 증가해도 유지보수가 용이하도록 **도메인 중심의 모듈화**를 적용했습니다. 각 모듈은 루트 패키지를 통해서만 노출되며, 직접적인 참조 대신 **Spring Events**를 통한 느슨한 결합(Loose Coupling)을 지향합니다.

```text
com.yeonghoon.heemo
├── common       // 공통 컴포넌트 (Exception Handling, Response DTO, Security Utils)
├── auth         // 소셜 로그인 처리 및 토큰 발급 로직
├── user         // 사용자 개인 정보 및 프로필 관리
├── couple       // 커플 매칭 시스템, 연결 이력(History) 및 기념일 관리
└── notification // [Event Consumer] 외부 플랫폼 알림 전송 (Push, SMS)
```

---

## 🚀 Key Implementation Details

### 1. Robust Couple Matching System
*   **Invite Flow**: UUID 기반의 유니크한 초대 코드를 통해 보안성 높은 매칭 프로세스 구현.
*   **Strict Business Rules**: 한 커플당 최대 2명만 연결되도록 쿼리 레벨에서 검증 로직 적용.
*   **Audit History**: 연결 해제 시 `tb_couple_history`에 데이터를 이관하여 비즈니스 분석 데이터 확보 및 데이터 파편화 방지.

### 2. Event-driven Domain Communication
*   모듈 간의 직접적인 서비스 호출을 지양하고 `ApplicationEventPublisher`를 활용.
*   커플 연결/해제 시 이벤트를 발행하여 알림 모듈이 비동기적으로 처리할 수 있는 구조 설계.

### 3. Type-safe Database Layer
*   복잡한 조인 및 필터링 쿼리(예: 관리자용 이력 조회)를 **QueryDSL**로 구현하여 컴파일 타임에 오류를 발견하고 가독성을 극대화.

---

## 📝 API Reference

프론트엔드 개발자의 원활한 개발을 위해 자동화된 Swagger 문서를 제공합니다.

*   **Swagger UI (Local)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
*   **API Docs (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> **💡 Integration Tip for Frontend**
> 1.  OAuth2 로그인을 통해 `accessToken`을 발급받습니다.
> 2.  Swagger UI 상단의 **Authorize** 버튼을 클릭하여 토큰을 입력합니다.
> 3.  이후 모든 요청 헤더에 `Authorization: Bearer {token}`이 자동으로 포함됩니다.
> 4.  **PROD 환경**에서는 보안을 위해 Swagger UI 접근이 차단됩니다.

---

## ⚙️ Development Guide

### Prerequisites
*   Java 21 / Kotlin 2.1.10
*   Docker (PostgreSQL, Redis 가동용)

### Application Setup
\`\`\`bash
# 1. Clone the repository
git clone https://github.com/Heemo-Project/Heemo-API.git

# 2. Configure Environment Variables (.env or application.yaml)
# GOOGLE_CLIENT_ID / KAKAO_CLIENT_ID 등 필요

# 3. Build & Run
./gradlew bootRun
\`\`\`

---
**Lead Developer**: Yeonghoon Mo ([GitHub](https://github.com/Yeonghoon-mo))  
**Project Repository**: [Heemo-Project/Heemo-API](https://github.com/Heemo-Project/Heemo-API)