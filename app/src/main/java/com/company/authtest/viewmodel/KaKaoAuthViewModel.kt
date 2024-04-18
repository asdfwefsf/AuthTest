package com.company.authtest.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KaKaoAuthViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
//    private val dataStore = MyApp.instance.dataStore

//    val isLoggedIn = MutableStateFlow<Boolean>(false)
//    val LOGGED_IN_KEY = booleanPreferencesKey("LOGGED_IN")
    // DataStore 인스턴스 생성 : LOGIN이라는 이름을 가진 DataStore 인스턴스를 생성한거야.
    private val Context.dataStore by preferencesDataStore("LOGIN")

    companion object {


        const val TAG = "KaKaoAuthViewModel"

        val LOGGED_IN_KEY = booleanPreferencesKey("LOGGED_IN")
    }

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    // dataStore에서 LOGGED_IN_KEY 키에 isLoggedIn이라는 값을 넣어준다.
//    suspend fun saveLoginState(isLoggedIn: Boolean) {
//        context.dataStore.edit { LOGIN ->
//            LOGIN[LOGGED_IN_KEY] = isLoggedIn
//        }
//    }

    // dataStore에서 LOGGED_IN_KEY를 키값으로 갖는 값을 "읽어서" Flow 방출
//    val loggedInState: Flow<Boolean> = context.dataStore.data.map { LOGIN ->
//        LOGIN[LOGGED_IN_KEY] ?: false
//    }

//    init {
//        // 데이터스토어에서 로그인 상태를 구독하고, 값이 방출될 때마다 StateFlow 업데이트
//        loggedInState.onEach { isLoggedInValue ->
//            isLoggedIn.value = isLoggedInValue
//        }.launchIn(viewModelScope)  // 이 부분은 코루틴 스코프 내에서 실행되어야 함
//    }

    fun kakaoLogin() {
        viewModelScope.launch {
            // 로그인 시도 및 결과를 handleKakaoLogin 함수에서 받아옵니다.
            val loginResult = handleKakaoLogin()

            // 로그인이 성공했다면,
            if (loginResult) {
                // 로그인 상태를 저장합니다.
//                saveLoginState(true)

                // 사용자 정보를 불러오는 함수를 호출합니다.
                suspendGetAuthInfo(userInfoList)
            }

            // 로그인 결과에 따라 로그인 상태를 업데이트합니다.
            isLoggedIn.emit(loginResult)
        }
    }

    private suspend fun handleKakaoLogin(): Boolean =
        suspendCoroutine<Boolean> { continuation ->
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                    continuation.resume(false)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    viewModelScope.launch {
//                        saveLoginState(true)
                    }
                    continuation.resume(true)
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

    // 카카오 로그 아웃
    fun kakaoLogout() {
        viewModelScope.launch {
            if (handleKakaoLogout()) {
                isLoggedIn.emit(false)
            }
        }
    }

    private suspend fun handleKakaoLogout(): Boolean =
        suspendCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패 , SDK에서 토큰 삭제됨", error)
                    continuation.resume(false)
                } else {
                    Log.i(TAG, "로그아웃 성공 , SDK에서 토큰 삭제됨")
                    continuation.resume(true)

                }

            }
        }


    // 사용자 정보 반환 관련 ViewModel
    private val _userInfoList = MutableStateFlow<List<String>>(emptyList())
    val userInfoList = _userInfoList

    private suspend fun suspendGetAuthInfo(list: MutableStateFlow<List<String>>) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)

            } else if (user != null) {
                val userInfo = listOf(
                    "회원번호: ${user.id}",
                    "이메일: ${user.kakaoAccount?.email}",
                    "닉네임: ${user.kakaoAccount?.profile?.nickname}",
                    "프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                list.value = userInfo // MutableStateFlow의 value를 갱신합니다.
                Log.i(TAG, "사용자 정보 요청 성공\n${userInfo.joinToString(separator = "\n")}")

            }
        }
    }


}



