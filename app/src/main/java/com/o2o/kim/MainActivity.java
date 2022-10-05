package com.o2o.kim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

import com.airbnb.lottie.LottieAnimationView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import com.o2o.kim.interfaces.DialogflowBotReply;
import com.o2o.kim.utils.SendMessageInBg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DialogflowBotReply, TextToSpeech.OnInitListener {

    //ui component
    private TextToSpeech tts;       //TTS 변수 선언
    private Button btn_Speak;

    private TextView textView;
    private EditText editText;
    private Button button;
    private LottieAnimationView voiceButton;

    //dialogFlow
    private SessionsClient sessionsClient; // 세션 클라이언트
    private SessionName sessionName; //세션 이름
    private String uuid = UUID.randomUUID().toString(); //식별자
    private String TAG = "mainactivity"; //Tag 명


    private String command = "default";

    final int PERMISSION = 1;
    RecognitionListener listener;
    SpeechRecognizer mRecognizer;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.responseText);
        editText = findViewById(R.id.editText1);
        button = findViewById(R.id.button1);
        voiceButton = findViewById(R.id.voice_btn);
        tts = new TextToSpeech(this, this);
        btn_Speak = findViewById(R.id.btnSpeak);


        btn_Speak.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)    //LOLLIPOP이상 버전에서만 실행 가능
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });

        //퍼미션 체크( 인터넷, 오디오 )
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        //RecognizerIntent 객체 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko_KR");


        //button 클릭 리스너 등록
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceButton.playAnimation();
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });

        //listener 생성
        listener = new RecognitionListener() {

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onResults(Bundle results) {
                // TODO Auto-generated method stub
                // 아래 코드는 음성인식된 결과를 ArrayList로 모아옵니다.
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result="";
                // 이후 for문으로 textView에 setText로 음성인식된 결과를 수정해줍니다.
                for (int i = 0; i < matches.size(); i++) {
                    result=matches.get(i);
                }
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                voiceButton.cancelAnimation();

                //command 를 default로 설정
                setCommand("default");
                if (!result.isEmpty()) {
                    //메시지를 담아 sendMessageToBot() 호출
                    sendMessageToBot(result);
                }
            }
                @Override
                public void onReadyForSpeech (Bundle params){
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPartialResults (Bundle partialResults){
                    // TODO Auto-generated method stub

                }

                @Override
                public void onEvent ( int eventType, Bundle params){
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError ( int error){
                    // TODO Auto-generated method stub
                    String message;

                    switch (error) {
                        case SpeechRecognizer.ERROR_AUDIO:
                            message = "오디오 에러";
                            break;
                        case SpeechRecognizer.ERROR_CLIENT:
                            message = "클라이언트 에러";
                            break;
                        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                            message = "퍼미션 없음";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK:
                            message = "네트워크 에러";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                            message = "네트웍 타임아웃";
                            break;
                        case SpeechRecognizer.ERROR_NO_MATCH:
                            message = "찾을 수 없음";
                            break;
                        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                            message = "RECOGNIZER가 바쁨";
                            break;
                        case SpeechRecognizer.ERROR_SERVER:
                            message = "서버가 이상함";
                            break;
                        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                            message = "말하는 시간초과";
                            break;
                        default:
                            message = "알 수 없는 오류임";
                            break;
                    }

                    Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
                    voiceButton.cancelAnimation();

                }

                @Override
                public void onEndOfSpeech () {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onBufferReceived ( byte[] buffer){
                    // TODO Auto-generated method stub

                }

                @Override
                public void onBeginningOfSpeech () {
                    // TODO Auto-generated method stub

                }
            };




        button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v){
                //command 를 default로 설정
                setCommand("default");

                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    //UI or something to do Task
                    editText.setText("");
                    //메시지를 담아 sendMessageToBot() 호출
                    sendMessageToBot(message);
                } else {
                    Toast.makeText(MainActivity.this, "보낼 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            });





            //dialogflowAgent key정보 Setup
            setUpBot();
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null)  {   // 사용한 TTS객체 제거
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)   // LOLLIPOP이상 버전에서만 실행 가능
    private void speakOut() {

        tts.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
        //textview에 있는 글자를 읽음
        //진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {    // OnInitListener를 통해서 TTS 초기화
        if (status == TextToSpeech.SUCCESS)  {
            int result = tts.setLanguage(Locale.KOREA);  // TTS언어 한국어로 설정

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btn_Speak.setEnabled(true);
                speakOut();   // onInit에 음성출력할 텍스트를 넣어줌
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


        //credential(GoogleService 자격 증명서) 파일을 통해 session 설정
        private void setUpBot () {
            try {
                InputStream stream = this.getResources().openRawResource(R.raw.chatbotgibd7c1726d902b9);
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
                String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

                SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
                SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                        FixedCredentialsProvider.create(credentials)).build();
                sessionsClient = SessionsClient.create(sessionsSettings);
                sessionName = SessionName.of(projectId, uuid);

                Log.d(TAG, "projectId : " + projectId);
            } catch (Exception e) {
                Log.d(TAG, "setUpBot: " + e.getMessage());
            }
        }

        //dialogflow로 message를 보내는 메서드
        private void sendMessageToBot (String message){
            QueryInput input = QueryInput.newBuilder()
                    .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
            new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
        }

        @Override
        public void callback (DetectIntentResponse returnResponse){
            //dialogflowAgent와 통신 성공한 경우
            if (returnResponse != null) {
                String dialogflowBotReply = returnResponse.getQueryResult().getFulfillmentText();
                Log.d("text", returnResponse.getQueryResult().toString());

                new Handler().postDelayed(new Runnable()   //통신 성공 후 읽어준다
                {
                    @Override
                    public void run()
                    {
                        speakOut();
                    }
                }, 600);  //0.6초 딜레이

                //getFulfillmentText가 있을 경우
                if (!dialogflowBotReply.isEmpty()) {
                    //UI or something to do Task
                    switch (command) {
                        default:
                            //scenarioTextView.setText(scenarioTextView.getText()+"\n답장: "+dialogflowBotReply);
                            textView.setText(dialogflowBotReply);
                            break;
                    }

                } else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
            }
        }

        public void setCommand (String command){
            this.command = command;
        }

    }