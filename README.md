# todoapp
스파르타 내일배움캠프 Spring 숙련주차 개인과제

Spring 3기 B-4조 이예진

## 목차
[1. 프로젝트 소개](#프로젝트-소개)  
[2. 요구사항](#요구사항)  
[3. 추가 요구사항](#추가-요구사항)  
[4. 기술 스택](#기술-스택)  
[5. ERD](#erd)  
[6. API 명세](#API-명세)  
[7. 참조](#참조)

## 프로젝트 소개
회원가입, 로그인 기능이 있는 투두앱 백엔드 서버 만들기

## 요구사항
1. 회원가입 API
2. 로그인 API
3. 할일카드 작성 기능 API
4. 선택한 할일카드 조회 기능 API
5. 할일카드 목록 조회 기능 API
6. 선택한 할일카드 수정 기능 API
7. 할일카드 완료 기능 API
8. 댓글 작성 API
9. 댓글 수정 API
10. 댓글 삭제 API
11. ResponseEntity를 사용한 예외 처리

## 추가 요구사항
1. 완료된 카드는 숨김처리로 인해 조회할 수 없는 기능
2. 할일카드를 작성자만 볼 수 있는 비공개 기능
3. 할일카드를 제목으로 검색하여 목록을 출력하는 기능

## 기술 스택
**Server**: SpringBoot

## ERD
![todoapp drawio](https://github.com/dlwls423/todoapp/assets/99391320/4be51951-a1c7-4820-9ed9-b7cb95661160)

## API 명세
postman 명세: <https://documenter.getpostman.com/view/30859017/2s9YXnzyr7>

<br/>

**1. 회원가입**

    POST /api/user/signup

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|username|String|필수|사용자명|
|password|String|필수|비밀번호|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|statusCode|int|필수|http 상태 코드|
|msg|String|필수|상태 메시지|

<br/>

**2. 로그인**

    POST /api/user/login

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|username|String|필수|사용자명|
|password|String|필수|비밀번호|

**Response Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|statusCode|int|필수|http 상태 코드|
|msg|String|필수|상태 메시지|

<br/>

**3. 할일카드 작성 기능**

    POST /api/cards

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|title|String|필수|제목|
|content|String|필수|내용|
|privateCard|boolean|선택(기본값: false)|비공개 여부|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|cardId|Long|필수|할일카드ID|
|title|String|필수|제목|
|content|String|필수|내용|
|createdAt|LocatDateTime|필수|작성일|
|username|String|필수|작성자|


<br/>

**4. 선택한 할일카드 조회 기능**

    GET /api/cards/{cardId}

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|cardId|Long|필수|할일카드ID|
|title|String|필수|제목|
|content|String|필수|내용|
|createdAt|LocatDateTime|필수|작성일|
|username|String|필수|작성자|

<br/>

**5. 할일카드 목록 조회 기능**

    GET /api/cards

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response Syntax**

    {
      "robbie" : [
        {
            "cardId" : 1,
            "title" : "카드 제목",
            "createdAt" : "2023-11-15T20:22:53.002196",
            "username" : "robbie"
        }
      ],
      "lucy" : [
        {
            "cardId" : 2,
            "title" : "카드 제목",
            "createdAt" : "2023-11-15T20:22:53.002196",
            "username" : "lucy"
        },
        {
            "cardId" : 3,
            "title" : "카드 제목",
            "createdAt" : "2023-11-15T20:22:53.002196",
            "username" : "lucy"
        }
      ]
    }

<br/>

**6. 선택한 할일카드 수정 기능**

    PUT /api/cards/{cardId}

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|title|String|필수|제목|
|content|String|필수|내용|
|privateCard|boolean|선택(기본값: false)|비공개 여부|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|cardId|Long|필수|할일카드ID|
|title|String|필수|제목|
|content|String|필수|내용|
|createdAt|LocatDateTime|필수|작성일|
|username|String|필수|작성자|

<br/>

**7. 할일카드 완료 기능**

    PATCH /api/cards/{cardId}

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|statusCode|int|필수|http 상태 코드|
|msg|String|필수|상태 메시지|

<br/>

**8. 할일카드 제목 검색 기능**

    GET /api/cards/search?title=value

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**

    {
        {
            "cardId" : 1,
            "title" : "카드 제목",
            "createdAt" : "2023-11-15T20:22:53.002196",
            "username" : "lucy"
        },
        {
            "cardId" : 2,
            "title" : "카드 제목",
            "createdAt" : "2023-11-15T20:22:53.002196",
            "username" : "lucy"
        }
    }
    
<br/>

**9. 댓글 작성 기능**

    POST /api/cards/{cardId}/comments

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|content|String|필수|내용|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|commentId|Long|필수|댓글ID|
|content|String|필수|내용|
|username|String|필수|작성자|
|cardId|Long|필수|댓글이 달린 할일카드ID|

<br/>

**10. 댓글 수정 기능**

    PATCH /api/cards/{cardId}/comments/{commentId}

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Request**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|content|String|필수|내용|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|commentId|Long|필수|댓글ID|
|content|String|필수|내용|
|username|String|필수|작성자|
|cardId|Long|필수|댓글이 달린 할일카드ID|

<br/>

**11. 댓글 삭제 기능**

    DELETE /api/cards/{cardId}/comments/{commentId}

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|statusCode|int|필수|http 상태 코드|
|msg|String|필수|상태 메시지|

<br/>

**12. 댓글 조회 기능**

    GET /api/cards/{cardId}/comments

**Request Header**
|파라미터|타입|필수여부|설명|
|---|---|---|---|
|Authorivation|String|필수|JWT 토큰|

**Response**

    {
        {
            "commentId" : 1,
            "content" : "댓글 내용",
            "username" : "robbie",
            "cardId" : 1
        },
        {
            "commentId" : 2,
            "content" : "댓글 내용",
            "username" : "lucy",
            "cardId" : 1
        }
    }

## 참조
<https://velog.io/@hellozin/Valid-예외를-전역-컨트롤러로-간단하게-처리하기>  
<https://dnjsrud.tistory.com/192>  
<https://hongong.hanbit.co.kr/http-상태-코드-표-1xx-5xx-전체-요약-정리/>  
<https://velog.io/@kimdy0915/Spring-Security-Filter-예외처리는-어떻게-할까>  


