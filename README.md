# ☕ 김도윤의 안드로이드 개발일기 (Jetpack Compose)

Jetpack Compose를 활용해 **UI 구성부터 상태 관리, 게임 로직 구현까지**  
매주 하나씩 실습하며 성장한 안드로이드 개발 기록입니다 🚀  

---

## 📚 Project 1 — 나만의 도서관 앱 (Library App)

> 🔍 **책 검색 & 내 서재 관리 앱**

### ✨ 주요 기능
- 책 제목 / 저자 / 출판사 검색 기능
- 책 상세 정보 확인 (출판사, 출판년도, ISBN)
- 내 서재에 책 추가 및 삭제
- TabRow를 이용한 화면 전환
- `State` / `mutableStateListOf` 를 활용한 상태 관리

### 🛠 사용 기술
- **Jetpack Compose**
- Material 3
- LazyColumn
- TabRow
- 상태 관리 (remember, mutableState)

### 📌 핵심 포인트
- 중복 ISBN 방지 로직 구현
- UI와 상태 로직 분리
- 실제 앱 구조에 가까운 구성

---

## 🎮 Project 2 — Compose 게임 모음 앱

> 🕹️ **Compose로 구현한 미니 게임 컬렉션**

### 🎯 포함된 게임
1. 🔢 숫자 맞추기  
2. 🔨 두더지 잡기  
3. 🃏 카드 짝맞추기  
4. ⭕ 틱택토  
5. 🧱 벽돌깨기  
6. 🐍 뱀 게임  

### ✨ 주요 기능
- LazyVerticalGrid 기반 게임 선택 메뉴
- 게임별 상태 관리 및 리셋 기능
- `LaunchedEffect` + `delay` 를 이용한 게임 루프 구현
- 터치 이벤트 및 방향 버튼 처리

### 🛠 사용 기술
- Jetpack Compose
- LazyVerticalGrid
- Canvas 개념 응용
- Coroutine (`LaunchedEffect`, `delay`)
- 랜덤 로직 (`Random`)

### 📌 핵심 포인트
- 게임별 독립적인 상태 관리
- 반복 UI 컴포넌트 재사용
- 로직 중심의 Compose 활용 연습

---

## 🗓️ 학습 기록

### 🗓️ Week 3 — Compose Coffee ☕
- Jetpack Compose 기본 UI 구성
- 버튼 & 이미지 배치 연습
- 첫 미니 프로젝트 완성

### 🗓️ Week 4 — 자기소개 앱 🙋‍♂️
- Text / Image 컴포넌트 활용
- 간단한 레이아웃 디자인
- 나를 표현하는 첫 앱 제작

### 🗓️ Week 5 — 스탑워치 앱 ⏱️
- 시간 측정 및 리셋 기능
- 상태(State) 관리 이해
- 앱 구조 설계 연습

### 🗓️ Week 6 — 버블게임 🎮
- Canvas 및 애니메이션 기초
- 좌표 기반 터치 이벤트 처리
- 게임 로직 설계 경험

---

✨ **매주 성장하는 안드로이드 개발자 김도윤의 기록**  
📅 다음 프로젝트도 계속 업데이트 예정입니다!
