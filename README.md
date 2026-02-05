# Heemo API Server 🌸

> **Couple Healing & Date Course Service Backend**  
> 자주 다투는 연인을 위한 화해 솔루션 및 스마트 데이트 코스 추천 서비스

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Spring Modulith](https://img.shields.io/badge/Spring%20Modulith-Applied-green?style=flat-square)

## 📖 Project Overview

**Heemo(희모)**는 연인 간의 건강한 관계 회복과 즐거운 데이트 경험을 돕기 위해 설계된 웹 애플리케이션 서비스의 백엔드 시스템입니다.
단순한 정보 제공을 넘어, AI 기반 감정 분석을 통한 중재 기능과 게이미피케이션(캐릭터 성장) 요소를 결합하여 사용자의 지속적인 참여를 유도합니다.

본 프로젝트는 **확장성**과 **유지보수성**을 최우선으로 고려하여 **Spring Modulith** 기반의 모듈형 모놀리스 아키텍처로 설계되었습니다.

## 🚀 Key Features

*   **Relationship Recovery (Healing)**
    *   AI 기반 감정 분석 및 '백기(White Flag)' 푸시 알림 시스템
    *   갈등 상황별 맞춤형 사과 문구 생성 (OpenAI API 연동 예정)
*   **Smart Date Planner**
    *   위치 기반(GIS) 데이트 코스 검색 및 동선 최적화
    *   커플 공유 투두리스트 (Real-time Sync)
*   **Gamification**
    *   활동 기여도(Relationship Point)에 따른 커플 캐릭터 육성 시스템
    *   화해 및 미션 성공 시 보상 지급 로직

## 🛠 Tech Stack & Decision Making

### Backend
*   **Language**: `Kotlin 2.1.10` (JDK 21) - 강력한 코루틴 지원과 Null Safety를 통한 안정성 확보.
*   **Framework**: `Spring Boot 3.4.2` - 최신 안정화 버전 활용. (기존 4.0.2-SNAPSHOT에서 라이브러리 호환성 이슈로 인해 3.4.2로 다운그레이드 및 최적화 진행)
*   **Database**: `PostgreSQL` - 복잡한 연관 관계 및 공간 데이터(PostGIS) 확장을 고려.
*   **ORM**: `Spring Data JPA` + `QueryDSL` - 타입 안전한(Type-safe) 동적 쿼리 작성 및 컴파일 타임 오류 감지.
*   **Architecture**: `Spring Modulith 1.3.1` - 도메인 간 결합도를 낮추고, 추후 마이크로서비스(MSA) 전환이 용이한 구조 채택.
*   **Documentation**: `Swagger (SpringDoc 2.8.5)` - 프론트엔드와의 원활한 협업을 위한 API 문서 자동화.

### Infrastructure (Planned)
*   **CI/CD**: GitHub Actions
*   **Deploy**: AWS EC2 (Docker), RDS

## 🏗 Architecture (Spring Modulith)

본 프로젝트는 도메인 주도 설계(DDD) 원칙을 따르며, Spring Modulith를 통해 패키지 간 순환 참조를 방지합니다.

```text
com.yeonghoon.heemo
├── common       // 전역 공통 유틸리티 (Error Handling, BaseEntity)
├── couple       // 커플 매칭 및 관계 관리 도메인
├── healing      // 화해 및 AI 분석 도메인
├── datecourse   // 데이트 장소 및 코스 도메인
└── notification // 알림 처리 (Async Event Handling)
```

## ⚙️ Getting Started

### Prerequisites
*   JDK 21

### Run Application

\`\`\`bash
./gradlew clean build
java -jar build/libs/Heemo-API-0.0.1-SNAPSHOT.jar
\`\`\`

## 📝 API Documentation

서버 실행 후 아래 주소에서 API 명세를 확인할 수 있습니다.
*   Swagger UI: `http://localhost:8080/swagger-ui.html`

---
**Developer**: Yeonghoon Mo  
**Contact**: [GitHub Profile Link]