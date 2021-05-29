<!DOCTYPE html>
<html>
<head>
    <style>
        #email tr:nth-child(even) {
            background-color: #3e4a4c;
        }
    </style>
</head>
<body style="font-family: 'Roboto', sans-serif;font-weight: 300;font-size: 17px;color: #e4ecec;">

<table align="center"
       style="left: 0;right: 0;top: 10px;margin: auto;width: 100%;border-radius: 10px;background: #5b6561;overflow: hidden;border-collapse: collapse;">
    <tbody>
    <tr style="height: 40px">
        <td><img alt="FoodHub Logo"
                 src="https://t2s-staging-automation.s3.amazonaws.com/Docs/logos/foodhub_white_logo.png">
        </td>
        <td align="center" style="color: #ffffff;font-size: 25px"><b>API Test Suite :${testSuiteName}</b>
            <div style="text-align:center;color: white;font-size: 14px">Total Time : ${timeTaken} &nbsp;
                ||&nbsp; Executed By : ${executedBy} &nbsp;
                ||&nbsp; Test Type : ${testType} &nbsp;
                ||&nbsp; OS : ${executedSystemOS} &nbsp;
                ||&nbsp; FrameWork Version : ${frameworkVersion} &nbsp;
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="4" style="background: #e6e8ea;height: 1px; padding: 1px"></td>
    </tr>
    </tbody>
</table>
<table align="center"
       style="left: 0;right: 0;top: 110px;margin: auto;width: 100%;border-radius: 10px; font-size:15px;color:#fefefe;background: #4b5461;overflow: hidden;border-collapse: collapse;">
    <tr align="center" style="height: 45px;font-weight: bold;background: #3e4a4c">
        <td>Test Duration</td>
        <td>TotalTest's</td>
        <td>Passed</td>
        <td>Failed</td>
        <td>Skipped</td>
        <td>Total Endpoint's</td>
        <td>Detailed Reports</td>
    </tr>
    <tr align="center" style="height: 40px">
        <td>StartTime : ${testStartTime} <br> EndTime : ${testEndTime}</td>
        <td>${totalTests}</td>
        <td>${passed}</td>
        <td>${failed}</td>
        <td>${skipped}</td>
        <td>${totalEndPoints}</td>
        <td>
            <div class="pb-sm-1" style="box-sizing: border-box;padding-bottom: .25rem!important;"><a
                        href="https://bhaskar.com" class="btn btn-primary btn-sm active" role="button"
                        aria-pressed="true"
                        style="box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #007bff;">Detailed
                    Report!!</a></div>
            <div class="pb-sm-1" style="box-sizing: border-box;padding-bottom: .25rem!important;"><a
                        href="https://bhaskar.com" class="btn btn-primary btn-sm active" role="button"
                        aria-pressed="true"
                        style="box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #007bff;">Summary
                    Report</a></div>
            <div class="pb-sm-1" style="box-sizing: border-box;padding-bottom: .25rem!important;"><a
                        href="https://bhaskar.com" class="btn btn-primary btn-sm active" role="button"
                        aria-pressed="true"
                        style="box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #007bff;">Detailed
                    Logger!</a></div>
        </td>
    </tr>
    <tr>
        <td colspan="8" style="background: #e6e8ea;height: 1px; padding: 1px"></td>
    </tr>
</table>

<#if checkFailStatus = true>
    <table align="center" id="failed_summary"
           style="font-size: 14px;left: 0;right: 0;top: 290px;margin: auto;width: 100%;border-radius: 10px;background: #4b5361;overflow: hidden;border-collapse: collapse;">
        <tbody>
        <tr>
            <td align="center" colspan="6" style="background: #ca0b0bd9;height: 30px;padding: 10px;"><b>FAILED/SKIPPED
                    Tests Summary</b></td>
        </tr>
        <tr style="height: 45px;font-weight: bold;background: #3e4a4c">
            <td>TestStatus</td>
            <td>Method</td>
            <td>Test Analysis</td>
        </tr>
        <#list failSummary as fsummary>
            <tr>
                <#if fsummary.testStatus == "FAILED">
                    <td style="padding: 10px;"><span
                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px solid #bdc2c1;background: #FF0000;border-color: #FF0000;"></span>
                    </td>
                <#else>
                    <td style="padding: 10px;"><span
                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px solid #6849a0;background: #6849a0;border-color: #6849a0;"></span>
                    </td>
                </#if>
                <td style="padding-top: 3px;padding: 10px;">
                    <b>${fsummary.testMethod}</b>
                </td>
                <td style="font-size: 14px">${fsummary.testAnalysis}
                </td>

            </tr>
        </#list>
        <tr>
            <td colspan="6" style="height: 8px;padding: 1px; background: white"></td>
        </tr>

        </tbody>
    </table>
</#if>

<table align="center" id="api_email_body"
       style="font-size: 14px;left: 0;right: 0;top: 290px;margin: auto;width: 100%;border-radius: 10px;background: #4b5361;overflow: hidden;border-collapse: collapse;">
    <tbody>
    <#list moduleData as tmoduleData>
        <tr>
            <td align="center" colspan="6" style="background: #5f6961d9;height: 55px;padding: 10px;"><b>Test
                    Module :${tmoduleData.testModuleName}</b>
                <div style="  color: #d0e3e0;font-weight: 300;"> ${tmoduleData.testModuleStartTime} &nbsp;|&nbsp;
                    ${tmoduleData.testModuleEndTime} &nbsp;|&nbsp; ${tmoduleData.testModuleDuration}
                    &nbsp;|&nbsp;
                    ${tmoduleData.testApiURL} &nbsp;|&nbsp;
                    ${tmoduleData.testApiHost} &nbsp;|&nbsp; ${tmoduleData.testApiCountry}
                    |&nbsp; ${tmoduleData.testApiVersion}
                </div>
            </td>
        </tr>
        <tr style="height: 55px;font-weight: bold">
            <td>TestStatus</td>
            <td>Method</td>
            <td>HttpMethod/Endpoint <br>Response Time</td>
            <td>StatusCode</td>
            <td>Response<br>Validation</td>
            <td>Test<br>Analysis</td>
        </tr>
        <#list tmoduleData.classDataList as tclassData>
            <#list tclassData.testMethodData as tMethodData>
                <#assign depCount = 0>
                <#list tMethodData.apiTableData as tapiTabledata> <#assign depCount++> </#list>
                <#list tMethodData.apiTableData as tapiTabledata>
                    <#if tapiTabledata?index == depCount-1>
                        <tr>
                            <#if tMethodData.testMethodStatus == "FAILED">
                                <td style="padding: 10px;"><span
                                            style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px solid #bdc2c1;background: #FF0000;border-color: #FF0000;"></span>
                                </td>
                            <#elseif tMethodData.testMethodStatus == "SKIPPED">
                                <td style="padding: 10px;"><span
                                            style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px solid #6849a0;background: #6849a0;border-color: #6849a0;"></span>
                                </td>
                            <#else>
                                <td style="padding: 10px;"><span
                                            style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px solid #bdc2c1;background: #17ce17;border-color: #17ce17;"></span>
                                </td>
                            </#if>

                            <td style="padding-top: 3px;padding: 10px;">
                                <b>${tMethodData.testMethodName}</b>
                                <div style="  color: #e0d5d5;font-size: 12px">Module Under
                                    Test: ${tclassData.testClassName}<br>
                                    Author : ${tMethodData.testMethodAuthorName} <br>
                                    Duration: ${tMethodData.testMethodDuration}<br>
                                    Scenario: ${tMethodData.testMethodScenario}
                                </div>
                            </td>
                            <td style="font-size: 14px"><b>${tapiTabledata.httpMethod}:${tapiTabledata.endPoint}</b>
                                <div style="  color: #e0d5d5;">Response: ${tapiTabledata.responseTime} <br> No Of
                                    Dependancy Tests: ${depCount-1}</div>
                            </td>
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
                                    ${tk} : ${tv} <br>
                                    </#list><br>
                                    <b>Actual
                                        Response:</b><br> <#list tapiTabledata.actualResponse as tk,tv>
                                        ${tk} : ${tv} <br>
                                    </#list></td>
                            <#else>
                                <td></td>
                            </#if>

                            <td>${tMethodData.testMethodAnalysis}</td>
                        </tr>
                    </#if>
                </#list>
            </#list>
        </#list>
        <tr>
            <td colspan="6" style="height: 1px;padding: 1px; background: white"></td>
        </tr>
    </#list>

    <tr>
        <td align="center" colspan="6" style="height: 65px;font-size: 14px; background: #595d5a">
            <footer>&copy; 2020 - FoodHub Automation Team</footer>
            <br> NOTE : Attached Detailed & Summary Report, Download and Open in your
            favourite browser for better view
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>
