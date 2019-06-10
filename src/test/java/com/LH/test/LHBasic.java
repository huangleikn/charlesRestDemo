package com.LH.test;


import io.restassured.RestAssured;
import io.restassured.http.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.Header;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Request;

import static org.hamcrest.core.IsEqual.equalTo;

/*
- Practice on GET some information from an endpoint
*/
public class LHBasic {

    private String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJnYnNlcjAxQHVzLmlibS5jb20iLCJhdF9oYXNoIjoiX01ENVYtQjVCbFJHWEhZOUtKNjQ2QSIsInJlYWxtTmFtZSI6IlczSURSZWFsbSIsImlzcyI6Imh0dHBzOi8vdzNpZC5hbHBoYS5zc28uaWJtLmNvbS9pc2FtIiwiYXVkIjoiTUdFek5qa3daR0V0TldZMll5MDAiLCJleHAiOjE1NTkzMTg2MTcsImlhdCI6MTU1OTMxMTQxNywiZW1haWxBZGRyZXNzIjoiZ2JzZXIwMUB1cy5pYm0uY29tIiwibGFzdE5hbWUiOiJKb3NlJTJGSUJNIiwiYmx1ZUdyb3VwcyI6WyJLVmlldyUyMEF1dGhvcnMtQkNTLVdXIiwidzNrbS1SRVMtdXNlcnMiLCJpYm1sZWFybmluZyIsIklUU0FTJTIwR2VuZXJhbCUyMEFjY2VzcyUyMDIiLCJ1c2VyJTIwLSUyMHBlcmYiLCJVUyUyMEVtcCIsIklEQ19Ccm5vX0xhYl91c2VycyIsImNvbS5pYm0udGFwLm1sZGIuYWNjZXNzLmludGVybmFsLmF1dG8udXMiLCJjb20uaWJtLnRhcC5tbGRiLmFjY2Vzcy5pbnRlcm5hbCIsIkxJUyUyME5vblJlZ3VsYXIlMjBVUyIsImxlZ2FsaWJtIiwiY29nbm9zLnByb2QuaHIud3BhLmNvbS51cyIsIkxpZ2h0aG91c2UlMjBEaWdpdGFsJTIwQXNzaXN0YW50JTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtIiwiTkFfZ3JwIiwiTUQlMjB0ZXN0JTIwZ3JvdXAxIiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3lzdGVtJTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtJTIwdGVzdCUyMGVudmlyb25tZW50IiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3BhY2UlMjAtJTIwUHJhY3RpY2UlMjBBZG1pbnMlMjBUZXN0IiwiRGF2aWRVcGRhdGVVc2VyR3JvdXAzMDE1IiwiS1ZpZXclMjBHUFAlMjBCbHVlVHViZSUyMEFkbWluaXN0cmF0b3JzJTIwLSUyMERldiIsIktWaWV3JTIwR1BQJTIwQmx1ZVR1YmUlMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBUZXN0IiwiS1ZpZXclMjBHUFAlMjBMZWFybmluZyUyMFRhYiUyMC0lMjBUZXN0IiwiTGlnaHRob3VzZSUyMEJveCUyMENNUyUyMC0lMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBEZXYiLCIyMDE5MDUyOF9keW5hbWljR3JvdXBUZXN0Il0sImNuIjoiR0JTZXIwMSUyRlNhbiUyMEpvc2UlMkZJQk0iLCJkbiI6InVpZD1DLUs4ODA4OTcsYz11cyxvdT1ibHVlcGFnZXMsbz1pYm0uY29tIiwidWlkIjoiQy1LODgwODk3IiwiZmlyc3ROYW1lIjoiR0JTZXIwMSUyRlNhbiJ9.C0DpNRP0TygfZQRsZO8LhI_OsIoc6hMpLMeiFyQx0jLq-7A-2Fd_U6k0Qulg2Bdrf3i5brOfLk4StwdjBsHrIHBttWj4GPb1YwPPvc2t1Hq7_jfW-3y22Y-P8sbVLzV74jcxGR9G4D61-ZcCAvhk7X2dj58pKKH1dyRweqK6_0Kw6bhjm7YLWwxKrpKUWLsysT1NnE-_B8QyF_MNcZaFrNP7r5vkIIo5Whfo8fbVvYvnx2pTaL28ZQn5dMep52_4v1DQGenFXKb_ejgb2gfd3CXxm8g2Wd1mzf7mGCCqhp-zsp73A9IBFjCPfKffmPQp9YleslPMi0L4thQtr10VQARwkfad8IdRgNNEfS6uQUWTYI7pp2xFgFhyvIj5aOEZJNkdNNt-u7ieA4KQHrEkagc9N7ARVq8xMbe4VA2iKpN9XCDt-SziQTIkvquY118FJPF8f6i15jSePAn4X7d2XXmXgOV_zZMJ7ystvRuYjRmrsxE4PbKMnLiPM_6SlnAYiPBXHbECnVcCKAcQsxB9pZWg72yWh1qLUI-g_qPpyy_PfLWyoMsyfRjprHJGmtss24ByWsIiO36EEGlhPcmPlZv033rjjsqsEsjcvpA5UD_k8yazUpkWxgAIEDbyAo74Dzxx7dTNKC19KZGpz3ps_UeMs41goKw1kmUb2GxCxCs";

    @Test
    public void GetSeriesWithJWTToken(){

        // use RestAssured to make an HTML Call
        //String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJnYnNlcjAxQHVzLmlibS5jb20iLCJhdF9oYXNoIjoib0dxWDBMZHA3WXFmMFUtYUlwdlVJQSIsInJlYWxtTmFtZSI6IlczSURSZWFsbSIsImlzcyI6Imh0dHBzOi8vdzNpZC5hbHBoYS5zc28uaWJtLmNvbS9pc2FtIiwiYXVkIjoiTkRRNE56RTNZamd0TnpBNU9TMDAiLCJleHAiOjE1NTkyMDU3ODUsImlhdCI6MTU1OTE5ODU4NSwiZW1haWxBZGRyZXNzIjoiZ2JzZXIwMUB1cy5pYm0uY29tIiwibGFzdE5hbWUiOiJKb3NlJTJGSUJNIiwiYmx1ZUdyb3VwcyI6WyJLVmlldyUyMEF1dGhvcnMtQkNTLVdXIiwidzNrbS1SRVMtdXNlcnMiLCJpYm1sZWFybmluZyIsIklUU0FTJTIwR2VuZXJhbCUyMEFjY2VzcyUyMDIiLCJ1c2VyJTIwLSUyMHBlcmYiLCJVUyUyMEVtcCIsIklEQ19Ccm5vX0xhYl91c2VycyIsImNvbS5pYm0udGFwLm1sZGIuYWNjZXNzLmludGVybmFsLmF1dG8udXMiLCJjb20uaWJtLnRhcC5tbGRiLmFjY2Vzcy5pbnRlcm5hbCIsIkxJUyUyME5vblJlZ3VsYXIlMjBVUyIsImxlZ2FsaWJtIiwiY29nbm9zLnByb2QuaHIud3BhLmNvbS51cyIsIkxpZ2h0aG91c2UlMjBEaWdpdGFsJTIwQXNzaXN0YW50JTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtIiwiTkFfZ3JwIiwiTUQlMjB0ZXN0JTIwZ3JvdXAxIiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3lzdGVtJTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtJTIwdGVzdCUyMGVudmlyb25tZW50IiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3BhY2UlMjAtJTIwUHJhY3RpY2UlMjBBZG1pbnMlMjBUZXN0IiwiRGF2aWRVcGRhdGVVc2VyR3JvdXAzMDE1IiwiS1ZpZXclMjBHUFAlMjBCbHVlVHViZSUyMEFkbWluaXN0cmF0b3JzJTIwLSUyMERldiIsIktWaWV3JTIwR1BQJTIwQmx1ZVR1YmUlMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBUZXN0IiwiS1ZpZXclMjBHUFAlMjBMZWFybmluZyUyMFRhYiUyMC0lMjBUZXN0IiwiTGlnaHRob3VzZSUyMEJveCUyMENNUyUyMC0lMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBEZXYiLCIyMDE5MDUyOF9keW5hbWljR3JvdXBUZXN0Il0sImNuIjoiR0JTZXIwMSUyRlNhbiUyMEpvc2UlMkZJQk0iLCJkbiI6InVpZD1DLUs4ODA4OTcsYz11cyxvdT1ibHVlcGFnZXMsbz1pYm0uY29tIiwidWlkIjoiQy1LODgwODk3IiwiZmlyc3ROYW1lIjoiR0JTZXIwMSUyRlNhbiJ9.ZGaDPpjJFtWwlz6ybq_flhRyiEazbbUDQ1rhu6z47ObEda6hRi0F379CJ9r3qv-qwhPkLsQf1ndGYTazEK79ur6oeAVrmvIt3iGAbmKAoAvRQ7PujbvUws7wdo5F5lDtxfQkiksGVJ6QE99y8PAG8HPLfPyDyckc23a9NWGWctG0GbTvExGvo89h-wC5AXzIsvyk98q5CHJ5DrbMw0selWQFu4QTC4m6xX_vfsTSGFQeGKQ6FK9lE7KqDf7I-QMV46G1e4WTE1LMruU2FTwS2LHZR_bt5aCLLoU5gl8soehZZctpnQzrAk-0axOA7PDYIimyYkJPA9rcdRG_ZDNijEt13zTF41dc5XCkNYLZPhWuT76OabFzqbc3o592R-bUqsEbQ6WoN4XYDDebF9ava_JUrXz8ysgCEpw3afLKQ87gGhrVcHKnLEfFx5xaUFkKcS02GBcDGKI4j3jFoFZYS0xDfvKOroKQabuyFFLRkD7kh2COuLSED5XlsqYWmsLuMLBOs-3esMbvWSqonC7D86kRTI6r6ZsxRoIRPVbrbsW9mseLBSWpgUtM70RMFXxxwLcnh7nodHwHjMcD36iBGwdI6r03A3eu6jUHG-YcRO8h9oKp_hLmkoIdKY65NcRmQe9jO171YLaYY5BvExopoVltWL34Y-KNqf_Q_ctb1FQ";
//        RequestSpecification requestSpect = RestAssured.with();
//        requestSpect.given().contentType("application/json");

        //requestSpect.headers("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJnYnNlcjAxQHVzLmlibS5jb20iLCJhdF9oYXNoIjoibXpXdjRzdWREakRGdG1nYkdHNldTUSIsInJlYWxtTmFtZSI6IlczSURSZWFsbSIsImlzcyI6Imh0dHBzOi8vdzNpZC5hbHBoYS5zc28uaWJtLmNvbS9pc2FtIiwiYXVkIjoiTkRRNE56RTNZamd0TnpBNU9TMDAiLCJleHAiOjE1NTkxODc1OTIsImlhdCI6MTU1OTE4MDM5MiwiZW1haWxBZGRyZXNzIjoiZ2JzZXIwMUB1cy5pYm0uY29tIiwibGFzdE5hbWUiOiJKb3NlJTJGSUJNIiwiYmx1ZUdyb3VwcyI6WyJLVmlldyUyMEF1dGhvcnMtQkNTLVdXIiwidzNrbS1SRVMtdXNlcnMiLCJpYm1sZWFybmluZyIsIklUU0FTJTIwR2VuZXJhbCUyMEFjY2VzcyUyMDIiLCJ1c2VyJTIwLSUyMHBlcmYiLCJVUyUyMEVtcCIsIklEQ19Ccm5vX0xhYl91c2VycyIsImNvbS5pYm0udGFwLm1sZGIuYWNjZXNzLmludGVybmFsLmF1dG8udXMiLCJjb20uaWJtLnRhcC5tbGRiLmFjY2Vzcy5pbnRlcm5hbCIsIkxJUyUyME5vblJlZ3VsYXIlMjBVUyIsImxlZ2FsaWJtIiwiY29nbm9zLnByb2QuaHIud3BhLmNvbS51cyIsIkxpZ2h0aG91c2UlMjBEaWdpdGFsJTIwQXNzaXN0YW50JTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtIiwiTkFfZ3JwIiwiTUQlMjB0ZXN0JTIwZ3JvdXAxIiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3lzdGVtJTIwQWRtaW5pc3RyYXRpb24lMjB0ZWFtJTIwdGVzdCUyMGVudmlyb25tZW50IiwiTGlnaHRob3VzZSUyMGluJTIwdGhlJTIwQ2xvdWQlMjAtJTIwU3BhY2UlMjAtJTIwUHJhY3RpY2UlMjBBZG1pbnMlMjBUZXN0IiwiRGF2aWRVcGRhdGVVc2VyR3JvdXAzMDE1IiwiS1ZpZXclMjBHUFAlMjBCbHVlVHViZSUyMEFkbWluaXN0cmF0b3JzJTIwLSUyMERldiIsIktWaWV3JTIwR1BQJTIwQmx1ZVR1YmUlMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBUZXN0IiwiS1ZpZXclMjBHUFAlMjBMZWFybmluZyUyMFRhYiUyMC0lMjBUZXN0IiwiTGlnaHRob3VzZSUyMEJveCUyMENNUyUyMC0lMjBBZG1pbmlzdHJhdG9ycyUyMC0lMjBEZXYiLCIyMDE5MDUyOF9keW5hbWljR3JvdXBUZXN0Il0sImNuIjoiR0JTZXIwMSUyRlNhbiUyMEpvc2UlMkZJQk0iLCJkbiI6InVpZD1DLUs4ODA4OTcsYz11cyxvdT1ibHVlcGFnZXMsbz1pYm0uY29tIiwidWlkIjoiQy1LODgwODk3IiwiZmlyc3ROYW1lIjoiR0JTZXIwMSUyRlNhbiJ9.jUKcS0bLA9XjjcsKiP7pLFun5pwJ1NybAjeQVX8Nup37KrQFmk4CAgjhoZ1dDzqNCcQYsRCzXs2Ae8kN80haAQvhs6gHITumsXVyEUpTCQq24mMsI_5XJr77DbSbww62jqPzfN3OTZsBGPCRBxXxgyklGSqmg7OFuJd_IcxgenDqZirJA5K743lS3LcF-nWtscgs_NnjkwtvmGfQ_kJUC4EQdEm7wLxzo4V6wAnJ0AU2bF7uLpRuMHdyzA404D-Ye20NNE7fCS_V29ll3uqEh-1-B9S_ykUP_vMd8_b-D5RI6ur1-COI2Bhjn-nerQ9rBjgZG0uofcXuTdImzU8_avzLUTyiHw_-vK4C4OBOkoLygjVexPm-CpjOg4TBp7X4z3hCFeNS-CbbfhpU2N0Yam0HJMSwwUcrGNkrgUHLMpFvgnxZZZD-tjU3Q1DT5SvIXV70rIvGv238Eb2xoVa4ZtJmv2il5rP08cNr5-V2zq6IvIXD4-NXzy4yaufk33r8ZbLX2fAd5EupXeMB0WtzONPIG49wtR7h5RdGJhf9rGgRbSFw4ERzyz0skSCaq0efoPXRt0ZOAeYGF4yw0OfFOyCnvDtim2BCKSj69c1e6XtDZW-PppRgYe_nYtdj6HyWGcm6D8XcrBUSTYAR_7Kfd7ebvmSgfcv96JFRRcXCVxo");

        //Response Request = RestAssured.given().header(new header("","" + token));
                RestAssured.baseURI = "https://w3-lighthouse-test-nonprod.intranet.ibm.com/services/lighthouse-nextgen/api/v1/content";
                RestAssured
                .given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .header("Authorization", "Bearer "+ token)
                .when()
                .get("/series")
                .then()
                .assertThat()
                .statusCode(200);

//        String json = response.getBody().asString();
//        System.out.println(json);

    }

    private static String body = "{\n" +
            "  \"name\": \"CharlesImage-20190516162711\",\n" +
            "  \"imageType\": \"image/jpeg\",\n" +
            "  \"storageId\": \"0567ee3e-3d24-4a8b-8671-95481f6dea41\",\n" +
            "  \"source\": \"library\",\n" +
            "   \"categories\": [\n" +
            "    {\n" +
            "      \"rootSchemaLogicKey\": \"topics\",\n" +
            "      \"schemaLogicKey\": \"TP088\"\n" +
            "    }\n" +
            "  ]\n" +
            " }";

    @Test
    public void postImageJWTToken() {
                RestAssured.baseURI = "https://w3-lighthouse-test-nonprod.intranet.ibm.com/services/lighthouse-nextgen/api/v1/content";
                RestAssured
                .given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .body(body)
                        .header("Authorization", "Bearer "+ token)
                .when()
                .post("/images")
                .then()
                .assertThat()
                .statusCode(201);
    }




}
