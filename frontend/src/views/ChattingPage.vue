<template>
    <TopHeader/>
    <div class="chatting-whole">
        <div class="show-chatting-info">
            <div class="catter">
                <div style="margin: 15px 10px; padding-top:64px">
                    <p class="mobile-none" style="font-size: 25px; margin-left: 15px; font-weight:bold">채팅</p>
                    
                    <div class="recipient-list"  @click="formMessageLauch(item.guest_id, item.guest_name)" v-for="(item, idx) in userlist" :key="idx"> 
                        <div class="chat-recipient">
                            <img class="user-img" :src="'/api/user/' + item.guest_name + '/profileImage'" alt="">
                            <p class="mobile-none"> {{item.guest_name}}</p>
                        </div>
                        <i class="fa-regular fa-trash-can mobile-none" @click="DeleteRoom(item.guest_id, item.id, item.user_id, item.guest_name)"></i>
                    </div>


                </div>
            </div>
    
            <div class="old-massage">
                <template v-if="(who.length != 0)">
                <div class="chat-header">
                    <div class="chat-recipient noclick">
                        <img class="user-img" :src="'/api/user/' + this.selectedUsername + '/profileImage'" alt="">
                        <p > {{this.selectedUsername}}</p>
                    </div>
                </div>
                <div class="chat-massage">
                    <div ref="formMessageBody" class="card-body msg_card_body" id="formMessageBody">
                    </div>
                </div>
                <div class="card-footer">
                    <div class="input-group" id="formSubmit">
                        <div class="input-group-append">
                            <span class="input-group-text attach_btn">
                                <i class="fa-ragular fa-paper-plane"></i>
                            </span>
                        </div>
                        <textarea
                            id="message-to-send"
                            username="message-to-send"
                            class="form-control type_msg"
                            placeholder="메시지 내용 입력"></textarea>

                        <div class="input-group-append" id="buttonSend">
                            <button class="input-group-text send_btn" @click="sendMessage">
                                <i style="font-size: 20px;" class="fas fa-location-arrow"></i>
                            </button>
                        </div>
                    </div>
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
import TopHeader from '@/components/TopHeader.vue';
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import $ from 'jquery'
import Axios from 'axios';
import dayjs from 'dayjs';

export default {
    components: {
        TopHeader,
    },
    data() {
        return {
            userlist: [],
            newMessages: null,
            stompClient: null,
            selectedUserOrGrup: "10000000000000000",
            selectedUsername: "",
            to: 0,
            who:"",
        }
    },
    mounted() {
        window.onload = this.setting();
    },
    beforeUnmount() {
        this.stompClient.disconnect(() => {
            console.log("Disconnected before unmount.");
        })
    },
    methods: {
        autoScrolling() {
            const chatWrapEl = document.getElementById('formMessageBody');  
            console.log(chatWrapEl.scrollHeight)
            console.log(chatWrapEl.scrollTop)

            chatWrapEl.scrollTop = chatWrapEl.scrollHeight;
        },
        sendMsgUser(from, text) {
            this
                .stompClient
                .send(
                    "/topic/messages/" + this.who,
                    {},
                    JSON.stringify({fromLogin: from, message: text})
                );
            const data = {
                message_text: text,
                message_from: from,
                message_to: this.to
            }
            Axios
                .post("/api/get/chatting", data)
                .then(res => {
                    console.log("is message send? ", res)
                })

            this.autoScrolling();

        },
        sendMessage() {
            let message = $('#message-to-send').val();
            var userId = this.$store.state.Userid.userid;
            this.sendMsgUser(userId, message);

            let messageTemplateHTML = "";
            messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                    'lex justify-content-end mb-4"><div id="child_message" class="msg_cotainer">' +
                    message +
                    `</div><p class="chat-date">${dayjs().format('MM-DD hh:mm')}</p></div>`;
            $('#formMessageBody').append(messageTemplateHTML);
            console.log("append success")

            document
                .getElementById("message-to-send")
                .value = "";

            this.autoScrolling();
        },
        onError() {
            console.log("Disconnected from console");
        },
        connectToChat(userName) {
            var vm = this;
            console.log("connecting to chat...");
            let socket = new SockJS('/api/ws');
            // let socket=new WebSocket("wss://localhost:9090/ws")
            vm.stompClient = Stomp.over(socket);
            vm
                .stompClient
                .connect(
                    {
                        "X-Authorization": "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJVQmppQkZXYmM4NnpBaER" +
                                    "0M1QtTUJ6cnl3R3FnYkF5QlFxYjRjN0w3VHpNIn0.eyJleHAiOjE2MzE1ODc4NzksImlhdCI6MTYzM" +
                                    "DM3ODI3OSwianRpIjoiODAyZGQyYzAtNjlhYi00Yjk2LTllZjgtODA5YWY3MWJmNmFmIiwiaXNzIjo" +
                                    "iaHR0cHM6Ly9rZXljbG9hay1kZXYuZ2l0c29sdXRpb25zLmlkL2F1dGgvcmVhbG1zL2dpdCIsImF1Z" +
                                    "CI6WyJyZWFsbS1tYW5hZ2VtZW50IiwiYWNjb3VudCJdLCJzdWIiOiJhYTkzMzMxMi0wMjhkLTQ3MzQ" +
                                    "tYTlhNC1hMGYxNmNlZDY5ZTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJnaXQtY2xpZW50Iiwic2Vzc" +
                                    "2lvbl9zdGF0ZSI6IjM5YTRhNzFhLTJmZWYtNDkwMS1hNjdlLTYwYTViNjI0YjllNyIsImFjciI6IjE" +
                                    "iLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphd" +
                                    "GlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidml" +
                                    "ldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZ" +
                                    "GVycyIsImltcGVyc29uYXRpb24iLCJyZWFsbS1hZG1pbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2U" +
                                    "tdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzI" +
                                    "iwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHM" +
                                    "iLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ" +
                                    "2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWN" +
                                    "jb3VudCIsInZpZXctYXBwbGljYXRpb25zIiwidmlldy1jb25zZW50IiwibWFuYWdlLWFjY291bnQtb" +
                                    "Glua3MiLCJkZWxldGUtYWNjb3VudCIsIm1hbmFnZS1jb25zZW50Iiwidmlldy1wcm9maWxlIl19fSw" +
                                    "ic2NvcGUiOiJnaXRzY29wZSIsInRlbmFudF9pZCI6WyI1ZDEyNWI2OS05YzcxLTRhYzktODVhNi1lM" +
                                    "WQ4NGU3ZDFiNWIiXSwiZ3JvdXBfbmFtZSI6IklEU3RhciIsInVzZXJfbmFtZSI6ImZyZWR5LmZlcm5" +
                                    "hbmRvIiwiaW5zdGFuY2VfdXJsIjpbImh0dHBzOi8vZGV2LmdpdHNvbHV0aW9ucy5pZCJdLCJpbnN0Y" +
                                    "W5jZV9hcGkiOlsiaHR0cHM6Ly9hcGktZGV2LmdpdHNvbHV0aW9ucy5pZCJdLCJlbXBfaWQiOiJlYmE" +
                                    "yYjQ2Yi05MjljLTExZWItOTdjZS0wYWRiY2M5ZWFhYTgifQ.SAW95PiA3DZFwjOCeY3-aLzHPKkH9J" +
                                    "_ucbZQ6rV9b8QjZ8zbzW_0F2yrYa7GpKjFNmZ7cL1mFm46wepnGwZvqUIb08EDN0wIqgf20XUsnck7" +
                                    "Ji8av4HVEgAuLseiOwoHKHSjRGY8Rj-AeOQ3clbmYz_wy0RtlRResmr0_M59X-iYBtIaWxIDnfarqK" +
                                    "vAWHz1Sus0y1abPvRyahLTjtAeKYNITmVhYQb66vWomttJiEDvKmCcNpQtJjW2WkJi7SiojxrsjFOo" +
                                    "9R_PiPnYV3vMjsZMRfa8n3PXeG1g-cRst6nYZ0YYoarhAS_aLv-cCzEty5-rgEOMPGWPtyYGtKbJbg"
                    },
                    function (frame) {
                        console.log("connected to: " + frame);
                        vm
                            .stompClient
                            .subscribe("/topic/messages/" + userName, function (response) {
                                console.log("response", response)
                                console.log("headeer", response.headers)
                                var h = response.headers
                                console.log(h[0])
                                var mes ="";
                                for(var c in Object.keys(h)) {

                                    if(h[c]== "\\c") {
                                        mes += ":";
                                    } else if(h[c] == "") {
                                        mes += " ";
                                    } else {
                                        mes += h[c];
                                    }

                                    if (h[c] === "}")
                                        break;
                                }
                                console.log("%o",mes);
                                const json = JSON.parse(mes);
                                console.log(json);
                                console.log(json.message);

                                console.log("data.fromLogin = " + json.fromLogin)
                                if (vm.selectedUserOrGrup == json.fromLogin) {

                                    let messageTemplateHTML = "";
                                    messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                            'lex justify-content-start mb-4"><div id="child_message" class="msg_cotainer_se' +
                                            'nd">' + json.message +
                                            `</div><p class="chat-date">${dayjs().format('MM-DD hh:mm')}</p></div>`;
                                    $('#formMessageBody').append(messageTemplateHTML);


                                    console.log("Append success");
                                } else {
                                    // 현재 열린 사용자가 아닌 사용자로부터 메시지를 받은 경우

                                    // console.log("data.group_id "+data.group_id)
                                    vm.newMessages = new Map();
                                    vm
                                        .newMessages
                                        .set(json.fromLogin, json.message);
                                    $('#userNameAppender_' + json.fromLogin).append(
                                        '<span id="newMessage_' +json.fromLogin + '" style="color: red">+1</span>'
                                    );

                                    console.log("Received message but not opened user");
                                }
                            }, {});
                    },
                    vm.onError
                );
        },
        setting() {
            if (this.$store.state.Userid.userid === null) {
                window.location.href = "index.html";
                return false;
            }
            this.fetchAll();
            this.connectToChat(this.$store.state.Username.username);
        },
        fetchAll() {
            var vm = this;
            var userId = this.$store.state.Userid.userid;

            console.log(userId);

            Axios
                .get("/api/get/chatter/" + userId)
                .then(response => {
                    console.log("fetchAllUsers : ", response);
                    vm.userlist = response.data;
                    console.log(vm.userlist);
                })
                .catch(e => {
                    console.log(e);
                })
        },
        formMessageLauch(id, name) {
            console.log(id, name)
            this.who = name;
            this.selectedUsername = name;

            let nama = $('#formMessageHeader .user_info').find('span')

            nama.html("Chat With " + name);
            nama.attr("data-id", id);
            let isNew = document.getElementById("newMessage_" + id) !== null;
            if (isNew) {
                let element = document.getElementById("newMessage_" + id);
                element
                    .parentNode
                    .removeChild(element);
            }
            this.selectedUserOrGrup = id;

            let isHistoryMessage = document.getElementById("formMessageBody");
            if (isHistoryMessage !== null && isHistoryMessage.hasChildNodes()) {
                isHistoryMessage.innerHTML = "";
            }

            var userId = this.$store.state.Userid.userid;
            this.to = id

            var vm = this

            Axios
                .get("/api/listmessage", {
                    params: {
                        from: userId,
                        to: id
                    }
                })
                .then(response => {
                    let messages = response.data
                    console.log(messages);


                    let messageTemplateHTML = "";
                    for (let i = 0; i < messages.length; i++) {
                        if (messages[i]['message_from'] == userId) {
                            messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                    'lex justify-content-end mb-4"><div id="child_message" class="msg_cotainer">' +
                                    messages[i]['message_text'] +
                                    `</div><p class="chat-date">${dayjs(messages[i]['created_datetime']).format('MM-DD hh:mm')}</p></div>`;
                        } else {
                            messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                    'lex justify-content-start mb-4"><div id="child_message" class="msg_cotainer_se' +
                                    'nd">' + messages[i]['message_text'] +
                                    `</div><p class="chat-date">${dayjs(messages[i]['created_datetime']).format('MM-DD hh:mm')}</p></div>`;
                        }
                    }
                    $('#formMessageBody').append(messageTemplateHTML);

                    vm.autoScrolling();
                })
                .catch(e => {
                    console.log(e);
                })

            },
            DeleteRoom(gid, id , uid, gname){
                Axios.get("/api/get/chat/dinfo",{
                    params:{
                        id : id,
                        gid : gid,
                        uid : uid
                    }
                }).then(res=>{
                    console.log(res)
                    this.formMessageLauch(gid, gname);
                    this.fetchAll();
                    this.who=""
                })
            }
    }
}
</script>



<style scoped>

    p{
        padding: 0;
        margin: 0;
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
        overflow-y: scroll;
        width: 30%;
        border-right: 0.5px solid rgba(0,0,0,.15);
    }

    .old-massage{
        background: rgba(242, 242, 242, 0.689);
        height: calc(100% - 58.4px);
        width: 70%;
        box-sizing: border-box;
        padding-top: 64px;
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
        border: 0.5px solid rgb(163, 163, 163);
        border-radius: 25px;
        object-fit: cover;
        margin-right: 10px;
    }

    .chat-recipient > p{
        font-size: 18px;
    }

    .chat-massage{
        background: rgb(214, 245, 221);
        width: 100%;
        height: 100%;
    }

    .msg_card_body {
        overflow-y: scroll !important;
        padding-bottom: 100px;
    }

    .card-footer {
        position: relative;
        border-radius: 0 0 15px 15px !important;
        border-top: 0 !important;
    }

    .noclick {
        background-color: rgb(214, 245, 221);
        padding: 8px;
        cursor: unset;
        border-bottom: 0.5px solid rgba(0,0,0,.15);
        box-sizing: border-box;
    }
    .noclick > img{
        width: 40px;
        height: 40px;
        border-radius: 20px;
    }
    .noclick:hover {
        background-color: rgb(214, 245, 221) !important;
        padding: 8px;
        background: unset;
        border-radius: unset;
    }
    
    .attach_btn {
        border-radius: 15px 0 0 15px !important;
        background-color: rgba(0, 0, 0, 0.3) !important;
        border: 0 !important;
        color: white !important;
        cursor: pointer;
    }

    .type_msg {
        flex: 1;
        background-color: #fff !important;
        border: 0 !important;
        color: #000 !important;
        height: 60px !important;
        overflow-y: auto;
        resize: none;
        padding: 10px;
        font-size: 14px;
        font-family: inherit;
    }

    .type_msg:focus {
        box-shadow: none !important;
        outline: 0 !important;
    }

    .send_btn {
        background-color: rgba(73, 73, 73, 0.3);
        border: 0 !important;
        color: white !important;
        cursor: pointer;
        height: 100%;
        width: 60px;
        border-radius: 0 !important;
    }
    .send_btn:hover {
        background-color: #5EDB97;
    }


    .chat-massage {
        overflow-y: scroll;
        padding-top: 10px;
        box-sizing: border-box;
    }
    .input-group {
        display: flex;
        background: #fff;
        position: absolute;
        bottom: 0;
        width: 100%;
        border-top: 0.5px solid rgba(0,0,0,.15);
    }
    .attach_btn {
        display: none;
    }

    @media only screen and (max-width:738px) {
        .mobile-none {
            display: none;
        }
        .chat-recipient {
            margin-right: 0;
        }
        .catter {
            width: unset;
        }
        .old-massage {
            width: 100%;
        }
        .recipient-list {
            align-items: center;
        }
    
    }

</style>