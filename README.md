#카카오 로그인 구현하는 방법

1. 의존성 설정하기
[settings.gradle.kts]
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

[모듈 수준의 build.gradle]
  implementation "com.kakao.sdk:v2-all:2.20.1" // 전체 모듈 설치, 2.11.0 버전부터 지원
  implementation "com.kakao.sdk:v2-user:2.20.1" // 카카오 로그인 API 모듈
  implementation "com.kakao.sdk:v2-share:2.20.1" // 카카오톡 공유 API 모듈
  implementation "com.kakao.sdk:v2-talk:2.20.1" // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
  implementation "com.kakao.sdk:v2-friend:2.20.1" // 피커 API 모듈
  implementation "com.kakao.sdk:v2-navi:2.20.1" // 카카오내비 API 모듈
  implementation "com.kakao.sdk:v2-cert:2.20.1" // 카카오톡 인증 서비스 API 모듈

2. 아래 내용 AndroidManifest.xml에 넣기

<activity
            android:name="com.kakao.sdk.auth.AuthCodeHandlerActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <!-- Redirect URI: "kakao${NATIVE_APP_KEY}://oauth" -->
                <data android:host="oauth"
                    android:scheme="@string/kakao_oauth_host" />
            </intent-filter>
        </activity>



3. 인터넷 권한 허용하기
    <uses-permission android:name="android.permission.INTERNET" />




4. 디버그 키 해쉬
keytool -exportcert -alias androiddebugkey -keystore %USERPROFILE%\.android\debug.keystore -storepass android -keypass android | openssl sha1 -binary | openssl base64




5. Application 생성하기
-BuildConfig 에러
gradle.properties 파일에 다음 코드를 추가하면 해결된다.

android.defaults.buildfeatures.buildconfig=true
-
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig)
    }
}

6.local.properties
->아래 내용추가하기
KAKAO_NATIVE_APP_KEY=f0cce72055aa4391f2f1eb54eddc8924
kakao_oauth_host = kakaof0cce72055aa4391f2f1eb54eddc8924



-> androd 블락 위에 아래 코딩
import org.jetbrains.kotlin.konan.properties.Properties
val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

->defaultConfiit 블락 내부 아래 코딩
buildConfigField("String" , "KAKAO_NATIVE_APP_KEY" ,
            "\"${properties["KAKAO_NATIVE_APP_KEY"]}\""
        )
        resValue("string" , "kakao_oauth_host" , "\"${properties["kakao_oauth_host"]}\"")

->buildFeatures에 buildConfig 추가
buildFeatures {
        compose = true
        buildConfig = true
    }





7. ViewModel 생성
class KaKaoAuthViewModel(application : Application) : AndroidViewModel(application){

    private val context = application.applicationContext

    companion object {
        const val TAG = "KaKaoAuthViewModel"
    }

    fun handleKakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

}
