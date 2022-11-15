<template>
    <div>
        <div id="container">
            <div class="slide_wrap">
                <div class="slide_box">
                    <div class="slide_list clearfix">
                        <div class="slide_content slide01">
                            <p>1</p>
                        </div>
                        <div class="slide_content slide02">
                            <p>2</p>
                        </div>
                        <div class="slide_content slide03">
                            <p>3</p>
                        </div>
                        <div class="slide_content slide04">
                            <p>4</p>
                        </div>
                        <div class="slide_content slide05">
                            <p>5</p>
                        </div>

                        <!-- 이부분을 v-for로 생성 mount에서 
                            data table 만들어서

                            export default{
                                name: "TestSlider",
                                props: "images", 
                                // 이게 부모 컴포넌트(MapSidbar2로 부터 받아올 data부분)
                            }
                        -->
                    </div>
                <!-- // .slide_list -->
                </div>
                <!-- // .slide_box -->
                <div class="slide_btn_box">
                    <button type="button" class="slide_btn_prev">P</button>
                    <button type="button" class="slide_btn_next">N</button>
                </div>
                <!-- // .slide_btn_box -->
                <ul class="slide_pagination"></ul>
                <!-- // .slide_pagination -->
            </div>
            <!-- // .slide_wrap -->
        </div>
        <!-- // .container -->
    </div>
</template>


<script>
//https://codepen.io/kkt9102/pen/XWXbqKV 참고 위치 
export default{
    mounted(){
        (function () {
    const slideList = document.querySelector('.slide_list');  // Slide parent dom
    console.log("slideList :", slideList);
    const slideContents = document.querySelectorAll('.slide_content');  // each slide dom
    console.log("slideContent :",slideContents);
    const slideBtnNext = document.querySelector('.slide_btn_next'); // next button
    console.log("slideBtnNext :", slideBtnNext);
    const slideBtnPrev = document.querySelector('.slide_btn_prev'); // prev button
    console.log("slidBtnPrev :", slideBtnPrev);
    const pagination = document.querySelector('.slide_pagination');
    console.log("pagination :", pagination);
    const slideLen = slideContents.length;  // slide length
    const slideWidth = 200; // slide width
    const slideSpeed = 900; // slide speed
    const startNum = 0; // initial slide index (0 ~ 4)
    
    slideList.style.width = slideWidth * (slideLen + 2) + "px";
    
    // Copy first and last slide
    let firstChild = slideList.firstElementChild;
    let lastChild = slideList.lastElementChild;
    let clonedFirst = firstChild.cloneNode(true);
    let clonedLast = lastChild.cloneNode(true);
    // cloneNode는 엘리먼트, 노드를 복제하여 사용한다, 즉 동일한 내용의 노드를 만들어낸다, true는 자식 노드를 함께 복사할 것인지 아닌지에 대한 옵션 선택 

    // Add copied Slides
    slideList.appendChild(clonedFirst);
    slideList.insertBefore(clonedLast, slideList.firstElementChild);
    // insertBefore는 여기 참고 https://developer.mozilla.org/ko/docs/Web/API/Node/insertBefore

    // Add pagination dynamically
    let pageChild = '';
    for (var i = 0; i < slideLen; i++) {
    pageChild += '<li class="dot';
    pageChild += (i === startNum) ? ' dot_active' : '';
    if(i==0 || i==slideLen){
        pageChild += '" display="none"';
    }else{
        pageChild += '"';
    }
    pageChild += ' data-index="' + i + '"><a href="#"></a></li>';
    }
    console.log(pageChild);
    pagination.innerHTML = pageChild;
    // 0~slidLen-1까지의 동일한 노드들 생성해서 html의 밑에 더하기
    const pageDots = document.querySelectorAll('.dot'); // each dot from pagination

    slideList.style.transform = "translate3d(-" + (slideWidth * (startNum + 1)) + "px, 0px, 0px)";
    // 초기 위치로 이동 즉 0번 노드로 이동 

    let curIndex = startNum; // current slide index (except copied slide)
    let curSlide = slideContents[curIndex]; // current slide dom
    curSlide.classList.add('slide_active');
    // 초기 위치 설정
    // 초기 위치 가져와서 class명에 slied_active 추가 

    /** Next Button Event */
    slideBtnNext.addEventListener('click', function() {
        if (curIndex <= slideLen - 1) {
            slideList.style.transition = slideSpeed + "ms";
            // slideSpeed가 클수록 더 느려짐, css속성 더하기
            slideList.style.transform = "translate3d(-" + (slideWidth * (curIndex + 2)) + "px, 0px, 0px)";
            // translate3d : 현재 위치에서 해당 요소를 주어진 x축과 y축, z축의 거리만큼 이동시킵니다 주어진 거리가 양수이면 해당 축의 양의 방항, 음수이면 음의 방향 이동 
            // 여기서는 우로 이동하는거니까 (기존에 있는게 좌측으로 밀려남으로) index는 0,1,2,3이므로 이를 움직이기 위해서는 
            // slideWidth에다가 curIndex(0,1,2,3)+1을 하면 현제 index, 다시 +1을 하면 다음 인덱스가 됨 

        }
        if (curIndex === slideLen - 1) {
            // 마지막일때 
            setTimeout(function() {
            slideList.style.transition = "0ms";
            slideList.style.transform = "translate3d(-" + slideWidth + "px, 0px, 0px)";
            }, slideSpeed);
            curIndex = -1;
        }

        console.log("usually :" ,curSlide);
        curSlide.classList.remove('slide_active');
        console.log("removed :" ,curSlide);
        pageDots[(curIndex === -1) ? slideLen - 1 : curIndex].classList.remove('dot_active');
        // pageDots에서 이동하기 전의 패이지의 class명중 dot_active제거 
        curSlide = slideContents[++curIndex];
        // curSlide가 가르키는 위치를 다음 Contents로 변경
        curSlide.classList.add('slide_active');
        pageDots[curIndex].classList.add('dot_active');
        // 위아래 전부 해당하는 번호의 div class명에 각각 slide_active와 dot_active추가 
    });

    /** Prev Button Event */
    slideBtnPrev.addEventListener('click', function() {
    if (curIndex >= 0) {
        slideList.style.transition = slideSpeed + "ms";
        slideList.style.transform = "translate3d(-" + (slideWidth * curIndex) + "px, 0px, 0px)";
    }
    if (curIndex === 0) {
        setTimeout(function() {
        slideList.style.transition = "0ms";
        slideList.style.transform = "translate3d(-" + (slideWidth * slideLen) + "px, 0px, 0px)";
        }, slideSpeed);
        curIndex = slideLen;
    }
    curSlide.classList.remove('slide_active');
    pageDots[(curIndex === slideLen) ? 0 : curIndex].classList.remove('dot_active');
    curSlide = slideContents[--curIndex];
    curSlide.classList.add('slide_active');
    pageDots[curIndex].classList.add('dot_active');
    });

    /** Pagination Button Event */
    let curDot;
    Array.prototype.forEach.call(pageDots, function (dot, i) {
    dot.addEventListener('click', function (e) {
        console.log("i",i);
        e.preventDefault();
        curDot = document.querySelector('.dot_active');
        curDot.classList.remove('dot_active');
        
        curDot = this;
        this.classList.add('dot_active');

        curSlide.classList.remove('slide_active');
        curIndex = Number(this.getAttribute('data-index'));
        curSlide = slideContents[curIndex];
        curSlide.classList.add('slide_active');
        slideList.style.transition = slideSpeed + "ms";
        slideList.style.transform = "translate3d(-" + (slideWidth * (curIndex + 1)) + "px, 0px, 0px)";
    });
    });
})();
    }
}
</script>



<style>

    *, *:before, *:after { box-sizing: inherit; }
    .clearfix:after { content: ''; display: block; clear: both; float: none; }
    /* container - body */
    #container { width: 600px; margin: auto; margin: 20px 0px}
    .slide_wrap { position: relative; width: 200px; margin: auto;}
    .slide_box { width: 100%; margin: auto; overflow-x: hidden; }
    .slide_content { display: table; float: left; width: 200px; height: 200px; }
    .slide_content > p { display: table-cell; vertical-align: middle; text-align: center; font-size: 20px; font-weight: bold; color: #555; }
    .slide_content.slide01 { background: #ddbdff; }
    .slide_content.slide02 { background: #9fd6c2; }
    .slide_content.slide03 { background: #abe2f7; }
    .slide_content.slide04 { background: #f08c78; }
    .slide_content.slide05 { background: #fbdb65; }
    .slide_btn_box > button { position: absolute; top: 50%; margin-top: -10px; width: 20px; height: 20px; font-size: 16px; color: #999; background: none; border: 1px solid #ddd; cursor: pointer; }
    .slide_btn_box > .slide_btn_prev { left: -30px; }
    .slide_btn_box > .slide_btn_next { right: -30px; }
    .slide_pagination { position: absolute; left: 50%; bottom: -20px; list-style: none; margin: 0; padding: 0; transform: translateX(-50%); display: flex;}

    .slide_pagination .dot { display: inline-block; width: 10px; height: 10px; margin: 0 5px; overflow: hidden; background: #ddd; border-radius: 50%; transition: 0.3s; }
    .slide_pagination .dot_active { background: #333; }
    .slide_pagination .dot a { display: block; width: 100%; height: 100%; }

</style>