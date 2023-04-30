# 나눔광장

나눔광장은 지역 기반의 일회용품 재활용 서비스로서, 스티로폼이나 아이스팩 등 자주 발생하는 일회용품을 필요한 사람에게 전달하여 재사용이 가능하도록 지원하고, 더 나아가 일상생활 속 환경보호를 실천할 수 있도록 도움을 주는 웹서비스입니다.

## Build Guide

### Prerequisites

- IntelliJ or Gradle
- Node.js
- MariaDB

### Installing and build

#### Spring gradlew build

- Gradle

```shell
cd backend
./gradlew build
```

- IntelliJ
  
  인텔리제이에서는 `Run` 버튼을 누르면 자동으로 의존성 설치와 빌드가 진행됩니다.

#### Vue.js install

```shell
cd frontend
npm install
npm run build
```

#### Database 생성 및 비밀번호 입력

- `sharesquare` 데이터베이스 생성
- `application.properties`에 MariaDB 계정과 비밀번호 입력

## Run

- Gradle

```shell
cd backend
./gradlew run
```

- IntelliJ
  
  인텔리제이에서는 `Run` 버튼을 누르면 실행됩니다.

## Built With

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)

## Authors

* [**김성욱**](https://github.com/sori9899)

* [**배재원**](https://github.com/JaewonB37)

* [**이우창**](https://github.com/changi1122)
