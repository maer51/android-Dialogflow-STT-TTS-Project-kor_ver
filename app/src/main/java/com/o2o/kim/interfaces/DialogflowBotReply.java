package com.o2o.kim.interfaces;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface DialogflowBotReply {

    void callback(DetectIntentResponse returnResponse);
}
