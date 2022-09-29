//메인 타입 변경시 서브타입이 알맞게 들어가도록
function changeSubType(){
    const mainType = document.getElementById('main-type');
    const mainTypeValue = mainType.options[mainType.selectedIndex].value;
    const subType = document.getElementById('sub-type');

    console.log(mainType)
    console.log(subType);
    if(mainTypeValue === "바다로드"){
        subType.innerHTML = `
                        <option value="인쇼어">인쇼어</option>
                        <option value="라이트지깅">라이트지깅</option>
                        <option value="볼락/락피쉬">볼락/락피쉬</option>
                        <option value="지깅로드">지깅로드</option>
                        <option value="선상캐스트로드">선상캐스트로드</option>
                        <option value="외수질/침선">외수질/침선</option>
                        <option value="타이라바">타이라바</option>
                        <option value="좌대용">좌대용</option>
                        <option value="두족류">두족류</option>

            `;
    }else if(mainTypeValue === "민물로드"){
        subType.innerHTML = `
                        <option value="스피닝로드">스피닝로드</option>
                        <option value="베이트로드">베이트로드</option>
                        <option value="계류로드">계류로드</option>
                        <option value="가물치로드">가물치로드</option>
                        <option value="빙어로드">빙어로드</option>
                        <option value="플라이로드">플라이로드</option>

            `;
    }else if(mainTypeValue === "원투낚시"){
        subType.innerHTML = `
                        <option value="원투로드">원투로드</option>
                        <option value="원투릴">원투릴</option>
                        <option value="원투채비">원투채비</option>
                        <option value="원투소품">원투소품</option>
            `;
    }else if(mainTypeValue === "릴/용품"){
        console.log('릴용품')
        subType.innerHTML = `
                        <option value="스피닝릴">스피닝릴</option>
                        <option value="베이트릴">베이트릴</option>
                        <option value="전동릴">전동릴</option>
                        <option value="빙어릴">빙어릴</option>
                        <option value="전동릴부품">전동릴부품</option>
                        <option value="튜닝용품">튜닝용품</option>
                        <option value="플라이">플라이</option>
            `;
    }else if(mainTypeValue === "라인/용품"){
        subType.innerHTML = `
                        <option value="카본라인">카본라인</option>
                        <option value="합사라인">합사라인</option>
                        <option value="모노라인">모노라인</option>
                        <option value="하이브리드라인">하이브리드라인</option>
                        <option value="플라이라인">플라이라인</option>
                        <option value="라인결속기">라인결속기</option>
                        <option value="플라이">플라이</option>
            `;
    }else if(mainTypeValue === "바늘/훅"){
        subType.innerHTML = `
                        <option value="지그헤드">지그헤드</option>
                        <option value="다운샷">다운샷</option>
                        <option value="와이드갭">와이드갭</option>
                        <option value="스트레이트">스트레이트</option>
                        <option value="플래시스위머">플래시스위머</option>
                        <option value="웜스프링">웜스프링</option>
                        <option value="플라이">플라이</option>
                        <option value="타이라바">타이라바</option>
            `;
    }else{
        //기타일 시
        subType.innerHTML = `
                        <option value="기타">기타</option>
            `;
    }
}