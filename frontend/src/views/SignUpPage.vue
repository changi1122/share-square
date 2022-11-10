<template>
    <div class="su">
        <LogoutTopTitle/>

        <div class="login">
            <p class="login-title">회원가입</p>

            <form class="login-form" action="" method="">
                <ul>
                    <li class="id-section">
                        <div class="wrap">
                            <input v-model="username" @keydown="checkId" type="text" style="width: 250px;" id="login-form-id" placeholder="아이디(6~20자)"/>
                            <button type="button" id="check-overlap" @click="checkUsernameDuplicate">중복 확인</button>
                        </div>
                        <span :class="{'red': idErrorColor === 'red', 'green': idErrorColor === 'green', 'login-msg': true, 'id': true}" id="idError">{{ idError }}</span>
                    </li>
                    <li class="pw-section">
                        <input v-model="password" @keyup="[checkPasswordRule(), checkPasswordMatched()]" style="width: 500px;" type="password" id="login-form-pw" placeholder="비밀번호(영문, 숫자, 특수문자 포함 8~20자)"/>
                        <div id="keyShow">SHOW</div>
                        <span class="login-msg id" id="passwordError">{{ passwordError }}</span>
                    </li>
                    <li class="pw-check-section">
                        <input v-model="rePassword" @keyup="checkPasswordMatched" style="width: 500px;" type="password" id="login-form-pw2" placeholder="비밀번호 재입력"/>
                        <div id="keyShow2">SHOW</div>
                        <span v-if="isPasswordNotMatched" class="login-msg id" id="passwordCheckError">* 일치하지 않는 비밀번호입니다.</span>
                    </li>
                    <li class="email-section">
                        <input v-model="emailUsername" style="width: 100px;" type="email" id="login-form-email" placeholder="이메일 주소"/>
                        <span id="email-center"> @</span>
                        <input v-model="emailHost" style="width: 100px;" type="text" id="login-form-email-backaddress" placeholder="직접입력" :readonly="emailHostSelect !== 'SELF' ? true : false"/>

                        <select v-model="emailHostSelect" @change="selectEmailHost" title="emailHostSelect" name="Email" id="email-address">
                            <option value="gmail.com" class="email-back-address">gmail.com</option>
                            <option value="naver.com" class="email-back-address">naver.com</option>
                            <option value="daum.net" class="email-back-address">daum.net</option>
                            <option value="SELF" class="email-back-address">직접입력</option>
                        </select>
                    </li>
                    <li class="btn-section">
                        <button @click="submit" type="button" id="checkKey">회원 가입</button>    
                    </li>
                </ul>
            </form>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from "../components/LogoutTopTitle.vue";
import $ from 'jquery';
import Axios from 'axios';

export default{
        mounted(){
            $("#login-form-pw").on("keyup", function(event){
                if(event.keyCode === 13) {
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                } else {
                    if(this.value){
                        $("#keyShow").css("display", "inline-block");
                    }else{
                        $("#keyShow").hide();
                    }
                }
            })

            $("#keyShow").on("click", function() {
                if($("#login-form-pw").attr("type") == "password") {
                    $("#login-form-pw").attr("type", "text");
                    $($(this)).text("HIDE");
                } else {
                    $("#login-form-pw").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            })

            $("#login-form-pw2").on("keyup", function(event){
                if(event.keyCode === 13) {
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                } else {
                    if(this.value) {
                        $("#keyShow2").css("display", "inline-block");
                    } else{
                        $("#keyShow2").hide();
                    }
                }
            })

            $("#keyShow2").on("click", function() {
                if($("#login-form-pw2").attr("type") == "password") {
                    $("#login-form-pw2").attr("type", "text");
                    $($(this)).text("HIDE");
                } else {
                    $("#login-form-pw2").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            });
        },
        name:"SignUpPage",
        components: {
            LogoutTopTitle
        },
        data() {
            return {
                username: "",
                password: "",
                rePassword: "",
                emailUsername: "",
                emailHost: "gmail.com",
                emailHostSelect: "gmail.com",

                idError: "",
                idErrorColor: "red",
                isUsernameChecked: false,

                passwordError: "",
                isPasswordChecked: false,
                isPasswordNotMatched: false,
            }
        },
        methods:{
            checkId() {
                if (this.username === '') {
                    this.idError = '* 아이디를 입력해주세요.';
                    this.idErrorColor = 'red';
                    this.idError = '';
                } else {
                    this.idError = '* 사용가능한 아이디인지 중복 확인이 필요합니다.';
                    this.idErrorColor = 'red';
                    this.isUsernameChecked = false;
                }
            },
            async checkUsernameDuplicate() {
                const url ='/api/checkforduplicate';
                const data = {
                    username: this.username
                };

                try {
                    const response = await Axios.post(url,data);
                    this.isUsernameChecked = response.data.result;
                    if (this.isUsernameChecked) {
                        this.idError = '* 사용가능한 아이디입니다.';
                        this.idErrorColor = 'green';
                    }
                    else {
                        this.idError = '* 사용할 수 없는 아이디입니다.';
                        this.idErrorColor = 'red';
                    }
                }
                catch (e) {
                    this.isUsernameChecked = false;
                    this.idError = '* 사용할 수 없는 아이디입니다.';
                    this.idErrorColor = 'red';
                }
            },
            checkPasswordRule() {
                this.isPasswordChecked = false;

                const num = this.password.search(/[0-9]/g);
                const eng = this.password.search(/[a-z]/ig);
                const spe = this.password.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

                if(this.password.length < 8 || this.password.length > 20) {
                    this.passwordError = '* 8 ~ 20자리 이내로 입력해주세요.';
                } else if(this.password.search(/\s/) != -1) {
                    this.passwordError = '* 공백없이 입력해주세요.';
                } else if(2 <= (num < 0) + (eng < 0) + (spe < 0)) {
                    this.passwordError = '* 영어, 숫자, 특수문자를 중 두 종류를 조합해 사용해주세요.';
                } else {
                    this.isPasswordChecked = true;
                    this.passwordError = '';
                }
            },
            checkPasswordMatched() {
                if(this.password !== '' || this.rePassword !== ''){
                    if (this.password === this.rePassword){
                        this.isPasswordNotMatched = false;
                    } else {
                        this.isPasswordNotMatched = true;
                    }
                } else {
                    this.isPasswordNotMatched = false;
                }
            },
            selectEmailHost() {
                if (this.emailHostSelect !== 'SELF')
                    this.emailHost = this.emailHostSelect;
                else
                    this.emailHost = '';
            },
            async submit() {
                if (!this.isUsernameChecked || !this.isPasswordChecked || this.isPasswordNotMatched ||
                    this.emailUsername == '' || this.emailHost === '') {
                    return;
                }

                const url ='/api/register';
                const data = {
                    username: this.username,
                    password: this.password,
                    email: this.emailUsername + '@' + this.emailHost
                };

                try {
                    const response = await Axios.post(url,data);
                    if (response.status.toString().startsWith('2')) {
                        this.$router.push({
                            path:'/signup/end'
                        });
                    } else {
                        throw new Error('회원 가입 중 오류');
                    }
                }
                catch (e) {
                    console.log(e);
                    alert('회원 가입 중 알 수 없는 오류가 발생하였습니다.');
                }
            }
        }
    };
</script>

<style scoped>

.red {
    color: red !important;
}
.green {
    color: green !important;
}

.su{
    overflow: hidden;
}

ul {
padding-left: 0px;
}

.login {
    margin-top : 80px;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.login-title{

    margin-top: 60px;
    margin-bottom: 10px;

    font-family:'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 30px;
    line-height: 77px;
    letter-spacing: 0.05em;

    color: black;
}

.login-form{
    width: 560px;
    height: fit-content;
    display: flex;
    flex-direction: column;
}

.login-form > ul, .login-form li {
    width: 100%;
    height: fit-content;
}

.login-form input{
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;   
}

.login-form li {
    width: 100%;
    margin-bottom: 20px;
    position: relative;
}

.login-form li:nth-child(1) > div {
    width: 100%;
    display:  flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.login-form > ul li:nth-child(1) > div,
.login-form > ul li:nth-child(2) > input,
.login-form > ul li:nth-child(3) > input {
    margin-bottom: 3px;
}

.login-form > ul li .login-msg {
    font-size: 12px;
    color: red;
}

#login-form-id, 
#login-form-pw, 
#login-form-pw2, 
#login-form-email,
#login-form-email-backaddress {
    font-family: 'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;

    color: #878787;
}


#keyShow,
#keyShow2{
    position: absolute;
    display: none;
    font-size: 9px;
    cursor: pointer;
    color: grey;
    top: 20px;
    right: 20px;
}

#email-center{
    font-size: 20px;
    font-weight: 900;
}

#checkKey {

    margin-top: 15px;
    font-size: 13px;
    border-radius: 30px;
    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 7px 22px;
}

#checkKey:hover{
    color: white;
    background-color: #5EDB97;
}

.login-form > ul li:nth-child(4) { 
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

#email-address:focus {
    outline: none;
}

#login-form-email {
    height: fit-content;
}

#email-address{
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;
    appearance: none;
    
    font-family: 'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;
    
    width: 130px;

    border: none;
    color: #878787;
}

.login-form > ul li:nth-child(5) {
    display: flex;
    flex-direction: row;
    justify-content: end;
    margin-top: 20px;
}

#check-overlap {
    font-size: 13px;
    border-radius: 30px;

    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 15px 36px;
}

#check-overlap:hover{
    color: white;
    background-color: #5EDB97;
}

</style>
