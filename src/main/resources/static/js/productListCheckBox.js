import {cb_value} from "./dummydata.js";

const search_option_wrap = document.querySelector('.search_option_wrap');

const productList = [];

    for(let item in cb_value.mainvalue){
        const divWrap = document.createElement("div");
        divWrap.classList.add("search_option_list");
        const divTitle = document.createElement("div");
        divWrap.appendChild(divTitle);
        divTitle.classList.add("search_cate_title");
        const divList = document.createElement("div");
        divList.classList.add("search_cate_list");
        divTitle.innerText = cb_value.mainvalue[item];
        search_option_wrap.appendChild(divWrap);
        divWrap.appendChild(divList);
}
    for(let value1 in cb_value.mainsubvalue){
        const ul = document.createElement("ul");
        const li = document.createElement("li");
        const setdiv = document.querySelector(".search_cate_list");
        ul.appendChild(li);
        li.id = value1;
        li.innerText = cb_value.mainsubvalue.item1[cb_value.mainsubvalue[value1]];
        setdiv.appendChild(ul);
    }