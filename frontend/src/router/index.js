import { createWebHistory, createRouter } from "vue-router";
import CommunityPage from "@/views/CommunityPage"
import WritePage from "@/views/WritePage"
import UpdatePage from "@/views/UpdatePage"
import MainPage from "@/views/MainPage"
import SignUpEndPage from "@/views/SignUpEndPage"
import SignUpPage from "@/views/SignUpPage"
import FindPage from "@/views/FindPage"
import LoginPage from "@/views/LoginPage"
import MyArticlePage from "@/views/MyArticlePage"
import UserPage from "@/views/UserPage"
import MapPage from "@/views/MapPage"
import ComuViewPage from "@/views/ComuViewPage"
import ShareWritePage from "@/views/ShareWritePage"
import CommentPage from "@/views/CommentPage"
import ChattingPage from "@/views/ChattingPage"
import AdminPage from "@/views/AdminPage"

const routes= [ // path별 component를 추가한다.
        { path : "/" , name:"MainPage", component:MainPage},
        { path : "/community", name : "CommunityPage", component : CommunityPage },
        { path : "/community/write", name : "WritePage", component : WritePage },
        { path : "/community/update/:updateId", name : "UpdatePage", component : UpdatePage },
        { path : "/signup/end", name : "SignUpEndPage", component : SignUpEndPage },
        { path : "/signup", name : "SignUpPage", component : SignUpPage },
        { path : "/find", name : "FindPage", component : FindPage },
        { path : "/login" , name : "LoginPage", component : LoginPage },
        { path : "/user/article", name : "MyArticlePage", component : MyArticlePage},
        { path : "/user/page", name : "UserPage", component : UserPage},
        { path : "/map", name : "MapPage", component : MapPage},
        { path : "/community/view/:contentId", name : "ComuViewPage", component : ComuViewPage},
        { path : "/share/write/:func", name :"ShareWritePage", component: ShareWritePage},
        { path : "/community/comment/:contentId", name :"CommentPage", component: CommentPage},
        { path : "/chat", name:"ChattingPage", component: ChattingPage},
        { path : "/CBUN/javaIsland/threeDork", name:"AdminPage", component:AdminPage}
    ]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;

