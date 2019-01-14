# XPay


## Usage

### gradle
对应的项目中的build.gradle文件添加依赖：

```xml
dependencies {
	implementation 'com.mcgrady:xpay:1.0.1'
	// or
	implementation('com.mcgrady:xpay:1.0.1') {
        exclude module: 'support-annotations'
        exclude module: 'rxjava'
    }
}
```

### Alipay

```java
AliPayReq aliPayReq = new AliPayReq.Builder()
    .with(PayDemoActivity.this)
    .payResultCallBack(new PayResultCallBack() {
        @Override
        public void onPaySuccess(String result) {
        }

        @Override
        public void onPayFailure(String result) {
        }

        @Override
        public void onPayConfirm(String result) {
        }

        @Override
        public void onPayCheck(String status) {
        }
    })
    .create();

PayAPI.getInstance().pay(aliPayReq);

```


### WeChatPay


```java
WeChatPayReq weChatPayReq = new WeChatPayReq.Builder()
    .with(WXAPIFactory.createWXAPI(PayDemoActivity.this, null))
    .appId("")
    .partnerId("")
    .prepayId("")
    .nonceStr("")
    .timeStamp("")
    .sign("")
    .payCallback(new PayResultCallBack() {
        @Override
        public void onPaySuccess(String result) {
        }

        @Override
        public void onPayFailure(String result) {
        }

        @Override
        public void onPayConfirm(String result) {
        }

        @Override
        public void onPayCheck(String status) {
        }
    })
    .create();

PayAPI.getInstance().pay(weChatPayReq);

```