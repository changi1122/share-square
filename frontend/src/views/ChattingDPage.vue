<template>
    <LogoutTopTitle/>
    <div class="chatting-whole">
        <div class="none"></div>
        <div class="show-chatting-info">
            <div class="catter">
                <div style="margin: 15px 10px;">
                    <p style="font-size: 25px;">Chatting</p>
                    
                    <div class="recipient-list" @click="Showchat"> 
                        <div class="chat-recipient">
                            <img src="@/assets/chat.png" alt="">
                            <p >Username</p>
                        </div>
                        <i class="fa-regular fa-trash-can"></i>
                    </div>


                </div>
                <div class="catter-list" >
        
                </div>
            </div>
    
            <div class="old-massage">
                <template v-if="(who.length != 0)">
                <div class="chat-massage">
                    
                </div>
                </template>
                <template v-else>

                    <div class="none-chat-massage-img">
                        <img src="@/assets/category.png" alt="">
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';

export default{
    components:{
        LogoutTopTitle,
    },
    data(){
        return{
            chater:[
                {
                    from : 15,
                    to : 14,
                    name : "배재원" },
                {
                    from : 16,
                    to : 12,
                    name : "김성욱"
                },
                {
                    from : 15,
                    to : 13,
                    name : "유현진",
                },
                {
                    from : 15,
                    to : 20,
                    name : "몰라"
                }
            ],
            message : [
                { from : 15, to: 14, massage:"g2"},
                { from : 14, to: 15, massage:"g2"},
                { from : 15, to: 14, massage:"g2222"},
                { from : 14, to: 15, massage:"g2222"},
                { from : 15, to: 14, massage:"오늘 어디서?"},
                { from : 14, to: 15, massage:"오늘 어디서?"},
            ],
            who:"",
        }
    },
    methods:{
        Showchat(){
            this.who = "랄라"
        },
    }
}
</script>



<style scoped>

    p{
        padding: 0;
        margin: 0;
    }

    .none{
        height: 51px;
    }

    .chatting-whole{
        display: flex;
        width: 100%;
        height: 100%;
        flex-direction: column;
    }

    .show-chatting-info{
        display: flex;
        height: 100%;
    }

    .catter{

        width: 30%;
    }

    .old-massage{
        background: rgba(242, 242, 242, 0.689);
        height: 100%;
        width: 70%;
    }

    .none-chat-massage-img{
        display: flex;
        height: 100%;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .none-chat-massage-img > img{
        width: 100px;
        height: 100px;
        opacity: 0.2;
    }
    
    .recipient-list{
        display: flex;
        flex-direction: row;
        align-items: center;
        margin: 10px 10px;
    }

    .chat-recipient{
        display: flex;
        width: 100%;
        flex-direction: row;
        align-items: center;
        cursor: pointer;
        padding: 5px 5px;
        margin-right: 10px;
    }

    .chat-recipient:hover{
        background: #e0ffee;
        border-radius: 15px;
        padding: 5px 5px;
    }

    .chat-recipient > img{
        width: 50px;
        height: 50px;
        border:1px solid black;
        border-radius: 15px;
        object-fit: cover;
        margin-right: 10px;
    }

    .chat-recipient > p{
        font-size: 20px;
    }

    .chat-massage{
        background: rgb(214, 245, 221);
        width: 100%;
        height: 100%;
    }

</style>