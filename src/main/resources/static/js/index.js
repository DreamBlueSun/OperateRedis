$(function () {
    $("#connect").click(function () {
        var host = nextPath + $("#host").val();
        var port = nextPath + $("#port").val();
        if (host.length < 2 || port.length < 2) {
            alert("未填写host或port");
            return;
        }
        var password = nextPath + $("#password").val();
        var url = pathHead + "operateRedis/connect" + host + port + password;
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
                alert(data);
                if (data == "连接成功") {
                    $("#div_connect").hide();
                    $("#div_operation").show();
                }
            }
        });
    });
    $("#disconnect").click(function () {
        var url = pathHead + "operateRedis/disconnect";
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
                alert(data);
                if (data == "成功") {
                    $("#div_table").hide();
                    $("#div_operation").hide();
                    $("#div_connect").show();
                }
            }
        });
    });
    $("#keys").click(function () {
        $('#keysTable').bootstrapTable('destroy');
        $("#div_table").show();
        $("#keysTable").bootstrapTable({
            url: pathHead + "operateRedis/keySet",
            method: "get",
            dataType: "json",
            height: 560, /**表格的高度*/
            pagination: true, /**表格展示分页*/
            pageSize: 10, /**分页的时候设置每页的条数*/
            pageList: [5, 10], /**分页的时候设置分页数的列表*/
            smartDisplay: true, /**默认为true，会机智地根据情况显示分页（pagination）或卡片视图（card view）*/
            search: true, /**显示搜索框*/
            showRefresh: false, /**刷新按钮*/
            striped: false, /**表格偶数行背景稍微灰色*/
            cache: false, /**使用AJAX请求的缓存。*/
            sidePagination: 'client', /**设置在哪进行分页，默认”client”，可选”server”，如果设置 “server”，则必须设置url或者重写ajax方法*/
            formatLoadingMessage: function () {
                return "请稍等，正在加载中...";
            },
            formatNoMatches: function () {
                return '没有KEY';
            },
            columns: [
                {
                    checkbox: true
                },
                {
                    field: "key",
                    title: "KEY",
                    align: 'center'
                }]
        });
    });
    $("#get").click(function () {
        var key = nextPath + $("#key").val();
        var url = pathHead + "operateRedis/get" + key;
        $.ajax({
            url: url,
            type: "GET",
            success: function (data) {
                $("#text_value").text(data);
            }
        });
    });
});