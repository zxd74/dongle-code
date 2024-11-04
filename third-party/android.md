# 数据转换
## TextToSpeech(TTS)

1. 配置manifest，选择TTS引擎(示例为原生引擎`android.intent.action.TTS_SERVICE`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.dongle.demo">

    <!-- application -->

    <queries> 
        <intent>
            <!-- 若未配置，则报警告：W/TextToSpeech: speak failed: not bound to TTS engine -->
            <action android:name="android.intent.action.TTS_SERVICE"/>
        </intent>
    </queries>
</manifest>
```
2. 实例化TTS
```kotlin
// 建议单例模式
var tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
    if (status == TextToSpeech.SUCCESS) {
        tts.setLanguage(Locale.CHINA)
        tts.setPitch(1.5f);
        tts.setSpeechRate(0.5f);
    }
})
```
```java
final TextToSpeech textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(Locale.CHINESE);
            textToSpeech.setPitch(1.5f);
            textToSpeech.setSpeechRate(0.5f);
        }
    }
});
```
3. tts配置：所有设置都有返回值(int)，**大于或等于0代表成功，小于0代表失败**
   * `setLanguage` 设置语言或地区
   * `setPitch` 设置音调（**大于1.0为女声，小于1.0为男声**）
   * `setSpeechRate` 设置语速
4. 文字转语音适用
```java
    tts.speak("你好", TextToSpeech.QUEUE_FLUSH, null, null)
```
5. 注意
   * 更换其它**引擎**需要先下载引擎
   * 指定语言时需确保**语言包**已下载