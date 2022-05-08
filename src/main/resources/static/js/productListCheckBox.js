import {cb_mainvalue, cb_barnd, category} from "./dummydata.js";

const search_option_wrap = document.querySelector('.search_option_wrap');

const productList = [];

const optionDivWrap = document.createElement("div");//search category list 목록 공간
optionDivWrap.classList.add("search_option_list");
for (let index in cb_mainvalue) {
    const divWrap = document.createElement("div");
    divWrap.classList.add("search_option_item");
    const item = cb_mainvalue[index];
    //카테고리 타이틀
    const divTitle = document.createElement("div");
    divTitle.classList.add("search_cate_title");
    divTitle.innerText = item["p_name"];
    divWrap.appendChild(divTitle);

    //카테고리 목록
    const divListWrap = document.createElement("div");
    divListWrap.classList.add("search_cate_list");
    const ULList = document.createElement("ul");
    ULList.classList.add("cate_list");
    const sub_values = item["sub_value"];//실제 카테고리들
    divListWrap.appendChild(ULList);

    divWrap.appendChild(divListWrap);

    optionDivWrap.appendChild(divWrap);
    for (let sub_value in sub_values) {
        const list = document.createElement("li");
        list.classList.add("cate_item");
        const checkBox = document.createElement("input");
        checkBox.setAttribute("type", "checkbox");
        checkBox.id = sub_value;
        const text = document.createElement("span");
        text.innerText = item["sub_value"][sub_value];
        list.appendChild(checkBox);
        list.appendChild(text);
        ULList.appendChild(list);
    }
    search_option_wrap.appendChild(optionDivWrap);
}
