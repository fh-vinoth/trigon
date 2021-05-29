<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>FoodHub API Detailed Report</title>
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css' rel='stylesheet prefetch'>
    <link href="https://t2s-staging-automation.s3.amazonaws.com/Docs/api-css/percentage.css" rel="stylesheet">
    <link href="https://t2s-staging-automation.s3.amazonaws.com/Docs/api-css/apistyle.css" rel="stylesheet">
    <script src="https://t2s-staging-automation.s3.amazonaws.com/Docs/api-css/jsontree.js"></script>
</head>
<body>

<div class="dark" id="container">
    <div class="container-fluid">
        <header>
            <nav>
                <div class="container-fluid">
<#--                    <img style="position: absolute; top: 15px; left: 62px;" alt="FoodHub Logo"-->
<#--                         src="https://t2s-staging-automation.s3.amazonaws.com/Docs/logos/foodhub_white_logo.png">-->
                    <h3>API Test Suite : ${testSuiteName}</h3>
                    <p style="text-align:center;color: white">Total Time : ${timeTaken!} &nbsp;
                        ||&nbsp; Executed By : ${executedBy} &nbsp;
                        ||&nbsp; OS : ${executedSystemOS} &nbsp;
                        ||&nbsp; FrameWork Version : ${frameworkVersion} &nbsp;
                    </p>
                    <label class="switch">
                        <input checked id="swap-btn" type="checkbox">
                        <span class="slider round"></span>
                    </label>
                </div>
            </nav>
        </header>
        <div class="report-overview-module">
            <div class="row">
                <div class="col-md-12">
                    <div class="col-md-3">
                        <div class="report-statistic-box">
                            <div class="box-header">TOTAL TEST CASES</div>
                            <span>Test Start Time :  ${testStartTime}</span>
                            <div class="box-content">
                                <div class="sentTotal">${totalTests}</div>
                            </div>
                            <div>
                                <span>Test End Time :  ${testEndTime!}</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="report-statistic-box">
                            <div class="box-header">PASSED (${passed})</div>
                            <div class="c100 p${passPercentage} green">
                                <span>${passPercentage}%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="report-statistic-box">
                            <div class="box-header">FAILED (${failed})</div>
                            <div class="c100 p${failPercentage} red">
                                <span>${failPercentage}%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="report-statistic-box">
                            <div class="box-header">SKIPPED (${skipped})</div>
                            <div class="c100 p${skipPercentage} pink">
                                <span>${skipPercentage}%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <button class="btn btn-primary" id="collapse-init">
            Expand All
        </button>
        <div class="col-md-12 col-sm-12">
            <div class="panel-group wrap" id="bs-collapse">
                <#assign classcount = 0>
                <#assign counter = 0>
                <#list moduleData as tmoduleData>
                    <div class="panel-heading tests-module">
                        <h4 class="tests-title">
                            <div class="quiz-window-header">
                                <div class="quiz-window-method-title">Test Module: ${tmoduleData.testModuleName}</div>
                                <div class="testModuleSubBody"> ${tmoduleData.testModuleStartTime} &nbsp|&nbsp
                                    ${tmoduleData.testModuleEndTime} &nbsp|&nbsp ${tmoduleData.testModuleDuration}
                                    &nbsp|&nbsp
                                    ${tmoduleData.testApiURL} &nbsp|&nbsp
                                    <#if tmoduleData.testApiHost??>
                                        ${tmoduleData.testApiHost} &nbsp|
                                    </#if>
                                    <#if tmoduleData.testApiCountry??>
                                        &nbsp ${tmoduleData.testApiCountry}
                                    </#if>

                                    |&nbsp; ${tmoduleData.testApiVersion}
                                </div>
                            </div>
                        </h4>
                    </div>
                    <#list tmoduleData.classDataList as tclassData>
                        <#assign classcount++>
                        <div class="panel">
                            <div class="panel-heading test-case-pass">
                                <h4 class="panel-title">
                                    <a data-parent="#" data-toggle="collapse" href="#${classcount}-1">
                                        Module Under Test : ${tclassData.testClassName}
                                    </a>
                                </h4>
                            </div>
                            <div class="panel-collapse collapse" id="${classcount}-1">
                                <#list tclassData.testMethodData as tMethodData>
                                    <#assign counter++>

                                    <div class="quiz-window-header">
                                        <div class="quiz-window-method-title">Test
                                            Method: ${tMethodData.testMethodName}</div>
                                        <div class="testMethodSubBody"> ${tMethodData.testMethodStartTime} &nbsp|&nbsp
                                            ${tMethodData.testMethodEndTime}
                                            &nbsp|&nbsp ${tMethodData.testMethodDuration} &nbsp|&nbsp Authors
                                            : ${tMethodData.testMethodAuthorName}
                                        </div>
                                        <p>Scenario Description: ${tMethodData.testMethodScenario}</p>
                                    </div>
                                    <ul class="nav nav-tabs">
                                        <li class="active"><a data-toggle="tab"
                                                              href="#m_name_${classcount}-${counter}-1">${tMethodData.testMethodName}</a>
                                        </li>
                                        <#assign depCount = 0>
                                        <#list tMethodData.apiTableData as tapiTabledata> <#assign depCount++> </#list>
                                        <#if depCount-1 == 0>
                                        <#else>
                                            <#if tMethodData.testMethodStatus == "FAILED">
                                                <li><a class="failed2" data-toggle="tab"
                                                       href="#m_name_${classcount}-${counter}-2">dependency_Tests
                                                        (${depCount-1}
                                                        )</a></li>
                                            <#else>
                                                <li><a class="" data-toggle="tab"
                                                       href="#m_name_${classcount}-${counter}-2">dependency_Tests
                                                        (${depCount-1})</a></li>
                                            </#if>
                                        </#if>
                                    </ul>
                                    <#assign apiTableCount = 0>
                                    <#list tMethodData.apiTableData as tapiTabledata> <#assign apiTableCount++></#list>
                                    <div class="tab-content">
                                        <div id="m_name_${classcount}-${counter}-1" class="tab-pane fade in active">
                                            <#list tMethodData.apiTableData as tapiTabledata>
                                                <#if tapiTabledata?index == apiTableCount-1>
                                                    <div class="table-responsive quiz-window-body">
                                                        <table class="table table-borderless">
                                                            <thead>
                                                            <tr class="guiz-awards-row guiz-awards-header">
                                                                <th class="testStatusheaders">Test Status</th>
                                                                <th>HttpMethod/Endpoint <br>Response Time</th>
                                                                <th>RequestHeaders/<br>Cookies</th>
                                                                <th>QueryParams/<br>PathParams</th>
                                                                <th>FormParams/<br>RequestBody</th>
                                                                <th>StatusCode</th>
                                                                <th>Response<br>Validation</th>
                                                                <th>Test<br>Analysis</th>
                                                                <th>Response</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr class="guiz-awards-row guiz-awards-row-even">
                                                                <#if tapiTabledata.subTestStatus??>
                                                                    <#if tapiTabledata.subTestStatus == "FAILED">
                                                                        <td class="testStatusBody failed"><span
                                                                                    class="star failed1"></span></td>
                                                                    <#else>
                                                                        <td class="testStatusBody"><span
                                                                                    class="star passed"></span></td>
                                                                    </#if>
                                                                <#else>
                                                                    <td class="testStatusBody"><span
                                                                                class="star skipped"></span></td>
                                                                </#if>
                                                                <td>
                                                                    <b>${tapiTabledata.httpMethod}</b>:${tapiTabledata.endPoint}
                                                                    <br> <b>Response
                                                                        Time:</b> ${tapiTabledata.responseTime}</td>
                                                                <#if tapiTabledata.headers??>
                                                                    <td> <#list tapiTabledata.headers as tk,tv>
                                                                            <b>${tk}</b> : ${tv}<br>
                                                                        </#list> </td>
                                                                <#else>
                                                                    <td></td>
                                                                </#if>
                                                                <#if tapiTabledata.queryParams??>
                                                                    <td><#list tapiTabledata.queryParams as tk,tv>
                                                                            <b>${tk}</b> : ${tv}<br>
                                                                        </#list></td>
                                                                <#else>
                                                                    <td></td>
                                                                </#if>
                                                                <#if tapiTabledata.formParams??>
                                                                    <td><#list tapiTabledata.formParams as tk,tv>
                                                                            <b>${tk}</b> : ${tv}<br>
                                                                        </#list></td>
                                                                <#else>
                                                                    <td></td>
                                                                </#if>
                                                                <#if tapiTabledata.statusCode??>
                                                                    <td><#list tapiTabledata.statusCode as tk,tv>
                                                                            <b>${tk}</b> : ${tv} <br>
                                                                        </#list></td>
                                                                <#else>
                                                                    <td></td>
                                                                </#if>
                                                                <#if tapiTabledata.expectedResponse??>
                                                                    <td><b>Expected
                                                                            Response:</b><br> <#list tapiTabledata.expectedResponse as tk,tv>
                                                                        <b>${tk}</b> : ${tv} <br>
                                                                        </#list><br>
                                                                        <b>Actual
                                                                            Response:</b><br> <#list tapiTabledata.actualResponse as tk,tv>
                                                                            <b>${tk}</b> : ${tv} <br>
                                                                        </#list></td>
                                                                <#else>
                                                                    <td></td>
                                                                </#if>

                                                                <td>
                                                                    <#if tMethodData.testMethodAnalysis??>
                                                                        <#list tMethodData.testMethodAnalysis as tv>
                                                                            <div>${tv}</div>
                                                                        </#list>
                                                                    <#else>
                                                                    </#if>
                                                                </td>

                                                                <td>
                                                                    <a class="btn btn-link" data-toggle="collapse"
                                                                       href="#responseCollapse_${classcount}-${counter}-${apiTableCount}"
                                                                       role="button" aria-expanded="false"
                                                                       aria-controls="responseCollapse">
                                                                        View Response
                                                                        <button class="btn" data-toggle="tooltip"
                                                                                title="Copy to Clipboard"
                                                                                data-clipboard-target="#responseCopy_${classcount}-${counter}-${apiTableCount}">
                                                                            <img width="20em" height="20em"
                                                                                 src="https://t2s-staging-automation.s3.amazonaws.com/Docs/clipboard/clippy.svg"
                                                                                 alt="Copy"></button>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="9">
                                                                    <div class="collapse"
                                                                         id="responseCollapse_${classcount}-${counter}-${apiTableCount}">
                                                    <pre>
                                                        <#if tapiTabledata.responseJSON??>
                                                            <div class='json-tree'
                                                                 id='responseCopy_${classcount}-${counter}-${apiTableCount}'></div>
                                                            <script>function jsonTreeCreate1() {
                                                                    document.getElementById('responseCopy_${classcount}-${counter}-${apiTableCount}').innerHTML = JSONTree.create(${tapiTabledata.responseJSON});
                                                                }

                                                                jsonTreeCreate1();</script>
                                                            <#else>

                                                            <div> Invalid Response !! Fix request data and rerun Tests</div>
                                                        </#if>
                                                    </pre>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </#if>
                                            </#list>
                                        </div>
                                        <#if depCount-1 gt 0>
                                            <div id="m_name_${classcount}-${counter}-2" class="tab-pane fade">
                                                <div class="table-responsive quiz-window-body">
                                                    <table class="table table-borderless">
                                                        <thead>
                                                        <tr class="guiz-awards-row guiz-awards-header">
                                                            <th class="testStatusheaders">Test Status</th>
                                                            <th>HttpMethod/Endpoint <br>Response Time</th>
                                                            <th>RequestHeaders/<br>Cookies</th>
                                                            <th>QueryParams/<br>PathParams</th>
                                                            <th>FormParams/<br>RequestBody</th>
                                                            <th>StatusCode</th>
                                                            <th>Response<br>Validation</th>
                                                            <th>Response</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <#list tMethodData.apiTableData as tapiTabledata>
                                                            <#if tapiTabledata?index != apiTableCount-1>
                                                                <tr class="guiz-awards-row guiz-awards-row-even">
                                                                    <#if tapiTabledata.subTestStatus??>
                                                                    <#if tapiTabledata.subTestStatus == "FAILED">
                                                                        <td class="testStatusBody failed"><span
                                                                                    class="star failed1"></span></td>
                                                                    <#else>
                                                                        <td class="testStatusBody"><span
                                                                                    class="star passed"></span></td>
                                                                    </#if>
                                                                    <#else>
                                                                        <td class="testStatusBody"><span
                                                                                    class="star skipped"></span></td>
                                                                    </#if>
                                                                    <td>
                                                                        <b>${tapiTabledata.httpMethod}</b>:${tapiTabledata.endPoint}
                                                                        <br> <b>Response
                                                                            Time:</b> ${tapiTabledata.responseTime}</td>
                                                                    <#if tapiTabledata.headers??>
                                                                        <td> <#list tapiTabledata.headers as tk,tv>
                                                                                <b>${tk}</b> : ${tv}<br>
                                                                            </#list> </td>
                                                                    <#else>
                                                                        <td></td>
                                                                    </#if>
                                                                    <#if tapiTabledata.queryParams??>
                                                                        <td><#list tapiTabledata.queryParams as tk,tv>
                                                                                <b>${tk}</b> : ${tv}<br>
                                                                            </#list></td>
                                                                    <#else>
                                                                        <td></td>
                                                                    </#if>
                                                                    <#if tapiTabledata.formParams??>
                                                                        <td><#list tapiTabledata.formParams as tk,tv>
                                                                                <b>${tk}</b> : ${tv}<br>
                                                                            </#list></td>
                                                                    <#else>
                                                                        <td></td>
                                                                    </#if>
                                                                    <#if tapiTabledata.statusCode??>
                                                                        <td><#list tapiTabledata.statusCode as tk,tv>
                                                                                <b>${tk}</b> : ${tv} <br>
                                                                            </#list></td>
                                                                    <#else>
                                                                        <td></td>
                                                                    </#if>
                                                                    <#if tapiTabledata.expectedResponse??>
                                                                        <td><b>Expected
                                                                                Response:</b><br> <#list tapiTabledata.expectedResponse as tk,tv>
                                                                            <b>${tk}</b> : ${tv} <br>
                                                                            </#list><br>
                                                                            <b>Actual
                                                                                Response:</b><br> <#list tapiTabledata.actualResponse as tk,tv>
                                                                                <b>${tk}</b> : ${tv} <br>
                                                                            </#list></td>
                                                                    <#else>
                                                                        <td></td>
                                                                    </#if>
                                                                    <td>
                                                                        <a class="btn btn-link" data-toggle="collapse"
                                                                           href="#responseCollapse_${classcount}-${counter}-${tapiTabledata?index}"
                                                                           role="button" aria-expanded="false"
                                                                           aria-controls="responseCollapse">
                                                                            View Response
                                                                            <button class="btn" data-toggle="tooltip"
                                                                                    title="Copy to Clipboard"
                                                                                    data-clipboard-target="#responseCopy_${classcount}-${counter}-${tapiTabledata?index}">
                                                                                <img width="20em" height="20em"
                                                                                     src="https://t2s-staging-automation.s3.amazonaws.com/Docs/clipboard/clippy.svg"
                                                                                     alt="Copy"></button>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="9">
                                                                        <div class="collapse"
                                                                             id="responseCollapse_${classcount}-${counter}-${tapiTabledata?index}">

                                                    <pre>
                                                        <#if tapiTabledata.responseJSON??>
                                                            <div class='json-tree'
                                                                 id='responseCopy_${classcount}-${counter}-${tapiTabledata?index}'></div>
                                                            <script>function jsonTreeCreate1() {
                                                                    document.getElementById('responseCopy_${classcount}-${counter}-${tapiTabledata?index}').innerHTML = JSONTree.create(${tapiTabledata.responseJSON});
                                                                }

                                                                jsonTreeCreate1();</script>
                                                            <#else>

                                                            <div> Invalid Response !! Fix request data and rerun Tests</div>
                                                        </#if>
                                                    </pre>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </#if>
                                                        </#list>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </#if>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </#list>
                </#list>
            </div>
        </div>
    </div>
</div>

<#--<footer class="dark" id="footercontainer">-->
<#--    &copy; 2020 - FoodHub Automation Team-->
<#--</footer>-->

<script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js'></script>
<script src="https://t2s-staging-automation.s3.amazonaws.com/Docs/clipboard/clipboard.min.js"></script>

<script>
    let clipboard = new ClipboardJS('.btn');
    clipboard.on('success', function (e) {
        console.log(e);
    });

    clipboard.on('error', function (e) {
        console.log(e);
    });
</script>

<script type="text/javascript">
    const container = document.querySelector('#container');
    const footercontainer = document.querySelector('#footercontainer');

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
    $(function () {
        var active = true;
        $('#collapse-init').on('click', function () {
            if (active) {
                active = false;
                $('.panel-collapse').collapse('show');
                $('.panel-title').attr('data-toggle', '');
                $(this).text('Hide All');
            } else {
                active = true;
                $('.panel-collapse').collapse('hide');
                $('.panel-title').attr('data-toggle', 'collapse');
                $(this).text('Expand All');
            }
        });
    });
    // Event listener on button
    $('#swap-btn').on('click', () => {
        container.classList.toggle('dark')
    });
    $('#swap-btn').on('click', () => {
        footercontainer.classList.toggle('dark')
    });

</script>
</body>
</html>