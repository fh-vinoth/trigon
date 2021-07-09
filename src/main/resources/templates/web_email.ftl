<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FH Web Automation Summary Report</title>
</head>
<body style="font-family: 'Poppins', sans-serif;font-size: 12px;color:#e9e6e6">

<table style="width: 90%;  table-layout: auto;overflow-x:auto;  margin-left: auto; margin-right: auto;">
<#--    <tr style="text-align: center">-->
<#--        <td><img alt="FoodHub Logo" src="https://t2s-staging-automation.s3.amazonaws.com/Docs/logos/foodhub_logo.png">-->
<#--        </td>-->
<#--    </tr>-->
    <tr>
        <td>
            <div>
                <table style="width: 100%;border-radius: 10px;background: #314e4e;overflow: hidden;border-collapse: collapse;">
                    <tr>
                        <td colspan="5" style="height: 50px;text-align: center">
                            <div style="font-size: 15px">Web Test Suite :${testSuiteName}</div>
                        </td>
                    </tr>
                    <tr style="height: 40px;background: #3a646b;text-align: center">
                        <td colspan="5">TEST DETAILS</td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <table style="width: 95%;border-radius: 10px;background: #314e4e;overflow: hidden;border-collapse: collapse;table-layout: auto;overflow-x:auto;  margin-left: auto; margin-right: auto;">
                                <tr>
                                    <td style="height: 50px;border-radius: 10px;overflow: hidden;border-collapse: collapse;text-align: center">
                                        <div>TotalTime : ${timeTaken!} </div>
                                        <div>ExecutedBy : ${executedBy} </div>
                                        <div>TestType : ${testType} </div>
                                        <div>OS : ${executedSystemOS} </div>
                                    </td>
                                    <td style="height: 50px;border-radius: 10px;overflow: hidden;border-collapse: collapse;text-align: center">
                                        <div>FrameWorkVersion : ${frameworkVersion}</div>
                                        <div>TestStartTime:${testStartTime}</div>
                                        <div>TestEndTime:${testEndTime!}</div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr style="height: 40px;background: #3a646b;text-align: center">
                        <td>TOTAL TEST CASES</td>
                        <td>PASSED (${passed})</td>
                        <td>FAILED (${failed})</td>
                        <td>SKIPPED (${skipped})</td>
                        <td></td>
                    </tr>
                    <tr style="height: 130px;text-align: center">
                        <td>
                            <div style="font-size: 25px;padding: 2.45rem .20rem;">${totalTests}</div>
                        </td>
                        <td>
                            <img src="https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_White/P_${passPercentage}.png"
                                 alt="${passPercentage}%"></td>
                        <td>
                            <img src="https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_White/R_${failPercentage}.png"
                                 alt="${failPercentage}%"></td>
                        <td>
                            <img src="https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_White/B_${skipPercentage}.png"
                                 alt="${skipPercentage}%"></td>
                        <td>
                            <div style="font-size: 25px;padding: 2.45rem .20rem;"></div>
                        </td>
                    </tr>
                    <tr style="height: 40px;background: #3a646b;text-align: center">
                        <td colspan="5">VIEW DETAILED REPORTS</td>
                    </tr>
                    <tr style="height: 55px;text-align: center">
                        <td colspan="5">
                            <div style="padding-top: 10px">
<#--                                <a href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/Index.html"-->
<#--                                   style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Detailed-->
<#--                                    Report</a>-->
                                <a href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/EmailReport.html"
                                   style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Summary
                                    Report</a>
                                <a
                                        href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/RunTimeLogs/RunTimeExecutionLog.html"
                                        style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Detailed
                                    Logs</a>
                            </div>
<#--                            <div style="padding-top: 10px;padding-bottom: 10px">-->
<#--                                <#if testRailProjectID??>-->
<#--                                    <a href="https://touch2success.testrail.com/index.php?/projects/overview/${testRailProjectID!}"-->
<#--                                       style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">TestRail-->
<#--                                        Report</a>-->
<#--                                <#else>-->
<#--                                    <a href="https://touch2success.testrail.com/index.php?/projects/overview"-->
<#--                                       style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">TestRail-->
<#--                                        Report</a>-->
<#--                                </#if>-->
<#--                            </div>-->
                        </td>
                    </tr>
                </table>
            </div>

            <#if checkFailStatus = true>
                <div style="padding-top: 5px">
                    <table style="width: 100%;border-radius: 10px;background: #314e4e;overflow: hidden;border-collapse: collapse;">
                        <tr style="height: 40px;background: #d94949;text-align: center">
                            <td colspan="5">FAILED/SKIPPED TESTS SUMMARY</td>
                        </tr>
                        <tr style="height: 40px;background: #3a646b;text-align: center">
                            <td>TEST STATUS</td>
                            <td>TEST METHOD</td>
                            <td colspan="3">TEST ANALYSIS</td>
                        </tr>
                        <#list failSummary as fsummary>
                            <tr style="text-align: center">
                                <#if fsummary.testStatus == "FAILED">
                                    <td style="width: 8%;"><span
                                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px;background: #FF0000;margin-left: 30px;"></span>
                                    </td>
                                <#else>
                                    <td style="width: 8%;"><span
                                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px;background: #6849a0;margin-left: 30px;"></span>
                                    </td>
                                </#if>
                                <td style="word-break: break-all; width: 30%;">
                                    ${fsummary.testMethod}
                                </td>
                                <td colspan="3"
                                    style="word-break: break-word; width: 60%;padding-left: 15px;text-align: justify; text-justify: inter-word;">
                                    <#if fsummary.testAnalysis??>
                                        <#list fsummary.testAnalysis as tv>
                                            <div>${tv}</div>
                                        </#list>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </#if>

            <div style="padding-top: 5px">
                <table style="width: 100%;border-radius: 10px;background: #626b7c;overflow: hidden;border-collapse: collapse;">
                    <tr style="height: 40px;background: #3a646b;text-align: center">
                        <td colspan="5">ALL TEST SCENARIO'S</td>
                    </tr>
                    <#list moduleData as tmoduleData>
                        <tr>
                            <td colspan="5" style="background: #5f6961d9;text-align: center; text-justify: inter-word;">
                                <div style=" font-size: 15px;">Test
                                    Module :${tmoduleData.testModuleName}
                                </div>
                                <div style="padding-top: 10px">
                                    ${tmoduleData.testModuleStartTime}|
                                    ${tmoduleData.testModuleEndTime}|
                                    ${tmoduleData.testModuleDuration}
                                </div>
                                <#if tmoduleData.testApiURL??>
                                    <div>
                                        ${tmoduleData.testApiURL!}|
                                        ${tmoduleData.testApiHost!}|${tmoduleData.testApiCountry!}
                                        |${tmoduleData.testApiVersion!}|
                                        ${tmoduleData.testApiHost!}
                                    </div>
                                <#else>
                                </#if>
                                <div>
                                    ${tmoduleData.testExecutionType!}|
                                    ${tmoduleData.testSystemOS!}|
                                    ${tmoduleData.testSystemOSVersion!}|
                                    ${tmoduleData.testBrowser!}|
                                    ${tmoduleData.testBrowserVersion!}|
                                    ${tmoduleData.testWebUrl!}|
                                    ${tmoduleData.testWebBuildNumber!}
                                </div>
                            </td>
                        </tr>
                        <#list tmoduleData.classDataList as tclassData>
                            <#list tclassData.testMethodData as tMethodData>

                                <tr>
                                    <td colspan="5" style="padding-top: 10px">
                                        <table style="width: 95%;border-radius: 10px;background: #314e4e;overflow: hidden;border-collapse: collapse;table-layout: auto;overflow-x:auto;  margin-left: auto; margin-right: auto;">
                                            <tr style="padding-top: 15px;">
                                                <#if tMethodData.testMethodStatus == "FAILED">
                                                    <td style="width: 8%;"><span
                                                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px ;background: #FF0000;margin-left: 30px;"></span>
                                                    </td>
                                                <#elseif tMethodData.testMethodStatus == "SKIPPED">
                                                    <td style="width: 8%;"><span
                                                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px ;background: #6849a0;margin-left: 30px;"></span>
                                                    </td>
                                                <#else>
                                                    <td style="width: 8%;"><span
                                                                style="display: block;width: 50px;height: 50px;border-radius: 50%;border: 2px ;background: #17ce17;margin-left: 30px;"></span>
                                                    </td>
                                                </#if>

                                                <#if tMethodData.testMethodAnalysis??>
                                                    <td style="width: 40%;word-break: break-all;text-align: justify; text-justify: inter-word;">
                                                        <div style="font-weight: bold;font-size: 14px;">Method :
                                                            ${tMethodData.testMethodName}
                                                        </div>
                                                        <div style="color: #c8c1c1;">
                                                            <div>Scenario: ${tMethodData.testMethodScenario}</div>
                                                            <div>Author : ${tMethodData.testMethodAuthorName}</div>
                                                            <div>Duration: ${tMethodData.testMethodDuration}</div>
                                                            <div style="font-weight: bold;color: #e9e6e6">Module
                                                                Under Test:
                                                            </div>
                                                            <div>${tclassData.testClassName}</div>
                                                        </div>
                                                    </td>
                                                    <td style="width: 60%;word-break: break-all;text-align: justify; text-justify: inter-word;">
                                                        <div style="font-weight: bold">Test
                                                            Analysis/Comments/Failures
                                                        </div>
                                                        <div style="color: #c8c1c1;text-align: justify; text-justify: inter-word;">
                                                            <#list tMethodData.testMethodAnalysis as tv>
                                                                <div> ${tv}</div>
                                                            </#list>
                                                        </div>
                                                    </td>
                                                <#else>
                                                    <td style="width: 40%;word-break: break-all;text-align: justify; text-justify: inter-word;">
                                                        <div style="font-weight: bold;font-size: 14px;">Method :
                                                            ${tMethodData.testMethodName}
                                                        </div>
                                                        <div style="color: #c8c1c1;">
                                                            <div>Author : ${tMethodData.testMethodAuthorName}</div>
                                                            <div>Duration: ${tMethodData.testMethodDuration}</div>
                                                        </div>
                                                    </td>
                                                    <td style="width: 60%;word-break: break-all;text-align: justify; text-justify: inter-word;">
                                                        <div>Scenario: <span
                                                                    style="color: #c8c1c1;">${tMethodData.testMethodScenario}</span>
                                                        </div>
                                                        <div style="font-weight: bold;color: #e9e6e6">Module
                                                            Under Test: <span
                                                                    style="color: #c8c1c1;">${tclassData.testClassName}</span>
                                                        </div>
                                                    </td>
                                                </#if>
                                            </tr>

                                        </table>
                                    </td>
                                </tr>

                            </#list>
                        </#list>
                        <tr>
                            <td style="padding-top: 10px"></td>
                        </tr>
                    </#list>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="5"
            style="height: 50px; background: #595d5a;border-radius: 10px;overflow: hidden;border-collapse: collapse;text-align: center">
<#--            <footer>&copy; 2020 - FoodHub Test Automation Team</footer>-->
            <div>NOTE : Attached Detailed & Summary Report, Download and Open in your
                favourite browser for better view
            </div>
        </td>
    </tr>
</table>


</body>
</html>
