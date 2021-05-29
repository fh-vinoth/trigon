package com.fh.unittests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.fh.unittests.frameworktest.timecalc.getRunDuration;

public class PerformanceCheck {
    public static void main(String[] args) {
        List<String> l = new ArrayList<>();
        long x = System.currentTimeMillis();
        LinkedHashMap<String, String> lm = new LinkedHashMap<>();

        for(int i =0;i<=200000;i++){
            l.add("{\"current_page\":1,\"data\":[{\"id\":1779873,\"host\":\"automation-nz1.t2scdn.com\",\"name\":\"Autocategory Sdnad\",\"pos\":0,\"pos_back\":0,\"pos_receipt\":0,\"background_color\":\"0000FF\",\"font_color\":\"ffffff\",\"shrink\":0,\"hidden\":\"0\",\"collection\":1,\"delivery\":1,\"mip_id\":0,\"monday\":1,\"tuesday\":1,\"wednesday\":1,\"thursday\":1,\"friday\":1,\"saturday\":1,\"sunday\":1,\"coupon_allowed\":1,\"collection_discount_allowed\":1,\"online_discount_allowed\":1,\"added\":\"2021-05-29 06:50:09\",\"exclude_free\":1,\"second_language_name\":\"\",\"hidden_fix\":\"NO\",\"printer\":0,\"section\":0,\"food_type_id\":0,\"is_print_label\":\"0\",\"updated_at\":\"2021-05-29 06:50:09\",\"food_type\":\"\",\"sections\":null,\"created_by\":null,\"is_vat_included\":\"1\",\"show_online\":1,\"is_schedule\":0,\"second_language_description\":null,\"region_tax_id\":null}],\"first_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d1\",\"from\":1,\"last_page\":25,\"last_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d25\",\"next_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d2\",\"path\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\",\"per_page\":\"1\",\"prev_page_url\":null,\"to\":1,\"total\":25}");
            lm.put(""+i+"","{\"current_page\":1,\"data\":[{\"id\":1779873,\"host\":\"automation-nz1.t2scdn.com\",\"name\":\"Autocategory Sdnad\",\"pos\":0,\"pos_back\":0,\"pos_receipt\":0,\"background_color\":\"0000FF\",\"font_color\":\"ffffff\",\"shrink\":0,\"hidden\":\"0\",\"collection\":1,\"delivery\":1,\"mip_id\":0,\"monday\":1,\"tuesday\":1,\"wednesday\":1,\"thursday\":1,\"friday\":1,\"saturday\":1,\"sunday\":1,\"coupon_allowed\":1,\"collection_discount_allowed\":1,\"online_discount_allowed\":1,\"added\":\"2021-05-29 06:50:09\",\"exclude_free\":1,\"second_language_name\":\"\",\"hidden_fix\":\"NO\",\"printer\":0,\"section\":0,\"food_type_id\":0,\"is_print_label\":\"0\",\"updated_at\":\"2021-05-29 06:50:09\",\"food_type\":\"\",\"sections\":null,\"created_by\":null,\"is_vat_included\":\"1\",\"show_online\":1,\"is_schedule\":0,\"second_language_description\":null,\"region_tax_id\":null}],\"first_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d1\",\"from\":1,\"last_page\":25,\"last_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d25\",\"next_page_url\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\u0026page\u003d2\",\"path\":\"https:\\/\\/sit-api.t2scdn.com\\/category?api_token\u003d097bb703604d87f33ae82d4a21a79818\u0026limit\u003d1\",\"per_page\":\"1\",\"prev_page_url\":null,\"to\":1,\"total\":25}");
        }

        System.out.println(getRunDuration(x, System.currentTimeMillis()));
        System.out.println(l.size());
        System.out.println(lm.size());
    }
}
