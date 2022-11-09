<template>
    <div class="su">
        <LogoutTopTitle/>

        <div class="login">
            <p class ="login-title"> 회원가입 </p>

            <form class ="login-form" action="" method="">
                <ul>
                    <li class="id-section">
                        <div class="wrap">
                            <input type="text" style="width: 250px;" id="login-form-id" placeholder="아이디(6~20자)"/>
                            <button type="button" id="check-overlap" @click="checkid"> 중복확인</button>
                        </div>
                        <span class="login-msg id" id="idError">* 사용할 수 없는 아이디입니다.</span>
                    </li>
                    <li class="pw-section">
                        <input style="width: 500px;" type="password" id="login-form-pw" placeholder="비밀번호(영문, 숫자, 특수문자 포함 8~20자)"/>
                        <div id="keyShow">SHOW</div>
                        <span class="login-msg id" id="passwordError">* 사용할 수 없는 비밀번호입니다.</span>
                    </li>
                    <li class="pw-check-section">
                        <input style="width: 500px;" type="password" id="login-form-pw2" placeholder="비밀번호 재입력"/>
                        <div id="keyShow2">SHOW</div>
                        <span class="login-msg id" id="passwordCheckError">* 일치하지 않는 비밀번호입니다.</span>
                    </li>
                    <li class="email-section">
                        <input style="width: 100px;" type="email" id="login-form-email" placeholder="이메일 주소"/>
                        <span id="email-center"> @</span>
                        <input style="width: 100px;" type="text" id="login-form-email-backaddress" placeholder="직접입력" readonly/>

                        <select name="Email" id="email-address">
                            <option class="email-back-address" value="gmail.com" selected>gmail.com</option>
                            <option class="email-back-address" value="naver.com"> naver.com </option>
                            <option class="email-back-address" value="daum.net"> daum.net </option>  
                            <option class="email-back-address" value="direct"> 직접입력 </option>                 
                        </select>

                        
                    </li>
                    <li class="btn-section">
                        <button @click="finalCheck" type="button" id="checkKey" > Sign up</button>    
                    </li>
                </ul>
            </form>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from "../components/LogoutTopTitle.vue";
import $ from 'jquery';

export default{
        mounted(){
            $("#login-form-pw").on("keyup", function(event){
                if(event.keyCode === 13){
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                }else{
                    if(this.value){
                        $("#keyShow").css("display", "inline-block");
                    }else{
                        $("#keyShow").hide();
                    }
                }
            }).focus()

            $("#keyShow").on("click", function() {
                if($("#login-form-pw").attr("type") == "password"){
                    $("#login-form-pw").attr("type", "text");
                    $($(this)).text("HIDE");
                }else{
                    $("#login-form-pw").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            })

            $("#login-form-pw2").on("keyup", function(event){
                if(event.keyCode === 13){
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                }else{
                    if(this.value){
                        $("#keyShow2").css("display", "inline-block");
                    }else{
                        $("#keyShow2").hide();
                    }
                }
            }).focus();

            $("#keyShow2").on("click", function() {
                if($("#login-form-pw2").attr("type") == "password"){
                    $("#login-form-pw2").attr("type", "text");
                    $($(this)).text("HIDE");
                }else{
                    $("#login-form-pw2").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            });



            $("#login-form-pw").on("keyup", function() { overlap() }).focus();
            $("#login-form-pw2").on("keyup", function() { overlap() }).focus();


            function overlap(){
                var pw = document.getElementById("login-form-pw").value;
                var pwc = document.getElementById("login-form-pw2").value;

                if(pw!="" || pwc!=""){
                    if(pw == pwc){
                        $("#passwordCheckError").html("* 일치한 비밀번호입니다.");
                        $("#passwordCheckError").css('color', 'green');
                    }else{
                        $("#passwordCheckError").html("* 일치하지 않는 비밀번호입니다.");
                        $("#passwordCheckError").css('color', 'red');
                    }
                }

            }
            

            $("#login-form-pw").on("keyup", function(){

                var pw = $("#login-form-pw").val();
                var num = pw.search(/[0-9]/g);
                var eng = pw.search(/[a-z]/ig);
                var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

                if(pw.length<8 || pw.length > 20){
                    $("#passwordError").html("* 8~20자리 이내로 입력해주세요.");
                    $("#passwordError").css('color', 'red');

                }else if(pw.search(/\s/) != -1){
                    $("#passwordError").html("* 공백없이 입력해주세요.");
                    $("#passwordError").css('color', 'red');

                }else if(num <0 || eng<0 || spe <0){
                    $("#passwordError").html("* 영어, 숫자, 특수문자를 사용해주세요.");
                    $("#passwordError").css('color', 'red');

                }else{
                    $("#passwordError").html("* 사용가능한 비밀번호입니다.");
                    $("#passwordError").css('color', 'green');
                }

            }).focus();



            $("#login-form-id").on("keyup", function(){
                var id = document.getElementById("login-form-id").value;
                
                if(id ==""){
                    $("#idError").html("* 아이디를 입력해주세요.");
                    $("#idError").css('color', 'red');
                }

            }).focus();


            $(function(){
                $("#email-address").change(function() {
                    if($("#email-address").val() == "direct") {
                        $("#login-form-email-backaddress").attr("readonly", false);
                    }  else {
                        $("#login-form-email-backaddress").attr("readonly", true);
                    }
                }) 
            });





        },
        name:"SignUpPage",
        components: {
            LogoutTopTitle
        },
        methods:{
            checkid(){
                console.log('checkid');
                // 아이디 중복 확인 코드 들어가야함 
            },
            finalCheck(){
                console.log('finalCheck')

                this.$router.push({
                path:'/signup/end'
                })
            },
        }
    };
</script>

<style scoped>

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
