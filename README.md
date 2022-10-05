# Dialogflow-STT-TTS-Project
Google Dialogflow와 STT, TTS를 android에 연동한 앱

## 사용한 도구 
* Android Studio (Java)
* Google Dialogflow
* Google TTS API 
 (안드로이드 안에 무료로 TTS 내장API를 지원한다. 삼성핸드폰은 samsung TTS API를 지원한다.)

## Dialogflow챗봇 연동
<table>
  <tr>
    <td><b>1.왼쪽 상단의 톱니바퀴 누르고 나서 Project ID 클릭</b><img width="992" alt="20221005_213834" src="https://user-images.githubusercontent.com/115002427/194062301-cafdb713-263b-41b2-9d19-f90f50c02b63.png"></td>
  </tr>  
</table>    

<table>
  <tr>
    <td><b>2.서비스 계정 클릭</b><img width="737" alt="20221005_215627" src="https://user-images.githubusercontent.com/115002427/194065589-be958598-06b3-4436-a3dd-35196d627d22.png"></td>
  </tr>  
</table>   
      
<table>   
  <tr>
    <td><b>3.이메일 클릭 후 위쪽에 있는 키를 클릭
    </b><img width="830" alt="20221005_215841" src="https://user-images.githubusercontent.com/115002427/194066082-1479716e-adf5-458f-b966-6a744d090781.png"></td>
  </tr>
  
</table>  

<table>   
  <tr>
    <td><b>4.키 추가 - 새 키 만들기 - json파일로 받기
    </b><img width="806" alt="20221005_220110" src="https://user-images.githubusercontent.com/115002427/194066623-9e47de40-2442-483a-8353-0f3b4035e3ea.png"></td>
  </tr>
  
</table>  

<table>   
  <tr>
    <td><b>5. 안드로이드 스튜디오에서 res - raw 폴더에 집어넣기                                                                                                          
    </b></td>
  </tr>
  
</table>  

## 기능
* ## Speech To Text(STT)
  왼쪽 상단의 버튼 클릭 시 음성인식을 시작하여 음성인식 결과를 dialogflow Agent에 전달
* ## Text 입력
  상단의 editText에 원하는 쿼리를 입력하고 전송 버튼을 누르면 dialogflow Agent에 전달
* ## Text To Speech(TTS)
  dialogflow에서 준 답을 자동으로 읽어준다.
  가운데에 있는 버튼 클릭 시 ResponseText에 나온 글을 읽어준다.
  
## 음성 목소리 바꾸는 법
<table>
  <tr>
    <td><b>1.일반</b></td>
     <td><b>2.글자 읽어주기</b></td>
     <td><b>3.기본 엔진 옆 톱니바퀴</b></td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/115002427/194069837-46a35b57-47bf-4266-8edb-81d222a67b1e.png" width=270 height=480></td>
    <td><img src="https://user-images.githubusercontent.com/115002427/194072715-463f484f-91f4-4efa-ba5e-a429d7ec9dcb.jpg" width=270 height=480></td>
    <td><img src="https://user-images.githubusercontent.com/115002427/194073053-ed9925fc-6f49-4faf-afad-125e2bf9118a.jpg" width=270 height=480></td>
  </tr>
 </table>
 
## 사용 시
<table>
  <tr>
    <td><b>1.음성인식</b></td>
     <td><b>2.텍스트 입력</b></td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/115002427/194075105-06d7a964-bec7-4119-babb-049010af12b6.gif" width=270 height=480></td>
    <td><img src="https://user-images.githubusercontent.com/115002427/194072715-463f484f-91f4-4efa-ba5e-a429d7ec9dcb.jpg" width=270 height=480></td>
  </tr>
 </table>
    
  
    
     <img src="https://user-images.githubusercontent.com/115002427/194075105-06d7a964-bec7-4119-babb-049010af12b6.gif" width=270 height=480>
  



