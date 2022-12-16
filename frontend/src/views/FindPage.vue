<template>
    <div class="su">
        <LogoutTopTitle/>

        <div class="row">
            <div>
                <p class="subtitle">아이디 찾기</p>
                <p class="description">가입한 이메일을 사용하여 아이디를 찾습니다.</p>
                <input v-model="email" class="email-input" type="email" placeholder="가입한 이메일 입력"/>
                <button class="find-button" @click="tryFindUsername">아이디 찾기</button>
            </div>

            <div style="margin: 80px 0 120px">
                <p class="subtitle">비밀번호 찾기</p>
                <p class="description">개발진에 메일을 보내 비밀번호를 찾습니다.</p>
                <p class="description">아이디와 가입한 이메일을 적어 보내주세요.</p>
                <a class="find-button" href="mailto:changi112242@gmail.com">비밀번호 찾기</a>
            </div>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from "../components/LogoutTopTitle.vue";
import Axios from 'axios';

export default{
        name:"FindPage",
        components: {
            LogoutTopTitle,
        },
        data() {
            return {
                email: ""
            }
        },
        methods:{
            async tryFindUsername() {
                if (!this.email) {
                    return;
                }
                
                const url =`/api/findusername`;
                const data = {
                    email: this.email
                };

                try {
                    const response = await Axios.post(url, data);
                    if (response.status.toString().startsWith('2') &&
                        response.data.result !== "") {
                        alert(`이 이메일로 가입한 아이디는 '${response.data.result}' 입니다.`);
                        this.$router.push({
                            path:'/login'
                        });
                    } else {
                        alert('잘못된 이메일 주소이거나 알 수 없는 오류가 발생하였습니다.');
                    }
                }
                catch (e) {
                    alert('아이디 찾기 중 알 수 없는 오류가 발생하였습니다.');
                }
            }
        }
    };
</script>

<style scoped>
input[type=text], input[type=password] {
    width: 100%;
}

.row {
    max-width: 480px;
    margin: 0 auto;
    margin-top: 150px;
    padding: 0 15px;
    box-sizing: border-box;
    text-align: center;
}

.subtitle {
    margin-top: 60px;
    margin-bottom: 10px;

    font-style: normal;
    font-weight: bold;
    font-size: 32px;
    text-align: center;
    line-height: 48px;
    letter-spacing: 0.05em;

    color: black;
}
.description {
    margin: 4px 0;
    color: #555555;
    font-size: 14px;
}

.email-input {
    width: 100%;
    max-width: 350px;
    margin: 20px 0 0;
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;
    font-family: inherit;
    font-style: normal;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;
    color: #878787;
    box-sizing: border-box;
}

.find-button {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;

    border-radius: 24px;
    width: 100%;
    max-width: 350px;
    height: 48px;
    margin: 20px 0;
    padding: 5px 25px;
    box-sizing: border-box;

    font-size: 18px;
    font-family: inherit;
    font-weight: bold;
    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    cursor: pointer;
}
.find-button:hover {
    color: white;
    background-color: #5EDB97;
}

@media only screen and (max-width:738px) {
    .row {
        max-width: 100%;
        padding: 0 15px;
    }
}

</style>
