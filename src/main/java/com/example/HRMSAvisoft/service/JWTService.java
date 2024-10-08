package com.example.HRMSAvisoft.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.HRMSAvisoft.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
public class JWTService {

    //TODO: move key to eparate properties file (not in git)

    private static String JWT_KEY = "slkfjewofjowifwoi409u34jr43ut3oi4jtfoi3j";

    private static final long EXPIRATION_TIME_MILLIS = 86400000; // 1 day in milliseconds

    private static Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);

    public static String createJWT(Long userId, Set<Role> roles){
        List<Role> rolesList = new ArrayList<>(roles);
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME_MILLIS); // Set expiry time

//        List<String> roleNames = roles.stream()    // List<String> privilegeNames = roles.getPrivileges().stream().map(privilege -> return privilege).collect(Collectores.toList());
//                .map(Role::getRole).collect(Collectors.toList());


        List<String> privilegeList = rolesList.get(0).getPrivilege().stream().map(privilege -> privilege.toString()).collect(Collectors.toList());

//        System.out.println( rolesList.get(0).toString());
//        List<String> privilegeNames = rolesList.get(0).getPrivilege().stream().map(privilege->privilege.toString()).collect(Collectors.toList());

        List<String> privilegeNames = new ArrayList<>();
        if (!roles.isEmpty()) {
            privilegeNames = rolesList.get(0).getPrivilege().stream().map(privilege -> privilege.toString()).collect(Collectors.toList());
        }


        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("roles", privilegeList)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public static DecodedJWT retrieveJWT(String jwtString){

        return JWT.require(algorithm)
                .build()
                .verify(jwtString);
    }

    public static Long retrieveUserId(String jwtString){
        DecodedJWT jwt = retrieveJWT(jwtString);
        String userIdString = jwt.getSubject();
        return Long.parseLong(userIdString);
    }

    public static List<String> retrieveRoles(String jwtString) {
        DecodedJWT jwt = retrieveJWT(jwtString);
        Claim rolesClaim = jwt.getClaim("roles");
        return rolesClaim.asList(String.class);
    }

//    public static List<String> retrievePrivileges(String jwtString) {
//        DecodedJWT jwt = retrieveJWT(jwtString);
//        Claim privilegesClaim = jwt.getClaim("privileges");
//        return privilegesClaim.asList(String.class);
//    }
}
