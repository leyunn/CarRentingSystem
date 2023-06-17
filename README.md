# Installation Tutorial

### 如何跑 backend_A

##### 安裝

1. 產生Token (see tutorial below)
2. 在本地端輸入指令：`git clone https://[你的Token]@github.com/oop-finalproject-group4/backend_A.git `

##### 執行

1. I**ntelliJ** 就直接用開啟`final`的資料夾，然後在裡面到`FinalApplication` 按執行按鈕即可。

2. **eclipse** : 到Project Explorer, 點擊 project name -> select "Run As" -> "Maven Build..."
   在goals那邊, 輸入 `spring-boot:run` 然後按執行。

##### 測試

成功執行後，在瀏覽器輸入http://localhost:8080/test ，若顯示`ok!` 即為成功

##### 使用API

在前端送出 `http://localhost:8080/[API路徑]` 即可使用。

Example:

```
http://localhost:8080/account/info/1 //取得id為1的使用者的資訊
```



### 產生 Token

> 這個Token可以重複使用，可以上傳、下載任何你有權限的Repositry

1. 到 https://github.com/settings/profile > `Developer settings` > `Persional Access Token (Select Classic)` 
2. 點擊 `Generate New Token(Select Classic)`
3. Tick every box, 點擊 `Generate Token`
4. 複製 Token（應該是一串亂碼）



#### 如何上傳Code到Github

照著Github上的教學，除了這行的地方中間要加入你的Token：

```
git remote add origin https://[你的Token]@github.com/oop-finalproject-group4/[你的repo名稱].git
```



### API

#### POST /account/login
登入

request:
```jsonld
{
    "username": String,
    "password": String[6:]
}
```
response:
```jsonld
200 {"userid": int}
404 {"error": "user not found"}
401 {"error": "authentication failed"}
415 {"error": "format error"}
```

#### POST /account/signup
註冊
＊請在前端提醒/註明（如下面說明 e.g. cardnumber長度、email格式等），如果不符合後端只會回傳format error

request:
```jsonld
{
    "username": String,
    "password": String[6:],
    "cardNumber": String[16], 
    "safeNumber": String[3],
    "phoneNumber": String[10],
    "email": String(*@*.*),
}
```
response:
```jsonld
200 {}
409 {"error": "user exist"}
415 {"error": "format error"}
```
#### GET /account/info/:userid
回傳某個使用者的帳號資訊

response:
```jsonld
200 {
    "username": String,
    "cardNumber": String[16], 
    "safeNumber": String[3],
    "phoneNumber": String[8:],
    "email": String(*@*.*),
}
404 {"error": "user does not exist"}
```