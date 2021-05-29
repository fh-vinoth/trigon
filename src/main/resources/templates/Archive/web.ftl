<html>
<!DOCTYPE html>

<head>
    <meta charset="UTF-8">
    <title>FHWeb EmailReport</title>
    <style>
        table {
            border-collapse: collapse;
            border: 1px solid #e9eaec;
            width: 100%;
            background-color: #c3ccce80;
            font-family: Roboto, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            color: #585858;
        }

        td,
        th {
            padding: 9px;
            border: 2px solid #e9eaec;
            font-family: Roboto, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            color: #585858;
        }

        td {
            font-size: 12px;
            text-align: center;
            background-color: white;
        }

        th {
            text-align: center;
            font-size: 12px;
            font-weight: normal;
        }

        .labels {
            background-color: #96a973cc;
            font-weight: bold;
            font-family: Roboto, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            color: #731d1d;
        }

        .headlabels {
            font-weight: bold;
            background-color: #e2e5e8cc;
            text-align: center;
            font-size: 12px;
            font-family: Roboto, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            color: #650f0f;
        }

        .testlabels {
            font-weight: bold;
            background-color: #467b77;
            text-align: center;
            font-size: 12px;
            font-family: Roboto, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            color: #dae6e5;
        }

        .label tr td label {
            display: block;
        }

    </style>
</head>
<table>
    <tr>
        <th class="labels" colspan="8"><h2> Test Summary Report : ${suiteName} </h2></th>
    </tr>
    <tr>
        <th class="labels" colspan="4">
            <ul style="list-style-type:none;">
                <li style="text-align: left;">Test Environment Details:</li>

                <li style="text-align: left;">OS = Mac OS X</li>
                <li style="text-align: left;">Selenium Version = 3.141.59</li>
                <li style="text-align: left;">TestNg Version = 6.14.2</li>
                <li style="text-align: left;">RestAssured Version = 3.3.0</li>
                <li style="text-align: left;">Appium Version = 7.0.0</li>
            </ul>
        </th>
        <th class="labels" colspan="4">
            <ul style="list-style-type:none;">
                <li style="text-align: left;">Test Name : FrameWorkUnitTestSuite</li>
                <li style="text-align: left;">URL = https://amazon.com/</li>
                <li style="text-align: left;">BrowserName = chrome</li>
                <li style="text-align: left;">BrowserVersion = 76.6</li>
                <li style="text-align: left;">ExecutedBy = bhaskarreddy</li>
            </ul>
        </th>
    </tr>
</table>
<br>
<table>
    <tr>
        <th class="labels">TestMethodName</th>
        <th class="labels">ScenarioName</th>
        <th class="labels">Author</th>
        <th class="labels">ExecutionTime</th>
        <th class="labels">TestStatus</th>
        <th class="labels" colspan="2">Comments</th>
    </tr>


    <#list test as tkey,tvalues>

        <tr>
            <th class="testlabels" colspan="8"><label>Test Module : ${tvalues.testName} &emsp; URL : ${tvalues.url}
                    &emsp; Browser : ${tvalues.browser}</label> &emsp; Test Group : ${tvalues.groups}</th>
        </tr>

        <#list tvalues.classResultJsonList as tclass>
            <tr>
                <th class="headlabels" colspan="8"><label> Module Under Test : ${tclass.className} </label></th>
            </tr>
            <#list tclass.methodResultJsonList as tmethod>
                <tr>
                    <td><b>${tmethod.methodName}</b></td>
                    <td>${tmethod.scenarioName}</td>
                    <td>${tmethod.author}</td>
                    <td>${tmethod.executionTime}</td>
                    <#if tmethod.testStatus == "FAILED">
                        <td><font color="red"><b>${tmethod.testStatus}</b></font></td>
                    <#else>
                        <td>${tmethod.testStatus}</td>
                    </#if>

                    <td colspan="2">
                        <#list tmethod.testComments as tcomments>
                            ${tcomments}</br>
                        </#list>
                    </td>
                </tr>

            </#list>
        </#list>
    </#list>
</table>

</html>