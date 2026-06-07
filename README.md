## WorkManager — 일해조 BACKEND
> 사장님과 알바생을 위한 통합 업장 관리 플랫폼의 백엔드 서버입니다. 스케줄 관리, 대타 신청, 실시간 채팅, AI 기반 업무 추천 및 대타 조정을 제공합니다.
<img width="70%" height="50%" alt="image" src="https://github.com/user-attachments/assets/0b71852d-42cb-4fe7-8888-5f3cbe627508" />

## SW Architecture
<img width="70%" height="70%" alt="image" src="https://github.com/user-attachments/assets/be8cc38a-8ceb-4fa7-9c6b-ed3bd27df845" />

### 도메인 설계
비즈니스 도메인 단위로 패키지를 분리하고, 각 도메인 내부를 레이어로 구성했습니다.         

```text
                                                                                                                                                                       
  com.example.workmanager                                                                                                                                              
  ├── member/       # 회원 및 인증                                                                                                                                     
  ├── store/        # 가게 및 직원 관리                                                                                                                                
  ├── schedule/     # 고정 스케줄 / 실제 근무 스케줄                                                                                                                   
  ├── substitute/   # 대타 신청 / 수락 / 거절                                                                                                                          
  ├── chat/         # 실시간 채팅                                                                                                                                      
  ├── todo/         # 매장 공유 TODO                                                                                                                                   
  ├── ai/           # AI 기능 (채팅→TODO 추출, 대타 후보 정렬)                                                                                                         
  └── global/       # 공통 인프라 (보안, 예외 처리, 응답 형식)
```                                                                                                
                                                                                                                                                                       
  각 도메인은 아래 레이어로 구성됩니다.   

```text
                                                                                                                                                                       
  {domain}/                                                                                                                                                            
  ├── domain/           # 엔티티, 레포지토리 인터페이스, 도메인 예외                                                                                                   
  ├── application/      # 서비스, DTO (request/response)                                                                                                               
  ├── infrastructure/   # 외부 연동                                                                                                                                    
  └── presentation/     # 컨트롤러, Swagger Docs 인터페이스
```                                                                                                       
                                                                                                                                                                       
  > Swagger 어노테이션은 `*ControllerDocs.java` 인터페이스에만 작성해                                                                                                  
  > 컨트롤러 코드와 API 문서를 분리했습니다.

## 기술 스택
[![My Skills](https://skillicons.dev/icons?i=java,spring,aws,docker,githubactions,mysql&perline=3)](https://skillicons.dev)

## 주요 기능

- **인증**: JWT AccessToken + RefreshToken (Redis 저장, 자동 재발급)
- **스케줄 관리**: 고정 근무 패턴(요일 기반) + 실제 근무 스케줄 분리 관리
- **대타 신청**: 대타 가능 시간 등록 → AI 후보 추천 → 요청 전송 → 수락/거절
- **실시간 채팅**: WebSocket + STOMP, 가게 단체 채팅방 + 1:1 DM 자동 생성
- **AI TODO**: 채팅 내용에서 할 일 자동 추출 (GPT-4o-mini)
