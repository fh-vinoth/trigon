<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FH API Automation Failed Summary Report</title>
</head>
<body style="font-family: 'Poppins', sans-serif;font-size: 12px;color:#e9e6e6">

<table style="width: 90%;  table-layout: auto;overflow-x:auto;  margin-left: auto; margin-right: auto;">
    <tr style="text-align: center">
        <td><img alt="FoodHub Logo" src="https://t2s-staging-automation.s3.amazonaws.com/Docs/logos/foodhub_logo.png">
        </td>
    </tr>
    <tr>
        <td>
            <div>
                <table style="width: 100%;border-radius: 10px;background: #314e4e;overflow: hidden;border-collapse: collapse;">
                    <tr>
                        <td colspan="5" style="height: 50px;text-align: center">
                            <div style="font-size: 15px">API Test Suite :${testSuiteName}</div>
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
                                        <div>API Version : ${apiVersion}</div>
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
                        <td>ENDPOINTS COVERED</td>
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
                            <div style="font-size: 25px;padding: 2.45rem .20rem;">${totalEndPoints}</div>
                        </td>
                    </tr>
                    <tr style="height: 40px;background: #3a646b;text-align: center">
                        <td colspan="5">VIEW DETAILED REPORTS</td>
                    </tr>
                    <tr style="height: 55px;text-align: center">
                        <td colspan="5">
                            <div style="padding-top: 10px">
                                <a href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/APIDetailedReport.html"
                                   style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Detailed
                                    Report</a>
                                <a href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/APIEmailReport.html"
                                   style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Summary
                                    Report</a>
                            </div>
                            <div style="padding-top: 10px;padding-bottom: 10px"><a
                                        href="https://s3.amazonaws.com/t2s-staging-automation/TestResults/${testSuiteNameWithTime}/RunTimeLogs/RunTimeExecutionLog.html"
                                        style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">Detailed
                                    Logs</a>
                                <#if testRailProjectID??>
                                    <a href="https://touch2success.testrail.com/index.php?/projects/overview/${testRailProjectID!}"
                                       style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">TestRail
                                        Report</a>
                                <#else>
                                    <a href="https://touch2success.testrail.com/index.php?/projects/overview"
                                       style="width:32%;box-sizing: border-box;color: #fff;text-decoration: none;background-color: #007bff;cursor: pointer;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;border: 1px solid transparent;padding: .25rem .5rem;font-size: .875rem;line-height: 1.5;border-radius: .2rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;border-color: #689b3c;">TestRail
                                        Report</a>
                                </#if>
                            </div>
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
                                    <td style="width: 8%;"><div><img src="https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/fail.png" height="60" width="60" alt="fail"></div></td>
                                <#else>
                                    <td style="width: 8%;"><div><img src="https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/skip.png" height="60" width="60" alt="skip"></div></td>
                                </#if>
                                <td style="word-break: break-all; width: 30%;">
                                   <div>${fsummary.testMethod}</div>
                                    <div>${fsummary.testModule}</div>
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
        </td>
    </tr>
    <tr>
        <td colspan="5"
            style="height: 50px; background: #595d5a;border-radius: 10px;overflow: hidden;border-collapse: collapse;text-align: center">
            <footer>&copy; 2021 - FoodHub Test Automation Team</footer>
            <div>NOTE : Attached Detailed & Summary Report, Download and Open in your
                favourite browser for better view
            </div>
        </td>
    </tr>
</table>


</body>
</html>
