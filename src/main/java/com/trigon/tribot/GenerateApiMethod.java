package com.trigon.tribot;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.trigon.api.APICoreController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import java.util.*;


public class GenerateApiMethod {

    @Test
    @Parameters({"jsonFilePath"})
    public void generateAPIMethods(String path) {
        String methodName = null;
        String type = null;
        String curl = null;
        try {
            String method = null;
            JSONParser parser = new JSONParser();
            FileWriter fw = new FileWriter("src/test/resources/TestData/CreatedAPIMethods");
            Object obj = parser.parse(new FileReader(path));
            Map<String, Object> map = JsonFlattener.flattenAsMap(((JSONObject) obj).toJSONString());
            for (Map.Entry<String, Object> i : map.entrySet()) {
                String key = i.getKey();
                String value = i.getValue().toString();
                if (key.endsWith("].Curl")) {
                    String split = key.split("].Curl")[0];
                    curl = value;
                    type = map.get(split + "].API_Type").toString();
                    methodName = map.get(split + "].MethodName").toString();
                } else {
                    continue;
                }
                Map<String, Object> queryparam = queryParam(curl);
                Map<String, Object> headder = headder(curl);
                Map<String, Object> formparam = null;
                String appsyncbody = null;
                if (type.equalsIgnoreCase("legacy")) {
                    formparam = bodyValues(curl);
                } else {
                    appsyncbody = appSyncBody(curl);
                }
                String httpMethod = httpMethod(curl);
                String endpoint = endpoint(curl);
                method = buildMethod(headder, formparam, queryparam, httpMethod, methodName, endpoint, appsyncbody, type);
                fw.write("*******" + methodName + "**********\n\n" + method + "\n\n\n");
            }
            System.out.println("******* Created Method in the Below File Path *******\nsrc/test/resources/TestData/CreatedAPIMethods");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String appSyncBody(String curl) {
        String[] split = curl.split("--data-raw '");
        String s = split[1];
        String[] split1 = s.split("'");
        String body = split1[0];
        return body;
    }

    public Map<String, Object> headder(String curl) {
        Map<String, Object> headder = new HashMap<>();
        if (curl.contains("--header ")) {

            String[] value = new String[0];
            String[] splitbyheaer = curl.split("--header ");
            for (int i = 1; i < splitbyheaer.length; i++) {

                String[] header1 = splitbyheaer[i].split(":");

                if (header1[1].contains("--data-urlencode") || header1[1].contains("--form '")) {
                    String key = header1[0];
                    if (header1[1].contains("'")) {
                        value = header1[1].split("'");
                    }
                    if (key.contains("'")) {
                        key = key.replaceAll("'", "");
                    }
                    headder.put(key, value[0]);
                    break;
                }
                String key = header1[0];
                if (header1[1].contains("'")) {
                    value = header1[1].split("'");
                }
                if (key.contains("'")) {
                    key = key.replaceAll("'", "");
                }
                headder.put(key, value[0]);
            }
        }
        return headder;
    }

    public Map<String, Object> bodyValues(String curl) {
        Map<String, Object> formparam = new HashMap<>();

        String value = "";
        String key = "";
        String value1 = "";
        String[] body = null;
        if (curl.contains("--data-urlencode") || curl.contains("--form ")) {
            if (curl.contains("--data-urlencode")) {
                body = curl.split("--data-urlencode '");
            }
            if (curl.contains("--form ")) {
                body = curl.split("--form '");
            }
            String s = body[1];
            for (int i = 1; i < body.length; i++) {
                if (body[i].contains("=")) {
                    String[] param = body[i].split("=");
                    key = param[0];
                    value = param[1];
                    for (int j = 0; j < value.length(); j++) {
                        if (String.valueOf(value.charAt(j)).contains("'")) {
                            break;
                        }
                        value1 = value1 + String.valueOf(value.charAt(j));
                    }
                }
                formparam.put(key, value1);
                key = "";
                value1 = "";
            }
        }
        if (curl.contains("--data-raw ")) {
            String keyy = "";
            body = curl.split("--data-raw '");
            String kv = body[1];
            for (int i = 0; i < kv.length(); i++) {
                char k = kv.charAt(i);
                if (String.valueOf(k).contains("\"") || String.valueOf(k).contains("{") || String.valueOf(k).contains("}") || String.valueOf(k).contains("'")) {
                } else {
                    keyy = keyy + k;

                }
            }
            String[] keyAndValue = keyy.split(":");
            key = keyAndValue[0];
            value1 = keyAndValue[1];
            formparam.put(key, value1);
        }
        return formparam;
    }

    public Map<String, Object> queryParam(String curl) {
        String value = null;
        Map<String, Object> queryparam = new HashMap<>();
        String qpKey = null;
        String qpValue = null;
        String[] arr = curl.split("request ");
        value = arr[1];
        String[] s = value.split(" ");
        String k = s[1];
        String[] split = null;
        if (k.contains(".com")) {
            split = k.split("m/", 2);
        } else if (k.contains(".dev")) {
            split = k.split("v/", 2);

        }
        String endpoint0 = split[1];
        String[] endpointAndQueryparam = null;
        if (endpoint0.contains("?") || endpoint0.contains("&")) {
            endpointAndQueryparam = endpoint0.split("\\?");
            String firstQueryparam = endpointAndQueryparam[1];
            String[] keynvalue = firstQueryparam.split("&");
            String[] qparam = null;
            for (int i = 0; i < keynvalue.length; i++) {
                if (keynvalue[i].contains("%3D")) {
                    qparam = keynvalue[i].split("%3D");
                } else {
                    qparam = keynvalue[i].split("=");
                }
                qpKey = qparam[0];
                qpValue = qparam[1];
                qpKey = "\"" + qpKey + "\"";
                qpValue = "\"" + qpValue + "\"";
                if (qpValue.contains("'")) {
                    qpValue = qpValue.replaceAll("'", "");
                }
                if (qpKey.contains("'")) {
                    qpKey = qpKey.replaceAll("'", "");
                }
                queryparam.put(qpKey, qpValue);
            }
        }
        return queryparam;
    }

    public String endpoint(String curl) {
        String[] arr = curl.split("request ");
        String m = arr[1];
        String[] s = m.split(" ");
        String httpMethod = s[0];
        String k = s[1];
        String[] split = null;

        if (k.contains(".com")) {
            split = k.split("m/", 2);
        } else if (k.contains(".dev")) {
            split = k.split("v/", 2);
        }
        String url = split[0];
        String endpoint0 = split[1];
        String[] endpoint = endpoint0.split("\\?");
        String oendpoint = endpoint[0];
        if (oendpoint.contains("'")) {
            oendpoint = oendpoint.replaceAll("'", "");
        }
        return oendpoint;
    }

    public String httpMethod(String curl) {
        String[] arr = curl.split("request ");
        String m = arr[1];
        String[] s = m.split(" ");
        String httpMethod = s[0];
        return httpMethod;
    }

    public String buildMethod(Map<String, Object> header, Map<String, Object> form, Map<String, Object> query, String httpMethod, String methodName, String endPoint, String appsyncbody, String type) {

        String returnType = "Map<String, Object>";
        String method = "  public " + returnType + " " + methodName + "_" + httpMethod + "() {\n" +
                "        APIModels api = new APIModels();\n" +
                "        Map<String, Object> response = new HashMap<>();\n" +
                "  try {";
        /*System.out.println("*------------------------* METHOD STARTS*-------------------------*");
        System.out.println(method);*/
        List<String> queryparam = new ArrayList<>();
        for (String key : query.keySet()) {
            queryparam.add("api.queryParams.put(" + key + ", " + query.get(key) + ");");
        }
      /*  queryparam.stream().forEach(i -> {
            System.out.print(i + "\n");
        });*/
        List<String> headers = new ArrayList<>();
        for (String key : header.keySet()) {
            headers.add("api.headers.put(" + "\"" + key + "\"" + ", " + "\"" + header.get(key) + "\"" + ");");
        }
       /* headers.stream().forEach(i -> {
            System.out.print(i + "\n");
        });*/
        List<String> formparam = null;
        String requestBody = null;
        if (type.equalsIgnoreCase("legacy")) {
            formparam = new ArrayList<>();
            for (String key : form.keySet()) {
                if (form.get(key).toString().contains("\"")) {
                    formparam.add("api.formParams.put(" + "\"" + key + "\"" + ", " + form.get(key) + ");");
                } else {
                    formparam.add("api.formParams.put(" + "\"" + key + "\"" + ", " + "\"" + form.get(key) + "\"" + ");");
                }
            }
          /*  formparam.stream().forEach(i -> {
                System.out.print(i + "\n");
            });*/
        } else {
            requestBody = " String requestBody = \"\";";
            /*System.out.println(requestBody);*/
        }
        String validateStaticResponse = null;
        if (type.equalsIgnoreCase("legacy")) {
            validateStaticResponse =
                    "        String endPoint = " + "\"" + endPoint + "\";\n" +
                            "response = api.validateStaticResponse(" + "\"" + httpMethod + "\"" + ", " + "endPoint" + ", null, \"200\");\n" +
                            "        } catch (Exception e) {\n" +
                            "            hardFail(\"Failed to get " + methodName + "\"" + " + e);\n" +
                            "        }\n" +
                            "        return response;\n" +
                            "    }";
        } else {
            validateStaticResponse = "response = api.validateStaticResponse(" + "\"" + httpMethod + "\"" + ", " + "\"" + endPoint + "\"" + ",requestBody, \"200\");\n" +
                    "        } catch (Exception e) {\n" +
                    "            hardFail(\"Failed to get " + methodName + "\"" + " + e);\n" +
                    "        }\n" +
                    "        return response;\n" +
                    "    }";
        }
       /* System.out.println(validateStaticResponse);
        System.out.println("*------------------------* METHOD ENDS*-------------------------*");*/
        List ai = new ArrayList<>();
        ai.add(method + "\n");
        queryparam.stream().forEach(i -> {
            ai.add(i + "\n");
        });
        headers.stream().forEach(i -> {
            ai.add(i + "\n");
        });
        if (formparam != null) {
            if (formparam.size() > 0) {
                formparam.stream().forEach(i -> {
                    ai.add(i + "\n");
                });
            }
        } else {
            ai.add(requestBody);
        }
        ai.add(validateStaticResponse);
        final String[] k = {""};
        ai.stream().forEach(i -> {
            k[0] = k[0] + i;
        });
       /* System.out.println(k[0]);*/
        return k[0];
    }

}