class MyBroadcastReceiver : BroadcastReceiver() {
  
  private var callDirection = CallDirection

  override fun onReceive(context: Context, intent: Intent) {

        when (intent.action){
            TelephonyManager.ACTION_PHONE_STATE_CHANGED ->{
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                
                callDirection.onPhoneStateChange(intent)

                if(intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                    ==TelephonyManager.EXTRA_STATE_RINGING){

                  // when ringing... do something... 

                }else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                    ==TelephonyManager.EXTRA_STATE_IDLE) {

                   
                    callDirection.isIncomingCall()

                    // when finished call... do something... 
          

                }

            }
            else -> {
                return
            }
        }
    }

    object CallDirection {
        private var IS_INCOMING_CALL: Boolean = false
        //
        private var prevState = TelephonyManager.EXTRA_STATE_IDLE
        private var currentState = TelephonyManager.EXTRA_STATE_IDLE

        /**
         * intent로 수신/발신 상태를 기록, isIncomingCall()로 반환
         */
        fun onPhoneStateChange(intent : Intent) {
            if(intent.action ==
                TelephonyManager.ACTION_PHONE_STATE_CHANGED) {

                // 현 상태
                currentState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

                if (prevState == TelephonyManager.EXTRA_STATE_IDLE &&
                    currentState == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    // IDLE -> OFFHOOK 발신 (OUTGOING)
                    Timber.d("onPhoneStateChange 발신전화")
                    IS_INCOMING_CALL = false
                }else if (prevState == TelephonyManager.EXTRA_STATE_IDLE &&
                    currentState == TelephonyManager.EXTRA_STATE_RINGING) {
                    // IDLE -> RINGING 수신 (INCOMING)
                    Timber.d("onPhoneStateChange 수신전화")
                    IS_INCOMING_CALL = true
                }

                // 이전 상태와 동기화 처리
                prevState = currentState
            }
        }

        /**
         * onPhoneStateChange 등록 필수
         * true : 수신 (INCOMING)
         * false : 발신 (OUTGOING)
         * 통화종료 후 마지막 상태값을 가진다.
         */
        fun isIncomingCall() = IS_INCOMING_CALL
    }
}
