$(document).ready(()=> {
    console.log("Ready!");

    $("form").on("submit", function(event){
        event.preventDefault();
        console.log($("#userNm").val(), $("#userPwd").val());
        let params = {
            userNm : $("#userNm").val(),
            userPwd : $("#userPwd").val()

        }
        console.log(params);
        // {params} == {params : params}

        $.post("http://localhost:80/login", params )
            .done(res => {
                console.log(res)
                if(res.status) {
                    alert("로그인 성공 했다!");
                    $("#content2").removeClass("d-none");
                    $("#content1").addClass("d-none");
                    
                    $("nav li").eq(0).addClass("d-none");
                    $("nav li").eq(1).removeClass("d-none");
                    $("nav li").eq(2).removeClass("d-none");
                    $("nav li").eq(2).children("button").addClass("active");

                    $("#userName").val(res.user.userNm);

                    $("#roles").empty();
                    $.each(res.roles, (i, role) => {
                        let html = `<li class="list-group-item">${role.roleNm}</li>`;
                        $("#roles").append(html);
                    })
                } else {
                    alert("그런 사람 없다~");
                }
            })
            .fail(res => console.log(res));
/*
        $.ajax({
            type: "POST",
            url: "http://localhost:80/login",
            contentType: "application/json", // 데이터 타입 설정
            data: JSON.stringify(params),    // 데이터를 JSON 문자열로 변환
            datatype: "json"
        })
        .done(res => console.log(res))
        .fail(res => console.log(res));
*/
    });
});