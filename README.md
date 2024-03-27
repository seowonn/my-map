## 🗺️ My Map
### : 나의 방문 일지들로 만드는 나만의 지도

## ⭐ 활용 목적 
* 대상 : 기록하고 싶은 장소들로 지역별 나만의 일지를 만들고 싶은 사람들
* 목적 : 내가 방문한 장소들을 사진과 함께 기록하여 지역별로 나만의 맵을 만들 수 있다.
* 효과 :
  * 사용자는 자신만의 지역별 맵을 구축하여 하나의 일지로 사용할 수 있다.
  * 사용자는 검색을 통해 다른 사용자가 작성한 장소들을 볼 수 있고 참고할 수 있다.
  * 새로운 장소 발굴 + 검색을 통해 알게 된 장소들을 통해 지역별로 나만의 장소 및 인사이트를 얻을 수 있다.
# 
## 구성 ERD
![다운로드_waifu2x_photo_noise1](https://github.com/seowonn/my-map/assets/144876148/8bf878f2-2242-47bd-8508-f606108fe42f)
#
## 🙎‍♀ 회원
- [ ] 회원 가입
  - [ ] 회원은 사용자 - USER, 관리자 - ADMIN 의 권한(role)을 갖는다.
  - [ ] 회원 가입 시 아이디(이메일)는 이메일 인증을 통해 인증 절차를 거친다.
  - [ ] 아이디는 유일하며, 비밀번호는 암호화된 비밀번호가 저장된다.
- [ ] 회원 정보 조회
- [ ] 회원 정보 수정
  - [ ] 아이디, 비밀번호 수정 시 이메일 인증을 거친다.
   - [ ] 인증 시, 서버는 임의의 인증 코드를 전송해주고 동시에 redis에 저장된다.
   - [ ] 사용자가 입력한 인증 코드와 redis에 저장된 인증 코드를 비교하여 인증을 진행한다.
  - [ ] 전화번호 수정 시 전화번호 인증을 거친다.
- [ ] 회원 탈퇴
  - [ ] 회원 탈퇴 시, 해당 회원 정보는 익명으로 전환되고 작성된 방문일지들은 삭제되지 않는다.
#
### 🔐 로그인
- [ ] 아이디(이메일) & 비밀번호를 통해 로그인한다.
- [ ] 성공적으로 로그인한 사용자는 JWT 토큰을 발급 받는다.
#
### 🔑 비밀번호 재발급
- [ ] 이메일을 통하여 임의의 비밀번호를 발송해준다.
- [ ] 임의의 비밀번호를 통해 로그인 후, 회원 정보 수정을 통해 비밀번호를 변경한다.
#
## 🗺️ 마이맵
- [ ] 마이맵 등록
  - [ ] 사용자는 만들 마이맵의 지역 (시) 를 선택한다. 
  - [ ] 사용자는 마이맵 이름, 공개 여부를 작성하여 마이맵을 생성한다.
- [ ] 사용자의 마이맵 조회
  - [ ] 작성자는 등록한 최신순으로 마이맵의 항목들을 조회할 수 있다.
  - [ ] 마이맵을 검색한 사용자는 마이맵에 들어있는 방문 일지들을 조회할 수 있다.
    - [ ] 방문일지들의 나열 순서는 작성자의 방문 순번이 존재할 경우, 해당 순서로, 아닐 경우 최신 등록순으로 나열한다. 
- [ ] 마이맵 수정
  - [ ] 마이맵의 작성자만 공개 여부, 마이맵 제목을 수정할 수 있다.
- [ ] 마이맵 삭제
  - [ ] 작성자만 해당 마이맵을 삭제할 수 있고, 포함된 모든 방문일지들도 삭제 된다.  
#
### 🏡 지역 
- [ ] 관리자는 지역 정보를 추가, 수정, 삭제할 수 있다.
### 📗 방문일지
- [ ] 방문일지 작성
  - [ ] 사진(이미지 파일)을  최소 1개 ~ 10개 등록할 수 있다.
  - [ ] 지역은 정해진 틀(OO시 OO동)로 저장돼야 한다.
  - [ ] 방문일지는 마이맵의 하위 내용으로 지역의 더 구체적인 위치 구, 동을 작성하여 데이터를 추가하게 됩니다.
  - [ ] 사용자는 원하는 방문일지만 따로 공개 여부를 설정할 수 있고, 그 외는 마이맵의 공개 여부 설정을 따라가게 된다. 
- [ ] 좋아요, 조회한 사람
  - [ ] 좋아요는 로그인한 사용자 아이디 1명 당 1회 적립된다.
  - [ ] 조회수는 하루당 로그인한 사용자 아이디 1명 당 1회 적립된다.
#
### 🏷️ 즐겨찾기
  - [ ] 사용자는 (필요하다면) 즐겨찾기 제목을 설정하고 저장하고 싶은 방문일지를 추가할 수 있다.
  - [ ] 즐겨찾기 조회 순서는 등록순이다.
#
## 🔎 검색
### 🗳️ 검색 필터
* 검색 필터는 사용자가 기본적으로 선정한 검색어(지역)에 추가적으로 필터링할 항목을 의미한다.
- [ ] 관리자는 검색 필터를 추가, 삭제할 수 있다. (ex. 카페, 공원, 음식점, 아워맵)
- [ ] 검색어는 (우선) 지역과 카테고리 등 관리자가 지정한 필터로 설정, 방문일지들의 지역과 카테고리 항목만 검색 범위로 사용한다.
- [ ] 검색 결과는 검색 조건의 교집합인 방문일지들을 조회순으로 페이지 당 10개씩 나열해서 보여준다.
- [ ] 아워맵으로 검색할 경우, 공개처리된 마이맵들의 목록이 총 조회수 순으로 페이지당 10개씩 나열해서 나온다.
#
### 🗃️ 카테고리
- [ ] 방문일지 작성 시 사용자는 카테고리를 지정한다.
- [ ] 카테고리는 검색 필터와 연동되어 있다. 
#
---
### 추가해 볼 기능
* 검색 기능 구체화 및 보완 - 필터링 항목 뿐만 아니라 다양한 검색어 및 검색 범위를 넓혀서 활용하기
* 카카오 로그인 같은 간편 로그인 기능 도입 (OAuth 2.0)
* 활동명 미 설정 시, 랜덤명으로 설정해주기
* 조회수에 대한 개선된 측정 방법
