import {cb_mainvalue,cb_barnd,category} from "./dummydata.js";

const search_option_wrap = document.querySelector('.search_option_wrap');

const productList = [];

const divWrap = document.createElement("div");//search category list 목록 공간
divWrap.classList.add("search_option_list");
    for(let index in cb_mainvalue){
        const item = cb_mainvalue[index];
        //카테고리 타이틀
        const divTitle = document.createElement("div");
        divTitle.classList.add("search_cate_title");
        divTitle.innerText = item["p_name"];
        divWrap.appendChild(divTitle);

        //카테고리 목록
        const divListWrap = document.createElement("div");
        divListWrap.classList.add("search_cate_list");
        const divList = document.createElement("ul");
        divListWrap.appendChild(divList);
        const sub_values = item["sub_value"];//실제 카테고리들

        for(let sub_value in sub_values){
            const list = document.createElement("li");
            const listContent = document.createElement("div");
            const checkBox = document.createElement("input");
            checkBox.setAttribute("type", "checkbox");
            listContent.appendChild(checkBox);
            const text = document.createElement("div");
            text.innerText = item["sub_value"][sub_value];
            listContent.appendChild(text);
            list.appendChild(listContent);
            divList.appendChild(list);
        }
        search_option_wrap.appendChild(divWrap);
        divWrap.appendChild(divList);

    }
