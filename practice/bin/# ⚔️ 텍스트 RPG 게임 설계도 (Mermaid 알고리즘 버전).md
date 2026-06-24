# ⚔️ 텍스트 RPG 게임 설계도 (Mermaid 알고리즘 버전)

본 설계도는 자바의 객체 지향 특징과 방어적 프로그래밍 기법을 녹여낸 터미널 기반 턴제 RPG 게임의 핵심 흐름도입니다.

---

## 📊 1. 전체 게임 프로세스 흐름도

MD 파일에서 Mermaid를 지원하는 환경(VS Code 미리보기, GitHub 등)이라면 아래 코드가 자동으로 예쁜 그래픽 흐름도로 변환됩니다.

```mermaid
flowchart TD
    Start([▶ 게임 시작: 플레이어 생성]) --> Init[목표 설정: 전진 5번]
    Init --> LoopStart{🔄 게임 루프 <br> 상태 == PLAYING?}
    
    LoopStart -- YES --> PrintMenu[행동 메뉴 출력 <br> 1.전진 / 2.휴식 / 3.포기]
    LoopStart -- NO --> JudgeResult{🏆 최종 결과 판정}

    PrintMenu --> InputTry[⌨️ 사용자 입력 유도]
    
    InputTry --> TryCatch{🛡️ try-catch 감시 <br> 숫자가 맞는가?}
    TryCatch -- ❌ 문자가 들어옴 --> ExceptionHandler["숫자만 입력하세요" 출력 <br> 버퍼 비우기]
    ExceptionHandler --> PrintMenu

    TryCatch -- ⭕ 숫자 확인 --> SwitchBranch{선택 분기 처리}
    
    SwitchBranch -- 1. 전진 --> Move[진행도 증가 ++ <br> 확률로 몬스터 조우]
    SwitchBranch -- 2. 휴식 --> Heal[플레이어 체력 회복 함수 호출]
    SwitchBranch -- 3. 포기 --> GiveUp[게임 상태 = GAMEOVER]
    SwitchBranch -- 그 외 숫자 --> WrongNum["잘못된 번호" 출력]

    Move --> CheckStatus{🛡️ 진행도 & 체력 검사}
    Heal --> CheckStatus
    GiveUp --> CheckStatus
    WrongNum --> CheckStatus

    CheckStatus -- 목표 달성 --> SetVictory[게임 상태 = VICTORY]
    CheckStatus -- 체력 0 이하 --> SetGameOver[게임 상태 = GAMEOVER]
    CheckStatus -- 아직 진행 중 --> LoopStart

    SetVictory --> LoopStart
    SetGameOver --> LoopStart

    JudgeResult -- 상태 == VICTORY --> DisplayVictory[🎉 던전 탈출 성공 출력]
    JudgeResult -- 상태 == GAMEOVER --> DisplayGameOver[💀 게임 오버 출력]
    
    DisplayVictory --> End([🏁 게임 종료])
    DisplayGameOver --> End