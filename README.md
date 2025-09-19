# QURE — QR 시설 관리 서비스
<img width="1437" height="564" alt="image" src="https://github.com/user-attachments/assets/fc217e40-8ba0-4bdf-ab47-7cfd4347112c" />

> QR 태그로 **점검**과 **민원**을 한 플랫폼에서 처리하는 풀스택 웹앱.  
> 클라우드 3-Tier(웹/애플리케이션/DB) 구조, 모바일 현장 사용 최적화.

---

## 1) 개요
- 기간: 2025-09-03 ~ 2025-09-16
- 팀: 3조 SNAPSHOT (김민서, 구희원, 최온유, 최이서)
- 목적: 종이 점검표·전화 민원을 **디지털 전환**. 실시간 데이터로 **예방적 관리**와 **처리 속도** 향상.

### 차별점
- 관리자 **지도 기반** 통합 관리
- **점검 + 민원** 단일 플랫폼

### 스크린 샷

<img width="2558" height="1327" alt="image" src="https://github.com/user-attachments/assets/c5b3392c-d1c8-4691-a36d-d4c4fb1245cb" />

#### 로그인 화면

<img width="2533" height="1346" alt="image" src="https://github.com/user-attachments/assets/eaa28290-c04f-4ef2-abc5-b52239f5cb22" />

#### 대시보드 화면

<img width="1326" height="696" alt="image" src="https://github.com/user-attachments/assets/b79d5a92-12c7-4eb1-abf3-d87fecb72b12" />

#### 지도 화면





---

## 2) 주요 기능

### 관리자(Admin)
- **설비 관리**
  - 설비 등록/수정/삭제, 도메인(소방·순찰·미화) 분류, 상태/위치(GPS) 관리
  - 설비별 **QR 코드 발급·폐기** 라이프사이클, 일괄 발급
  - 검색/필터: 도메인, 설비명, 상태, 최근 점검일
- **점검 관리**
  - **템플릿 기반 체크리스트**: 항목 타입(BOOL/NUM/TEXT/IMAGE), 필수 여부, 가이드, 범위 검증(min/max/step)
  - **제출 검증**: 필수항목 누락 금지, 중복 제출 방지, 위치 일치 실패 시 반려
  - 이력/로그: 점검자, 시간, 위치, 결과 변경 추적
- **민원 관리**
  - 접수→처리중→완료 **워크플로** 상태 전환 및 코멘트 기록
  - 유형 분류, 첨부(이미지) 조회, 설비와 연계된 히스토리 확인
- **대시보드/리포트**
  - 오늘 예정 점검 **완료/전체 진행률**, 최근 10일 추이(클릭 드릴다운)
  - 미점검/지연 설비 카운트, 도메인별 분포
- **지도(시설 현황)**
  - **Kakao Map** 기반 GPS 마커, 클러스터링, 상태색상(정상/주의/이상)
  - 팝업: 설비명, 주소, 최근 점검일, 현재 상태, 상세 링크
- **인증/권한**
  - **카카오 OAuth2 + Spring Security**, 관리자 권한 구분
  - 세션/보안 헤더, HTTPS 전제(OCI)

### 점검자(Inspector)
- **QR 태그 진입 + PIN 검증**
  - 설비별 PIN 확인 후 체크리스트 접근 허용
- **모바일 체크리스트**
  - 템플릿 자동 로딩, 입력 유효성(필수/범위/형식) 즉시 검증
  - **사진 업로드 → AWS S3 저장**, 메타데이터 DB 연동
- **위치 기반 제출 보호**
  - **Geolocation API**로 현재 위치 획득
  - 설비 좌표와 허용 반경 비교 후 **불일치 시 제출 차단**
- **제출 신뢰성**
  - 이중 제출 방지 토큰, 실패 시 부분 롤백, 서버측 재검증

### 민원인(Citizen)
- **QR 기반 원터치 신고**
  - 설비 식별 자동 매핑, **유형 선택 + 상세 내용 입력**
- **첨부 및 연락**
  - 사진 첨부(S3), 선택적 이메일 기재
- **처리 피드백**
  - 접수 완료 안내, 상태 변경 알림(페이지 기준)

### 공통(플랫폼)
- **데이터 무결성**
  - 트랜잭션 처리, 필수/외래키 제약 준수, 서버측 최종 검증
- **감사 가능성**
  - 모든 중요 이벤트(로그인, 점검/민원 생성·수정) **감사 로그** 기록
- **확장성**
  - 3-Tier 구조(웹/Tomcat/Oracle), 정적 자산 분리, 외부 API 모듈화

---

## 3) 아키텍처
<img width="1659" height="922" alt="image" src="https://github.com/user-attachments/assets/bbdad673-68d1-4958-87be-69e7d2f07eb2" />

- 프런트: JSP/JSTL
- 백엔드: Spring MVC 6, Spring Security, MyBatis
- DB: Oracle Autonomous DB(OCI)
- 저장소: AWS S3
- 외부: Kakao Map, Kakao OAuth2
- 배포: OCI Compute(Tomcat, HTTPS)

---

## 4) 기술 스택
- Language/Build: Java 17, Maven, WAR
- Web: Spring Web MVC 6.0.23, JSP, JSTL, Servlet 6.0
- Security: Spring Security 6.0.5 + OAuth2 Client
- DB/Access: Oracle(OCI), ojdbc8 21.11, MyBatis 3.5.12, DBCP2
- JSON/Log: Jackson 2.14, Logback 1.5
- 특화: ZXing(QR), AWS SDK v2 S3, Lombok
- 협업/배포: GitHub, Figma, Jira, OCI Compute(Linux, Tomcat)

---

## 5) 팀과 역할
<img width="1588" height="588" alt="image" src="https://github.com/user-attachments/assets/1576f618-9e60-4059-824a-d6277332444f" />

| 팀명 | 이름 | 수행 역할 |
|---|---|---|
| SNAPSHOT | 최온유 | AWS S3 연동, Geolocation 위치검증, 모바일 점검/민원 제출 UI·구현, 민원관리 UI |
|  | 구희원 | 카카오 OAuth2 + Spring Security 회원관리, 점검자 관리 UI·구현, 로그인 UI |
|  | 최이서 | Chart.js 대시보드, 점검표 관리 UI, 점검 결과 페이지 |
|  | 김민서(팀장) | Jira 도입·운영, OCI HTTPS 배포(인증서/보안헤더), 설비/QR 관리 UI, Kakao Map, Oracle Cloud 연결 |

---

## 6) 성과 요약
- **점검·민원 통합** 웹앱 완성. 현장 QR → 즉시 입력 → 대시보드 집계.
- **OCI HTTPS 배포** 안정화. 외부 API 연동 보안 확인.
- **지도 기반 운영**과 **최근 10일 통계**로 관리 효율 향상.
- 브랜치·PR·빌드 검증 흐름 정착.

---

## 사전 요구사항 (Prerequisites)

- **운영체제**: Linux, macOS, Windows (Oracle Cloud 환경 권장)
- **Java**: JDK 17 (LTS)
- **빌드 도구**: Maven (최소 3.8.x 이상)
- **애플리케이션 서버**: Apache Tomcat (WAR 배포 지원 버전)
- **데이터베이스**: Oracle Database (Oracle Autonomous Database 권장)
- **패키지 관리자 / 연결 도구**
  - Git (저장소 클론 및 협업)
  - Oracle JDBC Driver (ojdbc8)
  - Oracle SQL Developer (DB 관리용, 선택적)
- **추가 요구사항**
  - 인터넷 연결 (외부 API 연동: 카카오맵, 카카오 OAuth2, 행안부 API 등)
  - AWS 계정 및 S3 접근 권한 (점검 이미지 및 첨부파일 저장용)

---

## 설치 (Installation)

1. **저장소 클론**
    ```bash
    # GitHub 저장소를 클론하고 디렉토리로 이동
    git clone https://github.com/sysone-3/qure.git
    cd qure
    ```

2. **환경 변수 설정**
   아래 예시 파일을 참고하여 `src/main/resources` 경로에 설정 파일을 생성하세요.  
   실제 키 값은 보안상 커밋하지 말고, `.gitignore`에 추가하는 것을 권장합니다.

   ### 1) application-db.properties
   - Oracle DB 연결 정보
   - AWS S3 버킷 정보
   - 서비스 도메인 설정

        ```properties
        db.url=jdbc:oracle:thin:@<DB_SERVICE_NAME>?TNS_ADMIN=<WALLET_PATH>
        db.user=<DB_USERNAME>
        db.pass=<DB_PASSWORD>
        db.driver=oracle.jdbc.OracleDriver

        aws.region=ap-northeast-2
        s3.bucket=<S3_BUCKET_NAME>
        aws.accessKeyId=<AWS_ACCESS_KEY>
        aws.secretAccessKey=<AWS_SECRET_KEY>

        app.public-domain=https://<DOMAIN_OR_IP>/qure
        ```

   ### 2) application-login.properties
        ```properties
        kakao.client-id=<KAKAO_CLIENT_ID>
        kakao.client-secret=<KAKAO_CLIENT_SECRET>
        ```

   ### 3) application-map.properties
        ```properties
        kakao.api.key=<KAKAO_API_KEY>
        kakao.app.key=<KAKAO_APP_KEY>
        ```

3. **의존성 설치**
    ```bash
    # Maven을 사용하여 필요한 라이브러리를 설치
    mvn clean install
    ```

4. **빌드 및 패키징**
    ```bash
    # Maven으로 WAR 파일 빌드
    mvn package
    # 생성된 WAR 파일은 /target 디렉토리에서 확인 가능
    ```

5. **애플리케이션 서버 배포**
   - Apache Tomcat 서버에 WAR 파일을 배포합니다.
   - 서버 실행 후 [http://localhost:8080](http://localhost:8080) 또는 설정된 도메인([https://qure.site](https://qure.site))으로 접속합니다.
   - 운영 환경에서는 Oracle Cloud Infrastructure(OCI) Compute Instance에 배포합니다.

6. **데이터베이스 초기화**
   - Oracle Autonomous Database를 사용합니다.
   - 초기화 스크립트를 실행하여 테이블을 생성합니다.  
     TODO: 실제 초기 스키마 파일 경로/이름 기입 필요

7. **애플리케이션 실행**
   - Tomcat이 정상적으로 기동되면 아래와 같이 접근 가능합니다:
     - 관리자 대시보드: 웹 브라우저(데스크톱) → [https://qure.site](https://qure.site)
     - 점검자 화면: QR 코드 스캔 후 모바일 브라우저
     - 민원인 화면: QR 코드 스캔 후 모바일 브라우저
