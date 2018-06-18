# OSS-Team-8-Tetris

* GUI와 소켓통신을 활용한 멀티 테트리스 게임 *

1인모드와 2인모드로 구성되어 있으며, 2인 모드 선택 시 채팅이 가능하며, 상대방의 플레이화면을 볼 수 있습니다.

▶ 프로그램 주요 특성

* Swing을 이용한 GUI 환경 구축
   - Eclipse에서 편리하게 GUI 환경을 구축이 가능한 WindowBuilder를 활용
   - 내부클래스를 사용하여 버튼 액션리스너 동작 시에 다른 창이 보일 수 있도록 구축

  * 테트리스 게임 블록과 화면 구현을 위하여 Graphics 패키지 이용
   - 입체적인 테트리스 블록 사각형을 그리기 위해 fillRect(), drawRect(), drawLine()을 사용
   - 테트리스 맵의 그리드 선을 그려내기 위해 drawLine()을 사용
  
  * 테트리스 게임 화면 뿐만 아닌 hold와 next 화면을 넣어 보다 편리한 게임 방식으로 구현
   - 임시저장 공간인 hold에 현재 블록을 저장
   - random 메소드를 이용하여 다음에 올 블록 생성
   - ArrayList를 이용하여 다음에 올 테트리스 블록들을 미리 저장
   - 아래의 긴 next칸 → 작은 next칸 → 플레이화면 순으로 호출

  * 테트리스 게임을 방향키로 조정 가능
   - 키이벤트리스너를 사용하여 도형이 회전하거나 내려오도록 함
   - Shift - hold, Space - 바로 내려 보내기 기능 추가

  * 배열을 이용한 테트리스 맵 구현
   - 가로 10, 세로 21의 2차원배열로 잡고 맵 공간 할당
   - 게임 플레이 중 배열 칸 수를 체크하여 블록이 할당 된 공간에서 벗어나지 않도록 함
   - 블록의 줄이 지워질 경우, 지워진 줄의 위에 있는 블록들의 배열 값 변경

  * ArrayList를 사용하여 테트리스 게임 현황 체크 용이
   - 현재 배열에 존재하는 블록을 체크하여 ArrayList에 기록
   - 줄을 만들어 지울 경우 ArrayList에서 삭제
   - 현재 ArrayList에 존재하는 공간과 겹치지 않게 테트리스 블록이 쌓이도록 함

  * TCP통신을 이용한 채팅 구현
   - 서버와 클라이언트용 접속 구분
   - PrintWriter와 OutputStream, InputStream을 이용하여 채팅 송수신을 하도록 함
   - 클라이언트가 준비하기 버튼을 누를 시에 메시지를 전송하여 서버에게 준비상태 임을 알려주어
     게임을 시작할 수 있도록 함

  * UDP통신을 이용한 상대화면 출력
   - Robot 클래스를 활용하여 화면 캡처
   - ByteArrayOutputStream, ByteArrayInputStream, Datagrampacket을 사용하여 캡처한 이미지를
     송수신
   - 상대화면 부분은 Jlabel로 처리하여 ImageIcon형식으로 띄우도록 함 
  
  * 게임, 통신부 각각의 메소드를 스레드로 동작
   - 각 메소드 간 동작 충돌을 막기 위함

 * 각 파일 설명
  - TetrisBoard.java : 2인 모드 게임 코드
  - TetrisOnePlayer.java : 1인 모드 게임 코드
  - TetrisStart.java : 첫 시작 화면
  - com.tetris.blocks.Block : 테트리스 블록 생성 코드
  - com.tetris.blocks.TetrisBlock : 테트리스 좌표 코드
  - com.tetris.controller.TetrisController : 테트리스블록 이동 코드
  - com.tetris.shape 패키지 : 테트리스 블록 모양 코드
 