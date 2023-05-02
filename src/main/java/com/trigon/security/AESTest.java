package com.trigon.security;

public class AESTest {
    public static void main(String[] args) {

        String originalString = "abcd";
        //String encryptedString = "";
//        String encryptedString = AES.encrypt(originalString, secretKey) ;
        getListOfEncryptedKey("abcde","dbjkhggkjhb");
    }

    public static void getListOfEncryptedKey(String... list ){
        for (int i = 0;i<list.length;i++){
            //uncomment this during encryptoperation.
//            String encryptedString = AES.encrypt(list[i], "t2sautomation") ;
//            System.out.println(list[i]  +" : : : : "+encryptedString );
        }
    }


}
