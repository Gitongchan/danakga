import {cb_mainvalue,cb_barnd} from "./dummydata.js";

const search_option_wrap = document.querySelector('.search_option_wrap');

const productList = [];

    for(let item in cb_mainvalue){
        const divWrap = document.createElement("div");
        divWrap.classList.add("search_option_list");
        const divTitle = document.createElement("div");
        divWrap.appendChild(divTitle);
        divTitle.classList.add("search_cate_title");
        const divList = document.createElement("div");
        divList.classList.add("search_cate_list");
        divTitle.innerText = cb_mainvalue[item].p_name;
        search_option_wrap.appendChild(divWrap);
        divWrap.appendChild(divList);
    }
    // 밑밥 부분 객체를 돌며 안에있는 값을 뿌려준다.
    for(let subitem in cb_mainvalue.chum){
        // ul태그를 만들어줌
        const ul = document.createElement("ul");
        // li태그를 만들어줌
        const li = document.createElement("li");
        // .search_cate_list라는 클래스가 있는 노드를 찾는다.
        const setdiv = document.querySelector(".search_cate_list");
        // ul안에 li태그를 넣는다.
        ul.appendChild(li);
        // li의 id,text 정해주기
        li.id = cb_mainvalue.chum[subitem].sub_value;
        li.innerText = cb_mainvalue.chum[subitem].sub_value;
        //찾은 div노드 안에 ul태그를 넣어준다.
        setdiv.appendChild(ul);
    }

    // 브랜드 -> 브랜드에 관한 세부종류
    // 낚시대 -> 낚시대에 관한 세부종류
    /*
        주 메뉴를 뿌리고, 그 해당하는 메뉴마다 for in 을 돌려서 메뉴 객체 안에 있는 세부 메뉴를 뿌린다?
        그렇다면, 메뉴 하나하나 다 for in문을 만들어서 돌려야하나 ?
    */