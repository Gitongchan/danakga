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
    for(let subitem in cb_mainvalue){
        const ul = document.createElement("ul");
        const li = document.createElement("li");
        const setdiv = document.querySelector(".search_cate_list");
        ul.appendChild(li);
        li.id = cb_mainvalue[subitem].sub_value[subitem];
        li.innerText = cb_mainvalue[cb_mainvalue[subitem]];
        setdiv.appendChild(ul);
    }