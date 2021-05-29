<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Web Test Summary Report</title>
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css'>
    <link rel="stylesheet" href="../../../templates/css/style.css">
</head>
<style>

    <!--
    Start Of Header-- >
    body {
        font-family: 'Open Sans', sans-serif;
        font-weight: 300;
        text-align: center;
        color: #7f7e7e;
        background-color: #ececec;
        margin: 0;
        padding: 0;
    }

    h3 {

        text-align: center;
        color: #fff;
        font-size: 30px;
    }

    .content {
        max-width: 1200px;
        margin: 0 auto;
    }

    .report-statistic-box {
        float: left;
        width: 25%;
        height: 275px;
        background-color: #fafafa;
        border-right: 2px solid #ececec;
        text-align: center;
    }

    .report-statistic-box .box-header {
        background-color: #f2f2f2;
        font-weight: 400;
        font-size: 15px;
        height: 60px;
        padding-top: 20px;
    }

    .report-statistic-box .box-header span {
        display: inline-block;
        width: 25px;
        height: 25px;
        vertical-align: middle;
    }

    .report-statistic-box .box-header .icon-sent {
        background: url(../images/icon_sent.svg);
    }

    .report-statistic-box .box-header .icon-delivery {
        background: url(../images/icon_delivery.svg);
    }

    .report-statistic-box .box-header .icon-openrate {
        background: url(../images/icon_openrate.svg);
    }

    .report-statistic-box .box-header .icon-ctor {
        background: url(../images/icon_ctor.svg);
    }

    .report-statistic-box .box-content {
        position: relative;
        margin: 20px auto 15px;
        width: 130px;
        height: 130px;
    }

    .report-statistic-box .box-content .sentTotal {
        font-size: 46px;
        font-weight: 400;
        color: #80cdbe;
        padding-top: 32px;
    }

    .report-statistic-box .box-content .percentage {
        position: absolute;
        font-size: 28px;
        top: 34%;
        left: 31%;
    }

    .report-statistic-box .box-content .conversionValue {
        font-size: 28px;
        font-weight: 300;
        color: #f5ab34;
        padding-top: 46px;
    }

    .conversionValue .conversionCurrency {
        font-size: 18px;
        font-weight: 400;
        color: #f5ab34;
        padding-top: 46px;
    }

    .report-statistic-box .delivery-rate {
        color: #f5ab34;
    }

    .report-statistic-box .open-rate {
        color: #FF3F3A;
    }

    .report-statistic-box .click-to-open {
        color: #80cdbe;
    }

    .report-statistic-box .box-foot {
        position: relative;
        font-size: 13px;
        font-weight: 400;
        padding: 0 20px;
    }

    .report-statistic-box .box-foot .box-foot-stats {
        font-size: 15px;
    }

    .report-statistic-box .box-foot .box-foot-left {
        float: left;
        text-align: left;
    }

    .report-statistic-box .box-foot .box-foot-right {
        float: right;
        text-align: right;

    }

    .report-statistic-box .box-foot .box-foot-center {
        float: left;
        text-align: center;
        padding: 0 10px;
    }

    .report-statistic-box .box-foot .arrow {
        display: none;
        position: absolute;
        width: 15px;
        height: 15px;
    }

    @media (max-width: 1024px) {
        .report-statistic-box {
            width: 50%;
        }
    }

    <!--
    End of Header-- >
    html {
        position: relative;
        min-height: 100%;
    }

    body {
        margin: 0 0 80px;;
        font-family: 'Roboto';
        font-size: 14px;
        background: #455A64;
    }

    table, th, td {
        /*border: 1px solid gray;*/
        border-collapse: collapse;
    }

    table[id^='report'] th, table[id^='report'] td, table#total-report th {
        border: 1px solid #dddddd;
        padding: 5px;
        text-align: left;
        font-family: Arial;
        font-size: 15px;
    }

    table#total-report td {
        border: 1px solid #dddddd;
        padding: 5px;
        text-align: center;
        font-family: Arial;
        font-size: 15px;
    }

    table[id^='report'] th {
        color: black;
        background-color: #40E0D0;
    }

    tr:nth-child(odd) {
        background-color: #dddddd;
    }

    tr:nth-child(even) {
        background-color: #ffffff;
    }

    .failed {
        background: #FF3F3A;
    }

    .passed {
        background: #62C662;
    }

    .blocked {
        background: #A9A9A9;
    }

    h2 {
        color: #fff;
        font-size: 40px;
        text-align: left;
        margin-top: 20px;
        /*padding-bottom: 30px;*/
        /*border-bottom: 1px solid #eee;*/
        margin-bottom: 20px;
        font-weight: 300;
    }

    div#tb-total {
        text-align: left;
        /*margin-top: 30px;*/
        padding-bottom: 20px;
        border-bottom: 1px solid #eee;
        margin-bottom: 20px;
    }

    .container {
        max-width: 1500px;
        min-height: 100%;
        margin-bottom: 70px;
    }

    div[class*='col-'] {
        padding: 0 0px;
    }

    .wrap {
        box-shadow: 0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 3px 1px -2px rgba(0, 0, 0, 0.2), 0px 1px 5px 0px rgba(0, 0, 0, 0.12);
        border-radius: 4px;
    }

    a:focus,
    a:hover,
    a:active {
        outline: 0;
        text-decoration: none;
    }

    .panel {
        border-width: 0 0 1px 0;
        border-style: solid;
        border-color: #fff;
        background: none;
        box-shadow: none;
    }

    .panel:last-child {
        border-bottom: none;
    }

    .panel-group > .panel:first-child .panel-heading {
        border-radius: 4px 4px 0 0;
    }

    .panel-group .panel {
        border-radius: 0;
    }

    .panel-group .panel + .panel {
        margin-top: 0;
    }

    .panel-heading {
        /*background-color: #009688;*/
        border-radius: 0;
        border: none;
        color: #fff;
        padding: 0;
    }

    .test-case-pass {
        background-color: #009688;
    }

    .test-case-fail {
        background-color: #FF3F3A;
    }

    .test-case-pass:hover {
        background-color: #ffffff;
    }

    .test-case-fail:hover {
        background-color: #ffffff;
    }

    /*.panel-heading:hover {
    background-color: #ffffff;
    }*/
    .panel-title a {
        display: block;
        color: #fff;
        padding: 15px;
        position: relative;
        font-size: 16px;
        font-weight: 400;
    }

    .tests-title {
        display: block;
        color: #fff;
        padding: 15px;
        position: relative;
        font-size: 16px;
        font-weight: 400;
    }

    .tests-module {
        background-color: #5e8ccd;
    }

    .panel-title a:hover {
        color: #009688;
    }

    .panel-body {
        background: #fff;
    }

    .panel:last-child .panel-body {
        border-radius: 0 0 4px 4px;
    }

    .panel:last-child .panel-heading {
        border-radius: 0 0 4px 4px;
        -webkit-transition: border-radius 0.3s linear 0.2s;
        transition: border-radius 0.3s linear 0.2s;
    }

    .panel:last-child .panel-heading.active {
        border-radius: 0;
        -webkit-transition: border-radius linear 0s;
        transition: border-radius linear 0s;
    }

    /* #bs-collapse icon scale option */
    .panel-heading a:before {
        content: '\e146';
        position: absolute;
        font-family: 'Material Icons';
        right: 5px;
        top: 10px;
        font-size: 24px;
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
        -webkit-transform: scale(1);
        transform: scale(1);
    }

    .panel-heading.active a:before {
        content: ' ';
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
        -webkit-transform: scale(0);
        transform: scale(0);
    }

    #bs-collapse .panel-heading a:after {
        content: ' ';
        font-size: 24px;
        position: absolute;
        font-family: 'Material Icons';
        right: 5px;
        top: 10px;
        -webkit-transform: scale(0);
        transform: scale(0);
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
    }

    #bs-collapse .panel-heading.active a:after {
        content: '\e909';
        -webkit-transform: scale(1);
        transform: scale(1);
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
    }

    /* #accordion rotate icon option */
    #accordion .panel-heading a:before {
        content: '\e316';
        font-size: 24px;
        position: absolute;
        font-family: 'Material Icons';
        right: 5px;
        top: 10px;
        -webkit-transform: rotate(180deg);
        transform: rotate(180deg);
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
    }

    #accordion .panel-heading.active a:before {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
        -webkit-transition: all 0.5s;
        transition: all 0.5s;
    }

    footer, .push {
        margin: 0 auto;
        width: 100%;
        border-top: 1px solid lightgrey;
        bottom: 100;
        color: #ffffff;
        height: 70px;
        position: absolute;
        text-align: center;
        padding: 1.25em;
        overflow: hidden;
    }
</style>
<body onload="countTotal()">

<div class="container">
    <h3> Test Summary Report : ${suiteName}</h3>,
    <div class="content">
        <div class="report-overview-module"></div>
    </div>
    <br>
    <div class="col-md-12 col-sm-12">
        <div class="panel-group wrap" id="bs-collapse">
            <#assign count = 0>

            <!--        Start List 1-->
            <#list test as tkey,tvalues>
                <div class="panel-heading tests-module">
                    <h4 class="tests-title">
                        <table>
                            <tr>
                                <th bgcolor="#5e8ccd" width="20%">Test : ${tvalues.testName}</th>
                                <th width="20%" bgcolor="#5e8ccd">URL : ${tvalues.url}</th>
                                <th width="10%" bgcolor="#5e8ccd">Browser : ${tvalues.browser}</th>
                                <th width="10%" bgcolor="#5e8ccd">Groups : ${tvalues.groups}</th>
                            </tr>
                        </table>
                    </h4>
                </div>
                <!--        start class list 1-->
                <#list tvalues.classResultJsonList as tclass>
                    <#assign count++>
                    <div class="panel">
                        <div class="panel-heading test-case-pass">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#" href="#${count}-1">
                                    Module Under Test : ${tclass.className}
                                </a>
                            </h4>
                        </div>
                        <div id="${count}-1" class="panel-collapse collapse">
                            <table id="report_${count}-1" class="panel-body">
                                <tr>
                                    <th width="30%">TestMethodName</th>
                                    <th width="30%">ScenarioName</th>
                                    <th width="10%">Author</th>
                                    <th width="5%">ExecutionTime</th>
                                    <th width="5%">TestStatus</th>
                                    <th width="50%">Comments</th>
                                </tr>
                                <!--start method List -->
                                <#list tclass.methodResultJsonList as tmethod>
                                    <tr>
                                        <td><b>${tmethod.methodName}</b></td>
                                        <td>${tmethod.scenarioName}</td>
                                        <td>${tmethod.author}</td>
                                        <td>${tmethod.executionTime}</td>
                                        <#if tmethod.testStatus == "FAIL">
                                            <td class="failed">${tmethod.testStatus}</td>
                                        <#else>
                                            <td class="passed">${tmethod.testStatus}</td>
                                        </#if>
                                        <td>
                                            <#list tmethod.testComments as tcomments>
                                                ${tcomments}</br> </#list>
                                        </td>
                                    </tr>
                                </#list>
                                <!--End Method List-->
                            </table>
                        </div>
                    </div>
                </#list>
            </#list>

        </div>
    </div>
</div>

<footer>
    &copy; 2020 - Touch2Success Automation Team
</footer>
<script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js'></script>
<script src="../../../templates/js/index.js"></script>
<script src="https://www.gstatic.com/charts/loader.js" type="text/javascript"></script>
<script type="text/javascript">
    var totalTestcases;
    var totalModule;
    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
    }

    $(document).ready(function () {
        $('.collapse.in').prev('.panel-heading').addClass('active');
        $('#accordion, #bs-collapse')
            .on('show.bs.collapse', function (a) {
                $(a.target).prev('.panel-heading').addClass('active');
            })
            .on('hide.bs.collapse', function (a) {
                $(a.target).prev('.panel-heading').removeClass('active');
            });
    });

    $(document).ready(function () {
        var len = $('.panel:last').index() + 1;
        for (var i = 1; i <= len; i++) {
            var id = i + '-1';
            var result = $('div[id="' + id + '"] table tr td.failed').length;
            if (result > 0) {
                $('div[id="' + id + '"]').prev().removeClass('test-case-pass');
                $('div[id="' + id + '"]').prev().addClass('test-case-fail');
            }
        }
        ;
    });

    function countTotal() {
        var stepPass = document.evaluate('count(.//*[contains(@id,"report")]/tbody//td[5][text()="PASS"])', document, null, XPathResult.ANY_TYPE, null);
        <!--document.getElementById("total-report").rows[2].cells[2].innerHTML = stepPass.numberValue;-->

        var stepFail = document.evaluate('count(.//*[contains(@id,"report")]/tbody//td[5][text()="FAIL"])', document, null, XPathResult.ANY_TYPE, null);
        <!--document.getElementById("total-report").rows[3].cells[2].innerHTML = stepFail.numberValue;-->
        var stepIgnored = document.evaluate('count(.//*[contains(@id,"report")]/tbody//td[5][text()="IGNORED"])', document, null, XPathResult.ANY_TYPE, null);

        var casePass = document.evaluate('count(//div[contains(@class,"test-case-pass")])', document, null, XPathResult.ANY_TYPE, null);
        <!--document.getElementById("total-report").rows[2].cells[1].innerHTML = casePass.numberValue;-->

        var caseFail = document.evaluate('count(//div[contains(@class,"test-case-fail")])', document, null, XPathResult.ANY_TYPE, null);
        <!--document.getElementById("total-report").rows[3].cells[1].innerHTML = caseFail.numberValue;-->
        totalModule = casePass.numberValue + caseFail.numberValue
        <!--document.getElementById("total-report").rows[1].cells[1].innerHTML = totalModule;-->
        totalTestcases = stepPass.numberValue + stepFail.numberValue;
        <!--document.getElementById("total-report").rows[1].cells[2].innerHTML = totalTestcases;-->

        (function activateReportOverviewModule($) {
            "use strict";

            var $el = $(".report-overview-module");
            return new ReportOverviewModule({
                element: $el,
                data: {
                    date: "2020-05-02",
                    sentTotal: totalTestcases,
                    passed: stepPass.numberValue,
                    failed: stepFail.numberValue,
                    skipped: stepIgnored.numberValue
                }
            });
        })(jQuery);
    }

    (function umd(root, name, factory) {
        "use strict";
        if ("function" === typeof define && define.amd) {
            // AMD. Register as an anonymous module.
            define(name, ["jquery"], factory);
        } else {
            // Browser globals
            root[name] = factory();
        }
    })(this, "ReportOverviewModule", function UMDFactory() {
        "use strict";

        var ReportOverview = ReportOverviewConstructor;

        reportCircleGraph();

        return ReportOverview;

        function ReportOverviewConstructor(options) {
            var factory = {
                    init: init
                },
                _elements = {
                    $element: options.element
                };

            init();

            return factory;

            function init() {
                _elements.$element.append($(getTemplateString()));
                $(".delivery-rate").percentCircle({
                    width: 130,
                    trackColor: "#ececec",
                    barColor: "#62C662",
                    barWeight: 5,
                    endPercent: options.data.passed / options.data.sentTotal,
                    fps: 60
                });

                $(".open-rate").percentCircle({
                    width: 130,
                    trackColor: "#ececec",
                    barColor: "#FF3F3A",
                    barWeight: 5,
                    endPercent: options.data.failed / options.data.sentTotal,
                    fps: 60
                });

                $(".click-to-open").percentCircle({
                    width: 130,
                    trackColor: "#ececec",
                    barColor: "#80cdbe",
                    barWeight: 5,
                    endPercent: options.data.skipped / options.data.sentTotal,
                    fps: 60
                });
            }

            function getTemplateString() {
                return [
                    "<div>",
                    '<div class="row">',
                    '<div class="col-md-12">',
                    '<div class="report-statistic-box">',
                    '<div class="box-header">TOTAL TEST CASES</div>',
                    '<div class="box-content">',
                    '<div class="sentTotal">{{sentTotal}}</div>'.replace(
                        /{{sentTotal}}/,
                        options.data.sentTotal
                    ),
                    "</div>",
                    '<div class="box-foot">',
                    '<div class="sendTime box-foot-left"><span class="box-foot-stats">' + options.data.date + '</span><br><span class="box-foot-stats">' + options.data.date + '</span></div>',
                    '<div class="sendTime box-foot-right">TimeTaken<br><span class="box-foot-stats">(03hs)</span></div>',

                    "</div>",
                    "</div>",

                    '<div class="report-statistic-box">',
                    '<div class="box-header">PASSED</div>',
                    '<div class="box-content delivery-rate">',
                    '<div class="percentage">' + ((options.data.passed * 100) / options.data.sentTotal).toFixed(1) + '%</div>',
                    "</div>",
                    '<div class="box-foot">',
                    '<span class="arrow arrow-up"></span>',
                    '<div class="box-foot-left">Passed<br><span class="box-foot-stats"><strong>' + options.data.passed + '</strong> (' + ((options.data.passed * 100) / options.data.sentTotal).toFixed(1) + '%)</span></div>',
                    '<span class="arrow arrow-down"></span>',
                    '<div class="box-foot-right">Remaining<br><span class="box-foot-stats" title="% = unfailed emails / passed emails"><strong>6</strong> (30%)</span></div>',
                    "</div>",
                    "</div>",

                    '<div class="report-statistic-box">',
                    '<div class="box-header">FAILED</div>',
                    '<div class="box-content open-rate">',
                    '<div class="percentage">30%</div>',
                    "</div>",
                    '<div class="box-foot">',
                    '<span class="arrow arrow-up"></span>',
                    '<div class="box-foot-left">Failed<br><span class="box-foot-stats"><strong>' + options.data.failed + '</strong> (' + (options.data.failed * 100) / options.data.sentTotal + '%)</span></div>',
                    '<span class="arrow arrow-down"></span>',
                    '<div class="box-foot-right">Remaining<br><span class="box-foot-stats"><strong>0</strong> (0%)</span></div>',
                    "</div>",
                    "</div>",

                    '<div class="report-statistic-box">',
                    '<div class="box-header">SKIPPED</div>',
                    '<div class="box-content click-to-open">',
                    '<div class="percentage">0%</div>',
                    "</div>",
                    '<div class="box-foot">',
                    '<span class="arrow arrow-up"></span>',
                    '<div class="box-foot-left">Skipped<br><span class="box-foot-stats"><strong>' + options.data.skipped + '</strong> (' + (options.data.skipped * 100) / options.data.sentTotal + '%)</span></div>',
                    '<div class="box-foot-right">Remaining<br><span class="box-foot-stats"><strong>0</strong> (0%)</span></div>',
                    "</div>",
                    "</div>"
                ].join("");
            }
        }

        function reportCircleGraph() {
            $.fn.percentCircle = function pie(options) {
                var settings = $.extend(
                    {
                        width: 130,
                        trackColor: "#fff",
                        barColor: "#fff",
                        barWeight: 5,
                        startPercent: 0,
                        endPercent: 1,
                        fps: 60
                    },
                    options
                );

                this.css({
                    width: settings.width,
                    height: settings.width
                });

                var _this = this,
                    canvasWidth = settings.width,
                    canvasHeight = canvasWidth,
                    id = $("canvas").length,
                    canvasElement = $(
                        '<canvas id="' +
                        id +
                        '" width="' +
                        canvasWidth +
                        '" height="' +
                        canvasHeight +
                        '"></canvas>'
                    ),
                    canvas = canvasElement.get(0).getContext("2d"),
                    centerX = canvasWidth / 2,
                    centerY = canvasHeight / 2,
                    radius = settings.width / 2 - settings.barWeight / 2,
                    counterClockwise = false,
                    fps = 1000 / settings.fps,
                    update = 0.01;

                this.angle = settings.startPercent;

                this.drawInnerArc = function (startAngle, percentFilled, color) {
                    var drawingArc = true;
                    canvas.beginPath();
                    canvas.arc(
                        centerX,
                        centerY,
                        radius,
                        (Math.PI / 180) * (startAngle * 360 - 90),
                        (Math.PI / 180) * (percentFilled * 360 - 90),
                        counterClockwise
                    );
                    canvas.strokeStyle = color;
                    canvas.lineWidth = settings.barWeight - 2;
                    canvas.stroke();
                    drawingArc = false;
                };

                this.drawOuterArc = function (startAngle, percentFilled, color) {
                    var drawingArc = true;
                    canvas.beginPath();
                    canvas.arc(
                        centerX,
                        centerY,
                        radius,
                        (Math.PI / 180) * (startAngle * 360 - 90),
                        (Math.PI / 180) * (percentFilled * 360 - 90),
                        counterClockwise
                    );
                    canvas.strokeStyle = color;
                    canvas.lineWidth = settings.barWeight;
                    canvas.lineCap = "round";
                    canvas.stroke();
                    drawingArc = false;
                };

                this.fillChart = function (stop) {
                    var loop = setInterval(function () {
                        canvas.clearRect(0, 0, canvasWidth, canvasHeight);

                        _this.drawInnerArc(0, 360, settings.trackColor);
                        _this.drawOuterArc(
                            settings.startPercent,
                            _this.angle,
                            settings.barColor
                        );

                        _this.angle += update;

                        if (_this.angle > stop) {
                            clearInterval(loop);
                        }
                    }, fps);
                };

                this.fillChart(settings.endPercent);
                this.append(canvasElement);
                return this;
            };
        }
    });

</script>
</body>
</html>