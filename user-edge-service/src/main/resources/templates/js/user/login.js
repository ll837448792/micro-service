/*
*
*
*
* */

$(function () {

    $('#login_submit').click(function () {

        var loginUrl = "/login";

        var userName = $('#username').val();

        var password = document.getElementById("password").value;

        var formData = new FormData();
        formData.append('username', userName);
        formData.append('password', password);

        $.ajax({
            url: loginUrl,
            type: 'POST',
            // contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                $.toast("发布OK了：" + data);

            },
            error: function (retData) {
                $.toast("发布失败了：" + retData);
            }
        });

    })
})