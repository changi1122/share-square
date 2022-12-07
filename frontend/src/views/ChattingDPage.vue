<template>
    <div>
        <title>Chat</title>
        <!-- <button @click="MakeRoom">Test Make Room</button> -->
        <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
            integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
            crossorigin="anonymous">
            <link
                rel="stylesheet"
                href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
                integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU"
                crossorigin="anonymous">
                <link
                    rel="stylesheet"
                    type="text/css"
                    href="https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.min.css"></div>

                <div class="container-fluid h-100">
                    <div class="col-md-4 col-xl-3 chat">
                        <div class="card mb-sm-3 mb-md-0 contacts_card">
                            <div class="card-header">
                                <div class="input-group">
                                    <input
                                        type="text"
                                        placeholder="Search..."
                                        username=""
                                        class="form-control search">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text search_btn">
                                                <i class="fas fa-search"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body contacts_body">
                                    <ui class="contacts" id="userlist">
                                        <template v-for="(item, idx) in userlist" :key="idx">
                                            <li
                                                class="active"
                                                id="child_message"
                                                @click="formMessageLauch(item.id, item.username)">
                                                <div class="d-flex bd-highlight">
                                                    <div class="img_cont">
                                                        <img
                                                            src="https://png.clipart.me/istock/previews/9349/93493545-people-icon.jpg"
                                                            class="rounded-circle user_img"></div>
                                                        <div class="user_info" id=" 'userNameAppender_' + {{item.id}}">
                                                            <span>{{item.username}}</span>
                                                        </div>
                                                    </div>
                                                </li>
                                            </template>
                                        </ui>
                                    </div>
                                </div>
                                <div class="col-md-8 col-xl-6 chat">
                                    <div class="card">
                                        <div class="card-header msg_head" id="formMessageHeader">
                                            <div class="d-flex bd-highlight">
                                                <div class="img_cont">
                                                    <img
                                                        src="https://png.clipart.me/istock/previews/9349/93493545-people-icon.jpg"
                                                        class="rounded-circle user_img"></div>
                                                    <div class="user_info">
                                                        <span id="userName"></span>
                                                        <p></p>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="card-body msg_card_body" id="formMessageBody"></div>
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </template>

                        <script>
                            import Stomp from 'webstomp-client'
                            import SockJS from 'sockjs-client'
                            import $ from 'jquery'
                            import Axios from 'axios';
                            export default {
                                data() {
                                    return {
                                        userlist: [],
                                        url: "http://localhost:9090",
                                        newMessages: null,
                                        stompClient: null,
                                        selectedUserOrGrup: "10000000000000000",
                                        to: 0
                                    }
                                },
                                mounted() {
                                    window.onload = this.setting();
                                },
                                methods: {
                                    autoScrolling() {
                                        const chatWrapEl = document.getElementById('formMessageBody');  
                                        chatWrapEl.scrollTop = chatWrapEl.scrollHeight;
                                    },

                                    sendMsgUser(from, text) {
                                        this
                                            .stompClient
                                            .send(
                                                "/api/chat/" + this.selectedUserOrGrup,
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
                                        let username = $('#userName').attr("data-id");
                                        let message = $('#message-to-send').val();
                                        var userId = this.$store.state.Userid.userid;
                                        this.selectedUserOrGrup = username;
                                        this.sendMsgUser(userId, message);

                                        let messageTemplateHTML = "";
                                        messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                                'lex justify-content-end mb-4"><div id="child_message" class="msg_cotainer">' +
                                                message + '</div></div>';
                                        $('#formMessageBody').append(messageTemplateHTML);
                                        console.log("append success")

                                        document
                                            .getElementById("message-to-send")
                                            .value = "";

                                        this.autoScrolling();
                                    },
                                    onError() {
                                        console.log("Disconed from console")
                                    },
                                    connectToChat(userName) {
                                        var vm = this
                                        console.log("connecting to chat...")
                                        let socket = new SockJS(vm.url + '/ws');
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
                                                            let data = JSON.parse(response.body);
                                                            // console.log("selectedUserOrGrup = "+selectedUserOrGrup)
                                                            console.log("data.fromLogin = " + data.fromLogin)
                                                            if (vm.selectedUserOrGrup == data.fromLogin) {
                                                                console.log("selectedUserOrGrup === data.fromLogin")

                                                                let messageTemplateHTML = "";
                                                                messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                                                        'lex justify-content-start mb-4"><div id="child_message" class="msg_cotainer_se' +
                                                                        'nd">' + data.message + '</div></div>';
                                                                $('#formMessageBody').append(messageTemplateHTML);
                                                                console.log("append success")
                                                            } else {
                                                                // console.log("data.group_id "+data.group_id)
                                                                vm.newMessages = new Map();
                                                                vm
                                                                    .newMessages
                                                                    .set(data.fromLogin, data.message);
                                                                $('#userNameAppender_' + data.fromLogin).append(
                                                                    '<span id="newMessage_' + data.fromLogin + '" style="color: red">+1</span>'
                                                                );

                                                                console.log("kebuat")
                                                                let messageTemplateHTML = "";
                                                                messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                                                        'lex justify-content-start mb-4"><div class="msg_cotainer_send">' + data.message +
                                                                        '</div></div>';
                                                                console.log(messageTemplateHTML)

                                                                console.log("append success")
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
                                        var vm = this
                                        var userId = this.$store.state.Userid.userid;

                                        console.log(userId);

                                        Axios
                                            .get("/api/fetchAllUsers/" + userId)
                                            .then(response => {
                                                console.log("fetchAllUsers : ", response);
                                                vm.userlist = response.data;
                                                console.log(vm.userlist)
                                            })
                                            .catch(e => {
                                                console.log(e);
                                            })
                                        },
                                    formMessageLauch(id, name) {
                                        console.log(id, name)

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
                                        let username = $('#userName').attr("data-id");
                                        this.selectedUserOrGrup = username;

                                        let isHistoryMessage = document.getElementById("formMessageBody");
                                        if (isHistoryMessage !== null && isHistoryMessage.hasChildNodes()) {
                                            isHistoryMessage.innerHTML = "";
                                        }

                                        var userId = this.$store.state.Userid.userid;
                                        this.to = id

                                        Axios
                                            .get("/api/listmessage", {
                                                params: {
                                                    from: userId,
                                                    to: id
                                                }
                                            })
                                            .then(response => {
                                                let messages = response.data

                                                let messageTemplateHTML = "";
                                                for (let i = 0; i < messages.length; i++) {
                                                    if (messages[i]['message_from'] == userId) {
                                                        messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                                                'lex justify-content-end mb-4"><div id="child_message" class="msg_cotainer">' +
                                                                messages[i]['message_text'] + '</div></div>';
                                                    } else {
                                                        messageTemplateHTML = messageTemplateHTML + '<div id="child_message" class="d-f' +
                                                                'lex justify-content-start mb-4"><div id="child_message" class="msg_cotainer_se' +
                                                                'nd">' + messages[i]['message_text'] + '</div></div>';
                                                    }
                                                }
                                                $('#formMessageBody').append(messageTemplateHTML);

                                                this.autoScrolling();
                                            })
                                            .catch(e => {
                                                console.log(e);
                                            })

                                        }
                                }
                            }
                        </script>

                        <style scoped>
                            body,
                            html {
                                margin: 0;
                                background: #ffffff;
                            }

                            .chat {
                                max-width: 100% !important;
                                display: flex;
                                width: 100%;

                                margin-top: auto;
                                margin-bottom: auto;
                                padding: 0 !important;
                            }

                            .card {

                                height: 500px;
                                border-radius: 15px !important;
                                background-color: rgba(0, 0, 0, 0.4) !important;
                            }

                            .contacts_body {
                                padding: 0.75rem 0 !important;
                                overflow-y: scroll !important;
                                white-space: nowrap;
                            }

                            .msg_card_body {
                                overflow-y: scroll !important;
                            }

                            .card-header {
                                border-radius: 15px 15px 0 0 !important;
                                border-bottom: 0 !important;
                            }

                            .card-footer {
                                border-radius: 0 0 15px 15px !important;
                                border-top: 0 !important;
                            }

                            .container {
                                align-content: center;
                            }

                            .search {
                                border-radius: 15px 0 0 15px !important;
                                background-color: rgba(0, 0, 0, 0.3) !important;
                                border: 0 !important;
                                color: white !important;
                            }

                            .search:focus {
                                box-shadow: none !important;
                                outline: 0 !important;
                            }

                            .type_msg {
                                background-color: rgba(0, 0, 0, 0.3) !important;
                                border: 0 !important;
                                color: white !important;
                                height: 60px !important;
                                overflow-y: auto;
                            }

                            .type_msg:focus {
                                box-shadow: none !important;
                                outline: 0 !important;
                            }

                            .attach_btn {
                                border-radius: 15px 0 0 15px !important;
                                background-color: rgba(0, 0, 0, 0.3) !important;
                                border: 0 !important;
                                color: white !important;
                                cursor: pointer;
                            }

                            .send_btn {
                                border-radius: 0 15px 15px 0 !important;
                                background-color: rgba(0, 0, 0, 0.3) !important;
                                border: 0 !important;
                                color: white !important;
                                cursor: pointer;
                            }

                            .search_btn {
                                border-radius: 0 15px 15px 0 !important;
                                background-color: rgba(0, 0, 0, 0.3) !important;
                                border: 0 !important;
                                color: white !important;
                                cursor: pointer;
                            }

                            .contacts {
                                list-style: none;
                                padding: 0;
                            }

                            .contacts li {
                                width: 100% !important;
                                padding: 5px 10px;
                                margin-bottom: 15px !important;
                            }

                            .active {
                                background-color: rgba(0, 0, 0, 0.3);
                            }

                            .user_img {
                                height: 70px;
                                width: 70px;
                                border: 1.5px solid #f5f6fa;
                            }

                            .user_img_msg {
                                height: 40px;
                                width: 40px;
                                border: 1.5px solid #f5f6fa;
                            }

                            .img_cont {
                                position: relative;
                                height: 70px;
                                width: 70px;
                            }

                            .img_cont_msg {
                                height: 40px;
                                width: 40px;
                            }

                            .user_info {
                                margin-top: auto;
                                margin-bottom: auto;
                                margin-left: 15px;
                            }

                            .user_info span {
                                font-size: 20px;
                                color: white;
                            }

                            .user_info p {
                                font-size: 10px;
                                color: rgba(255, 255, 255, 0.6);
                            }

                            .video_cam {
                                margin-left: 50px;
                                margin-top: 5px;
                            }

                            .video_cam span {
                                color: white;
                                font-size: 20px;
                                cursor: pointer;
                                margin-right: 20px;
                            }

                            .msg_cotainer {
                                margin-top: auto;
                                margin-bottom: auto;
                                margin-right: 10px;
                                border-radius: 25px;
                                background-color: #78e08f;
                                padding: 10px;
                                position: relative;
                            }

                            .msg_cotainer_send {
                                margin-top: auto;
                                margin-bottom: auto;
                                margin-left: 10px;
                                border-radius: 25px;
                                background-color: #82ccdd;
                                padding: 10px;
                                position: relative;
                            }

                            .msg_time {
                                position: absolute;
                                left: 0;
                                bottom: -15px;
                                color: rgba(255, 255, 255, 0.5);
                                font-size: 10px;
                            }

                            .msg_time_send {
                                position: absolute;
                                right: 0;
                                bottom: -15px;
                                color: rgba(255, 255, 255, 0.5);
                                font-size: 10px;
                            }

                            .msg_head {
                                position: relative;
                            }

                            #action_menu_btn {
                                position: absolute;
                                right: 10px;
                                top: 10px;
                                color: white;
                                cursor: pointer;
                                font-size: 20px;
                            }

                            .action_menu {
                                z-index: 1;
                                position: absolute;
                                padding: 15px 0;
                                background-color: rgba(0, 0, 0, 0.5);
                                color: white;
                                border-radius: 15px;
                                top: 30px;
                                right: 15px;
                                display: none;
                            }

                            .action_menu ul {
                                list-style: none;
                                padding: 0;
                                margin: 0;
                            }

                            .action_menu ul li {
                                width: 100%;
                                padding: 10px 15px;
                                margin-bottom: 5px;
                            }

                            .action_menu ul li i {
                                padding-right: 10px;

                            }

                            .action_menu ul li:hover {
                                cursor: pointer;
                                background-color: rgba(0, 0, 0, 0.2);
                            }

                            @media(max-width: 576px) {
                                .contacts_card {
                                    margin-bottom: 15px !important;
                                }
                            }

                            body {
                                background: #ffffff;
                            }

                            body,
                            html {
                                position: relative;
                                height: 100%;
                            }

                            .login-container {
                                position: relative;
                                width: 300px;
                                margin: 80px auto;
                                padding: 20px 40px 40px;
                                text-align: center;
                                background: #fff;
                                border: 1px solid #ccc;
                            }

                            #output {
                                position: absolute;
                                width: 300px;
                                top: -75px;
                                left: 0;
                                color: #fff;
                            }

                            #output.alert-success {
                                background: rgb(25, 204, 25);
                            }

                            #output.alert-danger {
                                background: rgb(228, 105, 105);
                            }

                            .login-container::after,
                            .login-container::before {
                                content: "";
                                position: absolute;
                                width: 100%;
                                height: 100%;
                                top: 3.5px;
                                left: 0;
                                background: #fff;
                                z-index: -1;
                                -webkit-transform: rotateZ(4deg);
                                -moz-transform: rotateZ(4deg);
                                -ms-transform: rotateZ(4deg);
                                border: 1px solid #ccc;

                            }

                            .login-container::after {
                                top: 5px;
                                z-index: -2;
                                -webkit-transform: rotateZ(-2deg);
                                -moz-transform: rotateZ(-2deg);
                                -ms-transform: rotateZ(-2deg);

                            }

                            .avatar {
                                width: 100px;
                                height: 100px;
                                margin: 10px auto 30px;
                                border-radius: 100%;
                                border: 2px solid #aaa;
                                background-size: cover;
                            }

                            .form-box input {
                                width: 100%;
                                padding: 10px;
                                text-align: center;
                                height: 40px;
                                border: 1px solid #ccc;
                                background: #fafafa;
                                transition: 0.2s ease-in-out;

                            }

                            .form-box input:focus {
                                outline: 0;
                                background: #eee;
                            }

                            .form-box input[type="text"] {
                                border-radius: 5px 5px 0 0;
                                text-transform: lowercase;
                            }

                            .form-box input[type="password"] {
                                border-radius: 0 0 5px 5px;
                                border-top: 0;
                            }

                            .form-box button.login {
                                margin-top: 15px;
                                padding: 10px 20px;
                            }

                            .animated {
                                -webkit-animation-duration: 1s;
                                animation-duration: 1s;
                                -webkit-animation-fill-mode: both;
                                animation-fill-mode: both;
                            }

                            @-webkit-keyframes fadeInUp {
                                0% {
                                    opacity: 0;
                                    -webkit-transform: translateY(20px);
                                    transform: translateY(20px);
                                }

                                100% {
                                    opacity: 1;
                                    -webkit-transform: translateY(0);
                                    transform: translateY(0);
                                }
                            }

                            @keyframes fadeInUp {
                                0% {
                                    opacity: 0;
                                    -webkit-transform: translateY(20px);
                                    -ms-transform: translateY(20px);
                                    transform: translateY(20px);
                                }

                                100% {
                                    opacity: 1;
                                    -webkit-transform: translateY(0);
                                    -ms-transform: translateY(0);
                                    transform: translateY(0);
                                }
                            }

                            .fadeInUp {
                                -webkit-animation-name: fadeInUp;
                                animation-name: fadeInUp;
                            }
                        </style>



