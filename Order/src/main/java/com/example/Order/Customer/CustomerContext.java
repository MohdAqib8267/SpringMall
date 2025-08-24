package com.example.Order.Customer;

public class CustomerContext {
//    private static final ThreadLocal<String> JWT_TOKEN = new ThreadLocal<>();
    //ThreadLocal variables are differe from normal variable, as it creates a seperate copy of data for each thread and do not affect the copies held by other threads.
    // it is provides thread-safety without explicit synchronization mechanisms.


    private static String JWT_TOKEN;
    public static void setJwtToken(String jwt){
//        JWT_TOKEN.set(jwt);
        JWT_TOKEN=jwt;
    }
    public static String getJwtToken(){
//        return JWT_TOKEN.get();
        return JWT_TOKEN;
    }
    public static void clearJwt(String jwt){
//        JWT_TOKEN.remove();
        JWT_TOKEN=null;
    }

}
