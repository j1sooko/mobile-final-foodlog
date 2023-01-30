# 모바일응용 최종 프로젝트: 푸드로그


## 📂 프로젝트 소개

**푸드로그**(식비&식단 관리 어플)

- 식단과 식비를 한 번에 기록
- 특정 장소에서 먹은 음식의 정보(식비, 열량, 사진, 메모)를 기록

## 📆 개발 기간
2021.12.22 Wed ~ 2021.12.26 Sun


## ⚙️ 개발 환경 및 기술
- Android Studio
- Java, SQLite


## 📌 주요 기능
1. 일별 기록 조회(총 금액과 열량 표시)
  - 뷰페이저와 탭 레이아웃 구현으로 좌우로 밀어서 페이지 전환
  - 리스트뷰 형태로 custom cursor adapter를 활용하여 DB를 불러옴
  - DatePickerDialog를 이용하여 달력 표시
  
2. 모든 기록 조회
  - 항목 롱클릭시 삭제, 항목 클릭시 수정
  
3. 월별기록 조회
  - '연도-월'로 분류하여 총 합산 금액과 열량 정보 확인
4. 기록 생성/상세조회/수정/삭제
  - 음식 이름, 가격, 시간대, 사진, 장소, 열량 정보 기록
  - floatingactionbutton, DatePickerDialog, spinner 사용, 외부 카메라 호출
  - LBS, 지오코딩, 구글맵으로 현재 위치 수신 하여 표시
  - 음식 열량 검색: 농총진흥청 국립농업과학원의 음식 정보 제공, 음식별 영양성분 제공 Open API 활용
  - XMLPullParser와 AsyncTask 사용
5. 알림 설정
  - option menu로 알림 설정/개발자 소개 화면을 띄움
  - Notification, AlarmManager, BroadcastReceiver 사용하여 매일 입력한 시간에 앱을 사용하도록 알림 생성
  - SharedPreferences를 사용하여 목표 칼로리 달성 알림 설정


## 구현 결과
1. 일별 기록 조회

![image](https://user-images.githubusercontent.com/70093771/215394769-f828cb25-4810-442d-bb00-832ce93bf648.png)

2. 모든 기록 조회/월별 기록 조회

![image](https://user-images.githubusercontent.com/70093771/215394794-2023d328-4d68-4f17-acaa-b25802580bc4.png)

3. 기록 생성/상세 조회/수정/삭제

![image](https://user-images.githubusercontent.com/70093771/215394833-d4c0c290-85bd-4245-9404-5b60097ccfa2.png)
![image](https://user-images.githubusercontent.com/70093771/215394867-37b0bb32-4483-46f2-9bf3-ea1124b80bd1.png)
![image](https://user-images.githubusercontent.com/70093771/215395031-de3b9eaf-5e95-4daa-a791-2eca975f4fad.png)
![image](https://user-images.githubusercontent.com/70093771/215395096-2a46497f-9153-40ac-b6ff-ba0a8cab3c48.png)

4.알림 설정/개발자 소개 화면

![image](https://user-images.githubusercontent.com/70093771/215395188-74d87eb7-eb8e-4acf-be56-53be02f97184.png)



## 개선 방향
1. 칼로리 검색 기능
  - 다른 활용할 수 있는 OpenAPI로 폭넓은 음식 검색
  - 음식의 중량 저장 기능
2. 사진 기능
  - 기존에 저장된 사진 불러오기
3. SNS 공유
  - 기록한 정보를 SNS(카카오톡, 블로그 등)로 공유

