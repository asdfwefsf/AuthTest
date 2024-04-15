package com.company.authtest.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KaKaoAuthViewModel(application : Application) : AndroidViewModel(application){

    private val context = application.applicationContext

    companion object {
        const val TAG = "KaKaoAuthViewModel"
    }

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    fun kakaoLogin() {
        viewModelScope.launch {
            isLoggedIn.emit(handleKakaoLogin())

            suspendGetAuthInfo(userInfoList)

        }
    }
    private suspend fun handleKakaoLogin() : Boolean = 
        
        suspendCoroutine<Boolean> { continuation ->
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                    continuation.resume(false)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
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

    private val _userInfoList = MutableStateFlow<List<String>>(emptyList())
    val userInfoList = _userInfoList
    fun getAuthInfo() {
        viewModelScope.launch {
            suspendGetAuthInfo(_userInfoList)
        }
    }
    private suspend fun suspendGetAuthInfo(list : MutableStateFlow<List<String>>)  {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)

            }
            else if (user != null) {
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