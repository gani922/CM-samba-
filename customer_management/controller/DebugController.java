//package com.pramaindia.customer_management.controller;
//
//import com.pramaindia.customer_management.dao.AdminLoginDAO;
//import com.pramaindia.customer_management.model.AdminLoginInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/debug")
//@RequiredArgsConstructor
//public class DebugController {
//
//    private final AdminLoginDAO adminLoginDAO;
//
//    @GetMapping("/check-user")
//    public String checkUser(@RequestParam String username) {
//        Optional<AdminLoginInfo> user = adminLoginDAO.findByUsername(username);
//
//        if (user.isPresent()) {
//            AdminLoginInfo admin = user.get();
//            return String.format(
//                    "User found: %s\n" +
//                            "Password Hash: %s\n" +
//                            "Hash Length: %d\n" +
//                            "Is Active: %s\n" +
//                            "Role: %s",
//                    admin.getUsername(),
//                    admin.getPasswordHash(),
//                    admin.getPasswordHash().length(),
//                    admin.getIsActive(),
//                    admin.getRole()
//            );
//        } else {
//            return "User not found: " + username;
//        }
//    }
//
//    @GetMapping("/verify-password")
//    public String verifyPassword(@RequestParam String username,
//                                 @RequestParam String password) {
//        Optional<AdminLoginInfo> user = adminLoginDAO.findByUsername(username);
//
//        if (user.isPresent()) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
//            String storedHash = user.get().getPasswordHash();
//            boolean matches = encoder.matches(password, storedHash);
//
//            return String.format(
//                    "Username: %s\n" +
//                            "Input Password: %s\n" +
//                            "Stored Hash: %s\n" +
//                            "Password Matches: %s\n" +
//                            "Hash Length: %d",
//                    username, password, storedHash, matches, storedHash.length()
//            );
//        } else {
//            return "User not found: " + username;
//        }
//    }
//
//    @GetMapping("/list-users")
//    public String listUsers() {
//        StringBuilder result = new StringBuilder("All Users:\n");
//        result.append("===========\n");
//
//        for (AdminLoginInfo user : adminLoginDAO.selectAll()) {
//            result.append(String.format(
//                    "ID: %s, Username: %s, Hash: %s... (length: %d), Role: %s, Active: %s\n",
//                    user.getAdminId(),
//                    user.getUsername(),
//                    user.getPasswordHash().substring(0, 30),
//                    user.getPasswordHash().length(),
//                    user.getRole(),
//                    user.getIsActive()
//            ));
//        }
//
//        return result.toString();
//    }
//}