<template>
    <div>
        <LogoutTopTitle/>
        <div class="main-banner">
            <div class="row main-banner-content">
                <h1>나눔 광장</h1>
                <div class="subtitle">필요 없는<span class="braket">[</span>
                    <div class="text-animation">
                        <span class="label">
                            <span class='letter'></span>
                            <span class='letter'></span>
                            <span class='letter'></span>
                            <span class='letter'></span>
                            <span class='letter'></span>
                            <span class='letter'></span>
                        </span>
                    </div>
                <span class="braket">]</span>도 주인 찾아주기</div>
                <div class="banner-button">
                    <a @click="() => {this.$router.push({path:'/map'})}" class="button">필요 없는 물건 나눠주기</a>
                    <a @click="() => {this.$router.push({path:'/map'})}" class="button">필요한 물건 찾아보기</a>
                </div>
            </div>
        </div>
        <section class="content">
            <article class="row about">
                <h1>나눔 광장이 무엇인가요?</h1>
                <p>나눔 광장은 지역 기반의 물건 재활용 서비스입니다.</p>
                <p>스티로폼이나 아이스팩 등 일회용품부터 필요 없는 물건까지 필요한 사람에게 전달해보세요.</p>
            </article>
            <article class="share-list-area">
                <div class="row share-list">
                    <h1>👓 지금 공유 중인 물건들</h1>
                    <div class="listview">
                        <div class="item"></div>
                        <div class="item"></div>
                        <div class="item"></div>
                        <div class="item"></div>
                    </div>
                </div>
            </article>
            <article class="row pr">
                <div>
                    <img alt="빈 종이박스 그림" src="../assets/main-emptybox.jpg"/>
                    <h2>택배 받고 남은 종이박스</h2>
                    <p>누군가는 필요할 종이박스와 아이스팩</p>
                    <img alt="자취방 그림" src="../assets/main-room.jpg"/>
                    <h2>자취방 처지곤란 물품</h2>
                    <p>자취방 정리하며 버릴까 고민되는 테이블과 청소기</p>
                    <img alt="옷 그림" src="../assets/main-recycle.jpg"/>
                    <h2>버리긴 아까운 물건들</h2>
                    <p>에코백과 텀블러 쌓이는데 쓸데는 없다</p>
                </div>
                <div class="right-area">
                    <span class="colorbar"></span>
                    <h1 class="right">🤔 어떤 물건을 공유하지?</h1>
                </div>
            </article>
            <article class="mirror">
                <div class="row pr pr-reverse">
                    <div class="left-area">
                        <span class="colorbar colorbar-reverse"></span>
                        <h1 class="left">이런 물건이 필요하다면 😆</h1>
                    </div>
                    <div class="right-flex">
                        <img alt="택배박스 그림" src="../assets/main-newbox.jpg"/>
                        <h2>택배 보낼 때 필요한 종이박스</h2>
                        <p>필요할 땐 꼭 없는 종이박스나 스티로폼박스</p>
                        <img alt="자취방 그림" src="../assets/main-kitchen.jpg"/>
                        <h2>누가 주면 냉큼 받는 자취용 가구</h2>
                        <p>밥상용 테이블이나 의자는 사려면 비싸</p>
                        <img alt="옷 그림" src="../assets/main-cloth.jpg"/>
                        <h2>누군가의 사이즈 안 맞는 옷</h2>
                        <p>사이즈를 맞추지 말고 주인을 바꾸면 돼</p>
                    </div>
                </div>
            </article>
            <article class="row">
                <div class="start">
                    <h1>나눔 광장 시작하기</h1>
                    <a v-if="!this.$store.state.Islogin.is_login" @click="() => {this.$router.push({path:'/signup'})}">여기를 눌러 회원가입</a>
                    <a v-else @click="() => {this.$router.push({path:'/map'})}">여기를 눌러 시작하기</a>
                </div>
            </article>
            <footer class="footer">
                <p>Crafted by 팀 <u>자바섬 세얼간이</u></p>
            </footer>
        </section>
    </div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import anime from 'animejs/lib/anime.es.js';

export default {
    mounted() {
        function setLabel(label) {
            const emojiRegex = /(?:[\u2700-\u27bf]|(?:\ud83c[\udde6-\uddff]){2}|[\ud800-\udbff][\udc00-\udfff]|[\u0023-\u0039]\ufe0f?\u20e3|\u3299|\u3297|\u303d|\u3030|\u24c2|\ud83c[\udd70-\udd71]|\ud83c[\udd7e-\udd7f]|\ud83c\udd8e|\ud83c[\udd91-\udd9a]|\ud83c[\udde6-\uddff]|\ud83c[\ude01-\ude02]|\ud83c\ude1a|\ud83c\ude2f|\ud83c[\ude32-\ude3a]|\ud83c[\ude50-\ude51]|\u203c|\u2049|[\u25aa-\u25ab]|\u25b6|\u25c0|[\u25fb-\u25fe]|\u00a9|\u00ae|\u2122|\u2139|\ud83c\udc04|[\u2600-\u26FF]|\u2b05|\u2b06|\u2b07|\u2b1b|\u2b1c|\u2b50|\u2b55|\u231a|\u231b|\u2328|\u23cf|[\u23e9-\u23f3]|[\u23f8-\u23fa]|\ud83c\udccf|\u2934|\u2935|[\u2190-\u21ff])/g;
            const bannerLabel = document.querySelector('.text-animation>.label');
            if (!bannerLabel) return;
            const letters = Array.prototype.slice.call(bannerLabel.children);
            let l = 0;
            for (const letter of letters) {
                if (l < label.length - 1 && emojiRegex.test(label[l] + label[l + 1])) {
                    letter.innerText = label[l] + label[l + 1];
                    l += 2;
                } else if (l < label.length) {
                    letter.innerText = label[l];
                    l++;
                } else {
                    letter.innerText = '';
                }
            }
        }

        const labels = [
            '📦종이박스',
            '🧊아이스팩',
            '👜에코백',
            '👕티셔츠',
            '🧺바구니',
            '🧻화장지',
        ];
        let labelNowIndex = 0;

        setLabel(labels[labelNowIndex]);

        anime.timeline({
            loop: true,
            loopComplete: function() {
                labelNowIndex++;
                if (labels.length <= labelNowIndex)
                    labelNowIndex = 0;
                setLabel(labels[labelNowIndex]);
            }
        }).add({
            targets: '.label .letter',
            translateY: [-20,0],
            opacity: [0, 1],
            easing: "easeOutExpo",
            duration: 500,
            delay: (el, i) => 60 * i
        }).add({
            targets: '.label .letter',
            translateY: 0,
            opacity: 1,
            duration: 1000,
            delay: (el, i) => 60 * i
        }).add({
            targets: '.label .letter',
            translateY: [0,10],
            opacity: 0,
            duration: 500,
            easing: "easeOutExpo",
            delay: (el, i) => 60 * i,
        });
    },
    beforeUnmount() {
        anime.remove(".label .letter");
    },
    name:"MainPage",
    components: {
        LogoutTopTitle
    }
};
</script>

<style scoped>

.row {
    max-width: 960px;
    margin: 0 auto;
    padding: 0 20px;
}
.main-banner {
    background-color:lightblue;
    width: 100%;
    height: 450px;
    padding-top: 50px;
}
.main-banner-content {
    margin-top: 100px;
}
.main-banner h1 {
    font-size: 48px;
    margin: 0 0;
}
.main-banner .subtitle {
    margin-top: 20px;
    font-size: 24px;
    font-weight: bold;
}
.main-banner .braket {
    display: inline-block;
    color: #555555;
    opacity: 0.4;
    margin: 0 4px;
}
.banner-button {
    margin-top: 60px;
}
.banner-button a {
    display: inline-block;
    margin-right: 16px;
    padding: 8px 20px;
    color: white;
    background-color: black;
    border-radius: 20px;
}
.banner-button a:hover {
    cursor: pointer;
    color: #5EDB97;
}

.text-animation {
    display: inline-block;
    width: 160px;
    margin: 0 10px;
    text-align: center;
}
.text-animation .label {
    display: inline-block;
}
.text-animation .label>.letter {
    display: inline-block;
}

.content {
    width: 100%;
}
.content>article {
    margin-top: 60px;
}
.content>article h1 {
    font-size: 24px;
    margin: 0;
    margin-bottom: 20px;
}

.about p {
    margin: 0 0;
    font-size: 15px;
    line-height: 1.8;
    word-break: keep-all;
    color: #555555;
}

.content>.share-list-area {
    background-color: #f8f9fa;
    padding: 40px 0;
}
.share-list .listview {
    display: flex;
    margin: 30px -10px;
}
.share-list .listview>.item {
    background-color: white;
    display: inline-block;
    width: 100%;
    height: 240px;
    margin: 0 10px;
    border: 1px solid #6BD69C;
    box-sizing: border-box;
}

.pr {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}
.pr> div {
    width: 400px;
}
.pr .right-area {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    position: sticky;
    top: 80px;
}
.pr .right {
    width: 230px;
    font-size: 32px;
    text-align: right;
    word-break: keep-all;
}
.pr h2 {
    font-size: 20px;
    margin-bottom: 6px;
}
.pr p {
    margin: 0 0;
    font-size: 15px;
    color: #555555;
}
.pr img {
    width: 400px;
    margin-top: 40px;
    border-radius: 16px;
}

.content>.mirror {
    background-color: #f8f9fa;
    padding: 40px 0;
}
.pr .left-area {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    position: sticky;
    top: 80px;
}
.pr .left {
    width: 220px;
    font-size: 32px;
    word-break: keep-all;
}
.mirror .right-flex {
    text-align: right;
}

.start {
    background-color: #6BD69C;
    width: 100%;
    padding: 20px;
    border-radius: 16px;
    color: white;
    text-align: center;
    box-sizing: border-box;
}
.start a {
    cursor: pointer;
    text-decoration: underline;
}

.footer {
    margin-top: 120px;
    padding: 0 0 40px;
    text-align: center;
}
.footer p {
    color: #555555;
    font-size: 14px;
}

.colorbar {
    background: linear-gradient(to right, #BFF7B0, #6BD69C);
    display: inline-block;
    width: 80px;
    height: 4px;
    margin-bottom: 8px;
    border-radius: 2px;
}
.colorbar-reverse {
    background: linear-gradient(to right, #6BD69C, #BFF7B0);
}

@media only screen and (max-width:738px) {
    .row {
        max-width: 100%;
        padding: 0 15px;
    }

    .main-banner h1 {
        font-size: 36px;
        text-align: center;
    }
    .main-banner .subtitle {
        font-size: 18px;
        text-align: center;
    }
    .text-animation {
        width: 110px;
        margin: 0 5px;
    }
    .banner-button {
        text-align: center;
    }
    .banner-button a {
        margin-bottom: 10px;
    }

    .content>article h1 {
        text-align: center;
    }

    .share-list .listview {
        flex-wrap: wrap;
        margin: 0 0 -24px
    }
    .share-list .listview>.item {
        width: calc(50% - 12px);
        margin: 0 6px 24px;
    }

    .pr {
        flex-direction: column-reverse
    }
    .pr-reverse {
        flex-direction: column
    }
    .pr>div {
        max-width: 100%;
        margin: 0 auto;
    }
    .pr img {
        width: 100%;
        max-width: 400px;
        margin-top: 40px;
    }
    .pr .right-area {
        position: relative;
        top: 0;
    }
    .pr .left-area {
        position: relative;
        top: 0;
    }
    .pr .right {
        width: 100%;
        font-size: 24px;
    }
    .pr .left {
        width: 100%;
        text-align: left;
        font-size: 24px;
    }
}

</style>