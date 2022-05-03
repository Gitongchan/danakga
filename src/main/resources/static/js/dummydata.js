const Product = {
    id_1:{
        pd_state: "재고있음",
        pd_type: "낚시대",
        pd_detail_type: "바다",
        pd_name:"최고의 낚시대",
        pd_photo:{
            img1 : "이미지경로1",
            img2 : "이미지경로2",
            img3 : "이미지경로3"
        },
        pd_content:"굉장히 강력한 낚시대입니다. 지금 사시면 굉장히 저렴합니다!",
        pd_price: 500000,
        pd_brand: "블랙이소",
        pd_stock: 10,
        pd_upload_date : "22-02-01",
        pd_view_cnt : 59,
        pd_order_cont : 1000
    },
    id_2:{
        pd_state: "재고있음",
        pd_type: "낚시대",
        pd_detail_type: "민물",
        pd_name:"최고의 민물 낚시대",
        pd_photo:{
            img1 : "이미지경로1",
            img2 : "이미지경로2",
            img3 : "이미지경로3"
        },
        pd_content:"민물에서 쓰기 좋은 낚시대입니다. 쉽게 부러지지 않아 많이 사용합니다!",
        pd_price: 290000,
        pd_brand: "블랙이소",
        pd_stock: 25,
        pd_upload_date : "21-12-25",
        pd_view_cnt : 106,
        pd_order_cont : 388
    },
    id_3:{
        pd_state: "재고없음",
        pd_type: "낚시대",
        pd_detail_type: "민물",
        pd_name:"민물 낚시대",
        pd_photo:{
            img1 : "이미지경로1",
            img2 : "이미지경로2",
            img3 : "이미지경로3"
        },
        pd_content:"가성비 낚시대입니다. 입문하시는 사람한테 딱 맞는 낚시대 입니다!",
        pd_price: 150000,
        pd_brand: "털보낚시",
        pd_stock: 17,
        pd_upload_date : "21-07-06",
        pd_view_cnt : 238,
        pd_order_cont : 197
    }
}

const testUser = {
    u_id1:{
        u_id: "1",
        u_userid: "test1234",
        u_password: "비밀",
        u_name: "홍길동",
        u_phone: "010-1234-5744",
        u_email: "test@naver.com",
        u_role: "사용자",
        u_adr_num: "132-111",
        u_def_adr: "서울시 종로구 oo동",
        u_detail_adr: "경민아파트 103동 805호"
    },
    s_id2:{
        u_id: "1",
        u_userid: "test1234",
        u_password: "비밀",
        u_name: "홍길동",
        u_phone: "010-1234-5744",
        u_email: "test@naver.com",
        u_role: "사업자",
        u_adr_num: "132-111",
        u_def_adr: "서울시 종로구 oo동",
        u_detail_adr: "oo상가",
        u_com_name: "강태공",
        u_com_num:"02-123-4343",
        u_com_adr_num: "144-223"
    }
}
const cb_barnd ={
        brand1:"블랙이소",
        brand2:"털보네",
        brand3:"용명"
}

const cb_mainvalue = {
        brand:{
            p_name:"브랜드",
            sub_value:{
                item1:"블랙이소",
                item2:"털보네",
                item3:"용명"
            }
        },
        fishingType:{
            p_name:"낚시종류",
            sub_value:{
                item1:"바다",
                item2:"민물",
                item3:"루어"
            }
        },
        rod:{
            p_name:"낚시대",
            sub_value : {
                item1:"강한낚시대",
                item2:"들 강한 낚시대",
                item3:"굉장히 강한 낚시대",
                item4:"엄청나게 강한 낚시대"
            }
        },
        reel:{
            p_name:"릴",
            sub_value : {
                item1:"스피닝릴",
                item2:"큰릴",
                item3:"릴릴",
                item4:"어쭈릴"
            }
        },
        line:{
            p_name:"라인",
            sub_value : {
                item1:"라인",
                item2:"하라인",
                item3:"주웅 라인",
                item4:"대라인"
            }
        },
        chum:{
            p_name:"밑밥",
            sub_value : {
                item1:"강한낚시대",
                item2:"들 강한 낚시대",
                item3:"굉장히 강한 낚시대",
                item4:"엄청나게 강한 낚시대"
            }
        },
        item7:"루어",
        item8:"의류",
        item9:"공통장비",
        item10:"바다소품"
}




export {Product, testUser, cb_mainvalue, cb_barnd}