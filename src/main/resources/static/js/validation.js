    // jquery 가져와서 사용하는 곳
    let script = document.createElement('script');
    script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
    script.type = 'text/javascript';
    document.getElementsByTagName('head')[0].appendChild(script);
    
    // 공백 정규식
    // var regExp = /^[0-9]+$/;

    //  영문 대문자, 소문자, 숫자, 문자 사이 공백 및 특수문자.
    // /^[a-zA-Z0-9-_/,.][a-zA-Z0-9-_/,. ]*$/;

    //  아이디 체크(영문자 또는 숫자 6~20자)
    //  /^[a-z]+[a-z0-9]{5,19}$/g;

    //blur란? input태그에서 focus가 벗어나면? 이벤트가 발생하도록 하는 것

    let useridCheck =  /^[a-z]+[a-z0-9]{5,19}$/g; //[A-Za-z\d]{4,15}$/; // 최소4자, 최대 15자의 문자

    let passwordCheck = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/; //최소 8 자, 최소 하나의 문자 및 하나의 숫자

    let numberCheck = /[0-9]/;	// 숫자인경우

    let nameCheck = /[a-zA-Z가-힣]/; // 한글, 영어만


    <!-- ID입력 시 정규식으로 확인하는 함수-->
    function checkId(){
        const id = document.getElementById('userID').value; // ID값이 id인 입력란의 값을 저장
        console.log(useridCheck.test(id));
        if(useridCheck.test(id)) {
            $.ajax({
                url:'/test/index', //Controller에서 인식할 주소
                type:'post',
                data: {userid:id},
                success:function(result){ //컨트롤러에서 넘어온 cnt값을 받는다
                    if(result != -1){ //cnt가 -1이 아니면(=1일 경우) -> 사용 가능한 아이디
                        $('.id_check').css("display","block");
                        $('.id_check').css("color","#6A82FB");
                        $('.id_check').text("사용가능한 아이디입니다.");
                    } else { // cnt가 -1일 경우 -> 이미 존재하는 아이디
                        $('.id_check').css("display","block");
                        $('.id_check').css("color","#B02A37");
                        $('.id_check').text("이미 존재하는 아이디입니다.");
                    }
                },
                error:function(){
                }
            });
        }else{
            $('.id_check').css("display","block");
            $('.id_check').css("color","#B02A37");
            $('.id_check').text("공백 및 특수문자를 제외한 영문, 숫자 4~10자를 입력해주세요!");
        }

    };

    //userID에서 focus가 벗어나면 뜸
    $('#userID').blur(function(){
        if(!useridCheck.test($('#userID').val())){
            $('#userID').removeClass('_success');
            $('#userID').addClass('_error');
        }else{
            $('#userID').removeClass('_error');
            $('#userID').addClass('_success');
        }
    });

    <!-- 비밀번호 정규식 체크하는 부분-->
    $('#reg-pass').blur(function() {
        const password1 = document.getElementById('reg-pass').value; // ID값이 id인 입력란의 값을 저장
        console.log(passwordCheck.test(password1));
        if(passwordCheck.test(password1)){
            <!--비밀번호 정규화 true-->
            $('.pw_check').css("display","none");
            $('#reg-pass').removeClass("_error");
            $('#reg-pass').addClass("_success");
        }else{
            <!-- 비밀번호 정규화 false-->
            $('.pw_check').css("display","block");
            $('.pw_check').css("color","#B02A37");
            $('.pw_check').text("숫자, 영문, 특수문자 포함 최소8자 이상입력해주세요!");
            $('#reg-pass').removeClass("_success");
            $('#reg-pass').addClass("_error");
        }
    });

    
    <!-- 두번째 비밀번호 input에  입력시 비밀번호 두 개가 맞는지 확인하는 함수-->
    function passwordCheckFunction() {

        let password1 = $('#reg-pass').val();
        let password2 = $('#reg-pass-confirm').val();

        if(password1 != password2) {
            $('.pw_confrimcheck').css("display","block");
            $('.pw_confrimcheck').css("color","#B02A37");
            $('.pw_confrimcheck').text("비밀번호가 일치하지 않습니다!");
            $('#reg-pass-confirm').removeClass("_success");
            $('#reg-pass-confirm').addClass("_error");
        }  else{
            $('.pw_confrimcheck').css("display","none");
            $('#reg-pass-confirm').removeClass("_error");
            $('#reg-pass-confirm').addClass("_success");
        }

    };


    $('#username').blur(function(){

            console.log($('#username'),val());

            if($('#username').val().length < 4){

                $('#helper1').text("4자 이상 입력해주세요.");

            } if($('#username').val().length > 11) {

                $('#helper1').text("10자 이하로 입력해주세요.");

            }

        });


    $('#phone').blur(function(){

        console.log($('#phone').val());

        if(!numberCheck.test($('#phone').val())){

            $('#helper4').text("숫자만 입력 가능합니다.");

        }else{

            console.log("입력가능");

            $('#helper4').text("입력가능합니다.");

        }

    });



    function registerCheckFunction() {
        var username = $('#username').val();

        $.ajax({

            type: 'POST',

            url: './UserRegisterCheckServlet',

            data:  {username: username},

            success: function(result) {

                if(result == 1) {

                    $('#registerCheckMessage').html('*사용 가능한 아이디입니다.');

                    $('#registerCheckMessage').css('color','green');

                }

                else {

                    $('#registerCheckMessage').html('*이미 사용중인 아이디입니다.');

                    $('#registerCheckMessage').css('color','#d64643');

                }

            }



        });

    } ;


