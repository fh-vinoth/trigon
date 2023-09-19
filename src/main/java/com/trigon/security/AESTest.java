package com.trigon.security;

public class AESTest {
    public static void main(String[] args) {
        String originalString = "abcd";
        //String encryptedString = "";
//        String encryptedString = AES.encrypt(originalString, secretKey) ;
//        getListOfEncryptedKey("erefe");

        System.out.printf(AES.decrypt("ZywTVUUwg8ew","t2sautomation"));
    }



  /*  public static void getListOfEncryptedKey(String... list ){
        for (int i = 0;i<list.length;i++){
//            uncomment this during encryptoperation.
            String encryptedString = AES.encrypt(list[i], "") ;
            System.out.println(list[i]  +" : : : : "+"AES.decrypt(\""+encryptedString+"\", \"\")" );
        }
    }*/



}
