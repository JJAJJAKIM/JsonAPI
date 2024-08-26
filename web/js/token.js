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
                console.log(res);
            },
            error: function(res) {
                console.log(res);
            }
        });
    }

});