# SAP JCo Service

SAP JCo를 사용하여 RFC/BAPI 함수를 호출하는 Spring Boot 기반 REST API 서비스입니다.

## 📋 목차

- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [기술 스택 및 적용된 개선 사항](#기술-스택-및-적용된-개선-사항)
- [API 엔드포인트](#api-엔드포인트)
- [설정](#설정)
- [사용 예시](#사용-예시)
- [빌드 및 실행](#빌드-및-실행)
- [로깅](#로깅)
- [주의사항](#주의사항)
- [문제 해결](#문제-해결)

## 🚀 주요 기능

- **공통 API**: 모든 RFC/BAPI 함수를 하나의 엔드포인트로 호출
- **함수 검색**: 패턴 기반 함수 검색 및 목록 조회
- **메타데이터 조회**: BAPI/RFC 함수의 파라미터 구조 확인
- **함수 인터페이스 조회**: 상세한 함수 인터페이스 정보 제공
- **연결 상태 확인**: SAP JCo 연결 상태 모니터링
- **JSON 기반**: 모든 요청/응답이 JSON 형식
- **구조화된 로깅**: 상세한 로깅 및 모니터링

## 🏗️ 프로젝트 구조

```
src/main/java/com/basis/template/svcsapjco/
├── controller/                    # REST API 컨트롤러
│   ├── SapJcoExecutionController.java  # 함수 실행 API
│   └── SapJcoQueryController.java      # 함수 조회/검색 API
├── service/                       # 비즈니스 로직 서비스
│   ├── SapJcoService.java              # 함수 실행 오케스트레이션
│   ├── SapJcoFunctionDiscoveryService.java  # 함수 검색/인터페이스 조회 오케스트레이션
│   ├── SapJcoResponseBuilder.java      # 응답 DTO 조립
│   ├── SapJcoValidationService.java    # 요청 검증
│   ├── SapJcoFunctionExecutor.java     # 함수 실행기
│   ├── SapJcoFunctionBuilder.java      # 함수 빌더
│   ├── SapJcoConnectionManager.java    # 연결 관리
│   ├── RfcFunctionSearchExecutor.java  # RFC_FUNCTION_SEARCH 실행
│   ├── RfcFunctionInterfaceExecutor.java # RFC_GET_FUNCTION_INTERFACE 실행
│   ├── FunctionSearchResultMapper.java # 검색 결과 매핑
│   └── ParamsTableMapper.java          # PARAMS 테이블 → 인터페이스 정보 매핑
├── dto/                           # 데이터 전송 객체
│   ├── ApiResponse.java               # 공통 API 응답
│   ├── SapFunctionRequest.java        # 함수 실행 요청
│   ├── SapFunctionResponse.java       # 함수 실행 응답
│   ├── SapFunctionSearchResponse.java # 함수 검색 응답
│   ├── SapParameterMap.java           # 파라미터 맵
│   ├── SapTableData.java              # 테이블 데이터
│   ├── FunctionInfo.java              # 함수 정보
│   └── FunctionInterfaceInfo.java     # 함수 인터페이스 정보 (ParameterInfo 내부 클래스 포함)
├── config/                        # 설정 클래스
│   ├── SapJcoConfig.java             # SAP JCo 설정 (test 프로필 제외)
│   ├── SapDestinationDataProvider.java # Destination 프로퍼티 제공
│   └── WebConfig.java                # 웹 설정
├── util/                          # 유틸리티
│   ├── JCoResultExtractor.java       # JCo 결과(Export/Table) 추출
│   ├── RequestContext.java           # 요청 컨텍스트 (requestId, startTime)
│   ├── StructuredLogger.java         # 구조화된 로깅
│   ├── SapJcoDataUtil.java           # 데이터 변환
│   └── SapPatternConverter.java      # 검색 패턴 → SAP 와일드카드 변환
├── exception/                     # 예외 (SapJcoException 계층, GlobalExceptionHandler)
├── interceptor/                   # 로깅 인터셉터
├── constant/                      # 상수 정의
└── SvcSapjcoApplication.java      # 메인 애플리케이션
```

## 🛠️ 기술 스택 및 적용된 개선 사항

### 기술 스택

- **Java 21**, Spring Boot 3.3.x, SAP JCo 3
- 단일 책임 원칙(SRP)에 따른 역할 분리

### 적용된 개선 사항

- **결과 추출**: `JCoResultExtractor`로 Export/Table 추출 통합, 실패 시 예외 전파(`SapResultExtractionException`)
- **파라미터 설정**: 설정 실패 시 `SapParameterException` 발생 (기존 로그만 하던 동작 제거)
- **함수 검색/인터페이스**: `SapPatternConverter`, `RfcFunctionSearchExecutor`, `FunctionSearchResultMapper`, `RfcFunctionInterfaceExecutor`, `ParamsTableMapper`로 분리
- **요청 컨텍스트**: `RequestContext`(ThreadLocal)와 `StructuredLogger` 분리
- **설정**: `SapDestinationDataProvider` 별도 클래스로 분리, `SapJcoConfig`는 test 프로필에서 미로드
- **병렬 처리**: `sap.jco.response.parallel-table-conversion`으로 테이블 변환 시 parallel stream 옵션 제공
- **Virtual threads**: `spring.threads.virtual.enabled`로 가상 스레드 사용 가능 (SAP JCo 호환성 확인 후 활성화 권장)
- **예외**: 함수 검색/인터페이스 조회 실패 메시지 구체화, `GlobalExceptionHandler`에서 `getErrorCode()` 활용

## 🔌 API 엔드포인트

### 1. 함수 실행 API

#### 기본 함수 실행
```http
POST /api/sap/execute
Content-Type: application/json

{
  "functionName": "BAPI_USER_GET_DETAIL",
  "importParams": {
    "USERNAME": "HANRFC01"
  },
  "tables": {}
}
```

#### Import 파라미터만으로 실행
```http
POST /api/sap/execute/{functionName}
Content-Type: application/json

{
  "USERNAME": "HANRFC01"
}
```

#### 파라미터 없이 실행
```http
GET /api/sap/execute/{functionName}
```

### 2. 함수 검색 및 조회 API

#### 패턴으로 함수 검색
```http
GET /api/sap/functions/search?pattern=BAPI_*&groupName=SPACE&language=KO
```

**응답 예시:**
```json
{
  "success": true,
  "data": {
    "pattern": "BAPI_*",
    "groupName": "SPACE",
    "language": "KO",
    "functions": [
      {
        "functionName": "BAPI_USER_GET_DETAIL",
        "groupName": "SPACE",
        "application": "SAP",
        "host": "SAP",
        "description": "사용자 상세 정보 조회"
      }
    ],
    "count": 1,
    "timestamp": 1756187575938
  }
}
```

#### 함수 인터페이스 조회
```http
GET /api/sap/functions/{functionName}/interface
```

**응답 예시:**
```json
{
  "success": true,
  "data": {
    "functionName": "BAPI_USER_GET_DETAIL",
    "interface": {
      "functionName": "BAPI_USER_GET_DETAIL",
      "importParameters": {
        "fieldCount": 2,
        "hasFields": true,
        "fieldNames": ["CACHE_RESULTS", "USERNAME"]
      },
      "exportParameters": {
        "fieldCount": 13,
        "hasFields": true,
        "fieldNames": ["ADDRESS", "ADMINDATA", "ALIAS", ...]
      },
      "tableParameters": {
        "fieldCount": 23,
        "hasFields": true,
        "fieldNames": ["ACTIVITYGROUPS", "ADDCOMREM", ...]
      },
      "changingParameters": {
        "fieldCount": 0,
        "hasFields": false,
        "fieldNames": []
      }
    }
  }
}
```

## ⚙️ 설정

### 환경 변수

다음 환경 변수를 설정하거나 `application.yml`에서 직접 수정할 수 있습니다:

```yaml
spring:
  threads:
    virtual:
      enabled: ${SPRING_THREADS_VIRTUAL_ENABLED:false}  # Java 21 가상 스레드 (선택)

sap:
  jco:
    # SAP 시스템 연결 설정
    ashost: ${SAP_ASHOST:192.168.230.239}
    sysnr: ${SAP_SYSNR:01}
    client: ${SAP_CLIENT:100}
    user: ${SAP_USER:HANRFC01}
    passwd: ${SAP_PASSWD:Rfc01mm}
    lang: ${SAP_LANG:KO}
    
    # 연결 풀 설정
    pool:
      capacity: ${SAP_POOL_CAPACITY:10}
      peak-limit: ${SAP_POOL_PEAK_LIMIT:20}
      
    # 타임아웃 설정
    timeout:
      connection: ${SAP_CONNECTION_TIMEOUT:60000}
      
    # 트레이스 설정
    trace:
      enabled: ${SAP_TRACE_ENABLED:true}
    
    # 응답 테이블 변환 시 병렬 처리 (대량 테이블 시 유리)
    response:
      parallel-table-conversion: ${SAP_PARALLEL_TABLE_CONVERSION:false}
```

### 필수 환경 변수

- `SAP_ASHOST`: SAP 서버 호스트
- `SAP_SYSNR`: SAP 시스템 번호
- `SAP_CLIENT`: SAP 클라이언트
- `SAP_USER`: SAP 사용자명
- `SAP_PASSWD`: SAP 비밀번호

## 📝 사용 예시

### 1. 함수 검색

```bash
# BAPI 함수 검색
curl -X GET "http://localhost:8080/api/sap/functions/search?pattern=BAPI_*" | jq .

# 커스텀 함수 검색
curl -X GET "http://localhost:8080/api/sap/functions/search?pattern=Z*" | jq .

# RFC 함수 검색
curl -X GET "http://localhost:8080/api/sap/functions/search?pattern=RFC_*" | jq .
```

### 2. 함수 인터페이스 확인

```bash
# BAPI_USER_GET_DETAIL 함수의 파라미터 구조 확인
curl -X GET "http://localhost:8080/api/sap/functions/BAPI_USER_GET_DETAIL/interface" | jq .

# ZFI_BALANCE_LIST 함수의 파라미터 구조 확인
curl -X GET "http://localhost:8080/api/sap/functions/ZFI_BALANCE_LIST/interface" | jq .
```

### 3. 함수 실행

```bash
# 사용자 정보 조회
curl -X POST "http://localhost:8080/api/sap/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "functionName": "BAPI_USER_GET_DETAIL",
    "importParams": {
      "USERNAME": "HANRFC01"
    }
  }' | jq .

# 재무 잔액 조회
curl -X POST "http://localhost:8080/api/sap/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "functionName": "ZFI_BALANCE_LIST",
    "importParams": {
      "COMPANYCODE": "HS01",
      "CUSTOMER": "103090",
      "KEYDATE": "20250825"
    }
  }' | jq .

# Import 파라미터만으로 실행
curl -X POST "http://localhost:8080/api/sap/execute/BAPI_USER_GET_DETAIL" \
  -H "Content-Type: application/json" \
  -d '{"USERNAME": "HANRFC01"}' | jq .

# 파라미터 없이 실행
curl -X GET "http://localhost:8080/api/sap/execute/BAPI_USER_GET_DETAIL" | jq .
```

## 🔧 빌드 및 실행

### 시스템 요구사항

- Java 21 이상
- Gradle 7.x 이상
- SAP JCo 3 라이브러리

### 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 단위 테스트 실행 (test 프로필 사용, SAP 연결 없이 컨텍스트 로드 검증)
./gradlew test

# 애플리케이션 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
java -jar build/libs/svc-sapjco-all-0.0.1-SNAPSHOT.jar
```

### Docker 실행 (선택사항)

```bash
# Docker 이미지 빌드
docker build -t sap-jco-service .

# Docker 컨테이너 실행
docker run -p 8080:8080 \
  -e SAP_ASHOST=192.168.230.239 \
  -e SAP_SYSNR=01 \
  -e SAP_CLIENT=100 \
  -e SAP_USER=HANRFC01 \
  -e SAP_PASSWD=Rfc01mm \
  sap-jco-service
```

## 📊 로깅

### 로깅 설정

```yaml
logging:
  level:
    com.basis.template.svcsapjco: DEBUG
    com.sap.conn.jco: DEBUG
    org.springframework.web: DEBUG
```

### 구조화된 로깅

`RequestContext`에 요청 ID·시작 시각을 두고, `StructuredLogger`에서 로그 메시지/필드를 구성합니다.

- API 요청/응답 로그
- 함수 실행 시작/완료/실패 로그
- 함수 검색·인터페이스 조회 로그
- 검증 실패·예외 로그

### 로그 확인

```bash
# 애플리케이션 로그 확인
tail -f logs/application.log

# SAP JCo 트레이스 확인
tail -f logs/sapjco.log
```

## ⚠️ 주의사항

1. **보안**: SAP 연결 정보는 환경 변수로 관리하세요
2. **성능**: 대용량 데이터 처리 시 메모리 사용량을 모니터링하세요
3. **에러 처리**: 모든 API 호출에 적절한 에러 처리를 구현하세요
4. **로깅**: 운영 환경에서는 DEBUG 레벨 로깅을 비활성화하세요
5. **연결 풀**: 동시 요청이 많은 경우 연결 풀 설정을 조정하세요

## 🔍 문제 해결

### 일반적인 오류

1. **연결 실패**
   - SAP 서버 정보와 인증 정보를 확인하세요
   - 네트워크 연결 상태를 확인하세요
   - 방화벽 설정을 확인하세요

2. **함수 없음**
   - 함수명이 정확한지 확인하세요
   - 함수 검색 API로 사용 가능한 함수를 확인하세요

3. **파라미터 오류**
   - 함수 인터페이스 API로 파라미터 구조를 확인하세요
   - 필수 파라미터가 누락되지 않았는지 확인하세요

### 디버깅

```bash
# 상세 로그 확인
tail -f logs/application.log

# SAP JCo 트레이스 확인
tail -f logs/sapjco.log

# 애플리케이션 상태 확인 (Actuator 사용 시)
curl -X GET "http://localhost:8080/api/health" | jq .
```

### 성능 최적화

1. **연결 풀 설정 조정**
   ```yaml
   sap:
     jco:
       pool:
         capacity: 20
         peak-limit: 50
   ```

2. **타임아웃 설정 조정**
   ```yaml
   sap:
     jco:
       timeout:
         connection: 30000
   ```

## 📚 지원하는 함수 타입

- **BAPI (Business Application Programming Interface)**: 비즈니스 로직 함수
- **RFC (Remote Function Call)**: 원격 함수 호출
- **Custom Functions**: 사용자 정의 함수 (예: ZFI_BALANCE_LIST)

## 🔗 주요 BAPI 함수 예시

### 사용자 관리
- `BAPI_USER_GET_DETAIL`: 사용자 상세 정보 조회
- `BAPI_USER_CREATE`: 사용자 생성
- `BAPI_USER_CHANGE`: 사용자 정보 변경

### 자재 관리
- `BAPI_MATERIAL_GET_DETAIL`: 자재 상세 정보 조회
- `BAPI_MATERIAL_SAVEDATA`: 자재 생성/수정

### 고객 관리
- `BAPI_CUSTOMER_GETDETAIL`: 고객 정보 조회
- `BAPI_CUSTOMER_CREATE`: 고객 생성

### 회계 관리
- `BAPI_ACC_DOCUMENT_POST`: 회계 전표 생성
- `BAPI_GL_ACC_GETDETAIL`: G/L 계정 정보 조회

### 재고 관리
- `BAPI_GOODSMVT_CREATE`: 재고 이동 생성

## 📄 라이선스

이 프로젝트는 내부 사용을 위한 프로젝트입니다.

## 🤝 기여

프로젝트 개선을 위한 제안이나 버그 리포트는 언제든지 환영합니다.
