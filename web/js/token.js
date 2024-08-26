$(document).ready(()=> {
    console.log("Token!!");

    const EVENT1 = () => {
        $.ajax({
            method: "GET",
            url: "http://localhost:80/token",
            beforeSend : function(xhr){
                xhr.setRequestHeader("Authorization", "Token");
            },
            success: function(res) {
                // console.log(res);
                // $("#token").text(res);
                // $("#tokens").empty();
                // res.split(".").forEach(e => $("#tokens").append(`<li class="text-break">${e}</li>`));
                localStorage.setItem("token", res);
                alert(localStorage.getItem("token"));
            },
            error: function(res) {
                console.log(res);
            }
        });
    }
    // EVENT1();
    const EVENT2 = (token) => {
        $.ajax({
            method: "POST",
            url: "http://localhost:80/token",
            data: {"token": token},
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Token");
            },
            success: function (res) {
                console.log(res);
            },
            error: function (res) {
                console.log(res);
            }
        });
    }
    $("#btn1").on("click", () => {
       EVENT1();
    });
    $("#btn2").on("click", ()=> {
       let token = localStorage.getItem("token");
       if (token == null){
           alert("토큰 없당~");
           return;
       }
       EVENT2(token);
       // alert("토큰 있당~");

    });
    localStorage.removeItem("token");
});