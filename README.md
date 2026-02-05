# Heemo API Server 🌸

> **Couple Healing & Date Course Service Backend**  
> 자주 다투는 연인을 위한 화해 솔루션 및 스마트 데이트 코스 추천 서비스

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Spring Modulith](https://img.shields.io/badge/Spring%20Modulith-Applied-green?style=flat-square)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5.1.0-blue?style=flat-square)

## 📖 Project Overview

**Heemo(희모)**는 연인 간의 건강한 관계 회복과 즐거운 데이트 경험을 돕기 위해 설계된 백엔드 시스템입니다. 
단순한 정보 제공을 넘어, **Spring Modulith**를 활용한 모듈형 구조와 **이벤트 기반 통신**을 통해 확장성 있는 아키텍처를 지향합니다.

---

## 🛠 Tech Stack & Technical Decisions

### 1. Backend Core
*   **Kotlin 2.1.10**: 최신 문법과 강력한 코루틴 지원을 통해 비동기 처리 성능 최적화.
*   **Spring Boot 3.4.2**: 라이브러리 호환성(SpringDoc 등) 및 안정성을 고려하여 최신 3.x 버전 채택.
*   **Spring Modulith**: 도메인 간의 결합도를 낮추고 비즈니스 로직의 독립성을 보장하는 **Modular Monolith** 아키텍처 적용.

### 2. Database & ORM
*   **PostgreSQL 16**: 관계형 데이터의 안정성과 PostGIS 확장성을 고려한 선택.
*   **QueryDSL 5.1.0**: 복잡한 동적 쿼리를 타입 안전하게 작성하고, 가독성 낮은 JPA 메소드 네이밍 문제를 해결.

### 3. Security & Auth
*   **OAuth2 + JWT**: Google, Kakao 소셜 로그인을 통한 간편한 가입 및 JWT 기반의 Stateless 인증 시스템 구축.
*   **Role-based Access Control**: 일반 사용자(USER)와 관리자(ADMIN) 권한 분리.

---

## 🚀 Key Features (Implemented)

### 💑 Couple Management (핵심 도메인)
*   **초대 코드 기반 매칭**: UUID 기반의 고유 초대 코드를 생성하고, 이를 통해 연인과 연결되는 시스템.
*   **2인 제한 검증**: 커플 도메인의 비즈니스 규칙에 따라 한 커플당 최대 2명만 연결되도록 엄격한 검증 로직 적용.
*   **연결 해제 및 이력 관리**: 커플 해제 시 데이터 파편화를 방지하고, `tb_couple_history`를 통해 과거 연결 이력을 영구 보존.
*   **D-Day 및 기념일**: 기념일 자동 설정 및 사귄 날짜 계산 로직 구현.

### 🔔 Event-driven Architecture
*   **Spring Events 활용**: 커플 연결/해제 시 이벤트를 발행하여 알림 모듈 등 타 도메인과의 결합도를 제거.

### 👮 Admin Statistics
*   **이력 필터링 조회**: 관리자 전용 페이지를 위한 기간별(StartDate, EndDate) 커플 해제 이력 조회 API 제공.

### 📝 API Documentation (Swagger)
*   **Security Integration**: Swagger UI에서 바로 JWT 인증을 테스트할 수 있는 **Authorize** 기능 통합.
*   **Schema Documentation**: 공통 응답 포맷(`ApiResponse`)의 명확한 문서화.
*   **환경별 최적화**: 운영(PROD) 환경에서는 보안을 위해 자동으로 스웨거 비활성화.

---

## 🏗 Architecture (Modular Monolith)

Spring Modulith의 규칙에 따라 각 모듈은 루트 패키지를 통해서만 외부와 소통합니다.

```text
com.yeonghoon.heemo
├── common       // 공통 유틸리티, 시큐리티 설정, 공통 DTO
├── auth         // OAuth2, JWT 인증 로직 (CustomOAuth2UserService)
├── user         // 사용자 프로필 및 권한 관리
├── couple       // 커플 매칭, 이력 관리, 기념일 로직
└── notification // 알림 처리 (Event Listener 기반)
```

---

## ⚙️ How to Run

### Environment Variables
다음 환경 변수가 설정되어야 합니다:
*   `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
*   `KAKAO_CLIENT_ID`, `KAKAO_CLIENT_SECRET`

### Run
\`\`\`bash
./gradlew clean build
java -jar build/libs/Heemo-API-0.0.1-SNAPSHOT.jar
\`\`\`

---
**Developer**: Yeonghoon Mo  
**GitHub**: [https://github.com/Yeonghoon-mo](https://github.com/Yeonghoon-mo)
